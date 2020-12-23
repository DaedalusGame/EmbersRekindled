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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import teamroots.embers.EventManager;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.event.DialInformationEvent;
import teamroots.embers.api.event.EmberEvent;
import teamroots.embers.api.event.MachineRecipeEvent;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.api.tile.IExtraDialInformation;
import teamroots.embers.api.tile.IHammerable;
import teamroots.embers.api.tile.IMechanicallyPowered;
import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.api.upgrades.UpgradeUtil;
import teamroots.embers.block.BlockAutoHammer;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntityAutoHammer extends TileEntity implements ITileEntityBase, ITickable, IMechanicallyPowered, IExtraDialInformation {
	public static final double EMBER_COST = 40.0;
	public static final int PROCESS_TIME = 20;
	public IEmberCapability capability = new DefaultEmberCapability();
	int ticksExisted = 0;
	int progress = -1;
	Random random = new Random();
	private List<IUpgradeProvider> upgrades = new ArrayList<>();

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
	public void update() {
		IBlockState state = world.getBlockState(getPos());
		EnumFacing facing = state.getValue(BlockAutoHammer.facing);
		upgrades = UpgradeUtil.getUpgrades(world, pos, new EnumFacing[]{facing.getOpposite()});
		UpgradeUtil.verifyUpgrades(this, upgrades);
		if (UpgradeUtil.doTick(this, upgrades))
			return;
		ticksExisted++;
		double ember_cost = UpgradeUtil.getTotalEmberConsumption(this, EMBER_COST, upgrades);
		TileEntity tile = world.getTileEntity(getPos().down().offset(facing));
		if (tile instanceof IHammerable) {
			IHammerable hammerable = (IHammerable) tile;
			boolean redstoneEnabled = getWorld().isBlockPowered(getPos());
			if (hammerable.isValid() && redstoneEnabled && capability.getEmber() >= ember_cost) {
				boolean cancel = UpgradeUtil.doWork(this, upgrades);
				if (!cancel && progress == -1 && ticksExisted % UpgradeUtil.getWorkTime(this,PROCESS_TIME, upgrades) == 0) {
					progress = 10;
					markDirty();
				}
			}
			if (progress > 0) {
				progress--;
				if (progress == 5) {
					if (capability.getEmber() >= ember_cost) {
						UpgradeUtil.throwEvent(this, new EmberEvent(this, EmberEvent.EnumType.CONSUME, ember_cost), upgrades);
						capability.removeAmount(ember_cost, true);
						hammerable.onHit(this);
					}
				}
				markDirty();
			}
		}
		if (progress == 0){
			progress = -1;
			markDirty();
		}
	}

	@Override
	public void markDirty() {
		super.markDirty();
		Misc.syncTE(this);
	}

	@Override
	public double getMechanicalSpeed(double power) {
		return Misc.getDiminishedPower(power,20,1.5/20);
	}

	@Override
	public double getNominalSpeed() {
		return 1;
	}

	@Override
	public double getMinimumPower() {
		return 10;
	}

	@Override
	public void addDialInformation(EnumFacing facing, List<String> information, String dialType) {
		UpgradeUtil.throwEvent(this,new DialInformationEvent(this,information,dialType),upgrades);
	}
}
