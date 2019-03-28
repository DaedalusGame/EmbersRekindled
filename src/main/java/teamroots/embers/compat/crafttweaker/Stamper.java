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
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import teamroots.embers.recipe.ItemStampingRecipe;
import teamroots.embers.recipe.RecipeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ZenRegister
@ZenClass(Stamper.CLASS)
public class Stamper {
    public static final String NAME = "Stamper";
    public static final String CLASS = "mods.embers.Stamper";

    @ZenMethod
    public static void add(IItemStack output, ILiquidStack liquid, @NotNull IIngredient stamp, @Optional IIngredient input) {
        ItemStampingRecipe recipe = new ItemStampingRecipe(CTUtil.toIngredient(input), CraftTweakerMC.getLiquidStack(liquid),CTUtil.toIngredient(stamp),CraftTweakerMC.getItemStack(output));
        CraftTweaker.LATE_ACTIONS.add(new Add(recipe));
    }

    @ZenMethod
    public static void remove(IItemStack output) {
        CraftTweaker.LATE_ACTIONS.add(new RemoveByOutput(CraftTweakerMC.getItemStack(output)));
    }

    @ZenMethod
    public static void removeAll() {
        CraftTweaker.LATE_ACTIONS.add(new RemoveAll());
    }

    private static List<ItemStampingRecipe> getRecipesByOutput(ItemStack stack)
    {
        return RecipeRegistry.stampingRecipes.stream().filter(recipe -> ItemStack.areItemStacksEqual(stack,recipe.result)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static class Add implements IAction
    {
        ItemStampingRecipe recipe;

        public Add(ItemStampingRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void apply() {
            RecipeRegistry.stampingRecipes.add(recipe);
        }

        @Override
        public String describe() {
            return String.format("Adding %s recipe: %s",NAME,recipe.toString());
        }
    }

    public static class RemoveByOutput implements IAction
    {
        ItemStack output;

        protected RemoveByOutput(ItemStack output) {
            this.output = output;
        }

        @Override
        public void apply() {
            RecipeRegistry.stampingRecipes.removeAll(getRecipesByOutput(output));
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
            RecipeRegistry.stampingRecipes.clear();
        }

        @Override
        public String describe() {
            return String.format("Removing all %s recipes",NAME);
        }
    }
}