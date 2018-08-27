package teamroots.embers.recipe;

import mezz.jei.api.recipe.IFocus;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IFocusRecipe {
    List<ItemStack> getOutputs(IFocus<ItemStack> focus, int slot);

    List<ItemStack> getInputs(IFocus<ItemStack> focus, int slot);
}
