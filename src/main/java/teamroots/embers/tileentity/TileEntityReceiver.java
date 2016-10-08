package teamroots.embers.tileentity;

import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockLever;
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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import teamroots.embers.block.BlockEmberEmitter;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.power.IEmberPacketReceiver;

public class TileEntityReceiver extends TileEntity implements ITileEntityBase, ITickable, IEmberPacketReceiver {
	public IEmberCapability capability = new DefaultEmberCapability();
	Random random = new Random();
	long ticksExisted = 0;
	public TileEntityReceiver(){
		super();
		capability.setEmberCapacity(200);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		capability.writeToNBT(tag);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		capability.readFromNBT(tag);
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
	public void update() {
		this.ticksExisted ++;
		if (ticksExisted % 5 == 0 && getWorld().getTileEntity(getPos().offset(getWorld().getBlockState(getPos()).getValue(BlockEmberEmitter.facing),-1)) != null){
			if (getWorld().getTileEntity(getPos().offset(getWorld().getBlockState(getPos()).getValue(BlockEmberEmitter.facing),-1)).hasCapability(EmberCapabilityProvider.emberCapability, null)){
				IEmberCapability cap = getWorld().getTileEntity(getPos().offset(getWorld().getBlockState(getPos()).getValue(BlockEmberEmitter.facing),-1)).getCapability(EmberCapabilityProvider.emberCapability, null);
				if (cap.getEmber() < cap.getEmberCapacity() && capability.getEmber() > 0){
					double added = cap.addAmount(Math.min(10,capability.getEmber()), true);
					double removed = capability.removeAmount(added, true);
					markDirty();
					getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 8);
					BlockPos offset = getPos().offset(getWorld().getBlockState(getPos()).getValue(BlockEmberEmitter.facing),-1);
					getWorld().getTileEntity(offset).markDirty();
					getWorld().notifyBlockUpdate(offset,getWorld().getBlockState(offset),getWorld().getBlockState(offset),8);
				}
			}
		}
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		super.hasCapability(capability, facing);
		if (capability == EmberCapabilityProvider.emberCapability){
			return true;
		}
		return false;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		super.getCapability(capability, facing);
		if (capability == EmberCapabilityProvider.emberCapability){
			return (T)this.capability;
		}
		return (T)this.capability;
	}
	
	@Override
	public boolean isFull(){
		return capability.getEmber() >= capability.getEmberCapacity();
	}
}
