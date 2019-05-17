package teamroots.embers.util;

import net.minecraft.item.ItemStack;

public interface IFilterComparator {
    int getPriority();

    String getName();

    boolean match(ItemStack stack1, ItemStack stack2);

    Comparable getCompare(ItemStack stack);

    default boolean isBetween(ItemStack stack1, ItemStack stack2, ItemStack testStack, EnumFilterSetting setting) {
        if(!match(stack1,testStack))
            return false;

        Comparable a = getCompare(stack1);
        Comparable b = getCompare(stack2);
        Comparable test = getCompare(testStack);

        if(setting == EnumFilterSetting.FUZZY && a.compareTo(b) == 0)
            return true;
        else if(a.compareTo(test) <= 0 && b.compareTo(test) >= 0)
            return true;
        else if(a.compareTo(test) >= 0 && b.compareTo(test) <= 0)
            return true;
        else
            return false;
    }

    String format(ItemStack stack1, ItemStack stack2, EnumFilterSetting setting, boolean inverted);
}
