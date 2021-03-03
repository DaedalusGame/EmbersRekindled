package teamroots.embers.entity;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import teamroots.embers.Embers;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageEmberBurstFX;
import teamroots.embers.network.message.MessageFireBlastFX;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.util.Misc;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Random;

public class EntityMagmaProjectile extends Entity {
    private final Predicate<Entity> VALID_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate<Entity>() {
        public boolean apply(Entity entity) {
            return entity.canBeCollidedWith() && entity != owner;
        }
    });

    enum Type {
        GUN, //Normal bullet
        MORTAR, //Falling bomb dropped from overhead
        PILLAR, //Ground Eruption
        HOMING, //Like Aldrich bullets in DS3
        SPLASH, //Splash
        TURRET_FLOAT, //Floating turret
        TURRET, //Sucks Ember from cells and shoots
        TURRET_HEAL, //Sucks Ember from cells and heals
    }

    public static final DataParameter<Integer> typeOrdinal = EntityDataManager.createKey(EntityMagmaProjectile.class, DataSerializers.VARINT);
    public static final DataParameter<Float> explosionTimer = EntityDataManager.createKey(EntityMagmaProjectile.class, DataSerializers.FLOAT);
    public static final DataParameter<Float> pillarHeight = EntityDataManager.createKey(EntityMagmaProjectile.class, DataSerializers.FLOAT);
    public static final DataParameter<Integer> tick = EntityDataManager.createKey(EntityMagmaProjectile.class, DataSerializers.VARINT);

    EntityMagmaWorm owner;

    Supplier<Vec3d> homingTarget;
    double homingRatio = 0.1;
    double homingVelocity = 0.3;

    Predicate<Entity> entityTarget = VALID_TARGETS;
    boolean pierceBlocks = false;

    double gravity;
    double maxGravity;

    public EntityMagmaProjectile(World worldIn) {
        super(worldIn);
    }

    public EntityMagmaProjectile(World worldIn, EntityMagmaWorm owner) {
        super(worldIn);
        this.owner = owner;
    }

    public void makeMortar(Vec3d velocity) {
        setType(Type.MORTAR);
        motionX = velocity.x;
        motionY = velocity.y;
        motionZ = velocity.z;
        gravity = 0.007;
        maxGravity = rand.nextDouble()*0.2 + 0.1;
        setExplosionTimer(1.0f);
        entityTarget = Predicates.alwaysFalse();
    }

    public void makePillar(float height, int delay) {
        setType(Type.PILLAR);
        setExplosionTimer(1.0f);
        entityTarget = Predicates.alwaysFalse();
        pierceBlocks = true;
        setPillarHeight(height);
        setTick(-delay);
    }

    public void makeHoming(Vec3d velocity, Entity target) {
        setType(Type.HOMING);
        motionX = velocity.x;
        motionY = velocity.y;
        motionZ = velocity.z;
        homingRatio = 0;
        homingVelocity = 0.6;
        homingTarget = () -> new Vec3d(target.posX, target.posY + target.height / 2, target.posZ);
    }

    public void makeSplash(Vec3d velocity) {
        setType(Type.SPLASH);
        motionX = velocity.x;
        motionY = velocity.y;
        motionZ = velocity.z;
        gravity = 0.007;
        maxGravity = 0.5;
        pierceBlocks = true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        int tick = getTick();

        float explosionTimer = getExplosionTimer();
        switch (getType()) {
            case GUN:
                break;
            case MORTAR:
                if(!world.isRemote) {
                    double height = world.getHeight(getPosition()).getY();
                    if (posY < height + 1.5)
                        motionY = Math.max(motionY,0) + 0.04;
                    if (tick > 100) {
                        motionX *= 0.9;
                        motionZ *= 0.9;
                    }
                    if ((posY < height + 3 && motionY > 0) || explosionTimer < 1) {
                        setExplosionTimer(explosionTimer - 1f / 20);
                    }
                    if (explosionTimer <= 0) {
                        setDead();
                        PacketHandler.INSTANCE.sendToAllTracking(new MessageFireBlastFX(posX,posY-1,posZ,new Color(255,64,16),5, 15),this);
                    }
                } else {
                    if(explosionTimer > 0 && explosionTimer < 1) {
                        showTimer(explosionTimer, 12, 30, new Color(255, 64, 16), 3.0, 1);
                    }
                }

                break;
            case PILLAR:
                float chargeTimer = 1 - tick / 40f;
                float pillarTimer = (tick - 40f) / 40f;
                if(!world.isRemote) {
                    if(pillarTimer > 1) {
                        setDead();
                    }
                } else {
                    if(chargeTimer > 0 && chargeTimer < 1) {
                        showTimer(chargeTimer, 50, 10, new Color(255, 64, 16), 3.0, 0);
                    } else if(pillarTimer > 0 && pillarTimer < 1) {
                        double height = Math.sin(MathHelper.clamp(pillarTimer,0,1)*Math.PI) * getPillarHeight();
                        Color particleColor = new Color(255,64,16);
                        for(int i=0; i<height; i++) {
                            for(int e=0; e<5; e++) {
                                double width = Math.cos(MathHelper.clamp(pillarTimer, 0, 1) * Math.PI * 0.5) * 6;
                                int lifetime = rand.nextInt(20) + 20;
                                double angle = rand.nextDouble() * Math.PI * 2;
                                double dx = Math.sin(angle) * width / lifetime;
                                double dy = (0.5 + rand.nextDouble() * 1) / lifetime;
                                double dz = Math.cos(angle) * width / lifetime;
                                ParticleUtil.spawnParticleGlow(world, (float) posX, (float) (posY + rand.nextDouble() * height), (float) posZ, (float) dx, (float) dy, (float) dz, particleColor.getRed(), particleColor.getGreen(), particleColor.getBlue(), (float) (width * 3.0f), lifetime);
                            }
                        }
                    }
                }
                break;
            case HOMING:
                if (tick > 250)
                    setDead();
                else if (tick > 150 || (homingTarget != null && getPositionVector().squareDistanceTo(homingTarget.get()) < 5*5))
                    homingTarget = null;
                else if(tick > 20)
                    homingRatio += 0.001;
                break;
            case SPLASH:
                motionX *= 0.98;
                motionZ *= 0.98;
                if(tick > 100)
                    setDead();
                break;
            case TURRET:
                break;
            case TURRET_FLOAT:
                break;
            case TURRET_HEAL:
                break;
        }

        updateMove();

        setTick(tick+1);
    }

    public void showTimer(float timer, int segments, int lifetime, Color color, double size, float variance) {
        Random random = new Random(getEntityId());
        int tick = getTick();
        float segmentSize = 360f / segments;
        float angleOffset = tick * 5;
        float pitchVariance = 20* variance;
        for(int i = 0; i < segments; i++) {

            float yaw =  (float)i / segments * 360 + angleOffset;
            Vec3d offset = Vec3d.fromPitchYaw(random.nextFloat()*pitchVariance-pitchVariance/2, yaw + random.nextFloat()*segmentSize* variance *0.5f).scale(size *timer);
            ParticleUtil.spawnParticleGlow(world, (float) (posX + offset.x), (float) (posY + offset.y), (float) (posZ + offset.z), 0, 0, 0, color.getRed(), color.getGreen(), color.getBlue(), 3f, lifetime);
        }
    }

    public void updateMove() {
        Vec3d currPosVec = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d newPosVector = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        Vec3d hitVec = newPosVector;
        RayTraceResult raytraceresult = this.world.rayTraceBlocks(currPosVec, newPosVector, false, true, false);

        if (raytraceresult != null && raytraceresult.typeOfHit != RayTraceResult.Type.MISS && !canPierce(raytraceresult))
            hitVec = raytraceresult.hitVec;

        RayTraceResult hitEntity = Misc.findEntityOnPath(world, this, owner, getEntityBoundingBox(), currPosVec, hitVec, entityTarget);

        if (hitEntity != null) {
            if (!canPierce(hitEntity))
                hitVec = hitEntity.hitVec;
            raytraceresult = hitEntity;
        }

        if (-motionY < maxGravity)
            motionY -= gravity;//MathHelper.clamp(gravity, 0, maxGravity + motionY);

        if (!world.isRemote && raytraceresult != null && raytraceresult.typeOfHit != RayTraceResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
            onHit(raytraceresult);
        }

        newPosVector = hitVec;

        posX = newPosVector.x;
        posY = newPosVector.y;
        posZ = newPosVector.z;

        handleHoming();

        handleTrail();

        this.setPosition(this.posX, this.posY, this.posZ);
    }

    public void handleTrail() {
        if (world.isRemote) {
            Color particleColor = new Color(255, 64, 16);
            double deltaX = posX - prevPosX;
            double deltaY = posY - prevPosY;
            double deltaZ = posZ - prevPosZ;
            double dist = Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 10);
            for (double i = 0; i < dist; i++) {
                double coeff = i / dist;
                float px = (float) (prevPosX + (posX - prevPosX) * coeff);
                float py = (float) (prevPosY + (posY - prevPosY) * coeff);
                float pz = (float) (prevPosZ + (posZ - prevPosZ) * coeff);
                switch (getType()) {

                    case GUN:
                        break;
                    case MORTAR:
                        float explosionTimer = getExplosionTimer();
                        double height = world.getHeight(getPosition()).getY();
                        if (posY < height + 4 || explosionTimer < 1)
                            ParticleUtil.spawnParticleGlow(world, px, py, pz, 0.025f * (rand.nextFloat() - 0.5f), 0.025f * (rand.nextFloat()), 0.025f * (rand.nextFloat() - 0.5f), particleColor.getRed(), particleColor.getGreen(), particleColor.getBlue(), 6f, 20);
                        else
                            ParticleUtil.spawnParticleGlow(world, px, py, pz, 0.0125f * (rand.nextFloat() - 0.5f), 0.1f * rand.nextFloat(), 0.0125f * (rand.nextFloat() - 0.5f), particleColor.getRed(), particleColor.getGreen(), particleColor.getBlue(), 2f, 12);
                        break;
                    case PILLAR:
                        break;
                    case HOMING:
                        ParticleUtil.spawnParticleGlow(world, px, py, pz, 0.0125f * (rand.nextFloat() - 0.5f), 0.0125f * (rand.nextFloat() - 0.5f), 0.0125f * (rand.nextFloat() - 0.5f), particleColor.getRed(), particleColor.getGreen(), particleColor.getBlue(), 4f, 12);
                        break;
                    case SPLASH:
                        ParticleUtil.spawnParticleGlow(world, px, py, pz, 0.025f * (rand.nextFloat() - 0.5f), -0.025f * (rand.nextFloat()), 0.025f * (rand.nextFloat() - 0.5f), particleColor.getRed(), particleColor.getGreen(), particleColor.getBlue(), 6f, 40);
                        break;
                    case TURRET:
                        break;
                    case TURRET_HEAL:
                        break;
                }
            }
        }
    }

    private void handleHoming() {
        if (homingTarget != null) {
            Vec3d homingPosition = homingTarget.get();
            double targetX = homingPosition.x;
            double targetY = homingPosition.y;
            double targetZ = homingPosition.z;
            Vec3d targetVector = new Vec3d(targetX - posX, targetY - posY, targetZ - posZ);
            double length = targetVector.lengthVector();
            targetVector = targetVector.scale(homingVelocity / length);
            motionX = (1 - homingRatio) * motionX + (homingRatio) * targetVector.x;
            motionY = (1 - homingRatio) * motionY + (homingRatio) * targetVector.y;
            motionZ = (1 - homingRatio) * motionZ + (homingRatio) * targetVector.z;
        }
    }

    private boolean canPierce(@Nonnull RayTraceResult rayTraceResult) {
        if(rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK)
            return pierceBlocks;
        return true;
    }

    private void onHit(RayTraceResult rayTraceResult) {
        if(getType() == Type.SPLASH)
            return;
        Vec3d hitPos = rayTraceResult.hitVec;
        PacketHandler.INSTANCE.sendToAllTracking(new MessageEmberBurstFX(hitPos.x, hitPos.y, hitPos.z),this);
        setDead();

    }

    public float getExplosionTimer() {
        return getDataManager().get(explosionTimer);
    }

    public void setExplosionTimer(float time) {
        this.getDataManager().set(explosionTimer, time);
        this.getDataManager().setDirty(explosionTimer);
    }

    public float getPillarHeight() {
        return getDataManager().get(pillarHeight);
    }

    public void setPillarHeight(float height) {
        this.getDataManager().set(pillarHeight, height);
        this.getDataManager().setDirty(pillarHeight);
    }

    public int getTick() {
        return getDataManager().get(tick);
    }

    public void setTick(int n) {
        this.getDataManager().set(tick, n);
        this.getDataManager().setDirty(tick);
    }


    public Type getType() {
        Type[] values = Type.values();
        Integer ordinal = getDataManager().get(typeOrdinal);
        return values[MathHelper.clamp(ordinal, 0, values.length - 1)];
    }

    public void setType(Type type) {
        this.getDataManager().set(typeOrdinal, type.ordinal());
        this.getDataManager().setDirty(typeOrdinal);
    }

    @Override
    protected void entityInit() {
        this.getDataManager().register(typeOrdinal, Type.GUN.ordinal());
        this.getDataManager().register(explosionTimer, 0f);
        this.getDataManager().register(pillarHeight, 0f);
        this.getDataManager().register(tick, 0);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }
}
