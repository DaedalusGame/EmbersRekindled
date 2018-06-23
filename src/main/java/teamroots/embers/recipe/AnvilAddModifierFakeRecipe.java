package teamroots.embers.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import teamroots.embers.RegistryManager;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.util.IngredientSpecial;

import java.util.List;
import java.util.stream.Collectors;

public class AnvilAddModifierFakeRecipe extends DawnstoneAnvilRecipe {
    ItemStack modifier;

    public AnvilAddModifierFakeRecipe(ItemStack modifier) {
        super(new IngredientSpecial(stack -> ItemModUtil.getModifier(modifier).canApplyTo(stack)), Ingredient.fromStacks(modifier), new ItemStack[0]);
        this.modifier = modifier;
    }

    @Override
    public List<ItemStack> getBottomInputs() {
        return super.getBottomInputs().stream().map(stack -> {
            ItemStack input = stack.copy();
            ItemModUtil.addModifier(input,new ItemStack(RegistryManager.ancient_motive_core));
            ItemModUtil.setLevel(input,1);
            return input;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ItemStack> getOutputs() {
        return getBottomInputs().stream().map(stack -> {
            ItemStack output = stack.copy();
            ItemModUtil.addModifier(output,modifier);
            return output;
        }).collect(Collectors.toList());
    }
}
