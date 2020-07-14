package teamroots.embers.api.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import teamroots.embers.api.item.IFilter;

public interface IOrderDestination {
    void order(TileEntity source, IFilter filter, int orderSize);

    void resetOrder(TileEntity source);
}
