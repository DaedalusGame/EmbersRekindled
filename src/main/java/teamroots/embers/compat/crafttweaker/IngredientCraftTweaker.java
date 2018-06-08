package teamroots.embers.compat.crafttweaker;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import teamroots.embers.util.IHasSize;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IngredientCraftTweaker extends Ingredient implements IHasSize {
    IIngredient predicate;

    public IngredientCraftTweaker(IIngredient ingredient)
    {
        predicate = ingredient;
    }

    @Override
    public ItemStack[] getMatchingStacks() {
        List<IItemStack> stacks = predicate != null ? predicate.getItems() : new ArrayList<>();
        return stacks.stream().map(CraftTweakerMC::getItemStack).toArray(ItemStack[]::new);
    }

    @Override
    public boolean apply(@Nullable ItemStack stack) {
        if(predicate == null)
            return stack.isEmpty();
        return predicate.matches(CraftTweakerMC.getIItemStack(stack));
    }

    @Override
    public int getSize() {
        return predicate.getAmount();
    }
}
