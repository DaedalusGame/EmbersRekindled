package teamroots.embers.compat.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.api.itemmod.ModifierBase;

@ZenRegister
@ZenExpansion("crafttweaker.item.IItemStack")
public class ItemStackExtensions {
    @ZenGetter("hasHeat")
    public static boolean hasHeat(IItemStack itemStack) {
        ItemStack stack = (ItemStack) itemStack.getInternal();
        return ItemModUtil.hasHeat(stack);
    }

    @ZenGetter("heat")
    public static float getHeat(IItemStack itemStack) {
        ItemStack stack = (ItemStack) itemStack.getInternal();
        return ItemModUtil.getHeat(stack);
    }

    @ZenSetter("heat")
    public static void setHeat(IItemStack itemStack, float heat) {
        ItemStack stack = (ItemStack) itemStack.getInternal();
        ItemModUtil.setHeat(stack, heat);
    }

    @ZenGetter("maxHeat")
    public static float getMaxHeat(IItemStack itemStack) {
        ItemStack stack = (ItemStack) itemStack.getInternal();
        return ItemModUtil.getMaxHeat(stack);
    }

    @ZenGetter("heatLevel")
    public static int getHeatLevel(IItemStack itemStack) {
        ItemStack stack = (ItemStack) itemStack.getInternal();
        return ItemModUtil.getLevel(stack);
    }

    @ZenSetter("heatLevel")
    public static void getHeatLevel(IItemStack itemStack, int level) {
        ItemStack stack = (ItemStack) itemStack.getInternal();
        ItemModUtil.setLevel(stack, level);
    }

    @ZenMethod
    public static void addModifier(IItemStack itemStack, IItemStack modifier) {
        ItemStack stack = (ItemStack) itemStack.getInternal();
        ItemStack modifierStack = CraftTweakerMC.getItemStack(modifier);
        ItemModUtil.addModifier(stack,modifierStack);
    }

    @ZenMethod
    public static boolean isModifierValid(IItemStack itemStack, String modifierName) {
        ModifierBase modifier = ItemModUtil.getModifier(modifierName);
        ItemStack stack = (ItemStack) itemStack.getInternal();
        return ItemModUtil.isModValid(stack,modifier);
    }

    @ZenMethod
    public static boolean hasModifier(IItemStack itemStack, String modifierName) {
        ModifierBase modifier = ItemModUtil.getModifier(modifierName);
        ItemStack stack = (ItemStack) itemStack.getInternal();
        return ItemModUtil.hasModifier(stack,modifier);
    }

    @ZenMethod
    public static int getModifierLevel(IItemStack itemStack, String modifierName) {
        ModifierBase modifier = ItemModUtil.getModifier(modifierName);
        ItemStack stack = (ItemStack) itemStack.getInternal();
        return ItemModUtil.getModifierLevel(stack,modifier);
    }

    @ZenMethod
    public static void setModifierLevel(IItemStack itemStack, String modifierName, int level) {
        ModifierBase modifier = ItemModUtil.getModifier(modifierName);
        ItemStack stack = (ItemStack) itemStack.getInternal();
        ItemModUtil.setModifierLevel(stack,modifier,level);
    }
}
