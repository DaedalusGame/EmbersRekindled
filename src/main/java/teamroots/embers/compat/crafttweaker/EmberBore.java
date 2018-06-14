package teamroots.embers.compat.crafttweaker;

import com.google.common.collect.Sets;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.item.WeightedItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;
import teamroots.embers.recipe.BoreOutput;
import teamroots.embers.recipe.RecipeRegistry;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

@ZenRegister
@ZenClass(EmberBore.CLASS)
public class EmberBore {
    public static final String CLASS = "mods.embers.EmberBore";

    BoreOutput internal;

    @ZenGetter("dimensions")
    public Integer[] getDimensions() {
        return internal.dimensionIds.toArray(new Integer[internal.dimensionIds.size()]);
    }

    @ZenSetter("dimensions")
    public void setDimensions(Integer[] dimensions) {
        internal.dimensionIds = Sets.newHashSet(dimensions);
    }

    @ZenGetter("biomes")
    public String[] getBiomes() {
        return (String[]) internal.biomeIds.stream().map(ResourceLocation::toString).toArray();
    }

    @ZenSetter("biomes")
    public void setBiomes(String[] biomes) {
        internal.biomeIds = Arrays.stream(biomes).map(ResourceLocation::new).collect(Collectors.toCollection(HashSet<ResourceLocation>::new));
    }

    @ZenGetter("stacks")
    public WeightedItemStack[] getStacks() {
        return (WeightedItemStack[]) internal.stacks.stream().map(stack -> new WeightedItemStack(CraftTweakerMC.getIItemStack(stack.getStack()),stack.itemWeight)).toArray();
    }

    @ZenMethod
    public void addOutput(WeightedItemStack stack) {
        internal.stacks.add(new teamroots.embers.util.WeightedItemStack(CraftTweakerMC.getItemStack(stack.getStack()),(int)stack.getChance()));
    }

    @ZenMethod
    public void removeOutput(IItemStack stack) {
        ItemStack toRemove = CraftTweakerMC.getItemStack(stack);
        internal.stacks.removeIf(x -> x.getStack().isItemEqual(toRemove));
    }

    @ZenMethod
    public void clear() {
        internal.stacks.clear();
    }

    public EmberBore(BoreOutput internal) {
        this.internal = internal;
    }

    @ZenMethod
    public static EmberBore create(Integer[] dimensions, String[] biomes) {
        EmberBore emberBore = new EmberBore(new BoreOutput());
        emberBore.setDimensions(dimensions);
        emberBore.setBiomes(biomes);
        return emberBore;
    }

    @ZenMethod
    public static EmberBore getDefault() {
        return new EmberBore(RecipeRegistry.defaultBoreOutput);
    }
}