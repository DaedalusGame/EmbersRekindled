package teamroots.embers.api.power;

import net.minecraft.nbt.NBTTagCompound;

public interface IEmberCapability {
	double getEmber();
	double getEmberCapacity();
	void setEmber(double value);
	void setEmberCapacity(double value);
	double addAmount(double value, boolean doAdd);
	double removeAmount(double value, boolean doRemove);
	void writeToNBT(NBTTagCompound tag);
	void readFromNBT(NBTTagCompound tag);
}
