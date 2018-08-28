package teamroots.embers.api.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

public class EffectPotion implements IProjectileEffect {
    PotionEffect effect;

    public EffectPotion(PotionEffect effect) {
        this.effect = effect;
    }

    public PotionEffect getEffect() {
        return effect;
    }

    public void setEffect(PotionEffect effect) {
        this.effect = effect;
    }

    @Override
    public void onEntityImpact(Entity entity, IProjectilePreset projectile) {
        if(entity instanceof EntityLivingBase) {
            ((EntityLivingBase) entity).addPotionEffect(effect);
        }
    }
}
