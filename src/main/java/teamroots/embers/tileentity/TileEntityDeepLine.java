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
import net.minecraft.util.EnumParticleTypes;
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
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.power.IEmberPacketReceiver;
import teamroots.embers.world.EmberWorldData;

public class TileEntityDeepLine extends TileEntity implements ITileEntityBase, ITickable {
	public IEmberCapability capability = new DefaultEmberCapability();
	Random random = new Random();
	public long ticksExisted = 0;
	
	public TileEntityDeepLine(){
		super();
		capability.setEmberCapacity(8000);
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
		player.addChatMessage(new TextComponentString(""+capability.getEmber()+" / "+capability.getEmberCapacity()));
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		world.setTileEntity(pos, null);
	}

	@Override
	public void update() {
		ticksExisted ++;
		if (getWorld().isBlockIndirectlyGettingPowered(getPos()) != 0){
			EmberWorldData data = EmberWorldData.get(getWorld());
			if (!getWorld().isRemote && data.emberData.get(""+getPos().getX()/16+" "+getPos().getY()/16) > 0){
				if (ticksExisted % 20 == 0){
					this.capability.addAmount(Math.min(10,data.emberData.get(""+getPos().getX()/16+" "+getPos().getY()/16)), true);
					data.emberData.replace(""+getPos().getX()/16+" "+getPos().getY()/16,Math.max(0, data.emberData.get(""+getPos().getX()/16+" "+getPos().getY()/16)-10.0));
					data.markDirty();
					this.markDirty();
					getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
					getWorld().spawnParticle(random.nextInt(3) == 0 ? EnumParticleTypes.SMOKE_LARGE : EnumParticleTypes.SMOKE_NORMAL, getPos().getX()+random.nextDouble(), getPos().getY()+random.nextDouble(), getPos().getZ()+random.nextDouble(), 0, 0.05*random.nextDouble(), 0, 0);
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
}
