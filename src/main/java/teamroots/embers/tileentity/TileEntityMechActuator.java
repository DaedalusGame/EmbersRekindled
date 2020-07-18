package teamroots.embers.tileentity;

import mysticalmechanics.api.*;
import mysticalmechanics.tileentity.TileEntityMergebox;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.tile.IExtraCapabilityInformation;
import teamroots.embers.block.BlockMechActuator;
import teamroots.embers.upgrade.UpgradeActuator;
import teamroots.embers.util.ConsumerMechCapability;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class TileEntityMechActuator extends TileEntity implements ITickable, ITileEntityBase, IGearbox, IExtraCapabilityInformation {
    public UpgradeActuator upgrade;
    public GearHelperTile[] gears = new mysticalmechanics.api.GearHelperTile[6];

    public boolean shouldUpdate;

    private Random random = new Random();
    public ConsumerMechCapability capability = new ConsumerMechCapability() {
        @Override
        public void onPowerChange() {
            TileEntityMechActuator box = TileEntityMechActuator.this;
            shouldUpdate = true;
            box.markDirty();
        }

        @Override
        public double getVisualPower(EnumFacing from) {
            GearHelper gearHelper = getGearHelper(from);
            if (gearHelper != null && gearHelper.isEmpty()) {
                return 0;
            }

            double unchangedPower = getExternalPower(from);

            if (gearHelper == null)
                return unchangedPower;

            IGearBehavior behavior = gearHelper.getBehavior();
            return behavior.transformVisualPower(TileEntityMechActuator.this, from, gearHelper.getGear(), gearHelper.getData(), unchangedPower);
        }

        @Override
        public double getPower(EnumFacing from) {
            GearHelper gearHelper = getGearHelper(from);
            if (gearHelper != null && gearHelper.isEmpty()) {
                return 0;
            }
            return super.getPower(from);
        }

        @Override
        public void setPower(double value, EnumFacing from) {
            GearHelper gearHelper = getGearHelper(from);
            if (isInput(from) && gearHelper.isEmpty()) {
                super.setPower(0, from);
            }
            if(isInput(from)) {
                powerExternal[from.getIndex()] = value;
                if(!gearHelper.isEmpty()) {
                    IGearBehavior behavior = gearHelper.getBehavior();
                    value = behavior.transformPower(TileEntityMechActuator.this, from,gearHelper.getGear(),gearHelper.getData(),value);
                    super.setPower(value, from);
                }
            }
        }

        @Override
        public boolean isInput(EnumFacing from) {
            return canAttachGear(from);
        }
    };

    private GearHelper getGearHelper(EnumFacing facing) {
        if (facing == null)
            return null;
        return gears[facing.getIndex()];
    }

    public TileEntityMechActuator() {
        upgrade = new UpgradeActuator(this);
        for(int i = 0; i < gears.length; i++)
            gears[i] = new GearHelperTile(this, EnumFacing.getFront(i));
        capability.setAdditive(true); //Possible balance mistake but we shall see
    }

    public void updateNeighbors() {
        for (EnumFacing f : EnumFacing.VALUES) {
            MysticalMechanicsAPI.IMPL.pullPower(this, f, capability, !getGear(f).isEmpty());
        }
        markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        for (int i = 0; i < 6; i++) {
            tag.setTag("side" + i, gears[i].writeToNBT(new NBTTagCompound()));
        }
        capability.writeToNBT(tag);
        for (int i = 0; i < 6; i++) {
            tag.setDouble("mech_power" + i, capability.power[i]);
        }
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        for (int i = 0; i < 6; i++) {
            gears[i].readFromNBT(tag.getCompoundTag("side" + i));
        }
        readLegacyGears(tag);
        for (int i = 0; i < 6; i++) {
            capability.power[i] = tag.getDouble("mech_power" + i);
        }
        capability.readFromNBT(tag);
        capability.markDirty();
    }

    private void readLegacyGears(NBTTagCompound tag) {
        for (int i = 0; i < 6; i++) {
            if(tag.hasKey("gear"+i))
                gears[i].setGear(new ItemStack(tag.getCompoundTag("gear" + i)));
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

    public EnumFacing getFacing() {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockMechActuator)
            return state.getValue(BlockMechActuator.facing);
        return null;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY)
            return getFacing().getOpposite() == facing;
        if (capability == MysticalMechanicsAPI.MECH_CAPABILITY)
            return facing == null || canAttachGear(facing);
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY && getFacing().getOpposite() == facing)
            return (T) upgrade;
        if (capability == MysticalMechanicsAPI.MECH_CAPABILITY && (facing == null || canAttachGear(facing)))
            return (T) this.capability;
        return super.getCapability(capability, facing);
    }

    private double getGearInPower(EnumFacing facing) {
        return capability.getExternalPower(facing);
    }

    private double getGearOutPower(EnumFacing facing) {
        return capability.getInternalPower(facing);
    }

    @Override
    public void update() {
        if (shouldUpdate) {
            updateNeighbors();
            shouldUpdate = false;
        }
        for (EnumFacing facing : EnumFacing.VALUES) {
            int i = facing.getIndex();
            if (world.isRemote) {
                gears[i].visualUpdate(getGearInPower(facing), capability.getVisualPower(facing));
            }
            gears[i].tick(getGearInPower(facing), getGearOutPower(facing));
            if (gears[i].isDirty())
                shouldUpdate = true;
        }
    }

    @Override
    public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (!heldItem.isEmpty() && canAttachGear(side, heldItem)) {
            if (getGear(side).isEmpty() && MysticalMechanicsAPI.IMPL.isValidGear(heldItem)) {
                ItemStack gear = heldItem.copy();
                gear.setCount(1);
                attachGear(side, gear, player);
                heldItem.shrink(1);
                if (heldItem.isEmpty()) {
                    player.setHeldItem(hand, ItemStack.EMPTY);
                }
                capability.onPowerChange();
                return true;
            }
        } else if (!getGear(side).isEmpty()) {
            ItemStack gear = detachGear(side, player);
            if (!world.isRemote) {
                world.spawnEntity(new EntityItem(world, player.posX, player.posY + player.height / 2.0f, player.posZ, gear));
            }
            capability.onPowerChange();
            return true;
        }
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        for (int i = 0; i < 6; i++) {
            ItemStack stack = gears[i].detach(player);
            if (!world.isRemote) {
                world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack));
            }
        }
        capability.setPower(0f, null);
        updateNeighbors();
    }

    @Override
    public void markDirty() {
        super.markDirty();
        Misc.syncTE(this);
    }

    @Override
    public void attachGear(EnumFacing facing, ItemStack stack, @Nullable EntityPlayer player) {
        if (!canAttachGear(facing))
            return;
        gears[facing.getIndex()].attach(player, stack);
        markDirty();
    }

    @Override
    public ItemStack detachGear(EnumFacing facing, @Nullable EntityPlayer player) {
        if (!canAttachGear(facing))
            return ItemStack.EMPTY;
        ItemStack stack = gears[facing.getIndex()].detach(player);
        markDirty();
        return stack;
    }

    public ItemStack getGear(EnumFacing facing) {
        if (!canAttachGear(facing))
            return ItemStack.EMPTY;
        return gears[facing.getIndex()].getGear();
    }

    @Override
    public boolean canAttachGear(EnumFacing facing, ItemStack stack) {
        return canAttachGear(facing);
    }

    @Override
    public boolean canAttachGear(EnumFacing facing) {
        return facing != null && getFacing().getAxis() != facing.getAxis();
    }

    @Override
    public int getConnections() {
        return 1;
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return false;
    }

    @Override
    public void addCapabilityDescription(List<String> strings, Capability<?> capability, EnumFacing facing) {
        //NOOP
    }
}
