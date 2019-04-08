package teamroots.embers.compat.jei.wrapper;

import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.compat.jei.EmbersJEIPlugin;
import teamroots.embers.recipe.ItemMeltingRecipe;

public class MeltingRecipeWrapper extends BaseRecipeWrapper<ItemMeltingRecipe> {
	public MeltingRecipeWrapper(ItemMeltingRecipe recipe){
		this.recipe = recipe;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, EmbersJEIPlugin.expandIngredients(recipe.getInput()));
		ingredients.setOutputs(FluidStack.class, Lists.newArrayList(recipe.getFluid(), recipe.getBonusOutput()));
	}
}
