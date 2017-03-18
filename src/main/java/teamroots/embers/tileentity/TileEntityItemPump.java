package teamroots.embers.tileentity;

import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.EventManager;
import teamroots.embers.item.ItemTinkerHammer;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.tileentity.TileEntityItemPipe.EnumPipeConnection;
import teamroots.embers.util.Misc;

public class TileEntityItemPump extends TileEntity implements ITileEntityBase, ITickable, IPressurizable {
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
		NONE, PIPE, BLOCK, LEVER, FORCENONE, NEIGHBORNONE
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
	
	public TileEntityItemPump(){
		super();
	}
	
	public void updateNeighbors(IBlockAccess world){
		up = getConnection(world,getPos().offset(EnumFacing.UP),EnumFacing.UP);
		down = getConnection(world,getPos().offset(EnumFacing.DOWN),EnumFacing.DOWN);
		north = getConnection(world,getPos().offset(EnumFacing.NORTH),EnumFacing.NORTH);
		south = getConnection(world,getPos().offset(EnumFacing.SOUTH),EnumFacing.SOUTH);
		west = getConnection(world,getPos().offset(EnumFacing.WEST),EnumFacing.WEST);
		east = getConnection(world,getPos().offset(EnumFacing.EAST),EnumFacing.EAST);
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
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return (T)this.inventory;
		}
		return super.getCapability(capability, facing);
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
		return EnumPipeConnection.NONE;
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
	
	public void reverseConnection(EnumFacing face){
	}

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		if (heldItem != ItemStack.EMPTY){
			if (heldItem.getItem() instanceof ItemTinkerHammer){
				if (side == EnumFacing.UP || side == EnumFacing.DOWN){
					if (Math.abs(hitX-0.5) > Math.abs(hitZ-0.5)){
						if (hitX < 0.5){
							this.west = reverseForce(west);
							this.reverseConnection(EnumFacing.WEST);
						}
						else {
							this.east = reverseForce(east);
							this.reverseConnection(EnumFacing.EAST);
						}
					}
					else {
						if (hitZ < 0.5){
							this.north = reverseForce(north);
							this.reverseConnection(EnumFacing.NORTH);
						}
						else {
							this.south = reverseForce(south);
							this.reverseConnection(EnumFacing.SOUTH);
						}
					}
				}
				if (side == EnumFacing.EAST || side == EnumFacing.WEST){
					if (Math.abs(hitY-0.5) > Math.abs(hitZ-0.5)){
						if (hitY < 0.5){
							this.down = reverseForce(down);
							this.reverseConnection(EnumFacing.DOWN);
						}
						else {
							this.up = reverseForce(up);
							this.reverseConnection(EnumFacing.UP);
						}
					}
					else {
						if (hitZ < 0.5){
							this.north = reverseForce(north);
							this.reverseConnection(EnumFacing.NORTH);
						}
						else {
							this.south = reverseForce(south);
							this.reverseConnection(EnumFacing.SOUTH);
						}
					}
				}
				if (side == EnumFacing.NORTH || side == EnumFacing.SOUTH){
					if (Math.abs(hitX-0.5) > Math.abs(hitY-0.5)){
						if (hitX < 0.5){
							this.west = reverseForce(west);
							this.reverseConnection(EnumFacing.WEST);
						}
						else {
							this.east = reverseForce(east);
							this.reverseConnection(EnumFacing.EAST);
						}
					}
					else {
						if (hitY < 0.5){
							this.down = reverseForce(down);
							this.reverseConnection(EnumFacing.DOWN);
						}
						else {
							this.up = reverseForce(up);
							this.reverseConnection(EnumFacing.UP);
						}
					}
				}
				updateNeighbors(world);
				markDirty();
				return true;
			}
		}
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, inventory);
		world.setTileEntity(pos, null);
	}
	
	public boolean isConnected(EnumFacing face){
		TileEntity tile = getWorld().getTileEntity(getPos().offset(face));
		if (tile instanceof TileEntityItemPipe){
			if (((TileEntityItemPipe)tile).getConnection(Misc.getOppositeFace(face)) != TileEntityItemPipe.EnumPipeConnection.FORCENONE
					&& ((TileEntityItemPipe)tile).getConnection(Misc.getOppositeFace(face)) != TileEntityItemPipe.EnumPipeConnection.NONE){
				return true;
			}
		}
		if (tile instanceof TileEntityItemPump){
			if (((TileEntityItemPump)tile).getConnection(Misc.getOppositeFace(face)) != EnumPipeConnection.FORCENONE
					&& ((TileEntityItemPump)tile).getConnection(Misc.getOppositeFace(face)) != EnumPipeConnection.NONE){
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
		if (getWorld().isBlockIndirectlyGettingPowered(getPos()) != 0 && !world.isRemote){
			ArrayList<BlockPos> toUpdate = new ArrayList<BlockPos>();
			ArrayList<EnumFacing> connections = new ArrayList<EnumFacing>();
			if (up != EnumPipeConnection.NONE && up != EnumPipeConnection.FORCENONE && up != EnumPipeConnection.LEVER && isConnected(EnumFacing.UP)){
				connections.add(EnumFacing.UP);
			}
			if (down != EnumPipeConnection.NONE && down != EnumPipeConnection.FORCENONE && down != EnumPipeConnection.LEVER && isConnected(EnumFacing.DOWN)){
				connections.add(EnumFacing.DOWN);
			}
			if (north != EnumPipeConnection.NONE && north != EnumPipeConnection.FORCENONE && north != EnumPipeConnection.LEVER && isConnected(EnumFacing.NORTH)){
				connections.add(EnumFacing.NORTH);
			}
			if (south != EnumPipeConnection.NONE && south != EnumPipeConnection.FORCENONE && south != EnumPipeConnection.LEVER && isConnected(EnumFacing.SOUTH)){
				connections.add(EnumFacing.SOUTH);
			}
			if (east != EnumPipeConnection.NONE && east != EnumPipeConnection.FORCENONE && east != EnumPipeConnection.LEVER && isConnected(EnumFacing.EAST)){
				connections.add(EnumFacing.EAST);
			}
			if (west != EnumPipeConnection.NONE && west != EnumPipeConnection.FORCENONE && west != EnumPipeConnection.LEVER && isConnected(EnumFacing.WEST)){
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
						if (handler != null){
							int slot = -1;
							for (int j = 0; j < handler.getSlots() && slot == -1; j ++){
								if (!handler.getStackInSlot(j).isEmpty()){
									if (handler.getStackInSlot(j).getCount() > 0){
										slot = j;
									}
								}
							}
							if (slot != -1){
								if (this.inventory.getStackInSlot(0).isEmpty()){
									ItemStack extracted = handler.extractItem(slot, 1, false);
									this.inventory.insertItem(0, extracted, false);
									lastReceived = getPos().offset(face);
									if (!toUpdate.contains(getPos().offset(face))){
										toUpdate.add(getPos().offset(face));
									}
									if (!toUpdate.contains(getPos())){
										toUpdate.add(getPos());
									}
									takenItems = true;
								}
							}
						}
					}
				}
				
				if (connections.size() > 0){
					for (int i = 0; i < 1; i ++){
						if (!inventory.getStackInSlot(0).isEmpty()){
							EnumFacing face = connections.get(random.nextInt(connections.size()));
							TileEntity tile = getWorld().getTileEntity(getPos().offset(face));
							if (tile instanceof TileEntityItemPipe){
								if (((TileEntityItemPipe)tile).pressure != Math.max(0, pressure-1)){
									((TileEntityItemPipe)tile).pressure = Math.max(0, pressure-1);
									IBlockState state = getWorld().getBlockState(getPos().offset(face));
									toUpdate.add(pos.offset(face));
								}
								IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite());
								if (handler != null){
									ItemStack passStack = new ItemStack(inventory.getStackInSlot(0).getItem(),1,inventory.getStackInSlot(0).getMetadata());
									if (inventory.getStackInSlot(0).hasTagCompound()){
										passStack.setTagCompound(inventory.getStackInSlot(0).getTagCompound());
									}
									int slot = -1;
									for (int j = 0; j < handler.getSlots() && slot == -1; j ++){
										if (handler.getStackInSlot(j).isEmpty()){
											slot = j;
										}
										else {
											if (handler.getStackInSlot(j).getCount() < handler.getStackInSlot(j).getMaxStackSize() && ItemStack.areItemsEqual(handler.getStackInSlot(j), inventory.getStackInSlot(0)) && ItemStack.areItemStackTagsEqual(handler.getStackInSlot(j), inventory.getStackInSlot(0))){
												slot = j;
											}
										}
									}
									if (slot != -1){
										ItemStack added = handler.insertItem(slot, passStack, false);
										if (added.isEmpty()){
											ItemStack extracted = this.inventory.extractItem(0, 1, false);
											if (!extracted.isEmpty()){
												if (tile instanceof TileEntityItemPipe){
													((TileEntityItemPipe)tile).lastReceived = getPos();
												}
												if (!toUpdate.contains(getPos().offset(face))){
													toUpdate.add(getPos().offset(face));
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
	}

	@Override
	public int getPressure() {
		return pressure;
	}

	@Override
	public void setPressure(int pressure) {
		this.pressure = pressure;
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
