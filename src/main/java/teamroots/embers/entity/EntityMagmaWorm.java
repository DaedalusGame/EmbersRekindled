package teamroots.embers.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import teamroots.embers.Embers;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.util.ExtraSerializers;
import teamroots.embers.util.Misc;
import teamroots.embers.util.Spline;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.DoubleFunction;
import java.util.function.Function;

public class EntityMagmaWorm extends EntityFlying implements IEntityMultiPart {// implements IRangedAttackMob {

    public static final int RANGE_ATTACK = 72;
    public ArrayList<Vec3d> pastPositions = new ArrayList<>();
    public ArrayList<Vec3d> pastPositionsLast = new ArrayList<>();
    public MultiPartEntityPart[] wormPartArray;
    public static final DataParameter<Float> targetDirectionX = EntityDataManager.createKey(EntityMagmaWorm.class, DataSerializers.FLOAT);
    public static final DataParameter<Float> targetDirectionY = EntityDataManager.createKey(EntityMagmaWorm.class, DataSerializers.FLOAT);
    public static final DataParameter<Boolean> pacified = EntityDataManager.createKey(EntityMagmaWorm.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> tracking = EntityDataManager.createKey(EntityMagmaWorm.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Integer> fadeTimer = EntityDataManager.createKey(EntityMagmaWorm.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> segments = EntityDataManager.createKey(EntityMagmaWorm.class, DataSerializers.VARINT);
    public static final DataParameter<float[]> segmentFade = EntityDataManager.createKey(EntityMagmaWorm.class, ExtraSerializers.FLOAT_ARRAY);
    public static final DataParameter<Boolean> digging = EntityDataManager.createKey(EntityMagmaWorm.class, DataSerializers.BOOLEAN);
    Random random = new Random();
    Spline currentSpline;
    double distMoved;
    double splineInterval;
    double pastPositionDistance;

    MagmaWormPhase currentPhase;

    public Vec3d moveVec = new Vec3d(0, 0, 0);
    //public Vec3d prevMoveVec = new Vec3d(0, 0, 0);
    public static SoundEvent ambientSound;
    public static SoundEvent hurtSound;
    public static SoundEvent departureSound;
    private final BossInfoServer bossInfo = (BossInfoServer) (new BossInfoServer(this.getDisplayName(), BossInfo.Color.GREEN, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);

    public void setCurrentSpline(DoubleFunction<Vec3d> spline, int segments, double splineInterval) {
        this.moveVec = new Vec3d(motionX,motionY,motionZ);
        this.currentSpline = new Spline(spline);
        this.currentSpline.cachePoints(segments,1.0,1.0);
        this.splineInterval = currentSpline.getTotalArcLength() * splineInterval;
        this.distMoved = 0;
    }

    public void setCurrentSpline(Spline spline, double splineInterval) {
        this.moveVec = new Vec3d(motionX,motionY,motionZ);
        this.currentSpline = spline;
        this.splineInterval = splineInterval;
        this.distMoved = 0;
    }

    public void setSegmentFade(int index, float fade) {
        float[] value = this.getDataManager().get(segmentFade);
        if(value[index] == fade)
            return;
        value[index] = fade;
        this.getDataManager().set(segmentFade,value);
        this.getDataManager().setDirty(segmentFade);
    }

    public float getSegmentFade(int index) {
        float[] value = this.getDataManager().get(segmentFade);
        return value[index];
    }

    public void setSegments(int n) {
        this.getDataManager().set(segments,n);
        this.getDataManager().setDirty(segments);
    }

    public int getSegments() {
        return this.getDataManager().get(segments);
    }

    public void setDigging(boolean flag) {
        this.getDataManager().set(digging,flag);
        this.getDataManager().setDirty(digging);
    }

    public boolean isDigging() {
        return this.getDataManager().get(digging);
    }


    @Nullable
    @Override
    public Entity[] getParts() {
        return wormPartArray;
    }

    public EntityMagmaWorm(World worldIn) {
        super(worldIn);
        setSize(2.0f, 2.0f);
        this.noClip = true;
        this.isAirBorne = true;
        this.experienceValue = 20;
        int segments = getSegments();
        for (int i = 0; i < segments * 2; i++) {
            pastPositions.add(new Vec3d(posX, posY, posZ));
            pastPositionsLast.add(new Vec3d(posX, posY, posZ));
        }
        wormPartArray = new MultiPartEntityPart[segments];
        for(int i = 0; i < segments; i++)
            wormPartArray[i] = new MultiPartEntityPart(this,"tail",2.0f, 2.0f);
        this.rotationYaw = rand.nextInt(240) + 60;
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(pacified, false);
        this.getDataManager().register(tracking, false);
        this.getDataManager().register(targetDirectionX, 0f);
        this.getDataManager().register(targetDirectionY, 0f);
        this.getDataManager().register(fadeTimer, 0);
        this.getDataManager().register(segments, 15);
        this.getDataManager().register(segmentFade, new float[15]);
        this.getDataManager().register(digging, false);
    }

    @Override
    public void collideWithEntity(Entity entity) {
        if (this.getAttackTarget() != null && this.getHealth() > 0 && !getDataManager().get(pacified)) {
            if (entity.getUniqueID().compareTo(this.getAttackTarget().getUniqueID()) == 0 && entity instanceof EntityLivingBase) {
                EntityLivingBase living = ((EntityLivingBase) entity);
                if (Misc.isCreativePlayer(living)) {
                    return;
                }
                //TODO: NO CONFIG? HUH HMM WAT
                living.attackEntityFrom(DamageSource.GENERIC, 4.0f);
                float magnitude = (float) Math.sqrt(motionX * motionX + motionZ * motionZ);
                living.knockBack(this, 3.0f * magnitude + 0.1f, -motionX / magnitude + 0.1, -motionZ / magnitude + 0.1);
                living.attackEntityAsMob(this);
                living.setRevengeTarget(this);
            }
        }
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
    }

    public Vec3d getSegmentPosition(int index, float partialTicks) {
        Vec3d pos1 = pastPositionsLast.get(index);
        Vec3d pos2 = pastPositions.get(index);
        return new Vec3d(MathHelper.clampedLerp(pos1.x, pos2.x, partialTicks),
                MathHelper.clampedLerp(pos1.y, pos2.y, partialTicks),
                MathHelper.clampedLerp(pos1.z, pos2.z, partialTicks));
    }

    public Vec3d getHeadPosition(float partialTicks) {
        return new Vec3d(MathHelper.clampedLerp(prevPosX, posX, partialTicks),
                MathHelper.clampedLerp(prevPosY, posY, partialTicks),
                MathHelper.clampedLerp(prevPosZ, posZ, partialTicks));
    }

    public void setPhase(MagmaWormPhase phase, boolean interrupt) {
        if(currentPhase != null)
        if(interrupt)
            currentPhase.onInterrupt(this);
        else
            currentPhase.onEnd(this);
        currentPhase = phase;
        currentPhase.onStart(this);
    }

    public List<Entity> getAttackTargets(AxisAlignedBB aabb) {
        return world.getEntitiesWithinAABB(EntityPlayer.class,aabb);
        //return world.getEntitiesWithinAABB(EntityLivingBase.class,aabb,x -> x != this);
    }

    @Override
    public void onUpdate() {
        if(!world.isRemote) {
            if (currentPhase == null) {
                setPhase(MagmaWormPhase.START.get(), false);
            } else if (distMoved >= currentSpline.getTotalArcLength()) {
                MagmaWormPhase phase = currentPhase.pickFollowup(random);
                setPhase(phase, false);
            }
        }
        super.onUpdate();
        for (int i = 0; i < pastPositions.size(); i++) {
            pastPositionsLast.set(i, pastPositions.get(i));
        }
        if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
            return;
        }
        float velocityScale = 0.7f;
        float addedMotionY = 0.0f;
        if (getDataManager().get(pacified) && getDataManager().get(fadeTimer) > 0) {
            getDataManager().set(fadeTimer, getDataManager().get(fadeTimer) - 1);
            getDataManager().setDirty(fadeTimer);
        }
        if (getDataManager().get(fadeTimer) > 0) {
            for (int i = 0; i < 5; i++) {
                Vec3d location = pastPositions.get(rand.nextInt(20)).add((new Vec3d(rand.nextFloat() - 0.5, rand.nextFloat() - 0.5, rand.nextFloat() - 0.5)).scale(3.0f));
                ParticleUtil.spawnParticleGlowThroughBlocks(getEntityWorld(), (float) location.x, (float) location.y + 1.35f, (float) location.z, 0, 0, 0, 1.0f, 0.25f, 0.06f, 1.0f, 5.0f, 20);
            }
        }
        if (getDataManager().get(fadeTimer) == 0 && getDataManager().get(pacified)) {
            for (int i = 0; i < getSegments(); i++) {
                for (int j = 0; j < 5; j++) {
                    Vec3d location = pastPositions.get(i).add((new Vec3d(rand.nextFloat() - 0.5, rand.nextFloat() - 0.5, rand.nextFloat() - 0.5)).scale(3.0f));
                    ParticleUtil.spawnParticleGlowThroughBlocks(getEntityWorld(), (float) location.x, (float) location.y + 1.35f, (float) location.z, 0, 0, 0, 1.0f, 0.25f, 0.06f, 1.0f, 5.0f, 20);
                }
                for (int j = 0; j < 2; j++) {
                    Vec3d location = pastPositions.get(i).add((new Vec3d(rand.nextFloat() - 0.5, rand.nextFloat() - 0.5, rand.nextFloat() - 0.5)).scale(1.5f));
                    ParticleUtil.spawnParticleGlowThroughBlocks(getEntityWorld(), (float) location.x, (float) location.y + 1.35f, (float) location.z, 0, 0, 0, 1.0f, 0.25f, 0.06f, 1.0f, 5.0f, 20);
                }
            }
            //getEntityWorld().playSound(posX, posY, posZ, departureSound, SoundCategory.NEUTRAL, random.nextFloat() * 0.1f + 0.95f, random.nextFloat() * 0.1f + 0.95f, false);
            setDead();
        }
        if (world.isRemote) {
            for (int i = 0; i < Math.max(getSegments() - 2, 0); i++) {
                for (int j = 0; j < 5; j++) {
                    Vec3d velocity = Vec3d.fromPitchYaw(rand.nextFloat() * 360, rand.nextFloat() * 360).scale(0.2f);
                    Vec3d location = pastPositions.get(i).add((new Vec3d(rand.nextFloat() - 0.5, rand.nextFloat() - 0.5, rand.nextFloat() - 0.5)).scale(3.0f).scale(0.0f));
                    ParticleUtil.spawnParticleGlow(getEntityWorld(), (float) location.x, (float) location.y + 1.35f, (float) location.z, (float) velocity.x, (float) velocity.y, (float) velocity.z, 1.0f, 0.25f, 0.06f, 1.0f, 3.0f, 20);
                }
                for (int j = 0; j < 2; j++) {
                    Vec3d velocity = Vec3d.fromPitchYaw(rand.nextFloat() * 360, rand.nextFloat() * 360).scale(0.2f);
                    Vec3d location = pastPositions.get(i).add((new Vec3d(rand.nextFloat() - 0.5, rand.nextFloat() - 0.5, rand.nextFloat() - 0.5)).scale(1.5f).scale(0.0f));
                    ParticleUtil.spawnParticleGlow(getEntityWorld(), (float) location.x, (float) location.y + 1.35f, (float) location.z, 0, 0, 0, 1.0f, 0.25f, 0.06f, 1.0f, 5.0f, 20);
                }
            }
        }

        if(currentPhase != null && !world.isRemote)
            currentPhase.onUpdate(this);

        if (false && this.ticksExisted % 400 > 200 && this.ticksExisted % 20 == 0 && !world.isRemote) {
            BlockPos ground = world.getHeight(getPosition());
            EntityMagmaProjectile projectile = new EntityMagmaProjectile(world, this);
            projectile.setPosition(ground.getX() + 0.5, ground.getY() + 0.5, ground.getZ() + 0.5);
            //projectile.makePillar();
            world.spawnEntity(projectile);
        }

        /*if (this.ticksExisted % 200 > 150 && !world.isRemote) {
            List<Entity> targets = getAttackTargets(new AxisAlignedBB(posX - RANGE_ATTACK, posY - RANGE_ATTACK, posZ - RANGE_ATTACK, posX + RANGE_ATTACK, posY + RANGE_ATTACK, posZ + RANGE_ATTACK));
            if (!targets.isEmpty())
                for (int i = 0; i < 1; i++) {
                    int index = random.nextInt(Math.max(getSegments() - 2, 1));
                    Entity target = targets.get(random.nextInt(targets.size()));
                    Vec3d location = pastPositions.get(index);
                    //Vec3d velocity = Vec3d.fromPitchYaw(rand.nextFloat() * 360, rand.nextFloat() * 360).scale(0.3f);
                    Vec3d velocity = Vec3d.fromPitchYaw(-90 + rand.nextFloat() * 90 - 45f, rand.nextFloat() * 360).scale(0.3f);
                    EntityMagmaProjectile projectile = new EntityMagmaProjectile(world, this);
                    projectile.setPosition(location.x, location.y, location.z);
                    projectile.makeHoming(velocity,target);
                    //projectile.makeMortar(velocity);
                    world.spawnEntity(projectile);
                }
        }*/

        if (this.ticksExisted % 20 == 20) {
            List<EntityPlayer> playersValid = Misc.getNonCreativePlayers(world, new AxisAlignedBB(posX - RANGE_ATTACK, posY - RANGE_ATTACK, posZ - RANGE_ATTACK, posX + RANGE_ATTACK, posY + RANGE_ATTACK, posZ + RANGE_ATTACK));
            boolean foundPrevious = false;
            if (this.getAttackTarget() != null) {
                for (int i = 0; i < playersValid.size(); i++) {
                    if (playersValid.get(i).getUniqueID().compareTo(getAttackTarget().getUniqueID()) == 0) {
                        foundPrevious = true;
                    }
                }
            }
            if (!foundPrevious && playersValid.size() > 0) {
                this.setAttackTarget(playersValid.get(rand.nextInt(playersValid.size())));
            }
            /*else if (!foundPrevious && this.ticksExisted > 100) {
                for (int i = 0; i < 20; i++) {
                    for (int j = 0; j < 5; j++) {
                        Vec3d location = pastPositions.get(i).add((new Vec3d(rand.nextFloat() - 0.5, rand.nextFloat() - 0.5, rand.nextFloat() - 0.5)).scale(3.0f));
                        ParticleUtil.spawnParticleGlowThroughBlocks(getEntityWorld(),(float)location.x,(float)location.y+ 1.35f,(float)location.z,0,0,0, 1.0f, 0.25f, 0.06f, 1.0f, 3.0f, 20);
                    }
                    for (int j = 0; j < 2; j++) {
                        Vec3d location = pastPositions.get(i).add((new Vec3d(rand.nextFloat() - 0.5, rand.nextFloat() - 0.5, rand.nextFloat() - 0.5)).scale(1.5f));
                        ParticleUtil.spawnParticleGlowThroughBlocks(getEntityWorld(),(float)location.x,(float)location.y+ 1.35f,(float)location.z,0,0,0, 1.0f, 0.25f, 0.06f, 1.0f, 5.0f, 20);
                    }
                }
                //getEntityWorld().playSound(posX, posY, posZ, departureSound, SoundCategory.NEUTRAL, random.nextFloat() * 0.1f + 0.95f, random.nextFloat() * 0.1f + 0.95f, false);
                this.setDead();
            }*/
            if (random.nextInt(6) == 0) {
                //getEntityWorld().playSound(posX, posY, posZ, ambientSound, SoundCategory.NEUTRAL, random.nextFloat() * 0.1f + 0.95f, random.nextFloat() * 0.1f + 0.95f, false);
            }
        }
        if (getAttackTarget() != null) {
            float distanceToTarget = (float) Math.sqrt(Math.pow(posX - getAttackTarget().posX, 2) + Math.pow(posZ - getAttackTarget().posZ, 2));
            if (distanceToTarget > 30 && !getDataManager().get(tracking)) {
                getDataManager().set(tracking, true);
                getDataManager().setDirty(tracking);
            }
            if (!getDataManager().get(pacified)) {
                velocityScale = 1.0f + Math.max(-0.875f, (25.0f - distanceToTarget) / 20.0f);
            } else {
                velocityScale = (Math.max(0, 20.0f - Math.min(20.0f, 200.0f - (float) getDataManager().get(fadeTimer)))) / 200.0f;
            }
        }
        if (getAttackTarget() != null && getDataManager().get(tracking) && !this.getEntityWorld().isRemote) {
            this.getDataManager().set(targetDirectionX, (float) Math.toRadians(Misc.yawDegreesBetweenPointsSafe(posX, posY, posZ, getAttackTarget().posX, getAttackTarget().posY + getAttackTarget().getEyeHeight() / 2.0, getAttackTarget().posZ, getDataManager().get(targetDirectionX))));
            this.getDataManager().set(targetDirectionY, (float) Math.toRadians(Misc.pitchDegreesBetweenPoints(posX, posY, posZ, getAttackTarget().posX, getAttackTarget().posY + getAttackTarget().getEyeHeight() / 2.0, getAttackTarget().posZ)));
            this.getDataManager().setDirty(targetDirectionX);
            this.getDataManager().setDirty(targetDirectionY);
        }
        if (getAttackTarget() == null && this.ticksExisted % 25 == 0 && !this.getEntityWorld().isRemote) {
            this.getDataManager().set(targetDirectionX, (float) Math.toRadians(random.nextFloat() * 360.0f));
            this.getDataManager().set(targetDirectionY, (float) Math.toRadians(random.nextFloat() * 180.0f - 90.0f));
            this.getDataManager().setDirty(targetDirectionX);
            this.getDataManager().setDirty(targetDirectionY);
        }

        noClip = false;
        boolean insideBlock = isEntityInsideOpaqueBlock();
        if(currentPhase != null) {
            if (insideBlock && !isDigging())
                currentPhase.onDig(this);
            if (!insideBlock && isDigging())
                currentPhase.onUndig(this);
        }
        setDigging(insideBlock);
        noClip = true;


        //Spline test = new Spline(t -> new Vec3d(Math.sin(t*Math.PI)*80,t*80,t*80));
        //test.cachePoints(10,1.0, 1.0);

        if(currentPhase != null)
            velocityScale = (float)currentPhase.getVelocity(this);
        if (getDataManager().get(fadeTimer) <= 180 && getDataManager().get(fadeTimer) != 0) {
            velocityScale = 0;
        }

        int interval = 25;
        if (this.ticksExisted % 200 >= 180) {
            interval = 5;
        }
        /*if (this.ticksExisted % interval == 0 || this.ticksExisted < 3) {
            prevMoveVec = moveVec;
            moveVec = Misc.lookVector(this.getDataManager().get(targetDirectionX), this.getDataManager().get(targetDirectionY)).scale(0.45f * velocityScale);
        }*/
        //if(ticksExisted < 50) {
        //float motionInterp = ((float) (this.ticksExisted % interval)) / interval;
        //this.motionX = (1.0f - motionInterp) * prevMoveVec.x + (motionInterp) * moveVec.x;
        //this.motionY = (1.0f - motionInterp) * prevMoveVec.y + (motionInterp) * moveVec.y;
        //this.motionZ = (1.0f - motionInterp) * prevMoveVec.z + (motionInterp) * moveVec.z;
        for(int i = 0; i < getSegments(); i++) {
            setSegmentFade(i,1.0f);
        }
        if(currentSpline != null) {
            distMoved += velocityScale;
            Vec3d nextPoint = currentSpline.getPoint(distMoved);
            if (distMoved < splineInterval) {
                double intervalLerp = distMoved / splineInterval;
                nextPoint = new Vec3d(MathHelper.clampedLerp(posX + moveVec.x, nextPoint.x, intervalLerp),
                        MathHelper.clampedLerp(posY + moveVec.y, nextPoint.y, intervalLerp),
                        MathHelper.clampedLerp(posZ + moveVec.z, nextPoint.z, intervalLerp));
            } else if (distMoved > currentSpline.getTotalArcLength()) {
                moveVec = new Vec3d(motionX, motionY, motionZ);
            }
            this.motionX = nextPoint.x - posX;
            this.motionY = nextPoint.y - posY;
            this.motionZ = nextPoint.z - posZ;
        }
        this.rotationYaw = (float) Math.toRadians(Misc.yawDegreesBetweenPointsSafe(0, 0, 0, motionX, motionY, motionZ, rotationYaw));
        this.rotationPitch = (float) Math.toRadians(Misc.pitchDegreesBetweenPoints(0, 0, 0, motionX, motionY, motionZ));
        if (getDataManager().get(fadeTimer) > 180 || getDataManager().get(fadeTimer) == 0) {
            if (pastPositions.get(0).x == 0 && pastPositions.get(0).y == 0 && pastPositions.get(0).z == 0) {
                pastPositions.set(0, new Vec3d(posX,posY,posZ));
            }
            for (int i = 1; i < pastPositions.size(); i++) { //Sanitize
                if (pastPositions.get(i).x == 0 && pastPositions.get(i).y == 0 && pastPositions.get(i).z == 0) {
                    pastPositions.set(i, pastPositions.get(i - 1));
                }
            }
            if(world.isRemote)
                System.out.println();
            double dx = posX - prevPosX;
            double dy = posY - prevPosY;
            double dz = posZ - prevPosZ;
            pastPositionDistance += Math.sqrt(dx*dx+dy*dy+dz*dz);
            for(int g = 0; g < pastPositionDistance / 0.5; g++) {
                for (int i = pastPositions.size() - 1; i > 0; i--) {
                    pastPositions.set(i, pastPositions.get(i).scale(0.5).add(pastPositions.get(i - 1).scale(0.5)));
                }
                pastPositions.set(0, new Vec3d(posX, posY, posZ));
                pastPositionDistance -= 0.5;
            }
        }
        for(int i = 0; i < wormPartArray.length; i++) {
            Vec3d position = pastPositions.get(i);
            Vec3d positionLast = pastPositionsLast.get(i);
            MultiPartEntityPart part = wormPartArray[i];
            part.onUpdate();
            part.setLocationAndAngles(position.x,position.y,position.z,0,0);
            part.prevPosX = positionLast.x;
            part.prevPosY = positionLast.y;
            part.prevPosZ = positionLast.z;
        }
        //}
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        if (getDataManager().get(pacified)) {
            return true;
        }
        return false;
    }

    @Override
    public int getBrightnessForRender() {
        float f = 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        int i = super.getBrightnessForRender();
        int j = i & 255;
        int k = i >> 16 & 255;
        j = j + (int) (f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }
        return j | k << 16;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        //getEntityWorld().playSound(posX, posY, posZ, hurtSound, SoundCategory.NEUTRAL, random.nextFloat() * 0.1f + 0.95f, random.nextFloat() * 0.1f + 0.95f, false);
        if (source.getTrueSource() instanceof EntityLivingBase) {
            this.setAttackTarget((EntityLivingBase) source.getTrueSource());
            EntityLivingBase entity = ((EntityLivingBase) source.getTrueSource());
            //this.moveVec.addVector(entity.getLookVec().x, entity.getLookVec().y, entity.getLookVec().z);
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void damageEntity(DamageSource source, float amount) {
        if (this.getHealth() - amount <= 0 && !getDataManager().get(pacified).booleanValue()) {
            this.setHealth(1);
            this.bossInfo.setPercent(0);
            getDataManager().set(pacified, true);
            getDataManager().setDirty(pacified);
            getDataManager().set(fadeTimer, 200);
            if (source.getTrueSource() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) source.getTrueSource();
                //        if (!player.hasAchievement(RegistryManager.achieveGuardianBoss)) {
                //          PlayerManager.addAchievement(player, RegistryManager.achieveGuardianBoss);
                //        }
            }
        } else {
            if (!getDataManager().get(pacified).booleanValue()) {
                super.damageEntity(source, amount);
                this.bossInfo.setPercent(getHealth() / getMaxHealth());
            }
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if (entity instanceof EntityLivingBase) {
            this.setAttackTarget((EntityLivingBase) entity);
        }
        return super.attackEntityAsMob(entity);
    }

    @Override
    public boolean isAIDisabled() {
        return false;
    }

    @Override
    public void setDead() {
        if (!this.isDead && !this.world.isRemote) {
            this.entityDropItem(new ItemStack(Items.TOTEM_OF_UNDYING), 4.0F);
        }
        super.setDead();
        //world.playSound(posX, posY, posZ, hurtSound, SoundCategory.NEUTRAL, random.nextFloat() * 0.1f + 0.95f, (random.nextFloat() * 0.1f + 0.7f) / 2.0f, false);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(320.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(2.0D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        getDataManager().set(targetDirectionX, compound.getFloat("targetDirectionX"));
        getDataManager().set(targetDirectionY, compound.getFloat("targetDirectionY"));
        getDataManager().set(fadeTimer, compound.getInteger("fadeTimer"));
        getDataManager().set(pacified, compound.getBoolean("pacified"));
        getDataManager().set(tracking, compound.getBoolean("tracking"));
        getDataManager().setDirty(targetDirectionX);
        getDataManager().setDirty(targetDirectionY);
        getDataManager().setDirty(pacified);
        getDataManager().setDirty(tracking);
        getDataManager().setDirty(fadeTimer);
        this.bossInfo.setPercent(getHealth() / getMaxHealth());
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setFloat("targetDirectionX", getDataManager().get(targetDirectionX));
        compound.setFloat("targetDirectionY", getDataManager().get(targetDirectionY));
        compound.setBoolean("pacified", getDataManager().get(pacified));
        compound.setBoolean("tracking", getDataManager().get(tracking));
        compound.setInteger("fadeTimer", getDataManager().get(fadeTimer));
    }

    public float getFade(float partialTicks) {
        if (getDataManager().get(fadeTimer) == 0) {
            return 1.0f;
        } else {
            return Math.max(0, (((float) getDataManager().get(fadeTimer) - partialTicks)) / 200.0f);
        }
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        bossInfo.removePlayer(player);
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return new ResourceLocation(Embers.MODID, "entity/magma_worm");
    }

    @Override
    public World getWorld() {
        return world;
    }

    public int getIndex(MultiPartEntityPart part) {
        Entity[] parts = getParts();
        for(int i = 0; i < parts.length; i++) {
            if(parts[i] == part)
                return i;
        }
        return -1;
    }

    @Override
    public boolean attackEntityFromPart(MultiPartEntityPart part, DamageSource source, float damage) {
        int index = getIndex(part);
        return false;
    }
}