package teamroots.embers.api.item;

import net.minecraft.item.ItemStack;
import teamroots.embers.api.filter.IFilter;

public interface IFilterItem {
    IFilter getFilter(ItemStack stack);
}
