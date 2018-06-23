package teamroots.embers.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import teamroots.embers.RegistryManager;
import teamroots.embers.util.IngredientSpecial;

import java.util.List;
import java.util.stream.Collectors;

public class AnvilRepairMateriaRecipe extends DawnstoneAnvilRecipe {
    public AnvilRepairMateriaRecipe() {
        super(new IngredientSpecial(stack -> stack.getItem().isRepairable() && !RecipeRegistry.isBlacklistedFromMateriaRepair(stack)), Ingredient.fromItem(RegistryManager.isolated_materia), new ItemStack[0]);
    }

    @Override
    public List<ItemStack> getBottomInputs() {
        return super.getBottomInputs().stream().map(stack -> {
            ItemStack input = stack.copy();
            input.setItemDamage(stack.getMaxDamage() / 2);
            return input;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ItemStack> getOutputs() {
        return getBottomInputs().stream().map(stack -> {
            ItemStack output = stack.copy();
            output.setItemDamage(Math.max(0, output.getItemDamage() - output.getMaxDamage()));
            return output;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean matches(ItemStack input1, ItemStack input2) {
        return !input1.isEmpty() && input1.getItem().isRepairable() && input2.getItem() == RegistryManager.isolated_materia && !RecipeRegistry.isBlacklistedFromMateriaRepair(input1);
    }

    @Override
    public List<ItemStack> getResult(TileEntity tile, ItemStack input1, ItemStack input2) {
        ItemStack result = input1.copy();
        result.setItemDamage(Math.max(0, result.getItemDamage() - result.getMaxDamage()));
        return Lists.newArrayList(result);
    }
}
