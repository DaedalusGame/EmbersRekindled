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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import teamroots.embers.Embers;

public class TileEntitySeedRenderer extends TileEntitySpecialRenderer {
	public ResourceLocation textureIron = new ResourceLocation(Embers.MODID + ":textures/blocks/material_iron.png");
	public ResourceLocation textureGold = new ResourceLocation(Embers.MODID + ":textures/blocks/material_gold.png");
	public ResourceLocation textureCopper = new ResourceLocation(Embers.MODID + ":textures/blocks/material_copper.png");
	public ResourceLocation textureLead = new ResourceLocation(Embers.MODID + ":textures/blocks/material_lead.png");
	public ResourceLocation textureSilver = new ResourceLocation(Embers.MODID + ":textures/blocks/material_silver.png");
	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	Random random = new Random();
	public TileEntitySeedRenderer(){
		super();
	}
	
	public void drawCrystal(BufferBuilder b, float x, float y, float z, float rotation, float size, float minU, float minV, float maxU, float maxV){
		float offX1 = size * 0.25f * (float)Math.sin(Math.toRadians(rotation));
		float offZ1 = size * 0.25f * (float)Math.cos(Math.toRadians(rotation));
		float offX2 = size * 0.25f * (float)Math.sin(Math.toRadians(rotation+90.0f));
		float offZ2 = size * 0.25f * (float)Math.cos(Math.toRadians(rotation+90.0f));
		float pos1X = x;
		double pos1Y = y-size*0.5f;
		double pos1Z = z;
		double pos2X = x+offX1;
		double pos2Y = y;
		double pos2Z = z+offZ1;
		double pos3X = x+offX2;
		double pos3Y = y;
		double pos3Z = z+offZ2;
		double pos4X = x-offX1;
		double pos4Y = y;
		double pos4Z = z-offZ1;
		double pos5X = x-offX2;
		double pos5Y = y;
		double pos5Z = z-offZ2;
		double pos6X = x;
		double pos6Y = y+size*0.5f;
		double pos6Z = z;
		Vec3d diff1 = new Vec3d(pos3X-pos1X,pos3Y-pos1Y,pos3Z-pos1Z);
		Vec3d diff2 = new Vec3d(pos2X-pos1X,pos2Y-pos1Y,pos2Z-pos1Z);
		Vec3d normal1 = new Vec3d(diff1.y*diff2.z-diff1.z*diff2.y,diff1.x*diff2.z-diff1.z*diff2.x,diff1.x*diff2.y-diff1.y*diff2.x).normalize().scale(-1.0);
		Vec3d normal2 = new Vec3d(normal1.z,-normal1.y,normal1.x).scale(-1.0);
		Vec3d normal3 = new Vec3d(-normal1.x,-normal1.y,-normal1.z).scale(-1.0);
		Vec3d normal4 = new Vec3d(-normal1.z,-normal1.y,-normal1.x).scale(-1.0);
		Vec3d normal5 = new Vec3d(normal1.x,normal1.y,normal1.z).scale(-1.0);
		Vec3d normal6 = new Vec3d(normal1.z,normal1.y,normal1.x).scale(-1.0);
		Vec3d normal7 = new Vec3d(-normal1.x,normal1.y,-normal1.z).scale(-1.0);
		Vec3d normal8 = new Vec3d(-normal1.z,normal1.y,-normal1.x).scale(-1.0);
		normal1 = new Vec3d(normal1.x,-normal1.y,normal1.z).scale(-1.0); 
		b.pos(pos1X, pos1Y, pos1Z).tex((minU+maxU)/2.0, maxV).color(255, 255, 255, 255).normal((float)normal1.x,(float)normal1.y,(float)normal1.z).endVertex();
		b.pos(pos2X, pos2Y, pos2Z).tex(minU, minV).color(255, 255, 255, 255).normal((float)normal1.x,(float)normal1.y,(float)normal1.z).endVertex();
		b.pos(pos3X, pos3Y, pos3Z).tex(maxU, minV).color(255, 255, 255, 255).normal((float)normal1.x,(float)normal1.y,(float)normal1.z).endVertex();
	
		b.pos(pos1X, pos1Y, pos1Z).tex((minU+maxU)/2.0, maxV).color(255, 255, 255, 255).normal((float)normal2.x,(float)normal2.y,(float)normal2.z).endVertex();
		b.pos(pos3X, pos3Y, pos3Z).tex(minU, minV).color(255, 255, 255, 255).normal((float)normal2.x,(float)normal2.y,(float)normal2.z).endVertex();
		b.pos(pos4X, pos4Y, pos4Z).tex(maxU, minV).color(255, 255, 255, 255).normal((float)normal2.x,(float)normal2.y,(float)normal2.z).endVertex();
	
		b.pos(pos1X, pos1Y, pos1Z).tex((minU+maxU)/2.0, maxV).color(255, 255, 255, 255).normal((float)normal3.x,(float)normal3.y,(float)normal3.z).endVertex();
		b.pos(pos4X, pos4Y, pos4Z).tex(minU, minV).color(255, 255, 255, 255).normal((float)normal3.x,(float)normal3.y,(float)normal3.z).endVertex();
		b.pos(pos5X, pos5Y, pos5Z).tex(maxU, minV).color(255, 255, 255, 255).normal((float)normal3.x,(float)normal3.y,(float)normal3.z).endVertex();
	
		b.pos(pos1X, pos1Y, pos1Z).tex((minU+maxU)/2.0, maxV).color(255, 255, 255, 255).normal((float)normal4.x,(float)normal4.y,(float)normal4.z).endVertex();
		b.pos(pos5X, pos5Y, pos5Z).tex(minU, minV).color(255, 255, 255, 255).normal((float)normal4.x,(float)normal4.y,(float)normal4.z).endVertex();
		b.pos(pos2X, pos2Y, pos2Z).tex(maxU, minV).color(255, 255, 255, 255).normal((float)normal4.x,(float)normal4.y,(float)normal4.z).endVertex();
	
		b.pos(pos6X, pos6Y, pos6Z).tex((minU+maxU)/2.0, minV).color(255, 255, 255, 255).normal((float)normal5.x,(float)normal5.y,(float)normal5.z).endVertex();
		b.pos(pos2X, pos2Y, pos2Z).tex(minU, maxV).color(255, 255, 255, 255).normal((float)normal5.x,(float)normal5.y,(float)normal5.z).endVertex();
		b.pos(pos3X, pos3Y, pos3Z).tex(maxU, maxV).color(255, 255, 255, 255).normal((float)normal5.x,(float)normal5.y,(float)normal5.z).endVertex();
	
		b.pos(pos6X, pos6Y, pos6Z).tex((minU+maxU)/2.0, minV).color(255, 255, 255, 255).normal((float)normal6.x,(float)normal6.y,(float)normal6.z).endVertex();
		b.pos(pos3X, pos3Y, pos3Z).tex(minU, maxV).color(255, 255, 255, 255).normal((float)normal6.x,(float)normal6.y,(float)normal6.z).endVertex();
		b.pos(pos4X, pos4Y, pos4Z).tex(maxU, maxV).color(255, 255, 255, 255).normal((float)normal6.x,(float)normal6.y,(float)normal6.z).endVertex();
	
		b.pos(pos6X, pos6Y, pos6Z).tex((minU+maxU)/2.0, minV).color(255, 255, 255, 255).normal((float)normal7.x,(float)normal7.y,(float)normal7.z).endVertex();
		b.pos(pos4X, pos4Y, pos4Z).tex(minU, maxV).color(255, 255, 255, 255).normal((float)normal7.x,(float)normal7.y,(float)normal7.z).endVertex();
		b.pos(pos5X, pos5Y, pos5Z).tex(maxU, maxV).color(255, 255, 255, 255).normal((float)normal7.x,(float)normal7.y,(float)normal7.z).endVertex();
	
		b.pos(pos6X, pos6Y, pos6Z).tex((minU+maxU)/2.0, minV).color(255, 255, 255, 255).normal((float)normal8.x,(float)normal8.y,(float)normal8.z).endVertex();
		b.pos(pos5X, pos5Y, pos5Z).tex(minU, maxV).color(255, 255, 255, 255).normal((float)normal8.x,(float)normal8.y,(float)normal8.z).endVertex();
		b.pos(pos2X, pos2Y, pos2Z).tex(maxU, maxV).color(255, 255, 255, 255).normal((float)normal8.x,(float)normal8.y,(float)normal8.z).endVertex();
	}
	
	@Override
	public void render(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile instanceof TileEntitySeed){
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			TileEntitySeed seed = (TileEntitySeed)tile;

            Minecraft.getMinecraft().renderEngine.bindTexture(textureIron);
            if (seed.material == 0){
                Minecraft.getMinecraft().renderEngine.bindTexture(textureIron);
            }
            if (seed.material == 1){
                Minecraft.getMinecraft().renderEngine.bindTexture(textureGold);
            }
            if (seed.material == 2){
                Minecraft.getMinecraft().renderEngine.bindTexture(textureCopper);
            }
            if (seed.material == 3){
                Minecraft.getMinecraft().renderEngine.bindTexture(textureLead);
            }
            if (seed.material == 4){
                Minecraft.getMinecraft().renderEngine.bindTexture(textureSilver);
            }
            GlStateManager.disableCull();
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buffer = tess.getBuffer();
            GlStateManager.pushMatrix();
            GlStateManager.translate(x+0.5f, y+0.5f, z+0.5f);
            buffer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
    		GlStateManager.rotate(15.0f*(float)Math.sin(Math.toRadians(seed.ticksExisted+partialTicks)), 1, 0, 0);
            this.drawCrystal(buffer, 0, 0, 0, ((float)seed.ticksExisted+partialTicks)*6.0f, 1.0f, 0.25f, 0.0f, 0.75f, 1.0f);
    		tess.draw();
            GlStateManager.rotate(-15.0f*(float)Math.sin(Math.toRadians(seed.ticksExisted+partialTicks)), 1, 0, 0);
            GlStateManager.rotate(-15.0f*(float)Math.sin(Math.toRadians(2.5f*(seed.ticksExisted+partialTicks))), 1, 0, 0);
            buffer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            for (int i = 0; i < 12; i += 1){
            	if (seed.willSpawn[i]){
	            	float offX = 0.4f * (float)Math.sin(Math.toRadians(30*i+((float)seed.ticksExisted+partialTicks)*2.0f));
	            	float offZ = 0.4f * (float)Math.cos(Math.toRadians(30*i+((float)seed.ticksExisted+partialTicks)*2.0f));
	            	this.drawCrystal(buffer, offX, 0, offZ, ((float)seed.ticksExisted+partialTicks)*2.0f, 0.4f*((float)seed.size/1000.0f), 0.0f, 0.0f, 0.125f, 0.25f);
            	}
            }
    		tess.draw();
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
		}
	}
}
