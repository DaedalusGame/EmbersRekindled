package teamroots.embers.tileentity;

import java.util.Random;

import javax.annotation.Nullable;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import teamroots.embers.block.BlockBeamSplitter;
import teamroots.embers.entity.EntityEmberPacket;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.power.IEmberPacketProducer;
import teamroots.embers.power.IEmberPacketReceiver;

public class TileEntityBeamSplitter extends TileEntity implements ITileEntityBase, ITickable, IEmberPacketProducer {
	public IEmberCapability capability = new DefaultEmberCapability();
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
		if (capability == EmberCapabilityProvider.emberCapability){
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == EmberCapabilityProvider.emberCapability){
			return (T)this.capability;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void update() {
		this.ticksExisted ++;
		if (ticksExisted % 20 == 0 && !getWorld().isRemote && this.capability.getEmber() > 0){
			if (targetLeft != null && targetRight != null){
				double amount = this.capability.getEmber()/2.0;
				boolean didSend = false;
				IBlockState state = getWorld().getBlockState(getPos());
				if (state.getValue(BlockBeamSplitter.isXAligned)){
					if (getWorld().getTileEntity(targetLeft) != null){
						if (!((IEmberPacketReceiver)getWorld().getTileEntity(targetLeft)).isFull()){
							EntityEmberPacket packetLeft = new EntityEmberPacket(getWorld());
							packetLeft.initCustom(getPos(), targetLeft, 0, -0.01, -0.5, amount);
							getWorld().spawnEntity(packetLeft);
							didSend = true;
						}
					}
					if (getWorld().getTileEntity(targetRight) instanceof IEmberPacketReceiver){
						if (!((IEmberPacketReceiver)getWorld().getTileEntity(targetRight)).isFull()){
							EntityEmberPacket packetRight = new EntityEmberPacket(getWorld());
							packetRight.initCustom(getPos(), targetRight, 0, -0.01, 0.5, amount);
							getWorld().spawnEntity(packetRight);
							didSend = true;
						}
					}
				}
				else {
					if (getWorld().getTileEntity(targetLeft) instanceof IEmberPacketReceiver){
						if (!((IEmberPacketReceiver)getWorld().getTileEntity(targetLeft)).isFull()){
							EntityEmberPacket packetLeft = new EntityEmberPacket(getWorld());
							packetLeft.initCustom(getPos(), targetLeft, -0.5, -0.01, 0, amount);
							getWorld().spawnEntity(packetLeft);
							didSend = true;
						}
					}
					if (getWorld().getTileEntity(targetRight) instanceof IEmberPacketReceiver){
						if (!((IEmberPacketReceiver)getWorld().getTileEntity(targetRight)).isFull()){
							EntityEmberPacket packetRight = new EntityEmberPacket(getWorld());
							packetRight.initCustom(getPos(), targetRight, 0.5, -0.01, 0, amount);
							getWorld().spawnEntity(packetRight);
							didSend = true;
						}
					}
				}
				if (didSend){
					this.capability.setEmber(0);
					markDirty();
				}
			}
			else if (targetLeft != null){
				double amount = capability.getEmber();
				boolean didSend = false;
				IBlockState state = getWorld().getBlockState(getPos());
				if (state.getValue(BlockBeamSplitter.isXAligned)){
					if (getWorld().getTileEntity(targetLeft) instanceof IEmberPacketReceiver){
						if (!((IEmberPacketReceiver)getWorld().getTileEntity(targetLeft)).isFull()){
							EntityEmberPacket packetLeft = new EntityEmberPacket(getWorld());
							packetLeft.initCustom(getPos(), targetLeft, 0, -0.01, -0.5, amount);
							getWorld().spawnEntity(packetLeft);
							didSend = true;
						}
					}
				}
				else {
					if (getWorld().getTileEntity(targetLeft) instanceof IEmberPacketReceiver){
						if (!((IEmberPacketReceiver)getWorld().getTileEntity(targetLeft)).isFull()){
							EntityEmberPacket packetLeft = new EntityEmberPacket(getWorld());
							packetLeft.initCustom(getPos(), targetLeft, -0.5, -0.01, 0, amount);
							getWorld().spawnEntity(packetLeft);
							didSend = true;
						}
					}
				}
				if (didSend){
					this.capability.setEmber(0);
					markDirty();
				}
			}
			else if (targetRight != null){
				double amount = this.capability.getEmber();
				boolean didSend = false;
				IBlockState state = getWorld().getBlockState(getPos());
				if (state.getValue(BlockBeamSplitter.isXAligned)){
					if (getWorld().getTileEntity(targetRight) instanceof IEmberPacketReceiver){
						if (!((IEmberPacketReceiver)getWorld().getTileEntity(targetRight)).isFull()){
							EntityEmberPacket packetRight = new EntityEmberPacket(getWorld());
							packetRight.initCustom(getPos(), targetRight, 0, -0.01, 0.5, amount);
							getWorld().spawnEntity(packetRight);
							didSend = true;
						}
					}
				}
				else {
					if (getWorld().getTileEntity(targetRight) instanceof IEmberPacketReceiver){
						if (!((IEmberPacketReceiver)getWorld().getTileEntity(targetRight)).isFull()){
							EntityEmberPacket packetRight = new EntityEmberPacket(getWorld());
							packetRight.initCustom(getPos(), targetRight, 0.5, -0.01, 0, amount);
							getWorld().spawnEntity(packetRight);
							didSend = true;
						}
					}
				}
				if (didSend){
					this.capability.setEmber(0);
					markDirty();
				}
			}
		}
	}
	
	public boolean dirty = false;
	
	@Override
	public void markForUpdate(){
		dirty = true;
	}
	
	@Override
	public boolean needsUpdate(){
		return dirty;
	}
	
	@Override
	public void clean(){
		dirty = false;
	}
	
	@Override
	public void markDirty(){
		markForUpdate();
		super.markDirty();
	}

	@Override
	public void setTargetPosition(BlockPos pos, EnumFacing side) {
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
