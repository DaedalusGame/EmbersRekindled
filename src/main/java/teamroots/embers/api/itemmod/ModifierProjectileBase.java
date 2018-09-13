package teamroots.embers.api.itemmod;

import net.minecraft.item.ItemStack;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.item.IProjectileWeapon;

public class ModifierProjectileBase extends ModifierBase {
    public ModifierProjectileBase(String name, double cost, boolean levelCounts) {
        super(EnumType.ALL, name, cost, levelCounts);
    }

    @Override
    public boolean canApplyTo(ItemStack stack) {
        return stack.getItem() instanceof IProjectileWeapon || ItemModUtil.hasModifier(stack, EmbersAPI.CASTER_ORB);
    }
}
