package teamroots.embers.apiimpl;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import teamroots.embers.api.itemmod.IItemModUtil;
import teamroots.embers.api.itemmod.ModifierBase;
import teamroots.embers.util.ItemModUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class ItemModUtilImpl implements IItemModUtil {
    @Override
    public Collection<ModifierBase> getAllModifiers() {
        return ItemModUtil.modifierRegistry.values();
    }

    @Override
    public List<ItemStack> getAllModifierItems() {
        return ItemModUtil.modifierRegistry.keySet().stream().map(ItemStack::new).collect(Collectors.toList());
    }

    @Override
    public ModifierBase getModifier(String name) {
        return teamroots.embers.util.ItemModUtil.nameToModifier.get(name);
    }

    @Override
    public ModifierBase getModifier(ItemStack modStack) {
        return teamroots.embers.util.ItemModUtil.modifierRegistry.get(modStack.getItem());
    }

    @Override
    public boolean isModValid(ItemStack stack, ModifierBase modifier) {
        return modifier.canApplyTo(stack);
    }

    @Override
    public List<ModifierBase> getModifiers(ItemStack stack) {
            NBTTagCompound tagCompound = stack.getTagCompound();
            NBTTagList tagModifiers = tagCompound.getCompoundTag(IItemModUtil.HEAT_TAG).getTagList("modifiers", Constants.NBT.TAG_COMPOUND);
            if (tagModifiers.tagCount() > 0) {
                List<ModifierBase> results = new ArrayList<>();
                for (int i = 0; i < tagModifiers.tagCount(); i++) {
                    NBTTagCompound tagModifier = tagModifiers.getCompoundTagAt(i);
                    String name = tagModifier.getString("name");
                    ModifierBase modifier = getModifier(name);
                    if(modifier != null)
                    results.add(modifier);
                }
                return results;
            }
        return Lists.newArrayList();
    }

    @Override
    public int getTotalModifierLevel(ItemStack stack) {
        return teamroots.embers.util.ItemModUtil.getTotalModLevel(stack);
    }

    @Override
    public boolean hasModifier(ItemStack stack, ModifierBase modifier) {
        return teamroots.embers.util.ItemModUtil.hasModifier(stack,modifier.name);
    }

    @Override
    public void addModifier(ItemStack stack, ItemStack modifier) {
        teamroots.embers.util.ItemModUtil.addModifier(stack,modifier);
    }

    @Override
    public List<ItemStack> removeAllModifiers(ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        NBTTagList tagModifiers = tagCompound.getCompoundTag(IItemModUtil.HEAT_TAG).getTagList("modifiers", Constants.NBT.TAG_COMPOUND);
        if (tagModifiers.tagCount() > 0){ //TODO: cleanup
            List<ItemStack> results = new ArrayList<>();
            NBTTagList remainingModifiers = new NBTTagList();
            List<ModifierBase> removedModifiers = new ArrayList<>();
            for (int i = 0; i < tagModifiers.tagCount(); i ++){
                NBTTagCompound tagModifier = tagModifiers.getCompoundTagAt(i);
                ItemStack s = new ItemStack(tagModifier.getCompoundTag("item"));
                ModifierBase modifier = getModifier(s);
                if (modifier != null){
                    if(modifier.canRemove) {
                        for (int j = 0; j < tagModifier.getInteger("level"); j++) {
                            removedModifiers.add(modifier);
                            results.add(new ItemStack(tagModifier.getCompoundTag("item")));
                        }
                    } else {
                        remainingModifiers.appendTag(tagModifier);
                    }
                }
            }
            tagCompound.getCompoundTag(IItemModUtil.HEAT_TAG).setTag("modifiers", remainingModifiers);
            for (ModifierBase modifier : removedModifiers) {
                modifier.onRemove(stack);
            }
            return results;
        }
        return Lists.newArrayList();
    }

    @Override
    public void addModifierLevel(ItemStack stack, ModifierBase modifier, int levels) {
        setModifierLevel(stack,modifier,getModifierLevel(stack,modifier)+levels); //This is redundant but intuitive
    }

    @Override
    public void setModifierLevel(ItemStack stack, ModifierBase modifier, int level) {
        teamroots.embers.util.ItemModUtil.setModifierLevel(stack,modifier.name,level);
    }

    @Override
    public int getModifierLevel(ItemStack stack, ModifierBase modifier) {
        return teamroots.embers.util.ItemModUtil.getModifierLevel(stack,modifier.name);
    }

    @Override
    public boolean hasHeat(ItemStack stack) {
        return teamroots.embers.util.ItemModUtil.hasHeat(stack);
    }

    @Override
    public void addHeat(ItemStack stack, float heat) {
        teamroots.embers.util.ItemModUtil.addHeat(stack,heat);
    }

    @Override
    public void setHeat(ItemStack stack, float heat) {
        teamroots.embers.util.ItemModUtil.setHeat(stack,heat);
    }

    @Override
    public float getHeat(ItemStack stack) {
        return teamroots.embers.util.ItemModUtil.getHeat(stack);
    }

    @Override
    public float getMaxHeat(ItemStack stack) {
        return teamroots.embers.util.ItemModUtil.getMaxHeat(stack);
    }

    @Override
    public int getLevel(ItemStack stack) {
        return teamroots.embers.util.ItemModUtil.getLevel(stack);
    }

    @Override
    public void setLevel(ItemStack stack, int level) {
        teamroots.embers.util.ItemModUtil.setLevel(stack, level);
    }

    @Override
    public int getArmorModifierLevel(EntityLivingBase p, ModifierBase modifier) {
        return teamroots.embers.util.ItemModUtil.getArmorMod(p,modifier.name);
    }
}
