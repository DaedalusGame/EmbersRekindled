package teamroots.embers.api.itemmod;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;

public class ModifierBase {
	public enum EnumType {
		ALL,
		TOOL,
		ARMOR,
		HELMET,
		CHESTPLATE,
		LEGGINGS,
		BOOTS
	}
	
	public EnumType type = EnumType.ALL;
	public String name = "";
	public double cost = 0;
	public boolean countTowardsTotalLevel = false;

	public ModifierBase(EnumType type, String name, double cost, boolean levelCounts){
		this.type = type;
		this.name = name;
		this.cost = cost;
		this.countTowardsTotalLevel = levelCounts;
	}

	public boolean canApplyTo(ItemStack stack) {
		Item item = stack.getItem();
		switch (type) {
			case ALL:
				return true;
			case TOOL:
				return item instanceof ItemSword || item instanceof ItemTool;
			case ARMOR:
				return item instanceof ItemArmor;
			case HELMET:
				return item instanceof ItemArmor && ((ItemArmor) item).getEquipmentSlot() == EntityEquipmentSlot.HEAD;
			case CHESTPLATE:
				return item instanceof ItemArmor && ((ItemArmor) item).getEquipmentSlot() == EntityEquipmentSlot.CHEST;
			case LEGGINGS:
				return item instanceof ItemArmor && ((ItemArmor) item).getEquipmentSlot() == EntityEquipmentSlot.LEGS;
			case BOOTS:
				return item instanceof ItemArmor && ((ItemArmor) item).getEquipmentSlot() == EntityEquipmentSlot.FEET;
			default:
				return false;
		}
	}

	public void onApply(ItemStack stack) {
		//NOOP
	}

	public void onRemove(ItemStack stack) {
		//NOOP
	}
}
