package teamroots.embers.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import teamroots.embers.util.Misc;

import java.util.ArrayList;
import java.util.List;

public class AnvilRepairRecipe extends DawnstoneAnvilRecipe {
    @Override
    public boolean matches(ItemStack input1, ItemStack input2) {
        return !input1.isEmpty() && input1.getItem().getIsRepairable(input1,input2) && !RecipeRegistry.isBlacklistedFromRepair(input1);
    }

    @Override
    public List<ItemStack> getResult(TileEntity tile, ItemStack input1, ItemStack input2) {
        ItemStack result = input1.copy();
        result.setItemDamage(Math.max(0, result.getItemDamage() - result.getMaxDamage()));
        return Lists.newArrayList(result);
    }

    @Override
    public List<IWrappableRecipe> getWrappers() {
        ArrayList<IWrappableRecipe> recipes = Lists.newArrayList();
        for(Item item : Item.REGISTRY) {
            ItemStack stack = item.getDefaultInstance();
            ItemStack repairStack = Misc.getRepairItem(stack);
            if(!repairStack.isEmpty() && item.getIsRepairable(stack,repairStack) && !RecipeRegistry.isBlacklistedFromRepair(stack)) {
                recipes.add(new AnvilRepairFakeRecipe(stack));
            }
        }
        return recipes;
    }
}
