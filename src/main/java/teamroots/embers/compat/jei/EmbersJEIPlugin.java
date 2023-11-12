package teamroots.embers.compat.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.*;
import mezz.jei.api.recipe.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.ConfigManager;
import teamroots.embers.RegistryManager;
import teamroots.embers.api.misc.ILiquidFuel;
import teamroots.embers.apiimpl.EmbersAPIImpl;
import teamroots.embers.compat.MysticalMechanicsIntegration;
import teamroots.embers.compat.jei.category.*;
import teamroots.embers.compat.jei.wrapper.*;
import teamroots.embers.recipe.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@JEIPlugin
public class EmbersJEIPlugin implements IModPlugin {
    static class LiquidFuelWithInput {
        FluidStack input;
        ILiquidFuel handler;

        public LiquidFuelWithInput(FluidStack input, ILiquidFuel handler) {
            this.input = input;
            this.handler = handler;
        }
    }

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
        registry.addRecipeCategories(new BoilerRecipeCategory(guiHelper));
        if(ConfigManager.isMysticalMechanicsIntegrationEnabled())
            registry.addRecipeCategories(new EngineRecipeCategory(guiHelper));
        registry.addRecipeCategories(new ReactionChamberCategory(guiHelper));
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
        reg.handleRecipes(LiquidFuelWithInput.class, recipe -> new BoilerRecipeWrapper(recipe.handler, recipe.input), BoilerRecipeCategory.UID);
        reg.handleRecipes(FluidReactionRecipe.class, ReactionChamberRecipeWrapper::new, ReactionChamberCategory.UID);

        reg.addRecipes(expandRecipes(RecipeRegistry.stampingRecipes),StampRecipeCategory.UID);
        reg.addRecipes(expandRecipes(RecipeRegistry.meltingRecipes),MelterRecipeCategory.UID);
        reg.addRecipes(RecipeRegistry.meltingRecipes.stream().filter(recipe -> recipe.getBonusOutput() != null).collect(Collectors.toList()), GeologicSeparatorRecipeCategory.UID);
        reg.addRecipes(expandRecipes(RecipeRegistry.mixingRecipes),MixingRecipeCategory.UID);
        reg.addRecipes(expandRecipes(RecipeRegistry.alchemyRecipes),AlchemyRecipeCategory.UID);
        reg.addRecipes(expandRecipes(RecipeRegistry.dawnstoneAnvilRecipes),DawnstoneAnvilCategory.UID);
        reg.addRecipes(expandLiquidFuels(EmbersAPIImpl.boilerLiquids),BoilerRecipeCategory.UID);
        reg.addRecipes(expandRecipes(RecipeRegistry.fluidReactionRecipes),ReactionChamberCategory.UID);

        reg.addRecipeCatalyst(new ItemStack(RegistryManager.stamper),StampRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(RegistryManager.block_furnace),MelterRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(RegistryManager.geo_separator),GeologicSeparatorRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(RegistryManager.block_furnace),GeologicSeparatorRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(RegistryManager.mixer),MixingRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(RegistryManager.alchemy_tablet),AlchemyRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(RegistryManager.dawnstone_anvil),DawnstoneAnvilCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(RegistryManager.mini_boiler),BoilerRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(RegistryManager.reaction_chamber),ReactionChamberCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(RegistryManager.heat_coil), VanillaRecipeCategoryUid.SMELTING);

        if(ConfigManager.isMysticalMechanicsIntegrationEnabled()) {
            reg.handleRecipes(LiquidFuelWithInput.class, recipe -> new EngineRecipeWrapper(recipe.handler, recipe.input), EngineRecipeCategory.UID);

            reg.addRecipes(expandLiquidFuels(EmbersAPIImpl.steamEngineFuels),EngineRecipeCategory.UID);

            reg.addRecipeCatalyst(new ItemStack(MysticalMechanicsIntegration.steam_engine),EngineRecipeCategory.UID);
        }
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

    private static List<LiquidFuelWithInput> expandLiquidFuels(List<ILiquidFuel> liquidFuels) {
        List<LiquidFuelWithInput> rList = new ArrayList<>();
        for (ILiquidFuel handler : liquidFuels) {
            for (FluidStack input : handler.getMatchingFluids()) {
                rList.add(new LiquidFuelWithInput(input, handler));
            }
        }
        return rList;
    }
}
