package teamroots.embers.util;

import net.minecraft.item.ItemStack;

public abstract class ComparatorMatch implements IFilterComparator {
    private String name;
    private int priority;

    public ComparatorMatch(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Comparable getCompare(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean isBetween(ItemStack stack1, ItemStack stack2, ItemStack testStack, EnumFilterSetting setting) {
        return match(stack1,testStack);
    }
}
