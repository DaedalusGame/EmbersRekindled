package teamroots.embers.upgrade;

import net.minecraft.tileentity.TileEntity;
import teamroots.embers.util.DefaultUpgradeProvider;

public class UpgradeConservation extends DefaultUpgradeProvider {
    public UpgradeConservation(TileEntity tile) {
        super("conservation", tile);
    }
}
