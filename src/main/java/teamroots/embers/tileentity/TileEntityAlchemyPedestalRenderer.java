package teamroots.embers.tileentity;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import teamroots.embers.Embers;
import teamroots.embers.util.RenderUtil;
import teamroots.embers.util.StructUV;

public class TileEntityAlchemyPedestalRenderer extends TileEntitySpecialRenderer {
	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	Random random = new Random();
	public ResourceLocation texture = new ResourceLocation(Embers.MODID + ":textures/blocks/ash.png");
	public TileEntityAlchemyPedestalRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile instanceof TileEntityAlchemyPedestal){
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			TileEntityAlchemyPedestal pedestal = (TileEntityAlchemyPedestal)tile;
			if (pedestal.inventory.getStackInSlot(1) != ItemStack.EMPTY){
				if (Minecraft.getMinecraft().world != null){
					GL11.glPushMatrix();
					GL11.glTranslated(x+0.5, y+0.75, z+0.5);
					GL11.glRotated(pedestal.angle+((pedestal.turnRate))*partialTicks, 0, 1.0, 0);
					Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(pedestal.inventory.getStackInSlot(1).getItem(),1,pedestal.inventory.getStackInSlot(1).getMetadata()), TransformType.GROUND);
					GL11.glPopMatrix();
				}
			}
			
			if (pedestal.inventory.getStackInSlot(0) != ItemStack.EMPTY){
				float coeff = pedestal.inventory.getStackInSlot(0).getCount()/64.0f;
	            
	            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
	            Tessellator tess = Tessellator.getInstance();
	            BufferBuilder buffer = tess.getBuffer();
	            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
	            RenderUtil.addBoxExt(buffer, x+0.3125, y+0.125+coeff*0.375, z+0.3125, x+0.6875, y+0.125+coeff*0.35, z+0.6875, new StructUV[]{null,new StructUV(1, 1, 7, 7, 16, 16),null,null,null,null}, new int[]{1,1,1,1,1,1}, new boolean[]{false,true,false,false,false,false});
	            tess.draw();
			}
		}
	}
}
