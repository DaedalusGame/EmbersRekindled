package teamroots.embers.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.Explosion;
import teamroots.embers.Embers;
import teamroots.embers.SoundManager;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.api.projectile.EffectDamage;
import teamroots.embers.entity.EntityEmberProjectile;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageEmberSphereFX;
import teamroots.embers.network.message.MessageFireBlastFX;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.util.EnumPipeConnection;
import teamroots.embers.util.Misc;
import teamroots.embers.util.PipePriorityMap;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public abstract class TileEntityEmberPipeBase extends TileEntity implements ITileEntityBase, ITickable, IEmberPipeConnectable, IEmberPipePriority {
    public static final int PRIORITY_BLOCK = 0;
    public static final int PRIORITY_PIPE = PRIORITY_BLOCK;

    Random random = new Random();
    boolean[] from = new boolean[EnumFacing.VALUES.length];
    boolean clogged = false;
    double packet;
    EnumFacing lastTransfer;
    boolean syncPacket;
    boolean syncCloggedFlag;
    boolean syncTransfer;
    int ticksExisted;
    int lastRobin;

    protected TileEntityEmberPipeBase() {

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
            if (tile instanceof TileEntityEmberPipeBase && !((TileEntityEmberPipeBase) tile).clogged)
                return true;
        }
        return false;
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            ticksExisted++;
            boolean packetMoved = false;
            if (packet > 0) {
                PipePriorityMap<Integer, EnumFacing> possibleDirections = new PipePriorityMap<>();
                Object[] emberCapabilities = new Object[EnumFacing.VALUES.length];

                for (EnumFacing facing : EnumFacing.VALUES) {
                    if (!isConnected(facing))
                        continue;
                    if (isFrom(facing))
                        continue;
                    TileEntity tile = world.getTileEntity(pos.offset(facing));
                    if (tile != null) {
                        IEmberCapability handler = tile.getCapability(EmbersCapabilities.EMBER_CAPABILITY, facing.getOpposite());
                        int priority = PRIORITY_BLOCK;
                        if (tile instanceof IEmberPipePriority)
                            priority = ((IEmberPipePriority) tile).getPriority(facing.getOpposite());
                        if(handler != null && handler.acceptsVolatile()) {
                            possibleDirections.put(priority, facing);
                            emberCapabilities[facing.getIndex()] = handler;
                        } else if(tile instanceof TileEntityEmberPipeBase) {
                            possibleDirections.put(priority, facing);
                            emberCapabilities[facing.getIndex()] = tile;
                        }
                    }
                }

                for (int key : possibleDirections.keySet()) {
                    ArrayList<EnumFacing> list = possibleDirections.get(key);
                    for (int i = 0; i < list.size(); i++) {
                        EnumFacing facing = list.get((i + lastRobin) % list.size());
                        Object handler = emberCapabilities[facing.getIndex()];
                        packetMoved = pushStack(facing, handler);
                        if (lastTransfer != facing) {
                            syncTransfer = true;
                            lastTransfer = facing;
                            markDirty();
                        }
                        if (packetMoved) {
                            lastRobin++;
                            break;
                        }
                    }
                    if (packetMoved)
                        break;
                }
            }

            if (packet <= 0) {
                if (lastTransfer != null && !packetMoved) {
                    syncTransfer = true;
                    lastTransfer = null;
                    markDirty();
                }
                packetMoved = true;
                resetFrom();
            }
            if (clogged == packetMoved) {
                clogged = !packetMoved;
                syncCloggedFlag = true;
                markDirty();
            }
            if(clogged) {
                if(packet > 1) {
                    packet = Math.floor(packet * 0.7);
                    syncPacket = true;
                    markDirty();
                }
                //PacketHandler.INSTANCE.sendToAll(new MessageEmberSphereFX(pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5));
            }
        } else {
            if (lastTransfer != null && isConnected(lastTransfer)) {
                for (int i = 0; i < 3; i++) {
                    float dist = random.nextFloat() * 0.0f;
                    int lifetime = 10;
                    float vx = lastTransfer.getFrontOffsetX() / (float) (lifetime / (1 - dist));
                    float vy = lastTransfer.getFrontOffsetY() / (float) (lifetime / (1 - dist));
                    float vz = lastTransfer.getFrontOffsetZ() / (float) (lifetime / (1 - dist));
                    float x = pos.getX() + 0.4f + random.nextFloat() * 0.2f + lastTransfer.getFrontOffsetX() * dist;
                    float y = pos.getY() + 0.4f + random.nextFloat() * 0.2f + lastTransfer.getFrontOffsetY() * dist;
                    float z = pos.getZ() + 0.4f + random.nextFloat() * 0.2f + lastTransfer.getFrontOffsetZ() * dist;
                    float r = 255f;
                    float g = 64f;
                    float b = 16f;
                    float size = random.nextFloat() * 2 + 2;
                    ParticleUtil.spawnParticlePipeFlow(world, x, y, z, vx, vy, vz, r, g, b, 0.5f, size, lifetime);
                }
            }
        }
    }

    private boolean pushStack(EnumFacing facing, Object handler) {
        if(handler instanceof IEmberCapability) {
            IEmberCapability emberCapability = (IEmberCapability) handler;
            double added = emberCapability.addAmount(packet, true);
            if(added > 0) {
                packet = 0;
                return true;
            }
        }

        if(handler instanceof TileEntityEmberPipeBase) {
            TileEntityEmberPipeBase pipe = (TileEntityEmberPipeBase) handler;
            if(pipe.packet <= 0) {
                pipe.packet = packet;
                pipe.setFrom(facing.getOpposite(),true);
                packet = 0;
                return true;
            } else {
                boolean isColliding = true;
                int ends = 0;
                for (EnumFacing checkFacing : EnumFacing.VALUES) {
                    if(!pipe.isConnected(checkFacing))
                        continue;
                    ends += 1;
                    if(!pipe.isFrom(checkFacing) && checkFacing != facing.getOpposite()) {
                        isColliding = false;
                        break;
                    }
                }
                if(isColliding && ends > 1) {
                    double posX = pos.getX() + 0.5;
                    double posY = pos.getY() + 0.5;
                    double posZ = pos.getZ() + 0.5;
                    world.playSound(null,pos, SoundManager.MINI_BOILER_RUPTURE, SoundCategory.BLOCKS,1.0f,1.0f); //TODO: Random pitch
                    Explosion explosion = world.newExplosion(null, posX, posY, posZ, 3f, true, false);
                    world.setBlockToAir(pos);
                    EffectDamage effect = new EffectDamage(4.0f, preset -> DamageSource.causeExplosionDamage(explosion), 10, 0.0f);
                    for(int i = 0; i < 12; i++) {
                        EntityEmberProjectile proj = new EntityEmberProjectile(world);
                        proj.initCustom(posX, posY, posZ, random.nextDouble()-0.5, random.nextDouble()-0.5, random.nextDouble()-0.5, 10.0f, null);
                        proj.setLifetime(20+random.nextInt(40));
                        proj.setEffect(effect);
                        world.spawnEntity(proj);
                    }
                }
            }
        }

        if (isFrom(facing))
            setFrom(facing, false);
        return false;
    }

    protected void resetSync() {
        syncPacket = false;
        syncCloggedFlag = false;
        syncTransfer = false;
    }

    protected boolean requiresSync() {
        return syncPacket || syncCloggedFlag || syncTransfer;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    protected NBTTagCompound getSyncTag() {
        NBTTagCompound compound = new NBTTagCompound();
        if (syncPacket)
            writePacket(compound);
        if (syncCloggedFlag)
            writeCloggedFlag(compound);
        if (syncTransfer)
            writeLastTransfer(compound);
        return compound;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        writePacket(tag);
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

    private void writePacket(NBTTagCompound tag) {
        tag.setDouble("packet", packet);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("clogged"))
            clogged = tag.getBoolean("clogged");
        if (tag.hasKey("packet"))
            packet = tag.getDouble("packet");
        if (tag.hasKey("lastTransfer"))
            lastTransfer = Misc.readNullableFacing(tag.getInteger("lastTransfer"));
        for (EnumFacing facing : EnumFacing.VALUES)
            if (tag.hasKey("from" + facing.getIndex()))
                from[facing.getIndex()] = tag.getBoolean("from" + facing.getIndex());
        if (tag.hasKey("lastRobin"))
            lastRobin = tag.getInteger("lastRobin");
    }
}
