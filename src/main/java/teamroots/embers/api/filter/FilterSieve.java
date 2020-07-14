package teamroots.embers.api.filter;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import teamroots.embers.util.FilterUtil;

import java.util.List;
import java.util.Objects;

public class FilterSieve implements IFilter {
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation("embers", "sieve");

    private ItemStack stack1;
    private ItemStack stack2;
    private int offset;
    private EnumFilterSetting setting;
    private boolean inverted;
    private IFilterComparator comparator;

    public FilterSieve(NBTTagCompound tag) {
        readFromNBT(tag);
    }

    public FilterSieve(ItemStack stack1, ItemStack stack2, int offset, EnumFilterSetting setting, boolean inverted) {
        this.stack1 = stack1;
        this.stack2 = stack2;
        this.offset = offset;
        this.setting = setting;
        this.inverted = inverted;
        findComparator();
    }

    private void findComparator() {
        if(stack1.isEmpty() && stack2.isEmpty())
            comparator = FilterUtil.ANY;
        else {
            List<IFilterComparator> comparators = FilterUtil.getComparators(stack1, stack2);
            comparator = comparators.get(offset % comparators.size());
        }
    }

    @Override
    public ResourceLocation getType() {
        return RESOURCE_LOCATION;
    }

    @Override
    public boolean acceptsItem(ItemStack stack) {
        return comparator.isBetween(stack1, stack2, stack, setting) != inverted;
    }

    @Override
    public String formatFilter() {
        if(comparator == null)
            return "INVALID COMPARATOR";
        return comparator.format(stack1,stack2,setting,inverted);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("type",getType().toString());
        tag.setTag("stack1",stack1.serializeNBT());
        tag.setTag("stack2",stack2.serializeNBT());
        tag.setInteger("offset",offset);
        tag.setInteger("setting",setting.ordinal());
        tag.setBoolean("inverted",inverted);
        tag.setString("comparator", comparator.getName());
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        stack1 = new ItemStack(tag.getCompoundTag("stack1"));
        stack2 = new ItemStack(tag.getCompoundTag("stack2"));
        offset = tag.getInteger("offset");
        setting = EnumFilterSetting.values()[tag.getInteger("setting")];
        inverted = tag.getBoolean("invert");
        if(tag.hasKey("comparator"))
            comparator = FilterUtil.getComparator(tag.getString("comparator"));
        else
            findComparator();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FilterSieve)
            return equals((FilterSieve) obj);
        return super.equals(obj);
    }

    private boolean equals(FilterSieve other) {
        return Objects.equals(comparator, other.comparator)
                && Objects.equals(inverted,other.inverted)
                && Objects.equals(setting, other.setting)
                && ItemStack.areItemStacksEqual(stack1, other.stack1)
                && ItemStack.areItemStacksEqual(stack2, other.stack2);
    }
}
