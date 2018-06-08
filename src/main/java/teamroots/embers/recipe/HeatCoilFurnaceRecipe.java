package teamroots.embers.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;

public class HeatCoilFurnaceRecipe extends HeatCoilRecipe {
    @Override
    public boolean matches(ItemStack stack) {
        return !FurnaceRecipes.instance().getSmeltingResult(stack).isEmpty();
    }

    @Override
    public ItemStack getResult(TileEntity tile, ItemStack stack) {
        return FurnaceRecipes.instance().getSmeltingResult(stack).copy();
    }
}
