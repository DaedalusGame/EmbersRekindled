package teamroots.embers.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import teamroots.embers.recipe.ItemMeltingRecipe;
import teamroots.embers.recipe.RecipeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ZenRegister
@ZenClass(Melter.CLASS)
public class Melter {
    public static final String NAME = "Melter";
    public static final String CLASS = "mods.embers.Melter";

    @ZenMethod
    public static void add(ILiquidStack output, IIngredient input) {
        ItemMeltingRecipe recipe = new ItemMeltingRecipe(CTUtil.toIngredient(input), CraftTweakerMC.getLiquidStack(output));
        CraftTweaker.LATE_ACTIONS.add(new Add(recipe));
    }

    @ZenMethod
    public static void remove(IItemStack input)
    {
        CraftTweaker.LATE_ACTIONS.add(new RemoveByInput(CraftTweakerMC.getItemStack(input)));
    }

    @ZenMethod
    public static void remove(ILiquidStack output)
    {
        CraftTweaker.LATE_ACTIONS.add(new RemoveByOutput(CraftTweakerMC.getLiquidStack(output)));
    }

    private static List<ItemMeltingRecipe> getRecipesByInput(ItemStack stack)
    {
        return RecipeRegistry.meltingRecipes.stream().filter(recipe -> recipe.input.apply(stack)).collect(Collectors.toCollection(ArrayList::new));
    }

    private static List<ItemMeltingRecipe> getRecipesByOutput(FluidStack stack)
    {
        return RecipeRegistry.meltingRecipes.stream().filter(recipe -> stack.isFluidStackIdentical(recipe.getFluid())).collect(Collectors.toCollection(ArrayList::new));
    }

    public static class Add implements IAction
    {
        ItemMeltingRecipe recipe;

        public Add(ItemMeltingRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void apply() {
            RecipeRegistry.meltingRecipes.add(recipe);
        }

        @Override
        public String describe() {
            return String.format("Adding %s recipe: %s",NAME,recipe.toString());
        }
    }

    public static class RemoveByOutput implements IAction
    {
        FluidStack output;

        protected RemoveByOutput(FluidStack output) {
            this.output = output;
        }

        @Override
        public void apply() {
            RecipeRegistry.meltingRecipes.removeAll(getRecipesByOutput(output));
        }

        @Override
        public String describe() {
            return String.format("Removing %s recipes with output: %s",NAME,output.toString());
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
            RecipeRegistry.meltingRecipes.removeAll(getRecipesByInput(input));
        }

        @Override
        public String describe() {
            return String.format("Removing %s recipes with input: %s",NAME,input.toString());
        }
    }
}
