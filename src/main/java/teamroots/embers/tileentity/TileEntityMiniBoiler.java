package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import teamroots.embers.Embers;
import teamroots.embers.SoundManager;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.event.DialInformationEvent;
import teamroots.embers.api.misc.ILiquidFuel;
import teamroots.embers.api.projectile.EffectDamage;
import teamroots.embers.api.tile.IExtraCapabilityInformation;
import teamroots.embers.api.tile.IExtraDialInformation;
import teamroots.embers.api.upgrades.UpgradeUtil;
import teamroots.embers.block.BlockBreaker;
import teamroots.embers.block.BlockFluidGauge;
import teamroots.embers.block.BlockMiniBoiler;
import teamroots.embers.damage.DamageEmber;
import teamroots.embers.entity.EntityEmberProjectile;
import teamroots.embers.upgrade.UpgradeMiniBoiler;
import teamroots.embers.util.Misc;
import teamroots.embers.util.sound.ISoundController;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class TileEntityMiniBoiler extends TileEntity implements ITileEntityBase, ISoundController, ITickable, IExtraDialInformation, IExtraCapabilityInformation {
	public static int FLUID_CAPACITY = Fluid.BUCKET_VOLUME*16;
	public static int FLUID_PROCESS_AMOUNT = 1;

	public static final int SOUND_SLOW = 1;
	public static final int SOUND_MEDIUM = 2;
	public static final int SOUND_FAST = 3;
	public static final int SOUND_PRESSURE_LOW = 4;
	public static final int SOUND_PRESSURE_MEDIUM = 5;
	public static final int SOUND_PRESSURE_HIGH = 6;
	public static final int[] SOUND_IDS = new int[]{SOUND_SLOW, SOUND_MEDIUM, SOUND_FAST, SOUND_PRESSURE_LOW, SOUND_PRESSURE_MEDIUM, SOUND_PRESSURE_HIGH};

	Random random = new Random();
	HashSet<Integer> soundsPlaying = new HashSet<>();
	protected FluidTank fluidTank = new FluidTank(FLUID_CAPACITY);
	protected FluidTank gasTank = new FluidTank(FLUID_CAPACITY);
	protected UpgradeMiniBoiler upgrade;
	int lastBoil;
	int boilTime;

	public TileEntityMiniBoiler(){
		super();
		fluidTank.setTileEntity(this);
		gasTank.setTileEntity(this);

		upgrade = new UpgradeMiniBoiler(this);
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
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		fluidTank.readFromNBT(tag.getCompoundTag("fluidTank"));
		gasTank.readFromNBT(tag.getCompoundTag("gasTank"));
		lastBoil = tag.getInteger("lastBoil");
		boilTime = tag.getInteger("boilTime");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag = super.writeToNBT(tag);
		tag.setTag("fluidTank",fluidTank.writeToNBT(new NBTTagCompound()));
		tag.setTag("gasTank",gasTank.writeToNBT(new NBTTagCompound()));
		tag.setInteger("lastBoil",lastBoil);
		tag.setInteger("boilTime",boilTime);
		return tag;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return facing != null && facing.getAxis() == EnumFacing.Axis.Y;
		}
		else if(capability == EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY) {
			return facing == getFacing();
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			if(facing == EnumFacing.DOWN) return (T) fluidTank;
			if(facing == EnumFacing.UP) return (T) gasTank;
		}
		if(capability == EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY && facing == getFacing()) {
			return (T) upgrade;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		return false;
	}

	public EnumFacing getFacing() {
		IBlockState state = world.getBlockState(pos);
		return state.getValue(BlockMiniBoiler.facing);
	}

	public int getCapacity(){
		return FLUID_CAPACITY;
	}
	
	public int getFluidAmount(){
		return fluidTank.getFluidAmount();
	}

	public int getGasAmount(){
		return gasTank.getFluidAmount();
	}

	public FluidTank getFluidTank(){
		return fluidTank;
	}

	public FluidTank getGasTank(){
		return gasTank;
	}

	public Fluid getFluid(){
		if (fluidTank.getFluid() != null){
			return fluidTank.getFluid().getFluid();
		}
		return null;
	}

	public Fluid getGas(){
		if (gasTank.getFluid() != null){
			return gasTank.getFluid().getFluid();
		}
		return null;
	}

	public FluidStack getFluidStack() {
		return fluidTank.getFluid();
	}

	public FluidStack getGasStack() {
		return gasTank.getFluid();
	}

	public void boil(double heat)
	{
		FluidStack fluid = getFluidStack();
		ILiquidFuel fuelHandler = EmbersAPI.getBoilerFluid(fluid);
		if(fuelHandler != null && fluid.amount > 0) {
			int fluidBoiled = Math.min(fluid.amount, (int) (FLUID_PROCESS_AMOUNT * heat));
			if(fluidBoiled > 0) {
				fluid = fluidTank.drain(fluidBoiled,false);
				FluidStack gas = fuelHandler.getRemainder(fluid);
				if(gas != null) {
					fluidTank.drain(fluidBoiled,true);
					gas.amount -= gasTank.fill(gas,true);
					if(gas.amount > 0 && !world.isRemote) {
						explode();
					}
				}
			}
			lastBoil = fluidBoiled;
			boilTime = fluidBoiled / 200;
			markDirty();
		}
	}

	public void explode() {
		double posX = pos.getX() + 0.5;
		double posY = pos.getY() + 0.5;
		double posZ = pos.getZ() + 0.5;
		world.playSound(null,pos, SoundManager.MINI_BOILER_RUPTURE, SoundCategory.BLOCKS,1.0f,1.0f); //TODO: Random pitch
		Explosion explosion = world.newExplosion(null, posX, posY, posZ, 3f, true, false);
		world.setBlockToAir(pos);
		EffectDamage effect = new EffectDamage(4.0f, preset -> DamageSource.causeExplosionDamage(explosion), 10, 0.0f);
		for(int i = 0; i < 12; i++) {
			EntityEmberProjectile proj = new EntityEmberProjectile(world);
			proj.initCustom(posX, posY, posZ, random.nextDouble()-0.5, random.nextDouble()-0.5, random.nextDouble()-0.5, 10.0f, null);
			proj.setLifetime(20+random.nextInt(40));
			proj.setEffect(effect);
			world.spawnEntity(proj);
		}
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
		if(BlockFluidGauge.DIAL_TYPE.equals(dialType) && facing.getAxis() != EnumFacing.Axis.Y) {
			String gasFormat = "";
			if(getGasAmount() > getCapacity() * 0.8)
				gasFormat = TextFormatting.RED.toString()+" ";
			else if(getGasAmount() > getCapacity() * 0.5)
				gasFormat = TextFormatting.YELLOW.toString()+" ";
			information.add(gasFormat+BlockFluidGauge.formatFluidStack(getGasStack(),getCapacity()));
			information.add(BlockFluidGauge.formatFluidStack(getFluidStack(),getCapacity()));
		}
	}

	@Override
	public int getComparatorData(EnumFacing facing, int data, String dialType) {
		if(BlockFluidGauge.DIAL_TYPE.equals(dialType) && facing.getAxis() != EnumFacing.Axis.Y) {
			double fill = getGasAmount() / (double)getCapacity();
			return fill > 0 ? (int) (1 + fill * 14) : 0;
		}
		return data;
	}

	@Override
	public void update() {
		if(world.isRemote) {
			handleSound();
			spawnParticles();
		}
		if(boilTime > 0)
			boilTime--;
	}

	public void spawnParticles() {
		double gasRatio = getGasAmount() / (double)getCapacity();
		int spouts = 0;
		if(gasRatio > 0.8)
			spouts = 3;
		else if(gasRatio > 0.5)
			spouts = 2;
		else if(gasRatio > 0.25)
			spouts = 1;
		Misc.spawnClogParticles(world,pos,spouts,0.4f);
	}

	@Override
	public void playSound(int id) {
		float soundX = (float) pos.getX() + 0.5f;
		float soundY = (float) pos.getY() + 0.5f;
		float soundZ = (float) pos.getZ() + 0.5f;
		switch (id) {
			case SOUND_SLOW:
				Embers.proxy.playMachineSound(this, SOUND_SLOW, SoundManager.MINI_BOILER_LOOP_SLOW, SoundCategory.BLOCKS, true, 1.0f, 1.0f, soundX, soundY, soundZ);
				break;
			case SOUND_MEDIUM:
				Embers.proxy.playMachineSound(this, SOUND_MEDIUM, SoundManager.MINI_BOILER_LOOP_MID, SoundCategory.BLOCKS, true, 1.0f, 1.0f, soundX, soundY, soundZ);
				break;
			case SOUND_FAST:
				Embers.proxy.playMachineSound(this, SOUND_FAST, SoundManager.MINI_BOILER_LOOP_FAST, SoundCategory.BLOCKS, true, 1.0f, 1.0f, soundX, soundY, soundZ);
				break;
			case SOUND_PRESSURE_LOW:
				Embers.proxy.playMachineSound(this, SOUND_PRESSURE_LOW, SoundManager.MINI_BOILER_PRESSURE_LOW, SoundCategory.BLOCKS, true, 1.0f, 1.0f, soundX, soundY, soundZ);
				break;
			case SOUND_PRESSURE_MEDIUM:
				Embers.proxy.playMachineSound(this, SOUND_PRESSURE_MEDIUM, SoundManager.MINI_BOILER_PRESSURE_MID, SoundCategory.BLOCKS, true, 1.0f, 1.0f, soundX, soundY, soundZ);
				break;
			case SOUND_PRESSURE_HIGH:
				Embers.proxy.playMachineSound(this, SOUND_PRESSURE_HIGH, SoundManager.MINI_BOILER_PRESSURE_HIGH, SoundCategory.BLOCKS, true, 1.0f, 1.0f, soundX, soundY, soundZ);
				break;
		}
		soundsPlaying.add(id);
	}

	@Override
	public void stopSound(int id) {
		soundsPlaying.remove(id);
	}

	@Override
	public boolean isSoundPlaying(int id) {
		return soundsPlaying.contains(id);
	}

	@Override
	public int[] getSoundIDs() {
		return SOUND_IDS;
	}

	@Override
	public boolean shouldPlaySound(int id) {
		int speedId = 0;
		int pressureId = 0;

		int gasAmount = getGasAmount();
		double gasRatio = gasAmount / (double)getCapacity();
		if(gasRatio > 0.8)
			pressureId = SOUND_PRESSURE_HIGH;
		else if(gasRatio > 0.5)
			pressureId = SOUND_PRESSURE_MEDIUM;
		else if(gasRatio > 0.25)
			pressureId = SOUND_PRESSURE_LOW;

		if(boilTime > 0 && lastBoil > 0) {
			if (lastBoil >= 2400)
				speedId = SOUND_FAST;
			else if(lastBoil >= 400)
				speedId = SOUND_MEDIUM;
			else
				speedId = SOUND_SLOW;
		}

		return speedId == id || pressureId == id;
	}

	@Override
	public boolean hasCapabilityDescription(Capability<?> capability) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
	}

	@Override
	public void addCapabilityDescription(List<String> strings, Capability<?> capability, EnumFacing facing) {
		if(facing == EnumFacing.DOWN)
			strings.add(IExtraCapabilityInformation.formatCapability(EnumIOType.INPUT,"embers.tooltip.goggles.fluid",I18n.format("embers.tooltip.goggles.fluid.water")));
		if(facing == EnumFacing.UP)
			strings.add(IExtraCapabilityInformation.formatCapability(EnumIOType.OUTPUT,"embers.tooltip.goggles.fluid",I18n.format("embers.tooltip.goggles.fluid.steam")));
	}
}
