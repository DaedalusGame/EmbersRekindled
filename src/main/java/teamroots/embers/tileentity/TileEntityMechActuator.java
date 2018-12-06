package teamroots.embers.tileentity;

import mysticalmechanics.api.IGearBehavior;
import mysticalmechanics.api.IGearbox;
import mysticalmechanics.api.MysticalMechanicsAPI;
import mysticalmechanics.block.BlockGearbox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.tile.IExtraCapabilityInformation;
import teamroots.embers.block.BlockMechActuator;
import teamroots.embers.upgrade.UpgradeActuator;
import teamroots.embers.util.ConsumerMechCapability;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntityMechActuator extends TileEntity implements ITickable, ITileEntityBase, IGearbox, IExtraCapabilityInformation {
    public UpgradeActuator upgrade;
    public ItemStack[] gears = new ItemStack[]{
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ItemStack.EMPTY
    };
    public double angle, lastAngle;
    private Random random = new Random();
    public ConsumerMechCapability capability = new ConsumerMechCapability() {
        @Override
        public void onPowerChange() {
            TileEntityMechActuator box = TileEntityMechActuator.this;
            box.updateNeighbors();
            box.markDirty();
        }

        @Override
        public double getPower(EnumFacing from) {
            ItemStack gearStack = getGear(from);
            if (from != null && gearStack.isEmpty()) {
                return 0;
            }
            IGearBehavior behavior = MysticalMechanicsAPI.IMPL.getGearBehavior(gearStack);
            return behavior.transformPower(TileEntityMechActuator.this, from, gearStack, super.getPower(from));
        }

        @Override
        public void setPower(double value, EnumFacing from) {
            ItemStack gearStack = getGear(from);
            if (from != null && gearStack.isEmpty()) {
                super.setPower(0, from);
            }
            super.setPower(value, from);
        }
    };

    public TileEntityMechActuator() {
        upgrade = new UpgradeActuator(this);
        capability.setAdditive(true); //Possible balance mistake but we shall see
    }

    public void updateNeighbors() {
        for (EnumFacing f : EnumFacing.VALUES) {
            TileEntity t = world.getTileEntity(getPos().offset(f));
            if (t != null && t.hasCapability(MysticalMechanicsAPI.MECH_CAPABILITY, f.getOpposite()) && !getGear(f).isEmpty())
                capability.setPower(t.getCapability(MysticalMechanicsAPI.MECH_CAPABILITY, f.getOpposite()).getPower(f.getOpposite()), f);
            else
                capability.setPower(0, f);
        }
        markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        for (int i = 0; i < 6; i++) {
            tag.setTag("gear" + i, gears[i].writeToNBT(new NBTTagCompound()));
        }
        for (int i = 0; i < 6; i++) {
            tag.setDouble("mech_power" + i, capability.power[i]);
        }
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        for (int i = 0; i < 6; i++) {
            gears[i] = new ItemStack(tag.getCompoundTag("gear" + i));
        }
        for (int i = 0; i < 6; i++) {
            capability.power[i] = tag.getDouble("mech_power" + i);
        }
        capability.markDirty();
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
            return facing == null || getFacing().getAxis() != facing.getAxis();
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY && getFacing().getOpposite() == facing)
            return (T) upgrade;
        if (capability == MysticalMechanicsAPI.MECH_CAPABILITY && (facing == null || getFacing().getAxis() != facing.getAxis()))
            return (T) this.capability;
        return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        if (world.isRemote) {
            lastAngle = angle;
            angle += capability.getPower(null);
            for (EnumFacing facing : EnumFacing.VALUES) {
                ItemStack gear = getGear(facing);
                IGearBehavior behavior = MysticalMechanicsAPI.IMPL.getGearBehavior(gear);
                behavior.visualUpdate(this, facing, gear);
            }
        }
    }

    @Override
    public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (!heldItem.isEmpty() && canAttachGear(side, heldItem)) {
            if (getGear(side).isEmpty() && MysticalMechanicsAPI.IMPL.isValidGear(heldItem)) {
                ItemStack gear = heldItem.copy();
                gear.setCount(1);
                attachGear(side, gear);
                heldItem.shrink(1);
                if (heldItem.isEmpty()) {
                    player.setHeldItem(hand, ItemStack.EMPTY);
                }
                capability.onPowerChange();
                return true;
            }
        } else if (!getGear(side).isEmpty()) {
            ItemStack gear = detachGear(side);
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
            if (!world.isRemote) {
                world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, gears[i]));
            }
            gears[i] = ItemStack.EMPTY;
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
    public void attachGear(EnumFacing facing, ItemStack stack) {
        if (facing == null)
            return;
        gears[facing.getIndex()] = stack;
        markDirty();
    }

    @Override
    public ItemStack detachGear(EnumFacing facing) {
        if (facing == null || getFacing().getAxis() == facing.getAxis())
            return ItemStack.EMPTY;
        int index = facing.getIndex();
        ItemStack gear = gears[index];
        gears[index] = ItemStack.EMPTY;
        markDirty();
        return gear;
    }

    public ItemStack getGear(EnumFacing facing) {
        if (facing == null || getFacing().getAxis() == facing.getAxis())
            return ItemStack.EMPTY;
        return gears[facing.getIndex()];
    }

    @Override
    public boolean canAttachGear(EnumFacing facing, ItemStack stack) {
        return getFacing().getAxis() != facing.getAxis();
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
