package teamroots.embers.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.util.IngredientSpecial;
import teamroots.embers.util.Misc;

import java.util.List;
import java.util.stream.Collectors;

public class AnvilBreakdownRecipe extends DawnstoneAnvilRecipe {
    public AnvilBreakdownRecipe() {
        super(new IngredientSpecial(stack -> stack.getItem().getIsRepairable(stack,Misc.getRepairItem(stack)) && Misc.getResourceCount(stack) != -1 && !RecipeRegistry.isBlacklistedFromBreakdown(stack)), Ingredient.EMPTY, new ItemStack[0]);
    }

    @Override
    public boolean matches(ItemStack input1, ItemStack input2) {
        ItemStack repairItem = Misc.getRepairItem(input1);
        return !input1.isEmpty() && !ItemModUtil.hasHeat(input1) && input1.getItem().getIsRepairable(input1,repairItem) && input2.isEmpty() && Misc.getResourceCount(input1) != -1 && !RecipeRegistry.isBlacklistedFromBreakdown(input1);
    }

    @Override
    public List<ItemStack> getOutputs() {
        return getBottomInputs().stream().map(stack -> {
            int resourceAmount = Misc.getResourceCount(stack);
            ItemStack repairItem = Misc.getRepairItem(stack);
            return new ItemStack(repairItem.getItem(),resourceAmount, repairItem.getItemDamage());
        }).collect(Collectors.toList());
    }

    @Override
    public List<ItemStack> getResult(TileEntity tile, ItemStack input1, ItemStack input2) {
        int resourceAmount = Misc.getResourceCount(input1);
        ItemStack repairItem = Misc.getRepairItem(input1);
        return Lists.newArrayList(new ItemStack(repairItem.getItem(),resourceAmount, repairItem.getItemDamage()));
    }
}
