package teamroots.embers.tileentity;

import com.google.common.collect.TreeMultimap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.util.EnumPipeConnection;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class TileEntityItemPipeBase extends TileEntity implements ITileEntityBase, ITickable, IItemPipeConnectable, IItemPipePriority {
    public static final int PRIORITY_BLOCK = 0;
    public static final int PRIORITY_PIPE = PRIORITY_BLOCK;

    Random random = new Random();
    boolean[] from = new boolean[EnumFacing.VALUES.length];
    boolean clogged = false;
    public ItemStackHandler inventory;
    boolean syncInventory;
    boolean syncCloggedFlag;
    int ticksExisted = 0;

    protected TileEntityItemPipeBase() {
        initInventory();
    }

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
        };
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        if (requiresSync()) {
            NBTTagCompound updateTag = getSyncTag();
            resetSync();
            return new SPacketUpdateTileEntity(getPos(), 0, updateTag);
        }
        return null;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    abstract int getCapacity();

    @Override
    public int getPriority(EnumFacing facing) {
        return PRIORITY_PIPE;
    }

    public abstract EnumPipeConnection getInternalConnection(EnumFacing facing);

    abstract void setInternalConnection(EnumFacing facing, EnumPipeConnection connection);

    /**
     * @param facing
     * @return Whether items can be transferred through this side
     */
    abstract boolean isConnected(EnumFacing facing);

    public void setFrom(EnumFacing facing, boolean flag) {
        from[facing.getIndex()] = flag;
    }

    public void resetFrom() {
        for (EnumFacing facing : EnumFacing.VALUES) {
            setFrom(facing, false);
        }
    }

    protected boolean isFrom(EnumFacing facing) {
        return from[facing.getIndex()];
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            ticksExisted++;
            boolean itemsMoved = false;
            ItemStack passStack = this.inventory.extractItem(0, 1, true);
            if (!passStack.isEmpty()) {
                TreeMultimap<Integer, EnumFacing> possibleDirections = TreeMultimap.create();
                IItemHandler[] itemHandlers = new IItemHandler[EnumFacing.VALUES.length];

                for (EnumFacing facing : EnumFacing.VALUES) {
                    if (!isConnected(facing))
                        continue;
                    if (isFrom(facing))
                        continue;
                    TileEntity tile = world.getTileEntity(pos.offset(facing));
                    if (tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite())) {
                        IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
                        int priority = PRIORITY_BLOCK;
                        if (tile instanceof IItemPipePriority)
                            priority = ((IItemPipePriority) tile).getPriority(facing.getOpposite());
                        priority += random.nextInt(6);
                        //if (facing == EnumFacing.UP)
                        //    priority += 3; //aka go up last
                        //if (facing == EnumFacing.DOWN)
                        //    priority -= 3; //aka always go down first
                        if (isFrom(facing.getOpposite()))
                            priority -= 5; //aka always try opposite first
                        //if (facing.getAxis().isHorizontal() && isFrom(facing.rotateY()))
                        //    priority -= 1; //aka always turn right (probably)
                        //if (isFrom(facing))
                        //    priority += 50; //aka don't get stuck
                        possibleDirections.put(priority, facing);
                        itemHandlers[facing.getIndex()] = handler;
                    }
                }

                for (EnumFacing facing : possibleDirections.values()) {
                    IItemHandler handler = itemHandlers[facing.getIndex()];
                    itemsMoved = pushStack(passStack, facing, handler);
                    if(itemsMoved)
                        break;
                }
            }

            if (itemsMoved)
                resetFrom();
            if (inventory.getStackInSlot(0).isEmpty()) {
                itemsMoved = true;
            }
            if (clogged == itemsMoved) {
                clogged = !itemsMoved;
                syncCloggedFlag = true;
                markDirty();
            }
        }
    }

    private boolean pushStack(ItemStack passStack, EnumFacing facing, IItemHandler handler) {
        int slot = -1;
        for (int j = 0; j < handler.getSlots() && slot == -1; j++) {
            if (handler.insertItem(j, passStack, true).isEmpty()) {
                slot = j;
            }
        }

        if (slot != -1) {
            ItemStack added = handler.insertItem(slot, passStack, false);
            if (added.isEmpty()) {
                this.inventory.extractItem(0, 1, false);
                return true;
            }
        }

        if(isFrom(facing))
            setFrom(facing,false);
        return false;
    }

    protected void resetSync() {
        syncInventory = false;
        syncCloggedFlag = false;
    }

    protected boolean requiresSync() {
        return syncInventory || syncCloggedFlag;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    protected NBTTagCompound getSyncTag() {
        NBTTagCompound compound = super.getUpdateTag();
        if (syncInventory)
            writeInventory(compound);
        if (syncCloggedFlag)
            writeCloggedFlag(compound);
        return compound;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        writeInventory(tag);
        writeCloggedFlag(tag);
        return tag;
    }

    private void writeCloggedFlag(NBTTagCompound tag) {
        tag.setBoolean("clogged", clogged);
    }

    private void writeInventory(NBTTagCompound tag) {
        tag.setTag("inventory", inventory.serializeNBT());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("clogged"))
            clogged = tag.getBoolean("clogged");
        if (tag.hasKey("inventory"))
            inventory.deserializeNBT(tag.getCompoundTag("inventory"));
    }
}
