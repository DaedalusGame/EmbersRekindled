package teamroots.embers.item;

import net.minecraft.item.ItemStack;

public interface IEmberChargedTool {
	public static boolean hasEmber(ItemStack stack){
		if (stack.hasTagCompound()){
			return stack.getTagCompound().getBoolean("poweredOn");
		}
		return false;
	}
}
