package teamroots.embers.tileentity;

import java.util.HashSet;
import java.util.Random;

import javax.annotation.Nonnull;
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
import teamroots.embers.RegistryManager;
import teamroots.embers.SoundManager;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.util.ItemUtil;
import teamroots.embers.util.Misc;
import teamroots.embers.util.sound.ISoundController;

public class TileEntityAlchemyPedestal extends TileEntity implements ITileEntityBase, ITickable, ISoundController {
	int angle = 0;
	int turnRate = 0;
	int progress = 0;
	int ash = 0;
	int stackAsh = 0;
	int stackItem = 1;
	public ItemStackHandler inventory = new ItemStackHandler(2){
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
        	TileEntityAlchemyPedestal.this.markDirty();
        }

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			return slot == stackAsh && !ItemUtil.matchesOreDict(stack,"dustAsh") ? this.insertItem(slot + 1, stack, simulate) : super.insertItem(slot, stack, simulate);
		}
	};
	Random random = new Random();

	public static final int SOUND_PROCESS = 1;
	public static final int[] SOUND_IDS = new int[]{SOUND_PROCESS};

	HashSet<Integer> soundsPlaying = new HashSet<>();
	int active = 0;
	
	public TileEntityAlchemyPedestal(){
		super();
	}

	public void setActive(int time) {
		active = time;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setInteger("progress", 0);
		tag.setTag("inventory", inventory.serializeNBT());
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		ash = tag.getInteger("ash");
		progress = tag.getInteger("progress");
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
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return (T)this.inventory;
		}
		return super.getCapability(capability, facing);
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

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		if (!heldItem.isEmpty()){
			boolean isAsh = ItemUtil.matchesOreDict(heldItem,"dustAsh");

			if (isAsh)
				player.setHeldItem(hand, inventory.insertItem(stackAsh,heldItem,false));
			else
				player.setHeldItem(hand, inventory.insertItem(stackItem,heldItem,false));
			markDirty();
			return true;
		}
		else {
			ItemStack ashStack = inventory.getStackInSlot(stackAsh);
			ItemStack itemStack = inventory.getStackInSlot(stackItem);
			if (!ashStack.isEmpty()){
				if (!world.isRemote){
					player.setHeldItem(hand, inventory.extractItem(stackAsh, ashStack.getCount(), false));
					markDirty();
				}
				return true;
			}
			else if (!itemStack.isEmpty()) {
				if (!world.isRemote) {
					player.setHeldItem(hand, inventory.extractItem(stackItem, itemStack.getCount(), false));
					markDirty();
				}
				return true;
			}
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
		angle += turnRate;
		handleSound();
		active--;
	}

	@Override
	public void playSound(int id) {
		switch (id) {
			case SOUND_PROCESS:
				Embers.proxy.playMachineSound(this, SOUND_PROCESS, SoundManager.PEDESTAL_LOOP, SoundCategory.BLOCKS, true, 0.1f, 1.0f, (float)pos.getX()+0.5f,(float)pos.getY()+1.0f,(float)pos.getZ()+0.5f);
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
		return id == SOUND_PROCESS && active > 0;
	}
}
