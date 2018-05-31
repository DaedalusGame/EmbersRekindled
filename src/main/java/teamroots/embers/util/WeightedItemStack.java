package teamroots.embers.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

public class WeightedItemStack extends WeightedRandom.Item {
    ItemStack stack;

    public WeightedItemStack(ItemStack stack, int itemWeightIn) {
        super(itemWeightIn);
        this.stack = stack;
    }

    public ItemStack getStack() {
        return stack;
    }
}