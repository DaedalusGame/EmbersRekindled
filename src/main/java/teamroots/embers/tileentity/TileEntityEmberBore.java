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
import net.minecraft.tileentity.TileEntityFurnace;
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
import teamroots.embers.network.message.MessageEmberData;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.util.EmberGenUtil;
import teamroots.embers.util.Misc;
import teamroots.embers.world.EmberWorldData;

public class TileEntityEmberBore extends TileEntity implements ITileEntityBase, ITickable, IMultiblockMachine {
	Random random = new Random();
	public long ticksExisted = 0;
	public float angle = 0;
	public int ticksFueled = 0;
	int stackShards = 0;
	int stackCrystals = 1;
	int stackFuel = 2;
	public ItemStackHandler inventory = new ItemStackHandler(3){
        @Override
        protected void onContentsChanged(int slot) {
        	TileEntityEmberBore.this.markDirty();
        }
        
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate){
        	if (slot == stackCrystals || slot == stackShards){
        		return insertItem(slot+1,stack,simulate);
        	}
        	if (slot == stackFuel && TileEntityFurnace.getItemBurnTime(stack) == 0){
        		return stack;
        	}
        	return super.insertItem(slot, stack, simulate);
        }
        
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate){
        	if (slot == stackFuel){
        		return ItemStack.EMPTY;
        	}
        	return super.extractItem(slot, amount, simulate);
        }
        
	};
	
	public TileEntityEmberBore(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setTag("inventory", inventory.serializeNBT());
		tag.setInteger("fueled", ticksFueled);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		inventory.deserializeNBT(tag.getCompoundTag("inventory"));
		ticksFueled = tag.getInteger("fueled");
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
			EnumFacing side, float hitX, float hitY, float hitZ) {
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, inventory);
		world.setBlockToAir(pos.add(1,0,0));
		world.setBlockToAir(pos.add(0,0,1));
		world.setBlockToAir(pos.add(-1,0,0));
		world.setBlockToAir(pos.add(0,0,-1));
		world.setBlockToAir(pos.add(1,0,-1));
		world.setBlockToAir(pos.add(-1,0,1));
		world.setBlockToAir(pos.add(1,0,1));
		world.setBlockToAir(pos.add(-1,0,-1));
		world.setTileEntity(pos, null);
	}

	@Override
	public void update() {
		if (ticksFueled > 0){
			angle += 12.0f;
		}
		if (getPos().getY() <= 7 && !getWorld().isRemote){
			ticksExisted ++;
			if (ticksFueled > 0){
				ticksFueled --;
			}
			if (ticksFueled == 0){
				if (inventory.getStackInSlot(stackFuel) != ItemStack.EMPTY){
					ticksFueled = TileEntityFurnace.getItemBurnTime(inventory.getStackInSlot(stackFuel).copy());
					inventory.getStackInSlot(stackFuel).shrink(1);
					if (inventory.getStackInSlot(stackFuel).getCount() <= 0){
						inventory.setStackInSlot(stackFuel, ItemStack.EMPTY);
					}
					markDirty();
				}
			}
			else if (ticksExisted % 100 == 0){
				if (random.nextFloat()*0.5f > EmberGenUtil.getEmberDensity(world.getSeed(), getPos().getX(), getPos().getZ())){
					int chance = random.nextInt(4);
					if (chance == 0){
						if (inventory.getStackInSlot(stackCrystals) != ItemStack.EMPTY){ 
							if (inventory.getStackInSlot(stackCrystals).getCount() < inventory.getStackInSlot(stackCrystals).getMaxStackSize()){
								inventory.getStackInSlot(stackCrystals).setCount(Math.min(64, inventory.getStackInSlot(stackCrystals).getCount()));
							}
						}
						else {
							inventory.setStackInSlot(stackCrystals, new ItemStack(RegistryManager.crystal_ember,1));
						}
						markDirty();
					}
					else {
						if (inventory.getStackInSlot(stackShards) != ItemStack.EMPTY){
							if (inventory.getStackInSlot(stackShards).getCount() < inventory.getStackInSlot(stackShards).getMaxStackSize()){
								inventory.getStackInSlot(stackShards).setCount(Math.min(inventory.getStackInSlot(stackShards).getMaxStackSize(), inventory.getStackInSlot(stackShards).getCount()));
							}
						}
						else {
							inventory.setStackInSlot(stackShards, new ItemStack(RegistryManager.shard_ember,1));
						}
						markDirty();
					}
				}
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
}
