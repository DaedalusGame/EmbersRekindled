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

public class TileEntityChargerRenderer extends TileEntitySpecialRenderer {
	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	Random random = new Random();
	public TileEntityChargerRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile instanceof TileEntityCharger){
			TileEntityCharger charger = (TileEntityCharger)tile;
			if (!charger.inventory.getStackInSlot(0).isEmpty()){
				if (Minecraft.getMinecraft().world != null){
					GlStateManager.pushAttrib();
					GL11.glPushMatrix();
					EntityItem item = new EntityItem(Minecraft.getMinecraft().world,x,y,z,new ItemStack(charger.inventory.getStackInSlot(0).getItem(),1,charger.inventory.getStackInSlot(0).getMetadata()));
					item.hoverStart = 0;
					item.onGround = false;
					GL11.glTranslated(x+0.5, y+0.0625, z+0.5);
					GL11.glRotated(charger.angle+((charger.turnRate))*partialTicks, 0, 1.0, 0);
					Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, false);
					GL11.glPopMatrix();
					GlStateManager.popAttrib();
				}
			}
		}
	}
}
