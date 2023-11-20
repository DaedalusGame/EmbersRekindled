package teamroots.embers.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import teamroots.embers.ConfigManager;
import teamroots.embers.RegistryManager;
import teamroots.embers.config.ConfigWorld;

import java.util.Random;

public class WorldGenOres implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (ConfigManager.isOreSpawnEnabled(world.provider.getDimension()) && !world.isRemote) {
            if (ConfigWorld.ORE_COPPER.generate) {
                WorldGenMinable ore_copper = new WorldGenMinable(RegistryManager.ore_copper.getDefaultState(), ConfigWorld.ORE_COPPER.veinSize);
                for (int i = 0; i < ConfigWorld.ORE_COPPER.veinPerChunk; i++) {
                    int x = chunkX * 16 + random.nextInt(16);
                    int y = random.nextInt(ConfigWorld.ORE_COPPER.maxY - ConfigWorld.ORE_COPPER.minY) + ConfigWorld.ORE_COPPER.minY;
                    int z = chunkZ * 16 + random.nextInt(16);
                    ore_copper.generate(world, random, new BlockPos(x, y, z));
                }
            }
            if (ConfigWorld.ORE_LEAD.generate) {
                WorldGenMinable ore_lead = new WorldGenMinable(RegistryManager.ore_lead.getDefaultState(), ConfigWorld.ORE_LEAD.veinSize);
                for (int i = 0; i < ConfigWorld.ORE_LEAD.veinPerChunk; i++) {
                    int x = chunkX * 16 + random.nextInt(16);
                    int y = random.nextInt(ConfigWorld.ORE_LEAD.maxY - ConfigWorld.ORE_LEAD.minY) + ConfigWorld.ORE_LEAD.minY;
                    int z = chunkZ * 16 + random.nextInt(16);
                    ore_lead.generate(world, random, new BlockPos(x, y, z));
                }
            }
            if (ConfigWorld.ORE_SILVER.generate) {
                WorldGenMinable ore_silver = new WorldGenMinable(RegistryManager.ore_silver.getDefaultState(), ConfigWorld.ORE_SILVER.veinSize);
                for (int i = 0; i < ConfigWorld.ORE_SILVER.veinPerChunk; i++) {
                    int x = chunkX * 16 + random.nextInt(16);
                    int y = random.nextInt(ConfigWorld.ORE_SILVER.maxY - ConfigWorld.ORE_SILVER.minY) + ConfigWorld.ORE_SILVER.minY;
                    int z = chunkZ * 16 + random.nextInt(16);
                    ore_silver.generate(world, random, new BlockPos(x, y, z));
                }
            }
            if (ConfigWorld.ORE_QUARTZ.generate) {
                WorldGenMinable ore_quartz = new WorldGenMinable(RegistryManager.ore_quartz.getDefaultState(), ConfigWorld.ORE_QUARTZ.veinSize);
                for (int i = 0; i < ConfigWorld.ORE_QUARTZ.veinPerChunk; i++) {
                    int x = chunkX * 16 + random.nextInt(16);
                    int y = random.nextInt(ConfigWorld.ORE_QUARTZ.maxY - ConfigWorld.ORE_QUARTZ.minY) + ConfigWorld.ORE_QUARTZ.minY;
                    int z = chunkZ * 16 + random.nextInt(16);
                    ore_quartz.generate(world, random, new BlockPos(x, y, z));
                }
            }
            if (ConfigManager.enableAluminum && ConfigWorld.ORE_ALUMINUM.generate) {
                WorldGenMinable ore_aluminum = new WorldGenMinable(RegistryManager.ore_aluminum.getDefaultState(), ConfigWorld.ORE_ALUMINUM.veinSize);
                for (int i = 0; i < ConfigWorld.ORE_ALUMINUM.veinPerChunk; i++) {
                    int x = chunkX * 16 + random.nextInt(16);
                    int y = random.nextInt(ConfigWorld.ORE_ALUMINUM.maxY - ConfigWorld.ORE_ALUMINUM.minY) + ConfigWorld.ORE_ALUMINUM.minY;
                    int z = chunkZ * 16 + random.nextInt(16);
                    ore_aluminum.generate(world, random, new BlockPos(x, y, z));
                }
            }
            if (ConfigManager.enableNickel && ConfigWorld.ORE_NICKEL.generate) {
                WorldGenMinable ore_nickel = new WorldGenMinable(RegistryManager.ore_nickel.getDefaultState(), ConfigWorld.ORE_NICKEL.veinSize);
                for (int i = 0; i < ConfigWorld.ORE_NICKEL.veinPerChunk; i++) {
                    int x = chunkX * 16 + random.nextInt(16);
                    int y = random.nextInt(ConfigWorld.ORE_NICKEL.maxY - ConfigWorld.ORE_NICKEL.minY) + ConfigWorld.ORE_NICKEL.minY;
                    int z = chunkZ * 16 + random.nextInt(16);
                    ore_nickel.generate(world, random, new BlockPos(x, y, z));
                }
            }
            if (ConfigManager.enableTin && ConfigWorld.ORE_TIN.generate) {
                WorldGenMinable ore_tin = new WorldGenMinable(RegistryManager.ore_tin.getDefaultState(), ConfigWorld.ORE_TIN.veinSize);
                for (int i = 0; i < ConfigWorld.ORE_TIN.veinPerChunk; i++) {
                    int x = chunkX * 16 + random.nextInt(16);
                    int y = random.nextInt(ConfigWorld.ORE_TIN.maxY - ConfigWorld.ORE_TIN.minY) + ConfigWorld.ORE_TIN.minY;
                    int z = chunkZ * 16 + random.nextInt(16);
                    ore_tin.generate(world, random, new BlockPos(x, y, z));
                }
            }
        }
    }
}
