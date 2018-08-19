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
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.api.upgrades.UpgradeUtil;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.util.Misc;
import teamroots.embers.util.sound.ISoundController;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class TileEntityCrystalCell extends TileEntity implements ITileEntityBase, ITickable, IMultiblockMachine, ISoundController {
	public static final int MAX_CAPACITY = 1440000;
	Random random = new Random();
	public long ticksExisted = 0;
	public float angle = 0;
	public long seed = 0;
	public double renderCapacity;
	public double renderCapacityLast;
	public IEmberCapability capability = new DefaultEmberCapability();
	public ItemStackHandler inventory = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
        	TileEntityCrystalCell.this.markDirty();
        	if (!TileEntityCrystalCell.this.getWorld().isRemote){
        		IBlockState state = TileEntityCrystalCell.this.getWorld().getBlockState(TileEntityCrystalCell.this.getPos());
    			markDirty();
        	}
        }
        
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate){
        	if (stack.getItem() != RegistryManager.shard_ember && stack.getItem() != RegistryManager.crystal_ember){
        		return stack;
        	}
        	return super.insertItem(slot, stack, simulate);
        }
        
	};

	public static final int SOUND_AMBIENT = 1;
	public static final int[] SOUND_IDS = new int[]{SOUND_AMBIENT};

	HashSet<Integer> soundsPlaying = new HashSet<>();

	@Override
	public void markDirty() {
		super.markDirty();
		Misc.syncTE(this);
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		double xPos = pos.getX() + 0.5;
		double yPos = pos.getY() + 0.5;
		double zPos = pos.getZ() + 0.5;
		double layerHeight = 0.25;
		double numLayers = 2+Math.floor(capability.getEmberCapacity()/128000.0);
		double size = numLayers * layerHeight;
		return new AxisAlignedBB(xPos-size/2, yPos+0.5, zPos-size/2, xPos+size/2, yPos+0.5+size, zPos+size/2);
	}
	
	public TileEntityCrystalCell(){
		super();
		capability.setEmberCapacity(64000);
		seed = random.nextLong();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setLong("seed", seed);
		tag.setTag("inventory", inventory.serializeNBT());
		capability.writeToNBT(tag);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		inventory.deserializeNBT(tag.getCompoundTag("inventory"));
		capability.readFromNBT(tag);
		seed = tag.getLong("seed");
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

	@Override
	public void update() {
		List<IUpgradeProvider> upgrades = UpgradeUtil.getUpgradesForMultiblock(world, pos, new EnumFacing[]{EnumFacing.DOWN});
		UpgradeUtil.verifyUpgrades(this, upgrades);
		if (UpgradeUtil.doTick(this, upgrades))
			return;
		if(getWorld().isRemote)
			handleSound();
		ticksExisted ++;
		renderCapacityLast = renderCapacity;
		if(renderCapacity < this.capability.getEmberCapacity())
			renderCapacity += Math.min(10000,this.capability.getEmberCapacity() - renderCapacity);
		else
			renderCapacity -= Math.min(10000,renderCapacity - this.capability.getEmberCapacity());
		if (!inventory.getStackInSlot(0).isEmpty() && ticksExisted % 4 == 0){
			boolean cancel = UpgradeUtil.doWork(this,upgrades);
			if(!cancel) {
				ItemStack stack = inventory.extractItem(0, 1, true);
				if (!getWorld().isRemote && !stack.isEmpty()) {
					inventory.extractItem(0, 1, false);
					int maxCapacity = UpgradeUtil.getOtherParameter(this, "max_capacity", MAX_CAPACITY, upgrades);
					if (EmbersAPI.getEmberValue(stack) > 0 && this.capability.getEmberCapacity() < maxCapacity) {
						this.capability.setEmberCapacity(Math.min(maxCapacity, this.capability.getEmberCapacity() + EmbersAPI.getEmberValue(stack) * 10));
						markDirty();
					}
				}
				double angle = random.nextDouble() * 2.0 * Math.PI;
				double x = getPos().getX() + 0.5 + 0.5 * Math.sin(angle);
				double z = getPos().getZ() + 0.5 + 0.5 * Math.cos(angle);
				if (getWorld().isRemote && !stack.isEmpty()) {
					double x2 = getPos().getX() + 0.5;
					double z2 = getPos().getZ() + 0.5;
					float layerHeight = 0.25f;
					float numLayers = 2 + (float) Math.floor(capability.getEmberCapacity() / 128000.0f);
					float height = layerHeight * numLayers;
					for (float i = 0; i < 72; i++) {
						float coeff = i / 72.0f;
						ParticleUtil.spawnParticleGlow(getWorld(), (float) x * (1.0f - coeff) + (float) x2 * coeff, getPos().getY() + (1.0f - coeff) + (height / 2.0f + 1.5f) * coeff, (float) z * (1.0f - coeff) + (float) z2 * coeff, 0, 0, 0, 255, 64, 16, 2.0f, 24);
					}
				}
				world.playSound(null, x, pos.getY() + 0.5, z, SoundManager.CRYSTAL_CELL_GROW, SoundCategory.BLOCKS, 1.0f, 1.0f + random.nextFloat());
			}
		}
		float numLayers = 2+(float) Math.floor(capability.getEmberCapacity()/128000.0f);
		for (int i = 0; i < numLayers/2; i ++){
			float layerHeight = 0.25f;
			float height = layerHeight*numLayers;
			float xDest = getPos().getX()+0.5f;
			float yDest = getPos().getY()+height/2.0f+1.5f;
			float zDest = getPos().getZ()+0.5f;
			float x = getPos().getX()+0.5f+2.0f*(random.nextFloat()-0.5f);
			float z = getPos().getZ()+0.5f+2.0f*(random.nextFloat()-0.5f);
			float y = getPos().getY()+1.0f;
			if (getWorld().isRemote){
				ParticleUtil.spawnParticleGlow(getWorld(), x, y, z, (xDest-x)/24.0f * random.nextFloat(), (yDest-y)/24.0f * random.nextFloat(), (zDest-z)/24.0f * random.nextFloat(), 255, 64, 16, 2.0f, 24);
			}
		}
	}

	@Override
	public void playSound(int id) {
		switch (id) {
			case SOUND_AMBIENT:
				Embers.proxy.playMachineSound(this, SOUND_AMBIENT, SoundManager.CRYSTAL_CELL_LOOP, SoundCategory.BLOCKS, true, 1.0f, 1.0f, (float)pos.getX()+0.5f,(float)pos.getY()+0.5f,(float)pos.getZ()+0.5f);
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
		return id == SOUND_AMBIENT;
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
		super.getCapability(capability, facing);
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return (T)this.inventory;
		}
		if (capability == EmbersCapabilities.EMBER_CAPABILITY){
			return (T)this.capability;
		}
		return null;
	}
}
