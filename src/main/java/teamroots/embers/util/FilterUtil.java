package teamroots.embers.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import teamroots.embers.api.filter.ComparatorMatch;
import teamroots.embers.api.filter.EnumFilterSetting;
import teamroots.embers.api.filter.IFilterComparator;
import teamroots.embers.api.filter.FilterAny;
import teamroots.embers.api.filter.FilterExisting;
import teamroots.embers.api.filter.FilterNotExisting;
import teamroots.embers.api.filter.IFilter;

import java.util.*;
import java.util.function.Function;

public class FilterUtil {
    static List<IFilterComparator> comparators = new ArrayList<>();
    static Map<String, IFilterComparator> comparatorMap = new HashMap<>();
    static Map<ResourceLocation, Function<NBTTagCompound, IFilter>> filterRegistry = new LinkedHashMap<>();

    public static IFilterComparator ANY = new ComparatorMatch("any",999999) {
        @Override
        public boolean match(ItemStack stack1, ItemStack stack2) {
            return true;
        }

        @Override
        public String format(ItemStack stack1, ItemStack stack2, EnumFilterSetting setting, boolean inverted) {
            return I18n.format("embers.filter.any");
        }
    };

    public static IFilter FILTER_ANY = new FilterAny();
    public static IFilter FILTER_EXISTING = new FilterExisting();
    public static IFilter FILTER_NOT_EXISTING = new FilterNotExisting();

    public static void registerComparator(IFilterComparator comparator) {
        comparatorMap.put(comparator.getName(), comparator);
        comparators.add(comparator);
        comparators.sort(Comparator.comparingInt(IFilterComparator::getPriority).reversed());
    }

    public static void registerFilter(ResourceLocation resLoc, Function<NBTTagCompound, IFilter> generator) {
        filterRegistry.put(resLoc, generator);
    }

    public static void registerFilter(IFilter filter) {
        registerFilter(filter.getType(), tag -> filter);
    }

    public static IFilter deserializeFilter(NBTTagCompound tag) {
        ResourceLocation resLoc = new ResourceLocation(tag.getString("type"));
        Function<NBTTagCompound, IFilter> generator = filterRegistry.get(resLoc);
        if(generator != null)
            return generator.apply(tag);
        return FILTER_ANY;
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
