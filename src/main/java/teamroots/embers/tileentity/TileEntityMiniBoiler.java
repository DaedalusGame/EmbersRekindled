package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.event.DialInformationEvent;
import teamroots.embers.api.misc.ILiquidFuel;
import teamroots.embers.api.projectile.EffectDamage;
import teamroots.embers.api.tile.IExtraDialInformation;
import teamroots.embers.api.upgrades.UpgradeUtil;
import teamroots.embers.block.BlockBreaker;
import teamroots.embers.block.BlockFluidGauge;
import teamroots.embers.block.BlockMiniBoiler;
import teamroots.embers.damage.DamageEmber;
import teamroots.embers.entity.EntityEmberProjectile;
import teamroots.embers.upgrade.UpgradeMiniBoiler;
import teamroots.embers.util.Misc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class TileEntityMiniBoiler extends TileEntity implements ITileEntityBase, IExtraDialInformation {
	public static int FLUID_CAPACITY = Fluid.BUCKET_VOLUME*16;
	public static int FLUID_PROCESS_AMOUNT = 1;
	public static double STEAM_MULTIPLIER = 5;

	Random random = new Random();
	protected FluidTank fluidTank = new FluidTank(FLUID_CAPACITY);
	protected FluidTank gasTank = new FluidTank(FLUID_CAPACITY);
	protected UpgradeMiniBoiler upgrade;

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
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag = super.writeToNBT(tag);
		tag.setTag("fluidTank",fluidTank.writeToNBT(new NBTTagCompound()));
		tag.setTag("gasTank",gasTank.writeToNBT(new NBTTagCompound()));
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
		}
	}

	public void explode() {
		double posX = pos.getX() + 0.5;
		double posY = pos.getY() + 0.5;
		double posZ = pos.getZ() + 0.5;
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
		if(facing.getAxis() != EnumFacing.Axis.Y) {
			String gasFormat = "";
			if(getGasAmount() > getCapacity() * 0.8)
				gasFormat = TextFormatting.RED.toString()+" ";
			else if(getGasAmount() > getCapacity() * 0.5)
				gasFormat = TextFormatting.YELLOW.toString()+" ";
			information.add(gasFormat+BlockFluidGauge.formatFluidStack(getGasStack(),getCapacity()));
			information.add(BlockFluidGauge.formatFluidStack(getFluidStack(),getCapacity()));
		}
	}
}
