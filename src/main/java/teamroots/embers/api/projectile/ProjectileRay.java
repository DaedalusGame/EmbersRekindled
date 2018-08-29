package teamroots.embers.api.projectile;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageCannonBeamFX;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.List;
import java.util.*;

public class ProjectileRay implements IProjectilePreset {
    private static final Predicate<Entity> VALID_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate<Entity>() {
        public boolean apply(@Nullable Entity entity) {
            return entity.canBeCollidedWith();
        }
    });

    Vec3d pos;
    Vec3d velocity;
    IProjectileEffect effect;
    Entity shooter;
    boolean pierceEntities;
    Color color = new Color(255,64,16);

    public ProjectileRay(Entity shooter, Vec3d start, Vec3d end, boolean pierceEntities, IProjectileEffect effect) {
        this.pos = start;
        this.velocity = end.subtract(start);
        this.effect = effect;
        this.shooter = shooter;
        this.pierceEntities = pierceEntities;
    }

    @Override
    public Vec3d getPos() {
        return pos;
    }

    @Override
    public void setPos(Vec3d pos) {
        this.pos = pos;
    }

    @Override
    public Vec3d getVelocity() {
        return velocity;
    }

    @Override
    public void setVelocity(Vec3d velocity) {
        this.velocity = velocity;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public IProjectileEffect getEffect() {
        return effect;
    }

    @Override
    public void setEffect(IProjectileEffect effect) {
        this.effect = effect;
    }

    public boolean canPierceEntities() {
        return pierceEntities;
    }

    public void setPierceEntities(boolean pierceEntities) {
        this.pierceEntities = pierceEntities;
    }

    @Nullable
    @Override
    public Entity getEntity() {
        return null;
    }

    @Nullable
    @Override
    public Entity getShooter() {
        return shooter;
    }

    @Override
    public void shoot(World world) {
        double startX = getPos().x;
        double startY = getPos().y;
        double startZ = getPos().z;

        double dX = getVelocity().x;
        double dY = getVelocity().y;
        double dZ = getVelocity().z;

        double impactDist = Double.POSITIVE_INFINITY;
        boolean doContinue = true;
        Vec3d currPosVec = getPos();
        Vec3d newPosVector = getPos().add(getVelocity());
        RayTraceResult blockTrace = world.rayTraceBlocks(currPosVec, newPosVector, false, true, false);
        java.util.List<RayTraceResult> entityTraces = Misc.findEntitiesOnPath(world,null,shooter,new AxisAlignedBB(startX-0.3,startY-0.3,startZ-0.3,startX+0.3,startY+0.3,startZ+0.3),currPosVec,newPosVector,VALID_TARGETS);

        double distBlock = blockTrace != null ? getPos().squareDistanceTo(blockTrace.hitVec) : Double.POSITIVE_INFINITY;

        for(RayTraceResult entityTraceFirst : entityTraces) {
            if (doContinue && entityTraceFirst != null && getPos().squareDistanceTo(entityTraceFirst.hitVec) < distBlock) {
                effect.onHit(world, entityTraceFirst, this);
                if (!pierceEntities) {
                    impactDist = getPos().distanceTo(entityTraceFirst.hitVec);
                    doContinue = false;
                }
            }
        }

        if(doContinue && blockTrace != null)
        {
            effect.onHit(world,blockTrace,this);
            impactDist = getPos().distanceTo(blockTrace.hitVec);
            doContinue = false;
        }

        if(doContinue)
        {
            effect.onFizzle(world,newPosVector,this);
            impactDist = getPos().distanceTo(newPosVector);
        }

        if (!world.isRemote){
            PacketHandler.INSTANCE.sendToAll(new MessageCannonBeamFX(startX,startY,startZ,dX,dY,dZ,impactDist,color.getRGB()));
        }
    }
}
