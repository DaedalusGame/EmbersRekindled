package teamroots.embers.compat.jei;

import java.util.ArrayList;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;
import teamroots.embers.RegistryManager;
import teamroots.embers.compat.jei.AlchemyRecipeCategory;
import teamroots.embers.compat.jei.AlchemyRecipeHandler;
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
        for (int i = 0; i < RecipeRegistry.meltingRecipes.size(); i ++){
        	ItemStack key = (ItemStack)RecipeRegistry.meltingRecipes.get(i).getStack();
        	meltingRecipes.add(new MeltingRecipeWrapper(RecipeRegistry.meltingRecipes.get(i)));
        }
        for (int i = 0; i < RecipeRegistry.meltingOreRecipes.size(); i ++){
        	meltingRecipes.add(new MeltingRecipeWrapper(RecipeRegistry.meltingOreRecipes.get(i)));
        }
        reg.addRecipes(meltingRecipes);

        reg.addRecipeCategories(new MixingRecipeCategory(guiHelper));
        reg.addRecipeHandlers(new MixingRecipeHandler());

        ArrayList<MixingRecipeWrapper> mixingRecipes = new ArrayList<MixingRecipeWrapper>();
        for (int i = 0; i < RecipeRegistry.mixingRecipes.size(); i ++){
        	mixingRecipes.add(new MixingRecipeWrapper(RecipeRegistry.mixingRecipes.get(i)));
        }
        reg.addRecipes(mixingRecipes);

        reg.addRecipeCategories(new AlchemyRecipeCategory(guiHelper));
        reg.addRecipeHandlers(new AlchemyRecipeHandler());

        ArrayList<AlchemyRecipeWrapper> alchemyRecipes = new ArrayList<AlchemyRecipeWrapper>();
        for (int i = 0; i < RecipeRegistry.alchemyRecipes.size(); i ++){
        	alchemyRecipes.add(new AlchemyRecipeWrapper(RecipeRegistry.alchemyRecipes.get(i)));
        }
        reg.addRecipes(alchemyRecipes);

        reg.addRecipeCategoryCraftingItem(new ItemStack(RegistryManager.stamper),"embers.stamp");
        reg.addRecipeCategoryCraftingItem(new ItemStack(RegistryManager.block_furnace),"embers.melter");
        reg.addRecipeCategoryCraftingItem(new ItemStack(RegistryManager.mixer),"embers.mixer");
        reg.addRecipeCategoryCraftingItem(new ItemStack(RegistryManager.alchemy_tablet),"embers.alchemy");
    }
}
