package teamroots.embers.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import teamroots.embers.RegistryManager;
import teamroots.embers.item.ItemGolemEye;

public class GolemEyeOffsetRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        ItemStack eye = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() instanceof ItemGolemEye && eye.isEmpty())
                eye = stack;
            else if(!stack.isEmpty())
                return false;
        }
        return !eye.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack eye = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() instanceof ItemGolemEye)
                eye = stack;
        }
        eye = eye.copy();
        eye.setCount(1);
        ItemGolemEye item = (ItemGolemEye) eye.getItem();
        item.incrementOffset(eye);
        return eye;
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
        return width * height >= 3;
    }

}
