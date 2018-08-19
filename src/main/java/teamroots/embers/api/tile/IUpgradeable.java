package teamroots.embers.api.tile;

import teamroots.embers.api.upgrades.IUpgradeProvider;

import java.util.List;

public interface IUpgradeable {
    List<IUpgradeProvider> getUpgrades();

    default void handleUpgrades() {

    }
}
