package teamroots.embers.api.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.api.upgrades.IUpgradeProvider;

public class EmbersCapabilities {
    @CapabilityInject(IUpgradeProvider.class)
    public static final Capability<IUpgradeProvider> UPGRADE_PROVIDER_CAPABILITY = null;
    @CapabilityInject(IEmberCapability.class)
    public static final Capability<IEmberCapability> EMBER_CAPABILITY = null;
}
