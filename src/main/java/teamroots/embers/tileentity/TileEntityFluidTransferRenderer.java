package teamroots.embers.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;
import teamroots.embers.util.RenderUtil;

import java.util.Random;

public class TileEntityFluidTransferRenderer extends TileEntitySpecialRenderer<TileEntityFluidTransfer> {
	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	Random random = new Random();
	public TileEntityFluidTransferRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntityFluidTransfer tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile != null){
			if (tile.filterFluid != null && Minecraft.getMinecraft().world != null) {
				GlStateManager.pushMatrix();

				RenderHelper.disableStandardItemLighting();
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

				if (Minecraft.isAmbientOcclusionEnabled()) {
					GL11.glShadeModel(GL11.GL_SMOOTH);
				} else {
					GL11.glShadeModel(GL11.GL_FLAT);
				}

				GlStateManager.translate(x, y, z);

				RenderUtil.renderFluidCuboid(tile.filterFluid,tile.getPos(),0.25,0.25,0.25,0.75,0.75,0.75);

				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
				RenderHelper.enableStandardItemLighting();
			}
		}
	}
}
