package teamroots.embers.api.misc;

import net.minecraft.item.ItemStack;

public interface ICoefficientFuel {
    boolean matches(ItemStack stack);

    double getCoefficient(ItemStack stack);

    int getDuration(ItemStack stack);
}
