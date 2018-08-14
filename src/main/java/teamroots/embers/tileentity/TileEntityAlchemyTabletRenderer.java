package teamroots.embers.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import teamroots.embers.Embers;
import teamroots.embers.util.RenderUtil;

import java.util.Random;

public class TileEntityAlchemyTabletRenderer extends TileEntitySpecialRenderer<TileEntityAlchemyTablet> implements ITileEntitySpecialRendererLater {
	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	Random random = new Random();
	public ResourceLocation texture = new ResourceLocation(Embers.MODID + ":textures/entity/beam.png");
	public TileEntityAlchemyTabletRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntityAlchemyTablet tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile != null){
			if (tile.process != 0){
				float processSign = (tile.progress == 1) ? 1 : -1;
				if (tile.process == 20){
					processSign = 0;
				}
				Tessellator tess = Tessellator.getInstance();
				BufferBuilder b = tess.getBuffer();
				Minecraft.getMinecraft().renderEngine.bindTexture(texture);
				GlStateManager.disableLighting();
				GlStateManager.enableBlend();
				GlStateManager.enableAlpha();
				GlStateManager.disableCull();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
				int dfunc = GL11.glGetInteger(GL11.GL_DEPTH_FUNC);
				GlStateManager.depthFunc(GL11.GL_LEQUAL);
				int func = GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC);
				float ref = GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF);
				GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0);
				double sign = 1;
				if (Minecraft.getMinecraft().player.posY+Minecraft.getMinecraft().player.getEyeHeight() < y+1.5){
					sign = -1;
				}
				b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
				for (double i = 0; i < 8; i ++){
					RenderUtil.renderAlchemyCircle(b, x+0.5, y+1.0+sign*(i/1000.0), z+0.5, 1.0f, 0.25f, 0.0625f, (tile.process + (partialTicks*processSign))/40.0f, 0.4f*(tile.process + (partialTicks*processSign))/10.0f, tile.angle+partialTicks);
				}
				tess.draw();
				GlStateManager.alphaFunc(func, ref);
				GlStateManager.depthFunc(dfunc);
				GlStateManager.disableBlend();
				GlStateManager.enableLighting();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			}

			float itemScale = 0.25f;

			if (!tile.center.getStackInSlot(0).isEmpty()){
				if (Minecraft.getMinecraft().world != null){
					GlStateManager.pushAttrib();
					GL11.glPushMatrix();
					ItemStack item = tile.center.getStackInSlot(0);
					GL11.glTranslated(x,y,z);
					if (item.getItem() instanceof ItemBlock){
						GL11.glTranslated(0.5, 15.0/16, 0.5);
						GL11.glScalef(itemScale, itemScale, itemScale);
					} else {
						GL11.glTranslated(0.5, 14.25/16, 0.5);
						GL11.glScalef(itemScale, itemScale, itemScale);
						GlStateManager.rotate(90, 1, 0, 0);
					}
					Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.FIXED);
					GL11.glPopMatrix();
					GlStateManager.popAttrib();
				}
			}
			if (!tile.north.getStackInSlot(0).isEmpty()){
				if (Minecraft.getMinecraft().world != null){
					GlStateManager.pushAttrib();
					GL11.glPushMatrix();
					ItemStack item = tile.north.getStackInSlot(0);
					GL11.glTranslated(x,y,z);
					if (item.getItem() instanceof ItemBlock){
						GL11.glTranslated(0.5, 15.0/16, 0.25);
						GL11.glScalef(itemScale, itemScale, itemScale);
					} else {
						GL11.glTranslated(0.5, 14.25/16, 0.25);
						GL11.glScalef(itemScale, itemScale, itemScale);
						GlStateManager.rotate(90, 1, 0, 0);
					}
					Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.FIXED);
					GL11.glPopMatrix();
					GlStateManager.popAttrib();
				}
			}
			if (!tile.south.getStackInSlot(0).isEmpty()){
				if (Minecraft.getMinecraft().world != null){
					GlStateManager.pushAttrib();
					GL11.glPushMatrix();
					ItemStack item = tile.south.getStackInSlot(0);
					GL11.glTranslated(x,y,z);
					if (item.getItem() instanceof ItemBlock){
						GL11.glTranslated(0.5, 15.0/16, 0.75);
						GL11.glScalef(itemScale, itemScale, itemScale);
					} else {
						GL11.glTranslated(0.5, 14.25/16, 0.75);
						GL11.glScalef(itemScale, itemScale, itemScale);
						GlStateManager.rotate(90, 1, 0, 0);
					}
					Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.FIXED);
					GL11.glPopMatrix();
					GlStateManager.popAttrib();
				}
			}
			if (!tile.west.getStackInSlot(0).isEmpty()){
				if (Minecraft.getMinecraft().world != null){
					GlStateManager.pushAttrib();
					GL11.glPushMatrix();
					ItemStack item = tile.west.getStackInSlot(0);
					GL11.glTranslated(x,y,z);
					if (item.getItem() instanceof ItemBlock){
						GL11.glTranslated(0.25, 15.0/16, 0.5);
						GL11.glScalef(itemScale, itemScale, itemScale);
					} else {
						GL11.glTranslated(0.25, 14.25/16, 0.5);
						GL11.glScalef(itemScale, itemScale, itemScale);
						GlStateManager.rotate(90, 1, 0, 0);
					}
					Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.FIXED);
					GL11.glPopMatrix();
					GlStateManager.popAttrib();
				}
			}
			if (!tile.east.getStackInSlot(0).isEmpty()){
				if (Minecraft.getMinecraft().world != null){
					GlStateManager.pushAttrib();
					GL11.glPushMatrix();
					ItemStack item = tile.east.getStackInSlot(0);
					GL11.glTranslated(x,y,z);
					if (item.getItem() instanceof ItemBlock){
						GL11.glTranslated(0.75, 15.0/16, 0.5);
						GL11.glScalef(itemScale, itemScale, itemScale);
					} else {
						GL11.glTranslated(0.75, 14.25/16, 0.5);
						GL11.glScalef(itemScale, itemScale, itemScale);
						GlStateManager.rotate(90, 1, 0, 0);
					}
					Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.FIXED);
					GL11.glPopMatrix();
					GlStateManager.popAttrib();
				}
			}
		}
	}

	@Override
	public void renderLater(TileEntity tile, double x, double y, double z, float partialTicks) {
	}
}
