package teamroots.embers.tileentity;

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
import teamroots.embers.Embers;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.util.EnumPipeConnection;
import teamroots.embers.util.Misc;
import teamroots.embers.util.PipePriorityMap;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Random;

public abstract class TileEntityFluidPipeBase extends TileEntity implements ITileEntityBase, ITickable, IFluidPipeConnectable, IFluidPipePriority {
    public static final int PRIORITY_BLOCK = 0;
    public static final int PRIORITY_PIPE = PRIORITY_BLOCK;
    public static final int MAX_PUSH = 250;

    Random random = new Random();
    boolean[] from = new boolean[EnumFacing.VALUES.length];
    boolean clogged = false;
    public FluidTank tank;
    EnumFacing lastTransfer;
    boolean syncTank;
    boolean syncCloggedFlag;
    boolean syncTransfer;
    int ticksExisted;
    int lastRobin;

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

    protected boolean isAnySideUnclogged()
    {
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (!isConnected(facing))
                continue;
            TileEntity tile = world.getTileEntity(pos.offset(facing));
            if (tile instanceof TileEntityFluidPipeBase && !((TileEntityFluidPipeBase) tile).clogged)
                return true;
        }
        return false;
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
                        if (tile instanceof IFluidPipePriority)
                            priority = ((IFluidPipePriority) tile).getPriority(facing.getOpposite());
                        if (isFrom(facing.getOpposite()))
                            priority -= 5; //aka always try opposite first
                        possibleDirections.put(priority, facing);
                        fluidHandlers[facing.getIndex()] = handler;
                    }
                }

                for (int key : possibleDirections.keySet()) {
                    ArrayList<EnumFacing> list = possibleDirections.get(key);
                    for (int i = 0; i < list.size(); i++) {
                        EnumFacing facing = list.get((i + lastRobin) % list.size());
                        IFluidHandler handler = fluidHandlers[facing.getIndex()];
                        fluidMoved = pushStack(passStack, facing, handler);
                        if (lastTransfer != facing) {
                            syncTransfer = true;
                            lastTransfer = facing;
                            markDirty();
                        }
                        if (fluidMoved) {
                            lastRobin++;
                            break;
                        }
                    }
                    if (fluidMoved)
                        break;
                }
            }

            //if (fluidMoved)
            //    resetFrom();
            if (tank.getFluidAmount() <= 0) {
                if (lastTransfer != null && !fluidMoved) {
                    syncTransfer = true;
                    lastTransfer = null;
                    markDirty();
                }
                fluidMoved = true;
                resetFrom();
            }
            if (clogged == fluidMoved) {
                clogged = !fluidMoved;
                syncCloggedFlag = true;
                markDirty();
            }
        } else if (Embers.proxy.isPlayerWearingGoggles()) {
            if (lastTransfer != null) {
                for (int i = 0; i < 3; i++) {
                    float dist = random.nextFloat() * 0.0f;
                    int lifetime = 10;
                    float vx = lastTransfer.getFrontOffsetX() / (lifetime / (1 - dist));
                    float vy = lastTransfer.getFrontOffsetY() / (lifetime / (1 - dist));
                    float vz = lastTransfer.getFrontOffsetZ() / (lifetime / (1 - dist));
                    float x = pos.getX() + 0.4f + random.nextFloat() * 0.2f + lastTransfer.getFrontOffsetX() * dist;
                    float y = pos.getY() + 0.4f + random.nextFloat() * 0.2f + lastTransfer.getFrontOffsetY() * dist;
                    float z = pos.getZ() + 0.4f + random.nextFloat() * 0.2f + lastTransfer.getFrontOffsetZ() * dist;
                    float r = clogged ? 255f : 16f;
                    float g = clogged ? 16f : 255f;
                    float b = 16f;
                    float size = random.nextFloat() * 2 + 2;
                    ParticleUtil.spawnParticlePipeFlow(world, x, y, z, vx, vy, vz, r, g, b, 0.5f, size, lifetime);
                }
            }
        }
    }

    private boolean pushStack(FluidStack passStack, EnumFacing facing, IFluidHandler handler) {
        int added = handler.fill(passStack, false);
        if (added > 0) {
            handler.fill(passStack, true);
            this.tank.drain(added, true);
            passStack.amount -= added;
            return passStack.amount <= 0;
        }

        if (isFrom(facing))
            setFrom(facing, false);
        return false;
    }

    protected void resetSync() {
        syncTank = false;
        syncCloggedFlag = false;
        syncTransfer = false;
    }

    protected boolean requiresSync() {
        return syncTank || syncCloggedFlag || syncTransfer;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    protected NBTTagCompound getSyncTag() {
        NBTTagCompound compound = new NBTTagCompound();
        if (syncTank)
            writeTank(compound);
        if (syncCloggedFlag)
            writeCloggedFlag(compound);
        if (syncTransfer)
            writeLastTransfer(compound);
        return compound;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        writeTank(tag);
        writeCloggedFlag(tag);
        writeLastTransfer(tag);
        for (EnumFacing facing : EnumFacing.VALUES)
            tag.setBoolean("from" + facing.getIndex(), from[facing.getIndex()]);
        tag.setInteger("lastRobin",lastRobin);
        return tag;
    }

    private void writeCloggedFlag(NBTTagCompound tag) {
        tag.setBoolean("clogged", clogged);
    }

    private void writeLastTransfer(NBTTagCompound tag) {
        tag.setInteger("lastTransfer", Misc.writeNullableFacing(lastTransfer));
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
        if (tag.hasKey("lastTransfer"))
            lastTransfer = Misc.readNullableFacing(tag.getInteger("lastTransfer"));
        for (EnumFacing facing : EnumFacing.VALUES)
            if (tag.hasKey("from" + facing.getIndex()))
                from[facing.getIndex()] = tag.getBoolean("from" + facing.getIndex());
        if (tag.hasKey("lastRobin"))
            lastRobin = tag.getInteger("lastRobin");
    }
}
