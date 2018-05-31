package teamroots.embers.tileentity;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import teamroots.embers.Embers;

public class TileEntityCrystalCellRenderer extends TileEntitySpecialRenderer<TileEntityCrystalCell> {
    public ResourceLocation texture = new ResourceLocation(Embers.MODID + ":textures/blocks/crystal_material.png");
    RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
    Random random = new Random();

    public TileEntityCrystalCellRenderer() {
        super();
    }

    @Override
    public void render(TileEntityCrystalCell tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha) {
        if (tile != null) {
            random.setSeed(tile.seed);
            float capacityFactor = 128000.0f;
            double emberCapacity = tile.renderCapacity;
            double lerpCapacity = emberCapacity * partialTicks + tile.renderCapacityLast * (1-partialTicks);
            int numLayers = 2 + (int) Math.floor(lerpCapacity / capacityFactor) + 1;
            int numLayersOld = numLayers - 1;
            float growthFactor = getGrowthFactor(capacityFactor, lerpCapacity);
            float layerHeight = 0.25f;
            float height = layerHeight * numLayers * growthFactor + numLayersOld * layerHeight * (1-growthFactor);
            float[] widths = new float[numLayers + 1];
            float[] oldWidths = new float[numLayers + 1];
            for (float i = 0; i < numLayers + 1; i++) {
                float rand = random.nextFloat();
                if (i < numLayers / 2.0f) {
                    widths[(int) i] = (i / (numLayers / 2.0f)) * (layerHeight * 0.1875f + layerHeight * 0.09375f * rand) * numLayers;
                } else {
                    widths[(int) i] = ((numLayers - i) / (numLayers / 2.0f)) * (layerHeight * 0.1875f + layerHeight * 0.09375f * rand) * numLayers;
                }
                if (i >= numLayersOld)
                    continue;
                if (i < numLayersOld / 2.0) {
                    oldWidths[(int) i] = (i / (numLayersOld / 2.0f)) * (layerHeight * 0.1875f + layerHeight * 0.09375f * rand) * numLayersOld;
                } else {
                    oldWidths[(int) i] = ((numLayersOld - i) / (numLayersOld / 2.0f)) * (layerHeight * 0.1875f + layerHeight * 0.09375f * rand) * numLayersOld;
                }
            }

            GlStateManager.pushAttrib();
            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            GlStateManager.disableCull();
            GlStateManager.disableLighting();
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buffer = tess.getBuffer();

            int l = getWorld().getCombinedLight(tile.getPos(), 15);
            int lx = l >> 0x10 & 0xFFFF;
            int ly = l & 0xFFFF;

            for (float j = 0; j < 12; j++) {

                GlStateManager.pushMatrix();

                float scale = j / 12.0f;

                GlStateManager.translate(x + 0.5, y + height / 2.0f + 1.5, z + 0.5);
                GlStateManager.scale(scale, scale, scale);

                GlStateManager.rotate(partialTicks + tile.ticksExisted % 360, 0, 1, 0);
                GlStateManager.rotate(30.0f * (float) Math.sin(Math.toRadians((partialTicks / 3.0f) + (tile.ticksExisted / 3.0f) % 360)), 1, 0, 0);

                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
                for (int i = 0; i < widths.length - 1; i++) {
                    float width = widths[i] * growthFactor + oldWidths[i] * (1-growthFactor);
                    float nextWidth = widths[i + 1] * growthFactor + oldWidths[i + 1] * (1-growthFactor);
                    buffer.pos(-width, layerHeight * i - height / 2.0f, -width).tex(0, 0).lightmap(lx, ly).color(1, 1, 1, 0.65f).endVertex();
                    buffer.pos(width, layerHeight * i - height / 2.0f, -width).tex(0.5, 0).lightmap(lx, ly).color(1, 1, 1, 0.65f).endVertex();
                    buffer.pos(nextWidth, layerHeight + layerHeight * i - height / 2.0f, -nextWidth).tex(0.5, 0.5).lightmap(lx, ly).color(1, 1, 1, 0.65f).endVertex();
                    buffer.pos(-nextWidth, layerHeight + layerHeight * i - height / 2.0f, -nextWidth).tex(0, 0.5).lightmap(lx, ly).color(1, 1, 1, 0.65f).endVertex();

                    buffer.pos(-width, layerHeight * i - height / 2.0f, width).tex(0, 0).lightmap(lx, ly).color(1, 1, 1, 0.65f).endVertex();
                    buffer.pos(width, layerHeight * i - height / 2.0f, width).tex(0.5, 0).lightmap(lx, ly).color(1, 1, 1, 0.65f).endVertex();
                    buffer.pos(nextWidth, layerHeight + layerHeight * i - height / 2.0f, nextWidth).tex(0.5, 0.5).lightmap(lx, ly).color(1, 1, 1, 0.65f).endVertex();
                    buffer.pos(-nextWidth, layerHeight + layerHeight * i - height / 2.0f, nextWidth).tex(0, 0.5).lightmap(lx, ly).color(1, 1, 1, 0.65f).endVertex();

                    buffer.pos(-width, layerHeight * i - height / 2.0f, -width).tex(0, 0).lightmap(lx, ly).color(1, 1, 1, 0.65f).endVertex();
                    buffer.pos(-width, layerHeight * i - height / 2.0f, width).tex(0.5, 0).lightmap(lx, ly).color(1, 1, 1, 0.65f).endVertex();
                    buffer.pos(-nextWidth, layerHeight + layerHeight * i - height / 2.0f, nextWidth).tex(0.5, 0.5).lightmap(lx, ly).color(1, 1, 1, 0.65f).endVertex();
                    buffer.pos(-nextWidth, layerHeight + layerHeight * i - height / 2.0f, -nextWidth).tex(0, 0.5).lightmap(lx, ly).color(1, 1, 1, 0.65f).endVertex();

                    buffer.pos(width, layerHeight * i - height / 2.0f, -width).tex(0, 0).lightmap(lx, ly).color(1, 1, 1, 0.65f).endVertex();
                    buffer.pos(width, layerHeight * i - height / 2.0f, width).tex(0.5, 0).lightmap(lx, ly).color(1, 1, 1, 0.65f).endVertex();
                    buffer.pos(nextWidth, layerHeight + layerHeight * i - height / 2.0f, nextWidth).tex(0.5, 0.5).lightmap(lx, ly).color(1, 1, 1, 0.65f).endVertex();
                    buffer.pos(nextWidth, layerHeight + layerHeight * i - height / 2.0f, -nextWidth).tex(0, 0.5).lightmap(lx, ly).color(1, 1, 1, 0.65f).endVertex();
                }
                tess.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.enableCull();
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.popAttrib();
        }
    }

    private float getGrowthFactor(float capacityFactor, double emberCapacity) {
        return (float) (emberCapacity % capacityFactor) / capacityFactor;
    }
}
