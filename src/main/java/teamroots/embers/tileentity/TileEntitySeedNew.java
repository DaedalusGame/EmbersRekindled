package teamroots.embers.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;
import teamroots.embers.SoundManager;
import teamroots.embers.api.tile.IEmberInjectable;
import teamroots.embers.api.tile.IExtraCapabilityInformation;
import teamroots.embers.block.BlockSeedNew;
import teamroots.embers.util.Misc;
import teamroots.embers.util.sound.ISoundController;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class TileEntitySeedNew extends TileEntity implements ITileEntityBase, ITickable, IEmberInjectable, ISoundController, IExtraCapabilityInformation {
	BlockSeedNew type;
	protected boolean[] willSpawn;
	protected int size = 0;
	protected int xp = 0;
	protected int bonusParts = 0;
	protected int ticksExisted = 0;
	protected Random random = new Random();

	public static final int SOUND_AMBIENT = 1;
	public static final int[] SOUND_IDS = new int[]{SOUND_AMBIENT};

	HashSet<Integer> soundsPlaying = new HashSet<>();

	public TileEntitySeedNew() {
		resetSpawns();
	}

	public void resetSpawns(){
		int segments = Math.max(6 + bonusParts, 1);
		segments += getLevelBonus(getLevel(xp));
		willSpawn = new boolean[segments];
		for (int i = 0; i < willSpawn.length; i ++){
			willSpawn[i] = random.nextInt(3) == 0;
		}
	}

	private int getLevelBonus(int level) {
		if(level > 50) {
			return getLevelBonus(50) + (level-50)/25;
		} else if(level > 20) {
			return getLevelBonus(20) + (level-20)/10;
		} else if(level > 10) {
			return getLevelBonus(10) + (level-10)/5;
		} else if(level > 5) {
			return getLevelBonus(5) + (level-5)/3;
		} else {
			return (level+1)/2;
		}
	}

	public String getSpawnString(){
		String result = "";
		for (int i = 0; i < willSpawn.length; i ++){
			result += willSpawn[i] ? "1" : "0";
		}
		return result;
	}

	public void loadSpawnsFromString(String s){
		willSpawn = new boolean[s.length()];
		for (int i = 0; i < s.length(); i ++){
			willSpawn[i] = s.substring(i, i+1).compareTo("1") == 0;
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setString("spawns", getSpawnString());
		tag.setInteger("size", size);
		tag.setInteger("xp", xp);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		loadSpawnsFromString(tag.getString("spawns"));
		size = tag.getInteger("size");
		xp = tag.getInteger("xp");
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
	public void update() {
		if(getWorld().isRemote)
			handleSound();
		ticksExisted ++;
		if (size > 1000){
			size = 0;
			ItemStack[] stacks = getNuggetDrops(willSpawn.length);
			double oneAng = 360.0 / willSpawn.length;
			for (int i = 0; i < willSpawn.length; i ++){
				if (willSpawn[i] && !getWorld().isRemote){
					ItemStack nuggetStack = stacks[i];
					float offX = 0.4f*(float)Math.sin(Math.toRadians(i * oneAng));
					float offZ = 0.4f*(float)Math.cos(Math.toRadians(i * oneAng));
					world.spawnEntity(new EntityItem(world,getPos().getX()+0.5+offX,getPos().getY()+0.5f,getPos().getZ()+0.5+offZ,nuggetStack));
					world.playSound(null,getPos().getX()+0.5+offX,getPos().getY()+0.5f,getPos().getZ()+0.5+offZ, SoundManager.METAL_SEED_PING, SoundCategory.BLOCKS, 1.0f, 1.0f);
				}
			}
			markDirty();
			resetSpawns();
		}
	}

	private BlockSeedNew getBlockSeedType() {
		if(type == null) {
			Block block = getBlockType();
			if (block instanceof BlockSeedNew)
				type = (BlockSeedNew) block;
		}

		return type != null ? type : (BlockSeedNew)RegistryManager.seed_iron;
	}

	protected ItemStack[] getNuggetDrops(int n) {
		return getBlockSeedType().getNuggetDrops(this,n);
	}

	public void addExperience(int xp) {
		this.xp += xp;
	}

	public int getRequiredExperienceForLevel(int level)
	{
		return ((level*(level+1))/2)*1000;
	}

	public int getLevel(int xp)
	{
		return (int)Math.floor((Math.sqrt(5)*Math.sqrt(xp+125)-25)/50);
	}

	@Override
	public void inject(TileEntity injector, double ember) {
		size++;
		addExperience(1);
		markDirty();
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public void playSound(int id) {
		switch (id) {
			case SOUND_AMBIENT:
				Embers.proxy.playMachineSound(this, SOUND_AMBIENT, SoundManager.METAL_SEED_LOOP, SoundCategory.BLOCKS, true, 1.0f, 1.0f, (float)pos.getX()+0.5f,(float)pos.getY()+0.5f,(float)pos.getZ()+0.5f);
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
	public void markDirty() {
		super.markDirty();
		Misc.syncTE(this);
	}

	public ResourceLocation getTexture() {
		return getBlockSeedType().getTexture(this);
	}

	@Override
	public void addOtherDescription(List<String> strings, EnumFacing facing) {
		int level = getLevel(xp);
		int requiredCurrentXP = getRequiredExperienceForLevel(level);
		int requiredNextXP = getRequiredExperienceForLevel(level+1);

		strings.add(Embers.proxy.formatLocalize("embers.tooltip.crystal.level",level));
		strings.add(Embers.proxy.formatLocalize("embers.tooltip.crystal.xp",xp-requiredCurrentXP,requiredNextXP-requiredCurrentXP));
	}
}
