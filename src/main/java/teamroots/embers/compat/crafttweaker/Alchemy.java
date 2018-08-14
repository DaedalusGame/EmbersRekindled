package teamroots.embers.compat.crafttweaker;

import com.google.common.collect.Lists;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.value.IntRange;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.alchemy.AspectList;
import teamroots.embers.recipe.AlchemyRecipe;
import teamroots.embers.recipe.RecipeRegistry;
import teamroots.embers.util.AlchemyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ZenRegister
@ZenClass(Alchemy.CLASS)
public class Alchemy {
    public static final String NAME = "Alchemy";
    public static final String CLASS = "mods.embers.Alchemy";

    @ZenMethod
    public static void addAspect(String name, IIngredient item) {
        CraftTweakerAPI.apply(new AddAspect(name, item));
    }

    @ZenMethod
    public static void add(IItemStack output, @NotNull IIngredient[] input, Map<String, IntRange> aspects) {
        AspectList minAspects = new AspectList();
        AspectList maxAspects = new AspectList();
        for (Map.Entry<String, IntRange> entry : aspects.entrySet()) {
            String aspect = entry.getKey();
            minAspects.addAspect(aspect, entry.getValue().getFrom());
            maxAspects.addAspect(aspect, entry.getValue().getTo());
        }
        AspectList.AspectRangeList aspectRange = new AspectList.AspectRangeList(minAspects, maxAspects);
        AlchemyRecipe recipe = new AlchemyRecipe(aspectRange,
                CTUtil.toIngredient(input[0]),
                Lists.newArrayList(CTUtil.toIngredient(input[1]), CTUtil.toIngredient(input[2]), CTUtil.toIngredient(input[3]), CTUtil.toIngredient(input[4])),
                CraftTweakerMC.getItemStack(output));
        CraftTweakerAPI.apply(new Add(recipe));
    }

    @ZenMethod
    public static void remove(IItemStack output) {
        CraftTweakerAPI.apply(new RemoveByOutput(CraftTweakerMC.getItemStack(output)));
    }

    private static List<AlchemyRecipe> getRecipesByOutput(ItemStack stack) {
        return RecipeRegistry.alchemyRecipes.stream().filter(recipe -> ItemStack.areItemsEqual(recipe.result, stack)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static class AddAspect implements IAction
    {
        String name;
        IIngredient item;

        public AddAspect(String name, IIngredient item) {
            this.name = name;
            this.item = item;
        }

        @Override
        public void apply() {
            EmbersAPI.registerAlchemyAspect(CTUtil.toIngredient(item),name);
        }

        @Override
        public String describe() {
            return "Adding custom aspect '"+name+"'";
        }
    }

    public static class Add implements IAction
    {
        AlchemyRecipe recipe;

        public Add(AlchemyRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void apply() {
            RecipeRegistry.alchemyRecipes.add(recipe);
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
            RecipeRegistry.alchemyRecipes.removeAll(getRecipesByOutput(output));
        }

        @Override
        public String describe() {
            return String.format("Removing %s recipes with output: %s",NAME,output.toString());
        }
    }
}
