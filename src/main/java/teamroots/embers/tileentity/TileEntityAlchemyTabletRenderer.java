package teamroots.embers.tileentity;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityAlchemyTabletRenderer extends TileEntitySpecialRenderer {
	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	Random random = new Random();
	public TileEntityAlchemyTabletRenderer(){
		super();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage){
		if (tile instanceof TileEntityAlchemyTablet){
			TileEntityAlchemyTablet tablet = (TileEntityAlchemyTablet)tile;
			for (double i = 0; i < 3; i ++){
				for (double j = 0; j < 3; j ++){
					double baseX = 0.25+0.1875*i;
					double baseZ = 0.25+0.1875*j;
					int slot = ((int)i)*3+((int)j);
					if (tablet.inventory.getStackInSlot(slot) != null){
						if (Minecraft.getMinecraft().theWorld != null){
							GlStateManager.pushAttrib();
							GL11.glPushMatrix();
							EntityItem item = new EntityItem(Minecraft.getMinecraft().theWorld,x,y,z,new ItemStack(tablet.inventory.getStackInSlot(slot).getItem(),1,tablet.inventory.getStackInSlot(slot).getMetadata()));
							item.hoverStart = 0;
							item.isCollided = false;
							GL11.glTranslated(x, y, z);
							GL11.glTranslated(baseX-0.0625, 0.875+0.001*j+0.0005*i, baseZ-0.1875);
							GlStateManager.translate(0.0625, 0.0625, 0.0625);
							GlStateManager.rotate(180, 0, 0, 1);
							GlStateManager.rotate(90, 1, 0, 0);
							GlStateManager.translate(-0.0625, -0.0625, -0.0625);
							GlStateManager.scale(0.5, 0.5, 0.5);
							Minecraft.getMinecraft().getRenderManager().doRenderEntity(item, 0, 0, 0, 0, 0, false);
							GL11.glPopMatrix();
							GlStateManager.popAttrib();
						}
					}
				}
			}
		}
	}
}
