package teamroots.embers.compat.jei;

import java.util.ArrayList;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;
import teamroots.embers.RegistryManager;
import teamroots.embers.recipe.RecipeRegistry;


@JEIPlugin
public class EmbersJEIPlugin extends BlankModPlugin {
    @Override
    public void register(IModRegistry reg)
    {
    	IJeiHelpers helper = reg.getJeiHelpers();
        IGuiHelper guiHelper = helper.getGuiHelper();

        reg.addRecipeCategories(new StampRecipeCategory(guiHelper));
        reg.addRecipeHandlers(new StampRecipeHandler());

        ArrayList<StampingRecipeWrapper> stampingRecipes = new ArrayList<StampingRecipeWrapper>();
        for (int i = 0; i < RecipeRegistry.stampingRecipes.size(); i ++){
        	stampingRecipes.add(new StampingRecipeWrapper(RecipeRegistry.stampingRecipes.get(i)));
        }
        for (int i = 0; i < RecipeRegistry.stampingOreRecipes.size(); i ++){
        	stampingRecipes.add(new StampingRecipeWrapper(RecipeRegistry.stampingOreRecipes.get(i)));
        }
        reg.addRecipes(stampingRecipes);

        reg.addRecipeCategories(new MelterRecipeCategory(guiHelper));
        reg.addRecipeHandlers(new MelterRecipeHandler());

        ArrayList<MeltingRecipeWrapper> meltingRecipes = new ArrayList<MeltingRecipeWrapper>();
        for (int i = 0; i < RecipeRegistry.meltingRecipes.keySet().size(); i ++){
        	ItemStack key = (ItemStack)RecipeRegistry.meltingRecipes.keySet().toArray()[i];
        	meltingRecipes.add(new MeltingRecipeWrapper(RecipeRegistry.meltingRecipes.get(key)));
        }
    	Object[] keys = RecipeRegistry.meltingOreRecipes.keySet().toArray();
        for (int i = 0; i < keys.length; i ++){
        	String key = keys[i].toString();
        	meltingRecipes.add(new MeltingRecipeWrapper(RecipeRegistry.meltingOreRecipes.get(key)));
        }
        reg.addRecipes(meltingRecipes);

        reg.addRecipeCategoryCraftingItem(new ItemStack(RegistryManager.stamper),"embers.stamp");
        reg.addRecipeCategoryCraftingItem(new ItemStack(RegistryManager.blockFurnace),"embers.melter");
    }
}
