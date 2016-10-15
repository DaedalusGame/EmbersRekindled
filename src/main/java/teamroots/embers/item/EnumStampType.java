package teamroots.embers.item;

import net.minecraft.item.ItemStack;
import teamroots.embers.RegistryManager;

public enum EnumStampType {
	TYPE_FLAT, TYPE_BAR, TYPE_PLATE, TYPE_NULL;
	public static EnumStampType getType(ItemStack stack){
		if (stack != null){
			if (stack.getItem() == RegistryManager.stampBar){
				return TYPE_BAR;
			}
			if (stack.getItem() == RegistryManager.stampFlat){
				return TYPE_FLAT;
			}
			if (stack.getItem() == RegistryManager.stampPlate){
				return TYPE_PLATE;
			}
		}
		return TYPE_NULL;
	}
}
