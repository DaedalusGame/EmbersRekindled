package teamroots.embers.api.tile;

import net.minecraft.tileentity.TileEntity;

public interface IEmberInjectable {
    void inject(TileEntity injector, double ember);

    default boolean isValid() {
        return true;
    }
}
