package teamroots.embers.tileentity;

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
import teamroots.embers.EventManager;
import teamroots.embers.block.BlockEmberEmitter;
import teamroots.embers.block.BlockEmberPulser;
import teamroots.embers.entity.EntityEmberPacket;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.power.IEmberPacketProducer;
import teamroots.embers.power.IEmberPacketReceiver;
import teamroots.embers.tileentity.TileEntityItemPump.EnumPipeConnection;
import teamroots.embers.util.Misc;

public class TileEntityPulser extends TileEntity implements ITileEntityBase, ITickable, IEmberPacketProducer {
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
	
	public TileEntityPulser(){
		super();
		capability.setEmberCapacity(2000);
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
		else if (world.getBlockState(pos).getBlock() == Blocks.STONE_BUTTON){
			EnumFacing face = world.getBlockState(pos).getValue(BlockButton.FACING);
			if (face == Misc.getOppositeVerticalFace(side)){
				return EnumConnection.LEVER;
			}
		}
		else if (world.getBlockState(pos).getBlock() == Blocks.REDSTONE_TORCH){
			EnumFacing face = world.getBlockState(pos).getValue(BlockRedstoneTorch.FACING);
			if (face == Misc.getOppositeVerticalFace(side)){
				return EnumConnection.LEVER;
			}
		}
		return EnumConnection.NONE;
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
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
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
		if (ticksExisted % 5 == 0 && getWorld().getTileEntity(getPos().offset(getWorld().getBlockState(getPos()).getValue(BlockEmberPulser.facing),-1)) != null){
			if (getWorld().getTileEntity(getPos().offset(getWorld().getBlockState(getPos()).getValue(BlockEmberPulser.facing),-1)).hasCapability(EmberCapabilityProvider.emberCapability, null)){
				IEmberCapability cap = getWorld().getTileEntity(getPos().offset(getWorld().getBlockState(getPos()).getValue(BlockEmberPulser.facing),-1)).getCapability(EmberCapabilityProvider.emberCapability, null);
				if (cap.getEmber() > 0 && capability.getEmber() < capability.getEmberCapacity()){
					double removed = cap.removeAmount(100, true);
					double added = capability.addAmount(removed, true);
					markDirty();
					BlockPos offset = getPos().offset(getWorld().getBlockState(getPos()).getValue(BlockEmberPulser.facing),-1);
					getWorld().getTileEntity(offset).markDirty();
					if (!getWorld().isRemote && !(getWorld().getTileEntity(offset) instanceof ITileEntityBase)){
						EventManager.markTEForUpdate(offset,world.getTileEntity(offset));
					}
				}
			}
		}
		if ((this.ticksExisted+offset) % 20 == 0 && getWorld().isBlockIndirectlyGettingPowered(getPos()) != 0 && target != null && !getWorld().isRemote && this.capability.getEmber() > 100){
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
					
					packet.initCustom(getPos(), target, vx, vy, vz, Math.min(400.0,capability.getEmber()));
					this.capability.removeAmount(Math.min(400.0,capability.getEmber()), true);
					getWorld().spawnEntity(packet);
					markDirty();
				}
			}
		}
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		if (capability == EmberCapabilityProvider.emberCapability){
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == EmberCapabilityProvider.emberCapability){
			return (T)this.capability;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void setTargetPosition(BlockPos pos, EnumFacing side) {
		target = pos;
		markDirty();
	}
}
