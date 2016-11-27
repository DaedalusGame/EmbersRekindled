package teamroots.embers.tileentity;

import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockLever;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;

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
		}
		return EnumPipeConnection.NONE;
	}
	
	public TileEntityPump(){
		super();
		tank.setCapacity(1000);
	}
	
	@Override
	public void updateNeighbors(IBlockAccess world){
		up = getConnection(world,getPos().up(),EnumFacing.DOWN);
		down = getConnection(world,getPos().down(),EnumFacing.UP);
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
	
	@Override
	public EnumPipeConnection getConnection(IBlockAccess world, BlockPos pos, EnumFacing side){
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
		return EnumPipeConnection.NONE;
	}

	@Override
	public void update() {

		ArrayList<BlockPos> toUpdate = new ArrayList<BlockPos>();
		if (tank.getFluidAmount() < tank.getCapacity() && getWorld().isBlockIndirectlyGettingPowered(getPos()) != 0){
			ArrayList<EnumFacing> connectedFaces = new ArrayList<EnumFacing>();
			if (up == EnumPipeConnection.BLOCK){
				connectedFaces.add(EnumFacing.UP);
			}
			if (down == EnumPipeConnection.BLOCK){
				connectedFaces.add(EnumFacing.DOWN);
			}
			if (north == EnumPipeConnection.BLOCK){
				connectedFaces.add(EnumFacing.NORTH);
			}
			if (south == EnumPipeConnection.BLOCK){
				connectedFaces.add(EnumFacing.SOUTH);
			}
			if (west == EnumPipeConnection.BLOCK){
				connectedFaces.add(EnumFacing.WEST);
			}
			if (east == EnumPipeConnection.BLOCK){
				connectedFaces.add(EnumFacing.EAST);
			}
			for (int i = 0; i < connectedFaces.size(); i ++){
				if (getWorld().getTileEntity(getPos().offset(connectedFaces.get(i))) != null){
					IFluidHandler handler = getWorld().getTileEntity(getPos().offset(connectedFaces.get(i))).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, connectedFaces.get(i).getOpposite());
					if (handler != null){
						IFluidTankProperties[] properties = handler.getTankProperties();
						for (int j = 0; j < properties.length && tank.getFluidAmount() < tank.getCapacity(); j ++){
							FluidStack stack = properties[j].getContents();
							if (stack != null){
								int taken = tank.fill(stack, true);
								handler.drain(new FluidStack(stack.getFluid(),taken), true);
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
					if (getWorld().getTileEntity(getPos().offset(connectedFaces.get(i))) != null){
						IFluidHandler handler = getWorld().getTileEntity(getPos().offset(connectedFaces.get(i))).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, connectedFaces.get(i).getOpposite());
						if (handler != null){
							if (tank.getFluid() != null){
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
			if (tank.getFluidAmount() <= connectedFaces.size()){
				while (tank.getFluidAmount() > 0){
					int i = random.nextInt(connectedFaces.size());
					if (getWorld().getTileEntity(getPos().offset(connectedFaces.get(i))) != null){
						IFluidHandler handler = getWorld().getTileEntity(getPos().offset(connectedFaces.get(i))).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, connectedFaces.get(i).getOpposite());
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
		for (int i = 0; i < toUpdate.size(); i ++){
			getWorld().getTileEntity(toUpdate.get(i)).markDirty();
			if (!getWorld().isRemote){
				PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this));
			}
		}
	}
}
