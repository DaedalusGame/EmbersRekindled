package teamroots.embers.api.item;

import net.minecraft.item.ItemStack;

@Deprecated
public interface IEmberItem {
	double getEmber(ItemStack stack);
	double getEmberCapacity(ItemStack stack);
	void setEmber(ItemStack stack, double value);
	void setEmberCapacity(ItemStack stack, double value);
	double addAmount(ItemStack stack, double value, boolean doAdd);
	double removeAmount(ItemStack stack, double value, boolean doRemove);
}
