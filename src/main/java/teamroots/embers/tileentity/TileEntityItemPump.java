package teamroots.embers.tileentity;

import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockLever;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.tileentity.TileEntityItemPump.EnumPipeConnection;
import teamroots.embers.util.Misc;

public class TileEntityItemPump extends TileEntity implements ITileEntityBase, ITickable {
	public ItemStackHandler inventory = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
        	TileEntityItemPump.this.markDirty();
        }
	};
	public BlockPos lastReceived = new BlockPos(0,0,0);
	public int pressure = 16;
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
	
	public TileEntityItemPump(){
		super();
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
		tag.setTag("inventory", inventory.serializeNBT());
		tag.setInteger("lastX", this.lastReceived.getX());
		tag.setInteger("lastY", this.lastReceived.getY());
		tag.setInteger("lastZ", this.lastReceived.getZ());
		tag.setInteger("pressure", pressure);
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
		lastReceived = new BlockPos(tag.getInteger("lastX"),tag.getInteger("lastY"),tag.getInteger("lastZ"));
		pressure = tag.getInteger("pressure");
		inventory.deserializeNBT(tag.getCompoundTag("inventory"));
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
		super.hasCapability(capability, facing);
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return true;
		}
		return false;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return (T)this.inventory;
		}
		return super.getCapability(capability, facing);
	}
	
	public EnumPipeConnection getConnection(IBlockAccess world, BlockPos pos, EnumFacing side){
		if (world.getTileEntity(pos) instanceof TileEntityItemPipe && !(world.getTileEntity(pos) instanceof TileEntityItemPump)){
			return EnumPipeConnection.PIPE;
		}
		else if (world.getTileEntity(pos) != null && !(world.getTileEntity(pos) instanceof TileEntityItemPump)){
			if (world.getTileEntity(pos).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side)){
				return EnumPipeConnection.BLOCK;
			}
		}
		else if (world.getBlockState(pos).getBlock() == Blocks.LEVER){
			EnumFacing face = world.getBlockState(pos).getValue(BlockLever.FACING).getFacing();
			if (face == side || face == EnumFacing.DOWN && side == EnumFacing.UP || face == EnumFacing.UP && side == EnumFacing.DOWN){
				return EnumPipeConnection.LEVER;
			}
		}
		return EnumPipeConnection.NONE;
	}

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (player.isSneaking()){
			player.addChatMessage(new TextComponentString(lastReceived.toString()));
		}
		else if (inventory.getStackInSlot(0) != null){
			player.addChatMessage(new TextComponentString(""+inventory.getStackInSlot(0).stackSize));
		}
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, inventory);
		world.setTileEntity(pos, null);
	}

	@Override
	public void update() {
		if (getWorld().isBlockIndirectlyGettingPowered(getPos()) != 0){
			ArrayList<EnumFacing> connections = new ArrayList<EnumFacing>();
			if (up != EnumPipeConnection.NONE && up != EnumPipeConnection.LEVER){
				connections.add(EnumFacing.UP);
			}
			if (down != EnumPipeConnection.NONE && down != EnumPipeConnection.LEVER){
				connections.add(EnumFacing.DOWN);
			}
			if (north != EnumPipeConnection.NONE && north != EnumPipeConnection.LEVER){
				connections.add(EnumFacing.NORTH);
			}
			if (south != EnumPipeConnection.NONE && south != EnumPipeConnection.LEVER){
				connections.add(EnumFacing.SOUTH);
			}
			if (east != EnumPipeConnection.NONE && east != EnumPipeConnection.LEVER){
				connections.add(EnumFacing.EAST);
			}
			if (west != EnumPipeConnection.NONE && west != EnumPipeConnection.LEVER){
				connections.add(EnumFacing.WEST);
			}
			for (int i = 0; i < connections.size(); i ++){
				if ((getPos().offset(connections.get(i))).getX() == this.lastReceived.getX()
						&& (getPos().offset(connections.get(i))).getY() == this.lastReceived.getY()
						&& (getPos().offset(connections.get(i))).getZ() == this.lastReceived.getZ()){
					connections.remove(i);
					i = Math.max(0, i-1);
				}
			}

			ArrayList<EnumFacing> blockConnections = new ArrayList<EnumFacing>();
			if (up == EnumPipeConnection.BLOCK){
				blockConnections.add(EnumFacing.UP);
			}
			if (down == EnumPipeConnection.BLOCK){
				blockConnections.add(EnumFacing.DOWN);
			}
			if (north == EnumPipeConnection.BLOCK){
				blockConnections.add(EnumFacing.NORTH);
			}
			if (south == EnumPipeConnection.BLOCK){
				blockConnections.add(EnumFacing.SOUTH);
			}
			if (east == EnumPipeConnection.BLOCK){
				blockConnections.add(EnumFacing.EAST);
			}
			if (west == EnumPipeConnection.BLOCK){
				blockConnections.add(EnumFacing.WEST);
			}
			for (int k = 0; k < 1; k ++){
				boolean takenItems = false;
				for (int i = 0; i < blockConnections.size() && !takenItems; i ++){
					EnumFacing face = blockConnections.get(i);
					TileEntity tile = getWorld().getTileEntity(getPos().offset(face));
					if (tile != null){
						IItemHandler handler = getWorld().getTileEntity(getPos().offset(face)).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite());
						int slot = -1;
						for (int j = 0; j < handler.getSlots() && slot == -1; j ++){
							if (handler.getStackInSlot(j) != null){
								if (handler.getStackInSlot(j).stackSize > 0){
									slot = j;
								}
							}
						}
						if (slot != -1){
							if (this.inventory.getStackInSlot(0) == null){
								ItemStack extracted = handler.extractItem(slot, 1, false);
								this.inventory.insertItem(0, extracted, false);
								lastReceived = getPos().offset(face);
								IBlockState state = getWorld().getBlockState(getPos().offset(face));
								(tile).markDirty();
								getWorld().notifyBlockUpdate(getPos().offset(face), state, state, 3);
								state = getWorld().getBlockState(getPos());
								markDirty();
								getWorld().notifyBlockUpdate(getPos(), state, state, 3);
								takenItems = true;
							}
						}
					}
				}
				
				if (connections.size() > 0){
					for (int i = 0; i < pressure; i ++){
						if (inventory.getStackInSlot(0) != null){
							EnumFacing face = connections.get(random.nextInt(connections.size()));
							TileEntity tile = getWorld().getTileEntity(getPos().offset(face));
							if (tile instanceof TileEntityItemPipe){
								if (((TileEntityItemPipe)tile).pressure != Math.max(0, pressure-1)){
									((TileEntityItemPipe)tile).pressure = Math.max(0, pressure-1);
									IBlockState state = getWorld().getBlockState(getPos().offset(face));
									((TileEntityItemPipe)tile).markDirty();
									getWorld().notifyBlockUpdate(getPos().offset(face), state, state, 3);
								}
								IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite());
								ItemStack passStack = new ItemStack(inventory.getStackInSlot(0).getItem(),1,inventory.getStackInSlot(0).getMetadata());
								if (inventory.getStackInSlot(0).hasTagCompound()){
									passStack.setTagCompound(inventory.getStackInSlot(0).getTagCompound());
								}
								int slot = -1;
								for (int j = 0; j < handler.getSlots() && slot == -1; j ++){
									if (handler.getStackInSlot(j) == null){
										slot = j;
									}
									else {
										if (handler.getStackInSlot(j).stackSize < handler.getStackInSlot(j).getMaxStackSize() && ItemStack.areItemsEqual(handler.getStackInSlot(j), inventory.getStackInSlot(0)) && ItemStack.areItemStackTagsEqual(handler.getStackInSlot(j), inventory.getStackInSlot(0))){
											slot = j;
										}
									}
								}
								if (slot != -1){
									ItemStack added = handler.insertItem(slot, passStack, false);
									if (added == null){
										ItemStack extracted = this.inventory.extractItem(0, 1, false);
										if (extracted != null){
											if (tile instanceof TileEntityItemPipe){
												((TileEntityItemPipe)tile).pressure = Math.max(0, pressure-1);
												((TileEntityItemPipe)tile).lastReceived = getPos();
											}
											IBlockState state = getWorld().getBlockState(getPos().offset(face));
											(tile).markDirty();
											getWorld().notifyBlockUpdate(getPos().offset(face), state, state, 3);
											state = getWorld().getBlockState(getPos());
											markDirty();
											getWorld().notifyBlockUpdate(getPos(), state, state, 3);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
