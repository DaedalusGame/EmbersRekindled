package teamroots.embers.api.projectile;

import net.minecraft.entity.Entity;
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
    Color color;

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

    @Override
    public void shoot(World world) {
        entity = new EntityEmberProjectile(world);
        entity.initCustom(pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z, size, shooter);
        entity.setEffect(effect);
        entity.setPreset(this);
        entity.setLifetime(lifetime);
        entity.setColor(color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha());
        world.spawnEntity(entity);
    }
}
