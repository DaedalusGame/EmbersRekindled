package teamroots.embers.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public class AnvilRepairRecipe extends DawnstoneAnvilRecipe {
    @Override
    public boolean matches(ItemStack input1, ItemStack input2) {
        return !input1.isEmpty() && input1.getItem().getIsRepairable(input1,input2) && !RecipeRegistry.isBlacklistedFromRepair(input1);
    }

    @Override
    public List<ItemStack> getResult(TileEntity tile, ItemStack input1, ItemStack input2) {
        ItemStack result = input1.copy();
        result.setItemDamage(Math.max(0, result.getItemDamage() - result.getMaxDamage()));
        return Lists.newArrayList(result);
    }
}
