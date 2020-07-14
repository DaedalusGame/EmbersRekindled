package teamroots.embers.api.filter;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class FilterItem implements IFilter {
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation("embers", "item");

    private ItemStack filterItem = ItemStack.EMPTY;

    public FilterItem(ItemStack filterItem) {
        this.filterItem = filterItem;
    }

    public FilterItem(NBTTagCompound tag) {
        readFromNBT(tag);
    }

    @Override
    public ResourceLocation getType() {
        return RESOURCE_LOCATION;
    }

    @Override
    public boolean acceptsItem(ItemStack stack) {
        return filterItem.getItem() == stack.getItem() && filterItem.getItemDamage() == stack.getItemDamage();
    }

    @Override
    public String formatFilter() {
        return I18n.format("embers.filter.strict", filterItem.getDisplayName());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("type",getType().toString());
        tag.setTag("filterStack",filterItem.serializeNBT());
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        filterItem = new ItemStack(tag.getCompoundTag("filterStack"));
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FilterItem)
            return equals((FilterItem) obj);
        return super.equals(obj);
    }

    private boolean equals(FilterItem other) {
        return ItemStack.areItemStacksEqual(filterItem, other.filterItem);
    }
}
