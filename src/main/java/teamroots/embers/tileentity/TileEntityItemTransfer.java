package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.EventManager;
import teamroots.embers.api.item.IFilterItem;
import teamroots.embers.block.BlockItemTransfer;
import teamroots.embers.util.EnumPipeConnection;
import teamroots.embers.util.Misc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class TileEntityItemTransfer extends TileEntityItemPipeBase {
    public static final int PRIORITY_TRANSFER = -10;
    double angle = 0;
    double turnRate = 1;
    public ItemStack filterItem = ItemStack.EMPTY;
    Random random = new Random();
    boolean syncFilter;
    IItemHandler outputSide;

    public TileEntityItemTransfer() {
        super();
    }

    @Override
    protected void initInventory() {
        inventory = new ItemStackHandler(1) {
            @Override
            public int getSlotLimit(int slot) {
                return getCapacity();
            }

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if (!filterItem.isEmpty()) {
                    if (acceptsItem(stack))
                        return super.insertItem(slot, stack, simulate);
                    else
                        return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
        outputSide = Misc.makeRestrictedItemHandler(inventory,false,true);
    }

    public boolean acceptsItem(ItemStack stack) {
        if (filterItem.isEmpty())
            return true;
        Item item = filterItem.getItem();
        if (item instanceof IFilterItem)
            return ((IFilterItem) item).acceptsItem(filterItem,stack);
        else
            return item == stack.getItem() && filterItem.getItemDamage() == stack.getItemDamage();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        writeFilter(tag);
        return tag;
    }

    private void writeFilter(NBTTagCompound tag) {
        if (!filterItem.isEmpty()) {
            tag.setTag("filter", filterItem.writeToNBT(new NBTTagCompound()));
        } else {
            tag.setString("filter", "empty");
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("filter")) {
            filterItem = new ItemStack(tag.getCompoundTag("filter"));
        }
    }

    @Override
    public NBTTagCompound getSyncTag() {
        NBTTagCompound compound = super.getUpdateTag();
        if (syncFilter)
            writeFilter(compound);
        return compound;
    }

    @Override
    protected boolean requiresSync() {
        return syncFilter || super.requiresSync();
    }

    @Override
    protected void resetSync() {
        super.resetSync();
        syncFilter = false;
    }

    @Override
    int getCapacity() {
        return 4;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == null || facing.getAxis() == getFacing().getAxis())
                return true;
            else
                return false;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            EnumFacing transferFacing = getFacing();
            if (facing == transferFacing)
                return (T) this.outputSide;
            else if (facing == null || facing.getAxis() == transferFacing.getAxis())
                return (T) this.inventory;
            else
                return null;
        }
        return super.getCapability(capability, facing);
    }

    private EnumFacing getFacing() {
        IBlockState state = getWorld().getBlockState(getPos());
        return state.getValue(BlockItemTransfer.facing);
    }

    @Override
    public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                            EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (!world.isRemote) {
            if (heldItem != ItemStack.EMPTY) {
                this.filterItem = heldItem.copy();
                world.setBlockState(pos, state.withProperty(BlockItemTransfer.filter, true), 10);
            } else {
                this.filterItem = ItemStack.EMPTY;
                world.setBlockState(pos, state.withProperty(BlockItemTransfer.filter, false), 10);
            }
            syncFilter = true;
            markDirty();
            return true;
        }
        return true;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        this.invalidate();
        Misc.spawnInventoryInWorld(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, inventory);
        world.setTileEntity(pos, null);
    }

    @Override
    public void update() {
        if (world.isRemote && clogged && isAnySideUnclogged())
            Misc.spawnClogParticles(world,pos,2, 0.7f);
        angle += turnRate;
        super.update();
    }

    @Override
    public int getPriority(EnumFacing facing) {
        return PRIORITY_TRANSFER;
    }

    @Override
    public EnumPipeConnection getInternalConnection(EnumFacing facing) {
        return EnumPipeConnection.NONE;
    }

    @Override
    void setInternalConnection(EnumFacing facing, EnumPipeConnection connection) {
        //NOOP
    }

    @Override
    boolean isConnected(EnumFacing facing) {
        return getFacing().getAxis() == facing.getAxis();
    }

    @Override
    protected boolean isFrom(EnumFacing facing) {
        return facing == getFacing().getOpposite();
    }

    @Override
    public void markDirty() {
        super.markDirty();
        Misc.syncTE(this);
    }

    @Override
    public EnumPipeConnection getConnection(EnumFacing facing) {
        if(getFacing().getAxis() == facing.getAxis())
            return EnumPipeConnection.PIPE;
        return EnumPipeConnection.NONE;
    }
}
