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
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
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
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;
import teamroots.embers.block.BlockOven;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.recipe.ItemMeltingOreRecipe;
import teamroots.embers.recipe.ItemMeltingRecipe;
import teamroots.embers.recipe.RecipeRegistry;

public class TileEntityOven extends TileEntity implements ITileEntityBase, ITickable {
	public IEmberCapability capability = new DefaultEmberCapability();
	Random random = new Random();
	int progress = -1;
	double angle = 0;
	
	public ItemStackHandler inputs = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
        	TileEntityOven.this.markDirty();
        }
	};
	
	public ItemStackHandler outputs = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
        	TileEntityOven.this.markDirty();
        }
	};
	
	public TileEntityOven(){
		super();
		capability.setEmberCapacity(8000);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		capability.writeToNBT(tag);
		tag.setInteger("progress", progress);
		tag.setTag("inputs", inputs.serializeNBT());
		tag.setTag("outputs", outputs.serializeNBT());
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		capability.readFromNBT(tag);
		if (tag.hasKey("progress")){
			progress = tag.getInteger("progress");
		}
		if (tag.hasKey("inputs")){
			inputs.deserializeNBT(tag.getCompoundTag("inputs"));
		}
		if (tag.hasKey("outputs")){
			outputs.deserializeNBT(tag.getCompoundTag("outputs"));
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
		if (heldItem != null){
			player.setHeldItem(hand, inputs.insertItem(0, heldItem, false));
			markDirty();
			getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
			return true;
		}
		else {
			if (side == state.getValue(BlockOven.facing)){
				if (inputs.getStackInSlot(0) != null && !world.isRemote){
					world.spawnEntityInWorld(new EntityItem(world,player.posX,player.posY,player.posZ,inputs.getStackInSlot(0)));
					inputs.setStackInSlot(0, null);
					markDirty();
					getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
					return true;
				}
			}
			if (side == state.getValue(BlockOven.facing).getOpposite() && !world.isRemote){
				if (outputs.getStackInSlot(0) != null){
					world.spawnEntityInWorld(new EntityItem(world,player.posX,player.posY,player.posZ,outputs.getStackInSlot(0)));
					outputs.setStackInSlot(0, null);
					markDirty();
					getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		world.setTileEntity(pos, null);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		super.hasCapability(capability, facing);
		if (capability == EmberCapabilityProvider.emberCapability){
			return true;
		}
		return false;
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
		angle ++;
		if (inputs.getStackInSlot(0) != null){
			if (progress == -1){
				progress = 80;
				markDirty();
				getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);	
			}
			else if (capability.getEmber() >= 1){
				capability.removeAmount(1.0, true);
				progress --;
				markDirty();
				getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);	
				if (progress == 0){
					ItemStack stack = inputs.getStackInSlot(0);
					int[] ids = OreDictionary.getOreIDs(stack);
					for (int i = 0; i < ids.length; i ++){
						List<ItemStack> items = OreDictionary.getOres(OreDictionary.getOreName(ids[i]));
						for (int j = 0; j < items.size(); j ++){
							if (FurnaceRecipes.instance().getSmeltingList().containsKey(items.get(j))){
								ItemStack result = FurnaceRecipes.instance().getSmeltingList().get(items.get(j));
								if (outputs.getStackInSlot(0) != null){
									ItemStack added = outputs.insertItem(0, result, false);
									if (added != null){
										if (added.stackSize != stack.stackSize){
											return;
										}
									}
									inputs.extractItem(0, added.stackSize, false);
									markDirty();
									IBlockState state = getWorld().getBlockState(getPos());
									getWorld().notifyBlockUpdate(getPos(), state, state, 3);
								}
								else {
									ItemStack properResult = new ItemStack(result.getItem(),result.stackSize,result.getMetadata());
									if (result.hasTagCompound()){
										properResult.setTagCompound(result.getTagCompound());
									}
									outputs.setStackInSlot(0, properResult);
									markDirty();
									IBlockState state = getWorld().getBlockState(getPos());
									getWorld().notifyBlockUpdate(getPos(), state, state, 3);
								}
							}
						}
					}
				}
			}
		}
		else {
			if (progress != -1){
				progress = -1;
				markDirty();
				getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);	
			}
		}
	}
}
