package teamroots.embers.power;

import net.minecraft.nbt.NBTTagCompound;

public interface IStarlightCapability {
	public double getStarlight();
	public double getStarlightCapacity();
	public void setStarlight(double value);
	public void setStarlightCapacity(double value);
	public double addAmount(double value, boolean doAdd);
	public double removeAmount(double value, boolean doRemove);
	public void writeToNBT(NBTTagCompound tag);
	public void readFromNBT(NBTTagCompound tag);
}
