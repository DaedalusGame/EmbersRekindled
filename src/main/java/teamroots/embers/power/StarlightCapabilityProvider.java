package teamroots.embers.power;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class StarlightCapabilityProvider implements ICapabilityProvider {
	private IStarlightCapability capability = null;
	
	public StarlightCapabilityProvider(){
		capability = new DefaultStarlightCapability();
	}
	
	public StarlightCapabilityProvider(IStarlightCapability capability){
		this.capability = capability;
	}
	
	@CapabilityInject(IStarlightCapability.class)
	public static final Capability<IStarlightCapability> starlightCapability = null;
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == starlightCapability;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    	if (starlightCapability != null && capability == starlightCapability) return (T)capability;
    	return null;
	}
}
