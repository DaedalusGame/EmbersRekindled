package teamroots.embers.power;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
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
		double added = Math.min(capacity - ember,value);
		double newEmber = ember + added;
		if (doAdd){
			if(newEmber != ember)
				onContentsChanged();
			ember += added;
		}
		return value;
	}

	@Override
	public double removeAmount(double value, boolean doRemove) {
		double removed = Math.min(ember,value);
		double newEmber = ember - removed;
		if (doRemove){
			if(newEmber != ember)
				onContentsChanged();
			ember -= removed;
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

	@Override
	public void onContentsChanged() {

	}
}
