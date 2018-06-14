package teamroots.embers.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import teamroots.embers.recipe.HeatCoilRecipe;
import teamroots.embers.recipe.RecipeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ZenRegister
@ZenClass(HeatCoil.CLASS)
public class HeatCoil {
    public static final String NAME = "HeatCoil";
    public static final String CLASS = "mods.embers.HeatCoil";

    @ZenMethod
    public static void add(IItemStack output, IIngredient input) {
        HeatCoilRecipe recipe = new HeatCoilRecipe(CraftTweakerMC.getItemStack(output), CTUtil.toIngredient(input));
        CraftTweakerAPI.apply(new Add(recipe));
    }

    @ZenMethod
    public static void remove(IItemStack input) {
        CraftTweakerAPI.apply(new RemoveByInput(CraftTweakerMC.getItemStack(input)));
    }

    private static List<HeatCoilRecipe> getRecipesByInput(ItemStack stack) {
        return RecipeRegistry.heatCoilRecipes.stream().filter(recipe -> recipe.matches(stack)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static class Add implements IAction
    {
        HeatCoilRecipe recipe;

        public Add(HeatCoilRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void apply() {
            RecipeRegistry.heatCoilRecipes.add(recipe);
        }

        @Override
        public String describe() {
            return String.format("Adding %s recipe: %s",NAME,recipe.toString());
        }
    }

    public static class RemoveByInput implements IAction
    {
        ItemStack input;

        protected RemoveByInput(ItemStack input) {
            this.input = input;
        }

        @Override
        public void apply() {
            RecipeRegistry.heatCoilRecipes.removeAll(getRecipesByInput(input));
        }

        @Override
        public String describe() {
            return String.format("Removing %s recipes with input: %s",NAME, input.toString());
        }
    }
}
