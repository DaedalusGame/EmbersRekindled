package teamroots.embers.compat.jei;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import teamroots.embers.recipe.AlchemyRecipe;
import teamroots.embers.recipe.ItemMeltingOreRecipe;
import teamroots.embers.recipe.ItemMeltingRecipe;

public class AlchemyRecipeWrapper extends BlankRecipeWrapper {

	public AlchemyRecipe recipe = null;
	
	public AlchemyRecipeWrapper(AlchemyRecipe recipe){
		this.recipe = recipe;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		List<ItemStack> inputs = new ArrayList<ItemStack>();
		inputs.add(recipe.centerInput);
		inputs.addAll(recipe.inputs);
		ingredients.setInputs(ItemStack.class, inputs);
		ingredients.setOutput(ItemStack.class, recipe.result);
	}

}
