package teamroots.embers.compat.jei.wrapper;

import mezz.jei.api.recipe.IRecipeWrapper;
import teamroots.embers.recipe.IFocusRecipe;

public abstract class BaseRecipeWrapper<T> implements IRecipeWrapper {
    T recipe;

    public boolean isFocusRecipe() {
        return recipe instanceof IFocusRecipe;
    }

    public IFocusRecipe getFocusRecipe() {
        return (IFocusRecipe) recipe;
    }
}
