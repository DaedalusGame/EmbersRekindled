package teamroots.embers.compat.crafttweaker;

import crafttweaker.api.item.IIngredient;
import net.minecraft.item.crafting.Ingredient;

import java.awt.*;

public class CTUtil {
    public static Ingredient toIngredient(IIngredient ingredient) {
        if(ingredient == null)
            return Ingredient.EMPTY;
        return new IngredientCraftTweaker(ingredient);
    }

    public static Color parseColor(int[] rgb) {
        Color color = Color.WHITE;
        if (rgb != null && rgb.length >= 3 && rgb.length <= 4)
            color = new Color(rgb[0], rgb[1], rgb[2], rgb.length == 4 ? rgb[3] : 255);
        return color;
    }
}
