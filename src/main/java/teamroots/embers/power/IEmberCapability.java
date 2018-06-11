package teamroots.embers.power;

import net.minecraft.nbt.NBTTagCompound;

public interface IEmberCapability {
	public double getEmber();
	public double getEmberCapacity();
	public void setEmber(double value);
	public void setEmberCapacity(double value);
	public double addAmount(double value, boolean doAdd);
	public double removeAmount(double value, boolean doRemove);
	public void writeToNBT(NBTTagCompound tag);
	public void readFromNBT(NBTTagCompound tag);
}
