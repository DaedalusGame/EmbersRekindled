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
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.api.power.IEmberPacketReceiver;
import teamroots.embers.block.BlockEmberEmitter;
import teamroots.embers.entity.EntityEmberPacket;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;

public class TileEntityEmberFunnel extends TileEntity implements ITileEntityBase, ITickable, IEmberPacketReceiver {
    public static final int TRANSFER_SPEED = 100; //It has 2000 capacity c'mon it needs to push super fast
    public IEmberCapability capability = new DefaultEmberCapability();
    long ticksExisted = 0L;

    public TileEntityEmberFunnel()
    {
        this.capability.setEmberCapacity(2000.0D);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);
        capability.writeToNBT(tag);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag){
        super.readFromNBT(tag);
        capability.readFromNBT(tag);
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
    public boolean isFull(){
        return capability.getEmber() >= capability.getEmberCapacity();
    }

    @Override
    public boolean onReceive(EntityEmberPacket packet) {
        return true;
    }

    @Override
    public void update() {
        this.ticksExisted ++;
        EnumFacing facing = world.getBlockState(pos).getValue(BlockEmberEmitter.facing);
        BlockPos attachPos = pos.offset(facing.getOpposite());
        TileEntity attachTile = world.getTileEntity(attachPos);
        if (ticksExisted % 2 == 0 && attachTile != null){
            if (attachTile.hasCapability(EmbersCapabilities.EMBER_CAPABILITY, facing)){
                IEmberCapability cap = attachTile.getCapability(EmbersCapabilities.EMBER_CAPABILITY, facing);
                if (cap != null){
                    if (cap.getEmber() < cap.getEmberCapacity() && capability.getEmber() > 0){
                        double added = cap.addAmount(Math.min(TRANSFER_SPEED,capability.getEmber()), true);
                        double removed = capability.removeAmount(added, true);
                        markDirty();
                        attachTile.markDirty();
                        if (!(attachTile instanceof ITileEntityBase) && !world.isRemote){
                            attachTile.markDirty(); //Idk why this is duplicated but the github source has it, so I carried it over.
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean activate(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumHand enumHand, EnumFacing enumFacing, float v, float v1, float v2) {
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        this.invalidate();
        world.setTileEntity(pos, null);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing){
        if (capability == EmbersCapabilities.EMBER_CAPABILITY){
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing){
        if (capability == EmbersCapabilities.EMBER_CAPABILITY){
            return (T)this.capability;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        Misc.syncTE(this);
    }
}

