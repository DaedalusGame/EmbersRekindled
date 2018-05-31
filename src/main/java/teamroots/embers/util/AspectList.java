package teamroots.embers.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import teamroots.embers.tileentity.TileEntityAlchemyPedestal;

import java.util.*;

//TODO: This class is clunky. Can we improve this?
public class AspectList {
    HashMap<String,Integer> aspectList = new HashMap<>();

    public AspectList() {
    }

    public AspectList(HashMap<String, Integer> map) {
        aspectList.putAll(map);
    }

    public void addAspect(String aspect, int amount)
    {
        aspectList.put(aspect,getAspect(aspect)+amount);
    }

    public void removeAspect(String aspect, int amount)
    {
        aspectList.put(aspect,Math.max(0,getAspect(aspect)-amount));
    }

    public void setAspect(String aspect, int amount)
    {
        aspectList.put(aspect,amount);
    }

    public int getAspect(String aspect)
    {
        return aspectList.getOrDefault(aspect,0);
    }

    public boolean hasAspect(String aspect) {
        return aspectList.containsKey(aspect);
    }

    public Collection<String> getAspects()
    {
        return aspectList.keySet();
    }

    public int getTotal()
    {
        int total = 0;
        for (int aspectAmt : aspectList.values())
            total += aspectAmt;
        return total;
    }

    public void reset()
    {
        aspectList.clear();
    }

    public void collect(List<TileEntityAlchemyPedestal> pedestals)
    {
        for (TileEntityAlchemyPedestal pedestal: pedestals) {
            ItemStack aspect = pedestal.inventory.getStackInSlot(1);
            ItemStack ash = pedestal.inventory.getStackInSlot(0);
            addAspect(AlchemyUtil.getAspect(aspect),ash.getCount());
        }
    }

    public void deserializeNBT(NBTTagCompound compound)
    {
        compound.getKeySet().stream().filter(key -> compound.hasKey(key, 99)).forEach(key -> aspectList.put(key, compound.getInteger(key)));
    }

    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        for (Map.Entry<String, Integer> entry : aspectList.entrySet()) {
            compound.setInteger(entry.getKey(),entry.getValue());
        }
        return compound;
    }

    public static AspectList createStandard(int iron, int dawnstone, int copper, int silver, int lead) {
        AspectList rList = new AspectList();
        rList.setAspect("iron",iron);
        rList.setAspect("dawnstone",dawnstone);
        rList.setAspect("copper",copper);
        rList.setAspect("silver",silver);
        rList.setAspect("lead",lead);
        return rList;
    }

    public static class AspectRangeList {
        AspectList minAspects;
        AspectList maxAspects;
        Random random = new Random();
        int seedOffset;
        boolean fixMath;

        public AspectList getMinAspects()
        {
            return minAspects;
        }

        public AspectList getMaxAspects()
        {
            return maxAspects;
        }

        public AspectRangeList() {
            minAspects = new AspectList();
            maxAspects = new AspectList();
        }

        public AspectRangeList(AspectList min, AspectList max)
        {
            minAspects = min;
            maxAspects = max;
        }

        public AspectRangeList setRange(String aspect, int min, int max) {
            minAspects.setAspect(aspect,min);
            maxAspects.setAspect(aspect,max);
            return this;
        }

        //This will fix one of the two 'problems' with Embers' alchemy system. Two recipes with the same ranges defined will require the same amount of ash.
        public AspectRangeList setSeedOffset(int offset)
        {
            seedOffset = offset;
            return this;
        }

        //This will fix the other of two 'problems' with Embers' alchemy system. If two aspects have the same delta, the generated range will be the same.
        //This means that if a recipe requires 24-48 iron and 12-36 dawnstone, the recipe will always require 24+n iron and 12+n dawnstone instead of 12+m dawnstone.
        public AspectRangeList fixMathematicalError()
        {
            fixMath = true;
            return this;
        }

        public int getMin(String aspect)
        {
            return minAspects.getAspect(aspect);
        }

        public int getMax(String aspect)
        {
            return maxAspects.getAspect(aspect);
        }

        public int getRange(String aspect)
        {
            return getMax(aspect) - getMin(aspect);
        }

        public int getExact(String aspect, World world)
        {
            random.setSeed(world.getSeed() + seedOffset + (fixMath ? aspect.hashCode() : 0));
            int min = minAspects.getAspect(aspect);
            int max = maxAspects.getAspect(aspect);
            int range = max - min;
            return min + random.nextInt(range+1);
        }
    }
}

