package teamroots.embers.power;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StarlightCapabilityStorage implements IStorage<IStarlightCapability> {

	@Override
	public NBTBase writeNBT(Capability<IStarlightCapability> capability, IStarlightCapability instance, EnumFacing side) {
		return null;
	}

	@Override
	public void readNBT(Capability<IStarlightCapability> capability, IStarlightCapability instance, EnumFacing side,
			NBTBase nbt) {
		
	}

}
