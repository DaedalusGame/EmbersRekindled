package teamroots.embers.power;

import net.minecraft.nbt.NBTTagCompound;
import teamroots.embers.Embers;

public class DefaultStarlightCapability implements IStarlightCapability {
	private double starlight = 0;
	private double capacity = 0;
	@Override
	public double getStarlight() {
		return starlight;
	}

	@Override
	public double getStarlightCapacity() {
		return capacity;
	}

	@Override
	public void setStarlight(double value) {
		starlight = value;
	}

	@Override
	public void setStarlightCapacity(double value) {
		capacity = value;
	}

	@Override
	public double addAmount(double value, boolean doAdd) {
		if (starlight+value > capacity){
			double added = capacity-starlight;
			if (doAdd){
				starlight = capacity;
			}
			return added;
		}
		if (doAdd){
			starlight += value;
		}
		return value;
	}

	@Override
	public double removeAmount(double value, boolean doRemove) {
		if (starlight-value < 0){
			double removed = starlight;
			if (doRemove){
				starlight = 0;
			}
			return removed;
		}
		if (doRemove){
			starlight -= value;
		}
		return value;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		tag.setDouble(Embers.MODID+":ember", starlight);
		tag.setDouble(Embers.MODID+":emberCapacity", capacity);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		if (tag.hasKey(Embers.MODID+":ember")){
			starlight = tag.getDouble(Embers.MODID+":ember");
		}
		if (tag.hasKey(Embers.MODID+":emberCapacity")){
			capacity = tag.getDouble(Embers.MODID+":emberCapacity");
		}
	}
}
