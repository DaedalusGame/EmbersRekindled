package teamroots.embers.power;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public interface IMechCapability {
	public double getPower(EnumFacing from);
	public void onContentsChanged();
	public void setPower(double value, EnumFacing from);
	public void writeToNBT(NBTTagCompound tag);
	public void readFromNBT(NBTTagCompound tag);
}
