package teamroots.embers.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;

import java.util.List;
import java.util.stream.Collectors;

public class DawnstoneAnvilRecipe implements IWrappableRecipe {
    public Ingredient bottom = Ingredient.EMPTY;
    public Ingredient top = Ingredient.EMPTY;
    public List<ItemStack> result = Lists.newArrayList();

    public DawnstoneAnvilRecipe() {}

    public DawnstoneAnvilRecipe(Ingredient bottom, Ingredient top, ItemStack[] result) {
        this.result = Lists.newArrayList(result);
        this.bottom = bottom;
        this.top = top;
    }

    public List<ItemStack> getBottomInputs()
    {
        return Lists.newArrayList(bottom.getMatchingStacks());
    }

    public List<ItemStack> getTopInputs()
    {
        return Lists.newArrayList(top.getMatchingStacks());
    }

    public List<ItemStack> getOutputs() { return Lists.newArrayList(result); }

    public boolean matches(ItemStack input1, ItemStack input2)
    {
        return bottom.apply(input1) && (top == null || top.apply(input2));
    }

    public List<ItemStack> getResult(TileEntity tile, ItemStack input1, ItemStack input2) //For when you need your own handling
    {
        return result.stream().map(ItemStack::copy).collect(Collectors.toList());
    }

    @Override
    public List<IWrappableRecipe> getWrappers() {
        return Lists.newArrayList(this);
    }
}
