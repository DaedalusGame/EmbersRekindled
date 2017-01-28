package teamroots.embers.world;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import teamroots.embers.ConfigManager;
import teamroots.embers.RegistryManager;

public class WorldGenOres implements IWorldGenerator {
	double l = Math.sin(1);
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		switch (world.provider.getDimension()) {
		case 0:{
			WorldGenMinable ore_copper = new WorldGenMinable(RegistryManager.ore_copper.getDefaultState(), ConfigManager.copperVeinSize);
			for (int i = 0; i < ConfigManager.copperVeinsPerChunk; i ++){
				int x = chunkX*16+random.nextInt(16);
				int y = random.nextInt(ConfigManager.copperMaxY-ConfigManager.copperMinY)+ConfigManager.copperMinY;
				int z = chunkZ*16+random.nextInt(16);
				ore_copper.generate(world, random, new BlockPos(x,y,z));
			}
			WorldGenMinable ore_lead = new WorldGenMinable(RegistryManager.ore_lead.getDefaultState(), ConfigManager.leadVeinSize);
			for (int i = 0; i < ConfigManager.leadVeinsPerChunk; i ++){
				int x = chunkX*16+random.nextInt(16);
				int y = random.nextInt(ConfigManager.leadMaxY-ConfigManager.leadMinY)+ConfigManager.leadMinY;
				int z = chunkZ*16+random.nextInt(16);
				ore_lead.generate(world, random, new BlockPos(x,y,z));
			}
			WorldGenMinable ore_silver = new WorldGenMinable(RegistryManager.ore_silver.getDefaultState(), ConfigManager.silverVeinSize);
			for (int i = 0; i < ConfigManager.silverVeinsPerChunk; i ++){
				int x = chunkX*16+random.nextInt(16);
				int y = random.nextInt(ConfigManager.silverMaxY-ConfigManager.silverMinY)+ConfigManager.silverMinY;
				int z = chunkZ*16+random.nextInt(16);
				ore_silver.generate(world, random, new BlockPos(x,y,z));
			}
            break;
		}
			
		default: {
            break;
		}
		
		}
	}
}
