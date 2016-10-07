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
import teamroots.embers.entity.EntityEmberPacket;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.power.IEmberPacketProducer;
import teamroots.embers.power.IEmberPacketReceiver;
import teamroots.embers.tileentity.TileEntityEmitter.EnumConnection;
import teamroots.embers.tileentity.TileEntityPipe.EnumPipeConnection;

public class TileEntityEmitter extends TileEntity implements ITileEntityBase, ITickable, IEmberPacketProducer {
	public IEmberCapability capability = new DefaultEmberCapability();
	public BlockPos target = null;
	public long ticksExisted = 0;
	Random random = new Random();
	int offset = random.nextInt(40);
	public static enum EnumConnection{
		NONE, LEVER
	}
	
	public static EnumConnection connectionFromInt(int value){
		switch (value){
		case 0:
			return EnumConnection.NONE;
		case 1:
			return EnumConnection.LEVER;
		}
		return EnumConnection.NONE;
	}
	
	public EnumConnection up = EnumConnection.NONE, down = EnumConnection.NONE, north = EnumConnection.NONE, south = EnumConnection.NONE, east = EnumConnection.NONE, west = EnumConnection.NONE;
	
	public TileEntityEmitter(){
		super();
		capability.setEmberCapacity(200);
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
		if (target != null){
			tag.setInteger("targetX", target.getX());
			tag.setInteger("targetY", target.getY());
			tag.setInteger("targetZ", target.getZ());
		}
		capability.writeToNBT(tag);
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
		if (tag.hasKey("targetX")){
			target = new BlockPos(tag.getInteger("targetX"), tag.getInteger("targetY"), tag.getInteger("targetZ"));
		}
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
	
	public EnumConnection getConnection(IBlockAccess world, BlockPos pos, EnumFacing side){
		if (world.getBlockState(pos).getBlock() == Blocks.LEVER){
			EnumFacing face = world.getBlockState(pos).getValue(BlockLever.FACING).getFacing();
			if (face == side || face == EnumFacing.DOWN && side == EnumFacing.UP || face == EnumFacing.UP && side == EnumFacing.DOWN){
				return EnumConnection.LEVER;
			}
		}
		return EnumConnection.NONE;
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
				if (cap.getEmber() > 0 && capability.getEmber() < capability.getEmberCapacity()){
					double removed = cap.removeAmount(10, true);
					double added = capability.addAmount(removed, true);
					markDirty();
					getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 8);
					BlockPos offset = getPos().offset(getWorld().getBlockState(getPos()).getValue(BlockEmberEmitter.facing),-1);
					getWorld().getTileEntity(offset).markDirty();
					getWorld().notifyBlockUpdate(offset,getWorld().getBlockState(offset),getWorld().getBlockState(offset),8);
				}
			}
		}
		if ((this.ticksExisted+offset) % 20 == 0 && getWorld().isBlockIndirectlyGettingPowered(getPos()) != 0 && target != null && !getWorld().isRemote && this.capability.getEmber() > 10){
			if (getWorld().getTileEntity(target) instanceof IEmberPacketReceiver){
				if (!(((IEmberPacketReceiver)getWorld().getTileEntity(target)).isFull())){
					EntityEmberPacket packet = new EntityEmberPacket(getWorld());
					IBlockState state = getWorld().getBlockState(getPos());
					double vx = 0, vy = 0, vz = 0;
		
					if (state.getValue(BlockEmberEmitter.facing) == EnumFacing.UP){
						vy = 0.5;
					}
					if (state.getValue(BlockEmberEmitter.facing) == EnumFacing.DOWN){
						vy = -0.5;
					}
					if (state.getValue(BlockEmberEmitter.facing) == EnumFacing.NORTH){
						vz = -0.5;
						vy = -0.01;
					}
					if (state.getValue(BlockEmberEmitter.facing) == EnumFacing.SOUTH){
						vz = 0.5;
						vy = -0.01;
					}
					if (state.getValue(BlockEmberEmitter.facing) == EnumFacing.WEST){
						vx = -0.5;
						vy = -0.01;
					}
					if (state.getValue(BlockEmberEmitter.facing) == EnumFacing.EAST){
						vx = 0.5;
						vy = -0.01;
					}
					
					packet.initCustom(getPos(), target, vx, vy, vz, Math.min(40.0,capability.getEmber()));
					this.capability.removeAmount(Math.min(40.0,capability.getEmber()), true);
					getWorld().spawnEntityInWorld(packet);
					markDirty();
					getWorld().notifyBlockUpdate(getPos(), state, state, 8);
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
	public void setTargetPosition(BlockPos pos) {
		target = pos;
		markDirty();
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(pos), getWorld().getBlockState(pos), 3);
	}
}
