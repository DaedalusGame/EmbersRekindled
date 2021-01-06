package teamroots.embers.tileentity;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import teamroots.embers.Embers;
import teamroots.embers.EventManager;
import teamroots.embers.RegistryManager;
import teamroots.embers.SoundManager;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.event.EmberEvent;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.api.upgrades.UpgradeUtil;
import teamroots.embers.block.BlockInfernoForge;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageEmberActivationFX;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.util.Misc;
import teamroots.embers.util.sound.ISoundController;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class TileEntityInfernoForge extends TileEntity implements ITileEntityBase, ITickable, IMultiblockMachine, ISoundController {
	public static double EMBER_COST = 16.0;
	public static int MAX_LEVEL = 5;
	public static double MAX_CRYSTAL_VALUE = 3600 * 32.0;
	public static double CHANCE_MIDPOINT = 3600 * 4.0;
	public static int PROCESS_TIME = 200;
	public IEmberCapability capability = new DefaultEmberCapability() {
		@Override
		public boolean acceptsVolatile() {
			return true;
		}
	};
	Random random = new Random();
	int progress = 0;
	int heat = 0;
	int ticksExisted = 0;
	double emberValue = 0;

	public static final int SOUND_PROCESS = 1;
	public static final int[] SOUND_IDS = new int[]{SOUND_PROCESS};

	HashSet<Integer> soundsPlaying = new HashSet<>();

	public TileEntityInfernoForge(){
		super();
		capability.setEmberCapacity(32000);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		capability.writeToNBT(tag);
		tag.setInteger("progress", progress);
		tag.setInteger("heat", heat);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		capability.readFromNBT(tag);
		if (tag.hasKey("progress")){
			progress = tag.getInteger("progress");
		}
		if (tag.hasKey("heat")){
			heat = tag.getInteger("heat");
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
		((BlockInfernoForge)state.getBlock()).cleanEdges(world,pos);
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
	public void update() {
		if (getWorld().isRemote)
			handleSound();
		List<IUpgradeProvider> upgrades = UpgradeUtil.getUpgrades(world, pos, new EnumFacing[]{EnumFacing.DOWN});
		UpgradeUtil.verifyUpgrades(this, upgrades);
		if (UpgradeUtil.doTick(this, upgrades))
			return;
		ticksExisted++;
		if (progress <= 0)
			return;
		boolean cancel = UpgradeUtil.doWork(this, upgrades);
		double emberCost = UpgradeUtil.getTotalEmberConsumption(this, EMBER_COST, upgrades);
		if (cancel || capability.getEmber() < emberCost) {
			progress = 0;
			markDirty();
			return;
		}
		UpgradeUtil.throwEvent(this, new EmberEvent(this, EmberEvent.EnumType.CONSUME, emberCost), upgrades);
		capability.removeAmount(emberCost, true);
		progress--;
		if (getWorld().isRemote) {
			if (random.nextInt(10) == 0) {
				if (random.nextInt(3) == 0)
					ParticleUtil.spawnParticleSpark(world, getPos().getX() - 0.5f + 0.125f * (random.nextFloat() - 0.5f), getPos().getY() + 1.75f, getPos().getZ() - 0.5f + 0.125f * (random.nextFloat() - 0.5f), 0.125f * (random.nextFloat() - 0.5f), 0.125f * (random.nextFloat()), 0.125f * (random.nextFloat() - 0.5f), 255, 64, 16, random.nextFloat() * 0.75f + 0.45f, 80);
				if (random.nextInt(3) == 0)
					ParticleUtil.spawnParticleSpark(world, getPos().getX() + 1.5f + 0.125f * (random.nextFloat() - 0.5f), getPos().getY() + 1.75f, getPos().getZ() - 0.5f + 0.125f * (random.nextFloat() - 0.5f), 0.125f * (random.nextFloat() - 0.5f), 0.125f * (random.nextFloat()), 0.125f * (random.nextFloat() - 0.5f), 255, 64, 16, random.nextFloat() * 0.75f + 0.45f, 80);
				if (random.nextInt(3) == 0)
					ParticleUtil.spawnParticleSpark(world, getPos().getX() + 1.5f + 0.125f * (random.nextFloat() - 0.5f), getPos().getY() + 1.75f, getPos().getZ() + 1.5f + 0.125f * (random.nextFloat() - 0.5f), 0.125f * (random.nextFloat() - 0.5f), 0.125f * (random.nextFloat()), 0.125f * (random.nextFloat() - 0.5f), 255, 64, 16, random.nextFloat() * 0.75f + 0.45f, 80);
				if (random.nextInt(3) == 0)
					ParticleUtil.spawnParticleSpark(world, getPos().getX() - 0.5f + 0.125f * (random.nextFloat() - 0.5f), getPos().getY() + 1.75f, getPos().getZ() + 1.5f + 0.125f * (random.nextFloat() - 0.5f), 0.125f * (random.nextFloat() - 0.5f), 0.125f * (random.nextFloat()), 0.125f * (random.nextFloat() - 0.5f), 255, 64, 16, random.nextFloat() * 0.75f + 0.45f, 80);
			}
			ParticleUtil.spawnParticleSmoke(getWorld(), (float) getPos().getX() - 0.3f, (float) getPos().getY() + 1.85f, (float) getPos().getZ() - 0.3f, 0.025f * (random.nextFloat() - 0.5f), 0.05f * (random.nextFloat() + 1.0f), 0.025f * (random.nextFloat() - 0.5f), 72, 72, 72, 1.0f, 3.0f + 3.0f * random.nextFloat(), 48);
			ParticleUtil.spawnParticleSmoke(getWorld(), (float) getPos().getX() + 1.3f, (float) getPos().getY() + 1.85f, (float) getPos().getZ() - 0.3f, 0.025f * (random.nextFloat() - 0.5f), 0.05f * (random.nextFloat() + 1.0f), 0.025f * (random.nextFloat() - 0.5f), 72, 72, 72, 1.0f, 3.0f + 3.0f * random.nextFloat(), 48);
			ParticleUtil.spawnParticleSmoke(getWorld(), (float) getPos().getX() + 1.3f, (float) getPos().getY() + 1.85f, (float) getPos().getZ() + 1.3f, 0.025f * (random.nextFloat() - 0.5f), 0.05f * (random.nextFloat() + 1.0f), 0.025f * (random.nextFloat() - 0.5f), 72, 72, 72, 1.0f, 3.0f + 3.0f * random.nextFloat(), 48);
			ParticleUtil.spawnParticleSmoke(getWorld(), (float) getPos().getX() - 0.3f, (float) getPos().getY() + 1.85f, (float) getPos().getZ() + 1.3f, 0.025f * (random.nextFloat() - 0.5f), 0.05f * (random.nextFloat() + 1.0f), 0.025f * (random.nextFloat() - 0.5f), 72, 72, 72, 1.0f, 3.0f + 3.0f * random.nextFloat(), 48);
			/*
			if (random.nextInt(3) == 0)
				ParticleUtil.spawnParticleGlow(getWorld(), (float) getPos().getX() - 0.3f, (float) getPos().getY() + 1.85f, (float) getPos().getZ() - 0.3f, 0.0125f * (random.nextFloat() - 0.5f), 0.025f * (random.nextFloat() + 1.0f), 0.0125f * (random.nextFloat() - 0.5f), 255, 64, 16, 2.0f, 48);
			if (random.nextInt(3) == 0)
				ParticleUtil.spawnParticleGlow(getWorld(), (float) getPos().getX() + 1.3f, (float) getPos().getY() + 1.85f, (float) getPos().getZ() - 0.3f, 0.0125f * (random.nextFloat() - 0.5f), 0.025f * (random.nextFloat() + 1.0f), 0.0125f * (random.nextFloat() - 0.5f), 255, 64, 16, 2.0f, 48);
			if (random.nextInt(3) == 0)
				ParticleUtil.spawnParticleGlow(getWorld(), (float) getPos().getX() + 1.3f, (float) getPos().getY() + 1.85f, (float) getPos().getZ() + 1.3f, 0.0125f * (random.nextFloat() - 0.5f), 0.025f * (random.nextFloat() + 1.0f), 0.0125f * (random.nextFloat() - 0.5f), 255, 64, 16, 2.0f, 48);
			if (random.nextInt(3) == 0)
				ParticleUtil.spawnParticleGlow(getWorld(), (float) getPos().getX() - 0.3f, (float) getPos().getY() + 1.85f, (float) getPos().getZ() + 1.3f, 0.0125f * (random.nextFloat() - 0.5f), 0.025f * (random.nextFloat() + 1.0f), 0.0125f * (random.nextFloat() - 0.5f), 255, 64, 16, 2.0f, 48);
			*/
		}
		List<EntityItem> items = getValidItems();
		for (EntityItem e : items)
			e.setPickupDelay(20);
		if (progress != 0 || world.isRemote) {
			markDirty();
			return;
		}
		if (items.isEmpty()) {
			progress = 0;
			markDirty();
			return;
		}
		boolean forgeSuccess = false;
		TileEntityInfernoForgeOpening opening = getOpening();
		if (opening != null)
			opening.open();
		if(emberValue > 0) {
			for (EntityItem item : items)
				if (!ItemModUtil.hasHeat(item.getItem())) {
					world.removeEntity(item);
					item.setDead();
				} else if (emberValue <= MAX_CRYSTAL_VALUE && Misc.random.nextDouble() < UpgradeUtil.getOtherParameter(this, "reforge_chance",
						Math.atan(emberValue / CHANCE_MIDPOINT) / (Math.PI / 2.0), upgrades)  //clockwork arcane business
				) {
					ItemStack stack = item.getItem();
					ItemModUtil.setHeat(stack, 0);
					ItemModUtil.setLevel(stack, ItemModUtil.getLevel(stack) + 1);
					item.setItem(stack);
					progress = 0;
					forgeSuccess = true;
				}
		}
		if (!world.isRemote) {
			world.playSound(null, getPos().getX() + 0.5, getPos().getY() + 1.5, getPos().getZ() + 0.5, forgeSuccess ? SoundManager.INFERNO_FORGE_SUCCESS : SoundManager.INFERNO_FORGE_FAIL, SoundCategory.BLOCKS, 1.0f, 1.0f);
			Color flameColor = new Color(255, 64, 16);
			if (!forgeSuccess)
				flameColor = new Color(0, 0, 0);
			if (emberValue > MAX_CRYSTAL_VALUE)
				flameColor = new Color(16, 64, 255);
			Color sparkColor = new Color(255, 64, 16);

			PacketHandler.INSTANCE.sendToAll(new MessageEmberActivationFX(getPos().getX() + 0.5, getPos().getY() + 1.5, getPos().getZ() + 0.5, flameColor, sparkColor));
		}
		markDirty();
	}

	private TileEntityInfernoForgeOpening getOpening() {
		TileEntity tile = world.getTileEntity(pos.up());
		return tile instanceof TileEntityInfernoForgeOpening ? (TileEntityInfernoForgeOpening) tile : null;
	}

	public void updateProgress() {
		if (progress != 0) return;
		List<EntityItem> items = getValidItems();
		if (!items.isEmpty()) {
			progress = PROCESS_TIME;
			world.playSound(null, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, SoundManager.INFERNO_FORGE_START, SoundCategory.BLOCKS, 1.0f, 1.0f);
			markDirty();
		}
	}

	private List<EntityItem> getValidItems() {
		List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(getPos().getX(), getPos().getY() + 0.25, getPos().getZ(), getPos().getX() + 1, getPos().getY() + 1, getPos().getZ() + 1));
		ItemStack pickedItem = ItemStack.EMPTY;
		emberValue = 0;
		for (EntityItem item : items) {
			final ItemStack stack = item.getItem();
			if (ItemModUtil.hasHeat(stack))
				if (pickedItem.isEmpty()
						&& ItemModUtil.getLevel(stack) < MAX_LEVEL
						&& ItemModUtil.getHeat(stack) >= ItemModUtil.getMaxHeat(stack))
					pickedItem = stack;
				else return Lists.newArrayList();
			else if (EmbersAPI.getEmberValue(stack) > 0)
				emberValue += EmbersAPI.getEmberValue(stack) * stack.getCount();
			else return Lists.newArrayList();
		}
		if (!pickedItem.isEmpty() && emberValue > 0)
			return items;
		return Lists.newArrayList();
	}

	@Override
	public void playSound(int id) {
		if (id == SOUND_PROCESS)
			Embers.proxy.playMachineSound(this, SOUND_PROCESS, SoundManager.INFERNO_FORGE_LOOP, SoundCategory.BLOCKS, true, 1.0f, 1.0f, (float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f);
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
		return id == SOUND_PROCESS && progress > 0;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		Misc.syncTE(this);
	}
}
