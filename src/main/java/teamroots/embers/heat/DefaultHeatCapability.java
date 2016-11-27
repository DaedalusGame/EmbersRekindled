package teamroots.embers.heat;

import net.minecraft.nbt.NBTTagCompound;
import teamroots.embers.Embers;

public class DefaultHeatCapability implements IHeatCapability {
	private double heat = 0;
	private double capacity = 0;
	@Override
	public double getHeat() {
		return heat;
	}

	@Override
	public double getHeatCapacity() {
		return capacity;
	}

	@Override
	public void setHeat(double value) {
		heat = value;
	}

	@Override
	public void setHeatCapacity(double value) {
		capacity = value;
	}

	@Override
	public double addAmount(double value, boolean doAdd) {
		if (heat+value > capacity){
			double added = capacity-heat;
			if (doAdd){
				heat = capacity;
			}
			return added;
		}
		if (doAdd){
			heat += value;
		}
		return value;
	}

	@Override
	public double removeAmount(double value, boolean doRemove) {
		if (heat-value < 0){
			double removed = heat;
			if (doRemove){
				heat = 0;
			}
			return removed;
		}
		if (doRemove){
			heat -= value;
		}
		return value;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		tag.setDouble(Embers.MODID+":heat", heat);
		tag.setDouble(Embers.MODID+":heatCapacity", capacity);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		if (tag.hasKey(Embers.MODID+":heat")){
			heat = tag.getDouble(Embers.MODID+":heat");
		}
		if (tag.hasKey(Embers.MODID+":heatCapacity")){
			capacity = tag.getDouble(Embers.MODID+":heatCapacity");
		}
	}
}
