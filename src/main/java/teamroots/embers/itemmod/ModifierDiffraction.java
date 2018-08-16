package teamroots.embers.itemmod;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.event.EmberProjectileEvent;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.api.projectile.*;

import java.util.ListIterator;
import java.util.Random;

public class ModifierDiffraction extends ModifierProjectileBase {
    public ModifierDiffraction() {
        super("diffraction", 2.0, true);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onProjectileFire(EmberProjectileEvent event) {
        ListIterator<IProjectilePreset> projectiles = event.getProjectiles().listIterator();

        ItemStack weapon = event.getStack();
        if(!weapon.isEmpty() && ItemModUtil.hasHeat(weapon)) {
            int level = ItemModUtil.getModifierLevel(weapon, EmbersAPI.DIFFRACTION);
            Entity shooter = event.getShooter();
            Random random = shooter.world.rand;
            if(level > 0)
            while (projectiles.hasNext()) {
                IProjectilePreset projectile = projectiles.next();
                Vec3d velocity = projectile.getVelocity();
                double speed = velocity.lengthVector();
                int bullets = 3 + level;
                IProjectileEffect effect = projectile.getEffect();
                if (projectile instanceof ProjectileRay) {
                    double newspeed = 1.0;
                    adjustEffect(effect, 1.0 / 3.0);
                    projectiles.remove();
                    for (int i = 0; i < bullets; i++) {
                        double spread = 0.1 * level;
                        Vec3d newVelocity = velocity.addVector((random.nextDouble() - 0.5) * speed * 2 * spread, (random.nextDouble() - 0.5) * speed * 2 * spread, (random.nextDouble() - 0.5) * speed * 2 * spread).scale(newspeed / speed);
                        projectiles.add(new ProjectileFireball(projectile.getShooter(), projectile.getPos(), newVelocity, 2.4, 80, effect));
                    }
                }
                else if(projectile instanceof ProjectileFireball) {
                    ProjectileFireball fireball = (ProjectileFireball) projectile;
                    adjustEffect(effect, 1.0 / 3.0);
                    projectiles.remove();
                    for (int i = 0; i < bullets; i++) {
                        double spread = 0.1 * level;
                        Vec3d newVelocity = velocity.addVector((random.nextDouble() - 0.5) * speed * 2 * spread, (random.nextDouble() - 0.5) * speed * 2 * spread, (random.nextDouble() - 0.5) * speed * 2 * spread);
                        projectiles.add(new ProjectileFireball(projectile.getShooter(), projectile.getPos(), newVelocity, fireball.getSize() / 3, fireball.getLifetime() / 2, effect));
                    }
                }
            }
        }
    }

    private void adjustEffect(IProjectileEffect effect, double multiplier) {
        if (effect instanceof EffectArea) {
            EffectArea areaEffect = (EffectArea) effect;
            adjustEffect(areaEffect.getEffect(), multiplier);
            //areaEffect.setRadius(areaEffect.getRadius() * multiplier);
        } else if (effect instanceof EffectDamage) {
            EffectDamage damageEffect = (EffectDamage) effect;
            damageEffect.setDamage((float) (damageEffect.getDamage() * multiplier));
            damageEffect.setInvinciblityMultiplier(0.0);
        }
    }
}
