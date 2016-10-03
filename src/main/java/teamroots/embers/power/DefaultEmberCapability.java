package teamroots.embers.power;

import net.minecraft.nbt.NBTTagCompound;
import teamroots.embers.Embers;

public class DefaultEmberCapability implements IEmberCapability {
	private double ember = 0;
	private double capacity = 0;
	@Override
	public double getEmber() {
		return ember;
	}

	@Override
	public double getEmberCapacity() {
		return capacity;
	}

	@Override
	public void setEmber(double value) {
		ember = value;
	}

	@Override
	public void setEmberCapacity(double value) {
		capacity = value;
	}

	@Override
	public double addAmount(double value, boolean doAdd) {
		if (ember+value > capacity){
			if (doAdd){
				ember = capacity;
			}
			return capacity-ember;
		}
		if (doAdd){
			ember += value;
		}
		return value;
	}

	@Override
	public double removeAmount(double value, boolean doRemove) {
		if (ember-value < 0){
			if (doRemove){
				ember = 0;
			}
			return ember;
		}
		if (doRemove){
			ember -= value;
		}
		return value;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		tag.setDouble(Embers.MODID+":ember", ember);
		tag.setDouble(Embers.MODID+":emberCapacity", capacity);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		if (tag.hasKey(Embers.MODID+":ember")){
			ember = tag.getDouble(Embers.MODID+":ember");
		}
		if (tag.hasKey(Embers.MODID+":emberCapacity")){
			capacity = tag.getDouble(Embers.MODID+":emberCapacity");
		}
	}
}
