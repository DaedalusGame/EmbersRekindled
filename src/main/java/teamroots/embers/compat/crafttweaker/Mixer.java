package teamroots.embers.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import teamroots.embers.recipe.FluidMixingRecipe;
import teamroots.embers.recipe.RecipeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ZenRegister
@ZenClass(Mixer.CLASS)
public class Mixer {
    public static final String NAME = "Mixer";
    public static final String CLASS = "mods.embers.Mixer";

    @ZenMethod
    public static void add(ILiquidStack output, @NotNull ILiquidStack[] inputs, @Optional(valueDouble = 0.5) double powerRatio) {
        FluidMixingRecipe recipe = new FluidMixingRecipe(CraftTweakerMC.getLiquidStacks(inputs), CraftTweakerMC.getLiquidStack(output), powerRatio);
        CraftTweakerAPI.apply(new Add(recipe));
    }

    @ZenMethod
    public static void remove(ILiquidStack output)
    {
        CraftTweakerAPI.apply(new RemoveByOutput(CraftTweakerMC.getLiquidStack(output)));
    }

    @ZenMethod
    public static void removeAll() {
        CraftTweakerAPI.apply(new RemoveAll());
    }

    private static List<FluidMixingRecipe> getRecipesByOutput(FluidStack stack)
    {
        return RecipeRegistry.mixingRecipes.stream().filter(recipe -> recipe.output.isFluidStackIdentical(stack)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static class Add implements IAction
    {
        FluidMixingRecipe recipe;

        public Add(FluidMixingRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void apply() {
            RecipeRegistry.mixingRecipes.add(recipe);
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
            RecipeRegistry.mixingRecipes.removeAll(getRecipesByOutput(output));
        }

        @Override
        public String describe() {
            return String.format("Removing %s recipes with output: %s",NAME,output.toString());
        }
    }

    public static class RemoveAll implements IAction
    {
        protected RemoveAll() {

        }

        @Override
        public void apply() {
            RecipeRegistry.mixingRecipes.clear();
        }

        @Override
        public String describe() {
            return String.format("Removing all %s recipes",NAME);
        }
    }
}
