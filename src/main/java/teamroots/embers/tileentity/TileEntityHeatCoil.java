package teamroots.embers.tileentity;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.recipe.ItemMeltingOreRecipe;
import teamroots.embers.recipe.ItemMeltingRecipe;
import teamroots.embers.recipe.RecipeRegistry;

public class TileEntityHeatCoil extends TileEntity implements ITileEntityBase, ITickable, IMultiblockMachine {
	public IEmberCapability capability = new DefaultEmberCapability();
	public ItemStackHandler inventory = new ItemStackHandler(1){
        
	};
	Random random = new Random();
	int progress = 0;
	int heat = 0;
	int ticksExisted = 0;
	
	public TileEntityHeatCoil(){
		super();
		capability.setEmberCapacity(8000);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		capability.writeToNBT(tag);
		tag.setInteger("progress", progress);
		tag.setInteger("heat", heat);
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
			ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
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
		super.hasCapability(capability, facing);
		if (capability == EmberCapabilityProvider.emberCapability){
			return true;
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return true;
		}
		return false;
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
		//if (getWorld().provider.getDimensionType() == DimensionType.OVERWORLD){
			ticksExisted ++;
			if (capability.getEmber() >= 1.0){
				capability.removeAmount(1.0, true);
				if (ticksExisted % 20 == 0){
					heat ++;
					if (heat > 280){
						heat = 280;
					}
				}
			}
			else {
				if (ticksExisted % 20 == 0){
					heat --;
					if (heat < 0){
						heat = 0;
					}
				}
			}
			if (heat > 0 && ticksExisted % (300-heat) == 0 && !getWorld().isRemote){
				List<EntityItem> items = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(getPos().getX()-1,getPos().getY(),getPos().getZ()-1,getPos().getX()+2,getPos().getY()+2,getPos().getZ()+2));
				for (int i = 0; i < items.size(); i ++){
					items.get(i).setAgeToCreativeDespawnTime();
					items.get(i).lifespan = 10800;
				}
				if (items.size() > 0){
					int i = random.nextInt(items.size());
					if (FurnaceRecipes.instance().getSmeltingResult(items.get(i).getEntityItem()) != null){
						ItemStack recipeStack = new ItemStack(items.get(i).getEntityItem().getItem(),1,items.get(i).getEntityItem().getMetadata());
						if (items.get(i).getEntityItem().hasTagCompound()){
							recipeStack.setTagCompound(items.get(i).getEntityItem().getTagCompound());
						}
						ItemStack stack = FurnaceRecipes.instance().getSmeltingResult(recipeStack).copy();
						ItemStack remainder = inventory.insertItem(0, stack, false);
						items.get(i).getEntityItem().stackSize --;
						if (items.get(i).getEntityItem().stackSize == 0){
							items.get(i).setDead();
							for (int j = 0; j < 3; j ++){
								if (random.nextBoolean()){
									getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, items.get(i).posX, items.get(i).posY, items.get(i).posZ, 0, 0, 0, 0);
								}
								else {
									getWorld().spawnParticle(EnumParticleTypes.SMOKE_LARGE, items.get(i).posX, items.get(i).posY, items.get(i).posZ, 0, 0, 0, 0);
								}
							}
							getWorld().removeEntity(items.get(i));
						}
						markDirty();
						IBlockState state = getWorld().getBlockState(getPos());
						getWorld().notifyBlockUpdate(getPos(), state, state, 8);
						if (remainder != null){
							getWorld().spawnEntityInWorld(new EntityItem(getWorld(),items.get(i).posX,items.get(i).posY,items.get(i).posZ,remainder));
						}
					}
				}
			}
			if (getWorld().isRemote && heat > 0){
				float particleCount = (1+random.nextInt(2))*(1+(float)Math.sqrt((double)heat));
				for (int i = 0; i < particleCount; i ++){
					ParticleUtil.spawnParticleGlow(getWorld(), getPos().getX()-0.2f+random.nextFloat()*1.4f, getPos().getY()+1.275f, getPos().getZ()-0.2f+random.nextFloat()*1.4f, 0, 0, 0, 255, 64, 16);
				}
			}
		//}
	}
}
