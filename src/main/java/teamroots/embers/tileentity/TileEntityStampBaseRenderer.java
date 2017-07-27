package teamroots.embers.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
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
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import teamroots.embers.block.BlockStampBase;
import teamroots.embers.util.FluidTextureUtil;

public class TileEntityStampBaseRenderer extends TileEntitySpecialRenderer {
	int blue, green, red, alpha;
	int lightx, lighty;
	double minU, minV, maxU, maxV, diffU, diffV;
	public TileEntityStampBaseRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile instanceof TileEntityStampBase && !tile.getWorld().isAirBlock(tile.getPos())){
			TileEntityStampBase tank = (TileEntityStampBase)tile;
			if (tank.getWorld().getBlockState(tank.getPos()).getBlock() instanceof BlockStampBase){
				EnumFacing face = tank.getWorld().getBlockState(tank.getPos()).getValue(BlockStampBase.facing);
				int amount = tank.getAmount();
				int capacity = tank.getCapacity();
	            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
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
		            
		            if (sprite != null){
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
			            
						GL11.glPushMatrix();
						GL11.glTranslated(x, y, z);
						GL11.glTranslated(0.5, 0.5, 0.5);
						if (face == EnumFacing.UP){
							GL11.glRotated(180, 1, 0, 0);
						}
						
						if (face == EnumFacing.NORTH){
							GL11.glRotated(90, 1, 0, 0);
						}
						
						if (face == EnumFacing.WEST){
							GL11.glRotated(90, 0, 1, 0);
							GL11.glRotated(90, 1, 0, 0);
						}
						
						if (face == EnumFacing.SOUTH){
							GL11.glRotated(180, 0, 1, 0);
							GL11.glRotated(90, 1, 0, 0);
						}
						
						if (face == EnumFacing.EAST){
							GL11.glRotated(270, 0, 1, 0);
							GL11.glRotated(90, 1, 0, 0);
						}
						GL11.glTranslated(-0.5, -0.5, -0.5);
						
			            Tessellator tess = Tessellator.getInstance();
			            BufferBuilder buffer = tess.getBuffer();
			            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
			            buffer.pos(0.25, 0.75+0.1875*((float)amount/(float)capacity), 0.25).tex(minU, minV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
						buffer.pos(0.75, 0.75+0.1875*((float)amount/(float)capacity), 0.25).tex(maxU, minV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
						buffer.pos(0.75, 0.75+0.1875*((float)amount/(float)capacity), 0.75).tex(maxU, maxV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
						buffer.pos(0.25, 0.75+0.1875*((float)amount/(float)capacity), 0.75).tex(minU, maxV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
						tess.draw();
						GL11.glPopMatrix();
						
						GlStateManager.disableAlpha();
						GlStateManager.disableBlend();
						GlStateManager.enableLighting();
						GlStateManager.enableCull();
		            }
				}
				if (tank.inputs.getStackInSlot(0) != ItemStack.EMPTY){
					GL11.glPushMatrix();
					EntityItem item = new EntityItem(Minecraft.getMinecraft().world,x,y,z,new ItemStack(tank.inputs.getStackInSlot(0).getItem(),1,tank.inputs.getStackInSlot(0).getMetadata()));
					item.hoverStart = 0;
					GL11.glTranslated(x, y, z);
					GL11.glTranslated(0.5, 0.5, 0.5);
					
					if (face == EnumFacing.UP){
						GL11.glRotated(180, 1, 0, 0);
					}
					
					if (face == EnumFacing.NORTH){
						GL11.glRotated(90, 1, 0, 0);
					}
					
					if (face == EnumFacing.WEST){
						GL11.glRotated(90, 0, 1, 0);
						GL11.glRotated(90, 1, 0, 0);
					}
					
					if (face == EnumFacing.SOUTH){
						GL11.glRotated(180, 0, 1, 0);
						GL11.glRotated(90, 1, 0, 0);
					}
					
					if (face == EnumFacing.EAST){
						GL11.glRotated(270, 0, 1, 0);
						GL11.glRotated(90, 1, 0, 0);
					}
					
					GL11.glScaled(1.0, 1.0, 1.0);
					Minecraft.getMinecraft().getRenderManager().doRenderEntity(item, 0, 0, 0, 0, 0, true);
					GL11.glPopMatrix();
				}
			}
		}
	}
}
