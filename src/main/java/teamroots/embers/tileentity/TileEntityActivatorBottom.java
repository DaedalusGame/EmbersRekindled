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

public class TileEntityActivatorBottom extends TileEntity implements ITileEntityBase, ITickable {
	Random random = new Random();
	int progress = -1;
	public ItemStackHandler inventory = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
        	TileEntityActivatorBottom.this.markDirty();
        }
        
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate){
        	if (!stack.getItem().equals(RegistryManager.crystalEmber) && !stack.getItem().equals(RegistryManager.shardEmber)){
        		return stack;
        	}
        	return super.insertItem(slot, stack, simulate);
        }
	};
	
	public TileEntityActivatorBottom(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setTag("inventory", inventory.serializeNBT());
		tag.setInteger("progress", progress);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		inventory.deserializeNBT(tag.getCompoundTag("inventory"));
		if (tag.hasKey("progress")){
			progress = tag.getInteger("progress");
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
	public void update() {
		TileEntityActivatorTop top = (TileEntityActivatorTop)getWorld().getTileEntity(getPos().up());
		if (top != null){
			int i = random.nextInt(inventory.getSlots());
			if (inventory != null){
				if (inventory.getStackInSlot(i) != null){
					if (inventory.getStackInSlot(i).getItem() == RegistryManager.shardEmber){
						if (top.capability.getEmber() <= top.capability.getEmberCapacity()-750){
							top.capability.addAmount(750, true);
							inventory.extractItem(i, 1, false);
							markDirty();
							IBlockState state = getWorld().getBlockState(getPos());
							top.markDirty();
							state = getWorld().getBlockState(getPos().up());
							if (!getWorld().isRemote){
								PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this));
							}
							if (!getWorld().isRemote){
								PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(top));
							}
						}
					}
					else if (inventory.getStackInSlot(i).getItem() == RegistryManager.crystalEmber){
						if (top.capability.getEmber() <= top.capability.getEmberCapacity()-4500){
							top.capability.addAmount(4500, true);
							inventory.extractItem(i, 1, false);
							markDirty();
							IBlockState state = getWorld().getBlockState(getPos());
							top.markDirty();
							state = getWorld().getBlockState(getPos().up());
							if (!getWorld().isRemote){
								PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this));
							}
							if (!getWorld().isRemote){
								PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(top));
							}
						}
					}
				}
			}
		}
	}
}
