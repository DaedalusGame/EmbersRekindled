package teamroots.embers.api.upgrades;

import net.minecraft.util.EnumFacing;

import java.util.List;

public interface IUpgradeProxy {
    void collectUpgrades(List<IUpgradeProvider> upgrades);
    boolean isSocket(EnumFacing facing);
    boolean isProvider(EnumFacing facing);
}
