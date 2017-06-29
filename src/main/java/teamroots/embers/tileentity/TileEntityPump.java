package teamroots.embers.tileentity;

import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import teamroots.embers.EventManager;
import teamroots.embers.RegistryManager;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.tileentity.TileEntityItemPump.EnumPipeConnection;
import teamroots.embers.util.Misc;

public class TileEntityPump extends TileEntityPipe implements ITileEntityBase, ITickable {
	Random random = new Random();
	
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
	
	public TileEntityPump(){
		super();
		tank.setCapacity(1000);
	}
	
	@Override
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
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		up = connectionFromInt(tag.getInteger("up"));
		down = connectionFromInt(tag.getInteger("down"));
		north = connectionFromInt(tag.getInteger("north"));
		south = connectionFromInt(tag.getInteger("south"));
		west = connectionFromInt(tag.getInteger("west"));
		east = connectionFromInt(tag.getInteger("east"));
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
	public EnumPipeConnection getConnection(IBlockAccess world, BlockPos pos, EnumFacing side){
		if (getConnection(side) == EnumPipeConnection.FORCENONE){
			return EnumPipeConnection.FORCENONE;
		}
		if (world.getTileEntity(pos) != null){
			if (world.getTileEntity(pos).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side) && !(world.getTileEntity(pos) instanceof TileEntityPump)){
				return EnumPipeConnection.BLOCK;
			}
		}
		if (world.getBlockState(pos).getBlock() == Blocks.LEVER){
			EnumFacing face = world.getBlockState(pos).getValue(BlockLever.FACING).getFacing();
			if (face == side || face == EnumFacing.DOWN && side == EnumFacing.UP || face == EnumFacing.UP && side == EnumFacing.DOWN){
				return EnumPipeConnection.LEVER;
			}
		}
		else if (world.getBlockState(pos).getBlock() == Blocks.STONE_BUTTON){
			EnumFacing face = world.getBlockState(pos).getValue(BlockButton.FACING);
			if (face == side){
				return EnumPipeConnection.LEVER;
			}
		}
		else if (world.getBlockState(pos).getBlock() == Blocks.REDSTONE_TORCH){
			EnumFacing face = world.getBlockState(pos).getValue(BlockRedstoneTorch.FACING);
			if (face == side){
				return EnumPipeConnection.LEVER;
			}
		}
		else if (world.getBlockState(pos).getBlock() == RegistryManager.caminite_lever){
			EnumFacing face = world.getBlockState(pos).getValue(BlockLever.FACING).getFacing();
			if (face == side || face == EnumFacing.DOWN && side == EnumFacing.UP || face == EnumFacing.UP && side == EnumFacing.DOWN){
				return EnumPipeConnection.LEVER;
			}
		}
		return EnumPipeConnection.NONE;
	}
	
	public boolean isConnected(EnumFacing face){
		TileEntity tile = getWorld().getTileEntity(getPos().offset(face));
		if (tile instanceof TileEntityPipe){
			if (((TileEntityPipe)tile).getConnection(Misc.getOppositeFace(face)) != EnumPipeConnection.FORCENONE
					&& ((TileEntityPipe)tile).getConnection(Misc.getOppositeFace(face)) != EnumPipeConnection.NONE){
				return true;
			}
		}
		if (tile instanceof TileEntityPump){
			if (((TileEntityPump)tile).getConnection(Misc.getOppositeFace(face)) != EnumPipeConnection.FORCENONE
					&& ((TileEntityPump)tile).getConnection(Misc.getOppositeFace(face)) != EnumPipeConnection.NONE){
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

		ArrayList<BlockPos> toUpdate = new ArrayList<BlockPos>();
		if (tank.getFluidAmount() < tank.getCapacity() && getWorld().isBlockIndirectlyGettingPowered(getPos()) != 0){
			ArrayList<EnumFacing> connectedFaces = new ArrayList<EnumFacing>();
			if (up == EnumPipeConnection.BLOCK){
				if (isConnected(EnumFacing.UP)){
					connectedFaces.add(EnumFacing.UP);
				}
			}
			if (down == EnumPipeConnection.BLOCK){
				if (isConnected(EnumFacing.DOWN)){
					connectedFaces.add(EnumFacing.DOWN);
				}
			}
			if (north == EnumPipeConnection.BLOCK){
				if (isConnected(EnumFacing.NORTH)){
					connectedFaces.add(EnumFacing.NORTH);
				}
			}
			if (south == EnumPipeConnection.BLOCK){
				if (isConnected(EnumFacing.SOUTH)){
					connectedFaces.add(EnumFacing.SOUTH);
				}
			}
			if (west == EnumPipeConnection.BLOCK){
				if (isConnected(EnumFacing.WEST)){
					connectedFaces.add(EnumFacing.WEST);
				}
			}
			if (east == EnumPipeConnection.BLOCK){
				if (isConnected(EnumFacing.EAST)){
					connectedFaces.add(EnumFacing.EAST);
				}
			}
			for (int i = 0; i < connectedFaces.size(); i ++){
				TileEntity t = getWorld().getTileEntity(getPos().offset(connectedFaces.get(i)));
				if (t != null){
					if (t.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)){
						IFluidHandler handler = getWorld().getTileEntity(getPos().offset(connectedFaces.get(i))).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, connectedFaces.get(i).getOpposite());
						if (handler != null){
							IFluidTankProperties[] properties = handler.getTankProperties();
							for (int j = 0; j < properties.length && tank.getFluidAmount() < tank.getCapacity(); j ++){
								FluidStack stack = properties[j].getContents();
								if (stack != null){
									int toFill = tank.fill(stack, false);
									FluidStack taken = handler.drain(new FluidStack(stack.getFluid(),toFill), true);
									tank.fill(taken, true);
									IBlockState state = getWorld().getBlockState(getPos());
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
			}
		}
		if (tank.getFluid() != null){
			int distAmount = tank.getFluidAmount();
			ArrayList<EnumFacing> connectedFaces = new ArrayList<EnumFacing>();
			if (up == EnumPipeConnection.PIPE || up == EnumPipeConnection.BLOCK){
				connectedFaces.add(EnumFacing.UP);
			}
			if (down == EnumPipeConnection.PIPE || down == EnumPipeConnection.BLOCK){
				connectedFaces.add(EnumFacing.DOWN);
			}
			if (north == EnumPipeConnection.PIPE || north == EnumPipeConnection.BLOCK){
				connectedFaces.add(EnumFacing.NORTH);
			}
			if (south == EnumPipeConnection.PIPE || south == EnumPipeConnection.BLOCK){
				connectedFaces.add(EnumFacing.SOUTH);
			}
			if (west == EnumPipeConnection.PIPE || west == EnumPipeConnection.BLOCK){
				connectedFaces.add(EnumFacing.WEST);
			}
			if (east == EnumPipeConnection.PIPE || east == EnumPipeConnection.BLOCK){
				connectedFaces.add(EnumFacing.EAST);
			}
			if (connectedFaces.size() > 0){
				int toEach = distAmount / connectedFaces.size();
				for (int i = 0; i < connectedFaces.size(); i ++){
					TileEntity t = getWorld().getTileEntity(getPos().offset(connectedFaces.get(i)));
					if (t != null){
						if (t.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)){
							IFluidHandler handler = t.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, connectedFaces.get(i).getOpposite());
							if (handler != null){
								if (tank.getFluid() != null){
									if (tank.getFluid().getFluid() != null){
										FluidStack toAdd = new FluidStack(tank.getFluid().getFluid(),toEach);
										int filled = handler.fill(toAdd, true);
										tank.drainInternal(new FluidStack(tank.getFluid().getFluid(),filled), true);
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
				}
			}
			if (tank.getFluidAmount() <= connectedFaces.size()){
				while (tank.getFluidAmount() > 0){
					int i = random.nextInt(connectedFaces.size());
					TileEntity t = getWorld().getTileEntity(getPos().offset(connectedFaces.get(i)));
					if (t != null){
						if (t.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)){
							IFluidHandler handler = t.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, connectedFaces.get(i).getOpposite());
							if (handler != null){
								int filled = handler.fill(new FluidStack(tank.getFluid().getFluid(),1), true);
								tank.drainInternal(new FluidStack(tank.getFluid().getFluid(),1), true);
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
		}
		for (int i = 0; i < toUpdate.size(); i ++){
			TileEntity tile = getWorld().getTileEntity(toUpdate.get(i));
			tile.markDirty();
			if (!getWorld().isRemote && !(tile instanceof ITileEntityBase)){
				EventManager.markTEForUpdate(toUpdate.get(i),tile);
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
}
