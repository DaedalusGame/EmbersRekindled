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
import teamroots.embers.recipe.ItemMeltingOreRecipe;
import teamroots.embers.recipe.ItemMeltingRecipe;
import teamroots.embers.recipe.ItemStampingOreRecipe;
import teamroots.embers.recipe.ItemStampingRecipe;

public class MeltingRecipeWrapper extends BlankRecipeWrapper {

	public ItemMeltingRecipe recipe = null;
	
	public ItemMeltingOreRecipe oreRecipe = null;
	
	boolean isOreRecipe = false;
	
	public MeltingRecipeWrapper(ItemMeltingRecipe recipe){
		this.recipe = recipe;
		isOreRecipe = false;
	}
	
	public MeltingRecipeWrapper(ItemMeltingOreRecipe recipe){
		this.oreRecipe = recipe;
		isOreRecipe = true;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		if (!isOreRecipe){
			ingredients.setInput(ItemStack.class, recipe.getStack());
			ingredients.setOutput(FluidStack.class, recipe.getFluid());
		}
		else {
			ingredients.setInputs(ItemStack.class, OreDictionary.getOres(oreRecipe.getOreName()));
			ingredients.setOutput(FluidStack.class, oreRecipe.getFluid());
		}
	}

}
