package teamroots.embers.util;

import net.minecraft.item.ItemStack;

public abstract class ComparatorNormal implements IFilterComparator {
    private String name;
    private int priority;

    public ComparatorNormal(String name, int priority) {
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
}
