package teamroots.embers.world.dimension;

import net.minecraft.world.biome.BiomeProviderSingle;

public class BiomeProviderCave extends BiomeProviderSingle {
	public BiomeProviderCave(){
		super(new BiomeCave());
	}
}
