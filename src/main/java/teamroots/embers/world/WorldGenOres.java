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
		if (!ConfigManager.orespawnBlacklist.contains(world.provider.getDimension())){
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
			WorldGenMinable ore_quartz = new WorldGenMinable(RegistryManager.ore_quartz.getDefaultState(), ConfigManager.quartzVeinSize);
			for (int i = 0; i < ConfigManager.quartzVeinsPerChunk; i ++){
				int x = chunkX*16+random.nextInt(16);
				int y = random.nextInt(ConfigManager.quartzMaxY-ConfigManager.quartzMinY)+ConfigManager.quartzMinY;
				int z = chunkZ*16+random.nextInt(16);
				ore_quartz.generate(world, random, new BlockPos(x,y,z));
			}
			if (ConfigManager.enableAluminum){
				WorldGenMinable ore_aluminum = new WorldGenMinable(RegistryManager.ore_aluminum.getDefaultState(), ConfigManager.aluminumVeinSize);
				for (int i = 0; i < ConfigManager.aluminumVeinsPerChunk; i ++){
					int x = chunkX*16+random.nextInt(16);
					int y = random.nextInt(ConfigManager.aluminumMaxY-ConfigManager.aluminumMinY)+ConfigManager.aluminumMinY;
					int z = chunkZ*16+random.nextInt(16);
					ore_aluminum.generate(world, random, new BlockPos(x,y,z));
				}
			}
			if (ConfigManager.enableNickel){
				WorldGenMinable ore_nickel = new WorldGenMinable(RegistryManager.ore_nickel.getDefaultState(), ConfigManager.nickelVeinSize);
				for (int i = 0; i < ConfigManager.nickelVeinsPerChunk; i ++){
					int x = chunkX*16+random.nextInt(16);
					int y = random.nextInt(ConfigManager.nickelMaxY-ConfigManager.nickelMinY)+ConfigManager.nickelMinY;
					int z = chunkZ*16+random.nextInt(16);
					ore_nickel.generate(world, random, new BlockPos(x,y,z));
				}
			}
			if (ConfigManager.enableTin){
				WorldGenMinable ore_tin = new WorldGenMinable(RegistryManager.ore_tin.getDefaultState(), ConfigManager.tinVeinSize);
				for (int i = 0; i < ConfigManager.tinVeinsPerChunk; i ++){
					int x = chunkX*16+random.nextInt(16);
					int y = random.nextInt(ConfigManager.tinMaxY-ConfigManager.tinMinY)+ConfigManager.tinMinY;
					int z = chunkZ*16+random.nextInt(16);
					ore_tin.generate(world, random, new BlockPos(x,y,z));
				}
			}
		}
	}
}
