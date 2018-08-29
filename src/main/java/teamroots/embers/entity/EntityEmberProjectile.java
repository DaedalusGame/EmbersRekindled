package teamroots.embers.entity;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import teamroots.embers.api.projectile.IProjectileEffect;
import teamroots.embers.api.projectile.IProjectilePreset;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageEmberSizedBurstFX;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

//import elucent.albedo.lighting.ILightProvider;
//import elucent.albedo.lighting.Light;

//@Interface(iface = "elucent.albedo.lighting.ILightProvider", modid = "albedo")
public class EntityEmberProjectile extends Entity/* implements ILightProvider*/ {
    private static final Predicate<Entity> VALID_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate<Entity>() {
        public boolean apply(@Nullable Entity entity) {
            return entity.canBeCollidedWith();
        }
    });

    public static final DataParameter<Float> value = EntityDataManager.createKey(EntityEmberProjectile.class, DataSerializers.FLOAT);
    public static final DataParameter<Boolean> dead = EntityDataManager.createKey(EntityEmberProjectile.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityEmberProjectile.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> color = EntityDataManager.createKey(EntityEmberProjectile.class, DataSerializers.VARINT);
    //public UUID id = null;
    public Entity shootingEntity;
    public IProjectileEffect effect;
    private IProjectilePreset preset;
    double gravity;

    int homingTime;
    double homingRange;
    int homingIndex, homingModulo; //For spread homing
    Entity homingTarget;
    Predicate<Entity> homingPredicate;

    public EntityEmberProjectile(World worldIn) {
        super(worldIn);
        this.setInvisible(true);
        this.getDataManager().register(value, 0f);
        this.getDataManager().register(dead, false);
        this.getDataManager().register(lifetime, 160);
        this.getDataManager().register(color, new Color(255,64,16).getRGB());
    }

    public void initCustom(double x, double y, double z, double vx, double vy, double vz, double value, Entity shootingEntity) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.motionX = vx;
        this.motionY = vy;
        this.motionZ = vz;
        setSize((float) value / 10.0f, (float) value / 10.0f);
        getDataManager().set(EntityEmberProjectile.value, (float) value);
        getDataManager().setDirty(EntityEmberProjectile.value);
        setSize((float) value / 10.0f, (float) value / 10.0f);
        this.shootingEntity = shootingEntity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public void setColor(int red, int green, int blue, int alpha) {
        getDataManager().set(EntityEmberProjectile.color, new Color((red * alpha) / 255,(green * alpha) / 255,(blue * alpha) / 255).getRGB());
        getDataManager().setDirty(EntityEmberProjectile.color);
    }

    public void setHoming(int time, double range, int index, int modulo, Predicate<Entity> predicate) {
        homingTime = time;
        homingRange = range;
        homingIndex = index;
        homingModulo = modulo;
        homingPredicate = predicate;
    }

    public void setPreset(IProjectilePreset preset) {
        this.preset = preset;
    }

    public void setEffect(IProjectileEffect effect) {
        this.effect = effect;
    }

    public void setLifetime(int lifetime) {
        getDataManager().set(EntityEmberProjectile.lifetime,lifetime);
    }

    public Entity getShooter() {
        return shootingEntity;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        getDataManager().set(EntityEmberProjectile.value, compound.getFloat("value"));
        getDataManager().setDirty(EntityEmberProjectile.value);
        getDataManager().set(EntityEmberProjectile.color, compound.getInteger("color"));
        getDataManager().setDirty(EntityEmberProjectile.color);
        /*if (compound.hasKey("UUIDmost")){
			id = new UUID(compound.getLong("UUIDmost"),compound.getLong("UUIDleast"));
		}*/
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setFloat("value", getDataManager().get(value));
        compound.setInteger("color",getDataManager().get(color));
		/*if (id != null){
			compound.setLong("UUIDmost", id.getMostSignificantBits());
			compound.setLong("UUIDleast", id.getLeastSignificantBits());
		}*/
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        //if (!getEntityWorld().isRemote && getDataManager().get(lifetime) > 18 && getDataManager().get(dead)){
        //PacketHandler.INSTANCE.sendToAll(new MessageEmberSizedBurstFX(posX, posY, posZ,getDataManager().get(value)/1.75f));
        //}
        int lifetime = getDataManager().get(EntityEmberProjectile.lifetime);
        getDataManager().set(EntityEmberProjectile.lifetime, lifetime - 1);
        getDataManager().setDirty(EntityEmberProjectile.lifetime);
        World world = getEntityWorld();
        if (lifetime <= 0) {
            world.removeEntity(this);
            this.setDead();
        }
        if (!getDataManager().get(dead)) {
            getDataManager().set(value, getDataManager().get(value) - 0.025f);
            if (getDataManager().get(value) <= 0) {
                world.removeEntity(this);
            }

            Vec3d currPosVec = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d newPosVector = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            RayTraceResult raytraceresult = this.world.rayTraceBlocks(currPosVec, newPosVector, false, true, false);

            if (raytraceresult != null && raytraceresult.typeOfHit != RayTraceResult.Type.MISS)
                newPosVector = raytraceresult.hitVec;

            RayTraceResult hitEntity = Misc.findEntityOnPath(world,this,shootingEntity,getEntityBoundingBox(),currPosVec,newPosVector,VALID_TARGETS);

            if (hitEntity != null) {
                newPosVector = hitEntity.hitVec;
                raytraceresult = hitEntity;
            }

            posX = newPosVector.x;
            posY = newPosVector.y;
            posZ = newPosVector.z;

            motionY += gravity;

            if (!world.isRemote && raytraceresult != null && raytraceresult.typeOfHit != RayTraceResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                onHit(raytraceresult);
            }

            handleHoming(lifetime, world);

            if (world.isRemote) {
                Color particleColor = new Color(getDataManager().get(color),true);
                double deltaX = posX - prevPosX;
                double deltaY = posY - prevPosY;
                double deltaZ = posZ - prevPosZ;
                double dist = Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 10);
                for (double i = 0; i < dist; i++) {
                    double coeff = i / dist;
                    ParticleUtil.spawnParticleGlow(world, (float) (prevPosX + (posX - prevPosX) * coeff), (float) (prevPosY + (posY - prevPosY) * coeff), (float) (prevPosZ + (posZ - prevPosZ) * coeff), 0.0125f * (rand.nextFloat() - 0.5f), 0.0125f * (rand.nextFloat() - 0.5f), 0.0125f * (rand.nextFloat() - 0.5f), particleColor.getRed(), particleColor.getGreen(), particleColor.getBlue(), getDataManager().get(value) / 1.75f, 24);
                }
            }

            this.setPosition(this.posX, this.posY, this.posZ);
        } else {
            motionX = 0;
            motionY = 0;
            motionZ = 0;
        }
    }

    private void handleHoming(int lifetime, World world) {
        if (homingTime > 0) {
            if (!isTargetInvalid(homingTarget)) {
                double targetX = homingTarget.posX;
                double targetY = homingTarget.posY+homingTarget.height/2;
                double targetZ = homingTarget.posZ;
                Vec3d targetVector = new Vec3d(targetX-posX,targetY-posY,targetZ-posZ);
                double length = targetVector.lengthVector();
                targetVector = targetVector.scale(0.3/length);
                double weight  = 0;
                if (length <= homingRange){
                    weight = 0.9*((homingRange-length)/homingRange);
                }
                motionX = (0.9-weight)*motionX+(0.1+weight)*targetVector.x;
                motionY = (0.9-weight)*motionY+(0.1+weight)*targetVector.y;
                motionZ = (0.9-weight)*motionZ+(0.1+weight)*targetVector.z;
                homingTime--;
            }
            else if (lifetime % 5 == 0) {
                AxisAlignedBB homingAABB = new AxisAlignedBB(posX - homingRange, posY - homingRange, posZ - homingRange, posX + homingRange, posY + homingRange, posZ + homingRange);
                List<Entity> entities = world.getEntitiesInAABBexcluding(this, homingAABB, homingPredicate);
                Entity badTarget = null;
                for (Entity entity : entities) {
                    long leastSignificantBits = entity.getUniqueID().getLeastSignificantBits() & 0xFFFF;
                    if (leastSignificantBits % homingModulo == homingIndex % homingModulo) {
                        homingTarget = entity;
                    }
                    badTarget = entity;
                }
                if(homingTarget == null)
                    homingTarget = badTarget;
            }
        }
    }

    private boolean isTargetInvalid(Entity entity) {
        return entity == null || entity.isDead;
    }

    private void onHit(RayTraceResult raytraceresult) {
        PacketHandler.INSTANCE.sendToAll(new MessageEmberSizedBurstFX(posX, posY, posZ, getDataManager().get(value) / 1.75f, getDataManager().get(color)));
        getDataManager().set(lifetime, 20);
        getDataManager().setDirty(lifetime);
        this.getDataManager().set(dead, true);
        getDataManager().setDirty(dead);

        double aoeRadius = getDataManager().get(value) * 0.125; //TODO

        if(effect != null)
            effect.onHit(world,raytraceresult,preset);
       /* if (raytraceresult.entityHit != null) {
            Entity target = raytraceresult.entityHit;
            DamageSource source = new EntityDamageSourceIndirect("ember", this, shootingEntity).setMagicDamage();
            if (target.attackEntityFrom(source, getDataManager().get(value))) {
                if (shootingEntity instanceof EntityPlayer) {
                    target.setFire(1);
                    if (target instanceof EntityLivingBase) {
                        EntityLivingBase livingTarget = (EntityLivingBase) target;
                        livingTarget.setLastAttackedEntity(shootingEntity);
                        livingTarget.setRevengeTarget((EntityLivingBase) shootingEntity);
                        livingTarget.knockBack(this, 0.5f, -motionX, -motionZ);
                    }
                }
            }
        } else {

        }*/
    }



	/*@Method(modid = "albedo")
	@Override
	public Light provideLight() {
		if (getDataManager().get(dead)){
			return new Light((float)posX,(float)posY,(float)posZ,1.0f,0.5f,0.0625f,1.0f,(getDataManager().get(value)/2.625f) * ((float)getDataManager().get(lifetime)/20f));
		}
		return new Light((float)posX,(float)posY,(float)posZ,1.0f,0.5f,0.0625f,1.0f,(getDataManager().get(value)/2.625f));
	}*/
}
