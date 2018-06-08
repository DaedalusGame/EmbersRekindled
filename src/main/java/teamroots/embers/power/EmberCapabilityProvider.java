package teamroots.embers.power;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.power.IEmberCapability;

public class EmberCapabilityProvider implements ICapabilityProvider {
	private IEmberCapability capability = null;
	
	public EmberCapabilityProvider(){
		capability = new DefaultEmberCapability();
	}
	
	public EmberCapabilityProvider(IEmberCapability capability){
		this.capability = capability;
	}

	@CapabilityInject(IEmberCapability.class)
	@Deprecated
	public static final Capability<IEmberCapability> emberCapability = null;

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == EmbersCapabilities.EMBER_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    	if (EmbersCapabilities.EMBER_CAPABILITY != null && capability == EmbersCapabilities.EMBER_CAPABILITY) return
				(T)capability;
    	return null;
	}
}
