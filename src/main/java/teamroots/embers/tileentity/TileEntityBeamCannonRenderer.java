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
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import teamroots.embers.Embers;
import teamroots.embers.model.ModelBeamCannon;
import teamroots.embers.model.ModelManager;
import teamroots.embers.util.Misc;
import teamroots.embers.util.RenderUtil;
import teamroots.embers.util.StructBox;
import teamroots.embers.util.StructUV;

public class TileEntityBeamCannonRenderer extends TileEntitySpecialRenderer<TileEntityBeamCannon> {
	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	Random random = new Random();
	public StructBox base = new StructBox(-2.5*(0.0625),-2.5*(0.0625),-2.5*(0.0625),2.5*(0.0625),2.5*(0.0625),2.5*(0.0625),new StructUV[]{new StructUV(0,0,5,5,16,16),new StructUV(0,0,5,5,16,16),new StructUV(0,0,5,5,16,16),new StructUV(0,0,5,5,16,16),new StructUV(0,0,5,5,16,16),new StructUV(0,0,5,5,16,16)});
	public StructBox disc1 = new StructBox(-3.0*(0.0625),2.0*(0.0625),-3.0*(0.0625),3.0*(0.0625),4.0*(0.0625),3.0*(0.0625),new StructUV[]{new StructUV(0,5,6,11,16,16),new StructUV(0,5,6,11,16,16),new StructUV(0,11,6,13,16,16),new StructUV(0,11,6,13,16,16),new StructUV(0,11,6,13,16,16),new StructUV(0,11,6,13,16,16)});
	public StructBox disc2 = new StructBox(-3.0*(0.0625),5.0*(0.0625),-3.0*(0.0625),3.0*(0.0625),7.0*(0.0625),3.0*(0.0625),new StructUV[]{new StructUV(0,5,6,11,16,16),new StructUV(0,5,6,11,16,16),new StructUV(0,11,6,13,16,16),new StructUV(0,11,6,13,16,16),new StructUV(0,11,6,13,16,16),new StructUV(0,11,6,13,16,16)});
	public StructBox disc3 = new StructBox(-3.0*(0.0625),8.0*(0.0625),-3.0*(0.0625),3.0*(0.0625),10.0*(0.0625),3.0*(0.0625),new StructUV[]{new StructUV(0,5,6,11,16,16),new StructUV(0,5,6,11,16,16),new StructUV(0,11,6,13,16,16),new StructUV(0,11,6,13,16,16),new StructUV(0,11,6,13,16,16),new StructUV(0,11,6,13,16,16)});
	public StructBox barrel = new StructBox(-2.0*(0.0625),2.0*(0.0625),-2.0*(0.0625),2.0*(0.0625),12.0*(0.0625),2.0*(0.0625),new StructUV[]{new StructUV(6,0,10,4,16,16),new StructUV(6,0,10,4,16,16),new StructUV(6,4,10,14,16,16),new StructUV(6,4,10,14,16,16),new StructUV(6,4,10,14,16,16),new StructUV(6,4,10,14,16,16)});
	
	public ResourceLocation texture = new ResourceLocation(Embers.MODID + ":textures/blocks/beam_cannon.png");
	public TileEntityBeamCannonRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntityBeamCannon tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile != null){
			float yaw = 0;
			float pitch = 0;
			if (tile.target != null){
				yaw = Misc.yawDegreesBetweenPoints(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), tile.target.getX(), tile.target.getY(), tile.target.getZ());
				pitch = Misc.pitchDegreesBetweenPoints(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), tile.target.getX(), tile.target.getY(), tile.target.getZ());
			}
            GlStateManager.pushMatrix();
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			GlStateManager.translate(x+0.5, y+0.5, z+0.5);
			GlStateManager.rotate(yaw, 0, 1, 0);
			GlStateManager.rotate(90-pitch, 1, 0, 0);
			GlStateManager.disableCull();
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			Tessellator tess = Tessellator.getInstance();
			BufferBuilder b = tess.getBuffer();
			b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
			RenderUtil.addBox(b, base.x1, base.y1, base.z1, base.x2, base.y2, base.z2, base.textures, new int[]{1,1,1,1,1,1});
			RenderUtil.addBox(b, disc1.x1, disc1.y1, disc1.z1, disc1.x2, disc1.y2, disc1.z2, disc1.textures, new int[]{1,1,1,1,1,1});
			RenderUtil.addBox(b, disc2.x1, disc2.y1, disc2.z1, disc2.x2, disc2.y2, disc2.z2, disc2.textures, new int[]{1,1,1,1,1,1});
			RenderUtil.addBox(b, disc3.x1, disc3.y1, disc3.z1, disc3.x2, disc3.y2, disc3.z2, disc3.textures, new int[]{1,1,1,1,1,1});
			RenderUtil.addBox(b, barrel.x1, barrel.y1, barrel.z1, barrel.x2, barrel.y2, barrel.z2, barrel.textures, new int[]{1,1,1,1,1,1});
			tess.draw();
			GlStateManager.popMatrix();
		}
	}
}
