package teamroots.embers.tileentity;

import java.util.Random;

import javax.annotation.Nullable;

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
import teamroots.embers.api.tile.IHammerable;
import teamroots.embers.block.BlockAutoHammer;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.api.power.IEmberCapability;

public class TileEntityAutoHammer extends TileEntity implements ITileEntityBase, ITickable {
	public IEmberCapability capability = new DefaultEmberCapability();
	int ticksExisted = 0;
	int progress = -1;
	Random random = new Random();
	
	public TileEntityAutoHammer(){
		super();
		capability.setEmberCapacity(12000);
		capability.setEmber(0);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		capability.writeToNBT(tag);
		tag.setInteger("progress", progress);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		capability.readFromNBT(tag);
		progress = tag.getInteger("progress");
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
	public void update(){
		ticksExisted ++;
		if (ticksExisted % 20 == 0){
			TileEntity tile = world.getTileEntity(getPos().down().offset(world.getBlockState(getPos()).getValue(BlockAutoHammer.facing)));
			if (tile instanceof IHammerable && progress == -1 && capability.getEmber() >= 40.0f && getWorld().isBlockIndirectlyGettingPowered(getPos()) != 0){
				if (((IHammerable) tile).isValid()){
					progress = 10;
					markDirty();
				}
			}
		}
		if (progress > 0){
			progress --;
			if (progress == 5){
				TileEntity tile = world.getTileEntity(getPos().down().offset(world.getBlockState(getPos()).getValue(BlockAutoHammer.facing)));
				if (tile instanceof IHammerable && capability.getEmber() >= 40.0f){
					capability.removeAmount(40.0f, true);
					((IHammerable) tile).onHit(this);
					markDirty();
				}
			}
			markDirty();
		}
		if (progress == 0){
			progress = -1;
			markDirty();
		}
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
