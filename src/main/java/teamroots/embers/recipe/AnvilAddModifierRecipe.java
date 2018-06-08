package teamroots.embers.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import teamroots.embers.util.ItemModUtil;

import java.util.List;

public class AnvilAddModifierRecipe extends DawnstoneAnvilRecipe {
    @Override
    public boolean matches(ItemStack input1, ItemStack input2) {
        return ItemModUtil.hasHeat(input1) && ItemModUtil.modifierRegistry.containsKey(input2.getItem()) && ItemModUtil.getLevel(input1) > ItemModUtil.getTotalModLevel(input1) && ItemModUtil.isModValid(input1,input2);
    }

    @Override
    public List<ItemStack> getResult(TileEntity tile, ItemStack input1, ItemStack input2) {
        ItemStack result = input1.copy();
        ItemModUtil.addModifier(result, input2);
        return Lists.newArrayList(result);
    }
}
