package teamroots.embers.api.event;

import net.minecraft.tileentity.TileEntity;

public class UpgradeEvent {
    TileEntity tile;

    public UpgradeEvent(TileEntity tile) {
        this.tile = tile;
    }

    public TileEntity getTile() {
        return tile;
    }
}
