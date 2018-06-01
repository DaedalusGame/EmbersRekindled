package teamroots.embers.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import teamroots.embers.Embers;
import teamroots.embers.util.RenderUtil;
import teamroots.embers.util.StructBox;
import teamroots.embers.util.StructUV;

public class TileEntityInfernoForgeOpeningRenderer extends TileEntitySpecialRenderer<TileEntityInfernoForgeOpening> {
	public ResourceLocation texture = new ResourceLocation(Embers.MODID + ":textures/blocks/forge_opening.png");
	public int lightx = 0, lighty = 0;
	public StructBox left = new StructBox(0.0, 0.625, 0.0, 0.5, 0.75, 1.0,new StructUV[]{new StructUV(0,0,16,8,16,16),new StructUV(0,8,16,0,16,16),new StructUV(0,14,8,16,16,16),new StructUV(0,14,8,16,16,16),new StructUV(0,0,16,2,16,16),new StructUV(0,0,16,2,16,16)});
	public StructBox right = new StructBox(1.0, 0.75, 0.0, 0.5, 0.625, 1.0,new StructUV[]{new StructUV(0,8,16,16,16,16),new StructUV(0,16,16,8,16,16),new StructUV(0,14,8,16,16,16),new StructUV(0,14,8,16,16,16),new StructUV(0,0,16,2,16,16),new StructUV(0,0,16,2,16,16)});
	public TileEntityInfernoForgeOpeningRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntityInfernoForgeOpening tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile != null){
			float dx = 0.45f* tile.openAmount;
	            
            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            GlStateManager.disableCull();
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buffer = tess.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            RenderUtil.addBox(buffer, left.x1+x-dx, left.y1+y, left.z1+z, left.x2+x-dx, left.y2+y, left.z2+z, left.textures, new int[]{1,1,1,1,1,1});
            RenderUtil.addBox(buffer, right.x1+x+dx, right.y1+y, right.z1+z, right.x2+x+dx, right.y2+y, right.z2+z, right.textures, new int[]{-1,-1,1,1,1,1});
            tess.draw();
        }
	}
}
