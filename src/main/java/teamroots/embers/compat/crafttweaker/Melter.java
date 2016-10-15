package teamroots.embers.compat.crafttweaker;

import java.util.List;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import teamroots.embers.recipe.ItemMeltingRecipe;
import teamroots.embers.recipe.RecipeRegistry;

@ZenClass("mods.embers.melter")
public class Melter {
/*
    private final static List<ItemMeltingRecipe> recipes = RecipeRegistry.getMeltingRecipeList();
    @ZenMethod
    public static void add(IItemStack output, IItemStack secondary, IIngredient[] inputs) {
        MineTweakerAPI.apply((IUndoableAction) new Add(toStack(output),toStack(secondary),toInputs(inputs)));
    }

    @ZenMethod
    public static void remove(IItemStack output) {
        List<BulkRecipe> toRemove = new ArrayList<>();
        for(BulkRecipe recipe: recipes) {
            if(StackHelper.matches(output,toIItemStack(recipe.getOutput()))) {
                toRemove.add(recipe);
            }
        }
        if(!toRemove.isEmpty()) {
            MineTweakerAPI.apply(new Remove(toRemove));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", "mill", output.toString()));
        }
    }

    public static class AddRecipe extends BaseListAddition<ItemMeltingRecipe> {

        protected Add(ItemStack output, ItemStack secondary, Object[] inputs) {
            super("mill", Mill.recipes);
            recipes.add(new BulkRecipe(this.name,output,secondary,inputs));
        }

        @Override
        protected String getRecipeInfo(BulkRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getOutput());
        }
    }
    
    public static class Remove extends BaseListRemoval<ItemMeltingRecipe> {

        protected Remove(List<BulkRecipe> list) {
            super("mill", Mill.recipes, list);
        }

        @Override
        protected String getRecipeInfo(BulkRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getOutput());
        }
    }*/
}
