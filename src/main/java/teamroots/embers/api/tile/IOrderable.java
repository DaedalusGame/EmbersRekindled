package teamroots.embers.api.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public interface IOrderable {
    void order(TileEntity source, ItemStack filter, int orderSize);

    void resetOrder(TileEntity source);
}
