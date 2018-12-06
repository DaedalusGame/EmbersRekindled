package teamroots.embers.tileentity;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import teamroots.embers.Embers;
import teamroots.embers.EventManager;
import teamroots.embers.SoundManager;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.tile.IExtraCapabilityInformation;
import teamroots.embers.api.tile.IExtraDialInformation;
import teamroots.embers.block.BlockCatalyticPlug;
import teamroots.embers.block.BlockFluidGauge;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.upgrade.UpgradeCatalyticPlug;
import teamroots.embers.util.Misc;
import teamroots.embers.util.sound.ISoundController;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class TileEntityCatalyticPlug extends TileEntity implements ITickable, ITileEntityBase, ISoundController, IExtraDialInformation, IExtraCapabilityInformation {
    public static final int SOUND_OFF = 1;
    public static final int SOUND_ON = 2;
    public static final int[] SOUND_IDS = new int[]{SOUND_OFF,SOUND_ON};

    public int activeTicks = 0;
    public UpgradeCatalyticPlug upgrade;
    public FluidTank tank = new FluidTank(4000) {
        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return fluid != null && fluid.getFluid() == FluidRegistry.getFluid("alchemical_redstone");
        }
    };
    private Random random = new Random();

    HashSet<Integer> soundsPlaying = new HashSet<>();

    public TileEntityCatalyticPlug() {
        upgrade = new UpgradeCatalyticPlug(this);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);
        NBTTagCompound tankTag = new NBTTagCompound();
        tank.writeToNBT(tankTag);
        tag.setTag("tank", tankTag);
        tag.setInteger("active",activeTicks);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag){
        super.readFromNBT(tag);
        tank.readFromNBT(tag.getCompoundTag("tank"));
        activeTicks = tag.getInteger("active");
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

    public EnumFacing getFacing()
    {
        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() instanceof BlockCatalyticPlug)
            return state.getValue(BlockCatalyticPlug.FACING);
        return null;
    }

    public void setActive(int ticks) {
        activeTicks = Math.max(ticks,activeTicks);
        markDirty();
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
        if(getWorld().isRemote)
            handleSound();
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
                ParticleUtil.spawnParticleVapor(getWorld(), x, y, z, motionx, motiony, motionz, 255, 16, 16, 1.0f, 1.0f, 2.0f, 24);
            }
        }
    }

    @Override
    public void playSound(int id) {
        float soundX = (float) pos.getX() + 0.5f;
        float soundY = (float) pos.getY() + 0.5f;
        float soundZ = (float) pos.getZ() + 0.5f;
        switch (id) {
            case SOUND_ON:
                Embers.proxy.playMachineSound(this,SOUND_ON, SoundManager.CATALYTIC_PLUG_LOOP, SoundCategory.BLOCKS, true, 1.0f, 1.0f, soundX, soundY, soundZ);
                world.playSound(soundX,soundY,soundZ,SoundManager.CATALYTIC_PLUG_START,SoundCategory.BLOCKS,1.0f,1.0f,false);
                break;
            case SOUND_OFF:
                Embers.proxy.playMachineSound(this,SOUND_OFF, SoundManager.CATALYTIC_PLUG_LOOP_READY, SoundCategory.BLOCKS, true, 1.0f, 1.0f, soundX, soundY, soundZ);
                break;
        }
        soundsPlaying.add(id);
    }

    @Override
    public void stopSound(int id) {
        if(id == SOUND_ON) {
            world.playSound((float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f, SoundManager.CATALYTIC_PLUG_STOP, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
        }
        soundsPlaying.remove(id);
    }

    @Override
    public boolean isSoundPlaying(int id) {
        return soundsPlaying.contains(id);
    }

    @Override
    public int[] getSoundIDs() {
        return SOUND_IDS;
    }

    @Override
    public boolean shouldPlaySound(int id) {
        boolean isWorking = activeTicks > 0;

        switch (id)
        {
            case SOUND_OFF: return !isWorking && tank.getFluidAmount() > 0;
            case SOUND_ON: return isWorking;
            default: return false;
        }
    }

    @Override
    public float getCurrentVolume(int id, float volume) {
        boolean isWorking = activeTicks > 0;

        switch (id)
        {
            case SOUND_OFF: return !isWorking ? 1.0f : 0.0f;
            case SOUND_ON: return isWorking ? 1.0f : 0.0f;
            default: return 0f;
        }
    }

    @Override
    public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
    }

    public FluidStack getFluidStack() {
        return tank.getFluid();
    }

    public int getCapacity() {
        return tank.getCapacity();
    }

    @Override
    public void markDirty() {
        super.markDirty();
        Misc.syncTE(this);
    }

    @Override
    public void addDialInformation(EnumFacing facing, List<String> information, String dialType) {
        if(BlockFluidGauge.DIAL_TYPE.equals(dialType)) {
            information.clear();
            information.add(BlockFluidGauge.formatFluidStack(getFluidStack(),getCapacity()));
        }
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }

    @Override
    public void addCapabilityDescription(List<String> strings, Capability<?> capability, EnumFacing facing) {
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            strings.add(IExtraCapabilityInformation.formatCapability(EnumIOType.INPUT,"embers.tooltip.goggles.fluid",I18n.format("embers.tooltip.goggles.fluid.redstone")));
    }
}