package teamroots.embers.api.tile;

import net.minecraft.tileentity.TileEntity;

public interface IHammerable {
    void onHit(TileEntity hammer);

    default boolean isValid() {
        return true;
    }
}
