package teamroots.embers.upgrade;

import net.minecraft.tileentity.TileEntity;
import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.tileentity.TileEntityClockworkAttenuator;
import teamroots.embers.util.DefaultUpgradeProvider;

import java.util.List;

public class UpgradePurity extends DefaultUpgradeProvider {
    public UpgradePurity(TileEntity tile) {
        super("purity", tile);
    }
}
