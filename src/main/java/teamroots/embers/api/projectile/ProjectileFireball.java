package teamroots.embers.api.projectile;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import teamroots.embers.entity.EntityEmberProjectile;

import javax.annotation.Nullable;
import java.awt.*;

public class ProjectileFireball implements IProjectilePreset {
    Vec3d pos;
    Vec3d velocity;
    IProjectileEffect effect;
    double size;
    int lifetime;
    Entity shooter;
    EntityEmberProjectile entity;
    Color color = new Color(255,64,16);
    double gravity;

    int homingTime;
    double homingRange;
    int homingIndex, homingModulo;
    Predicate<Entity> homingPredicate;

    public ProjectileFireball(Entity shooter, Vec3d pos, Vec3d velocity, double size, int lifetime, IProjectileEffect effect) {
        this.pos = pos;
        this.velocity = velocity;
        this.effect = effect;
        this.size = size;
        this.lifetime = lifetime;
        this.shooter = shooter;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public int getLifetime() {
        return lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
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

    public double getGravity() {
        return gravity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    @Override
    public IProjectileEffect getEffect() {
        return effect;
    }

    @Nullable
    @Override
    public Entity getEntity() {
        return entity;
    }

    @Nullable
    @Override
    public Entity getShooter() {
        return shooter;
    }

    @Override
    public void setEffect(IProjectileEffect effect) {
        this.effect = effect;
    }

    public void setHoming(int time, double range, int index, int modulo, Predicate<Entity> predicate) {
        homingTime = time;
        homingRange = range;
        homingIndex = index;
        homingModulo = modulo;
        homingPredicate = predicate;
    }

    @Override
    public void shoot(World world) {
        entity = new EntityEmberProjectile(world);
        entity.initCustom(pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z, size, shooter);
        entity.setGravity(gravity);
        entity.setEffect(effect);
        entity.setPreset(this);
        entity.setLifetime(lifetime);
        entity.setColor(color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha());
        entity.setHoming(homingTime,homingRange,homingIndex,homingModulo,homingPredicate);
        world.spawnEntity(entity);
    }
}
