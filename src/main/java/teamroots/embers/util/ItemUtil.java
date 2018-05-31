package teamroots.embers.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemUtil {
    public static boolean matchesOreDict(ItemStack stack, String oreDictName)
    {
        if(stack.isEmpty()) return false;
        int checkid = OreDictionary.getOreID(oreDictName);
        for (int id:OreDictionary.getOreIDs(stack)) {
            if(id == checkid) return true;
        }
        return false;
    }
}
