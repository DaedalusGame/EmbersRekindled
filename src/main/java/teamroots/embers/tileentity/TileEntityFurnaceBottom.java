package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import teamroots.embers.Embers;
import teamroots.embers.SoundManager;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.event.EmberEvent;
import teamroots.embers.api.event.MachineRecipeEvent;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.api.upgrades.UpgradeUtil;
import teamroots.embers.config.ConfigMachine;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.recipe.ItemMeltingRecipe;
import teamroots.embers.recipe.RecipeRegistry;
import teamroots.embers.util.Misc;
import teamroots.embers.util.sound.ISoundController;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class TileEntityFurnaceBottom extends TileEntity implements ITileEntityBase, ITickable, ISoundController {
	public static int PROCESS_TIME = ConfigMachine.MELTER_CATEGORY.processTime;
	public IEmberCapability capability = new DefaultEmberCapability();
	Random random = new Random();
	int progress = -1;
	public static double EMBER_COST = ConfigMachine.MELTER_CATEGORY.emberCost;

	public static final int SOUND_PROCESS = 1;
	public static final int[] SOUND_IDS = new int[]{SOUND_PROCESS};

	HashSet<Integer> soundsPlaying = new HashSet<>();
	boolean isWorking;
	private List<IUpgradeProvider> upgrades;

	public TileEntityFurnaceBottom(){
		super();
		capability.setEmberCapacity(8000);
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
		if (tag.hasKey("progress")){
			progress = tag.getInteger("progress");
		}
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
			EnumFacing side, float hitX, float hitY, float hitZ) {
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		world.setTileEntity(pos, null);
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
	public void update() {
		if(getWorld().isRemote)
			handleSound();
		TileEntityFurnaceTop top = (TileEntityFurnaceTop) world.getTileEntity(getPos().up());
		upgrades = UpgradeUtil.getUpgrades(world, pos, EnumFacing.HORIZONTALS);
		UpgradeUtil.verifyUpgrades(this, upgrades);
		if (UpgradeUtil.doTick(this, upgrades))
			return;
		if(top != null && !top.inventory.getStackInSlot(0).isEmpty()) {
				double emberCost = UpgradeUtil.getTotalEmberConsumption(this,EMBER_COST, upgrades);
				if (capability.getEmber() >= emberCost) {
					boolean cancel = UpgradeUtil.doWork(this, upgrades);
					if(!cancel) {
						UpgradeUtil.throwEvent(this, new EmberEvent(this, EmberEvent.EnumType.CONSUME, emberCost), upgrades);
						capability.removeAmount(emberCost, true);
						if (world.isRemote) {
							if (random.nextInt(20) == 0) {
								ParticleUtil.spawnParticleSpark(world, getPos().getX() + 0.5f + 0.125f * (random.nextFloat() - 0.5f), getPos().getY() + 1.25f, getPos().getZ() + 0.5f + 0.125f * (random.nextFloat() - 0.5f), 0.125f * (random.nextFloat() - 0.5f), 0.125f * (random.nextFloat()), 0.125f * (random.nextFloat() - 0.5f), 255, 64, 16, random.nextFloat() * 0.75f + 0.45f, 80);
							}
							if (random.nextInt(10) == 0) {
								for (int i = 0; i < 12; i++) {
									ParticleUtil.spawnParticleSmoke(world, getPos().getX() + 0.5f + 0.125f * (random.nextFloat() - 0.5f), getPos().getY() + 1.25f, getPos().getZ() + 0.5f + 0.125f * (random.nextFloat() - 0.5f), 0, 0.03125f + 0.03125f * random.nextFloat(), 0, 64, 64, 64, 0.125f, 5.0f + 3.0f * random.nextFloat(), 80);
								}
							}
						}
						isWorking = true;
						progress++;
						markDirty();
						if (progress >= UpgradeUtil.getWorkTime(this, PROCESS_TIME, upgrades)) {
							ItemStack recipeStack = top.inventory.getStackInSlot(0);
							ItemMeltingRecipe recipe = getRecipe(recipeStack);
							if (recipe != null && !world.isRemote) {
								FluidStack output = recipe.getResult(this, recipeStack);
								FluidTank tank = top.getTank();
								output = UpgradeUtil.transformOutput(this, output, upgrades);
								if (output != null && tank.fill(output, false) >= output.amount) {
									tank.fill(output, true);
									top.markDirty();
									top.inventory.extractItem(0, recipe.getInputConsumed(), false);
									progress = 0;
									UpgradeUtil.throwEvent(this, new MachineRecipeEvent.Success<>(this,recipe), upgrades);
									markDirty();
								}
							}
						}
					}
                }
		} else {
			isWorking = false;
			if (progress > 0) {
				progress = 0;
				markDirty();
			}
		}
	}

	private ItemMeltingRecipe getRecipe(ItemStack recipeStack) {
		ItemMeltingRecipe recipe = RecipeRegistry.getMeltingRecipe(recipeStack);
		MachineRecipeEvent<ItemMeltingRecipe> event = new MachineRecipeEvent<>(this, recipe);
		UpgradeUtil.throwEvent(this, event,upgrades);
		return event.getRecipe();
	}

	@Override
	public void playSound(int id) {
		switch (id) {
			case SOUND_PROCESS:
				Embers.proxy.playMachineSound(this, SOUND_PROCESS, SoundManager.MELTER_LOOP, SoundCategory.BLOCKS, true, 1.0f, 1.0f, (float)pos.getX()+0.5f,(float)pos.getY()+1.0f,(float)pos.getZ()+0.5f);
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
		return id == SOUND_PROCESS && isWorking;
	}
}
