package teamroots.embers.api.misc;

import net.minecraft.item.ItemStack;

public interface IFuel {
    boolean matches(ItemStack stack);

    double getFuelValue(ItemStack stack);
}
