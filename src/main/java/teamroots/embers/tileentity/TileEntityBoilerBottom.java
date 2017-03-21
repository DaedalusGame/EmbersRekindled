package teamroots.embers.tileentity;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBucket;
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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.FluidEvent.FluidSpilledEvent;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.RegistryManager;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageEmberActivationFX;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.util.EmberGenUtil;
import teamroots.embers.util.Misc;

public class TileEntityBoilerBottom extends TileFluidHandler implements ITileEntityBase, ITickable {
	public static int capacity = Fluid.BUCKET_VOLUME*8;
	Random random = new Random();
	int progress = -1;
	public ItemStackHandler inventory = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
        	TileEntityBoilerBottom.this.markDirty();
        }
        
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate){
        	if (EmberGenUtil.getEmberForItem(stack.getItem()) == 0){
        		return stack;
        	}
        	return super.insertItem(slot, stack, simulate);
        }
	};
	
	public TileEntityBoilerBottom(){
		super();
		tank = new FluidTank(capacity);
		tank.setTileEntity(this);
		tank.setCanFill(true);
		tank.setCanDrain(true);
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
			EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		if (heldItem.getItem() instanceof ItemBucket || heldItem.getItem() instanceof UniversalBucket){
			FluidActionResult didFill = FluidUtil.interactWithFluidHandler(heldItem, this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side), player);
			player.setHeldItem(hand, didFill.getResult());
			this.markDirty();
			return didFill.success;
		}
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		Misc.spawnInventoryInWorld(getWorld(), pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, inventory);
		world.setTileEntity(pos, null);
	}
	
	public float getMultiplier(){
		float metalMultiplier = EmberGenUtil.getMetalCoefficient(world.getBlockState(pos.down()));
		float totalMult = 1.5f;
		IBlockState north = world.getBlockState(pos.down().north());
		IBlockState south = world.getBlockState(pos.down().south());
		IBlockState east = world.getBlockState(pos.down().east());
		IBlockState west = world.getBlockState(pos.down().west());
		if (north.getBlock() == Blocks.LAVA || north.getBlock() == Blocks.FLOWING_LAVA || north.getBlock() == Blocks.FIRE){
			totalMult += 0.375f*metalMultiplier;
		}
		if (south.getBlock() == Blocks.LAVA || south.getBlock() == Blocks.FLOWING_LAVA || south.getBlock() == Blocks.FIRE){
			totalMult += 0.375f*metalMultiplier;
		}
		if (east.getBlock() == Blocks.LAVA || east.getBlock() == Blocks.FLOWING_LAVA || east.getBlock() == Blocks.FIRE){
			totalMult += 0.375f*metalMultiplier;
		}
		if (west.getBlock() == Blocks.LAVA || west.getBlock() == Blocks.FLOWING_LAVA || west.getBlock() == Blocks.FIRE){
			totalMult += 0.375f*metalMultiplier;
		}
		return totalMult;
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
		TileEntity tile = getWorld().getTileEntity(getPos().up());
		if (!inventory.getStackInSlot(0).isEmpty()){
			if (tank.getFluid() != null){
				if (tank.getFluid().getFluid() == FluidRegistry.WATER && tank.getFluidAmount() > 25){
					if (tile instanceof TileEntityBoilerTop){
						TileEntityBoilerTop top = (TileEntityBoilerTop)tile;
						if (top != null){
							progress ++;
							if (progress > 20){
								progress = 0;
								int i = random.nextInt(inventory.getSlots());
								if (inventory != null){
									if (inventory.getStackInSlot(i) != ItemStack.EMPTY){
										if (EmberGenUtil.getEmberForItem(inventory.getStackInSlot(i).getItem()) > 0){
											double ember = EmberGenUtil.getEmberForItem(inventory.getStackInSlot(i).getItem())*getMultiplier();
											if (top.capability.getEmber() <= top.capability.getEmberCapacity()-ember){
												if (!world.isRemote){
													PacketHandler.INSTANCE.sendToAll(new MessageEmberActivationFX(getPos().getX()+0.5f,getPos().getY()+1.5f,getPos().getZ()+0.5f));
												}
												top.capability.addAmount(ember, true);
												inventory.extractItem(i, 1, false);
												markDirty();
												IBlockState state = getWorld().getBlockState(getPos());
												top.markDirty();
												state = getWorld().getBlockState(getPos().up());
											}
										}
									}
								}
							}
							markDirty();
						}
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
}
