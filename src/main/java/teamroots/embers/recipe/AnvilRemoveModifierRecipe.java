package teamroots.embers.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import teamroots.embers.util.ItemModUtil;

import java.util.ArrayList;
import java.util.List;

public class AnvilRemoveModifierRecipe extends DawnstoneAnvilRecipe {
    @Override
    public boolean matches(ItemStack input1, ItemStack input2) {
        return ItemModUtil.hasHeat(input1) && input1.getTagCompound().getCompoundTag(ItemModUtil.HEAT_TAG).getTagList("modifiers", Constants.NBT.TAG_COMPOUND).tagCount() > 0 && input2.isEmpty();
    }

    @Override
    public List<ItemStack> getResult(TileEntity tile, ItemStack input1, ItemStack input2) {
        NBTTagCompound tagCompound = input1.getTagCompound();
        NBTTagCompound heatCompound = tagCompound.getCompoundTag(ItemModUtil.HEAT_TAG);
        if (heatCompound.getTagList("modifiers", Constants.NBT.TAG_COMPOUND).tagCount() > 0){ //TODO: cleanup
            List<ItemStack> results = new ArrayList<>();
            for (int i = 0; i < heatCompound.getTagList("modifiers", Constants.NBT.TAG_COMPOUND).tagCount(); i ++){
                ItemStack s = new ItemStack(heatCompound.getTagList("modifiers", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(i).getCompoundTag("item"));
                if (ItemModUtil.modifierRegistry.get(s.getItem()) != null && ItemModUtil.modifierRegistry.get(s.getItem()).countTowardsTotalLevel){
                    for (int j = 0; j < heatCompound.getTagList("modifiers", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(i).getInteger("level"); j ++){
                        results.add(new ItemStack(heatCompound.getTagList("modifiers", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(i).getCompoundTag("item")));
                    }
                }
            }
            ItemStack result = input1.copy();
            result.getTagCompound().getCompoundTag(ItemModUtil.HEAT_TAG).setTag("modifiers", new NBTTagList());
            results.add(result.copy());
            return results;
        }
        return Lists.newArrayList(result);
    }
}
