package teamroots.embers.tileentity;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.oredict.OreDictionary;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.recipe.ItemMeltingOreRecipe;
import teamroots.embers.recipe.ItemMeltingRecipe;
import teamroots.embers.recipe.RecipeRegistry;

public class TileEntityFurnaceBottom extends TileEntity implements ITileEntityBase, ITickable {
	public IEmberCapability capability = new DefaultEmberCapability();
	Random random = new Random();
	int progress = -1;
	
	public TileEntityFurnaceBottom(){
		super();
		capability.setEmberCapacity(8000);
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
		TileEntityFurnaceTop furnace = (TileEntityFurnaceTop)getWorld().getTileEntity(getPos().up());
		if (furnace != null){
			if (furnace.inventory.getStackInSlot(0) != null){
				if (progress == -1){
					progress = 200;
					markDirty();
				}
				else if (capability.getEmber() >= 1){
					capability.removeAmount(1.0, true);
					progress --;
					markDirty();
					if (progress == 0){
						ItemStack stack = furnace.inventory.getStackInSlot(0);
						ItemStack recipeStack = new ItemStack(stack.getItem(),1,stack.getMetadata());
						if (stack.hasTagCompound()){
							recipeStack.setTagCompound(stack.getTagCompound());
						}
						ItemMeltingRecipe recipe = RecipeRegistry.meltingRecipes.get(recipeStack);
						if (recipe != null && !getWorld().isRemote){
							TileEntityFurnaceTop t = (TileEntityFurnaceTop)getWorld().getTileEntity(getPos().up());
							if (t.getFluid() != null){
								if (recipe.getFluid().getFluid().getName().compareTo(t.getFluid().getName()) == 0 && t.getAmount() < t.getCapacity()){
									t.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).fill(recipe.getFluid(), true);
									t.markDirty();
									if (!getWorld().isRemote){
										PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this));
									}
									if (!getWorld().isRemote){
										PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(t));
									}
									if (!getWorld().isRemote){
										furnace.inventory.getStackInSlot(0).stackSize --;
										if (furnace.inventory.getStackInSlot(0).stackSize <= 0){
											furnace.inventory.setStackInSlot(0, null);
										}
									}
									return;
								}
							}
							else {
								t.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).fill(recipe.getFluid(), true);
								t.markDirty();
								if (!getWorld().isRemote){
									PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(t));
								}
								if (!getWorld().isRemote){
									furnace.inventory.getStackInSlot(0).stackSize --;
									if (furnace.inventory.getStackInSlot(0).stackSize <= 0){
										furnace.inventory.setStackInSlot(0, null);
									}
								}
								return;
							}
						}
						int[] ids = OreDictionary.getOreIDs(recipeStack);
						for (int j = 0; j < ids.length; j ++){
							String oreName = OreDictionary.getOreName(ids[j]);
							ItemMeltingOreRecipe oreRecipe = RecipeRegistry.meltingOreRecipes.get(oreName);
							if (oreRecipe != null && !getWorld().isRemote){
								TileEntityFurnaceTop t = (TileEntityFurnaceTop)getWorld().getTileEntity(getPos().up());
								if (t.getFluid() != null){
									if (oreRecipe.getFluid().getFluid().getName().compareTo(t.getFluid().getName()) == 0 && t.getAmount() < t.getCapacity()){
										t.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).fill(oreRecipe.getFluid(), true);
										t.markDirty();
										if (!getWorld().isRemote){
											PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(t));
										}
										if (!getWorld().isRemote){
											PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this));
										}
										if (!getWorld().isRemote){
											furnace.inventory.getStackInSlot(0).stackSize --;
											if (furnace.inventory.getStackInSlot(0).stackSize <= 0){
												furnace.inventory.setStackInSlot(0, null);
											}
										}
										return;
									}
								}
								else {
									t.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).fill(oreRecipe.getFluid(), true);
									t.markDirty();
									if (!getWorld().isRemote){
										PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(t));
									}
									if (!getWorld().isRemote){
										PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this));
									}
									furnace.inventory.getStackInSlot(0).stackSize --;
									if (furnace.inventory.getStackInSlot(0).stackSize <= 0){
										furnace.inventory.setStackInSlot(0, null);
									}
									return;
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
					if (!getWorld().isRemote){
						PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(this));
					}	
				}
			}
		}
	}
}
