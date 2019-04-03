package teamroots.embers.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Predicate;

public class IngredientSpecial extends Ingredient {
    private static Set<IngredientSpecial> uncachedIngredients = Collections.newSetFromMap(new WeakHashMap<>());
    private ItemStack[] matchingStacks = new ItemStack[0];
    private boolean matchingStacksCached;
    private final Predicate<ItemStack> matcher;

    public IngredientSpecial(Predicate<ItemStack> matcher) {
        super(0);
        this.matcher = matcher;
        uncachedIngredients.add(this);
    }

    @Override
    public boolean apply(@Nullable ItemStack stack) {
        if (stack == null)
            stack = ItemStack.EMPTY;

        return matcher.test(stack);
    }

    @Override
    public ItemStack[] getMatchingStacks() {
        if (!matchingStacksCached)
            cacheMatchingStacks();
        return matchingStacks;
    }

    private static void cacheMatchingStacks() {
        //Update all ingredients at once, so we don't have to iterate the registry multiple times
        Map<IngredientSpecial, List<ItemStack>> matches = new HashMap<>();
        for (Item item : ForgeRegistries.ITEMS) {
            CreativeTabs[] tabs = item.getCreativeTabs();
            for (CreativeTabs tab : tabs) {
                if (tab == null)
                    continue;
                NonNullList<ItemStack> items = NonNullList.create();
                item.getSubItems(tab, items);
                for (IngredientSpecial ingredient : uncachedIngredients) {
                    items.stream().filter(ingredient.matcher).forEach(stack -> matches.computeIfAbsent(ingredient, ingredientSpecial -> new ArrayList<>()).add(stack));
                }
            }
        }
        for (Map.Entry<IngredientSpecial, List<ItemStack>> entry : matches.entrySet()) {
            entry.getKey().matchingStacks = entry.getValue() == null ? new ItemStack[0] : entry.getValue().toArray(new ItemStack[0]);
            entry.getKey().matchingStacksCached = true;
        }
        uncachedIngredients.clear();
    }

}