package teamroots.embers.tileentity;

import com.google.common.collect.TreeMultimap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import teamroots.embers.util.EnumPipeConnection;
import teamroots.embers.util.PipePriorityMap;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Random;

public abstract class TileEntityFluidPipeBase extends TileEntity implements ITileEntityBase, ITickable, IFluidPipeConnectable, IFluidPipePriority {
    public static final int PRIORITY_BLOCK = 0;
    public static final int PRIORITY_PIPE = PRIORITY_BLOCK;
    public static final int MAX_PUSH = 120;

    Random random = new Random();
    boolean[] from = new boolean[EnumFacing.VALUES.length];
    boolean clogged = false;
    public FluidTank tank;
    boolean syncTank;
    boolean syncCloggedFlag;
    int ticksExisted = 0;

    protected TileEntityFluidPipeBase() {
        initFluidTank();
    }

    protected void initFluidTank() {
        tank = new FluidTank(getCapacity()) {
            @Override
            protected void onContentsChanged() {
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
            boolean fluidMoved = false;
            FluidStack passStack = this.tank.drain(MAX_PUSH, false);
            if (passStack != null) {
                PipePriorityMap<Integer, EnumFacing> possibleDirections = new PipePriorityMap<>();
                IFluidHandler[] fluidHandlers = new IFluidHandler[EnumFacing.VALUES.length];

                for (EnumFacing facing : EnumFacing.VALUES) {
                    if (!isConnected(facing))
                        continue;
                    if (isFrom(facing))
                        continue;
                    TileEntity tile = world.getTileEntity(pos.offset(facing));
                    if (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite())) {
                        IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
                        int priority = PRIORITY_BLOCK;
                        if (tile instanceof IItemPipePriority)
                            priority = ((IItemPipePriority) tile).getPriority(facing.getOpposite());
                        priority += random.nextInt(6);
                        if (isFrom(facing.getOpposite()))
                            priority -= 5; //aka always try opposite first
                        possibleDirections.put(priority, facing);
                        fluidHandlers[facing.getIndex()] = handler;
                    }
                }

                for (int key : possibleDirections.keySet()) {
                    ArrayList<EnumFacing> list = possibleDirections.get(key);
                    for(int i = 0; i < list.size(); i++) {
                        EnumFacing facing = list.get((i+ticksExisted) % list.size());
                        IFluidHandler handler = fluidHandlers[facing.getIndex()];
                        fluidMoved = pushStack(passStack, facing, handler);
                        if(fluidMoved) {
                            break;
                        }
                    }
                    if(fluidMoved) {
                        break;
                    }
                }
            }

            if (fluidMoved)
                resetFrom();
            if (tank.getFluidAmount() <= 0) {
                fluidMoved = true;
            }
            if (clogged == fluidMoved) {
                clogged = !fluidMoved;
                syncCloggedFlag = true;
                markDirty();
            }
        }
    }

    private boolean pushStack(FluidStack passStack, EnumFacing facing, IFluidHandler handler) {
        if (handler.fill(passStack, false) > 0) {
            int added = handler.fill(passStack, true);
            if (added > 0) {
                this.tank.drain(added, true);
                passStack.amount -= added;
                return true;
            }
        }

        if(isFrom(facing))
            setFrom(facing,false);
        return false;
    }

    protected void resetSync() {
        syncTank = false;
        syncCloggedFlag = false;
    }

    protected boolean requiresSync() {
        return syncTank || syncCloggedFlag;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    protected NBTTagCompound getSyncTag() {
        NBTTagCompound compound = super.getUpdateTag();
        if (syncTank)
            writeTank(compound);
        if (syncCloggedFlag)
            writeCloggedFlag(compound);
        return compound;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        writeTank(tag);
        writeCloggedFlag(tag);
        return tag;
    }

    private void writeCloggedFlag(NBTTagCompound tag) {
        tag.setBoolean("clogged", clogged);
    }

    private void writeTank(NBTTagCompound tag) {
        tag.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("clogged"))
            clogged = tag.getBoolean("clogged");
        if (tag.hasKey("tank"))
            tank.readFromNBT(tag.getCompoundTag("tank"));
    }
}
