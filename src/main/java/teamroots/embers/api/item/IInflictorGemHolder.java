package teamroots.embers.api.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public interface IInflictorGemHolder {
    boolean canAttachGem(ItemStack holder, ItemStack gem);

    void attachGem(ItemStack holder, ItemStack gem);

    void clearGems(ItemStack holder);

    ItemStack[] getAttachedGems(ItemStack holder);

    float getTotalDamageResistance(EntityLivingBase entity, DamageSource source, ItemStack holder);
}
