package teamroots.embers.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;

public class TankClearingRecipe extends ShapelessOreRecipe {
    public TankClearingRecipe(ResourceLocation group, @Nonnull ItemStack tank) {
        super(group, NonNullList.from(Ingredient.EMPTY,Ingredient.fromStacks(tank)), tank);
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting grid) {
        for(int i = 0; i < grid.getSizeInventory(); i++) {
            ItemStack stack = grid.getStackInSlot(i).copy();
            stack.setCount(1);
            IFluidHandlerItem handler = FluidUtil.getFluidHandler(stack);
            if(handler != null && handler.drain(Integer.MAX_VALUE,false) != null) {
                handler.drain(Integer.MAX_VALUE,true);
                return handler.getContainer();
            }
        }
        return ItemStack.EMPTY;
    }
}
