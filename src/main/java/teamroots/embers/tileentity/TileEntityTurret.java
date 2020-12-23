package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.util.vector.Quaternion;
import teamroots.embers.api.event.EmberProjectileEvent;
import teamroots.embers.api.projectile.EffectArea;
import teamroots.embers.api.projectile.EffectDamage;
import teamroots.embers.api.projectile.IProjectilePreset;
import teamroots.embers.api.projectile.ProjectileFireball;
import teamroots.embers.damage.DamageEmber;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.util.Misc;
import teamroots.embers.util.TurretHelper;

import javax.annotation.Nullable;

public class TileEntityTurret extends TileEntity implements ITickable, ITileEntityBase {
    TurretHelper helper = new TurretHelper(new Vec3d(0,1,0), new Vec3d(0,0,1));

    int ticksExisted;

    @Override
    public void update() {
        ticksExisted++;

        float emitX = pos.getX() + 0.5f;
        float emitY = pos.getY() + 0.5f;
        float emitZ = pos.getZ() + 0.5f;

        if(ticksExisted % 1 == 0) {
            AxisAlignedBB aabbTarget = new AxisAlignedBB(pos).grow(10);
            for (Entity entity : world.getEntitiesWithinAABB(EntityLivingBase.class, aabbTarget)) {
                Vec3d delta = new Vec3d(entity.posX - emitX, entity.posY + entity.getEyeHeight() / 2 - emitY, entity.posZ - emitZ);
                helper.rotateTowards(delta);
                break;
            }
        }
        if(ticksExisted % 10 == 0 && !world.isRemote) {
            Vec3d forward = getUp();

            double eX = emitX + forward.x * 0.7;
            double eY = emitY + forward.y * 0.7;
            double eZ = emitZ + forward.z * 0.7;

            EffectDamage effect = new EffectDamage(1, DamageEmber.EMBER_DAMAGE_SOURCE_FACTORY, 1, 1.0);
            ProjectileFireball fireball = new ProjectileFireball(null, new Vec3d(eX, eY, eZ), new Vec3d(forward.x * 0.85, forward.y * 0.85, forward.z * 0.85), 2, 40, effect);
            EmberProjectileEvent event = new EmberProjectileEvent(null, ItemStack.EMPTY, 0, fireball);
            MinecraftForge.EVENT_BUS.post(event);
            if (!event.isCanceled()) {
                for (IProjectilePreset projectile : event.getProjectiles()) {
                    projectile.shoot(world);
                }
            }
        }

        if(world.isRemote) {
            Vec3d forward = getForward();
            Vec3d side = getSide();
            Vec3d up = getUp();

            //ParticleUtil.spawnParticleGlow(world, emitX + (float)forward.x, emitY + (float)forward.y, emitZ + (float)forward.z, (float)forward.x * 0.2f, (float)forward.y* 0.2f, (float)forward.z* 0.2f, 255, 128, 128, 4, 20);
            //ParticleUtil.spawnParticleGlow(world, emitX + (float)side.x, emitY + (float)side.y, emitZ + (float)side.z, (float)side.x * 0.2f, (float)side.y* 0.2f, (float)side.z* 0.2f, 255, 128, 128, 4, 20);
            //ParticleUtil.spawnParticleGlow(world, emitX + (float)up.x, emitY + (float)up.y, emitZ + (float)up.z, (float)up.x * 0.2f, (float)up.y* 0.2f, (float)up.z* 0.2f, 255, 128, 128, 4, 20);
        }

        float rotationSpeed = 0.5f;
        helper.update(rotationSpeed);
    }

    public void shoot() {

    }

    public Quaternion getCurrentAngle() {
        return helper.getCurrentAngle();
    }

    public Quaternion getCurrentAngle(float partialTicks) {
        return helper.getCurrentAngle(partialTicks);
    }

    public Vec3d getForward() {
        Quaternion angle = getCurrentAngle();
        return helper.getForward(angle).normalize();
    }

    public Vec3d getUp() {
        Quaternion angle = getCurrentAngle();
        return helper.getUp(angle).normalize();
    }

    public Vec3d getSide() {
        Quaternion angle = getCurrentAngle();
        return helper.getSide(angle).normalize();
    }

    public Vec3d rotateVector(Vec3d vec, Quaternion quat) {
        Vec3d quatVec = new Vec3d(quat.x,quat.y,quat.z);
        float quatScalar = quat.w;

        return quatVec.scale(2 * quatVec.dotProduct(vec)).add(vec.scale(quatScalar * quatScalar - quatVec.dotProduct(quatVec))).add(quatVec.crossProduct(vec).scale(2.0 * quatScalar));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        helper.writeToNBT(compound);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        helper.readFromNBT(compound);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
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

    }
}
