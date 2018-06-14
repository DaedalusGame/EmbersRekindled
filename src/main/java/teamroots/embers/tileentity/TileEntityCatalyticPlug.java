package teamroots.embers.tileentity;

import net.minecraft.block.state.BlockFaceShape;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import teamroots.embers.EventManager;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.block.BlockCatalyticPlug;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.upgrade.UpgradeCatalyticPlug;

import javax.annotation.Nullable;
import java.util.Random;

public class TileEntityCatalyticPlug extends TileEntity implements ITickable, ITileEntityBase {
    public int activeTicks = 0;
    public UpgradeCatalyticPlug upgrade;
    public FluidTank tank = new FluidTank(4000) {
        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return fluid != null && fluid.getFluid() == FluidRegistry.getFluid("alchemical_redstone");
        }
    };
    private Random random = new Random();

    public TileEntityCatalyticPlug() {
        upgrade = new UpgradeCatalyticPlug(this);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);
        NBTTagCompound tankTag = new NBTTagCompound();
        tank.writeToNBT(tankTag);
        tag.setTag("tank", tankTag);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag){
        super.readFromNBT(tag);
        tank.readFromNBT(tag.getCompoundTag("tank"));
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
    public void markDirty(){
        EventManager.markTEForUpdate(getPos(), this);
        super.markDirty();
    }

    public EnumFacing getFacing()
    {
        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() instanceof BlockCatalyticPlug)
            return state.getValue(BlockCatalyticPlug.FACING);
        return null;
    }

    public void setActive(int ticks) {
        activeTicks = Math.max(ticks,activeTicks);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY)
            return getFacing() == facing;
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return getFacing().getOpposite() == facing || facing == null;
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY && getFacing() == facing)
            return (T) upgrade;
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && (getFacing().getOpposite() == facing || facing == null))
            return (T) tank;
        return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        activeTicks--;
        IBlockState state = world.getBlockState(pos);
        if(activeTicks > 0 && world.isRemote && state.getBlock() instanceof BlockCatalyticPlug) {
            EnumFacing facing = state.getValue(BlockCatalyticPlug.FACING);
            float yoffset = 0.4f;
            float wideoffset = 0.5f;
            Vec3d baseOffset = new Vec3d(0.5 - facing.getFrontOffsetX() * yoffset, 0.5 - facing.getFrontOffsetY() * yoffset, 0.5 - facing.getFrontOffsetZ() * yoffset);
            EnumFacing[] planars;
            switch(facing.getAxis()) {
                case X:
                    planars = new EnumFacing[] {EnumFacing.DOWN,EnumFacing.UP,EnumFacing.NORTH,EnumFacing.SOUTH}; break;
                case Y:
                    planars = new EnumFacing[] {EnumFacing.EAST,EnumFacing.WEST,EnumFacing.NORTH,EnumFacing.SOUTH}; break;
                case Z:
                    planars = new EnumFacing[] {EnumFacing.DOWN,EnumFacing.UP,EnumFacing.EAST,EnumFacing.WEST}; break;
                default:
                    planars = null; break;
            }
            for(EnumFacing planar : planars) {
                IBlockState sideState = world.getBlockState(pos.offset(planar));
                if(sideState.getBlockFaceShape(world,pos.offset(planar),planar.getOpposite()) != BlockFaceShape.UNDEFINED)
                    continue;
                float x = getPos().getX() + (float) baseOffset.x + planar.getFrontOffsetX() * wideoffset;
                float y = getPos().getY() + (float) baseOffset.y + planar.getFrontOffsetY() * wideoffset;
                float z = getPos().getZ() + (float) baseOffset.z + planar.getFrontOffsetZ() * wideoffset;
                float motionx = planar.getFrontOffsetX() * 0.03f - facing.getFrontOffsetX() * 0.015f - 0.01f + random.nextFloat() * 0.02f;
                float motiony = planar.getFrontOffsetY() * 0.03f - facing.getFrontOffsetY() * 0.015f - 0.01f + random.nextFloat() * 0.02f;
                float motionz = planar.getFrontOffsetZ() * 0.03f - facing.getFrontOffsetZ() * 0.015f - 0.01f + random.nextFloat() * 0.02f;
                ParticleUtil.spawnParticleGlow(getWorld(), x, y, z, motionx, motiony, motionz, 255, 16, 16, 2.0f, 24);
            }
        }
    }

    @Override
    public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
    }

    @Override
    public void markForUpdate() {
    }
}