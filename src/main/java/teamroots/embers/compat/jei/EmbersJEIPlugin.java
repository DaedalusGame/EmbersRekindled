package teamroots.embers.compat.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import teamroots.embers.RegistryManager;
import teamroots.embers.compat.jei.category.*;
import teamroots.embers.compat.jei.wrapper.*;
import teamroots.embers.recipe.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@JEIPlugin
public class EmbersJEIPlugin implements IModPlugin {
    public static IJeiHelpers HELPER;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new StampRecipeCategory(guiHelper));
        registry.addRecipeCategories(new MelterRecipeCategory(guiHelper));
        registry.addRecipeCategories(new GeologicSeparatorRecipeCategory(guiHelper));
        registry.addRecipeCategories(new MixingRecipeCategory(guiHelper));
        registry.addRecipeCategories(new AlchemyRecipeCategory(guiHelper));
        registry.addRecipeCategories(new DawnstoneAnvilCategory(guiHelper));
    }

    @Override
    public void register(IModRegistry reg)
    {
        HELPER = reg.getJeiHelpers();

        reg.handleRecipes(ItemStampingRecipe.class,StampingRecipeWrapper::new,StampRecipeCategory.UID);
        reg.handleRecipes(ItemMeltingRecipe.class, MeltingRecipeWrapper::new,MelterRecipeCategory.UID);
        reg.handleRecipes(ItemMeltingRecipe.class, MeltingRecipeWrapper::new,GeologicSeparatorRecipeCategory.UID);
        reg.handleRecipes(FluidMixingRecipe.class,MixingRecipeWrapper::new,MixingRecipeCategory.UID);
        reg.handleRecipes(AlchemyRecipe.class,AlchemyRecipeWrapper::new,AlchemyRecipeCategory.UID);
        reg.handleRecipes(DawnstoneAnvilRecipe.class, DawnstoneAnvilWrapper::new,DawnstoneAnvilCategory.UID);

        reg.addRecipes(expandRecipes(RecipeRegistry.stampingRecipes),StampRecipeCategory.UID);
        reg.addRecipes(expandRecipes(RecipeRegistry.meltingRecipes),MelterRecipeCategory.UID);
        reg.addRecipes(RecipeRegistry.meltingRecipes.stream().filter(recipe -> recipe.getBonusOutput() != null).collect(Collectors.toList()), GeologicSeparatorRecipeCategory.UID);
        reg.addRecipes(expandRecipes(RecipeRegistry.mixingRecipes),MixingRecipeCategory.UID);
        reg.addRecipes(expandRecipes(RecipeRegistry.alchemyRecipes),AlchemyRecipeCategory.UID);
        reg.addRecipes(expandRecipes(RecipeRegistry.dawnstoneAnvilRecipes),DawnstoneAnvilCategory.UID);

        reg.addRecipeCatalyst(new ItemStack(RegistryManager.stamper),StampRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(RegistryManager.block_furnace),MelterRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(RegistryManager.geo_separator),GeologicSeparatorRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(RegistryManager.block_furnace),GeologicSeparatorRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(RegistryManager.mixer),MixingRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(RegistryManager.alchemy_tablet),AlchemyRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(RegistryManager.dawnstone_anvil),DawnstoneAnvilCategory.UID);
    }

    public static List<List<ItemStack>> expandIngredients(Ingredient ingredient) {
        return expandIngredients(Lists.newArrayList(ingredient));
    }

    public static List<List<ItemStack>> expandIngredients(List<Ingredient> ingredients) {
        IStackHelper stackHelper = HELPER.getStackHelper();
        return stackHelper.expandRecipeItemStackInputs(ingredients);
    }

    public static List<Object> expandRecipes(List<?> recipes) {
        return recipes.stream().map(EmbersJEIPlugin::expandRecipe).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public static List<?> expandRecipe(Object recipe) {
        if(recipe instanceof IWrappableRecipe)
            return ((IWrappableRecipe) recipe).getWrappers();
        else
            return Lists.newArrayList(recipe);
    }
}
