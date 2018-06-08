package teamroots.embers.power;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.power.IMechCapability;

public class MechCapabilityProvider implements ICapabilityProvider {
	private IMechCapability capability = null;
	
	public MechCapabilityProvider(){
		capability = new DefaultMechCapability();
	}
	
	public MechCapabilityProvider(IMechCapability capability){
		this.capability = capability;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == EmbersCapabilities.MECH_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    	if (EmbersCapabilities.MECH_CAPABILITY != null && capability == EmbersCapabilities.MECH_CAPABILITY) return (T)capability;
    	return null;
	}
}
