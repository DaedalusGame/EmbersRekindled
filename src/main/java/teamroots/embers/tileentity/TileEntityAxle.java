package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import teamroots.embers.EventManager;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.block.BlockAxle;
import teamroots.embers.power.DefaultMechCapability;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;

public class TileEntityAxle extends TileEntity implements ITileEntityBase {
	int ticksExisted = 0;
	EnumFacing front = null;
	EnumFacing from = null;
	public DefaultMechCapability capability = new DefaultMechCapability(){
		@Override
		public void setPower(double value, EnumFacing face){
			from = face;
			super.setPower(value, face);
		}
		
		@Override
		public void onContentsChanged(){
			TileEntityAxle axle = TileEntityAxle.this;
			axle.updateNeighbors();
			axle.markDirty();
		}
	};
	
	public void updateNeighbors(){
		IBlockState state = world.getBlockState(getPos());
		if (state.getBlock() instanceof BlockAxle){
			front = Misc.getOppositeFace(state.getValue(BlockAxle.facing));
			markDirty();
		}
		if (front != null && !this.isInvalid()){
			TileEntity lastTile = world.getTileEntity(getPos().offset(front));
			if (lastTile == null){
				capability.setPower(0,null);
				markDirty();
			}
			else {
				if (!lastTile.hasCapability(EmbersCapabilities.MECH_CAPABILITY, Misc.getOppositeFace(front))){
					capability.setPower(0,null);
					markDirty();
				}
				else {
					capability.setPower(lastTile.getCapability(EmbersCapabilities.MECH_CAPABILITY, Misc.getOppositeFace(front)).getPower(Misc.getOppositeFace(front)), front);
				}
			}
		}
		if (state.getBlock() instanceof BlockAxle){
			EnumFacing face = state.getValue(BlockAxle.facing);
			if (face != from){
				BlockPos p1 = getPos().offset(face);
				BlockPos fromPos = null;
				if (front != null){
					fromPos = getPos().offset(front);
				}
				if (fromPos == null || fromPos != null && !p1.equals(fromPos)){
					TileEntity t = world.getTileEntity(p1);
					if (t != null && t.hasCapability(EmbersCapabilities.MECH_CAPABILITY, Misc.getOppositeFace(face))){
						if (!(t instanceof TileEntityAxle && ((TileEntityAxle) t).front == Misc.getOppositeFace(front))){
							t.getCapability(EmbersCapabilities.MECH_CAPABILITY, Misc.getOppositeFace(face)).setPower(capability.getPower(face),Misc.getOppositeFace(face));
							t.markDirty();
						}
					}
				}
			}
		}
	}
	
	public TileEntityAxle(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		capability.writeToNBT(tag);
		if (front != null){
			tag.setInteger("front",front.getIndex());
		}
		if (from != null){
			tag.setInteger("from",from.getIndex());
		}
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		capability.readFromNBT(tag);
		if (tag.hasKey("front")){
			front = EnumFacing.getFront(tag.getInteger("front"));
		}
		if (tag.hasKey("from")){
			from = EnumFacing.getFront(tag.getInteger("from"));
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
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		if (capability == EmbersCapabilities.MECH_CAPABILITY){
			IBlockState state = world.getBlockState(getPos());
			if (state.getBlock() instanceof BlockAxle){
				EnumFacing face = state.getValue(BlockAxle.facing);
				if (face != null && facing != null){
					if (face.getAxis() == facing.getAxis()){
						return true;
					}
				}
			}
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == EmbersCapabilities.MECH_CAPABILITY){
			return (T)this.capability;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void markDirty() {
		super.markDirty();
		Misc.syncTE(this);
	}

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		System.out.println(front);
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		capability.setPower(0f,null);
		updateNeighbors();
		world.setTileEntity(pos, null);
	}
}
