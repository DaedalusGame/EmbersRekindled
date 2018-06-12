package teamroots.embers.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
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

@ZenClass(Stamper.CLASS)
public class Stamper {
    public static final String NAME = "Stamper";
    public static final String CLASS = "mods.embers.Stamper";

    @ZenMethod
    public static void add(IItemStack output, ILiquidStack liquid, @NotNull IIngredient stamp, @Optional IIngredient input) {
        ItemStampingRecipe recipe = new ItemStampingRecipe(CTUtil.toIngredient(input), CraftTweakerMC.getLiquidStack(liquid),CTUtil.toIngredient(stamp),CraftTweakerMC.getItemStack(output));
        CraftTweakerAPI.apply(new Add(recipe));
    }

    @ZenMethod
    public static void remove(IItemStack output)
    {
        CraftTweakerAPI.apply(new Remove(CraftTweakerMC.getItemStack(output)));
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

    public static class Remove implements IAction
    {
        ItemStack output;

        protected Remove(ItemStack output) {
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
}