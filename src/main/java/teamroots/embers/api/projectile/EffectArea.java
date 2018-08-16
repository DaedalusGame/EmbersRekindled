package teamroots.embers.api.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class EffectArea implements IProjectileEffect {
    IProjectileEffect effect;
    double radius;
    boolean activateOnFizzle;

    public EffectArea(IProjectileEffect effect, double radius, boolean activateOnFizzle) {
        this.effect = effect;
        this.radius = radius;
        this.activateOnFizzle = activateOnFizzle;
    }

    public IProjectileEffect getEffect() {
        return effect;
    }

    public void setEffect(IProjectileEffect effect) {
        this.effect = effect;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public boolean doesActivateOnFizzle() {
        return activateOnFizzle;
    }

    public void setActivateOnFizzle(boolean activateOnFizzle) {
        this.activateOnFizzle = activateOnFizzle;
    }

    @Override
    public void onHit(World world, RayTraceResult raytrace, IProjectilePreset projectile) {
        Vec3d pos = raytrace.hitVec;
        doAreaEffect(world, projectile, pos);
    }

    @Override
    public void onFizzle(World world, Vec3d pos, @Nullable IProjectilePreset projectile) {
        if(activateOnFizzle)
            doAreaEffect(world, projectile, pos);
    }

    private void doAreaEffect(World world, IProjectilePreset projectile, Vec3d pos) {
        AxisAlignedBB aabb = new AxisAlignedBB(pos.x - radius, pos.y - radius, pos.z - radius, pos.x + radius, pos.y + radius, pos.z + radius);
        List<Entity> entities = world.getEntitiesInAABBexcluding(projectile.getShooter(),aabb, entity -> true);
        for (Entity entity : entities) {
            effect.onHit(world,new RayTraceResult(entity),projectile);
        }
    }
}
