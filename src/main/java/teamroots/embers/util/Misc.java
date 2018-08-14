package teamroots.embers.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Biomes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Misc {
	public static Random random = new Random();

	public static boolean isValidLever(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (block instanceof BlockLever){
			EnumFacing face = state.getValue(BlockLever.FACING).getFacing();
			return face == side;
		}
		else if (block instanceof BlockButton){
			EnumFacing face = state.getValue(BlockButton.FACING);
			return face == side;
		}
		else if (block instanceof BlockRedstoneTorch){
			EnumFacing face = state.getValue(BlockRedstoneTorch.FACING);
			return face == side;
		}
		return false;
	}

	//TODO: DANNY DELETO
	public static EnumFacing getOppositeFace(EnumFacing face){
		return face.getOpposite();
	}

	public static EnumFacing getOppositeHorizontalFace(EnumFacing face){
		return face.getAxis().isHorizontal() ? face.getOpposite() : face;
	}

	public static EnumFacing getOppositeVerticalFace(EnumFacing face){
		return face.getAxis().isVertical() ? face.getOpposite() : face;
	}
	
	public static ItemStack getRepairItem(ItemStack stack){
		if (stack.getItem() instanceof ItemTool){
			ItemStack mat = ToolMaterial.valueOf(((ItemTool)stack.getItem()).getToolMaterialName()).getRepairItemStack().copy();
			if (mat.getItemDamage() == OreDictionary.WILDCARD_VALUE){
				mat.setItemDamage(0);
			}
			return mat;
		}
		if (stack.getItem() instanceof ItemSword){
			ItemStack mat = ToolMaterial.valueOf(((ItemSword)stack.getItem()).getToolMaterialName()).getRepairItemStack().copy();
			if (mat.getItemDamage() == OreDictionary.WILDCARD_VALUE){
				mat.setItemDamage(0);
			}
			return mat;
		}
		if (stack.getItem() instanceof ItemArmor){
			ItemStack mat = ((ItemArmor)stack.getItem()).getArmorMaterial().getRepairItemStack().copy();
			if (mat.getItemDamage() == OreDictionary.WILDCARD_VALUE){
				mat.setItemDamage(0);
			}
			return mat;
		}
		return ItemStack.EMPTY;
	}
	
	public static List<TileEntity> getAdjacentTiles(World world, BlockPos pos){
		List<TileEntity> tiles = new ArrayList<>();
		tiles.add(world.getTileEntity(pos.up()));
		tiles.add(world.getTileEntity(pos.down()));
		tiles.add(world.getTileEntity(pos.west()));
		tiles.add(world.getTileEntity(pos.east()));
		tiles.add(world.getTileEntity(pos.north()));
		tiles.add(world.getTileEntity(pos.south()));
		return tiles;
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
	
	public static boolean matchOreDict(ItemStack stack1, ItemStack stack2){
		int[] keys1 = OreDictionary.getOreIDs(stack1);
		int[] keys2 = OreDictionary.getOreIDs(stack2);
		for (int i = 0; i < keys1.length; i ++){
			for (int j = 0; j < keys2.length; j ++){
				if (keys1[i] == keys2[j]){
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean betweenAngles(float angleTest, float angleLow, float angleHigh){
		boolean between = angleTest >= angleLow && angleTest <= angleHigh;
		if (angleHigh < angleLow){
			between = angleTest >= angleLow && angleTest < 360 || angleTest > 0 && angleTest <= angleHigh;
		}
		return between;
	}
	
	public static float yawDegreesBetweenPoints(double posX, double posY, double posZ, double posX2, double posY2, double posZ2){
		float f = (float) ((180.0f*Math.atan2(posX2-posX,posZ2-posZ))/(float)Math.PI);
		return f;
	}
	
	public static float pitchDegreesBetweenPoints(double posX, double posY, double posZ, double posX2, double posY2, double posZ2){
		return (float)Math.toDegrees(Math.atan2(posY2-posY,Math.sqrt((posX2-posX)*(posX2-posX)+(posZ2-posZ)*(posZ2-posZ))));
	}
	
	public static int getResourceCount(ItemStack stack){
		int baseCount = 0;
		if (stack.getItem() instanceof ItemArmor){
			ItemArmor armor = (ItemArmor) stack.getItem();
			if (armor.armorType == EntityEquipmentSlot.HEAD)
				baseCount = 5;
			if (armor.armorType == EntityEquipmentSlot.CHEST)
				baseCount = 8;
			if (armor.armorType == EntityEquipmentSlot.LEGS)
				baseCount = 7;
			if (armor.armorType == EntityEquipmentSlot.FEET)
				baseCount = 4;
		}
		if (stack.getItem() instanceof ItemSword)
			baseCount = 2;
		if (stack.getItem() instanceof ItemBow)
			baseCount = 3;
		if (stack.getItem() instanceof ItemTool){
			if (stack.getItem() instanceof ItemPickaxe || stack.getItem().getHarvestLevel(stack, "pickaxe", null, null) > -1)
				baseCount = 3;
			else if (stack.getItem() instanceof ItemAxe || stack.getItem().getHarvestLevel(stack, "axe", null, null) > -1)
				baseCount = 3;
			else if (stack.getItem() instanceof ItemHoe)
				baseCount = 2;
			else if (stack.getItem() instanceof ItemSpade)
				baseCount = 1;
			else
				baseCount = 1;
		}
		if (baseCount > 0){
			return (int)((float)baseCount * (1.0f-(float)stack.getItemDamage() / (float)stack.getMaxDamage()));
		}
		return -1;
	}
	
	public static boolean isHills(Biome biome){
		return BiomeDictionary.hasType(biome, BiomeDictionary.Type.HILLS);
	}

	public static boolean isExtremeHills(Biome biome){
		return biome.getBiomeName().compareTo(Biomes.EXTREME_HILLS.getBiomeName()) == 0 || biome.getBiomeName().compareTo(Biomes.EXTREME_HILLS_EDGE.getBiomeName()) == 0 || biome.getBiomeName().compareTo(Biomes.EXTREME_HILLS_WITH_TREES.getBiomeName()) == 0 || biome.getBiomeName().compareTo(Biomes.MUTATED_EXTREME_HILLS.getBiomeName()) == 0 || biome.getBiomeName().compareTo(Biomes.MUTATED_EXTREME_HILLS_WITH_TREES.getBiomeName()) == 0;
	}
	
	public static void spawnInventoryInWorld(World world, double x, double y, double z, IItemHandler inventory){
		if (inventory != null && !world.isRemote){
			for (int i = 0; i < inventory.getSlots(); i ++){
				if (!inventory.getStackInSlot(i).isEmpty()){
					world.spawnEntity(new EntityItem(world,x,y,z,inventory.getStackInSlot(i)));
				}
			}
		}
	}

	public static ItemStack getStackFromState(IBlockState state) {
		if (state == null)
			return ItemStack.EMPTY;
		Block block = state.getBlock();
		int meta = block.damageDropped(state);
		return new ItemStack(block, 1, meta);
	}

	public static void syncTE(TileEntity tile) {
		IBlockState state = tile.getWorld().getBlockState(tile.getPos());
		tile.getWorld().notifyBlockUpdate(tile.getPos(), state, state, 3); //Does a good job
	}
}