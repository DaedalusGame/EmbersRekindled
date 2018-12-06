package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import teamroots.embers.EventManager;
import teamroots.embers.SoundManager;
import teamroots.embers.api.tile.IExtraCapabilityInformation;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageExplosionCharmFX;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;
import java.util.Random;

public class TileEntityExplosionPedestal extends TileEntity implements ITileEntityBase, ITickable, IExtraCapabilityInformation {
    Random random = new Random();

    int active = 0;

    public TileEntityExplosionPedestal(){
        super();
    }

    public boolean isActive() {
        return this.active > 0;
    }

    public void setActive(int time) {
        active = time;
    }

    public void absorb(Explosion explosion) {
        Vec3d explosionPos = explosion.getPosition();
        setActive(20);
        if(!world.isRemote) {
            world.playSound(null, explosionPos.x, explosionPos.y, explosionPos.z, SoundManager.EXPLOSION_CHARM_ABSORB, SoundCategory.BLOCKS, 1.0f, 1.0f);
            PacketHandler.INSTANCE.sendToAll(new MessageExplosionCharmFX(explosionPos.x, explosionPos.y, explosionPos.z, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
        }
        markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);
        tag.setInteger("active", active);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag){
        super.readFromNBT(tag);
        active = tag.getInteger("active");
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
    public void markDirty() {
        super.markDirty();
        Misc.syncTE(this);
    }

    @Override
    public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        this.invalidate();
        world.setTileEntity(pos, null);
    }

    @Override
    public void update() {
        active--;
        if(!world.isRemote)
            EventManager.putExplosionCharm(world,pos);
        if(world.isRemote && isActive()) {
            for(int i = 0; i < 4; i++) {
                float dist = random.nextFloat() * (active * 1.2f)/20.0f;
                float xOffset = random.nextBoolean() ? 1 : -1;
                float zOffset = 0;
                if(random.nextBoolean()) {
                    float h = xOffset;
                    xOffset = zOffset;
                    zOffset = h;
                }
                xOffset *= dist;
                zOffset *= dist;
                float size = (float)MathHelper.clampedLerp(6,2,dist/0.8f);
                int lifetime = (int)(random.nextDouble() * MathHelper.clampedLerp(20,10,dist/20.0));
                float speed = -1.0f/lifetime;
                ParticleUtil.spawnParticleGlow(getWorld(), pos.getX()+0.5f+xOffset, pos.getY()+1.0f, pos.getZ()+0.5f+zOffset, xOffset * speed, (random.nextFloat() - 0.5f) * 2f * 0.04f, zOffset * speed, 255, 64, 16, size, lifetime);
            }
        }
    }
}
