package teamroots.embers.util;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Biomes;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.items.IItemHandler;

public class Misc {
	public static Random random = new Random();
	
	public static boolean isExtremeHills(Biome biome){
		return biome.getBiomeName().compareTo(Biomes.EXTREME_HILLS.getBiomeName()) == 0 || biome.getBiomeName().compareTo(Biomes.EXTREME_HILLS_EDGE.getBiomeName()) == 0 || biome.getBiomeName().compareTo(Biomes.EXTREME_HILLS_WITH_TREES.getBiomeName()) == 0 || biome.getBiomeName().compareTo(Biomes.MUTATED_EXTREME_HILLS.getBiomeName()) == 0 || biome.getBiomeName().compareTo(Biomes.MUTATED_EXTREME_HILLS_WITH_TREES.getBiomeName()) == 0;
	}
	
	public static EnumFacing getOppositeHorizontalFace(EnumFacing face){
		if (face == EnumFacing.DOWN){
			return EnumFacing.DOWN;
		}
		else if (face == EnumFacing.UP){
			return EnumFacing.UP;
		}
		else {
			return face.getOpposite();
		}
	}
	
	public static EntityItem rayTraceItem(World world, double posX, double posY, double posZ, double dirX, double dirY, double dirZ){
		double x = posX;
		double y = posY;
		double z = posZ;
		for (int i = 0; i < 120; i ++){
			x += dirX/20.0f;
			y += dirY/20.0f;
			z += dirZ/20.0f;
			List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(x-0.2,y-0.2,z-0.2,x+0.2,y+0.2,z+0.2));
			if (items.size() > 0){
				return items.get(0);
			}
		}
		return null;
	}
	
	public static int intColor(int r, int g, int b){
		return (r*65536 + g*256 + b);
	}
	
	public static EnumFacing getOppositeFace(EnumFacing face){
		if (face == EnumFacing.DOWN){
			return EnumFacing.UP;
		}
		else if (face == EnumFacing.UP){
			return EnumFacing.DOWN;
		}
		else {
			return face.getOpposite();
		}
	}
	
	public static boolean isHills(Biome biome){
		return biome.getBiomeName().compareTo(Biomes.BIRCH_FOREST_HILLS.getBiomeName()) == 0 || biome.getBiomeName().compareTo(Biomes.COLD_TAIGA_HILLS.getBiomeName()) == 0 || biome.getBiomeName().compareTo(Biomes.DESERT_HILLS.getBiomeName()) == 0 || biome.getBiomeName().compareTo(Biomes.FOREST_HILLS.getBiomeName()) == 0 || biome.getBiomeName().compareTo(Biomes.JUNGLE_HILLS.getBiomeName()) == 0 || biome.getBiomeName().compareTo(Biomes.MUTATED_BIRCH_FOREST_HILLS.getBiomeName()) == 0 || biome.getBiomeName().compareTo(Biomes.MUTATED_REDWOOD_TAIGA_HILLS.getBiomeName()) == 0 || biome.getBiomeName().compareTo(Biomes.REDWOOD_TAIGA_HILLS.getBiomeName()) == 0 || biome.getBiomeName().compareTo(Biomes.TAIGA_HILLS.getBiomeName()) == 0;
	}
	
	public static void spawnInventoryInWorld(World world, double x, double y, double z, IItemHandler inventory){
		if (inventory != null && !world.isRemote){
			for (int i = 0; i < inventory.getSlots(); i ++){
				if (inventory.getStackInSlot(i) != null){
					world.spawnEntityInWorld(new EntityItem(world,x,y,z,inventory.getStackInSlot(i)));
				}
			}
		}
	}
}