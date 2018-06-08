package teamroots.embers.api.power;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public interface IMechCapability {
	double getPower(EnumFacing from);
	void onContentsChanged();
	void setPower(double value, EnumFacing from);
	void writeToNBT(NBTTagCompound tag);
	void readFromNBT(NBTTagCompound tag);
}
