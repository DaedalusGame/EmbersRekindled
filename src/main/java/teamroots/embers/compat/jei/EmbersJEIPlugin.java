package teamroots.embers.compat.jei;

import java.util.ArrayList;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import net.minecraft.item.ItemStack;
import teamroots.embers.RegistryManager;
import teamroots.embers.recipe.RecipeRegistry;

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

        reg.addRecipeCategoryCraftingItem(new ItemStack(RegistryManager.stamper),"embers.stamp");
    }
}
