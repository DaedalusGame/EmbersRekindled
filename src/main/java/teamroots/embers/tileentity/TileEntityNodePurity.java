package teamroots.embers.tileentity;

import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.upgrade.UpgradeConservation;
import teamroots.embers.upgrade.UpgradePurity;

public class TileEntityNodePurity extends TileEntityNodeBase {
	@Override
	protected IUpgradeProvider initUpgrade() {
		return new UpgradePurity(this);
	}
}
