package teamroots.embers.heat;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class HeatCapabilityStorage implements IStorage<IHeatCapability> {

	@Override
	public NBTBase writeNBT(Capability<IHeatCapability> capability, IHeatCapability instance, EnumFacing side) {
		return null;
	}

	@Override
	public void readNBT(Capability<IHeatCapability> capability, IHeatCapability instance, EnumFacing side,
			NBTBase nbt) {
		
	}

}
