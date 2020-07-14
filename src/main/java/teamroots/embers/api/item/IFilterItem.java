package teamroots.embers.api.item;

import net.minecraft.item.ItemStack;

public interface IFilterItem {
    IFilter getFilter(ItemStack stack);
}
