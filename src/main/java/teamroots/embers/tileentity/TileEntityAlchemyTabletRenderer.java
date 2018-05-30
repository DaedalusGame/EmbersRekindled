package teamroots.embers.tileentity;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import teamroots.embers.Embers;
import teamroots.embers.EventManager;
import teamroots.embers.util.RenderUtil;

public class TileEntityAlchemyTabletRenderer extends TileEntitySpecialRenderer implements ITileEntitySpecialRendererLater {
	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	Random random = new Random();
	public ResourceLocation texture = new ResourceLocation(Embers.MODID + ":textures/entity/beam.png");
	public TileEntityAlchemyTabletRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile instanceof TileEntityAlchemyTablet){
			TileEntityAlchemyTablet tablet = (TileEntityAlchemyTablet)tile;
			
			if (tablet.process != 0){
				float processSign = (tablet.progress == 1) ? 1 : -1;
				if (tablet.process == 20){
					processSign = 0;
				}
				Tessellator tess = Tessellator.getInstance();
				BufferBuilder b = tess.getBuffer();
				Minecraft.getMinecraft().renderEngine.bindTexture(texture);
				GlStateManager.disableLighting();
				GlStateManager.enableBlend();
				GlStateManager.enableAlpha();
				GlStateManager.disableCull();
				GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
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
					RenderUtil.renderAlchemyCircle(b, x+0.5, y+1.0+sign*(i/1000.0), z+0.5, 1.0f, 0.25f, 0.0625f, (tablet.process + (partialTicks*processSign))/40.0f, 0.4f*(tablet.process + (partialTicks*processSign))/10.0f, ((TileEntityAlchemyTablet) tile).angle+partialTicks);
				}
				tess.draw();
				GlStateManager.alphaFunc(func, ref);
				GlStateManager.depthFunc(dfunc);
				GlStateManager.disableBlend();
				GlStateManager.enableLighting();
				GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			}
			
			if (tablet.center.getStackInSlot(0) != ItemStack.EMPTY){
				if (Minecraft.getMinecraft().world != null){
					GlStateManager.pushAttrib();
					GL11.glPushMatrix();
					EntityItem item = new EntityItem(Minecraft.getMinecraft().world,x,y,z,tablet.center.getStackInSlot(0));
					item.hoverStart = 0;
					item.onGround = false;
					GL11.glTranslated(x,y,z);
					GL11.glScalef(0.5f, 0.5f, 0.5f);
					if (item.getItem().getItem() instanceof ItemBlock){
						GL11.glTranslated(1.0, 1.5, 1.0); //Decrease X by 0.5 to go one slot towards positive X, Decrease Z by 0.5 to go one slot towards positive Z
					}
					else {
						GL11.glTranslated(1.0, 1.75, 0.5); //Decrease X by 0.5 to go one slot towards positive X, Decrease Z by 0.5 to go one slot towards positive Z
						GlStateManager.rotate(90, 1, 0, 0);
					}
					Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, false);
					GL11.glPopMatrix();
					GlStateManager.popAttrib();
				}
			}
			if (tablet.north.getStackInSlot(0) != ItemStack.EMPTY){
				if (Minecraft.getMinecraft().world != null){
					GlStateManager.pushAttrib();
					GL11.glPushMatrix();
					EntityItem item = new EntityItem(Minecraft.getMinecraft().world,x,y,z,tablet.north.getStackInSlot(0));
					item.hoverStart = 0;
					item.onGround = false;
					GL11.glTranslated(x,y,z);
					GL11.glScalef(0.5f, 0.5f, 0.5f);
					if (item.getItem().getItem() instanceof ItemBlock){
						GL11.glTranslated(1.0, 1.5, 0.5); //Decrease X by 0.5 to go one slot towards positive X, Decrease Z by 0.5 to go one slot towards positive Z
					}
					else {
						GL11.glTranslated(1.0, 1.75, 0); //Decrease X by 0.5 to go one slot towards positive X, Decrease Z by 0.5 to go one slot towards positive Z
						GlStateManager.rotate(90, 1, 0, 0);
					}
					Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, false);
					GL11.glPopMatrix();
					GlStateManager.popAttrib();
				}
			}
			if (tablet.south.getStackInSlot(0) != ItemStack.EMPTY){
				if (Minecraft.getMinecraft().world != null){
					GlStateManager.pushAttrib();
					GL11.glPushMatrix();
					EntityItem item = new EntityItem(Minecraft.getMinecraft().world,x,y,z,tablet.south.getStackInSlot(0));
					item.hoverStart = 0;
					item.onGround = false;
					GL11.glTranslated(x,y,z);
					GL11.glScalef(0.5f, 0.5f, 0.5f);
					if (item.getItem().getItem() instanceof ItemBlock){
						GL11.glTranslated(1.0, 1.5, 1.5); //Decrease X by 0.5 to go one slot towards positive X, Decrease Z by 0.5 to go one slot towards positive Z
					}
					else {
						GL11.glTranslated(1.0, 1.75, 1.0); //Decrease X by 0.5 to go one slot towards positive X, Decrease Z by 0.5 to go one slot towards positive Z
						GlStateManager.rotate(90, 1, 0, 0);
					}
					Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, false);
					GL11.glPopMatrix();
					GlStateManager.popAttrib();
				}
			}
			if (tablet.west.getStackInSlot(0) != ItemStack.EMPTY){
				if (Minecraft.getMinecraft().world != null){
					GlStateManager.pushAttrib();
					GL11.glPushMatrix();
					EntityItem item = new EntityItem(Minecraft.getMinecraft().world,x,y,z,tablet.west.getStackInSlot(0));
					item.hoverStart = 0;
					item.onGround = false;
					GL11.glTranslated(x,y,z);
					GL11.glScalef(0.5f, 0.5f, 0.5f);
					if (item.getItem().getItem() instanceof ItemBlock){
						GL11.glTranslated(0.5, 1.5, 1.0); //Decrease X by 0.5 to go one slot towards positive X, Decrease Z by 0.5 to go one slot towards positive Z
					}
					else {
						GL11.glTranslated(0.5, 1.75, 0.5); //Decrease X by 0.5 to go one slot towards positive X, Decrease Z by 0.5 to go one slot towards positive Z
						GlStateManager.rotate(90, 1, 0, 0);
					}
					Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, false);
					GL11.glPopMatrix();
					GlStateManager.popAttrib();
				}
			}
			if (tablet.east.getStackInSlot(0) != ItemStack.EMPTY){
				if (Minecraft.getMinecraft().world != null){
					GlStateManager.pushAttrib();
					GL11.glPushMatrix();
					EntityItem item = new EntityItem(Minecraft.getMinecraft().world,x,y,z,tablet.east.getStackInSlot(0));
					item.hoverStart = 0;
					item.onGround = false;
					GL11.glTranslated(x,y,z);
					GL11.glScalef(0.5f, 0.5f, 0.5f);
					if (item.getItem().getItem() instanceof ItemBlock){
						GL11.glTranslated(1.5, 1.5, 1.0); //Decrease X by 0.5 to go one slot towards positive X, Decrease Z by 0.5 to go one slot towards positive Z
					}
					else {
						GL11.glTranslated(1.5, 1.75, 0.5); //Decrease X by 0.5 to go one slot towards positive X, Decrease Z by 0.5 to go one slot towards positive Z
						GlStateManager.rotate(90, 1, 0, 0);
					}
					Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, false);
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
