package teamroots.embers.api.itemmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.List;

public class ItemModUtil {
    public static IItemModUtil IMPL;
    public static final String HEAT_TAG = IItemModUtil.HEAT_TAG; //I'm gonna be honest with you, I have no idea where to put this.

    public static Collection<ModifierBase> getAllModifiers() {
        return IMPL.getAllModifiers();
    }

    public static List<ItemStack> getAllModifierItems() {
        return IMPL.getAllModifierItems();
    }

    public static ModifierBase getModifier(String name) {
        return IMPL.getModifier(name);
    }

    public static ModifierBase getModifier(ItemStack modStack) {
        return IMPL.getModifier(modStack);
    }

    public static boolean isModValid(ItemStack stack, ModifierBase modifier){
        return IMPL.isModValid(stack,modifier);
    }

    public static List<ModifierBase> getModifiers(ItemStack stack){
        return IMPL.getModifiers(stack);
    }

    public static int getTotalModifierLevel(ItemStack stack){
        return IMPL.getTotalModifierLevel(stack);
    }

    public static boolean hasModifier(ItemStack stack, ModifierBase modifier){
        return IMPL.hasModifier(stack,modifier);
    }

    public static void addModifier(ItemStack stack, ItemStack modifier){
        IMPL.addModifier(stack,modifier);
    }

    public static List<ItemStack> removeAllModifiers(ItemStack stack){
        return IMPL.removeAllModifiers(stack);
    }

    public static void addModifierLevel(ItemStack stack, ModifierBase modifier, int levels){
        IMPL.addModifierLevel(stack,modifier,levels);
    }

    public static void setModifierLevel(ItemStack stack, ModifierBase modifier, int level){
        IMPL.setModifierLevel(stack,modifier,level);
    }

    public static int getModifierLevel(ItemStack stack, ModifierBase modifier){
        return IMPL.getModifierLevel(stack,modifier);
    }

    public static boolean hasHeat(ItemStack stack){
        return IMPL.hasHeat(stack);
    }

    public static void addHeat(ItemStack stack, float heat){
        IMPL.addHeat(stack,heat);
    }

    public static void setHeat(ItemStack stack, float heat){
        IMPL.setHeat(stack,heat);
    }

    public static float getHeat(ItemStack stack){
        return IMPL.getHeat(stack);
    }

    public static float getMaxHeat(ItemStack stack){
        return IMPL.getMaxHeat(stack);
    }

    public static int getLevel(ItemStack stack){
        return IMPL.getLevel(stack);
    }

    public static void setLevel(ItemStack stack, int level){
        IMPL.setLevel(stack,level);
    }

    public static int getArmorModifierLevel(EntityPlayer p, ModifierBase modifier){
        return IMPL.getArmorModifierLevel(p,modifier);
    }
}
