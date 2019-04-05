package teamroots.embers.recipe;

import com.google.common.collect.Lists;
import mezz.jei.api.recipe.IFocus;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.util.IngredientSpecial;
import teamroots.embers.util.Misc;

import java.util.List;
import java.util.stream.Collectors;

public class AnvilBreakdownRecipe extends DawnstoneAnvilRecipe implements IFocusRecipe {
    public AnvilBreakdownRecipe() {
        super(new IngredientSpecial(stack -> canBreakdown(stack)), Ingredient.EMPTY, new ItemStack[0]);
    }

    public static boolean canBreakdown(ItemStack stack) {
        ItemStack repairItem = Misc.getRepairItem(stack);
        return !repairItem.isEmpty() && stack.getItem().getIsRepairable(stack, Misc.getRepairItem(stack)) && Misc.getResourceCount(stack) != -1 && !RecipeRegistry.isBlacklistedFromBreakdown(stack);
    }

    @Override
    public boolean matches(ItemStack input1, ItemStack input2) {
        ItemStack repairItem = Misc.getRepairItem(input1);
        return !input1.isEmpty() && !repairItem.isEmpty() && !ItemModUtil.hasHeat(input1) && input1.getItem().getIsRepairable(input1,repairItem) && input2.isEmpty() && Misc.getResourceCount(input1) != -1 && !RecipeRegistry.isBlacklistedFromBreakdown(input1);
    }

    @Override
    public List<ItemStack> getOutputs() {
        return getBottomInputs().stream().map(this::getBreakResult).collect(Collectors.toList());
    }

    @Override
    public List<ItemStack> getResult(TileEntity tile, ItemStack input1, ItemStack input2) {
        return Lists.newArrayList(getBreakResult(input1));
    }

    @Override
    public List<ItemStack> getOutputs(IFocus<ItemStack> focus, int slot) {
        if(slot == 2) {
            if(focus.getMode() == IFocus.Mode.OUTPUT) {
                List<ItemStack> collect = getBottomInputs().stream().filter(x -> Misc.getRepairItem(x).isItemEqual(focus.getValue())).map(this::getBreakResult).collect(Collectors.toList());
                return Lists.newArrayList(collect);
            }
            else {
                return Lists.newArrayList(getBreakResult(focus.getValue()));
            }
        }
        return Lists.newArrayList();
    }

    @Override
    public List<ItemStack> getInputs(IFocus<ItemStack> focus, int slot) {
        if(slot == 0) {
            if(focus.getMode() == IFocus.Mode.INPUT) {
                return Lists.newArrayList(focus.getValue());
            }
            else {
                return getBottomInputs().stream().filter(x -> Misc.getRepairItem(x).isItemEqual(focus.getValue())).collect(Collectors.toList());
            }
        }
        return Lists.newArrayList();
    }

    private ItemStack getBreakResult(ItemStack x) {
        int resourceAmount = Misc.getResourceCount(x);
        ItemStack repairItem = Misc.getRepairItem(x);
        return new ItemStack(repairItem.getItem(),resourceAmount,repairItem.getMetadata());
    }
}
