package teamroots.embers.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import teamroots.embers.recipe.DawnstoneAnvilRecipe;
import teamroots.embers.recipe.RecipeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ZenRegister
@ZenClass(DawnstoneAnvil.CLASS)
public class DawnstoneAnvil {
    public static final String NAME = "DawnstoneAnvil";
    public static final String CLASS = "mods.embers.DawnstoneAnvil";

    @ZenMethod
    public static void add(IItemStack[] outputs, IIngredient bottom, IIngredient top) {
        DawnstoneAnvilRecipe recipe = new DawnstoneAnvilRecipe(CTUtil.toIngredient(bottom), CTUtil.toIngredient(top), CraftTweakerMC.getItemStacks(outputs));
        CraftTweakerAPI.apply(new Add(recipe));
    }

    @ZenMethod
    public static void remove(IItemStack bottom,IItemStack top) {
        CraftTweakerAPI.apply(new RemoveByInput(CraftTweakerMC.getItemStack(bottom),CraftTweakerMC.getItemStack(top)));
    }

    @ZenMethod
    public static void blacklistRepair(IIngredient match) {
        CraftTweakerAPI.apply(new BlacklistRepair(CTUtil.toIngredient(match)));
    }

    @ZenMethod
    public static void blacklistMateriaRepair(IIngredient match) {
        CraftTweakerAPI.apply(new BlacklistMateria(CTUtil.toIngredient(match)));
    }

    @ZenMethod
    public static void blacklistBreakdown(IIngredient match) {
        CraftTweakerAPI.apply(new BlacklistBreakdown(CTUtil.toIngredient(match)));
    }

    private static List<DawnstoneAnvilRecipe> getRecipesByInput(ItemStack bottom, ItemStack top) {
        return RecipeRegistry.dawnstoneAnvilRecipes.stream().filter(recipe -> recipe.bottom.apply(bottom) && recipe.top.apply(top)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static class Add implements IAction
    {
        DawnstoneAnvilRecipe recipe;

        public Add(DawnstoneAnvilRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void apply() {
            RecipeRegistry.dawnstoneAnvilRecipes.add(recipe);
        }

        @Override
        public String describe() {
            return String.format("Adding %s recipe: %s",NAME,recipe.toString());
        }
    }

    public static class RemoveByInput implements IAction
    {
        ItemStack bottom;
        ItemStack top;

        public RemoveByInput(ItemStack bottom, ItemStack top) {
            this.bottom = bottom;
            this.top = top;
        }

        @Override
        public void apply() {
            RecipeRegistry.dawnstoneAnvilRecipes.removeAll(getRecipesByInput(bottom,top));
        }

        @Override
        public String describe() {
            return String.format("Removing %s recipes with inputs: %s,%s",NAME,bottom,top);
        }
    }

    public static class BlacklistRepair implements IAction
    {
        Ingredient match;

        public BlacklistRepair(Ingredient match) {
            this.match = match;
        }

        @Override
        public void apply() {
            RecipeRegistry.dawnstoneRepairBlacklist.add(match);
        }

        @Override
        public String describe() {
            return String.format("Blacklisting %s from being repaired at Dawnstone Anvil.",match);
        }
    }

    public static class BlacklistMateria implements IAction
    {
        Ingredient match;

        public BlacklistMateria(Ingredient match) {
            this.match = match;
        }

        @Override
        public void apply() {
            RecipeRegistry.dawnstoneMateriaBlacklist.add(match);
        }

        @Override
        public String describe() {
            return String.format("Blacklisting %s from being repaired with Isolated Materia.",match);
        }
    }

    public static class BlacklistBreakdown implements IAction
    {
        Ingredient match;

        public BlacklistBreakdown(Ingredient match) {
            this.match = match;
        }

        @Override
        public void apply() {
            RecipeRegistry.dawnstoneBreakdownBlacklist.add(match);
        }

        @Override
        public String describe() {
            return String.format("Blacklisting %s from being broken down at Dawnstone Anvil.",match);
        }
    }
}
