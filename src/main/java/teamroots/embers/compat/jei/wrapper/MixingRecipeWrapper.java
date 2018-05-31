package teamroots.embers.compat.jei.wrapper;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.recipe.FluidMixingRecipe;

public class MixingRecipeWrapper implements IRecipeWrapper {

	public FluidMixingRecipe recipe = null;
	
	public MixingRecipeWrapper(FluidMixingRecipe recipe){
		this.recipe = recipe;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		if (recipe.inputs != null){
			if (recipe.inputs.size() > 0){
				ingredients.setInputs(FluidStack.class, recipe.inputs);
			}
		}
		ingredients.setOutput(FluidStack.class, recipe.output);
	}

}
