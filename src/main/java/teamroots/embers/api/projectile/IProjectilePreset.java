package teamroots.embers.api.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IProjectilePreset {
    Vec3d getPos();

    Vec3d getVelocity();

    IProjectileEffect getEffect();

    @Nullable
    Entity getEntity();

    @Nullable
    Entity getShooter();

    void setPos(Vec3d pos);

    void setVelocity(Vec3d velocity);

    void setEffect(IProjectileEffect effect);

    void shoot(World world);
}
