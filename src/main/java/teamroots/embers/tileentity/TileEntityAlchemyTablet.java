package teamroots.embers.tileentity;

import java.util.ArrayList;
import java.util.Comparator;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.util.Misc;

public class TileEntityAlchemyTablet extends TileEntity implements ITileEntityBase, ITickable {
	public IEmberCapability capability = new DefaultEmberCapability();
	int angle = 0;
	int turnRate = 0;
	int progress = 0;
	int ash = 0;
	public ItemStackHandler inventory = new ItemStackHandler(9){
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
        	TileEntityAlchemyTablet.this.markDirty();
        	PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(TileEntityAlchemyTablet.this));
        }
        
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate){
        	if (slot > 8){
        		int realSlot = slot-9;
        		return super.insertItem(realSlot, stack, simulate);
        	}
        	ArrayList<Integer> possible = new ArrayList<Integer>();
        	for (int i = 0; i < getSlots(); i ++){
        		if (getStackInSlot(i) != null){
        			if (ItemStack.areItemStacksEqual(stack, getStackInSlot(i))){
        				possible.add(i);
        			}
        		}
        	}
        	possible.sort(new ComparatorStackSize());
        	if (possible.size() > 0){
        		return super.insertItem(possible.get(0), stack, simulate);
        	}
        	return super.insertItem(slot, stack, simulate);
        }
        
        @Override
        public void validateSlotIndex(int slot){
            if (slot < 0)
                throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.length + ")");
        }
        
        class ComparatorStackSize implements Comparator<Integer> {
			@Override
			public int compare(Integer arg0, Integer arg1) {
				if (getStackInSlot(arg0) != null && getStackInSlot(arg1) != null){
					return getStackInSlot(arg0).stackSize - getStackInSlot(arg1).stackSize;
				}
				else if (getStackInSlot(arg0) == null){
					return -65;
				}
				else if (getStackInSlot(arg1) == null){
					return 65;
				}
				return 0;
			}
        	
        }
	};
	Random random = new Random();
	
	public TileEntityAlchemyTablet(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setInteger("progress", 0);
		tag.setTag("inventory", inventory.serializeNBT());
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		ash = tag.getInteger("ash");
		progress = tag.getInteger("progress");
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
	
	public int getSlotForPos(float hitX, float hitZ){
		return ((int)(hitX/0.3333))*3 + ((int)(hitZ/0.3333));
	}

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		int slot = getSlotForPos(hitX,hitZ);
		if (heldItem != null){
			player.setHeldItem(hand, this.inventory.insertItem(slot+9,heldItem,false));
			markDirty();
			if (!getWorld().isRemote){
				PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this));
			}
			return true;
		}
		else {
			if (inventory.getStackInSlot(slot) != null){
				if (!getWorld().isRemote){
					player.setHeldItem(hand, inventory.extractItem(slot, inventory.getStackInSlot(slot).stackSize, false));
					markDirty();
					if (!getWorld().isRemote){
						PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this));
					}
				}
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

	@Override
	public void update() {
		turnRate = 1;
		angle += turnRate;
	}
}
