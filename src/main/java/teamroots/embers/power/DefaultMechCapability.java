package teamroots.embers.power;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import teamroots.embers.Embers;
import teamroots.embers.api.power.IMechCapability;

public class DefaultMechCapability implements IMechCapability {
	private double power = 0;
	@Override
	public double getPower(EnumFacing from) {
		return power;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		tag.setDouble(Embers.MODID+":mech_power", power);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		if (tag.hasKey(Embers.MODID+":mech_power")){
			power = tag.getDouble(Embers.MODID+":mech_power");
		}
	}

	@Override
	public void setPower(double value, EnumFacing from) {
		double oldPower = power;
		this.power = value;
		if (oldPower != value){
			onContentsChanged();
		}
	}

	@Override
	public void onContentsChanged() {
		
	}
}
