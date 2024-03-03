package teamroots.embers.api.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import teamroots.embers.api.filter.IFilter;

public interface IFilterItem {
    IFilter getFilter(ItemStack stack);

	default void setFilter(ItemStack stack, IFilter filter) {
        NBTTagCompound compound = stack.getOrCreateSubCompound("filter");
        filter.writeToNBT(compound);
	}
}
