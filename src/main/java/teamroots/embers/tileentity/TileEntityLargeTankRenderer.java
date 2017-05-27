package teamroots.embers.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import teamroots.embers.util.FluidTextureUtil;

public class TileEntityLargeTankRenderer extends TileEntitySpecialRenderer {
	int blue, green, red, alpha;
	int lightx, lighty;
	double minU, minV, maxU, maxV, diffU, diffV;
	public TileEntityLargeTankRenderer(){
		super();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage){
		if (tile instanceof TileEntityLargeTank){
			TileEntityLargeTank tank = (TileEntityLargeTank)tile;
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			int amount = tank.getAmount();
			int capacity = tank.getCapacity();
			Fluid fluid = tank.getFluid();
			if (fluid != null){
				int c = fluid.getColor();
	            blue = c & 0xFF;
	            green = (c >> 8) & 0xFF;
	            red = (c >> 16) & 0xFF;
	            alpha = (c >> 24) & 0xFF;
	            
	            TextureAtlasSprite sprite = FluidTextureUtil.stillTextures.get(fluid);
	            diffU = maxU-minU;
	            diffV = maxV-minV;
	            
	            minU = sprite.getMinU();
	            maxU = sprite.getMaxU();
	            minV = sprite.getMinV();
	            maxV = sprite.getMaxV();
	            
	            int i = getWorld().getCombinedLight(tile.getPos(), fluid.getLuminosity());
	            lightx = i >> 0x10 & 0xFFFF;
	            lighty = i & 0xFFFF;
	            
	            GlStateManager.disableCull();
	            GlStateManager.disableLighting();
	            GlStateManager.enableBlend();
	            GlStateManager.enableAlpha();
	            
	            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	            Tessellator tess = Tessellator.getInstance();
	            VertexBuffer buffer = tess.getBuffer();
	            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
	            buffer.pos(x-0.5, y+0.875+1.0*((float)amount/40000), z-0.5).tex(minU, minV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				buffer.pos(x+0.5, y+0.875+1.0*((float)amount/40000), z-0.5).tex(maxU, minV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				buffer.pos(x+0.5, y+0.875+1.0*((float)amount/40000), z+0.5).tex(maxU, maxV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				buffer.pos(x-0.5, y+0.875+1.0*((float)amount/40000), z+0.5).tex(minU, maxV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				buffer.pos(x+0.5, y+0.875+1.0*((float)amount/40000), z-0.5).tex(minU, minV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				buffer.pos(x+1.5, y+0.875+1.0*((float)amount/40000), z-0.5).tex(maxU, minV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				buffer.pos(x+1.5, y+0.875+1.0*((float)amount/40000), z+0.5).tex(maxU, maxV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				buffer.pos(x+0.5, y+0.875+1.0*((float)amount/40000), z+0.5).tex(minU, maxV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				buffer.pos(x+0.5, y+0.875+1.0*((float)amount/40000), z+0.5).tex(minU, minV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				buffer.pos(x+1.5, y+0.875+1.0*((float)amount/40000), z+0.5).tex(maxU, minV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				buffer.pos(x+1.5, y+0.875+1.0*((float)amount/40000), z+1.5).tex(maxU, maxV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				buffer.pos(x+0.5, y+0.875+1.0*((float)amount/40000), z+1.5).tex(minU, maxV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				buffer.pos(x-0.5, y+0.875+1.0*((float)amount/40000), z+0.5).tex(minU, minV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				buffer.pos(x+0.5, y+0.875+1.0*((float)amount/40000), z+0.5).tex(maxU, minV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				buffer.pos(x+0.5, y+0.875+1.0*((float)amount/40000), z+1.5).tex(maxU, maxV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				buffer.pos(x-0.5, y+0.875+1.0*((float)amount/40000), z+1.5).tex(minU, maxV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				tess.draw();
				
				GlStateManager.disableBlend();
				GlStateManager.enableLighting();
			}
		}
	}
}
