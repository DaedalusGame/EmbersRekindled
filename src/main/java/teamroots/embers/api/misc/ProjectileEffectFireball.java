package teamroots.embers.api.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import teamroots.embers.entity.EntityEmberProjectile;

import javax.annotation.Nullable;

public class ProjectileEffectFireball implements IProjectileEffect {
    float damage;
    DamageSource damageSource;
    int fireTicks;
    float knockback;
    double size;

    public ProjectileEffectFireball(float damage, DamageSource damageSource, int fireTicks, float knockback) {
        this.damage = damage;
        this.damageSource = damageSource;
        this.fireTicks = fireTicks;
        this.knockback = knockback;
    }

    @Override
    public void onEntityImpact(Entity entity, EntityLivingBase shooter, @Nullable Entity projectile) {
        entity.attackEntityFrom(damageSource,damage);
        entity.setFire(fireTicks);
        if(projectile != null && entity instanceof EntityLivingBase)
            ((EntityLivingBase) entity).knockBack(shooter,knockback,projectile.motionX,projectile.motionZ);
    }

    @Override
    public void onBlockImpact(World world, BlockPos pos, EntityLivingBase shooter, @Nullable Entity projectile) {
        //NOOP
    }

    @Override
    public void onFizzle(World world, Vec3d pos, @Nullable Entity projectile) {
        //NOOP
    }

    @Override
    public void shoot(World world, Vec3d pos, Vec3d velocity, EntityLivingBase shooter, ItemStack weapon) {
        EntityEmberProjectile proj = new EntityEmberProjectile(world);
        proj.initCustom(pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z, size, shooter.getUniqueID());
    }
}
