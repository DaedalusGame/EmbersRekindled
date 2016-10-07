package teamroots.embers.tileentity;

import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import teamroots.embers.tileentity.TileEntityPipe.EnumPipeConnection;

public class TileEntityPipe extends TileFluidHandler implements ITileEntityBase, ITickable {
	Random random = new Random();
	public static enum EnumPipeConnection{
		NONE, PIPE, BLOCK, LEVER
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
		}
		return EnumPipeConnection.NONE;
	}
	
	public EnumPipeConnection up = EnumPipeConnection.NONE, down = EnumPipeConnection.NONE, north = EnumPipeConnection.NONE, south = EnumPipeConnection.NONE, east = EnumPipeConnection.NONE, west = EnumPipeConnection.NONE;
	
	public TileEntityPipe(){
		super();
		tank.setCapacity(1000);
	}
	
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
	
	public EnumPipeConnection getConnection(IBlockAccess world, BlockPos pos, EnumFacing side){
		if (world.getTileEntity(pos) instanceof TileEntityPipe && !(world.getTileEntity(pos) instanceof TileEntityPump)){
			return EnumPipeConnection.PIPE;
		}
		else if (world.getTileEntity(pos) != null){
			if (world.getTileEntity(pos).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)){
				return EnumPipeConnection.BLOCK;
			}
		}
		return EnumPipeConnection.NONE;
	}

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		world.setTileEntity(pos, null);
	}

	@Override
	public void update() {
		if (tank.getFluid() != null){
			ArrayList<BlockPos> toUpdate = new ArrayList<BlockPos>();
			int distAmount = tank.getFluidAmount()/2;
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
				if (tank.getFluidAmount() <= connectedFaces.size() || tank.getFluidAmount() <= 8){
					while (tank.getFluidAmount() > 0){
						int i = random.nextInt(connectedFaces.size());
						if (getWorld().getTileEntity(getPos().offset(connectedFaces.get(i))) != null){
							IFluidHandler handler = getWorld().getTileEntity(getPos().offset(connectedFaces.get(i))).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, connectedFaces.get(i).getOpposite());
							int filled = handler.fill(new FluidStack(tank.getFluid().getFluid(),1), true);
							tank.drainInternal(new FluidStack(tank.getFluid().getFluid(),filled), true);
							IBlockState state = getWorld().getBlockState(getPos());
							markDirty();
							getWorld().notifyBlockUpdate(getPos(), state, state, 8);
							state = getWorld().getBlockState(getPos().offset(connectedFaces.get(i)));
							getWorld().getTileEntity(getPos().offset(connectedFaces.get(i))).markDirty();
							getWorld().notifyBlockUpdate(getPos().offset(connectedFaces.get(i)), state, state, 8);
						}
					}
				}
			}
			connectedFaces.clear();
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
			if (connectedFaces.size() > 0){
				for (int i = 0; i < connectedFaces.size() && tank.getFluidAmount() > 0; i ++){
					if (getWorld().getTileEntity(getPos().offset(connectedFaces.get(i))) != null){
						IFluidHandler handler = getWorld().getTileEntity(getPos().offset(connectedFaces.get(i))).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, connectedFaces.get(i).getOpposite());
						int filled = handler.fill(new FluidStack(tank.getFluid().getFluid(),1), true);
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
			for (int i = 0; i < toUpdate.size(); i ++){
				getWorld().getTileEntity(toUpdate.get(i)).markDirty();
				IBlockState state = getWorld().getBlockState(toUpdate.get(i));
				getWorld().notifyBlockUpdate(toUpdate.get(i), state, state, 3);
			}
		}
	}
}
