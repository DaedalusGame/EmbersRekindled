package teamroots.embers.tileentity;

import java.util.ArrayList;
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
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
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
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.recipe.BoreOutput;
import teamroots.embers.recipe.RecipeRegistry;
import teamroots.embers.util.EmberGenUtil;
import teamroots.embers.util.Misc;
import teamroots.embers.util.WeightedItemStack;
import teamroots.embers.util.sound.ISoundController;
import teamroots.embers.world.EmberWorldData;

public class TileEntityEmberBore extends TileEntity implements ITileEntityBase, ITickable, IMultiblockMachine, ISoundController {
	public static final int MAX_LEVEL = 7;
	public static final int BORE_TIME = 200;
	public static final int SLOT_FUEL = 8;

	public static final int SOUND_ON = 1;
	public static final int SOUND_ON_DRILL = 2;
	public static final int[] SOUND_IDS = new int[]{SOUND_ON,SOUND_ON_DRILL};

	Random random = new Random();
	public long ticksExisted = 0;
	public float angle = 0;
	public int ticksFueled = 0;
	public float lastAngle;
	boolean isRunning;

	HashSet<Integer> soundsPlaying = new HashSet<>();

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos.add(-1, -2, -1), pos.add(2, 1, 2));
	}
	
	public ItemStackHandler inventory = new EmberBoreInventory(9);
	
	public TileEntityEmberBore(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setTag("inventory", inventory.serializeNBT());
		tag.setInteger("fueled", ticksFueled);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		inventory.deserializeNBT(tag.getCompoundTag("inventory"));
		ticksFueled = tag.getInteger("fueled");
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
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, inventory);
		world.setBlockToAir(pos.add(1,0,0));
		world.setBlockToAir(pos.add(0,0,1));
		world.setBlockToAir(pos.add(-1,0,0));
		world.setBlockToAir(pos.add(0,0,-1));
		world.setBlockToAir(pos.add(1,0,-1));
		world.setBlockToAir(pos.add(-1,0,1));
		world.setBlockToAir(pos.add(1,0,1));
		world.setBlockToAir(pos.add(-1,0,-1));
		world.setTileEntity(pos, null);
	}

	public EmberBoreInventory getInventory() {
		return (EmberBoreInventory)inventory;
	}

	public boolean canMine() {
		return getPos().getY() <= MAX_LEVEL;
	}

	public boolean canInsert(ArrayList<ItemStack> returns) {
		for(ItemStack stack : returns) {
			ItemStack returned = stack;
			for(int slot = 0; slot < getInventory().getSlots()-1; slot++) {
				returned = getInventory().insertItemInternal(slot,returned,true);
			}
			if(!returned.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public void insert(ArrayList<ItemStack> returns) {
		for(ItemStack stack : returns) {
			ItemStack returned = stack;
			for(int slot = 0; slot < getInventory().getSlots()-1; slot++) {
				returned = getInventory().insertItemInternal(slot,returned,false);
			}
		}
	}

	@Override
	public void update() {
		handleSound();
		if (ticksFueled > 0){
			lastAngle = angle;
			angle += 12.0f;
		}
		if (!getWorld().isRemote){
			ticksExisted ++;
			if (ticksFueled > 0){
				ticksFueled --;
			}

			if (ticksFueled == 0){
				ItemStack fuel = inventory.getStackInSlot(SLOT_FUEL);
				if (!fuel.isEmpty()){
					ItemStack fuelCopy = fuel.copy();
					ticksFueled = TileEntityFurnace.getItemBurnTime(fuelCopy);
					fuel.shrink(1);
					if (fuel.isEmpty()){
						inventory.setStackInSlot(SLOT_FUEL, fuelCopy.getItem().getContainerItem(fuelCopy));
					}
					markDirty();
				}
			} else if(canMine()) {
				int boreTime = (int)Math.ceil(BORE_TIME);
				if (ticksExisted % boreTime == 0){
					if (random.nextFloat() < EmberGenUtil.getEmberDensity(world.getSeed(), getPos().getX(), getPos().getZ())){
						BoreOutput output = RecipeRegistry.getBoreOutput(world,getPos());
						if(output != null) {
							ArrayList<ItemStack> returns = new ArrayList<>();
							if(!output.stacks.isEmpty()) {
								WeightedItemStack picked = WeightedRandom.getRandomItem(random, output.stacks);
								returns.add(picked.getStack().copy());
							}
							if(canInsert(returns)) {
								insert(returns);
							}
						}
					}
				}
			}

			if (isRunning != ticksFueled > 0) {
				isRunning = ticksFueled > 0;
				markDirty();
			}
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

	@Override
	public void playSound(int id) {
		float soundX = (float) pos.getX() + 0.5f;
		float soundY = (float) pos.getY() - 0.5f;
		float soundZ = (float) pos.getZ() + 0.5f;
		switch (id) {
			case SOUND_ON:
				Embers.proxy.playMachineSound(this,SOUND_ON, SoundManager.BORE_LOOP, SoundCategory.BLOCKS, true, 1.0f, 1.0f, soundX, soundY, soundZ);
				break;
			case SOUND_ON_DRILL:
				Embers.proxy.playMachineSound(this,SOUND_ON_DRILL, SoundManager.BORE_LOOP_MINE, SoundCategory.BLOCKS, true, 1.0f, 1.0f, soundX, soundY, soundZ);
				break;
		}
		world.playSound(null,soundX,soundY,soundZ,SoundManager.BORE_START,SoundCategory.BLOCKS,1.0f,1.0f);
		soundsPlaying.add(id);
	}

	@Override
	public void stopSound(int id) {
		world.playSound(null, (float) pos.getX() + 0.5f, (float) pos.getY() - 0.5f, (float) pos.getZ() + 0.5f,SoundManager.BORE_STOP,SoundCategory.BLOCKS,1.0f,1.0f);
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
		return isRunning;
	}

	@Override
	public float getCurrentVolume(int id, float volume) {
		boolean isMining = canMine();

		switch (id)
		{
			case SOUND_ON: return !isMining ? 1.0f : 0.0f;
			case SOUND_ON_DRILL: return isMining ? 1.0f : 0.0f;
			default: return 0f;
		}
	}

	public class EmberBoreInventory extends ItemStackHandler {
		public EmberBoreInventory() {
		}

		public EmberBoreInventory(int size) {
			super(size);
		}

		public EmberBoreInventory(NonNullList<ItemStack> stacks) {
			super(stacks);
		}

		@Override
		protected void onContentsChanged(int slot) {
			TileEntityEmberBore.this.markDirty();
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate){
			int burntime = TileEntityFurnace.getItemBurnTime(stack);
			if (slot == SLOT_FUEL && burntime != 0){
				return super.insertItem(slot, stack, simulate);
			} else if(burntime != 0) {
				return super.insertItem(SLOT_FUEL, stack, simulate);
			}
			return stack;
		}

		public ItemStack insertItemInternal(int slot, ItemStack stack, boolean simulate){
			return super.insertItem(slot, stack, simulate);
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate){
			if (slot == SLOT_FUEL){
				return ItemStack.EMPTY;
			}
			return super.extractItem(slot, amount, simulate);
		}
	}
}
