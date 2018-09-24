package teamroots.embers.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class TileEntityBinRenderer extends TileEntitySpecialRenderer<TileEntityBin> {
	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	Random random = new Random();
	public TileEntityBinRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntityBin tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile != null){
			random.setSeed(tile.getWorld().getSeed());
			if (!tile.inventory.getStackInSlot(0).isEmpty()){

				ItemStack stack = tile.inventory.getStackInSlot(0);
				int itemCount = (int)Math.ceil((stack.getCount())/4.0);
				for (int i = 0; i < itemCount; i ++){
					GL11.glPushMatrix();
					//EntityItem item = new EntityItem(Minecraft.getMinecraft().world,x,y,z,new ItemStack(tile.inventory.getStackInSlot(0).getItem(),1, tile.inventory.getStackInSlot(0).getMetadata()));
					//item.hoverStart = 0;
					GL11.glTranslated(x, y+0.2+(i*0.0625), z);
					GL11.glTranslated(0.5, 0, 0.5);
					GL11.glRotated(random.nextFloat()*360.0, 0, 1.0, 0);
					GL11.glTranslated(-0.5, 0, -0.5);
					GL11.glRotated(90, 1, 0, 0);
					GL11.glTranslated(0.5+0.1*random.nextFloat(), -0.1875+0.1*random.nextFloat(), 0);
					GL11.glScaled(1.5, 1.5, 1.5);
					GL11.glTranslated(0, stack.getItem() instanceof ItemBlock ? 0.1 : 0.25, 0);
					Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
					//Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, true);
					GL11.glPopMatrix();
				}
			}
		}
	}
}
