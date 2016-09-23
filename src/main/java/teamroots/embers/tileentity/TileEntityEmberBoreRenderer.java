package teamroots.embers.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelFluid.FluidLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.Embers;
import teamroots.embers.util.FluidTextureUtil;
import teamroots.embers.util.RenderUtil;
import teamroots.embers.util.StructBox;
import teamroots.embers.util.StructUV;

public class TileEntityEmberBoreRenderer extends TileEntitySpecialRenderer {
	public ResourceLocation texture = new ResourceLocation(Embers.MODID + ":textures/blocks/boreBlade.png");
	public int lightx = 0, lighty = 0;
	public StructBox blade = new StructBox(-0.125,-1,-1,0.125,1,1,new StructUV[]{new StructUV(0,32,32,36,64,64),new StructUV(0,32,32,36,64,64),new StructUV(32,0,36,32,64,64),new StructUV(32,0,36,32,64,64),new StructUV(0,0,32,32,64,64),new StructUV(0,0,32,32,64,64)});
	public StructBox pole = new StructBox(-0.125,0,-0.125,0.125,1,0.125,new StructUV[]{new StructUV(32,32,36,36,64,64),new StructUV(32,32,36,36,64,64),new StructUV(36,0,40,16,64,64),new StructUV(36,0,40,16,64,64),new StructUV(36,0,40,16,64,64),new StructUV(36,0,40,16,64,64)});
	
	public TileEntityEmberBoreRenderer(){
		super();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity t, double x, double y, double z, float partialTicks, int destroyStage){
		if (t instanceof TileEntityEmberBore){
			TileEntityEmberBore tile = (TileEntityEmberBore)t;
	            
            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            GlStateManager.disableCull();
            Tessellator tess = Tessellator.getInstance();
            VertexBuffer buffer = tess.getBuffer();
            
            GlStateManager.pushMatrix();
            GlStateManager.translate(x-0.5, y-0.5, z+0.5);
            GlStateManager.rotate(tile.angle+partialTicks*12.0f, 1, 0, 0);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            RenderUtil.addBox(buffer, blade.x1, blade.y1, blade.z1, blade.x2, blade.y2, blade.z2, blade.textures, new int[]{1,1,1,1,1,1});
            tess.draw();
            GlStateManager.popMatrix();
            
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y-0.5, z+0.5);
            GlStateManager.rotate(tile.angle+partialTicks*12.0f+72.0f, 1, 0, 0);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            RenderUtil.addBox(buffer, blade.x1, blade.y1, blade.z1, blade.x2, blade.y2, blade.z2, blade.textures, new int[]{1,1,1,1,1,1});
            tess.draw();
            GlStateManager.popMatrix();
            
            GlStateManager.pushMatrix();
            GlStateManager.translate(x+0.5, y-0.5, z+0.5);
            GlStateManager.rotate(tile.angle+partialTicks*12.0f+144.0f, 1, 0, 0);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            RenderUtil.addBox(buffer, blade.x1, blade.y1, blade.z1, blade.x2, blade.y2, blade.z2, blade.textures, new int[]{1,1,1,1,1,1});
            tess.draw();
            GlStateManager.popMatrix();
            
            GlStateManager.pushMatrix();
            GlStateManager.translate(x+1.0, y-0.5, z+0.5);
            GlStateManager.rotate(tile.angle+partialTicks*12.0f+216.0f, 1, 0, 0);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            RenderUtil.addBox(buffer, blade.x1, blade.y1, blade.z1, blade.x2, blade.y2, blade.z2, blade.textures, new int[]{1,1,1,1,1,1});
            tess.draw();
            GlStateManager.popMatrix();
            
            GlStateManager.pushMatrix();
            GlStateManager.translate(x+1.5, y-0.5, z+0.5);
            GlStateManager.rotate(tile.angle+partialTicks*12.0f+288.0f, 1, 0, 0);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            RenderUtil.addBox(buffer, blade.x1, blade.y1, blade.z1, blade.x2, blade.y2, blade.z2, blade.textures, new int[]{1,1,1,1,1,1});
            tess.draw();
            GlStateManager.popMatrix();
            
            GlStateManager.pushMatrix();
            GlStateManager.translate(x-0.75, y-0.625, z+0.5);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            RenderUtil.addBox(buffer, pole.x1, pole.y1, pole.z1, pole.x2, pole.y2, pole.z2, pole.textures, new int[]{1,1,1,1,1,1});
            tess.draw();
            GlStateManager.popMatrix();
            
            GlStateManager.pushMatrix();
            GlStateManager.translate(x-0.25, y-0.625, z+0.5);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            RenderUtil.addBox(buffer, pole.x1, pole.y1, pole.z1, pole.x2, pole.y2, pole.z2, pole.textures, new int[]{1,1,1,1,1,1});
            tess.draw();
            GlStateManager.popMatrix();
            
            GlStateManager.pushMatrix();
            GlStateManager.translate(x+0.25, y-0.625, z+0.5);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            RenderUtil.addBox(buffer, pole.x1, pole.y1, pole.z1, pole.x2, pole.y2, pole.z2, pole.textures, new int[]{1,1,1,1,1,1});
            tess.draw();
            GlStateManager.popMatrix();
            
            GlStateManager.pushMatrix();
            GlStateManager.translate(x+0.75, y-0.625, z+0.5);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            RenderUtil.addBox(buffer, pole.x1, pole.y1, pole.z1, pole.x2, pole.y2, pole.z2, pole.textures, new int[]{1,1,1,1,1,1});
            tess.draw();
            GlStateManager.popMatrix();
            
            GlStateManager.pushMatrix();
            GlStateManager.translate(x+1.25, y-0.625, z+0.5);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            RenderUtil.addBox(buffer, pole.x1, pole.y1, pole.z1, pole.x2, pole.y2, pole.z2, pole.textures, new int[]{1,1,1,1,1,1});
            tess.draw();
            GlStateManager.popMatrix();
            
            GlStateManager.pushMatrix();
            GlStateManager.translate(x+1.75, y-0.625, z+0.5);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            RenderUtil.addBox(buffer, pole.x1, pole.y1, pole.z1, pole.x2, pole.y2, pole.z2, pole.textures, new int[]{1,1,1,1,1,1});
            tess.draw();
            GlStateManager.popMatrix();
            
            GlStateManager.enableCull();
		}
	}
}
