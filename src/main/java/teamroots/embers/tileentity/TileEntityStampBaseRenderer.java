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
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.ModelFluid.FluidLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.util.FluidTextureUtil;

public class TileEntityStampBaseRenderer extends TileEntitySpecialRenderer {
	int blue, green, red, alpha;
	int lightx, lighty;
	double minU, minV, maxU, maxV, diffU, diffV;
	public TileEntityStampBaseRenderer(){
		super();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage){
		if (tile instanceof TileEntityStampBase){
			TileEntityStampBase tank = (TileEntityStampBase)tile;
			int amount = tank.getAmount();
			int capacity = tank.getCapacity();
			Fluid fluid = tank.getFluid();
			if (tank.inputs.getStackInSlot(0) != null){
				GL11.glPushMatrix();
				EntityItem item = new EntityItem(Minecraft.getMinecraft().theWorld,x,y,z,new ItemStack(tank.inputs.getStackInSlot(0).getItem(),1,tank.inputs.getStackInSlot(0).getMetadata()));
				item.hoverStart = 0;
				GL11.glTranslated(x, y+0.9, z);
				GL11.glTranslated(0.5, 0, 0.5);
				GL11.glTranslated(-0.5, 0, -0.5);
				if (!(tank.inputs.getStackInSlot(0).getItem() instanceof ItemBlock)){
					GL11.glRotated(90, 1, 0, 0);
					GL11.glTranslated(0.5, 0, 0);
				}
				else {
					GL11.glTranslated(0.5, -0.375, 0.5);
				}
				GL11.glScaled(1.0, 1.0, 1.0);
				Minecraft.getMinecraft().getRenderManager().doRenderEntity(item, 0, 0, 0, 0, 0, true);
				GL11.glPopMatrix();
			}
			if (fluid != null){
				int c = fluid.getColor();
	            blue = c & 0xFF;
	            green = (c >> 8) & 0xFF;
	            red = (c >> 16) & 0xFF;
	            alpha = (c >> 24) & 0xFF;
	            
	            TextureAtlasSprite sprite = FluidTextureUtil.stillTextures.get(fluid);
	            diffU = maxU-minU;
	            diffV = maxV-minV;
	            
	            minU = sprite.getMinU()+diffU*0.25;
	            maxU = sprite.getMaxU()-diffU*0.25;
	            minV = sprite.getMinV()+diffV*0.25;
	            maxV = sprite.getMaxV()-diffV*0.25;
	            
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
	            buffer.pos(x+0.25, y+0.75+0.1875*((float)amount/(float)capacity), z+0.25).tex(minU, minV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				buffer.pos(x+0.75, y+0.75+0.1875*((float)amount/(float)capacity), z+0.25).tex(maxU, minV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				buffer.pos(x+0.75, y+0.75+0.1875*((float)amount/(float)capacity), z+0.75).tex(maxU, maxV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				buffer.pos(x+0.25, y+0.75+0.1875*((float)amount/(float)capacity), z+0.75).tex(minU, maxV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
				tess.draw();
				
				GlStateManager.disableAlpha();
				GlStateManager.disableBlend();
				GlStateManager.enableLighting();
				GlStateManager.enableCull();
			}
		}
	}
}
