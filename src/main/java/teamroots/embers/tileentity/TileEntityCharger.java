package teamroots.embers.tileentity;

import java.util.HashSet;
import java.util.Random;

import javax.annotation.Nullable;

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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.Embers;
import teamroots.embers.EventManager;
import teamroots.embers.SoundManager;
import teamroots.embers.item.IEmberItem;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.util.Misc;
import teamroots.embers.util.sound.ISoundController;

public class TileEntityCharger extends TileEntity implements ITileEntityBase, ITickable, ISoundController {
	public static final double MAX_TRANSFER = 10.0;

	public IEmberCapability capability = new DefaultEmberCapability();
	int angle = 0;
	int turnRate = 0;
	public ItemStackHandler inventory = new ItemStackHandler(1){
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
		if (capability == EmberCapabilityProvider.emberCapability){
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return (T)this.inventory;
		}
		if (capability == EmberCapabilityProvider.emberCapability){
			return (T)this.capability;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		ItemStack stack = inventory.getStackInSlot(0);
		if (!heldItem.isEmpty() && heldItem.getItem() instanceof IEmberItem){
			player.setHeldItem(hand, this.inventory.insertItem(0,heldItem,false));
			markDirty();
			return true;
		}
		else if (!stack.isEmpty()) {
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
		handleSound();
		ItemStack stack = inventory.getStackInSlot(0);
		isWorking = false;
		if (!stack.isEmpty() && capability.getEmber() > 0 && stack.getItem() instanceof IEmberItem) {
			double emberAdded = ((IEmberItem) stack.getItem()).addAmount(stack, Math.min(MAX_TRANSFER, capability.getEmber()), true);
			capability.removeAmount(emberAdded, true);
			if(emberAdded > 0)
				isWorking = true;
			markDirty();
			if (getWorld().isRemote && this.capability.getEmber() > 0 && getWorld().isRemote) {
				for (int i = 0; i < Math.ceil(this.capability.getEmber() / 500.0); i++) {
					ParticleUtil.spawnParticleGlow(getWorld(), getPos().getX() + 0.25f + random.nextFloat() * 0.5f, getPos().getY() + 0.25f + random.nextFloat() * 0.5f, getPos().getZ() + 0.25f + random.nextFloat() * 0.5f, 0, 0, 0, 255, 64, 16, 2.0f, 24);
				}
			}
		}
		angle += turnRate;
	}

	@Override
	public void playSound(int id) {
		switch (id) {
			case SOUND_PROCESS:
				Embers.proxy.playMachineSound(this, SOUND_PROCESS, SoundManager.COPPER_CHARGER_LOOP, SoundCategory.BLOCKS, true, 1.0f, 1.0f, (float)pos.getX()+0.5f,(float)pos.getY()+0.5f,(float)pos.getZ()+0.5f);
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
