package teamroots.embers.recipe;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import teamroots.embers.RegistryManager;
import teamroots.embers.api.item.IInflictorGem;
import teamroots.embers.api.item.IInflictorGemHolder;
import teamroots.embers.item.ItemGolemEye;

public class GolemEyeFilterRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        ItemStack eye = ItemStack.EMPTY;
        ItemStack stack1 = ItemStack.EMPTY;
        ItemStack stack2 = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() instanceof ItemGolemEye) {
                if(!eye.isEmpty())
                    return false;
                eye = stack;
            } else if (stack1.isEmpty())
                stack1 = stack;
            else if (stack2.isEmpty())
                stack2 = stack;
            else if(!stack.isEmpty())
                return false;
        }
        return !eye.isEmpty() && !stack1.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack eye = ItemStack.EMPTY;
        ItemStack stack1 = ItemStack.EMPTY;
        ItemStack stack2 = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() instanceof ItemGolemEye)
                eye = stack;
            else if (stack1.isEmpty())
                stack1 = stack;
            else if (stack2.isEmpty())
                stack2 = stack;
        }
        eye = eye.copy();
        ItemGolemEye item = (ItemGolemEye) eye.getItem();
        item.reset(eye);
        if(stack2.isEmpty())
            item.setStacks(eye,stack1);
        else
            item.setStacks(eye,stack1,stack2);
        return eye;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(RegistryManager.golems_eye, 1);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() instanceof ItemGolemEye) {
                continue;
            }
            remaining.set(i,stack.copy());
        }
        return remaining;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 3;
    }

}
