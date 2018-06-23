package teamroots.embers.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import teamroots.embers.util.IngredientSpecial;

public class AnvilRepairFakeRecipe extends DawnstoneAnvilRecipe {
    public AnvilRepairFakeRecipe(ItemStack repairable) {
        super(Ingredient.fromStacks(withDamage(repairable,repairable.getMaxDamage() / 2)),new IngredientSpecial(stack -> repairable.getItem().getIsRepairable(repairable,stack)),new ItemStack[]{repairable});
    }

    private static ItemStack withDamage(ItemStack stack, int damage) {
        stack = stack.copy();
        stack.setItemDamage(damage);
        return stack;
    }
}
