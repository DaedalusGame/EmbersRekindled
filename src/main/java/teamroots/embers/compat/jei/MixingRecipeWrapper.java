package teamroots.embers.compat.jei;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import teamroots.embers.RegistryManager;
import teamroots.embers.item.EnumStampType;
import teamroots.embers.recipe.FluidMixingRecipe;
import teamroots.embers.recipe.ItemStampingOreRecipe;
import teamroots.embers.recipe.ItemStampingRecipe;

public class MixingRecipeWrapper extends BlankRecipeWrapper {

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
