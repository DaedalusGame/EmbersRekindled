package teamroots.embers.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import teamroots.embers.RegistryManager;
import teamroots.embers.config.ConfigMaterial;
import teamroots.embers.config.ConfigWorld;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WorldGenOres implements IWorldGenerator {

    private boolean isOreSpawnEnabled(int dimension) {
        return IntStream.of(ConfigWorld.ORE.blacklist).boxed().collect(Collectors.toList()).contains(dimension) == ConfigWorld.ORE.isWhitelist;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (!isOreSpawnEnabled(world.provider.getDimension()) || world.isRemote) return;
        if (ConfigMaterial.COPPER.mustLoad() && ConfigWorld.ORE.COPPER.generate) {
            WorldGenMinable ore_copper = new WorldGenMinable(RegistryManager.ore_copper.getDefaultState(), ConfigWorld.ORE.COPPER.veinSize);
            for (int i = 0; i < ConfigWorld.ORE.COPPER.veinPerChunk; i++) {
                int x = chunkX * 16 + random.nextInt(16);
                int y = random.nextInt(ConfigWorld.ORE.COPPER.maxY - ConfigWorld.ORE.COPPER.minY) + ConfigWorld.ORE.COPPER.minY;
                int z = chunkZ * 16 + random.nextInt(16);
                ore_copper.generate(world, random, new BlockPos(x, y, z));
            }
        }
        if (ConfigMaterial.LEAD.mustLoad() && ConfigWorld.ORE.LEAD.generate) {
            WorldGenMinable ore_lead = new WorldGenMinable(RegistryManager.ore_lead.getDefaultState(), ConfigWorld.ORE.LEAD.veinSize);
            for (int i = 0; i < ConfigWorld.ORE.LEAD.veinPerChunk; i++) {
                int x = chunkX * 16 + random.nextInt(16);
                int y = random.nextInt(ConfigWorld.ORE.LEAD.maxY - ConfigWorld.ORE.LEAD.minY) + ConfigWorld.ORE.LEAD.minY;
                int z = chunkZ * 16 + random.nextInt(16);
                ore_lead.generate(world, random, new BlockPos(x, y, z));
            }
        }
        if (ConfigMaterial.SILVER.mustLoad() && ConfigWorld.ORE.SILVER.generate) {
            WorldGenMinable ore_silver = new WorldGenMinable(RegistryManager.ore_silver.getDefaultState(), ConfigWorld.ORE.SILVER.veinSize);
            for (int i = 0; i < ConfigWorld.ORE.SILVER.veinPerChunk; i++) {
                int x = chunkX * 16 + random.nextInt(16);
                int y = random.nextInt(ConfigWorld.ORE.SILVER.maxY - ConfigWorld.ORE.SILVER.minY) + ConfigWorld.ORE.SILVER.minY;
                int z = chunkZ * 16 + random.nextInt(16);
                ore_silver.generate(world, random, new BlockPos(x, y, z));
            }
        }
        if (ConfigWorld.ORE.QUARTZ.generate) {
            WorldGenMinable ore_quartz = new WorldGenMinable(RegistryManager.ore_quartz.getDefaultState(), ConfigWorld.ORE.QUARTZ.veinSize);
            for (int i = 0; i < ConfigWorld.ORE.QUARTZ.veinPerChunk; i++) {
                int x = chunkX * 16 + random.nextInt(16);
                int y = random.nextInt(ConfigWorld.ORE.QUARTZ.maxY - ConfigWorld.ORE.QUARTZ.minY) + ConfigWorld.ORE.QUARTZ.minY;
                int z = chunkZ * 16 + random.nextInt(16);
                ore_quartz.generate(world, random, new BlockPos(x, y, z));
            }
        }
        if (ConfigMaterial.ALUMINUM.mustLoad() && ConfigWorld.ORE.ALUMINUM.generate) {
            WorldGenMinable ore_aluminum = new WorldGenMinable(RegistryManager.ore_aluminum.getDefaultState(), ConfigWorld.ORE.ALUMINUM.veinSize);
            for (int i = 0; i < ConfigWorld.ORE.ALUMINUM.veinPerChunk; i++) {
                int x = chunkX * 16 + random.nextInt(16);
                int y = random.nextInt(ConfigWorld.ORE.ALUMINUM.maxY - ConfigWorld.ORE.ALUMINUM.minY) + ConfigWorld.ORE.ALUMINUM.minY;
                int z = chunkZ * 16 + random.nextInt(16);
                ore_aluminum.generate(world, random, new BlockPos(x, y, z));
            }
        }
        if (ConfigMaterial.NICKEL.mustLoad() && ConfigWorld.ORE.NICKEL.generate) {
            WorldGenMinable ore_nickel = new WorldGenMinable(RegistryManager.ore_nickel.getDefaultState(), ConfigWorld.ORE.NICKEL.veinSize);
            for (int i = 0; i < ConfigWorld.ORE.NICKEL.veinPerChunk; i++) {
                int x = chunkX * 16 + random.nextInt(16);
                int y = random.nextInt(ConfigWorld.ORE.NICKEL.maxY - ConfigWorld.ORE.NICKEL.minY) + ConfigWorld.ORE.NICKEL.minY;
                int z = chunkZ * 16 + random.nextInt(16);
                ore_nickel.generate(world, random, new BlockPos(x, y, z));
            }
        }
        if (ConfigMaterial.TIN.mustLoad() && ConfigWorld.ORE.TIN.generate) {
            WorldGenMinable ore_tin = new WorldGenMinable(RegistryManager.ore_tin.getDefaultState(), ConfigWorld.ORE.TIN.veinSize);
            for (int i = 0; i < ConfigWorld.ORE.TIN.veinPerChunk; i++) {
                int x = chunkX * 16 + random.nextInt(16);
                int y = random.nextInt(ConfigWorld.ORE.TIN.maxY - ConfigWorld.ORE.TIN.minY) + ConfigWorld.ORE.TIN.minY;
                int z = chunkZ * 16 + random.nextInt(16);
                ore_tin.generate(world, random, new BlockPos(x, y, z));
            }
        }
    }
}
