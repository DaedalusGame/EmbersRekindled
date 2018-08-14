package teamroots.embers.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class TileEntityLargeTankRenderer extends TileEntitySpecialRenderer<TileEntityLargeTank> {
	int blue, green, red, alpha;
	int lightx, lighty;
	double minU, minV, maxU, maxV, diffU, diffV;
	public TileEntityLargeTankRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntityLargeTank tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile != null){
			GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			FluidStack fluidStack = tile.getFluidStack();
			int capacity = tile.getCapacity();
			if (fluidStack != null){
				Fluid fluid = fluidStack.getFluid();
				int amount = fluidStack.amount;
				int c = fluid.getColor(fluidStack);
	            blue = c & 0xFF;
	            green = (c >> 8) & 0xFF;
	            red = (c >> 16) & 0xFF;
	            alpha = (c >> 24) & 0xFF;
	            
	            TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getStill(fluidStack).toString());
	            diffU = maxU-minU;
	            diffV = maxV-minV;

				minU = sprite.getMinU();
				maxU = sprite.getMaxU();
				minV = sprite.getMinV();
				maxV = sprite.getMaxV();

				int i = getWorld().getCombinedLight(tile.getPos(), fluid.getLuminosity(fluidStack));
				lightx = i >> 0x10 & 0xFFFF;
				lighty = i & 0xFFFF;

				GlStateManager.disableCull();
				GlStateManager.disableLighting();
				GlStateManager.enableBlend();
				GlStateManager.enableAlpha();

				Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				Tessellator tess = Tessellator.getInstance();
				BufferBuilder buffer = tess.getBuffer();
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
