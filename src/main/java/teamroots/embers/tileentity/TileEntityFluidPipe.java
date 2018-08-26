package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import teamroots.embers.EventManager;
import teamroots.embers.item.ItemTinkerHammer;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntityFluidPipe extends TileFluidHandler implements ITileEntityBase, ITickable {
	Random random = new Random();
	List<EnumFacing> from = new ArrayList<>();
	public enum EnumPipeConnection{
		NONE, PIPE, BLOCK, LEVER, FORCENONE
	}
	
	public static EnumPipeConnection connectionFromInt(int value){
		switch (value){
		case 0:
			return EnumPipeConnection.NONE;
		case 1:
			return EnumPipeConnection.PIPE;
		case 2:
			return EnumPipeConnection.BLOCK;
		case 3:
			return EnumPipeConnection.LEVER;
		case 4:
			return EnumPipeConnection.FORCENONE;
		}
		return EnumPipeConnection.NONE;
	}
	
	public EnumPipeConnection up = EnumPipeConnection.NONE, down = EnumPipeConnection.NONE, north = EnumPipeConnection.NONE, south = EnumPipeConnection.NONE, east = EnumPipeConnection.NONE, west = EnumPipeConnection.NONE;
	
	public TileEntityFluidPipe(){
		super();
		tank.setCapacity(1000);
	}
	
	public void updateNeighbors(IBlockAccess world){
		up = getConnection(world,getPos().up(),EnumFacing.UP);
		down = getConnection(world,getPos().down(),EnumFacing.DOWN);
		north = getConnection(world,getPos().north(),EnumFacing.NORTH);
		south = getConnection(world,getPos().south(),EnumFacing.SOUTH);
		west = getConnection(world,getPos().west(),EnumFacing.WEST);
		east = getConnection(world,getPos().east(),EnumFacing.EAST);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setInteger("up", up.ordinal());
		tag.setInteger("down", down.ordinal());
		tag.setInteger("north", north.ordinal());
		tag.setInteger("south", south.ordinal());
		tag.setInteger("west", west.ordinal());
		tag.setInteger("east", east.ordinal());
		NBTTagList l = new NBTTagList();
		for (EnumFacing f : from){
			l.appendTag(new NBTTagInt(f.getIndex()));
		}
		tag.setTag("from", l);
		return tag;
	}
	
	/*@SideOnly(Side.CLIENT)
	@Override
	public boolean hasFastRenderer(){
		return true;
	}*/
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		up = connectionFromInt(tag.getInteger("up"));
		down = connectionFromInt(tag.getInteger("down"));
		north = connectionFromInt(tag.getInteger("north"));
		south = connectionFromInt(tag.getInteger("south"));
		west = connectionFromInt(tag.getInteger("west"));
		east = connectionFromInt(tag.getInteger("east"));
		NBTTagList l = tag.getTagList("from", Constants.NBT.TAG_INT);
		for (int i = 0; i < l.tagCount(); i ++){
			from.add(EnumFacing.getFront(l.getIntAt(i)));
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
	
	public EnumPipeConnection getConnection(EnumFacing side){
		if (side == EnumFacing.UP){
			return up;
		}
		else if (side == EnumFacing.DOWN){
			return down;
		}
		else if (side == EnumFacing.EAST){
			return east;
		}
		else if (side == EnumFacing.WEST){
			return west;
		}
		else if (side == EnumFacing.NORTH){
			return north;
		}
		else if (side == EnumFacing.SOUTH){
			return south;
		}
		return EnumPipeConnection.NONE;
	}
	
	public void setConnection(EnumFacing side, EnumPipeConnection connect){
		if (side == EnumFacing.UP){
			up = connect;
		}
		else if (side == EnumFacing.DOWN){
			down = connect;
		}
		else if (side == EnumFacing.EAST){
			east = connect;
		}
		else if (side == EnumFacing.WEST){
			west = connect;
		}
		else if (side == EnumFacing.NORTH){
			north = connect;
		}
		else if (side == EnumFacing.SOUTH){
			south = connect;
		}
	}
	
	public EnumPipeConnection getConnection(IBlockAccess world, BlockPos pos, EnumFacing side){
		if (getConnection(side) == EnumPipeConnection.FORCENONE){
			return EnumPipeConnection.FORCENONE;
		}
		if (world.getTileEntity(pos) instanceof TileEntityFluidPipe && !(world.getTileEntity(pos) instanceof TileEntityFluidExtractor)){
			return EnumPipeConnection.PIPE;
		}
		else if (world.getTileEntity(pos) != null){
			if (world.getTileEntity(pos).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Misc.getOppositeFace(side))){
				return EnumPipeConnection.BLOCK;
			}
		}
		return EnumPipeConnection.NONE;
	}
	
	public void reverseConnection(EnumFacing face){
	}
	
	public static EnumPipeConnection reverseForce(EnumPipeConnection connect){
		if (connect == EnumPipeConnection.FORCENONE){
			return EnumPipeConnection.NONE;
		}
		if (connect != EnumPipeConnection.NONE && connect != EnumPipeConnection.LEVER){
			return EnumPipeConnection.FORCENONE;
		}
		return EnumPipeConnection.NONE;
	}

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemTinkerHammer) {
			if (side == EnumFacing.UP || side == EnumFacing.DOWN) {
				if (Math.abs(hitX - 0.5) > Math.abs(hitZ - 0.5)) {
					if (hitX < 0.5) {
						this.west = reverseForce(west);
						this.reverseConnection(EnumFacing.WEST);
					} else {
						this.east = reverseForce(east);
						this.reverseConnection(EnumFacing.EAST);
					}
				} else {
					if (hitZ < 0.5) {
						this.north = reverseForce(north);
						this.reverseConnection(EnumFacing.NORTH);
					} else {
						this.south = reverseForce(south);
						this.reverseConnection(EnumFacing.SOUTH);
					}
				}
			}
			if (side == EnumFacing.EAST || side == EnumFacing.WEST) {
				if (Math.abs(hitY - 0.5) > Math.abs(hitZ - 0.5)) {
					if (hitY < 0.5) {
						this.down = reverseForce(down);
						this.reverseConnection(EnumFacing.DOWN);
					} else {
						this.up = reverseForce(up);
						this.reverseConnection(EnumFacing.UP);
					}
				} else {
					if (hitZ < 0.5) {
						this.north = reverseForce(north);
						this.reverseConnection(EnumFacing.NORTH);
					} else {
						this.south = reverseForce(south);
						this.reverseConnection(EnumFacing.SOUTH);
					}
				}
			}
			if (side == EnumFacing.NORTH || side == EnumFacing.SOUTH) {
				if (Math.abs(hitX - 0.5) > Math.abs(hitY - 0.5)) {
					if (hitX < 0.5) {
						this.west = reverseForce(west);
						this.reverseConnection(EnumFacing.WEST);
					} else {
						this.east = reverseForce(east);
						this.reverseConnection(EnumFacing.EAST);
					}
				} else {
					if (hitY < 0.5) {
						this.down = reverseForce(down);
						this.reverseConnection(EnumFacing.DOWN);
					} else {
						this.up = reverseForce(up);
						this.reverseConnection(EnumFacing.UP);
					}
				}
			}
			updateNeighbors(world);
			return true;
		}
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		world.setTileEntity(pos, null);
	}
	
	public boolean isConnected(EnumFacing face){
		TileEntity tile = getWorld().getTileEntity(getPos().offset(face));
		if (tile instanceof TileEntityFluidPipe){
			if (((TileEntityFluidPipe)tile).getConnection(Misc.getOppositeFace(face)) != EnumPipeConnection.FORCENONE
					&& ((TileEntityFluidPipe)tile).getConnection(Misc.getOppositeFace(face)) != EnumPipeConnection.NONE){
				return true;
			}
		}
		if (tile instanceof TileEntityFluidExtractor){
			if (((TileEntityFluidExtractor)tile).getConnection(Misc.getOppositeFace(face)) != EnumPipeConnection.FORCENONE
					&& ((TileEntityFluidExtractor)tile).getConnection(Misc.getOppositeFace(face)) != EnumPipeConnection.NONE){
				return true;
			}
		}
		if (getConnection(face) == EnumPipeConnection.BLOCK){
			return true;
		}
		return false;
	}

	@Override
	public void update() {
		if (tank.getFluid() != null){
			ArrayList<BlockPos> toUpdate = new ArrayList<BlockPos>();
			int distAmount = Math.min(tank.getFluidAmount(),120);
			ArrayList<EnumFacing> connectedFaces = getConnectedFaces();
			
			for (int i = 0; i < connectedFaces.size(); i ++){
				TileEntity t = getWorld().getTileEntity(getPos().offset(connectedFaces.get(i)));
				if (t instanceof TileEntityFluidExtractor && getWorld().isBlockIndirectlyGettingPowered(t.getPos()) > 0){
					connectedFaces.remove(i);
					i = Math.max(i-1, 0);
				}
			}
			
			int count = 0;
			if (connectedFaces.size() >= 1){
				for (int i = 0; i < connectedFaces.size(); i ++){
					TileEntity t = getWorld().getTileEntity(getPos().offset(connectedFaces.get(i)));
					if (t != null && tank.getFluid() != null){
						if (t.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Misc.getOppositeFace(connectedFaces.get(i)))){
							IFluidHandler handler = t.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, connectedFaces.get(i).getOpposite());
							if (handler != null && handler.fill(tank.getFluid(),false) > 0){
								count++;
							}
						}
					}
				}
			}
			if (count >= 1 && !world.isRemote){
				int toEach = Math.max(1, distAmount / count);
				for (int i = 0; i < connectedFaces.size(); i ++){
					TileEntity t = getWorld().getTileEntity(getPos().offset(connectedFaces.get(i)));
					if (t != null && toEach > 0 && tank.getFluid() != null){
						if (t.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, connectedFaces.get(i).getOpposite())){
							IFluidHandler handler = t.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, connectedFaces.get(i).getOpposite());
							if (handler != null){
								if (t instanceof TileEntityFluidPipe){
									((TileEntityFluidPipe) t).from.add(Misc.getOppositeFace(connectedFaces.get(i)));
								}
								FluidStack toAdd = tank.getFluid().copy();
								toAdd.amount = toEach;
								int filled = handler.fill(toAdd, true);
								tank.drainInternal(filled, true);
								if (!toUpdate.contains(getPos().offset(connectedFaces.get(i)))){
									toUpdate.add(getPos().offset(connectedFaces.get(i)));
								}
								if (!toUpdate.contains(getPos())){
									toUpdate.add(getPos());
								}
							}
						}
					}
				}
			}
			/*for (int i = 0; i < toUpdate.size(); i ++){
				TileEntity tile = getWorld().getTileEntity(toUpdate.get(i));
				tile.markDirty();
			}*/
		}
		from.clear();
	}

	private ArrayList<EnumFacing> getConnectedFaces() {
		ArrayList<EnumFacing> connectedFaces = new ArrayList<>();
		if (up == EnumPipeConnection.PIPE || up == EnumPipeConnection.BLOCK){
            if (isConnected(EnumFacing.UP) && !from.contains(EnumFacing.UP)){
                connectedFaces.add(EnumFacing.UP);
            }
        }
		if (down == EnumPipeConnection.PIPE || down == EnumPipeConnection.BLOCK){
            if (isConnected(EnumFacing.DOWN) && !from.contains(EnumFacing.DOWN)){
                connectedFaces.add(EnumFacing.DOWN);
            }
        }
		if (north == EnumPipeConnection.PIPE || north == EnumPipeConnection.BLOCK){
            if (isConnected(EnumFacing.NORTH) && !from.contains(EnumFacing.NORTH)){
                connectedFaces.add(EnumFacing.NORTH);
            }
        }
		if (south == EnumPipeConnection.PIPE || south == EnumPipeConnection.BLOCK){
            if (isConnected(EnumFacing.SOUTH) && !from.contains(EnumFacing.SOUTH)){
                connectedFaces.add(EnumFacing.SOUTH);
            }
        }
		if (west == EnumPipeConnection.PIPE || west == EnumPipeConnection.BLOCK){
            if (isConnected(EnumFacing.WEST) && !from.contains(EnumFacing.WEST)){
                connectedFaces.add(EnumFacing.WEST);
            }
        }
		if (east == EnumPipeConnection.PIPE || east == EnumPipeConnection.BLOCK){
            if (isConnected(EnumFacing.EAST) && !from.contains(EnumFacing.EAST)){
                connectedFaces.add(EnumFacing.EAST);
            }
        }
		return connectedFaces;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		Misc.syncTE(this);
	}
}
