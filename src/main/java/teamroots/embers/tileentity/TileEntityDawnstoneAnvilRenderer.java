package teamroots.embers.tileentity;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDawnstoneAnvilRenderer extends TileEntitySpecialRenderer {
	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	Random random = new Random();
	public TileEntityDawnstoneAnvilRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile instanceof TileEntityDawnstoneAnvil){
			TileEntityDawnstoneAnvil anvil = (TileEntityDawnstoneAnvil)tile;
			random.setSeed(tile.getWorld().getSeed());
			if (!anvil.inventory.getStackInSlot(0).isEmpty()){
				GL11.glPushMatrix();
				EntityItem item = new EntityItem(Minecraft.getMinecraft().world,x,y,z,new ItemStack(anvil.inventory.getStackInSlot(0).getItem(),1,anvil.inventory.getStackInSlot(0).getMetadata()));
				item.hoverStart = 0;
				GL11.glTranslated(x, y+1.0f, z);
				GL11.glTranslated(0.5, 0, 0.5);
				GL11.glRotated(random.nextFloat()*360.0, 0, 1.0, 0);
				GL11.glTranslated(-0.5, 0, -0.5);
				GL11.glRotated(90, 1, 0, 0);
				GL11.glTranslated(0.5+0.1*random.nextFloat(), -0.1875+0.1*random.nextFloat(), 0);
				GL11.glScaled(1.5, 1.5, 1.5);
				Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, true);
				GL11.glPopMatrix();
			}
			if (!anvil.inventory.getStackInSlot(1).isEmpty()){
				GL11.glPushMatrix();
				EntityItem item = new EntityItem(Minecraft.getMinecraft().world,x,y,z,new ItemStack(anvil.inventory.getStackInSlot(1).getItem(),1,anvil.inventory.getStackInSlot(1).getMetadata()));
				item.hoverStart = 0;
				GL11.glTranslated(x, y+1.0625f, z);
				GL11.glTranslated(0.5, 0, 0.5);
				GL11.glRotated(random.nextFloat()*360.0, 0, 1.0, 0);
				GL11.glTranslated(-0.5, 0, -0.5);
				GL11.glRotated(90, 1, 0, 0);
				GL11.glTranslated(0.5+0.1*random.nextFloat(), -0.1875+0.1*random.nextFloat(), 0);
				GL11.glScaled(1.5, 1.5, 1.5);
				Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, true);
				GL11.glPopMatrix();
			}
		}
	}
}
