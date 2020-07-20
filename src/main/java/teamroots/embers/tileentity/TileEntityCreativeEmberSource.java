package teamroots.embers.tileentity;

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
import teamroots.embers.EventManager;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;

public class TileEntityCreativeEmberSource extends TileEntity implements ITileEntityBase, ITickable {
	int ticksExisted = 0;
	BlockPos receivedFrom = null;
	public DefaultEmberCapability capability = new DefaultEmberCapability() {
		@Override
		public boolean acceptsVolatile() {
			return true;
		}

		@Override
		public double addAmount(double value, boolean doAdd) {
			return value;
		}

		@Override
		public double removeAmount(double value, boolean doRemove) {
			return value;
		}
	};
	
	public TileEntityCreativeEmberSource(){
		super();
		capability.setEmberCapacity(80000.0);
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
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		if (capability == EmbersCapabilities.EMBER_CAPABILITY){
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == EmbersCapabilities.EMBER_CAPABILITY){
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

	@Override
	public void update() {
		if (capability.getEmber() < capability.getEmberCapacity()){
			capability.setEmber(capability.getEmberCapacity());
			markDirty();
		}
	}
}
