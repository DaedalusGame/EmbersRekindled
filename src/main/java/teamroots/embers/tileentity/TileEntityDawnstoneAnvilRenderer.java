package teamroots.embers.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class TileEntityDawnstoneAnvilRenderer extends TileEntitySpecialRenderer<TileEntityDawnstoneAnvil> {
	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	Random random = new Random();
	public TileEntityDawnstoneAnvilRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntityDawnstoneAnvil tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile != null){
			random.setSeed(tile.getPos().hashCode());
			if (!tile.inventory.getStackInSlot(0).isEmpty()){
				GL11.glPushMatrix();
				//EntityItem item = new EntityItem(Minecraft.getMinecraft().world,x,y,z,new ItemStack(tile.inventory.getStackInSlot(0).getItem(),1, tile.inventory.getStackInSlot(0).getMetadata()));
				GL11.glTranslated(x, y+1.0f, z);
				GL11.glTranslated(0.5, 0, 0.5);
				GL11.glRotated(random.nextFloat()*360.0, 0, 1.0, 0);
				GL11.glTranslated(-0.5, 0, -0.5);
				GL11.glRotated(90, 1, 0, 0);
				GL11.glTranslated(0.5+0.1*2*(random.nextFloat()-0.5), 0/*-0.1875+0.1*random.nextFloat()*/, 0);
				GL11.glScaled(1.5, 1.5, 1.5);
				GL11.glTranslated(0, 0.25, 0);
				Minecraft.getMinecraft().getRenderItem().renderItem(tile.inventory.getStackInSlot(0), ItemCameraTransforms.TransformType.GROUND);
				//Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, true);
				GL11.glPopMatrix();
			}
			if (!tile.inventory.getStackInSlot(1).isEmpty()){
				GL11.glPushMatrix();
				//EntityItem item = new EntityItem(Minecraft.getMinecraft().world,x,y,z,new ItemStack(tile.inventory.getStackInSlot(1).getItem(),1, tile.inventory.getStackInSlot(1).getMetadata()));
				GL11.glTranslated(x, y+1.0625f, z);
				GL11.glTranslated(0.5, 0, 0.5);
				GL11.glRotated(random.nextFloat()*360.0, 0, 1.0, 0);
				GL11.glTranslated(-0.5, 0, -0.5);
				GL11.glRotated(90, 1, 0, 0);
				GL11.glTranslated(0.5+0.1*2*(random.nextFloat()-0.5), 0/*-0.1875+0.1*random.nextFloat()*/, 0);
				GL11.glScaled(1.5, 1.5, 1.5);
				GL11.glTranslated(0, 0.25, 0);
				Minecraft.getMinecraft().getRenderItem().renderItem(tile.inventory.getStackInSlot(1), ItemCameraTransforms.TransformType.GROUND);
				//Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, true);
				GL11.glPopMatrix();
			}
		}
	}
}
