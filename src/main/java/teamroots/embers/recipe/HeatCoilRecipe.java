package teamroots.embers.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import teamroots.embers.util.IHasSize;

import java.util.List;

public class HeatCoilRecipe {
    Ingredient input = Ingredient.EMPTY;
    ItemStack output = ItemStack.EMPTY;

    public HeatCoilRecipe() {
    }

    public HeatCoilRecipe(ItemStack output, Ingredient input) {
        this.input = input;
        this.output = output;
    }

    public List<ItemStack> getInputs()
    {
        return Lists.newArrayList(input.getMatchingStacks());
    }

    public List<ItemStack> getOutputs()
    {
        return Lists.newArrayList(output);
    }

    public int getInputConsumed()
    {
        return input instanceof IHasSize ? ((IHasSize) input).getSize() : 1;
    }

    public boolean matches(ItemStack stack)
    {
        return input.apply(stack);
    }

    public ItemStack getResult(TileEntity tile, ItemStack stack)
    {
        return output.copy();
    }
}

