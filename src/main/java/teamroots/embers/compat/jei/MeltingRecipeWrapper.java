package teamroots.embers.compat.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import teamroots.embers.Embers;
import teamroots.embers.recipe.ItemMeltingOreRecipe;
import teamroots.embers.recipe.ItemMeltingRecipe;

public class MeltingRecipeWrapper extends BlankRecipeWrapper {

	public ItemMeltingRecipe recipe = null;

	
	public MeltingRecipeWrapper(ItemMeltingRecipe recipe){
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ItemStack.class, recipe.getStack());
		ingredients.setOutput(FluidStack.class, recipe.getFluid());
	}
}
