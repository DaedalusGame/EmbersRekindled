package teamroots.embers.compat.jei.wrapper;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import teamroots.embers.compat.jei.EmbersJEIPlugin;
import teamroots.embers.recipe.AlchemyRecipe;
import teamroots.embers.util.AspectList;
import teamroots.embers.util.IHasAspects;

import java.util.ArrayList;

public class AlchemyRecipeWrapper implements IRecipeWrapper, IHasAspects {

	public AlchemyRecipe recipe;

	public AlchemyRecipeWrapper(AlchemyRecipe recipe)
	{
		this.recipe = recipe;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ArrayList<Ingredient> inputs = new ArrayList<>();
		inputs.add(recipe.centerIngredient);
		inputs.addAll(recipe.outsideIngredients);

		ingredients.setInputLists(ItemStack.class, EmbersJEIPlugin.expandIngredients(inputs));
		ingredients.setOutput(ItemStack.class, recipe.result);
	}

	@Override
	public AspectList.AspectRangeList getAspects() {
		return recipe.getAspects();
	}

}
