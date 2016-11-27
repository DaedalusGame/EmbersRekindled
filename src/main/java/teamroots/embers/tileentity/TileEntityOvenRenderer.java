package teamroots.embers.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityOvenRenderer extends TileEntitySpecialRenderer {
	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	int blue, green, red, alpha;
	int lightx, lighty;
	double minU, minV, maxU, maxV, diffU, diffV;
	public TileEntityOvenRenderer(){
		super();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage){
		if (tile instanceof TileEntityOven){
			TileEntityOven furnace = (TileEntityOven)tile;
			for (int i = 0; i < furnace.inputs.getSlots(); i ++){
				ItemStack stack = furnace.inputs.getStackInSlot(i);
				if (stack != null){
					GlStateManager.pushMatrix();
					GlStateManager.translate(x+0.5,y,z+0.5);
					GlStateManager.rotate(1.0f*((float)furnace.angle+partialTicks),0,1,0);
					EntityItem item = new EntityItem(tile.getWorld(),0,0,0,stack);
					item.hoverStart = 0;
					Minecraft.getMinecraft().getRenderManager().doRenderEntity(item, 0, 0, 0, 0, 0, true);
					GlStateManager.popMatrix();
				}
			}
		}
	}
}
