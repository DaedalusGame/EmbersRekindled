package teamroots.embers.tileentity;

import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.upgrade.UpgradeConservation;

public class TileEntityNodeConservation extends TileEntityNodeBase {
	@Override
	protected IUpgradeProvider initUpgrade() {
		return new UpgradeConservation(this);
	}
}
