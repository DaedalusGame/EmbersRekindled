package teamroots.embers.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import teamroots.embers.block.BlockCatalyticPlug;

public class TileEntityCatalyticPlugRenderer extends TileEntitySpecialRenderer<TileEntityCatalyticPlug> {
    double minU, minV, maxU, maxV, diffU, diffV;

    @Override
    public void render(TileEntityCatalyticPlug tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if(tile != null && tile.getWorld().getBlockState(tile.getPos()).getBlock() instanceof BlockCatalyticPlug) {
            EnumFacing facing = tile.getWorld().getBlockState(tile.getPos()).getValue(BlockCatalyticPlug.FACING);
            FluidStack fluidStack = tile.getFluidStack();
            int capacity = tile.getCapacity();
            if (fluidStack != null){
                Fluid fluid = fluidStack.getFluid();
                int amount = fluidStack.amount;
                int c = fluid.getColor(fluidStack);
                int blue = c & 0xFF;
                int green = (c >> 8) & 0xFF;
                int red = (c >> 16) & 0xFF;
                alpha = (c >> 24) & 0xFF;

                TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getStill(fluidStack).toString());
                diffU = maxU-minU;
                diffV = maxV-minV;

                minU = sprite.getMinU()+diffU*0.25;
                maxU = sprite.getMaxU()-diffU*0.25;
                minV = sprite.getMinV()+diffV*0.25;
                maxV = sprite.getMaxV()-diffV*0.25;

                int i = getWorld().getCombinedLight(tile.getPos(), fluid.getLuminosity(fluidStack));
                int lightx = i >> 0x10 & 0xFFFF;
                int lighty = i & 0xFFFF;

                GlStateManager.disableCull();
                GlStateManager.disableLighting();
                GlStateManager.enableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                Tessellator tess = Tessellator.getInstance();
                BufferBuilder buffer = tess.getBuffer();
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
                buffer.pos(x+0.25, y+0.125+0.8125*((float)amount/(float)capacity), z+0.25).tex(minU, minV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
                buffer.pos(x+0.75, y+0.125+0.8125*((float)amount/(float)capacity), z+0.25).tex(maxU, minV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
                buffer.pos(x+0.75, y+0.125+0.8125*((float)amount/(float)capacity), z+0.75).tex(maxU, maxV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
                buffer.pos(x+0.25, y+0.125+0.8125*((float)amount/(float)capacity), z+0.75).tex(minU, maxV).lightmap(lightx,lighty).color(red,green,blue,alpha).endVertex();
                tess.draw();

                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                GlStateManager.enableLighting();
            }
            GlStateManager.popMatrix();
        }
        super.render(tile, x, y, z, partialTicks, destroyStage, alpha);
    }
}
