package teamroots.embers.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;
import teamroots.embers.RegistryManager;
import teamroots.embers.item.ItemGolemEye;
import teamroots.embers.util.ItemUtil;
import teamroots.embers.util.MatchUtil;

public class GolemEyeMergeRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        ItemStack eye1 = ItemStack.EMPTY;
        ItemStack eye2 = ItemStack.EMPTY;
        int slime = 0;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() instanceof ItemGolemEye) {
                if (eye1.isEmpty())
                    eye1 = stack;
                else if (eye2.isEmpty())
                    eye2 = stack;
                else
                    return false;
            }
            else if(ItemUtil.matchesOreDict(stack,"slimeball"))
                slime++;
            else if(!stack.isEmpty())
                return false;
        }
        return !eye1.isEmpty() && !eye2.isEmpty() && slime <= 1;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack eye1 = ItemStack.EMPTY;
        ItemStack eye2 = ItemStack.EMPTY;
        int slime = 0;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() instanceof ItemGolemEye) {
                if (eye1.isEmpty())
                    eye1 = stack;
                else if (eye2.isEmpty())
                    eye2 = stack;
            }
            else if(ItemUtil.matchesOreDict(stack,"slimeball"))
                slime++;
        }
        ItemStack result;
        ItemGolemEye item = (ItemGolemEye) eye1.getItem();
        if(slime > 0) {
            result = new ItemStack(RegistryManager.golems_eye);
            item.setStacks(result,eye1,eye2);
        } else {
            result = eye1.copy();
            result.setCount(2);
        }
        return result;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(RegistryManager.golems_eye, 1);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

}
