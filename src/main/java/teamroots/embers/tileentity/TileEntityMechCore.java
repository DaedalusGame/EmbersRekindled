package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import teamroots.embers.EventManager;
import teamroots.embers.api.tile.IExtraDialInformation;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntityMechCore extends TileEntity implements ITileEntityBase, IExtraDialInformation {
	Random random = new Random();
	
	public TileEntityMechCore(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		return super.writeToNBT(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
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

	public TileEntity getAttachedMultiblock() {
		if (getWorld().getTileEntity(getPos().offset(EnumFacing.DOWN)) instanceof IMultiblockMachine){
			return getWorld().getTileEntity(getPos().offset(EnumFacing.DOWN));
		}
		if (getWorld().getTileEntity(getPos().offset(EnumFacing.UP)) instanceof IMultiblockMachine){
			return getWorld().getTileEntity(getPos().offset(EnumFacing.UP));
		}
		if (getWorld().getTileEntity(getPos().offset(EnumFacing.WEST)) instanceof IMultiblockMachine){
			return getWorld().getTileEntity(getPos().offset(EnumFacing.WEST));
		}
		if (getWorld().getTileEntity(getPos().offset(EnumFacing.EAST)) instanceof IMultiblockMachine){
			return getWorld().getTileEntity(getPos().offset(EnumFacing.EAST));
		}
		if (getWorld().getTileEntity(getPos().offset(EnumFacing.NORTH)) instanceof IMultiblockMachine){
			return getWorld().getTileEntity(getPos().offset(EnumFacing.NORTH));
		}
		if (getWorld().getTileEntity(getPos().offset(EnumFacing.SOUTH)) instanceof IMultiblockMachine){
			return getWorld().getTileEntity(getPos().offset(EnumFacing.SOUTH));
		}
		return null;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		TileEntity multiblock = getAttachedMultiblock();
		if(multiblock != null)
			return multiblock.hasCapability(capability, facing);
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		TileEntity multiblock = getAttachedMultiblock();
		if(multiblock != null)
			return multiblock.getCapability(capability, facing);
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
	public void markDirty() {
		super.markDirty();
		Misc.syncTE(this);
	}

	@Override
	public void addDialInformation(EnumFacing facing, List<String> information, String dialType) {
		TileEntity multiblock = getAttachedMultiblock();
		if(multiblock instanceof IExtraDialInformation)
			((IExtraDialInformation) multiblock).addDialInformation(facing,information,dialType);
	}
}
