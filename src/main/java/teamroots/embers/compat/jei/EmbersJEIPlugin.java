package teamroots.embers.compat.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import teamroots.embers.RegistryManager;
import teamroots.embers.compat.jei.category.AlchemyRecipeCategory;
import teamroots.embers.compat.jei.category.MelterRecipeCategory;
import teamroots.embers.compat.jei.category.MixingRecipeCategory;
import teamroots.embers.compat.jei.category.StampRecipeCategory;
import teamroots.embers.compat.jei.wrapper.AlchemyRecipeWrapper;
import teamroots.embers.compat.jei.wrapper.MeltingRecipeWrapper;
import teamroots.embers.compat.jei.wrapper.MixingRecipeWrapper;
import teamroots.embers.compat.jei.wrapper.StampingRecipeWrapper;
import teamroots.embers.recipe.*;

import java.util.List;


@JEIPlugin
public class EmbersJEIPlugin implements IModPlugin {
    public static IJeiHelpers HELPER;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new StampRecipeCategory(guiHelper));
        registry.addRecipeCategories(new MelterRecipeCategory(guiHelper));
        registry.addRecipeCategories(new MixingRecipeCategory(guiHelper));
        registry.addRecipeCategories(new AlchemyRecipeCategory(guiHelper));
    }

    @Override
    public void register(IModRegistry reg)
    {
        HELPER = reg.getJeiHelpers();

        reg.handleRecipes(ItemStampingRecipe.class,StampingRecipeWrapper::new,"embers.stamp");
        reg.handleRecipes(ItemMeltingRecipe.class,MeltingRecipeWrapper::new,"embers.melter");
        reg.handleRecipes(FluidMixingRecipe.class,MixingRecipeWrapper::new,"embers.mixer");
        reg.handleRecipes(AlchemyRecipe.class,AlchemyRecipeWrapper::new,"embers.alchemy");

        reg.addRecipes(RecipeRegistry.stampingRecipes,"embers.stamp");
        reg.addRecipes(RecipeRegistry.meltingRecipes,"embers.melter");
        reg.addRecipes(RecipeRegistry.mixingRecipes,"embers.mixer");
        reg.addRecipes(RecipeRegistry.alchemyRecipes,"embers.alchemy");

        reg.addRecipeCatalyst(new ItemStack(RegistryManager.stamper),"embers.stamp");
        reg.addRecipeCatalyst(new ItemStack(RegistryManager.block_furnace),"embers.melter");
        reg.addRecipeCatalyst(new ItemStack(RegistryManager.mixer),"embers.mixer");
        reg.addRecipeCatalyst(new ItemStack(RegistryManager.alchemy_tablet),"embers.alchemy");
    }

    public static List<List<ItemStack>> expandIngredients(Ingredient ingredient) {
        return expandIngredients(Lists.newArrayList(ingredient));
    }

    public static List<List<ItemStack>> expandIngredients(List<Ingredient> ingredients) {
        IStackHelper stackHelper = HELPER.getStackHelper();
        return stackHelper.expandRecipeItemStackInputs(ingredients);
    }
}
