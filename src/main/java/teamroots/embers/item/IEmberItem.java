package teamroots.embers.item;

import net.minecraft.item.ItemStack;

public interface IEmberItem {
	public double getEmber(ItemStack stack);
	public double getEmberCapacity(ItemStack stack);
	public void setEmber(ItemStack stack, double value);
	public void setEmberCapacity(ItemStack stack, double value);
	public double addAmount(ItemStack stack, double value, boolean doAdd);
	public double removeAmount(ItemStack stack, double value, boolean doRemove);
}
