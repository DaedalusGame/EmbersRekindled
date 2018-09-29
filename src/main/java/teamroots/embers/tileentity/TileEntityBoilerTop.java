package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
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
import teamroots.embers.Embers;
import teamroots.embers.EventManager;
import teamroots.embers.SoundManager;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.api.tile.IExtraCapabilityInformation;
import teamroots.embers.api.tile.IExtraDialInformation;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.util.Misc;
import teamroots.embers.util.sound.ISoundController;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class TileEntityBoilerTop extends TileEntity implements ITileEntityBase, ITickable, ISoundController, IExtraDialInformation, IExtraCapabilityInformation {
	public IEmberCapability capability = new DefaultEmberCapability() {
		@Override
		public void onContentsChanged() {
			TileEntityBoilerTop.this.markDirty();
		}
	};
	Random random = new Random();
	int progress = -1;

	public static final int SOUND_HAS_EMBER = 1;
	public static final int[] SOUND_IDS = new int[]{SOUND_HAS_EMBER};

	HashSet<Integer> soundsPlaying = new HashSet<>();
	
	public TileEntityBoilerTop(){
		super();
		capability.setEmberCapacity(32000);
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
	public void markDirty() {
		super.markDirty();
		Misc.syncTE(this);
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
	public void update() {
		if(getWorld().isRemote)
			handleSound();
		if (this.capability.getEmber() > 0 && getWorld().isRemote){
			for (int i = 0; i < Math.ceil(this.capability.getEmber()/500.0); i ++){
				ParticleUtil.spawnParticleGlow(getWorld(), getPos().getX()+0.25f+random.nextFloat()*0.5f, getPos().getY()+0.25f+random.nextFloat()*0.5f, getPos().getZ()+0.25f+random.nextFloat()*0.5f, 0, 0, 0, 255, 64, 16, 2.0f, 24);
			}
		}
	}

	@Override
	public void playSound(int id) {
		switch (id) {
			case SOUND_HAS_EMBER:
				Embers.proxy.playMachineSound(this,SOUND_HAS_EMBER, SoundManager.GENERATOR_LOOP, SoundCategory.BLOCKS, true, 1.0f, 1.0f, (float)pos.getX()+0.5f,(float)pos.getY()+0.5f,(float)pos.getZ()+0.5f);
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
		return id == SOUND_HAS_EMBER && capability.getEmber() > 0;
	}

	@Override
	public void addDialInformation(EnumFacing facing, List<String> information, String dialType) {
		TileEntity bottom = world.getTileEntity(pos.down());
		if(bottom instanceof TileEntityBoilerBottom)
			((TileEntityBoilerBottom) bottom).addDialInformation(facing,information,dialType);
	}

	@Override
	public boolean hasCapabilityDescription(Capability<?> capability) {
		return true;
	}

	@Override
	public void addCapabilityDescription(List<String> strings, Capability<?> capability, EnumFacing facing) {
		if(capability == EmbersCapabilities.EMBER_CAPABILITY)
			strings.add(IExtraCapabilityInformation.formatCapability(EnumIOType.OUTPUT,"embers.tooltip.goggles.ember",null));
	}
}
