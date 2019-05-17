package teamroots.embers.api.item;

import net.minecraft.item.ItemStack;

public interface IFilterItem {
    boolean acceptsItem(ItemStack filterStack, ItemStack stack);

    String formatFilter(ItemStack filterStack);
}
