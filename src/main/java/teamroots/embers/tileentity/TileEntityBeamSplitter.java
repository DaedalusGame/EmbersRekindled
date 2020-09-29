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
import teamroots.embers.EventManager;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.api.power.IEmberPacketProducer;
import teamroots.embers.api.power.IEmberPacketReceiver;
import teamroots.embers.block.BlockBeamSplitter;
import teamroots.embers.entity.EntityEmberPacket;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;
import java.util.Random;

public class TileEntityBeamSplitter extends TileEntity implements ITileEntityBase, ITickable, IEmberPacketProducer, IEmberPacketReceiver {
	public IEmberCapability capability = new DefaultEmberCapability() {
		@Override
		public boolean acceptsVolatile() {
			return false;
		}
	};
	Random random = new Random();
	public BlockPos targetLeft = null;
	public BlockPos targetRight = null;
	long ticksExisted = 0;
	public TileEntityBeamSplitter(){
		super();
		capability.setEmberCapacity(400);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		capability.writeToNBT(tag);
		if (targetLeft != null){
			tag.setInteger("targetLeftX", targetLeft.getX());
			tag.setInteger("targetLeftY", targetLeft.getY());
			tag.setInteger("targetLeftZ", targetLeft.getZ());
		}
		if (targetRight != null){
			tag.setInteger("targetRightX", targetRight.getX());
			tag.setInteger("targetRightY", targetRight.getY());
			tag.setInteger("targetRightZ", targetRight.getZ());
		}
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		capability.readFromNBT(tag);
		if (tag.hasKey("targetLeftX")){
			targetLeft = new BlockPos(tag.getInteger("targetLeftX"), tag.getInteger("targetLeftY"), tag.getInteger("targetLeftZ"));
		}
		if (tag.hasKey("targetRightX")){
			targetRight = new BlockPos(tag.getInteger("targetRightX"), tag.getInteger("targetRightY"), tag.getInteger("targetRightZ"));
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
	public void update() {
		this.ticksExisted ++;
		if (ticksExisted % 20 == 0 && !getWorld().isRemote && this.capability.getEmber() > 0) {
			TileEntity tileLeft = targetLeft != null ? getWorld().getTileEntity(targetLeft) : null;
			TileEntity tileRight = targetRight != null ? getWorld().getTileEntity(targetRight) : null;
			boolean sendLeft = (tileLeft instanceof IEmberPacketReceiver && !((IEmberPacketReceiver) tileLeft).isFull());
			boolean sendRight = (tileRight instanceof IEmberPacketReceiver && !((IEmberPacketReceiver) tileRight).isFull());
			if (!sendLeft && !sendRight)
				return;
			double amount = this.capability.getEmber();
			if (sendLeft && sendRight)
				amount /= 2.0;
			boolean isXAligned = getWorld().getBlockState(getPos()).getValue(BlockBeamSplitter.isXAligned);
			if (sendLeft) {
				EntityEmberPacket packetLeft = new EntityEmberPacket(getWorld());
				if (isXAligned) {
					packetLeft.initCustom(getPos(), targetLeft, 0, -0.01, -0.5, amount);
				} else {
					packetLeft.initCustom(getPos(), targetLeft, -0.5, -0.01, 0, amount);
				}
				getWorld().spawnEntity(packetLeft);
			}
			if (sendRight) {
				EntityEmberPacket packetRight = new EntityEmberPacket(getWorld());
				if (isXAligned) {
					packetRight.initCustom(getPos(), targetRight, 0, -0.01, 0.5, amount);
				} else {
					packetRight.initCustom(getPos(), targetRight, 0.5, -0.01, 0, amount);
				}
				getWorld().spawnEntity(packetRight);
			}
			this.capability.setEmber(0);
			markDirty();
		}
	}

	@Override
	public boolean isFull() {
		return capability.getEmber() >= capability.getEmberCapacity();
	}

	@Override
	public boolean onReceive(EntityEmberPacket packet) {
		return true;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		Misc.syncTE(this);
	}

	@Override
	public void setTargetPosition(BlockPos pos, EnumFacing side) {
		if(pos.equals(getPos()))
			return;
		IBlockState state = getWorld().getBlockState(getPos());
		if (state.getValue(BlockBeamSplitter.isXAligned)){
			if (side == EnumFacing.NORTH){
				targetLeft = pos;
				markDirty();
			}
			if (side == EnumFacing.SOUTH){
				targetRight = pos;
				markDirty();
			}
		}
		else {
			if (side == EnumFacing.WEST){
				targetLeft = pos;
				markDirty();
			}
			if (side == EnumFacing.EAST){
				targetRight = pos;
				markDirty();
			}
		}
	}
}
