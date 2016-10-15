package teamroots.embers.tileentity;

import java.util.ArrayList;
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
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.recipe.FluidMixingRecipe;
import teamroots.embers.recipe.ItemMeltingOreRecipe;
import teamroots.embers.recipe.ItemMeltingRecipe;
import teamroots.embers.recipe.RecipeRegistry;

public class TileEntityMixerBottom extends TileEntity implements ITileEntityBase, ITickable {
	public FluidTank north = new FluidTank(8000);
	public FluidTank south = new FluidTank(8000);
	public FluidTank east = new FluidTank(8000);
	public FluidTank west = new FluidTank(8000);
	Random random = new Random();
	int progress = -1;
	
	public TileEntityMixerBottom(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		NBTTagCompound northTank = new NBTTagCompound();
		north.writeToNBT(tag);
		tag.setTag("northTank", northTank);
		NBTTagCompound southTank = new NBTTagCompound();
		south.writeToNBT(tag);
		tag.setTag("southTank", southTank);
		NBTTagCompound eastTank = new NBTTagCompound();
		east.writeToNBT(tag);
		tag.setTag("eastTank", eastTank);
		NBTTagCompound westTank = new NBTTagCompound();
		west.writeToNBT(tag);
		tag.setTag("westTank", westTank);
		tag.setInteger("progress", progress);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		north.readFromNBT(tag.getCompoundTag("northTank"));
		south.readFromNBT(tag.getCompoundTag("southTank"));
		east.readFromNBT(tag.getCompoundTag("eastTank"));
		west.readFromNBT(tag.getCompoundTag("westTank"));
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
		super.hasCapability(capability, facing);
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != EnumFacing.UP && facing != EnumFacing.DOWN){
			return true;
		}
		return false;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
			switch (facing) {
			case DOWN:
				//
			case EAST:
				return (T)east;
			case NORTH:
				return (T)north;
			case SOUTH:
				return (T)south;
			case UP:
				//
			case WEST:
				return (T)west;
			default:
				//
				
			}
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void update() {
		TileEntityMixerTop top = (TileEntityMixerTop)getWorld().getTileEntity(getPos().up());
		if (top != null){
			if (top.capability.getEmber() >= 2.0){
				ArrayList<FluidStack> fluids = new ArrayList<FluidStack>();
				if (north.getFluid() != null){
					fluids.add(north.getFluid());
				}
				if (south.getFluid() != null){
					fluids.add(south.getFluid());
				}
				if (east.getFluid() != null){
					fluids.add(east.getFluid());
				}
				if (west.getFluid() != null){
					fluids.add(west.getFluid());
				}
				FluidMixingRecipe recipe = RecipeRegistry.getMixingRecipe(fluids);
				if (recipe != null){
					IFluidHandler tank = top.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
					int amount = tank.fill(recipe.output, false);
					if (amount != 0){
						tank.fill(recipe.getResult(fluids), true);
						top.capability.removeAmount(2.0, true);
						markDirty();
						IBlockState state = getWorld().getBlockState(getPos());
						top.markDirty();
						IBlockState topState = getWorld().getBlockState(getPos().up());
					}
				}
			}
		}
	}
}
