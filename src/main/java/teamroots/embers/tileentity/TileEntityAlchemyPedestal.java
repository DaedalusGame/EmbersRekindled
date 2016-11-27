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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.RegistryManager;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.util.Misc;

public class TileEntityAlchemyPedestal extends TileEntity implements ITileEntityBase, ITickable {
	int angle = 0;
	int turnRate = 0;
	int progress = 0;
	int ash = 0;
	int stackAsh = 0;
	int stackItem = 1;
	public ItemStackHandler inventory = new ItemStackHandler(2){
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
        	TileEntityAlchemyPedestal.this.markDirty();
        	PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(TileEntityAlchemyPedestal.this));
        }
        
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate){
        	if (slot == stackAsh && stack.getItem() != RegistryManager.dustAsh){
        		return insertItem(slot+1,stack,simulate);
        	}
        	return super.insertItem(slot, stack, simulate);
        }
	};
	Random random = new Random();
	
	public TileEntityAlchemyPedestal(){
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

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (heldItem != null){
			if (heldItem.getItem() == RegistryManager.dustAsh){
				player.setHeldItem(hand, this.inventory.insertItem(0,heldItem,false));
				markDirty();
				if (!getWorld().isRemote){
					PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this));
				}
			}
			else {
				player.setHeldItem(hand, this.inventory.insertItem(1,heldItem,false));
				markDirty();
				if (!getWorld().isRemote){
					PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this));
				}
			}
			return true;
		}
		else {
			if (inventory.getStackInSlot(1) != null){
				if (!getWorld().isRemote){
					player.setHeldItem(hand, inventory.extractItem(1, inventory.getStackInSlot(1).stackSize, false));
					markDirty();
					if (!getWorld().isRemote){
						PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this));
					}
				}
				return true;
			}
			else if (inventory.getStackInSlot(0) != null){
				if (!getWorld().isRemote){
					player.setHeldItem(hand, inventory.extractItem(0, inventory.getStackInSlot(0).stackSize, false));
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
