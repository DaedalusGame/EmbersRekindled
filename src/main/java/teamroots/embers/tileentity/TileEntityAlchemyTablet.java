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
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.Embers;
import teamroots.embers.SoundManager;
import teamroots.embers.api.alchemy.AlchemyResult;
import teamroots.embers.api.alchemy.AspectList;
import teamroots.embers.api.event.AlchemyResultEvent;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.api.tile.ISparkable;
import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.api.upgrades.UpgradeUtil;
import teamroots.embers.item.ItemAlchemicWaste;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageEmberSphereFX;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.recipe.AlchemyRecipe;
import teamroots.embers.recipe.RecipeRegistry;
import teamroots.embers.util.AlchemyUtil;
import teamroots.embers.util.Misc;
import teamroots.embers.util.sound.ISoundController;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class TileEntityAlchemyTablet extends TileEntity implements ITileEntityBase, ITickable, ISparkable, ISoundController {
	public static final EnumFacing[] UPGRADE_SIDES = new EnumFacing[]{EnumFacing.DOWN};
	public IEmberCapability capability = new DefaultEmberCapability();
	int angle = 0;
	int turnRate = 0;
	public int progress = 0;
	public int process = 0;
	@Deprecated
	int copper = 0, iron = 0, dawnstone = 0, silver = 0, lead = 0;
	private AspectList aspects = new AspectList();
	public ItemStackHandler north = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
        	TileEntityAlchemyTablet.this.markDirty();
        }
	};
	public ItemStackHandler south = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
        	TileEntityAlchemyTablet.this.markDirty();
        }
	};
	public ItemStackHandler east = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
        	TileEntityAlchemyTablet.this.markDirty();
        }
	};
	public ItemStackHandler west = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
        	TileEntityAlchemyTablet.this.markDirty();
        }
	};
	public ItemStackHandler center = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
        	TileEntityAlchemyTablet.this.markDirty();
        }
	};
	Random random = new Random();

	public static final int SOUND_PROCESS = 1;
	public static final int[] SOUND_IDS = new int[]{SOUND_PROCESS};

	HashSet<Integer> soundsPlaying = new HashSet<>();
	
	public TileEntityAlchemyTablet(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setInteger("progress", progress);
		tag.setTag("aspects", aspects.serializeNBT());
		tag.setTag("north", north.serializeNBT());
		tag.setTag("south", south.serializeNBT());
		tag.setTag("east", east.serializeNBT());
		tag.setTag("west", west.serializeNBT());
		tag.setTag("center", center.serializeNBT());
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		progress = tag.getInteger("progress");
		aspects.deserializeNBT(tag.getCompoundTag("aspects"));
		north.deserializeNBT(tag.getCompoundTag("north"));
		south.deserializeNBT(tag.getCompoundTag("south"));
		east.deserializeNBT(tag.getCompoundTag("east"));
		west.deserializeNBT(tag.getCompoundTag("west"));
		center.deserializeNBT(tag.getCompoundTag("center"));
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
			if (facing != EnumFacing.UP && facing != null){
				return true;
			}
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			if (facing == EnumFacing.DOWN){
				return (T)center;
			}
			if (facing == EnumFacing.NORTH){
				return (T)north;
			}
			if (facing == EnumFacing.SOUTH){
				return (T)south;
			}
			if (facing == EnumFacing.EAST){
				return (T)east;
			}
			if (facing == EnumFacing.WEST){
				return (T)west;
			}
		}
		return super.getCapability(capability, facing);
	}
	
	public int getSlotForPos(float hitX, float hitZ){
		return ((int)(hitX/0.3333))*3 + ((int)(hitZ/0.3333));
	}
	
	public ItemStackHandler getInventoryForFace(EnumFacing facing){
		if (facing == EnumFacing.DOWN){
			return center;
		}
		if (facing == EnumFacing.NORTH){
			return north;
		}
		if (facing == EnumFacing.SOUTH){
			return south;
		}
		if (facing == EnumFacing.EAST){
			return east;
		}
		if (facing == EnumFacing.WEST){
			return west;
		}
		return center;
	}

	@Deprecated
	public void sparkProgress(){
		sparkProgress(null,0);
	}

	public void sparkProgress(TileEntity tile, double ember){
		if(progress != 0)
			return;
		AlchemyRecipe recipe = getRecipe();
		if(recipe == null)
			return;
		List<TileEntityAlchemyPedestal> pedestals = AlchemyUtil.getNearbyPedestals(getWorld(),getPos());
		AspectList list = new AspectList();
		list.collect(pedestals);
		AlchemyResult result = recipe.matchAshes(list,world);
		if(result.areAllPresent()) {
			aspects.reset();
			progress = 1;
			markDirty();
			world.playSound(null,pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5,SoundManager.ALCHEMY_START, SoundCategory.BLOCKS, 1.0f, 1.0f);
		}
	}

	private AlchemyRecipe getRecipe() {
		return RecipeRegistry.getAlchemyRecipe(center.getStackInSlot(0), Lists.newArrayList(north.getStackInSlot(0),east.getStackInSlot(0),south.getStackInSlot(0),west.getStackInSlot(0)));
	}

	@Override
	public void markDirty() {
		super.markDirty();
		Misc.syncTE(this);
	}

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		if (!heldItem.isEmpty()){
			player.setHeldItem(hand, getInventoryForFace(side).insertItem(0,heldItem,false));
			markDirty();
			return true;
		}
		else {
			if (!getInventoryForFace(side).getStackInSlot(0).isEmpty()){
				if (!getWorld().isRemote){
					player.setHeldItem(hand, getInventoryForFace(side).extractItem(0, getInventoryForFace(side).getStackInSlot(0).getCount(), false));
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
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, north);
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, south);
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, east);
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, west);
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, center);
		world.setTileEntity(pos, null);
	}
	
	public List<TileEntityAlchemyPedestal> getNearbyPedestals(){
		ArrayList<TileEntityAlchemyPedestal> pedestals = new ArrayList<TileEntityAlchemyPedestal>();
		for (int i = -3; i < 4; i ++){
			for (int j = -3; j < 4; j ++){
				TileEntity tile = getWorld().getTileEntity(getPos().add(i,1,j));
				if (tile instanceof TileEntityAlchemyPedestal){
					pedestals.add((TileEntityAlchemyPedestal)tile);
				}
			}
		}
		return pedestals;
	}
	
	public int getNearbyAsh(List<TileEntityAlchemyPedestal> pedestals){
		int count = 0;
		for (TileEntityAlchemyPedestal pedestal : pedestals){
			if (!pedestal.inventory.getStackInSlot(0).isEmpty()){
				count += pedestal.inventory.getStackInSlot(0).getCount();
			}
		}
		return count;
	}

	@Override
	public void update() {
		angle += 1.0f;
		List<IUpgradeProvider> upgrades = UpgradeUtil.getUpgrades(world, pos, new EnumFacing[]{EnumFacing.DOWN}); //Defer to when events are added to the upgrade system
		UpgradeUtil.verifyUpgrades(this, upgrades);
		if(getWorld().isRemote)
			handleSound();
		if (progress == 1){
			if (process < 20){
				process ++;
			}
			List<TileEntityAlchemyPedestal> pedestals = AlchemyUtil.getNearbyPedestals(getWorld(),getPos());
			if (getWorld().isRemote){
				for (TileEntityAlchemyPedestal pedestal : pedestals) {
					if(pedestal != null && !pedestal.inventory.getStackInSlot(1).isEmpty()) //If there's ash in the pedestal
						pedestal.setActive(3);
					ParticleUtil.spawnParticleStar(getWorld(), pedestal.getPos().getX() + 0.5f, pedestal.getPos().getY() + 1.0f, pedestal.getPos().getZ() + 0.5f, 0.0125f * (random.nextFloat() - 0.5f), 0.0125f * (random.nextFloat() - 0.5f), 0.0125f * (random.nextFloat() - 0.5f), 255, 64, 16, 3.5f + 0.5f * random.nextFloat(), 40);
					for (int j = 0; j < 8; j++) {
						float coeff = random.nextFloat();
						float x = (getPos().getX() + 0.5f) * coeff + (1.0f - coeff) * (pedestal.getPos().getX() + 0.5f);
						float y = (getPos().getY() + 0.875f) * coeff + (1.0f - coeff) * (pedestal.getPos().getY() + 1.0f);
						float z = (getPos().getZ() + 0.5f) * coeff + (1.0f - coeff) * (pedestal.getPos().getZ() + 0.5f);
						ParticleUtil.spawnParticleGlow(getWorld(), x, y, z, 0.0125f * (random.nextFloat() - 0.5f), 0.0125f * (random.nextFloat() - 0.5f), 0.0125f * (random.nextFloat() - 0.5f), 255, 64, 16, 2.0f, 24);
					}
				}
			}
			if (angle % 10 == 0){
				if (getNearbyAsh(pedestals) > 0){
					TileEntityAlchemyPedestal pedestal = pedestals.get(random.nextInt(pedestals.size()));
					while (pedestal.inventory.extractItem(0, 1, true).isEmpty()){
						pedestal = pedestals.get(random.nextInt(pedestals.size()));
					}
					if (!pedestal.inventory.getStackInSlot(1).isEmpty()){
						if (getWorld().isRemote){
							for (int j = 0; j < 20; j ++){
								float dx = (getPos().getX()+0.5f) - (pedestal.getPos().getX()+0.5f);
								float dy = (getPos().getY()+0.875f) - (pedestal.getPos().getY()+1.0f);
								float dz = (getPos().getZ()+0.5f) - (pedestal.getPos().getZ()+0.5f);
								float lifetime = random.nextFloat()*24.0f+24.0f;
								ParticleUtil.spawnParticleStar(getWorld(), pedestal.getPos().getX()+0.5f, pedestal.getPos().getY()+1.0f, pedestal.getPos().getZ()+0.5f, dx/lifetime, dy/lifetime, dz/lifetime, 255, 64, 16, 4.0f, (int)lifetime);
							}
						}
						pedestal.inventory.extractItem(0, 1, false);
						aspects.addAspect(AlchemyUtil.getAspect(pedestal.inventory.getStackInSlot(1)),1);
						markDirty();
						pedestal.markDirty();
					}
				}
				else {
					AlchemyRecipe recipe = getRecipe();
					if (recipe != null && !getWorld().isRemote){
						AlchemyResult result = recipe.matchAshes(aspects,world);
						ItemStack failure = recipe.isFailure(result) ? result.createFailure() : ItemStack.EMPTY;

						AlchemyResultEvent event = new AlchemyResultEvent(this,result,true,recipe.isFailure(result),failure);
						UpgradeUtil.throwEvent(this,event,upgrades);

						ItemStack stack = event.isFailure() ? event.getFailureStack() : recipe.getResult(this);
						SoundEvent finishSound = event.isFailure() ? SoundManager.ALCHEMY_FAIL : SoundManager.ALCHEMY_SUCCESS;
						world.playSound(null,pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5, finishSound, SoundCategory.BLOCKS, 1.0f, 1.0f);
						getWorld().spawnEntity(new EntityItem(getWorld(),getPos().getX()+0.5,getPos().getY()+1.0f,getPos().getZ()+0.5,stack));
						PacketHandler.INSTANCE.sendToAll(new MessageEmberSphereFX(getPos().getX()+0.5,getPos().getY()+0.875,getPos().getZ()+0.5));

						this.progress = 0;
						if(event.shouldConsumeIngredients()) {
							this.center.setStackInSlot(0, decrStack(this.center.getStackInSlot(0)));
							this.north.setStackInSlot(0, decrStack(this.north.getStackInSlot(0)));
							this.south.setStackInSlot(0, decrStack(this.south.getStackInSlot(0)));
							this.east.setStackInSlot(0, decrStack(this.east.getStackInSlot(0)));
							this.west.setStackInSlot(0, decrStack(this.west.getStackInSlot(0)));
						}

						markDirty();
					}
				}
			}
		}
		if (progress == 0){
			if (process > 0){
				process --;
			}
		}
	}
	
	public ItemStack decrStack(ItemStack stack){
		if (!stack.isEmpty()){
			stack.shrink(1);
			if (stack.getCount() == 0){
				return ItemStack.EMPTY;
			}
		}
		return stack;
	}

	@Override
	public void playSound(int id) {
		switch (id) {
			case SOUND_PROCESS:
				Embers.proxy.playMachineSound(this, SOUND_PROCESS, SoundManager.ALCHEMY_LOOP, SoundCategory.BLOCKS, true, 1.5f, 1.0f, (float)pos.getX()+0.5f,(float)pos.getY()+1.0f,(float)pos.getZ()+0.5f);
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
		return id == SOUND_PROCESS && progress > 0;
	}
}
