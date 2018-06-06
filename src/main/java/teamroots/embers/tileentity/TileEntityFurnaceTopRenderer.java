package teamroots.embers.tileentity;

import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;

public class TileEntityFurnaceTopRenderer extends TileEntitySpecialRenderer<TileEntityFurnaceTop> {
	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	int blue, green, red, alpha;
	int lightx, lighty;
	double minU, minV, maxU, maxV, diffU, diffV;
	public TileEntityFurnaceTopRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntityFurnaceTop tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile != null){
			FluidStack fluidStack = tile.getFluidStack();
			int capacity = tile.getCapacity();
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			for (int i = 0; i < tile.inventory.getSlots(); i ++){
				ItemStack stack = tile.inventory.getStackInSlot(i);
				if (!stack.isEmpty()){
					GlStateManager.pushMatrix();
					GlStateManager.translate(x+0.5,y,z+0.5);
					GlStateManager.rotate(1.0f*((float) tile.angle+partialTicks),0,1,0);
					EntityItem item = new EntityItem(tile.getWorld(),0,0,0,stack);
					item.hoverStart = 0;
					Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, true);
					GlStateManager.popMatrix();
				}
			}
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

				minU = sprite.getMinU()+diffU*0.25;
				maxU = sprite.getMaxU()-diffU*0.25;
				minV = sprite.getMinV()+diffV*0.25;
				maxV = sprite.getMaxV()-diffV*0.25;

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
				buffer.pos(x+0.25, y+0.0625+0.8125*((float)amount/(float)capacity), z+0.25).tex(minU, minV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				buffer.pos(x+0.75, y+0.0625+0.8125*((float)amount/(float)capacity), z+0.25).tex(maxU, minV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				buffer.pos(x+0.75, y+0.0625+0.8125*((float)amount/(float)capacity), z+0.75).tex(maxU, maxV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				buffer.pos(x+0.25, y+0.0625+0.8125*((float)amount/(float)capacity), z+0.75).tex(minU, maxV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				tess.draw();

				GlStateManager.disableAlpha();
				GlStateManager.disableBlend();
				GlStateManager.enableLighting();
			}
		}
	}
}
