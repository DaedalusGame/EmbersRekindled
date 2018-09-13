package teamroots.embers.api.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

import javax.annotation.Nullable;

public interface IInflictorGem {
    void attuneSource(ItemStack stack, @Nullable EntityLivingBase entity, DamageSource source);

    @Nullable
    String getAttunedSource(ItemStack stack);

    float getDamageResistance(ItemStack stack, float modifier);
}
