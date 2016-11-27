package teamroots.embers.world.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkGenerator;

import teamroots.embers.RegistryManager;

public class CaveProvider extends WorldProvider {
    @Override
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
        return new Vec3d(0, 0, 0);
    }

    @Override
    public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {
        return new Vec3d(0, 0, 0);
    }

    @Override
    public float getCloudHeight() {
        return -100;
    }

    @Override
    public double getHorizon()
    {
        return 44.0D;
    }

    @Override
    public int getAverageGroundLevel()
    {
        return 68;
    }

    @Override
    public int getDimension() {
        return super.getDimension();
    }

    @Override
    public DimensionType getDimensionType() {
        return RegistryManager.dimensionCave;
    }

    @Override
    public String getSaveFolder() {
        return "cave";
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new ChunkGeneratorCave(worldObj);
    }

    @Override
    protected void createBiomeProvider() {
        this.biomeProvider = new BiomeProviderCave();
    }

    @Override
    public boolean isDaytime() {
        return false;
    }

    @Override
    public long getWorldTime() {
        return 18000;
    }

}
