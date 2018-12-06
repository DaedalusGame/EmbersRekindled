package teamroots.embers.upgrade;

import net.minecraft.tileentity.TileEntity;
import teamroots.embers.tileentity.TileEntityCharger;
import teamroots.embers.util.DefaultUpgradeProvider;

public class UpgradeSiphon extends DefaultUpgradeProvider {
    public UpgradeSiphon(TileEntity tile) {
        super("siphon", tile);
    }

    @Override
    public int getLimit(TileEntity tile) {
        return tile instanceof TileEntityCharger ? 1 : 0;
    }

    @Override
    public double getSpeed(TileEntity tile, double speed) {
        return speed * -1;
    }
}
