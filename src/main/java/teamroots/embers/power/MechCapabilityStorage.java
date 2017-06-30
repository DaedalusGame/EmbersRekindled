package teamroots.embers.power;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class MechCapabilityStorage implements IStorage<IMechCapability> {

	@Override
	public NBTBase writeNBT(Capability<IMechCapability> capability, IMechCapability instance, EnumFacing side) {
		return null;
	}

	@Override
	public void readNBT(Capability<IMechCapability> capability, IMechCapability instance, EnumFacing side,
			NBTBase nbt) {
		
	}

}
