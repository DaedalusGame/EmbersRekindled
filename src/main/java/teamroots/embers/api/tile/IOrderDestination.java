package teamroots.embers.api.tile;

import net.minecraft.tileentity.TileEntity;
import teamroots.embers.api.filter.IFilter;

public interface IOrderDestination {
    void order(TileEntity source, IFilter filter, int orderSize);

    void resetOrder(TileEntity source);
}
