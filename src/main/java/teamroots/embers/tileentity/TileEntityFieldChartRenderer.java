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
import teamroots.embers.util.EmberGenUtil;
import teamroots.embers.util.RenderUtil;
import teamroots.embers.util.StructBox;
import teamroots.embers.util.StructUV;

public class TileEntityFieldChartRenderer extends TileEntitySpecialRenderer<TileEntityFieldChart> {
	public ResourceLocation texture = new ResourceLocation(Embers.MODID + ":textures/blocks/field_square.png");
	public TileEntityFieldChartRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntityFieldChart tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile != null){
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			GlStateManager.disableCull();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
            GlStateManager.disableLighting();
            GlStateManager.enableAlpha();
			int dfunc = GL11.glGetInteger(GL11.GL_DEPTH_FUNC);
			GlStateManager.depthFunc(GL11.GL_LEQUAL);
			int func = GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC);
			float ref = GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF);
			GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0);
            Tessellator tess = Tessellator.getInstance();
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            BufferBuilder buffer = tess.getBuffer();
            GlStateManager.depthMask(false);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
            for (float i = -160; i < 160; i += 32){
            	for (float j = -160; j < 160; j += 32){
            		float amountul = EmberGenUtil.getEmberDensity(tile.getWorld().getSeed(), tile.getPos().getX()+(int)i/2, tile.getPos().getZ()+(int)j/2);
            		float amountur = EmberGenUtil.getEmberDensity(tile.getWorld().getSeed(), tile.getPos().getX()+(int)i/2+16, tile.getPos().getZ()+(int)j/2);
            		float amountdr = EmberGenUtil.getEmberDensity(tile.getWorld().getSeed(), tile.getPos().getX()+(int)i/2+16, tile.getPos().getZ()+(int)j/2+16);
            		float amountdl = EmberGenUtil.getEmberDensity(tile.getWorld().getSeed(), tile.getPos().getX()+(int)i/2, tile.getPos().getZ()+(int)j/2+16);
            		float alphaul = Math.min(1.0f,Math.max(0.0f,1.0f-Math.max(Math.abs(i), Math.abs(j))/160f));
            		float alphaur = Math.min(1.0f,Math.max(0.0f,1.0f-Math.max(Math.abs(i+32f), Math.abs(j))/160f));
            		float alphadr = Math.min(1.0f,Math.max(0.0f,1.0f-Math.max(Math.abs(i+32f), Math.abs(j+32f))/160f));
            		float alphadl = Math.min(1.0f,Math.max(0.0f,1.0f-Math.max(Math.abs(i), Math.abs(j+32f))/160f));
            		buffer.pos(x+0.5f+1.25f*(i/160f), y+0.5f+amountul*0.25f, z+0.5f+1.25f*(j/160f)).tex(0, 0).lightmap(RenderUtil.lightx, RenderUtil.lighty).color(1.0f, 0.25f, 0.0625f, alphaul).endVertex();
            		buffer.pos(x+0.5f+1.25f*(i/160f)+0.25f, y+0.5f+amountur*0.25f, z+0.5f+1.25f*(j/160f)).tex(1.0, 0).lightmap(RenderUtil.lightx, RenderUtil.lighty).color(1.0f, 0.25f, 0.0625f, alphaur).endVertex();
            		buffer.pos(x+0.5f+1.25f*(i/160f)+0.25f, y+0.5f+amountdr*0.25f, z+0.5f+1.25f*(j/160f)+0.25f).tex(1.0, 1.0).lightmap(RenderUtil.lightx, RenderUtil.lighty).color(1.0f, 0.25f, 0.0625f, alphadr).endVertex();
            		buffer.pos(x+0.5f+1.25f*(i/160f), y+0.5f+amountdl*0.25f, z+0.5f+1.25f*(j/160f)+0.25f).tex(0, 1.0).lightmap(RenderUtil.lightx, RenderUtil.lighty).color(1.0f, 0.25f, 0.0625f, alphadl).endVertex();
                }
            }
            /*for (float i = -160; i < 160; i += 32){
            	for (float j = -160; j < 160; j += 32){
            		float amountul = EmberGenUtil.getEmberDensity(tile.getWorld().getSeed(), tile.getPos().getX()+(int)i/2, tile.getPos().getZ()+(int)j/2);
            		float amountur = EmberGenUtil.getEmberDensity(tile.getWorld().getSeed(), tile.getPos().getX()+(int)i/2+16, tile.getPos().getZ()+(int)j/2);
            		float amountdr = EmberGenUtil.getEmberDensity(tile.getWorld().getSeed(), tile.getPos().getX()+(int)i/2+16, tile.getPos().getZ()+(int)j/2+16);
            		float amountdl = EmberGenUtil.getEmberDensity(tile.getWorld().getSeed(), tile.getPos().getX()+(int)i/2, tile.getPos().getZ()+(int)j/2+16);
            		float alphaul = Math.min(1.0f,Math.max(0.0f,1.0f-Math.max(Math.abs(i), Math.abs(j))/160f))*amountul;
            		float alphaur = Math.min(1.0f,Math.max(0.0f,1.0f-Math.max(Math.abs(i+32f), Math.abs(j))/160f))*amountur;
            		float alphadr = Math.min(1.0f,Math.max(0.0f,1.0f-Math.max(Math.abs(i+32f), Math.abs(j+32f))/160f))*amountdr;
            		float alphadl = Math.min(1.0f,Math.max(0.0f,1.0f-Math.max(Math.abs(i), Math.abs(j+32f))/160f))*amountdl;
            		buffer.pos(x+0.5f+1.25f*(i/160f), y+0.5f+amountul*0.25f, z+0.5f+1.25f*(j/160f)).tex(0, 0).lightmap(RenderUtil.lightx, RenderUtil.lighty).color(1.0f, 0.5f, 0.0625f, alphaul).endVertex();
            		buffer.pos(x+0.5f+1.25f*(i/160f)+0.25f, y+0.5f+amountur*0.25f, z+0.5f+1.25f*(j/160f)).tex(1.0, 0).lightmap(RenderUtil.lightx, RenderUtil.lighty).color(1.0f, 0.5f, 0.0625f, alphaur).endVertex();
            		buffer.pos(x+0.5f+1.25f*(i/160f)+0.25f, y+0.5f+amountdr*0.25f, z+0.5f+1.25f*(j/160f)+0.25f).tex(1.0, 1.0).lightmap(RenderUtil.lightx, RenderUtil.lighty).color(1.0f, 0.5f, 0.0625f, alphadr).endVertex();
            		buffer.pos(x+0.5f+1.25f*(i/160f), y+0.5f+amountdl*0.25f, z+0.5f+1.25f*(j/160f)+0.25f).tex(0, 1.0).lightmap(RenderUtil.lightx, RenderUtil.lighty).color(1.0f, 0.5f, 0.0625f, alphadl).endVertex();
                }
            }*/
            for (float i = -160; i < 160; i += 32){
            	for (float j = -160; j < 160; j += 32){
            		float amountul = EmberGenUtil.getEmberDensity(tile.getWorld().getSeed(), tile.getPos().getX()+(int)i/2, tile.getPos().getZ()+(int)j/2);
            		float amountur = EmberGenUtil.getEmberDensity(tile.getWorld().getSeed(), tile.getPos().getX()+(int)i/2+16, tile.getPos().getZ()+(int)j/2);
            		float amountdr = EmberGenUtil.getEmberDensity(tile.getWorld().getSeed(), tile.getPos().getX()+(int)i/2+16, tile.getPos().getZ()+(int)j/2+16);
            		float amountdl = EmberGenUtil.getEmberDensity(tile.getWorld().getSeed(), tile.getPos().getX()+(int)i/2, tile.getPos().getZ()+(int)j/2+16);
            		float alphaul = Math.min(1.0f,Math.max(0.0f,1.0f-Math.max(Math.abs(i), Math.abs(j))/160f)*amountul*amountul);
            		float alphaur = Math.min(1.0f,Math.max(0.0f,1.0f-Math.max(Math.abs(i+32f), Math.abs(j))/160f)*amountur*amountur);
            		float alphadr = Math.min(1.0f,Math.max(0.0f,1.0f-Math.max(Math.abs(i+32f), Math.abs(j+32f))/160f)*amountdr*amountdr);
            		float alphadl = Math.min(1.0f,Math.max(0.0f,1.0f-Math.max(Math.abs(i), Math.abs(j+32f))/160f)*amountdl*amountdl);
            		buffer.pos(x+0.5f+1.25f*(i/160f), y+0.5f+amountul*0.25f, z+0.5f+1.25f*(j/160f)).tex(0, 0).lightmap(RenderUtil.lightx, RenderUtil.lighty).color(1.0f, 0.75f, 0.0625f, 0.875f*alphaul).endVertex();
            		buffer.pos(x+0.5f+1.25f*(i/160f)+0.25f, y+0.5f+amountur*0.25f, z+0.5f+1.25f*(j/160f)).tex(1.0, 0).lightmap(RenderUtil.lightx, RenderUtil.lighty).color(1.0f, 0.75f, 0.0625f, 0.875f*alphaur).endVertex();
            		buffer.pos(x+0.5f+1.25f*(i/160f)+0.25f, y+0.5f+amountdr*0.25f, z+0.5f+1.25f*(j/160f)+0.25f).tex(1.0, 1.0).lightmap(RenderUtil.lightx, RenderUtil.lighty).color(1.0f, 0.75f, 0.0625f, 0.875f*alphadr).endVertex();
            		buffer.pos(x+0.5f+1.25f*(i/160f), y+0.5f+amountdl*0.25f, z+0.5f+1.25f*(j/160f)+0.25f).tex(0, 1.0).lightmap(RenderUtil.lightx, RenderUtil.lighty).color(1.0f, 0.75f, 0.0625f, 0.875f*alphadl).endVertex();
                }
            }
            for (float i = -160; i < 160; i += 32){
            	for (float j = -160; j < 160; j += 32){
            		float amountul = EmberGenUtil.getEmberDensity(tile.getWorld().getSeed(), tile.getPos().getX()+(int)i/2, tile.getPos().getZ()+(int)j/2);
            		float amountur = EmberGenUtil.getEmberDensity(tile.getWorld().getSeed(), tile.getPos().getX()+(int)i/2+16, tile.getPos().getZ()+(int)j/2);
            		float amountdr = EmberGenUtil.getEmberDensity(tile.getWorld().getSeed(), tile.getPos().getX()+(int)i/2+16, tile.getPos().getZ()+(int)j/2+16);
            		float amountdl = EmberGenUtil.getEmberDensity(tile.getWorld().getSeed(), tile.getPos().getX()+(int)i/2, tile.getPos().getZ()+(int)j/2+16);
            		float alphaul = Math.min(1.0f,Math.max(0.0f,1.0f-Math.max(Math.abs(i), Math.abs(j))/160f)*amountul*amountul*amountul);
            		float alphaur = Math.min(1.0f,Math.max(0.0f,1.0f-Math.max(Math.abs(i+32f), Math.abs(j))/160f)*amountur*amountur*amountur);
            		float alphadr = Math.min(1.0f,Math.max(0.0f,1.0f-Math.max(Math.abs(i+32f), Math.abs(j+32f))/160f)*amountdr*amountdr*amountdr);
            		float alphadl = Math.min(1.0f,Math.max(0.0f,1.0f-Math.max(Math.abs(i), Math.abs(j+32f))/160f)*amountdl*amountdl*amountdl);
            		buffer.pos(x+0.5f+1.25f*(i/160f), y+0.5f+amountul*0.25f, z+0.5f+1.25f*(j/160f)).tex(0, 0).lightmap(RenderUtil.lightx, RenderUtil.lighty).color(1.0f, 1.0f, 0.0625f, alphaul).endVertex();
            		buffer.pos(x+0.5f+1.25f*(i/160f)+0.25f, y+0.5f+amountur*0.25f, z+0.5f+1.25f*(j/160f)).tex(1.0, 0).lightmap(RenderUtil.lightx, RenderUtil.lighty).color(1.0f, 1.0f, 0.1875f, alphaur).endVertex();
            		buffer.pos(x+0.5f+1.25f*(i/160f)+0.25f, y+0.5f+amountdr*0.25f, z+0.5f+1.25f*(j/160f)+0.25f).tex(1.0, 1.0).lightmap(RenderUtil.lightx, RenderUtil.lighty).color(1.0f, 1.0f, 0.1875f, alphadr).endVertex();
            		buffer.pos(x+0.5f+1.25f*(i/160f), y+0.5f+amountdl*0.25f, z+0.5f+1.25f*(j/160f)+0.25f).tex(0, 1.0).lightmap(RenderUtil.lightx, RenderUtil.lighty).color(1.0f, 1.0f, 0.1875f, alphadl).endVertex();
                }
            }
            tess.draw();
            GlStateManager.depthMask(true);
            GlStateManager.shadeModel(GL11.GL_FLAT);
            GlStateManager.alphaFunc(func, ref);
			GlStateManager.depthFunc(dfunc);
            GlStateManager.enableLighting();
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableBlend();
        }
	}
}
