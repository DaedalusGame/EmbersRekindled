package teamroots.embers.compat.crafttweaker;

import crafttweaker.api.item.IIngredient;
import net.minecraft.item.crafting.Ingredient;

public class CTUtil {
    public static Ingredient toIngredient(IIngredient ingredient) {
        if(ingredient == null)
            return Ingredient.EMPTY;
        return new IngredientCraftTweaker(ingredient);
    }
}
