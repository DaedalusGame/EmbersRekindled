package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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
import teamroots.embers.api.tile.IExtraDialInformation;
import teamroots.embers.block.BlockClockworkAttenuator;
import teamroots.embers.upgrade.UpgradeClockworkAttenuator;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class TileEntityClockworkAttenuator extends TileEntity implements ITickable, ITileEntityBase {
    public UpgradeClockworkAttenuator upgrade;
    public boolean powered, lastPowered = false;
    public double activeSpeed = 0, inactiveSpeed = 1;
    public double angle, lastAngle;
    private Random random = new Random();

    public double[] validSpeeds = new double[]{0.0, 0.0625, 0.125, 0.25, 0.5, 1.0};

    public TileEntityClockworkAttenuator() {
        upgrade = new UpgradeClockworkAttenuator(this);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setDouble("active_speed", activeSpeed);
        tag.setDouble("inactive_speed", inactiveSpeed);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        activeSpeed = tag.getDouble("active_speed");
        inactiveSpeed = tag.getDouble("inactive_speed");
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
        if (state.getBlock() instanceof BlockClockworkAttenuator)
            return state.getValue(BlockClockworkAttenuator.facing);
        return null;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY)
            return getFacing().getOpposite() == facing;
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY && getFacing().getOpposite() == facing)
            return (T) upgrade;
        return super.getCapability(capability, facing);
    }

    public double getSpeed() {
        return powered ? activeSpeed : inactiveSpeed;
    }

    public double getNext(double current) {
        for (int i = 0; i < validSpeeds.length - 1; i++) {
            double a = validSpeeds[i];
            double b = validSpeeds[i + 1];

            if (b > current && a <= current)
                return b;
        }
        return current;
    }

    public double getPrevious(double current) {
        for (int i = 0; i < validSpeeds.length - 1; i++) {
            double a = validSpeeds[i];
            double b = validSpeeds[i + 1];

            if (b >= current && a < current)
                return a;
        }
        return current;
    }

    @Override
    public void update() {
        lastPowered = powered;
        powered = world.isBlockPowered(pos);
        if (world.isRemote) {
            lastAngle = angle;
            angle += getSpeed();
        }
    }

    @Override
    public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (powered)
            activeSpeed = player.isSneaking() ? getPrevious(activeSpeed) : getNext(activeSpeed);
        else
            inactiveSpeed = player.isSneaking() ? getPrevious(inactiveSpeed) : getNext(inactiveSpeed);
        markDirty();
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {

    }

    @Override
    public void markDirty() {
        super.markDirty();
        Misc.syncTE(this);
    }
}
