package teamroots.embers.tileentity;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.EventManager;
import teamroots.embers.RegistryManager;
import teamroots.embers.SoundManager;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.api.tile.IMechanicallyPowered;
import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.api.upgrades.UpgradeUtil;
import teamroots.embers.block.BlockStamper;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageStamperFX;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.recipe.ItemStampingRecipe;
import teamroots.embers.recipe.RecipeRegistry;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class TileEntityStamper extends TileEntity implements ITileEntityBase, ITickable, IMechanicallyPowered {
    public static final double EMBER_COST = 80.0;
    public static final int STAMP_TIME = 70;
    public static final int RETRACT_TIME = 10;

    public IEmberCapability capability = new DefaultEmberCapability();
    public boolean prevPowered = false;
    public boolean powered = false;
    public long ticksExisted = 0;
    Random random = new Random();
    public ItemStackHandler stamp = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            TileEntityStamper.this.markDirty();
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };

    public TileEntityStamper() {
        super();
        capability.setEmberCapacity(8000);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setBoolean("powered", powered);
        capability.writeToNBT(tag);
        tag.setTag("stamp", stamp.serializeNBT());
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        powered = tag.getBoolean("powered");
        capability.readFromNBT(tag);
        stamp.deserializeNBT(tag.getCompoundTag("stamp"));
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                            EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (!heldItem.isEmpty()) {
            if (stamp.getStackInSlot(0).isEmpty()) {
                player.setHeldItem(hand, this.stamp.insertItem(0, heldItem.copy(), false));
                markDirty();
                return true;
            }
        } else {
            if (!stamp.getStackInSlot(0).isEmpty() && !world.isRemote) {
                world.spawnEntity(new EntityItem(world, player.posX, player.posY, player.posZ, stamp.getStackInSlot(0)));
                stamp.setStackInSlot(0, ItemStack.EMPTY);
                markDirty();
                return true;
            }
        }
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        this.invalidate();
        world.setTileEntity(pos, null);
        Misc.spawnInventoryInWorld(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stamp);
    }

    @Override
    public void update() {
        this.ticksExisted++;
        prevPowered = powered;
        EnumFacing face = getWorld().getBlockState(getPos()).getValue(BlockStamper.facing);
        if (getWorld().getBlockState(getPos().offset(face, 2)).getBlock() == RegistryManager.stamp_base) {
            List<IUpgradeProvider> upgrades = UpgradeUtil.getUpgrades(world, pos, EnumFacing.HORIZONTALS);
            UpgradeUtil.verifyUpgrades(this, upgrades);
            if (UpgradeUtil.doTick(this, upgrades))
                return;

            TileEntityStampBase stamp = (TileEntityStampBase) getWorld().getTileEntity(getPos().offset(face, 2));
            FluidStack fluid = null;
            IFluidHandler handler = stamp.getTank();
            if (handler != null)
                fluid = handler.drain(stamp.getCapacity(), false);
            ItemStampingRecipe recipe = getRecipe(stamp.inputs.getStackInSlot(0), fluid, this.stamp.getStackInSlot(0));
            if (!getWorld().isRemote && (recipe != null || powered)) {
                boolean cancel = UpgradeUtil.doWork(this, upgrades);
                int stampTime = UpgradeUtil.getWorkTime(this, STAMP_TIME, upgrades);
                int retractTime = UpgradeUtil.getWorkTime(this, RETRACT_TIME, upgrades);
                if (!cancel && !powered && this.ticksExisted >= stampTime && recipe != null) {
                    double emberCost = UpgradeUtil.getTotalEmberConsumption(this, EMBER_COST, upgrades);
                    if (this.capability.getEmber() >= emberCost) {
                        this.capability.removeAmount(emberCost, true);
                        if (!world.isRemote) {
                            PacketHandler.INSTANCE.sendToAll(new MessageStamperFX(getPos().offset(face, 2).getX() + 0.5f, getPos().offset(face, 2).getY() + 1.0f, getPos().offset(face, 2).getZ() + 0.5f));
                        }

                        world.playSound(null, getPos().getX() + 0.5, getPos().getY() - 0.5, getPos().getZ() + 0.5, SoundManager.STAMPER_DOWN, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        powered = true;
                        ticksExisted = 0;

                        List<ItemStack> results = Lists.newArrayList(recipe.getResult(this, stamp.inputs.getStackInSlot(0), stamp.getFluid() != null ? new FluidStack(stamp.getFluid(), stamp.getAmount()) : null, this.stamp.getStackInSlot(0)));
                        UpgradeUtil.transformOutput(this, results, upgrades);
                        stamp.inputs.extractItem(0, recipe.getInputConsumed(), false);
                        if (recipe.getFluid() != null) {
                            stamp.getTank().drain(recipe.getFluid(), true);
                        }
                        BlockPos middlePos = getPos().offset(face, 1);
                        BlockPos outputPos = getPos().offset(face, 3);
                        TileEntity outputTile = getWorld().getTileEntity(outputPos);
                        for (ItemStack remainder : results) {
                            if (outputTile instanceof TileEntityBin) {
                                remainder = ((TileEntityBin) outputTile).inventory.insertItem(0, remainder, false);
                            }
                            if (!remainder.isEmpty() && !getWorld().isRemote) {
                                getWorld().spawnEntity(new EntityItem(getWorld(), middlePos.getX() + 0.5, middlePos.getY() + 0.5, middlePos.getZ() + 0.5, remainder));
                            }
                        }
                        stamp.markDirty();
                    }

                    markDirty();
                } else if (!cancel && powered && this.ticksExisted >= retractTime) {
                    retract();
                }
            }
        } else if (powered) {
            retract();
        }
    }

    private void retract() {
        world.playSound(null, getPos().getX() + 0.5, getPos().getY() - 0.5, getPos().getZ() + 0.5, SoundManager.STAMPER_UP, SoundCategory.BLOCKS, 1.0f, 1.0f);
        powered = false;
        ticksExisted = 0;
        markDirty();
    }

    private ItemStampingRecipe getRecipe(ItemStack input, FluidStack fluid, ItemStack stamp) {
        return RecipeRegistry.getStampingRecipe(input, fluid, stamp);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == EmbersCapabilities.EMBER_CAPABILITY) {
            return true;
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == EmbersCapabilities.EMBER_CAPABILITY) {
            return (T) this.capability;
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) stamp;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        Misc.syncTE(this);
    }

    @Override
    public double getMechanicalSpeed(double power) {
        return Math.min(1.5,power);
    }

    @Override
    public double getNominalSpeed() {
        return 1;
    }

    @Override
    public double getMinimumPower() {
        return 10;
    }
}
