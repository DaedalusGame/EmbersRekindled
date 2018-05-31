package teamroots.embers.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.EventManager;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.recipe.HeatCoilRecipe;
import teamroots.embers.recipe.RecipeRegistry;

public class TileEntityHeatCoil extends TileEntity implements ITileEntityBase, ITickable, IMultiblockMachine {
	public static final double EMBER_COST = 1.0;
	public static final double HEATING_SPEED = 1.0;
	public static final double COOLING_SPEED = 1.0;
	public static final double MAX_HEAT = 280;
	public static final int MIN_COOK_TIME = 20;
	public static final int MAX_COOK_TIME = 300;

	public IEmberCapability capability = new DefaultEmberCapability();
	public ItemStackHandler inventory = new ItemStackHandler(1);
	protected Random random = new Random();
	protected int progress = 0;
	public double heat = 0;
	protected int ticksExisted = 0;
	
	public TileEntityHeatCoil(){
		super();
		capability.setEmberCapacity(8000);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		capability.writeToNBT(tag);
		tag.setInteger("progress", progress);
		tag.setDouble("heat", heat);
		tag.setTag("inventory", inventory.serializeNBT());
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		capability.readFromNBT(tag);
		inventory.deserializeNBT(tag.getCompoundTag("inventory"));
		if (tag.hasKey("progress")){
			progress = tag.getInteger("progress");
		}
		if (tag.hasKey("heat")){
			heat = tag.getDouble("heat");
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
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		if (capability == EmberCapabilityProvider.emberCapability){
			return true;
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == EmberCapabilityProvider.emberCapability){
			return (T)this.capability;
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return (T)this.inventory;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void update() {
		ticksExisted ++;

		if (capability.getEmber() >= EMBER_COST){
			capability.removeAmount(EMBER_COST, true);
			if (ticksExisted % 20 == 0){
				heat += HEATING_SPEED;
			}
		}
		else {
			if (ticksExisted % 20 == 0){
				heat -= COOLING_SPEED;
			}
		}
		heat = MathHelper.clamp(heat,0, MAX_HEAT);

		int cookTime = (int)Math.ceil(MathHelper.clampedLerp(MIN_COOK_TIME,MAX_COOK_TIME,1.0-(heat / MAX_HEAT)));
		if (heat > 0 && ticksExisted % cookTime == 0 && !getWorld().isRemote){
			List<EntityItem> items = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(getPos().getX()-1,getPos().getY(),getPos().getZ()-1,getPos().getX()+2,getPos().getY()+2,getPos().getZ()+2));
			for (EntityItem item : items) {
				item.setAgeToCreativeDespawnTime();
				item.lifespan = 10800;
			}
			if (items.size() > 0){
				int i = random.nextInt(items.size());
				EntityItem entityItem = items.get(i);
				HeatCoilRecipe recipe = RecipeRegistry.getHeatCoilRecipe(entityItem.getItem());
				if (recipe != null){
					ArrayList<ItemStack> returns = Lists.newArrayList(recipe.getResult(this, entityItem.getItem()));
					int inputCount = recipe.getInputConsumed();
					depleteItem(entityItem, inputCount);
					boolean dirty = false;
					for(ItemStack stack : returns) {
						ItemStack remainder = inventory.insertItem(0, stack, false);
						dirty = true;
						if (!remainder.isEmpty())
							getWorld().spawnEntity(new EntityItem(getWorld(), entityItem.posX, entityItem.posY, entityItem.posZ, remainder));
					}
					if(dirty)
						markDirty();
				}
			}
		}
		if (getWorld().isRemote && heat > 0){
			float particleCount = (1+random.nextInt(2))*(1+(float)Math.sqrt(heat));
			for (int i = 0; i < particleCount; i ++){
				ParticleUtil.spawnParticleGlow(getWorld(), getPos().getX()-0.2f+random.nextFloat()*1.4f, getPos().getY()+1.275f, getPos().getZ()-0.2f+random.nextFloat()*1.4f, 0, 0, 0, 255, 64, 16, 2.0f, 24);
			}
		}
	}

	public void depleteItem(EntityItem entityItem, int inputCount) {
		ItemStack stack = entityItem.getItem();
		stack.shrink(inputCount);
		entityItem.setItem(stack);
		if (stack.isEmpty()) {
			entityItem.setDead();
			for (int j = 0; j < 3; j++) {
				if (random.nextBoolean()) {
					getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, entityItem.posX, entityItem.posY, entityItem.posZ, 0, 0, 0, 0);
				} else {
					getWorld().spawnParticle(EnumParticleTypes.SMOKE_LARGE, entityItem.posX, entityItem.posY, entityItem.posZ, 0, 0, 0, 0);
				}
			}
			getWorld().removeEntity(entityItem);
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
}
