package teamroots.embers.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import teamroots.embers.util.Misc;

import java.util.List;

public class AnvilBreakdownRecipe extends DawnstoneAnvilRecipe {
    @Override
    public boolean matches(ItemStack input1, ItemStack input2) {
        ItemStack repairItem = Misc.getRepairItem(input1);
        return !input1.isEmpty() && input1.getItem().getIsRepairable(input1,repairItem) && input2.isEmpty() && Misc.getResourceCount(input1) != -1 && !RecipeRegistry.isBlacklistedFromBreakdown(input1);
    }

    @Override
    public List<ItemStack> getResult(TileEntity tile, ItemStack input1, ItemStack input2) {
        int resourceAmount = Misc.getResourceCount(input1);
        ItemStack repairItem = Misc.getRepairItem(input1);
        return Lists.newArrayList(new ItemStack(repairItem.getItem(),resourceAmount, repairItem.getItemDamage()));
    }
}
