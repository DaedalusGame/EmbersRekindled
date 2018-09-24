package teamroots.embers.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class TileEntityCinderPlinthRenderer extends TileEntitySpecialRenderer<TileEntityCinderPlinth> {
	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	Random random = new Random();
	public TileEntityCinderPlinthRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntityCinderPlinth tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile != null){
			if (!tile.inventory.getStackInSlot(0).isEmpty()){
				if (Minecraft.getMinecraft().world != null){
					GlStateManager.pushAttrib();
					GL11.glPushMatrix();
					//EntityItem item = new EntityItem(Minecraft.getMinecraft().world,x,y,z,new ItemStack(tile.inventory.getStackInSlot(0).getItem(),1, tile.inventory.getStackInSlot(0).getMetadata()));
					//item.hoverStart = 0;
					//item.onGround = false;
					GL11.glTranslated(x+0.5, y+0.75+0.25, z+0.5);
					GL11.glRotated(tile.angle+((tile.turnRate))*partialTicks, 0, 1.0, 0);
					Minecraft.getMinecraft().getRenderItem().renderItem(tile.inventory.getStackInSlot(0), ItemCameraTransforms.TransformType.GROUND);
					//Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, false);
					GL11.glPopMatrix();
					GlStateManager.popAttrib();
				}
			}
		}
	}
}
