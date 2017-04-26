package teamroots.embers.tileentity;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.RegistryManager;
import teamroots.embers.block.BlockInfernoForge;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageEmberActivationFX;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.util.EmberGenUtil;
import teamroots.embers.util.ItemModUtil;
import teamroots.embers.util.Misc;

public class TileEntityInfernoForge extends TileEntity implements ITileEntityBase, ITickable, IMultiblockMachine {
	public IEmberCapability capability = new DefaultEmberCapability();
	Random random = new Random();
	int progress = 0;
	int heat = 0;
	int ticksExisted = 0;
	
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
		if (capability == EmberCapabilityProvider.emberCapability){
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == EmberCapabilityProvider.emberCapability){
			return (T)this.capability;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void update() {
		ticksExisted ++;
		if (progress > 0){
			if (capability.getEmber() >= 16.0){
				capability.removeAmount(16.0, true);
				progress --;
				if (getWorld().isRemote){
					if (random.nextInt(10) == 0){
						if (random.nextInt(3) == 0){
							ParticleUtil.spawnParticleSpark(world, getPos().getX()-0.5f+0.125f*(random.nextFloat()-0.5f), getPos().getY()+1.75f, getPos().getZ()-0.5f+0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()), 0.125f*(random.nextFloat()-0.5f), 255, 64, 16, random.nextFloat()*0.75f+0.45f, 80);
						}
						if (random.nextInt(3) == 0){
							ParticleUtil.spawnParticleSpark(world, getPos().getX()+1.5f+0.125f*(random.nextFloat()-0.5f), getPos().getY()+1.75f, getPos().getZ()-0.5f+0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()), 0.125f*(random.nextFloat()-0.5f), 255, 64, 16, random.nextFloat()*0.75f+0.45f, 80);
						}
						if (random.nextInt(3) == 0){
							ParticleUtil.spawnParticleSpark(world, getPos().getX()+1.5f+0.125f*(random.nextFloat()-0.5f), getPos().getY()+1.75f, getPos().getZ()+1.5f+0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()), 0.125f*(random.nextFloat()-0.5f), 255, 64, 16, random.nextFloat()*0.75f+0.45f, 80);
						}
						if (random.nextInt(3) == 0){
							ParticleUtil.spawnParticleSpark(world, getPos().getX()-0.5f+0.125f*(random.nextFloat()-0.5f), getPos().getY()+1.75f, getPos().getZ()+1.5f+0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()), 0.125f*(random.nextFloat()-0.5f), 255, 64, 16, random.nextFloat()*0.75f+0.45f, 80);
						}
					}
					ParticleUtil.spawnParticleSmoke(getWorld(), (float)getPos().getX()-0.3f, (float)getPos().getY()+1.85f, (float)getPos().getZ()-0.3f, 0.025f*(random.nextFloat()-0.5f), 0.05f*(random.nextFloat()+1.0f), 0.025f*(random.nextFloat()-0.5f), 72, 72, 72, 1.0f, 3.0f+3.0f*random.nextFloat(), 48);
					ParticleUtil.spawnParticleSmoke(getWorld(), (float)getPos().getX()+1.3f, (float)getPos().getY()+1.85f, (float)getPos().getZ()-0.3f, 0.025f*(random.nextFloat()-0.5f), 0.05f*(random.nextFloat()+1.0f), 0.025f*(random.nextFloat()-0.5f), 72, 72, 72, 1.0f, 3.0f+3.0f*random.nextFloat(), 48);
					ParticleUtil.spawnParticleSmoke(getWorld(), (float)getPos().getX()+1.3f, (float)getPos().getY()+1.85f, (float)getPos().getZ()+1.3f, 0.025f*(random.nextFloat()-0.5f), 0.05f*(random.nextFloat()+1.0f), 0.025f*(random.nextFloat()-0.5f), 72, 72, 72, 1.0f, 3.0f+3.0f*random.nextFloat(), 48);
					ParticleUtil.spawnParticleSmoke(getWorld(), (float)getPos().getX()-0.3f, (float)getPos().getY()+1.85f, (float)getPos().getZ()+1.3f, 0.025f*(random.nextFloat()-0.5f), 0.05f*(random.nextFloat()+1.0f), 0.025f*(random.nextFloat()-0.5f), 72, 72, 72, 1.0f, 3.0f+3.0f*random.nextFloat(), 48);
					/*if (random.nextInt(3) == 0){
						ParticleUtil.spawnParticleGlow(getWorld(), (float)getPos().getX()-0.3f, (float)getPos().getY()+1.85f, (float)getPos().getZ()-0.3f, 0.0125f*(random.nextFloat()-0.5f), 0.025f*(random.nextFloat()+1.0f), 0.0125f*(random.nextFloat()-0.5f), 255, 64, 16, 2.0f, 48);
					}
					if (random.nextInt(3) == 0){
						ParticleUtil.spawnParticleGlow(getWorld(), (float)getPos().getX()+1.3f, (float)getPos().getY()+1.85f, (float)getPos().getZ()-0.3f, 0.0125f*(random.nextFloat()-0.5f), 0.025f*(random.nextFloat()+1.0f), 0.0125f*(random.nextFloat()-0.5f), 255, 64, 16, 2.0f, 48);
					}
					if (random.nextInt(3) == 0){
						ParticleUtil.spawnParticleGlow(getWorld(), (float)getPos().getX()+1.3f, (float)getPos().getY()+1.85f, (float)getPos().getZ()+1.3f, 0.0125f*(random.nextFloat()-0.5f), 0.025f*(random.nextFloat()+1.0f), 0.0125f*(random.nextFloat()-0.5f), 255, 64, 16, 2.0f, 48);
					}
					if (random.nextInt(3) == 0){
						ParticleUtil.spawnParticleGlow(getWorld(), (float)getPos().getX()-0.3f, (float)getPos().getY()+1.85f, (float)getPos().getZ()+1.3f, 0.0125f*(random.nextFloat()-0.5f), 0.025f*(random.nextFloat()+1.0f), 0.0125f*(random.nextFloat()-0.5f), 255, 64, 16, 2.0f, 48);
					}*/
				}
				List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(getPos().getX(),getPos().getY()+0.25,getPos().getZ(),getPos().getX()+1,getPos().getY()+1,getPos().getZ()+1));
				for (EntityItem e : items){
					e.setPickupDelay(20);
				}
				if (progress == 0 && !world.isRemote){
					ItemStack item = ItemStack.EMPTY;
					double emberValue = 0;
					for (int i = 0; i < items.size(); i ++){
						if (ItemModUtil.hasHeat(items.get(i).getEntityItem())){
							if (item.isEmpty() && ItemModUtil.getLevel(items.get(i).getEntityItem()) <= 5 && ItemModUtil.getHeat(items.get(i).getEntityItem()) >= ItemModUtil.getMaxHeat(items.get(i).getEntityItem())){
								item = items.get(i).getEntityItem();
							}
							else {
								progress = 0;
								markDirty();
								return;
							}
						}
						else {
							if (EmberGenUtil.getEmberForItem(items.get(i).getEntityItem().getItem()) > 0){
								emberValue += EmberGenUtil.getEmberForItem(items.get(i).getEntityItem().getItem());
							}
							else {
								progress = 0;
								markDirty();
								return;
							}
						}
					}
					if (!item.isEmpty() && emberValue > 0 && emberValue <= EmberGenUtil.getEmberForItem(RegistryManager.ember_cluster)*3.0){
						TileEntity tile = world.getTileEntity(pos.up());
						if (tile instanceof TileEntityInfernoForgeOpening){
							((TileEntityInfernoForgeOpening)tile).isOpen = true;
							((TileEntityInfernoForgeOpening)tile).prevState = false;
							((TileEntityInfernoForgeOpening)tile).markDirty();
						}
						if (!world.isRemote){
							PacketHandler.INSTANCE.sendToAll(new MessageEmberActivationFX(getPos().getX()+0.5,getPos().getY()+1.5,getPos().getZ()+0.5));
						}
						for (int i = 0; i < items.size(); i ++){
							if (!ItemModUtil.hasHeat(items.get(i).getEntityItem())){
								world.removeEntity(items.get(i));
								items.get(i).setDead();
							}
							else if (Math.atan(emberValue/1200.0f) > Misc.random.nextFloat()){
								ItemStack stack = items.get(i).getEntityItem();
								ItemModUtil.setHeat(stack, 0);
								ItemModUtil.setLevel(stack, ItemModUtil.getLevel(stack)+1);
								items.get(i).setEntityItemStack(stack);
								progress = 0;
							}
						}
					}
				}
			}
			else {
				progress = 0;
			}
			markDirty();
		}
	}
	
	public void updateProgress(){
		if (progress == 0){
			List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(getPos().getX(),getPos().getY()+0.25,getPos().getZ(),getPos().getX()+1,getPos().getY()+1,getPos().getZ()+1));
			ItemStack item = ItemStack.EMPTY;
			double emberValue = 0;
			for (int i = 0; i < items.size(); i ++){
				if (ItemModUtil.hasHeat(items.get(i).getEntityItem())){
					if (item.isEmpty() && ItemModUtil.getLevel(items.get(i).getEntityItem()) < 5 && ItemModUtil.getHeat(items.get(i).getEntityItem()) >= ItemModUtil.getMaxHeat(items.get(i).getEntityItem())){
						item = items.get(i).getEntityItem();
					}
					else {
						return;
					}
				}
				else {
					if (EmberGenUtil.getEmberForItem(items.get(i).getEntityItem().getItem()) > 0){
						emberValue += EmberGenUtil.getEmberForItem(items.get(i).getEntityItem().getItem());
					}
					else {
						return;
					}
				}
			}
			if (!item.isEmpty() && emberValue > 0 && emberValue < EmberGenUtil.getEmberForItem(RegistryManager.ember_cluster)*3.0){
				progress = 200;
				markDirty();
			}
		}
	}
	
	public boolean dirty = false;
	
	@Override
	public void markForUpdate(){
		dirty = true;
	}
	
	@Override
	public boolean needsUpdate(){
		return dirty;
	}
	
	@Override
	public void clean(){
		dirty = false;
	}
	
	@Override
	public void markDirty(){
		markForUpdate();
		super.markDirty();
	}
}
