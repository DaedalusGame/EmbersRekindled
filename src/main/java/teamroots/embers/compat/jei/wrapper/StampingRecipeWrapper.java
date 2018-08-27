package teamroots.embers.compat.jei.wrapper;

import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.recipe.ItemStampingRecipe;

import java.util.ArrayList;
import java.util.List;

public class StampingRecipeWrapper extends BaseRecipeWrapper<ItemStampingRecipe> {
	public StampingRecipeWrapper(ItemStampingRecipe recipe){
		this.recipe = recipe;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		List<ItemStack> inputStacks = recipe.getInputs();
		ArrayList<ItemStack> stampStacks = Lists.newArrayList(recipe.stamp.getMatchingStacks());
		List<ItemStack> outputStacks = recipe.getOutputs();
		ingredients.setInputLists(ItemStack.class, Lists.newArrayList(inputStacks,stampStacks));
		ingredients.setInput(FluidStack.class, recipe.getFluid());
		ingredients.setOutputLists(ItemStack.class, Lists.<List<ItemStack>>newArrayList(outputStacks));
	}

}
