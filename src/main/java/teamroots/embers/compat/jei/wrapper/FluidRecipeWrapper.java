package teamroots.embers.compat.jei.wrapper;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public abstract class FluidRecipeWrapper implements IRecipeWrapper {
    public abstract FluidStack getInput();

    public abstract FluidStack getOutput();

    public abstract void addInfo(List<String> tooltip);

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(FluidStack.class, getInput());
        ingredients.setOutput(FluidStack.class, getOutput());
    }


}
