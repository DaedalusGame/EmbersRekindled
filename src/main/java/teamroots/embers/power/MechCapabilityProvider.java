package teamroots.embers.power;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class MechCapabilityProvider implements ICapabilityProvider {
	private IMechCapability capability = null;
	
	public MechCapabilityProvider(){
		capability = new DefaultMechCapability();
	}
	
	public MechCapabilityProvider(IMechCapability capability){
		this.capability = capability;
	}
	
	@CapabilityInject(IMechCapability.class)
	public static final Capability<IMechCapability> mechCapability = null;
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == mechCapability;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    	if (mechCapability != null && capability == mechCapability) return (T)capability;
    	return null;
	}
}
