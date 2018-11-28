package teamroots.embers.api.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public interface IInflictorGemHolder {
    int getGemSlots(ItemStack holder);

    boolean canAttachGem(ItemStack holder, ItemStack gem);

    void attachGem(ItemStack holder, ItemStack gem, int slot);

    ItemStack detachGem(ItemStack holder, int slot);

    void clearGems(ItemStack holder);

    default int getAttachedGemCount(ItemStack holder) {
        int amt = 0;
        for (ItemStack stack : getAttachedGems(holder)) {
            if(!stack.isEmpty())
                amt++;
        }
        return amt;
    }

    ItemStack[] getAttachedGems(ItemStack holder);

    float getTotalDamageResistance(EntityLivingBase entity, DamageSource source, ItemStack holder);
}
