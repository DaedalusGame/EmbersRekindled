package teamroots.embers.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.util.*;

public class FilterUtil {
    static List<IFilterComparator> comparators = new ArrayList<>();
    static Map<String, IFilterComparator> comparatorMap = new HashMap<>();

    static IFilterComparator ANY = new ComparatorMatch("any",999999) {
        @Override
        public boolean match(ItemStack stack1, ItemStack stack2) {
            return true;
        }

        @Override
        public String format(ItemStack stack1, ItemStack stack2, EnumFilterSetting setting, boolean inverted) {
            return I18n.format("embers.filter.any");
        }
    };

    public static void registerComparator(IFilterComparator comparator) {
        comparatorMap.put(comparator.getName(), comparator);
        comparators.add(comparator);
        comparators.sort(Comparator.comparingInt(IFilterComparator::getPriority).reversed());
    }

    public static List<IFilterComparator> getComparators(ItemStack stack1, ItemStack stack2) {
        ArrayList<IFilterComparator> matched = new ArrayList<>();
        for (IFilterComparator comparator : comparators) {
            if (comparator.match(stack1, stack2))
                matched.add(comparator);
        }
        if(matched.isEmpty())
            matched.add(ANY);
        return matched;
    }

    public static IFilterComparator getComparator(String name) {
        return comparatorMap.get(name);
    }
}
