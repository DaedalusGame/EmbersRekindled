package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.Embers;
import teamroots.embers.SoundManager;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.api.tile.IExtraCapabilityInformation;
import teamroots.embers.api.tile.IExtraDialInformation;
import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.api.upgrades.UpgradeUtil;
import teamroots.embers.block.BlockEmberGauge;
import teamroots.embers.block.BlockItemGauge;
import teamroots.embers.config.ConfigMachine;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.util.Misc;
import teamroots.embers.util.sound.ISoundController;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class TileEntityCharger extends TileEntity implements ITileEntityBase, ITickable, ISoundController, IExtraDialInformation, IExtraCapabilityInformation {
	public static double MAX_TRANSFER = ConfigMachine.CHARGER_CATEGORY.maxTransfer;

	public IEmberCapability capability = new DefaultEmberCapability();
	int angle = 0;
	int turnRate = 0;
	public ItemStackHandler inventory = new ItemStackHandler(1){
		@Override
		public int getSlotLimit(int slot) {
			return 1;
		}

		@Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
        	TileEntityCharger.this.markDirty();
        }
	};
	Random random = new Random();
	boolean isWorking;

	public static final int SOUND_PROCESS = 1;
	public static final int[] SOUND_IDS = new int[]{SOUND_PROCESS};

	HashSet<Integer> soundsPlaying = new HashSet<>();

	public TileEntityCharger(){
		super();
		capability.setEmberCapacity(24000);
		capability.setEmber(0);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		capability.writeToNBT(tag);
		tag.setTag("inventory", inventory.serializeNBT());
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		capability.readFromNBT(tag);
		inventory.deserializeNBT(tag.getCompoundTag("inventory"));
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
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return true;
		}
		if (capability == EmbersCapabilities.EMBER_CAPABILITY){
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return (T)this.inventory;
		}
		if (capability == EmbersCapabilities.EMBER_CAPABILITY){
			return (T)this.capability;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		ItemStack stack = inventory.getStackInSlot(0);
		if (heldItem.hasCapability(EmbersCapabilities.EMBER_CAPABILITY,null)){
			player.setHeldItem(hand, this.inventory.insertItem(0,heldItem,false));
			markDirty();
			return true;
		}
		else if (!stack.isEmpty() && heldItem.isEmpty()) {
			if (!getWorld().isRemote) {
				player.setHeldItem(hand, inventory.extractItem(0, stack.getCount(), false));
				markDirty();
			}
			return true;
		}
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, inventory);
		world.setTileEntity(pos, null);
	}

	@Override
	public void update() {
		turnRate = 1;
		List<IUpgradeProvider> upgrades = UpgradeUtil.getUpgrades(world, pos, EnumFacing.VALUES);
		UpgradeUtil.verifyUpgrades(this, upgrades);
		if (UpgradeUtil.doTick(this, upgrades))
			return;
		World world = getWorld();
		if(world.isRemote)
			handleSound();
		ItemStack stack = inventory.getStackInSlot(0);
		isWorking = false;

		if (stack.hasCapability(EmbersCapabilities.EMBER_CAPABILITY,null)) {
			boolean cancel = UpgradeUtil.doWork(this,upgrades);
			if(!cancel) {
				IEmberCapability itemCapability = stack.getCapability(EmbersCapabilities.EMBER_CAPABILITY,null);
				double transferRate = UpgradeUtil.getTotalSpeedModifier(this, upgrades) * MAX_TRANSFER;
				double emberAdded;
				if(transferRate > 0) {
					emberAdded = itemCapability.addAmount(Math.min(Math.abs(transferRate), capability.getEmber()), !world.isRemote);
					capability.removeAmount(emberAdded, !world.isRemote);
				} else {
					emberAdded = capability.addAmount(Math.min(Math.abs(transferRate), itemCapability.getEmber()), !world.isRemote);
					itemCapability.removeAmount(emberAdded, !world.isRemote);
				}
				if (emberAdded > 0)
					isWorking = true;
				markDirty();
				if (world.isRemote && isWorking && this.capability.getEmber() > 0) {
					for (int i = 0; i < Math.ceil(this.capability.getEmber() / 500.0); i++) {
						ParticleUtil.spawnParticleGlow(world, getPos().getX() + 0.25f + random.nextFloat() * 0.5f, getPos().getY() + 0.25f + random.nextFloat() * 0.5f, getPos().getZ() + 0.25f + random.nextFloat() * 0.5f, 0, 0, 0, 255, 64, 16, 2.0f, 24);
					}
				}
			}
		}
		angle += turnRate;
	}

	@Override
	public void playSound(int id) {
        if (id == SOUND_PROCESS) {
            Embers.proxy.playMachineSound(this, SOUND_PROCESS, SoundManager.COPPER_CHARGER_LOOP, SoundCategory.BLOCKS, true, 1.0f, 1.0f, (float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f);
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

	@Override
	public void markDirty() {
		super.markDirty();
		Misc.syncTE(this);
	}

	@Override
	public void addDialInformation(EnumFacing facing, List<String> information, String dialType) {
		if(BlockEmberGauge.DIAL_TYPE.equals(dialType)) {
			ItemStack stack = inventory.getStackInSlot(0);
			if (stack.hasCapability(EmbersCapabilities.EMBER_CAPABILITY,null)) {
				IEmberCapability itemCapability = stack.getCapability(EmbersCapabilities.EMBER_CAPABILITY,null);
				information.add(BlockItemGauge.formatItemStack(stack));
				information.add(BlockEmberGauge.formatEmber(itemCapability.getEmber(),itemCapability.getEmberCapacity()));
			}
		}
	}

	@Override
	public boolean hasCapabilityDescription(Capability<?> capability) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Override
	public void addCapabilityDescription(List<String> strings, Capability<?> capability, EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			strings.add(IExtraCapabilityInformation.formatCapability(EnumIOType.BOTH,"embers.tooltip.goggles.item", I18n.format("embers.tooltip.goggles.item.ember_storage")));
	}
}
