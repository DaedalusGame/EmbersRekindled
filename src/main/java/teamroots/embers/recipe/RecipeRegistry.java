package teamroots.embers.recipe;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.RegistryManager;

public class RecipeRegistry {

	public static Map<ItemStack,ItemMeltingRecipe> meltingRecipes = new HashMap<ItemStack,ItemMeltingRecipe>();
	public static Map<String,ItemMeltingOreRecipe> meltingOreRecipes = new HashMap<String,ItemMeltingOreRecipe>();
	
	public static void init(){
		meltingOreRecipes.put("oreIron", new ItemMeltingOreRecipe("oreIron",new FluidStack(RegistryManager.fluidMoltenIron,288)));
		meltingOreRecipes.put("ingotIron", new ItemMeltingOreRecipe("ingotIron",new FluidStack(RegistryManager.fluidMoltenIron,144)));
		meltingOreRecipes.put("nuggetIron", new ItemMeltingOreRecipe("nuggetIron",new FluidStack(RegistryManager.fluidMoltenIron,16)));

		meltingOreRecipes.put("oreGold", new ItemMeltingOreRecipe("oreGold",new FluidStack(RegistryManager.fluidMoltenGold,1000)));
		meltingOreRecipes.put("ingotGold", new ItemMeltingOreRecipe("ingotGold",new FluidStack(RegistryManager.fluidMoltenGold,144)));
		meltingOreRecipes.put("nuggetGold", new ItemMeltingOreRecipe("nuggetGold",new FluidStack(RegistryManager.fluidMoltenGold,16)));
	}
}
