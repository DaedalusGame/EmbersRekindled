package teamroots.embers.world.dimension;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import teamroots.embers.Embers;

public class BiomeCave extends Biome {


    public BiomeCave() {
		super(new BiomeProperties("cave"));
        this.setRegistryName(Embers.MODID, "biome.cave");
        this.spawnableCreatureList.clear();
        this.theBiomeDecorator.treesPerChunk = -999;
	}
    
    public static float getGroundHeight(int x, int y){
    	return 128*NoiseGeneratorUtil.getOctave(x, y, 256)
    			+ 64*NoiseGeneratorUtil.getOctave(x, y, 128)
    			+ 16*NoiseGeneratorUtil.getOctave(x, y, 32)
    			+ 8*NoiseGeneratorUtil.getOctave(x, y, 16)
    			+ 4*NoiseGeneratorUtil.getOctave(x, y, 8)
    			+ 1.5f*NoiseGeneratorUtil.getOctave(x, y, 2);
    }
    
    public static float getCeilingHeight(int x, int y){
    	return 256-(64*NoiseGeneratorUtil.getOctave(x-16384, y+16384, 192)
    			+ 32*NoiseGeneratorUtil.getOctave(x+16384, y-16384, 96)
    			+ 8*NoiseGeneratorUtil.getOctave(x-16384, y-16384, 24)
    			+ 4*NoiseGeneratorUtil.getOctave(x+16384, y+16384, 12)
    			+ 2*NoiseGeneratorUtil.getOctave(x-16384, y+16384, 6)
    			+ 1f*NoiseGeneratorUtil.getOctave(x+16384, y+16384, 3));
    }

	@Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer primer, int x, int z, double noiseVal) {
		IBlockState bedrock = Blocks.BEDROCK.getDefaultState();
		IBlockState stone = Blocks.STONE.getDefaultState();
    	primer.setBlockState(x, 1, z, bedrock);
    	for (int i = 1; i < getGroundHeight(x,z); i ++){
    		primer.setBlockState(x, i, z, stone);
    	}
    	for (int i = 256; i > getCeilingHeight(x,z); i --){
    		primer.setBlockState(x, i, z, stone);
    	}
    }
}
