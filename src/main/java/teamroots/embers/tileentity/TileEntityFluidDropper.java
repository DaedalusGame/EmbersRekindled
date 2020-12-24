package teamroots.embers.tileentity;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import teamroots.embers.util.EnumPipeConnection;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;
import java.util.Random;

public class TileEntityFluidDropper extends TileEntity implements ITileEntityBase, ITickable, IFluidPipeConnectable, IFluidPipePriority {
	Random random = new Random();
	FluidTank tank = new FluidTank(1000);

	public TileEntityFluidDropper(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		tank.readFromNBT(tag.getCompoundTag("tank"));
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
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == EnumFacing.UP){
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public void markDirty() {
		super.markDirty();
		Misc.syncTE(this);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == EnumFacing.UP){
			return (T)this.tank;
		}
		return super.getCapability(capability, facing);
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
		if(!world.isRemote) {
			EnumFacing facing = EnumFacing.DOWN;
			for (int i = 1; i <= 5; i++) {
				BlockPos checkPos = pos.offset(facing, i);
				TileEntity tile = world.getTileEntity(checkPos);
				if (tile != null) {
					IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
					if (handler != null) {
						FluidStack stack = tank.drain(tank.getCapacity(), false);
						if (stack != null) {
							int pushed = handler.fill(stack, false);
							if (pushed > 0) {
								handler.fill(stack, true);
								tank.drain(pushed, true);
							}
						}
					}
				}
				IBlockState state = world.getBlockState(checkPos);
				if (state.getBlockFaceShape(world, checkPos, EnumFacing.UP) == BlockFaceShape.SOLID || state.getBlockFaceShape(world, checkPos, EnumFacing.DOWN) == BlockFaceShape.SOLID)
					break;
			}
		}
	}

	@Override
	public EnumPipeConnection getConnection(EnumFacing facing) {
		if(facing == EnumFacing.UP)
			return EnumPipeConnection.PIPE;
		return EnumPipeConnection.NONE;
	}

	@Override
	public int getPriority(EnumFacing facing) {
		return 50;
	}
}
