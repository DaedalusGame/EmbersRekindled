package teamroots.embers.compat.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.api.itemmod.ModifierBase;

@ZenRegister
@ZenExpansion("crafttweaker.item.IIngredient")
public class IngredientExtensions {
    @ZenMethod
    public static IIngredient anyHeat(IIngredient ingredient)
    {
        return ingredient.only(iItemStack -> {
            ItemStack stack = CraftTweakerMC.getItemStack(iItemStack);
            return ItemModUtil.hasHeat(stack);
        });
    }

    @ZenMethod
    public static IIngredient onlyHeatAtLeast(IIngredient ingredient, float threshold)
    {
        return ingredient.only(iItemStack -> {
            ItemStack stack = CraftTweakerMC.getItemStack(iItemStack);
            return ItemModUtil.getHeat(stack) >= threshold;
        });
    }

    @ZenMethod
    public static IIngredient onlyHeatAtMost(IIngredient ingredient, float threshold)
    {
        return ingredient.only(iItemStack -> {
            ItemStack stack = CraftTweakerMC.getItemStack(iItemStack);
            return ItemModUtil.getHeat(stack) <= threshold;
        });
    }

    @ZenMethod
    public static IIngredient onlyHeatLevelAtLeast(IIngredient ingredient, int threshold)
    {
        return ingredient.only(iItemStack -> {
            ItemStack stack = CraftTweakerMC.getItemStack(iItemStack);
            return ItemModUtil.getLevel(stack) >= threshold;
        });
    }

    @ZenMethod
    public static IIngredient onlyHeatLevelAtMost(IIngredient ingredient, int threshold)
    {
        return ingredient.only(iItemStack -> {
            ItemStack stack = CraftTweakerMC.getItemStack(iItemStack);
            return ItemModUtil.getLevel(stack) <= threshold;
        });
    }

    @ZenMethod
    public static IIngredient onlyWithModifier(IIngredient ingredient, String modifierName)
    {
        ModifierBase modifier = ItemModUtil.getModifier(modifierName);
        return ingredient.only(iItemStack -> {
            ItemStack stack = CraftTweakerMC.getItemStack(iItemStack);
            return ItemModUtil.hasModifier(stack, modifier);
        });
    }

    @ZenMethod
    public static IIngredient onlyWithModifierLevelAtLeast(IIngredient ingredient, String modifierName, int threshold)
    {
        ModifierBase modifier = ItemModUtil.getModifier(modifierName);
        return ingredient.only(iItemStack -> {
            ItemStack stack = CraftTweakerMC.getItemStack(iItemStack);
            return ItemModUtil.getModifierLevel(stack, modifier) >= threshold;
        });
    }

    @ZenMethod
    public static IIngredient onlyWithModifierLevelAtMost(IIngredient ingredient, String modifierName, int threshold)
    {
        ModifierBase modifier = ItemModUtil.getModifier(modifierName);
        return ingredient.only(iItemStack -> {
            ItemStack stack = CraftTweakerMC.getItemStack(iItemStack);
            return ItemModUtil.getModifierLevel(stack, modifier) >= threshold;
        });
    }

    @ZenMethod
    public static IIngredient onlyIfModifierValid(IIngredient ingredient, String modifierName)
    {
        ModifierBase modifier = ItemModUtil.getModifier(modifierName);
        return ingredient.only(iItemStack -> {
            ItemStack stack = CraftTweakerMC.getItemStack(iItemStack);
            return ItemModUtil.isModValid(stack, modifier);
        });
    }
}
