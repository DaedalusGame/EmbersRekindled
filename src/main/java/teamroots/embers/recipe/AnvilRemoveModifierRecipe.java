package teamroots.embers.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import teamroots.embers.api.itemmod.ItemModUtil;

import java.util.ArrayList;
import java.util.List;

public class AnvilRemoveModifierRecipe extends DawnstoneAnvilRecipe {
    @Override
    public boolean matches(ItemStack input1, ItemStack input2) {
        return ItemModUtil.hasHeat(input1) && ItemModUtil.getTotalModifierLevel(input1) > 1 && input2.isEmpty();
    }

    @Override
    public List<ItemStack> getResult(TileEntity tile, ItemStack input1, ItemStack input2) {
        List<ItemStack> results = new ArrayList<>();
        ItemStack result = input1.copy();
        results.add(result);
        results.addAll(ItemModUtil.removeAllModifiers(result));
        return results;
    }

    @Override
    public List<IWrappableRecipe> getWrappers() {
        return Lists.newArrayList();
    }
}
