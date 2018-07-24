package teamroots.embers.itemmod;

import net.minecraft.item.ItemStack;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.api.itemmod.ModifierBase;
import teamroots.embers.item.ItemCinderStaff;
import teamroots.embers.item.ItemIgnitionCannon;

public class ModifierProjectileBase extends ModifierBase {
    public ModifierProjectileBase(String name, double cost, boolean levelCounts) {
        super(EnumType.ALL, name, cost, levelCounts);
    }

    @Override
    public boolean canApplyTo(ItemStack stack) {
        return stack.getItem() instanceof ItemIgnitionCannon || stack.getItem() instanceof ItemCinderStaff || ItemModUtil.hasModifier(stack, EmbersAPI.CASTER_ORB);
    }
}
