package teamroots.embers.api.tile;

import net.minecraft.tileentity.TileEntity;

public interface ISparkable {
    void sparkProgress(TileEntity tile, double ember);
}
