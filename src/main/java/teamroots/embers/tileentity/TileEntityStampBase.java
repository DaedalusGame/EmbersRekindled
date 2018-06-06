package teamroots.embers.tileentity;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.EventManager;
import teamroots.embers.block.BlockStampBase;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.util.Misc;

public class TileEntityStampBase extends TileFluidHandler implements ITileEntityBase {
	public static int capacity = Fluid.BUCKET_VOLUME*1;
	public ItemStackHandler inputs = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
        	TileEntityStampBase.this.markDirty();
        }
	};
	
	public TileEntityStampBase(){
		super();
		tank = new FluidTank(capacity);
		tank.setTileEntity(this);
		tank.setCanFill(true);
		tank.setCanDrain(true);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	public EnumFacing getFacing(){
		return getWorld().getBlockState(getPos()).getValue(BlockStampBase.facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return (T)inputs;
		}
		return super.getCapability(capability, facing);
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
		if (!heldItem.isEmpty()){
			if (heldItem.getItem() instanceof ItemBucket || heldItem.getItem() instanceof UniversalBucket){
				boolean didFill = FluidUtil.interactWithFluidHandler(player, hand, this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side));
				this.markDirty();
				return didFill;
			}
			else {
				player.setHeldItem(hand, this.inputs.insertItem(0,heldItem,false));
				markDirty();
				return true;
			}
		}
		else {
			if (!inputs.getStackInSlot(0).isEmpty() && !world.isRemote){
				world.spawnEntity(new EntityItem(world,player.posX,player.posY,player.posZ,inputs.getStackInSlot(0)));
				inputs.setStackInSlot(0, ItemStack.EMPTY);
				markDirty();
				return true;
			}
		}
		return false;
	}
	
	public IFluidHandler getTank(){
		return tank;
	}
	
	public int getCapacity(){
		return tank.getCapacity();
	}
	
	public int getAmount(){
		return tank.getFluidAmount();
	}

	public Fluid getFluid(){
		if (tank.getFluid() != null){
			return tank.getFluid().getFluid();
		}
		return null;
	}

	public FluidStack getFluidStack() {
		return tank.getFluid();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setTag("inputs", inputs.serializeNBT());
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		inputs.deserializeNBT(tag.getCompoundTag("inputs"));
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, inputs);
		world.setTileEntity(pos, null);
	}
	
	public boolean dirty = false;
	
	@Override
	public void markForUpdate(){
		EventManager.markTEForUpdate(getPos(), this);
	}
	
	@Override
	public void markDirty(){
		markForUpdate();
		super.markDirty();
	}
}
