package teamroots.embers.compat.jei.wrapper;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import teamroots.embers.recipe.DawnstoneAnvilRecipe;
import teamroots.embers.recipe.IFocusRecipe;

import java.util.Arrays;

public class DawnstoneAnvilWrapper extends BaseRecipeWrapper<DawnstoneAnvilRecipe> {
    public DawnstoneAnvilWrapper(DawnstoneAnvilRecipe recipe)
    {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, Arrays.asList(recipe.getTopInputs(), recipe.getBottomInputs()));
        ingredients.setOutputLists(ItemStack.class, Arrays.asList(recipe.getOutputs()));
    }
}
