package teamroots.embers.tileentity;

import java.util.Random;

import javax.annotation.Nullable;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.oredict.OreDictionary;
import teamroots.embers.EventManager;
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
	public void update() {
		TileEntityFurnaceTop furnace = (TileEntityFurnaceTop)getWorld().getTileEntity(getPos().up());
		if (furnace != null){
			if (!furnace.inventory.getStackInSlot(0).isEmpty()){
				if (progress == -1){
					progress = 200;
					markDirty();
				}
				else if (capability.getEmber() >= 1){
					capability.removeAmount(1.0, true);
					if (world.isRemote){
						if (random.nextInt(20) == 0){
							ParticleUtil.spawnParticleSpark(world, getPos().getX()+0.5f+0.125f*(random.nextFloat()-0.5f), getPos().getY()+1.25f, getPos().getZ()+0.5f+0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()), 0.125f*(random.nextFloat()-0.5f), 255, 64, 16, random.nextFloat()*0.75f+0.45f, 80);
						}
						if (random.nextInt(10) == 0){
							for (int i = 0; i < 12; i ++){
								ParticleUtil.spawnParticleSmoke(world, getPos().getX()+0.5f+0.125f*(random.nextFloat()-0.5f), getPos().getY()+1.25f, getPos().getZ()+0.5f+0.125f*(random.nextFloat()-0.5f), 0, 0.03125f+0.03125f*random.nextFloat(), 0, 64, 64, 64, 0.125f, 5.0f+3.0f*random.nextFloat(), 80);
							}
						}
					}
					progress --;
					markDirty();
					if (progress == 0){
						ItemStack stack = furnace.inventory.getStackInSlot(0);
						ItemStack recipeStack = new ItemStack(stack.getItem(),1,stack.getMetadata());
						if (stack.hasTagCompound()){
							recipeStack.setTagCompound(stack.getTagCompound());
						}
						ItemMeltingRecipe recipe = RecipeRegistry.getMeltingRecipe(recipeStack);
						if (recipe != null && !getWorld().isRemote){
							TileEntityFurnaceTop t = (TileEntityFurnaceTop)getWorld().getTileEntity(getPos().up());
							if (t.getFluid() != null){
								if (recipe.getFluid().getFluid().getName().compareTo(t.getFluid().getName()) == 0 && t.getAmount() < t.getCapacity()){
									t.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).fill(recipe.getFluid(), true);
									t.markDirty();
									if (!getWorld().isRemote){
										furnace.inventory.getStackInSlot(0).shrink(1);
										if (furnace.inventory.getStackInSlot(0).getCount() <= 0){
											furnace.inventory.setStackInSlot(0, ItemStack.EMPTY);
										}
									}
									markDirty();
									return;
								}
							}
							else {
								t.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).fill(recipe.getFluid(), true);
								t.markDirty();
								if (!getWorld().isRemote){
									furnace.inventory.getStackInSlot(0).shrink(1);
									if (furnace.inventory.getStackInSlot(0).getCount() <= 0){
										furnace.inventory.setStackInSlot(0, ItemStack.EMPTY);
									}
								}
								markDirty();
								return;
							}
						}
						ItemMeltingOreRecipe oreRecipe = RecipeRegistry.getMeltingOreRecipe(recipeStack);
						if (oreRecipe != null && !getWorld().isRemote){
							TileEntityFurnaceTop t = (TileEntityFurnaceTop)getWorld().getTileEntity(getPos().up());
							if (t.getFluid() != null){
								if (oreRecipe.getFluid().getFluid().getName().compareTo(t.getFluid().getName()) == 0 && t.getAmount() < t.getCapacity()){
									t.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).fill(oreRecipe.getFluid(), true);
									t.markDirty();
									if (!getWorld().isRemote){
										furnace.inventory.getStackInSlot(0).shrink(1);
										if (furnace.inventory.getStackInSlot(0).getCount() <= 0){
											furnace.inventory.setStackInSlot(0, ItemStack.EMPTY);
										}
									}
									markDirty();
									return;
								}
							}
							else {
								t.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).fill(oreRecipe.getFluid(), true);
								t.markDirty();
								furnace.inventory.getStackInSlot(0).shrink(1);
								if (furnace.inventory.getStackInSlot(0).getCount() <= 0){
									furnace.inventory.setStackInSlot(0, ItemStack.EMPTY);
								}
								markDirty();
								return;
							}
						}
					}
				}
			}
			else {
				if (progress != -1){
					progress = -1;
					markDirty();	
				}
			}
		}
	}
}
