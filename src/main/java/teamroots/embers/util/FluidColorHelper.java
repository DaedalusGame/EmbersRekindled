package teamroots.embers.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class FluidColorHelper implements IResourceManagerReloadListener {
    static Map<String, Integer> cache = new HashMap<>();

    public static int getColor(FluidStack stack) {
        if(stack == null)
            return -1;
        Fluid fluid = stack.getFluid();
        if(fluid == null)
            return -1;
        ResourceLocation still = fluid.getStill(stack);
        if(still == null)
            return -1;
        TextureMap textureMapBlocks = Minecraft.getMinecraft().getTextureMapBlocks();
        TextureAtlasSprite sprite = textureMapBlocks.getTextureExtry(still.toString());
        if(sprite == null)
            return -1;
        if(sprite.getFrameCount() == 0)
            return -1;
        String cacheKey = fluid.getName() + sprite.frameCounter % sprite.getFrameCount();
        if (cache.containsKey(cacheKey))
            return finalizeColor(stack, cache.get(cacheKey));

        int totalColor = calculateColor(sprite);
        cache.put(cacheKey,totalColor);
        return finalizeColor(stack,  totalColor);
    }

    private static int calculateColor(TextureAtlasSprite sprite) {

        int[][] pixelData = sprite.getFrameTextureData(sprite.frameCounter % sprite.getFrameCount());
        int pixelCount = 0;
        long totalRed = 0;
        long totalGreen = 0;
        long totalBlue = 0;
        long totalAlpha = 0;
        for (int i = 0; i < pixelData.length; i++)
            for (int j = 0; j < pixelData[i].length; j++)
            {
                int pixel = pixelData[i][j];
                totalRed += (pixel >> 16) & 0xFF;
                totalGreen += (pixel >> 8) & 0xFF;
                totalBlue += (pixel >> 0) & 0xFF;
                totalAlpha += (pixel >> 24) & 0xFF;
                pixelCount++;
            }
        totalRed /= pixelCount;
        totalGreen /= pixelCount;
        totalBlue /= pixelCount;
        totalAlpha /= pixelCount;

        return (int) ((totalRed << 16) | (totalGreen << 8) | (totalBlue << 0) | (totalAlpha << 24));
    }

    public static int finalizeColor(FluidStack stack, int color) {
        Color fluidColor = new Color(color,true);
        Color fluidTint = new Color(stack.getFluid().getColor(stack),true);

        return new Color((fluidColor.getRed() * fluidTint.getRed()) / 255,
                (fluidColor.getGreen() * fluidTint.getGreen()) / 255,
                (fluidColor.getBlue() * fluidTint.getBlue()) / 255,
                (fluidColor.getAlpha() * fluidTint.getAlpha()) / 255).getRGB();
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        cache.clear();
    }
}
