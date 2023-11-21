package teamroots.embers.world;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import teamroots.embers.RegistryManager;
import teamroots.embers.config.ConfigWorld;
import teamroots.embers.entity.EntityAncientGolem;
import teamroots.embers.util.Misc;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WorldGenSmallRuin extends StructureBase implements IWorldGenerator {
	double l = Math.sin(1);
	public WorldGenSmallRuin(){
		super(7,7);
		this.replaceWithAir = false;
		addBlockMapping(" ",Blocks.AIR.getDefaultState());
		addBlockMapping(".",RegistryManager.structure_marker.getStateFromMeta(1));
		addBlockMapping("A",RegistryManager.archaic_bricks.getDefaultState());
		addBlockMapping("T",RegistryManager.ashen_tile.getDefaultState());
		addBlockMapping("L",RegistryManager.archaic_light.getDefaultState());
		addBlockMapping("G",RegistryManager.structure_marker.getStateFromMeta(0));
		addBlockMapping("R",RegistryManager.archaic_edge.getDefaultState());
		addBlockMapping("B",RegistryManager.ashen_brick.getDefaultState());
		addLayer(new String[]{
				"       ",
				" BBTBB ",
				" BTTTB ",
				" TTTTT ",
				" BTTTB ",
				" BBTBB ",
				"       ",
		});
		addLayer(new String[]{
				"  A.A  ",
				" AA.AA ",
				"AA...AA",
				"...G...",
				"AA...AA",
				" AA.AA ",
				"  A.A  ",
		});
		addLayer(new String[]{
				"  A.A  ",
				" AA.AA ",
				"AA...AA",
				".......",
				"AA...AA",
				" AA.AA ",
				"  A.A  ",
		});
		addLayer(new String[]{
				"  AAA  ",
				" AAAAA ",
				"AA...AA",
				"AA...AA",
				"AA...AA",
				" AAAAA ",
				"  AAA  ",
		});
		addLayer(new String[]{
				"       ",
				"  RRR  ",
				" R...R ",
				" R...R ",
				" R...R ",
				"  RRR  ",
				"       ",
		});
		addLayer(new String[]{
				"       ",
				"       ",
				"  AAA  ",
				"  ALA  ",
				"  AAA  ",
				"       ",
				"       ",
		});
	}
	
	@Override
	public void placeBlock(World world, BlockPos pos, IBlockState state){
		if (state.getBlock() == RegistryManager.structure_marker){
			if (state.getBlock().getMetaFromState(state) == 0){
				EntityAncientGolem golem = new EntityAncientGolem(world);
				golem.setPosition(pos.getX()+0.5, pos.getY(), pos.getZ());
				golem.onInitialSpawn(world.getDifficultyForLocation(pos), null);
				golem.enablePersistence();
				world.spawnEntity(golem);
				world.setBlockToAir(pos);
			}
			else if (state.getBlock().getMetaFromState(state) == 1){
				world.setBlockToAir(pos);
			}
		}
		else {
			super.placeBlock(world, pos, state);
		}
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
        if (!isSmallRuinEnabled(world.provider.getDimension()) || world.isRemote) return;
        int xx = chunkX*16 + 13 + Misc.random.nextInt(2);
        int zz = chunkZ*16 + 13 + Misc.random.nextInt(2);
        if (ConfigWorld.SMALL_RUIN.chance == 0) return;
        if (world.getHeight(xx, zz) > 16){
            int yy = 4+Misc.random.nextInt(world.getHeight(xx, zz));if (random.nextInt(ConfigWorld.SMALL_RUIN.chance) == 0){
                if (world.getBlockState(new BlockPos(xx,yy,zz)).getBlock() instanceof BlockStone){
                    boolean canGenerate = false;
                    BlockPos pos = new BlockPos(xx,yy,zz);
                    if (world.isAirBlock(pos.west(4)) ||
                            world.isAirBlock(pos.west(3)) ||
                            world.isAirBlock(pos.east(4)) ||
                            world.isAirBlock(pos.east(3)) ||
                            world.isAirBlock(pos.north(4)) ||
                            world.isAirBlock(pos.north(3)) ||
                            world.isAirBlock(pos.south(4)) ||
                            world.isAirBlock(pos.south(3))){
                        canGenerate = true;
                    }
                    if (canGenerate){
                        this.generateIn(world, xx, yy-2, zz,false);
                    }
                }
            }
        }
    }

	private static boolean isSmallRuinEnabled(int dimension) {
        return IntStream.of(ConfigWorld.SMALL_RUIN.blacklist).boxed().collect(Collectors.toList()).contains(dimension) == ConfigWorld.SMALL_RUIN.isWhiteList;
    }

}
