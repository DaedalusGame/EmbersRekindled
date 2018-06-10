package teamroots.embers.tileentity;

import net.minecraft.tileentity.TileEntity;

public interface ITileEntitySpecialRendererLater {
	void renderLater(TileEntity tile, double x, double y, double z, float partialTicks);
}	
