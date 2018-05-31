package teamroots.embers.item;

import net.minecraft.item.ItemStack;
import teamroots.embers.RegistryManager;

@Deprecated
public enum EnumStampType {
	TYPE_FLAT, TYPE_BAR, TYPE_PLATE, TYPE_NULL;
	public static EnumStampType getType(ItemStack stack){
		if (stack != ItemStack.EMPTY){
			if (stack.getItem() == RegistryManager.stamp_bar){
				return TYPE_BAR;
			}
			if (stack.getItem() == RegistryManager.stamp_flat){
				return TYPE_FLAT;
			}
			if (stack.getItem() == RegistryManager.stamp_plate){
				return TYPE_PLATE;
			}
		}
		return TYPE_NULL;
	}

	public static ItemStack getStack(EnumStampType type) {
		switch (type) {
			case TYPE_FLAT:
				return new ItemStack(RegistryManager.stamp_flat);
			case TYPE_BAR:
				return new ItemStack(RegistryManager.stamp_bar);
			case TYPE_PLATE:
				return new ItemStack(RegistryManager.stamp_plate);
			default:
				return ItemStack.EMPTY;
		}
	}
}
