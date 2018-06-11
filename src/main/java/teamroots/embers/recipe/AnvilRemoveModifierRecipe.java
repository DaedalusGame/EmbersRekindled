package teamroots.embers.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import teamroots.embers.api.itemmod.ModifierBase;
import teamroots.embers.util.ItemModUtil;

import java.util.ArrayList;
import java.util.List;

public class AnvilRemoveModifierRecipe extends DawnstoneAnvilRecipe {
    @Override
    public boolean matches(ItemStack input1, ItemStack input2) {
        return ItemModUtil.hasHeat(input1) && input1.getTagCompound().getCompoundTag(ItemModUtil.HEAT_TAG).getTagList("modifiers", Constants.NBT.TAG_COMPOUND).tagCount() > 1 && input2.isEmpty();
    }

    @Override
    public List<ItemStack> getResult(TileEntity tile, ItemStack input1, ItemStack input2) {
        NBTTagCompound tagCompound = input1.getTagCompound();
        NBTTagList tagModifiers = tagCompound.getCompoundTag(ItemModUtil.HEAT_TAG).getTagList("modifiers", Constants.NBT.TAG_COMPOUND);
        if (tagModifiers.tagCount() > 0){ //TODO: cleanup
            List<ItemStack> results = new ArrayList<>();
            NBTTagList remainingModifiers = new NBTTagList();
            for (int i = 0; i < tagModifiers.tagCount(); i ++){
                NBTTagCompound tagModifier = tagModifiers.getCompoundTagAt(i);
                ItemStack s = new ItemStack(tagModifier.getCompoundTag("item"));
                ModifierBase modifier = ItemModUtil.modifierRegistry.get(s.getItem());
                if (modifier != null){
                    if(modifier.countTowardsTotalLevel) {
                        for (int j = 0; j < tagModifier.getInteger("level"); j++) {
                            results.add(new ItemStack(tagModifier.getCompoundTag("item")));
                        }
                    } else {
                        remainingModifiers.appendTag(tagModifier);
                    }
                }
            }
            ItemStack result = input1.copy();
            result.getTagCompound().getCompoundTag(ItemModUtil.HEAT_TAG).setTag("modifiers", remainingModifiers);
            results.add(result.copy());
            return results;
        }
        return Lists.newArrayList(result);
    }
}
