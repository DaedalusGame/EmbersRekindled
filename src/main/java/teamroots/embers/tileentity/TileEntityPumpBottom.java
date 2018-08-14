package teamroots.embers.tileentity;

import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import teamroots.embers.EventManager;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.block.BlockPump;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.util.FluidUtil;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;

public class TileEntityPumpBottom extends TileEntity implements ITileEntityBase, ITickable {
	public static final double EMBER_COST = 0.5;

	int ticksExisted = 0;
	int progress = 0;
	EnumFacing front = EnumFacing.UP;
	public IEmberCapability capability = new DefaultEmberCapability();
	
	public TileEntityPumpBottom(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		capability.writeToNBT(tag);
		tag.setInteger("progress", progress);
		tag.setInteger("front", front.getIndex());
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		capability.readFromNBT(tag);
		front = EnumFacing.getFront(tag.getInteger("front"));
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
		if (capability == EmbersCapabilities.MECH_CAPABILITY){
			return facing.getAxis() == front.getAxis();
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == EmbersCapabilities.MECH_CAPABILITY){
			return (T)this.capability;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void markDirty() {
		super.markDirty();
		Misc.syncTE(this);
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
	
	public boolean attemptPump(BlockPos pos){
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof IFluidBlock && ((IFluidBlock)state.getBlock()).canDrain(world, pos) || state.getBlock() instanceof BlockStaticLiquid){
			if (capability.getEmber() > 0 || state.getBlock() == Blocks.WATER){
				FluidStack stack = FluidUtil.getFluid(world, pos, state);
				if (stack != null){
					TileEntityPumpTop t = (TileEntityPumpTop)world.getTileEntity(getPos().up());
					int filled = t.getTank().fill(stack, false);
					if (filled == stack.amount){
						if (!world.isRemote){
							t.getTank().fill(stack, true);
						}
						t.markDirty();
						world.setBlockToAir(pos);
						world.notifyBlockUpdate(pos, state, Blocks.AIR.getDefaultState(), 8);
						world.notifyNeighborsOfStateChange(pos.north(), Blocks.AIR, true);
						world.notifyNeighborsOfStateChange(pos.south(), Blocks.AIR, true);
						world.notifyNeighborsOfStateChange(pos.east(), Blocks.AIR, true);
						world.notifyNeighborsOfStateChange(pos.west(), Blocks.AIR, true);
						world.notifyNeighborsOfStateChange(pos.up(), Blocks.AIR, true);
						world.notifyNeighborsOfStateChange(pos.down(), Blocks.AIR, true);
						world.notifyNeighborsOfStateChange(pos, Blocks.AIR, true);
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public void update() {
		IBlockState state = world.getBlockState(getPos());
		if (state.getBlock() instanceof BlockPump){
			this.front = state.getValue(BlockPump.facing);
		}
		if (capability.getEmber() >= EMBER_COST) {
			this.progress += 1;
			capability.removeAmount(EMBER_COST,false);
			if (this.progress > 400) {
				progress -= 400;
				boolean doContinue = true;
				for (int r = 0; r < 6 && doContinue; r++) {
					for (int i = -r; i < r + 1 && doContinue; i++) {
						for (int j = -r; j < 1 && doContinue; j++) {
							for (int k = -r; k < r + 1 && doContinue; k++) {
								doContinue = attemptPump(getPos().add(i, j - 1, k));
							}
						}
					}
				}
			}
		}
		markDirty();
	}
}
