package teamroots.embers.recipe;

import com.google.common.collect.Lists;
import mezz.jei.api.recipe.IFocus;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import teamroots.embers.RegistryManager;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.util.IngredientSpecial;

import java.util.List;
import java.util.stream.Collectors;

public class AnvilAddModifierFakeRecipe extends DawnstoneAnvilRecipe implements IFocusRecipe {
    ItemStack modifier;

    public AnvilAddModifierFakeRecipe(ItemStack modifier) {
        super(new IngredientSpecial(stack -> ItemModUtil.getModifier(modifier).canApplyTo(stack)), Ingredient.fromStacks(modifier), new ItemStack[0]);
        this.modifier = modifier;
    }

    @Override
    public List<ItemStack> getBottomInputs() {
        return super.getBottomInputs().stream().map(this::prepareInput).collect(Collectors.toList());
    }


    @Override
    public List<ItemStack> getOutputs() {
        return getBottomInputs().stream().map(this::prepareOutput).collect(Collectors.toList());
    }

    private ItemStack prepareInput(ItemStack stack) {
        ItemStack input = stack.copy();
        if(!ItemModUtil.hasHeat(input))
            ItemModUtil.addModifier(input, new ItemStack(RegistryManager.ancient_motive_core));
        ItemModUtil.setLevel(input, ItemModUtil.getLevel(input)+1);
        return input;
    }

    private ItemStack prepareOutput(ItemStack stack) {
        ItemStack output = stack.copy();
        ItemModUtil.addModifier(output, modifier);
        return output;
    }

    @Override
    public List<ItemStack> getOutputs(IFocus<ItemStack> focus, int slot) {
        if (slot == 2) {
            if (focus.getValue().isItemEqual(modifier))
                return getOutputs();
            else
                return Lists.newArrayList(prepareOutput(prepareInput(focus.getValue())));
        }
        return Lists.newArrayList();
    }

    @Override
    public List<ItemStack> getInputs(IFocus<ItemStack> focus, int slot) {
        if (slot == 0)
            if (focus.getValue().isItemEqual(modifier))
                return getBottomInputs();
            else
                return Lists.newArrayList(prepareInput(focus.getValue()));
        if (slot == 1) return Lists.newArrayList(modifier);
        return Lists.newArrayList();
    }
}
