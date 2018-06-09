package teamroots.embers.util;

import net.minecraft.tileentity.TileEntity;
import teamroots.embers.api.upgrades.IUpgradeProvider;

public class DefaultUpgradeProvider implements IUpgradeProvider {
    protected final String id;
    protected final TileEntity tile;

    public DefaultUpgradeProvider(String id, TileEntity tile)
    {
        this.id = id;
        this.tile = tile;
    }

    @Override
    public String getUpgradeId() {
        return id;
    }
}
