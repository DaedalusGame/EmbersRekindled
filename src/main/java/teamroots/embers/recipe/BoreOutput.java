package teamroots.embers.recipe;

import net.minecraft.util.ResourceLocation;
import teamroots.embers.util.WeightedItemStack;

import java.util.ArrayList;
import java.util.HashSet;

public class BoreOutput {
    public HashSet<Integer> dimensionIds;
    public HashSet<ResourceLocation> biomeIds;
    public ArrayList<WeightedItemStack> stacks = new ArrayList<>();

    public BoreOutput() {
        this(new HashSet<>(), new HashSet<>(), new ArrayList<>());
    }

    public BoreOutput(HashSet<Integer> dimensionIds, HashSet<ResourceLocation> biomeIds, ArrayList<WeightedItemStack> stacks) {
        this.dimensionIds = dimensionIds;
        this.biomeIds = biomeIds;
        this.stacks = stacks;
    }
}