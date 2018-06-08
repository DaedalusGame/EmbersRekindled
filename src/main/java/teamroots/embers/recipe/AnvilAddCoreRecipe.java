package teamroots.embers.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import teamroots.embers.RegistryManager;
import teamroots.embers.itemmod.ModifierBase;
import teamroots.embers.util.ItemModUtil;

import java.util.List;

public class AnvilAddCoreRecipe extends DawnstoneAnvilRecipe {
    @Override
    public boolean matches(ItemStack input1, ItemStack input2) {
        ModifierBase modifier = ItemModUtil.modifierRegistry.get(RegistryManager.ancient_motive_core); //TODO: instead of hardcoding this, see if the modifier can be applied as a core
        return (!ItemModUtil.hasHeat(input1) || !ItemModUtil.hasModifier(input1, modifier.name)) && modifier.canApplyTo(input1) && input2.getItem() == RegistryManager.ancient_motive_core;
    }

    @Override
    public List<ItemStack> getResult(TileEntity tile, ItemStack input1, ItemStack input2) {
        ItemStack result = input1.copy();
        ItemModUtil.addModifier(result, input2);
        return Lists.newArrayList(result);
    }
}
