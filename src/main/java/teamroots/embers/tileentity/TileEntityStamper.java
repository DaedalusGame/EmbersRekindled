package teamroots.embers.tileentity;

import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockLever;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.RegistryManager;
import teamroots.embers.block.BlockEmberEmitter;
import teamroots.embers.block.BlockStamper;
import teamroots.embers.entity.EntityEmberPacket;
import teamroots.embers.item.EnumStampType;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.recipe.ItemStampingOreRecipe;
import teamroots.embers.recipe.ItemStampingRecipe;
import teamroots.embers.recipe.RecipeRegistry;

public class TileEntityStamper extends TileEntity implements ITileEntityBase, ITickable {
	public IEmberCapability capability = new DefaultEmberCapability();
	public boolean prevPowered = false;
	public boolean powered = false;
	public long ticksExisted = 0;
	Random random = new Random();
	public ItemStackHandler stamp = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
        	TileEntityStamper.this.markDirty();
        }
	};
	
	public TileEntityStamper(){
		super();
		capability.setEmberCapacity(200);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setBoolean("powered", powered);
		capability.writeToNBT(tag);
		tag.setTag("stamp",stamp.serializeNBT());
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		powered = tag.getBoolean("powered");
		capability.readFromNBT(tag);
		stamp.deserializeNBT(tag.getCompoundTag("stamp"));
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
			if (EnumStampType.getType(heldItem) != EnumStampType.TYPE_NULL){
				if (stamp.getStackInSlot(0) == null){
					ItemStack newStack = new ItemStack(heldItem.getItem(),1,heldItem.getMetadata());
					if (heldItem.hasTagCompound()){
						newStack.setTagCompound(heldItem.getTagCompound());
					}
					player.setHeldItem(hand, this.stamp.insertItem(0,newStack,false));
					markDirty();
					world.notifyBlockUpdate(pos, state, state, 3);
					return true;
				}
			}
		}
		else {
			if (stamp.getStackInSlot(0) != null && !world.isRemote){
				world.spawnEntityInWorld(new EntityItem(world,player.posX,player.posY,player.posZ,stamp.getStackInSlot(0)));
				stamp.setStackInSlot(0, null);
				markDirty();
				getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
				return true;
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
	public void update() {
		this.ticksExisted ++;
		prevPowered = powered;
		if (!getWorld().isRemote){
			EnumFacing face = getWorld().getBlockState(getPos()).getValue(BlockStamper.facing);
			if (getWorld().getBlockState(getPos().offset(face,2)).getBlock() == RegistryManager.stampBase){
				if (this.capability.getEmber() > 1.0){
					this.capability.removeAmount(1.0, true);
					if (this.ticksExisted % 80 == 60 && !powered){
						powered = true;
						TileEntityStampBase stamp = (TileEntityStampBase)getWorld().getTileEntity(getPos().offset(face,2));
						FluidStack fluid = null;
						if (stamp.getFluid() != null){
							fluid = new FluidStack(stamp.getFluid(),stamp.getAmount());
						}
						ItemStampingRecipe recipe = RecipeRegistry.getStampingRecipe(stamp.inputs.getStackInSlot(0), fluid, EnumStampType.getType(this.stamp.getStackInSlot(0)));
						if (recipe != null){
							ItemStack result = recipe.getResult(stamp.inputs.getStackInSlot(0), new FluidStack(stamp.getFluid(), stamp.getAmount()),EnumStampType.getType(this.stamp.getStackInSlot(0)));
							stamp.inputs.extractItem(0, 1, false);
							stamp.getTank().drain(recipe.getFluid(), true);
							BlockPos off = getPos().offset(face,1);
							if (getWorld().getTileEntity(getPos().offset(face,3)) instanceof TileEntityBin){
								TileEntityBin bin = (TileEntityBin)getWorld().getTileEntity(getPos().offset(face,3));
								ItemStack remainder = bin.inventory.insertItem(0, result, false);
								if (remainder != null && !getWorld().isRemote){
									EntityItem item = new EntityItem(getWorld(),off.getX()+0.5,off.getY()+0.5,off.getZ()+0.5,remainder);
									getWorld().spawnEntityInWorld(item);
								}
								bin.markDirty();
								IBlockState state = getWorld().getBlockState(getPos().offset(face,3));
								getWorld().notifyBlockUpdate(getPos().offset(face,3), state, state, 3);
							}
							else if (!getWorld().isRemote){
								EntityItem item = new EntityItem(getWorld(),off.getX()+0.5,off.getY()+0.5,off.getZ()+0.5,result);
								getWorld().spawnEntityInWorld(item);
							}
							stamp.markDirty();
							IBlockState state = getWorld().getBlockState(getPos().offset(face,2));
							getWorld().notifyBlockUpdate(getPos().offset(face,2), state, state, 3);
						}
						ItemStampingOreRecipe oreRecipe = RecipeRegistry.getStampingOreRecipe(stamp.inputs.getStackInSlot(0), fluid, EnumStampType.getType(this.stamp.getStackInSlot(0)));
						if (oreRecipe != null){
							
						}
						markDirty();
						IBlockState state = getWorld().getBlockState(getPos());
						getWorld().notifyBlockUpdate(getPos(), state, state, 3);
					}
					else if (this.ticksExisted % 80 == 0 && powered){
						powered = false;
						markDirty();
						IBlockState state = getWorld().getBlockState(getPos());
						getWorld().notifyBlockUpdate(getPos(), state, state, 3);
					}
				}
				else if (powered){
					powered = false;
					markDirty();
					IBlockState state = getWorld().getBlockState(getPos());
					getWorld().notifyBlockUpdate(getPos(), state, state, 3);
				}
			}
		}
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
		super.getCapability(capability, facing);
		if (capability == EmberCapabilityProvider.emberCapability){
			return (T)this.capability;
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return (T)stamp;
		}
		return (T)this.capability;
	}
}
