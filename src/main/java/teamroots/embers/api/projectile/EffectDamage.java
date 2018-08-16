package teamroots.embers.api.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;


import javax.annotation.Nullable;
import java.util.function.Function;

public class EffectDamage implements IProjectileEffect {
    float damage;
    Function<IProjectilePreset,DamageSource> source;
    int fire;
    double invinciblityMultiplier = 1.0;

    public EffectDamage(float damage, Function<IProjectilePreset,DamageSource> source, int fire, double invinciblityMultiplier) {
        this.damage = damage;
        this.source = source;
        this.fire = fire;
        this.invinciblityMultiplier = invinciblityMultiplier;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public Function<IProjectilePreset, DamageSource> getSource() {
        return source;
    }

    public void setSource(Function<IProjectilePreset, DamageSource> source) {
        this.source = source;
    }

    public int getFire() {
        return fire;
    }

    public void setFire(int seconds) {
        this.fire = seconds;
    }

    public double getInvinciblityMultiplier() {
        return invinciblityMultiplier;
    }

    public void setInvinciblityMultiplier(double multiplier) {
        this.invinciblityMultiplier = multiplier;
    }

    @Override
    public void onEntityImpact(Entity entity, @Nullable IProjectilePreset projectile) {
        Entity shooter = projectile != null ? projectile.getShooter() : null;
        Entity projectileEntity = projectile != null ? projectile.getEntity() : null;
        entity.attackEntityFrom(source.apply(projectile),damage);
        entity.setFire(fire);
        if(entity instanceof EntityLivingBase) {
            EntityLivingBase livingTarget = (EntityLivingBase) entity;
            livingTarget.setLastAttackedEntity(shooter);
            if (shooter instanceof EntityLivingBase)
                livingTarget.setRevengeTarget((EntityLivingBase) shooter);
            livingTarget.hurtResistantTime = (int) (livingTarget.hurtResistantTime * invinciblityMultiplier);
        }
    }
}
