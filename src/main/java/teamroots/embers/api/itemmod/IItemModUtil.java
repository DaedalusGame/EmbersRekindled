package teamroots.embers.api.itemmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import teamroots.embers.Embers;

import java.util.Collection;
import java.util.List;

public interface IItemModUtil {
    String HEAT_TAG = Embers.MODID+":heat_tag";

    Collection<ModifierBase> getAllModifiers();

    List<ItemStack> getAllModifierItems();

    ModifierBase getModifier(String name);

    ModifierBase getModifier(ItemStack modStack);

    boolean isModValid(ItemStack stack, ModifierBase modifier);

    List<ModifierBase> getModifiers(ItemStack stack);

    int getTotalModifierLevel(ItemStack stack);

    boolean hasModifier(ItemStack stack, ModifierBase modifier);

    void addModifier(ItemStack stack, ItemStack modifier);

    List<ItemStack> removeAllModifiers(ItemStack stack);

    void addModifierLevel(ItemStack stack, ModifierBase modifier, int levels);

    void setModifierLevel(ItemStack stack, ModifierBase modifier, int level);

    int getModifierLevel(ItemStack stack, ModifierBase modifier);

    boolean hasHeat(ItemStack stack);

    void addHeat(ItemStack stack, float heat);

    void setHeat(ItemStack stack, float heat);

    float getHeat(ItemStack stack);

    float getMaxHeat(ItemStack stack);

    int getLevel(ItemStack stack);

    void setLevel(ItemStack stack, int level);

    int getArmorModifierLevel(EntityPlayer p, ModifierBase modifier);
}
