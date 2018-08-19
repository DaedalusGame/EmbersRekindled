package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.EventManager;
import teamroots.embers.SoundManager;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.api.upgrades.UpgradeUtil;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageEmberActivationFX;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class TileEntityActivatorBottom extends TileEntity implements ITileEntityBase, ITickable {
    public static final int PROCESS_TIME = 40;
    Random random = new Random();
    int progress = -1;
    public ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            TileEntityActivatorBottom.this.markDirty();
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (EmbersAPI.getEmberValue(stack) == 0) {
                return stack;
            }
            return super.insertItem(slot, stack, simulate);
        }
    };

    public TileEntityActivatorBottom() {
        super();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setTag("inventory", inventory.serializeNBT());
        tag.setInteger("progress", progress);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        inventory.deserializeNBT(tag.getCompoundTag("inventory"));
        if (tag.hasKey("progress")) {
            progress = tag.getInteger("progress");
        }
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
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        this.invalidate();
        Misc.spawnInventoryInWorld(getWorld(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, inventory);
        world.setTileEntity(pos, null);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) this.inventory;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        List<IUpgradeProvider> upgrades = UpgradeUtil.getUpgrades(world, pos, EnumFacing.HORIZONTALS);
        UpgradeUtil.verifyUpgrades(this, upgrades);
        if(UpgradeUtil.doTick(this,upgrades))
            return;

        if (!inventory.getStackInSlot(0).isEmpty()) {
            TileEntity tile = getWorld().getTileEntity(getPos().up());
            boolean cancel = UpgradeUtil.doWork(this,upgrades);

            if (!cancel && tile instanceof TileEntityActivatorTop) {
                TileEntityActivatorTop top = (TileEntityActivatorTop) tile;
                progress++;
                if (progress > UpgradeUtil.getWorkTime(this, PROCESS_TIME, upgrades)) {
                    progress = 0;
                    int i = 0;
                    if (inventory != null) {
                        ItemStack emberStack = inventory.getStackInSlot(i);
                        double emberValue = EmbersAPI.getEmberValue(emberStack);
                        if (emberValue > 0) {
                            double ember = UpgradeUtil.getTotalEmberProduction(this, emberValue, upgrades);
                            if (top.capability.getEmber() <= top.capability.getEmberCapacity() - ember) {
                                if (!world.isRemote) {
                                    world.playSound(null, getPos().getX() + 0.5, getPos().getY() + 1.5, getPos().getZ() + 0.5, SoundManager.ACTIVATOR, SoundCategory.BLOCKS, 1.0f, 1.0f);
                                    PacketHandler.INSTANCE.sendToAll(new MessageEmberActivationFX(getPos().getX() + 0.5f, getPos().getY() + 1.5f, getPos().getZ() + 0.5f));
                                }
                                top.capability.addAmount(ember, true);
                                inventory.extractItem(i, 1, false);
                                markDirty();
                                top.markDirty();
                            }
                        }
                    }
                }
                markDirty();
            }
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        Misc.syncTE(this);
    }
}
