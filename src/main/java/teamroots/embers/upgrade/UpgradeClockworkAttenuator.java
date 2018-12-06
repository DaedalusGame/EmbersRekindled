package teamroots.embers.upgrade;

import net.minecraft.tileentity.TileEntity;
import teamroots.embers.api.tile.IMechanicallyPowered;
import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.tileentity.TileEntityClockworkAttenuator;
import teamroots.embers.tileentity.TileEntityMiniBoiler;
import teamroots.embers.util.DefaultUpgradeProvider;

import java.util.List;

public class UpgradeClockworkAttenuator extends DefaultUpgradeProvider {
    public UpgradeClockworkAttenuator(TileEntity tile) {
        super("clockwork_attenuator", tile);
    }

    @Override
    public int getLimit(TileEntity tile) {
        return 1;
    }

    @Override
    public double getSpeed(TileEntity tile, double speed) {
        return speed * getSpeedModifier();
    }

    private double getSpeedModifier() {
        if (this.tile instanceof TileEntityClockworkAttenuator)
            return ((TileEntityClockworkAttenuator) this.tile).getSpeed();
        return 0;
    }

    @Override
    public boolean doWork(TileEntity tile, List<IUpgradeProvider> upgrades) {
        return getSpeedModifier() == 0;
    }
}
