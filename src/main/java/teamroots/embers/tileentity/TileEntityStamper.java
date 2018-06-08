package teamroots.embers.tileentity;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.EventManager;
import teamroots.embers.RegistryManager;
import teamroots.embers.SoundManager;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.block.BlockStamper;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageStamperFX;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.recipe.ItemStampingRecipe;
import teamroots.embers.recipe.RecipeRegistry;
import teamroots.embers.util.Misc;

public class TileEntityStamper extends TileEntity implements ITileEntityBase, ITickable {
	public static final double EMBER_COST = 80.0;
	public static final int STAMP_TIME = 70;
	public static final int RETRACT_TIME = 10;

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
        
        @Override
        public int getSlotLimit(int slot){
        	return 1;
        }
	};
	
	public TileEntityStamper(){
		super();
		capability.setEmberCapacity(8000);
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
			EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		if (!heldItem.isEmpty()) {
			if (stamp.getStackInSlot(0).isEmpty()) {
				ItemStack newStack = new ItemStack(heldItem.getItem(), 1, heldItem.getMetadata());
				if (heldItem.hasTagCompound()) {
					newStack.setTagCompound(heldItem.getTagCompound());
				}
				player.setHeldItem(hand, this.stamp.insertItem(0, newStack, false));
				markDirty();
				return true;
			}
		} else {
			if (!stamp.getStackInSlot(0).isEmpty() && !world.isRemote) {
				world.spawnEntity(new EntityItem(world, player.posX, player.posY, player.posZ, stamp.getStackInSlot(0)));
				stamp.setStackInSlot(0, ItemStack.EMPTY);
				markDirty();
				return true;
			}
		}
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		world.setTileEntity(pos, null);
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, stamp);
	}

	@Override
	public void update() {
		this.ticksExisted ++;
		prevPowered = powered;
		EnumFacing face = getWorld().getBlockState(getPos()).getValue(BlockStamper.facing);
		if (getWorld().getBlockState(getPos().offset(face,2)).getBlock() == RegistryManager.stamp_base){
			int stampTime = (int) Math.ceil(STAMP_TIME);
			int retractTime = (int) Math.ceil(RETRACT_TIME);
            if (!powered && !getWorld().isRemote && this.ticksExisted >= stampTime){
                TileEntityStampBase stamp = (TileEntityStampBase)getWorld().getTileEntity(getPos().offset(face,2));
                FluidStack fluid = null;
				IFluidHandler handler = stamp.getTank();
				if (handler != null)
					fluid = handler.drain(stamp.getCapacity(), false);
                ItemStampingRecipe recipe = getRecipe(stamp.inputs.getStackInSlot(0), fluid, this.stamp.getStackInSlot(0));
                if (recipe != null){
                    if (this.capability.getEmber() > EMBER_COST){
                        this.capability.removeAmount(EMBER_COST, true);
                        if (!world.isRemote){
                            PacketHandler.INSTANCE.sendToAll(new MessageStamperFX(getPos().offset(face,2).getX()+0.5f,getPos().offset(face,2).getY()+1.0f,getPos().offset(face,2).getZ()+0.5f));
                        }

						world.playSound(null,getPos().getX()+0.5,getPos().getY()-0.5,getPos().getZ()+0.5, SoundManager.STAMPER_DOWN, SoundCategory.BLOCKS, 1.0f, 1.0f);
						powered = true;
						ticksExisted = 0;

						ItemStack result = recipe.getResult(this,stamp.inputs.getStackInSlot(0), stamp.getFluid() != null ? new FluidStack(stamp.getFluid(), stamp.getAmount()) : null,this.stamp.getStackInSlot(0));
						stamp.inputs.extractItem(0, recipe.getInputConsumed(), false);
                        if (recipe.getFluid() != null){
                            stamp.getTank().drain(recipe.getFluid(), true);
                        }

                        BlockPos middlePos = getPos().offset(face, 1);
						BlockPos outputPos = getPos().offset(face, 3);
						TileEntity outputTile = getWorld().getTileEntity(outputPos);
						if (outputTile instanceof TileEntityBin){
                            TileEntityBin bin = (TileEntityBin) outputTile;
                            ItemStack remainder = bin.inventory.insertItem(0, result, false);
                            if (!remainder.isEmpty() && !getWorld().isRemote){
                                EntityItem item = new EntityItem(getWorld(),middlePos.getX()+0.5,middlePos.getY()+0.5,middlePos.getZ()+0.5,remainder);
                                getWorld().spawnEntity(item);
                            }
                            bin.markDirty();
                            markDirty();
                        }
                        else if (!getWorld().isRemote){
                            EntityItem item = new EntityItem(getWorld(),middlePos.getX()+0.5,middlePos.getY()+0.5,middlePos.getZ()+0.5,result);
                            getWorld().spawnEntity(item);
                        }
                        stamp.markDirty();
                    }
                }
                markDirty();
            }
            else if (powered && !getWorld().isRemote && this.ticksExisted >= retractTime){
				retract();
            }
        }
        else if (powered){
			retract();
        }
	}

	private void retract() {
		world.playSound(null,getPos().getX()+0.5,getPos().getY()-0.5,getPos().getZ()+0.5, SoundManager.STAMPER_UP, SoundCategory.BLOCKS, 1.0f, 1.0f);
		powered = false;
		ticksExisted = 0;
		markDirty();
	}

	private ItemStampingRecipe getRecipe(ItemStack input, FluidStack fluid, ItemStack stamp) {
		return RecipeRegistry.getStampingRecipe(input, fluid, stamp);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		if (capability == EmbersCapabilities.EMBER_CAPABILITY){
			return true;
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == EmbersCapabilities.EMBER_CAPABILITY){
			return (T)this.capability;
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return (T)stamp;
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
}
