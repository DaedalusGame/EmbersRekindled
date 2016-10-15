package teamroots.embers.power;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class EmberCapabilityProvider implements ICapabilityProvider {
	private IEmberCapability capability = null;
	
	public EmberCapabilityProvider(){
		capability = new DefaultEmberCapability();
	}
	
	public EmberCapabilityProvider(IEmberCapability capability){
		this.capability = capability;
	}
	
	@CapabilityInject(IEmberCapability.class)
	public static final Capability<IEmberCapability> emberCapability = null;
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == emberCapability;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    	if (emberCapability != null && capability == emberCapability) return (T)capability;
    	return null;
	}
}
