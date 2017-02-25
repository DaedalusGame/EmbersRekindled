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
import teamroots.embers.block.BlockItemTransfer;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.util.Misc;

public class TileEntityItemTransfer extends TileEntity implements ITileEntityBase, ITickable, IPressurizable, IItemPipePriority {
	double angle = 0;
	double turnRate = 1;
	public ItemStackHandler inventory = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
        	TileEntityItemTransfer.this.markDirty();
        }
        
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate){
        	if (!filterItem.isEmpty()){
        		if (!stack.isEmpty()){
        			if (filterItem.getItem() == stack.getItem() && filterItem.getItemDamage() == stack.getItemDamage()){
            			return super.insertItem(slot, stack, simulate);	
        			}
        		}
        		return stack;
        	}
        	return super.insertItem(slot, stack, simulate);
        }
	};
	public ItemStack filterItem = ItemStack.EMPTY;
	public BlockPos lastReceived = new BlockPos(0,0,0);
	public int pressure = 0;
	Random random = new Random();
	
	public TileEntityItemTransfer(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setTag("inventory", inventory.serializeNBT());
		if (!filterItem.isEmpty()){
			tag.setTag("filter", filterItem.writeToNBT(new NBTTagCompound()));
		}
		tag.setInteger("lastX", this.lastReceived.getX());
		tag.setInteger("lastY", this.lastReceived.getY());
		tag.setInteger("lastZ", this.lastReceived.getZ());
		tag.setInteger("pressure", pressure);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		if (tag.hasKey("filter")){
			filterItem = new ItemStack(tag.getCompoundTag("filter"));
		}
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
			IBlockState state = getWorld().getBlockState(getPos());
			if (facing == state.getValue(BlockItemTransfer.facing) || facing == Misc.getOppositeFace(state.getValue(BlockItemTransfer.facing))){
				return true;
			}
			return false;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			IBlockState state = getWorld().getBlockState(getPos());
			if (facing == state.getValue(BlockItemTransfer.facing) || facing == Misc.getOppositeFace(state.getValue(BlockItemTransfer.facing))){
				return (T)this.inventory;
			}
			return null;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		if (heldItem != ItemStack.EMPTY){
			this.filterItem = heldItem.copy();
			markDirty();
			world.setBlockState(pos, state.withProperty(BlockItemTransfer.filter, true), 8);
			world.notifyBlockUpdate(pos, state, state.withProperty(BlockItemTransfer.filter, true), 8);
			return true;
		}
		else {
			this.filterItem = ItemStack.EMPTY;
			markDirty();
			world.setBlockState(pos, state.withProperty(BlockItemTransfer.filter, false), 8);
			world.notifyBlockUpdate(pos, state, state.withProperty(BlockItemTransfer.filter, false), 8);
			return true;
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, inventory);
		world.setTileEntity(pos, null);
	}

	@Override
	public void update() {
		angle += turnRate;
		IBlockState state = getWorld().getBlockState(getPos());
		ArrayList<BlockPos> toUpdate = new ArrayList<BlockPos>();
		ArrayList<EnumFacing> connections = new ArrayList<EnumFacing>();
		connections.add(state.getValue(BlockItemTransfer.facing));
		if (connections.size() > 0){
			for (int i = 0; i < 1; i ++){
				if (!inventory.getStackInSlot(0).isEmpty()){
					EnumFacing face = connections.get(random.nextInt(connections.size()));
					TileEntity tile = getWorld().getTileEntity(getPos().offset(face));
					if (tile != null){
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
		for (int i = 0; i < toUpdate.size(); i ++){
			TileEntity tile = getWorld().getTileEntity(toUpdate.get(i));
			tile.markDirty();
			if (!getWorld().isRemote && !(tile instanceof ITileEntityBase)){
				EventManager.toUpdate.add(tile);
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

	@Override
	public int getPriority() {
		return 1;
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
