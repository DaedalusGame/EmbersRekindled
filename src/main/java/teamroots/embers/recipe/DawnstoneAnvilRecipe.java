package teamroots.embers.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;

import java.util.List;
import java.util.stream.Collectors;

public class DawnstoneAnvilRecipe {
    public Ingredient bottom;
    public Ingredient top;
    public List<ItemStack> result;

    public DawnstoneAnvilRecipe() {}

    public DawnstoneAnvilRecipe(Ingredient bottom, Ingredient top, ItemStack[] result) {
        this.result = Lists.newArrayList(result);
        this.bottom = bottom;
        this.top = top;
    }

    public boolean matches(ItemStack input1, ItemStack input2)
    {
        return bottom.apply(input1) && (top == null || top.apply(input2));
    }

    public List<ItemStack> getResult(TileEntity tile, ItemStack input1, ItemStack input2) //For when you need your own handling
    {
        return result.stream().map(ItemStack::copy).collect(Collectors.toList());
    }
}
