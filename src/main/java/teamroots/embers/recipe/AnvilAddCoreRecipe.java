package teamroots.embers.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import teamroots.embers.RegistryManager;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.api.itemmod.ModifierBase;
import teamroots.embers.util.IngredientSpecial;

import java.util.List;
import java.util.stream.Collectors;

public class AnvilAddCoreRecipe extends DawnstoneAnvilRecipe {
    public AnvilAddCoreRecipe() {
        super(new IngredientSpecial(teamroots.embers.util.ItemModUtil::canAnyModifierApply), Ingredient.fromItem(RegistryManager.ancient_motive_core), new ItemStack[0]);
    }

    @Override
    public List<ItemStack> getOutputs() {
        return getBottomInputs().stream().map(stack -> {
            ItemStack output = stack.copy();
            ItemModUtil.addModifier(output, new ItemStack(RegistryManager.ancient_motive_core));
            return output;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean matches(ItemStack input1, ItemStack input2) {
        ModifierBase modifier = ItemModUtil.getModifier(input2); //TODO: instead of hardcoding this, see if the modifier can be applied as a core
        return input2.getItem() == RegistryManager.ancient_motive_core && (!ItemModUtil.hasHeat(input1) || !ItemModUtil.hasModifier(input1, modifier)) && modifier.canApplyTo(input1);
    }

    @Override
    public List<ItemStack> getResult(TileEntity tile, ItemStack input1, ItemStack input2) {
        ItemStack result = input1.copy();
        ItemModUtil.addModifier(result, input2);
        return Lists.newArrayList(result);
    }
}
