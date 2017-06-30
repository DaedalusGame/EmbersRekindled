package teamroots.embers.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.oredict.RecipeSorter.Category;
import teamroots.embers.ConfigManager;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;
import teamroots.embers.item.EnumStampType;

public class RecipeRegistry {
	public static ArrayList<ItemMeltingRecipe> meltingRecipes = new ArrayList<ItemMeltingRecipe>();
	public static ArrayList<ItemMeltingOreRecipe> meltingOreRecipes = new ArrayList<ItemMeltingOreRecipe>();
	
	public static ArrayList<ItemStampingRecipe> stampingRecipes = new ArrayList<ItemStampingRecipe>();
	public static ArrayList<ItemStampingOreRecipe> stampingOreRecipes = new ArrayList<ItemStampingOreRecipe>();
	
	public static ArrayList<FluidMixingRecipe> mixingRecipes = new ArrayList<FluidMixingRecipe>();
	
	public static ArrayList<AlchemyRecipe> alchemyRecipes = new ArrayList<AlchemyRecipe>();
	
	public static void registerMaterialSet(String ingotKey, String nuggetKey, String blockKey,
		Item ingot, Item nugget, Item plate, Block block, Item pickaxe, Item axe, Item shovel, Item hoe, Item sword){
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(block),true,new Object[]{
				"XXX",
				"XXX",
				"XXX",
				'X', ingotKey}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ingot),true,new Object[]{
				"XXX",
				"XXX",
				"XXX",
				'X', nuggetKey}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ingot,9),new Object[]{
				blockKey}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(nugget,9),new Object[]{
				ingotKey}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(sword,1),true,new Object[]{
				" C ",
				" C ",
				" S ",
				'C', ingotKey,
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(pickaxe,1),true,new Object[]{
				"CCC",
				" S ",
				" S ",
				'C', ingotKey,
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shovel,1),true,new Object[]{
				" C ",
				" S ",
				" S ",
				'C', ingotKey,
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(axe,1),true,new Object[]{
				" CC",
				" SC",
				" S ",
				'C', ingotKey,
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(axe,1),true,new Object[]{
				"CC ",
				"CS ",
				" S ",
				'C', ingotKey,
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(hoe,1),true,new Object[]{
				" CC",
				" S ",
				" S ",
				'C', ingotKey,
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(plate,1),new Object[]{
				ingotKey, ingotKey, ingotKey, ingotKey, RegistryManager.tinker_hammer}));
	}
	
	public static void initOreDict(){
		OreDictionary.registerOre("nuggetIron", RegistryManager.nugget_iron);
		OreDictionary.registerOre("ingotCopper", RegistryManager.ingot_copper);
		OreDictionary.registerOre("ingotLead", RegistryManager.ingot_lead);
		OreDictionary.registerOre("ingotSilver", RegistryManager.ingot_silver);
		OreDictionary.registerOre("ingotDawnstone", RegistryManager.ingot_dawnstone);
		OreDictionary.registerOre("nuggetCopper", RegistryManager.nugget_copper);
		OreDictionary.registerOre("nuggetLead", RegistryManager.nugget_lead);
		OreDictionary.registerOre("nuggetSilver", RegistryManager.nugget_silver);
		OreDictionary.registerOre("nuggetDawnstone", RegistryManager.nugget_dawnstone);
		OreDictionary.registerOre("plateGold", RegistryManager.plate_gold);
		OreDictionary.registerOre("plateIron", RegistryManager.plate_iron);
		OreDictionary.registerOre("plateCopper", RegistryManager.plate_copper);
		OreDictionary.registerOre("plateLead", RegistryManager.plate_lead);
		OreDictionary.registerOre("plateSilver", RegistryManager.plate_silver);
		OreDictionary.registerOre("plateDawnstone", RegistryManager.plate_dawnstone);
		OreDictionary.registerOre("blockCopper", RegistryManager.block_copper);
		OreDictionary.registerOre("blockLead", RegistryManager.block_lead);
		OreDictionary.registerOre("blockSilver", RegistryManager.block_silver);
		OreDictionary.registerOre("blockDawnstone", RegistryManager.block_dawnstone);
		OreDictionary.registerOre("oreCopper", RegistryManager.ore_copper);
		OreDictionary.registerOre("oreLead", RegistryManager.ore_lead);
		OreDictionary.registerOre("oreSilver", RegistryManager.ore_silver);
		OreDictionary.registerOre("oreQuartz", RegistryManager.ore_quartz);
		OreDictionary.registerOre("slimeball", RegistryManager.adhesive);
		
		if (ConfigManager.enableAluminum){
			OreDictionary.registerOre("blockAluminum", RegistryManager.block_aluminum);
			OreDictionary.registerOre("ingotAluminum", RegistryManager.ingot_aluminum);
			OreDictionary.registerOre("nuggetAluminum", RegistryManager.nugget_aluminum);
			OreDictionary.registerOre("plateAluminum", RegistryManager.plate_aluminum);
			OreDictionary.registerOre("oreAluminum", RegistryManager.ore_aluminum);
		}
		
		if (ConfigManager.enableTin){
			OreDictionary.registerOre("blockTin", RegistryManager.block_tin);
			OreDictionary.registerOre("ingotTin", RegistryManager.ingot_tin);
			OreDictionary.registerOre("nuggetTin", RegistryManager.nugget_tin);
			OreDictionary.registerOre("plateTin", RegistryManager.plate_tin);
			OreDictionary.registerOre("oreTin", RegistryManager.ore_tin);
		}
		
		if (ConfigManager.enableNickel){
			OreDictionary.registerOre("blockNickel", RegistryManager.block_nickel);
			OreDictionary.registerOre("ingotNickel", RegistryManager.ingot_nickel);
			OreDictionary.registerOre("nuggetNickel", RegistryManager.nugget_nickel);
			OreDictionary.registerOre("plateNickel", RegistryManager.plate_nickel);
			OreDictionary.registerOre("oreNickel", RegistryManager.ore_nickel);
		}
		
		if (ConfigManager.enableBronze){
			OreDictionary.registerOre("blockBronze", RegistryManager.block_bronze);
			OreDictionary.registerOre("ingotBronze", RegistryManager.ingot_bronze);
			OreDictionary.registerOre("nuggetBronze", RegistryManager.nugget_bronze);
			OreDictionary.registerOre("plateBronze", RegistryManager.plate_bronze);
		}
		
		if (ConfigManager.enableElectrum){
			OreDictionary.registerOre("blockElectrum", RegistryManager.block_electrum);
			OreDictionary.registerOre("ingotElectrum", RegistryManager.ingot_electrum);
			OreDictionary.registerOre("nuggetElectrum", RegistryManager.nugget_electrum);
			OreDictionary.registerOre("plateElectrum", RegistryManager.plate_electrum);
		}
	}
	
	public static void init(){
		RecipeSorter.register(Embers.MODID+":ashen_cloak_socket", AshenCloakSocketRecipe.class, Category.SHAPELESS, "");
		RecipeSorter.register(Embers.MODID+":ashen_cloak_unsocket", AshenCloakUnsocketRecipe.class, Category.SHAPELESS, "");
		
		initOreDict();
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.crystal_ember,1),true,new Object[]{
				"XXX",
				"XXX",
				'X', RegistryManager.shard_ember}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.shard_ember,6),new Object[]{
				RegistryManager.crystal_ember}));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.nugget_iron,9),new Object[]{
				"ingotIron"}));
		

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.plate_gold,1),new Object[]{
		"ingotGold","ingotGold","ingotGold","ingotGold",RegistryManager.tinker_hammer}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.plate_iron,1),new Object[]{
		"ingotIron","ingotIron","ingotIron","ingotIron",RegistryManager.tinker_hammer}));
		
		RecipeRegistry.registerMaterialSet("ingotLead", "nuggetLead", "blockLead", 
				RegistryManager.ingot_lead, 
				RegistryManager.nugget_lead, 
				RegistryManager.plate_lead, 
				RegistryManager.block_lead, 
				RegistryManager.pickaxe_lead, 
				RegistryManager.axe_lead, 
				RegistryManager.shovel_lead, 
				RegistryManager.hoe_lead, 
				RegistryManager.sword_lead);
		
		RecipeRegistry.registerMaterialSet("ingotCopper", "nuggetCopper", "blockCopper", 
				RegistryManager.ingot_copper, 
				RegistryManager.nugget_copper, 
				RegistryManager.plate_copper, 
				RegistryManager.block_copper, 
				RegistryManager.pickaxe_copper, 
				RegistryManager.axe_copper, 
				RegistryManager.shovel_copper, 
				RegistryManager.hoe_copper, 
				RegistryManager.sword_copper);
		
		RecipeRegistry.registerMaterialSet("ingotSilver", "nuggetSilver", "blockSilver", 
				RegistryManager.ingot_silver, 
				RegistryManager.nugget_silver, 
				RegistryManager.plate_silver, 
				RegistryManager.block_silver, 
				RegistryManager.pickaxe_silver, 
				RegistryManager.axe_silver, 
				RegistryManager.shovel_silver, 
				RegistryManager.hoe_silver, 
				RegistryManager.sword_silver);
		
		RecipeRegistry.registerMaterialSet("ingotDawnstone", "nuggetDawnstone", "blockDawnstone", 
				RegistryManager.ingot_dawnstone, 
				RegistryManager.nugget_dawnstone, 
				RegistryManager.plate_dawnstone, 
				RegistryManager.block_dawnstone, 
				RegistryManager.pickaxe_dawnstone, 
				RegistryManager.axe_dawnstone, 
				RegistryManager.shovel_dawnstone, 
				RegistryManager.hoe_dawnstone, 
				RegistryManager.sword_dawnstone);
		
		if (ConfigManager.enableAluminum){
			RecipeRegistry.registerMaterialSet("ingotAluminum", "nuggetAluminum", "blockAluminum", 
					RegistryManager.ingot_aluminum, 
					RegistryManager.nugget_aluminum, 
					RegistryManager.plate_aluminum, 
					RegistryManager.block_aluminum, 
					RegistryManager.pickaxe_aluminum, 
					RegistryManager.axe_aluminum, 
					RegistryManager.shovel_aluminum, 
					RegistryManager.hoe_aluminum, 
					RegistryManager.sword_aluminum);
		}
		
		if (ConfigManager.enableBronze){
			RecipeRegistry.registerMaterialSet("ingotBronze", "nuggetBronze", "blockBronze", 
					RegistryManager.ingot_bronze, 
					RegistryManager.nugget_bronze, 
					RegistryManager.plate_bronze, 
					RegistryManager.block_bronze, 
					RegistryManager.pickaxe_bronze, 
					RegistryManager.axe_bronze, 
					RegistryManager.shovel_bronze, 
					RegistryManager.hoe_bronze, 
					RegistryManager.sword_bronze);
		}
		
		if (ConfigManager.enableElectrum){
			RecipeRegistry.registerMaterialSet("ingotElectrum", "nuggetElectrum", "blockElectrum", 
					RegistryManager.ingot_electrum, 
					RegistryManager.nugget_electrum, 
					RegistryManager.plate_electrum, 
					RegistryManager.block_electrum, 
					RegistryManager.pickaxe_electrum, 
					RegistryManager.axe_electrum, 
					RegistryManager.shovel_electrum, 
					RegistryManager.hoe_electrum, 
					RegistryManager.sword_electrum);
		}
		
		if (ConfigManager.enableNickel){
			RecipeRegistry.registerMaterialSet("ingotNickel", "nuggetNickel", "blockNickel", 
					RegistryManager.ingot_nickel, 
					RegistryManager.nugget_nickel, 
					RegistryManager.plate_nickel, 
					RegistryManager.block_nickel, 
					RegistryManager.pickaxe_nickel, 
					RegistryManager.axe_nickel, 
					RegistryManager.shovel_nickel, 
					RegistryManager.hoe_nickel, 
					RegistryManager.sword_nickel);
		}
		
		if (ConfigManager.enableTin){
			RecipeRegistry.registerMaterialSet("ingotTin", "nuggetTin", "blockTin", 
					RegistryManager.ingot_tin, 
					RegistryManager.nugget_tin, 
					RegistryManager.plate_tin, 
					RegistryManager.block_tin, 
					RegistryManager.pickaxe_tin, 
					RegistryManager.axe_tin, 
					RegistryManager.shovel_tin, 
					RegistryManager.hoe_tin, 
					RegistryManager.sword_tin);
		}
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.blend_caminite,4),new Object[]{
		Items.CLAY_BALL, Items.CLAY_BALL, Items.CLAY_BALL, Items.CLAY_BALL,"sand"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.plate_caminite_raw,1),true,new Object[]{
				"XX",
				"XX",
				'X', RegistryManager.blend_caminite}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.stamp_bar_raw,1),true,new Object[]{
				" X ",
				"X X",
				" X ",
				'X', RegistryManager.blend_caminite}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.stamp_flat_raw,1),true,new Object[]{
				"XXX",
				"X X",
				"XXX",
				'X', RegistryManager.blend_caminite}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.stamp_plate_raw,1),true,new Object[]{
				"X X",
				"   ",
				"X X",
				'X', RegistryManager.blend_caminite}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.block_caminite_brick,1),true,new Object[]{
				"XX",
				"XX",
				'X', RegistryManager.brick_caminite}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.block_caminite_brick_slab,6),true,new Object[]{
				"XXX",
				'X', RegistryManager.block_caminite_brick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.stairs_caminite_brick,4),true,new Object[]{
				"X  ",
				"XX ",
				"XXX",
				'X', RegistryManager.block_caminite_brick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.wall_caminite_brick,6),true,new Object[]{
				"XXX",
				"XXX",
				'X', RegistryManager.block_caminite_brick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ashen_stone,4),true,new Object[]{
				" S ",
				"SAS",
				" S ",
				'S', "stone",
				'A', "dustAsh"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ashen_stone_slab,6),true,new Object[]{
				"XXX",
				'X', RegistryManager.ashen_stone}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.stairs_ashen_stone,4),true,new Object[]{
				"X  ",
				"XX ",
				"XXX",
				'X', RegistryManager.ashen_stone}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.wall_ashen_stone,6),true,new Object[]{
				"XXX",
				"XXX",
				'X', RegistryManager.ashen_stone}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ashen_brick,4),true,new Object[]{
				" S ",
				"SAS",
				" S ",
				'S', Blocks.STONEBRICK,
				'A', "dustAsh"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ashen_brick,4),true,new Object[]{
				"SS",
				"SS",
				'S', RegistryManager.ashen_stone}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ashen_brick_slab,6),true,new Object[]{
				"XXX",
				'X', RegistryManager.ashen_brick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.stairs_ashen_brick,4),true,new Object[]{
				"X  ",
				"XX ",
				"XXX",
				'X', RegistryManager.ashen_brick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.wall_ashen_brick,6),true,new Object[]{
				"XXX",
				"XXX",
				'X', RegistryManager.ashen_brick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ashen_tile,4),true,new Object[]{
				"SS",
				"SS",
				'S', RegistryManager.ashen_brick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ashen_tile_slab,6),true,new Object[]{
				"XXX",
				'X', RegistryManager.ashen_tile}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.stairs_ashen_tile,4),true,new Object[]{
				"X  ",
				"XX ",
				"XXX",
				'X', RegistryManager.ashen_tile}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.wall_ashen_tile,6),true,new Object[]{
				"XXX",
				"XXX",
				'X', RegistryManager.ashen_tile}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ashen_stone,4),true,new Object[]{
				"SS",
				"SS",
				'S', RegistryManager.ashen_tile}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.block_tank,1),true,new Object[]{
				"B B",
				"P P",
				"BIB",
				'I', "ingotIron",
				'P', "plateIron",
				'B', RegistryManager.brick_caminite}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.pipe,8),true,new Object[]{
				"IPI",
				'P', "plateIron",
				'I', "ingotIron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.pump,1),true,new Object[]{
				" R ",
				"PBP",
				" R ",
				'P', RegistryManager.pipe,
				'B', RegistryManager.plate_caminite,
				'R', "dustRedstone"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.block_furnace,1),true,new Object[]{
				"BPB",
				"BCB",
				"IFI",
				'P', RegistryManager.plate_caminite,
				'B', RegistryManager.brick_caminite,
				'F', Blocks.FURNACE,
				'I', "ingotIron",
				'C', "ingotCopper"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ember_receiver,4),true,new Object[]{
				"I I",
				"CPC",
				'I', "ingotIron",
				'C', "ingotCopper",
				'P', RegistryManager.plate_caminite}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ember_emitter,4),true,new Object[]{
				" C ",
				" C ",
				"IPI",
				'I', "ingotIron",
				'C', "ingotCopper",
				'P', RegistryManager.plate_caminite}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.copper_cell,1),true,new Object[]{
				"BIB",
				"ICI",
				"BIB",
				'I', "ingotIron",
				'C', "blockCopper",
				'B', RegistryManager.block_caminite_brick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.item_pipe,8),true,new Object[]{
				"IPI",
				'P', "plateLead",
				'I', "ingotLead"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.item_pump,1),true,new Object[]{
				" R ",
				"PBP",
				" R ",
				'P', RegistryManager.item_pipe,
				'B', RegistryManager.plate_caminite,
				'R', "dustRedstone"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.bin,1),true,new Object[]{
				"I I",
				"I I",
				"IPI",
				'P', "plateIron",
				'I', "ingotIron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.stamper,1),true,new Object[]{
				"XCX",
				"XBX",
				"X X",
				'B', "blockIron",
				'C', "ingotCopper",
				'Y', RegistryManager.block_caminite_brick,
				'X', RegistryManager.brick_caminite,
				'R', "dustRedstone"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.stamp_base,1),true,new Object[]{
				"I I",
				"XBX",
				'I', "ingotIron",
				'B', Items.BUCKET,
				'X', RegistryManager.block_caminite_brick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ember_bore,1),true,new Object[]{
				"YCY",
				"YBY",
				"III",
				'I', "ingotIron",
				'B', RegistryManager.mech_core,
				'Y', RegistryManager.stairs_caminite_brick,
				'C', "ingotCopper"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.mech_core,1),true,new Object[]{
				"IBI",
				" P ",
				"I I",
				'I', "ingotIron",
				'P', "plateLead",
				'B', Items.COMPASS}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.mech_accessor,1),true,new Object[]{
				"SPI",
				'P', "plateIron",
				'S', RegistryManager.stairs_caminite_brick,
				'I', "ingotIron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ember_activator,1),true,new Object[]{
				"CCC",
				"CCC",
				"IFI",
				'C', "ingotCopper",
				'F', Blocks.FURNACE,
				'I', "plateIron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.stone_edge,1),true,new Object[]{
				"XXX",
				"Y Y",
				"XXX",
				'Y', RegistryManager.brick_caminite,
				'X', RegistryManager.wall_caminite_brick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.mixer,1),true,new Object[]{
				"PPP",
				"PCP",
				"IMI",
				'P', "plateIron",
				'C', "ingotCopper",
				'M', RegistryManager.mech_core,
				'I', "ingotIron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.heat_coil,1),true,new Object[]{
				"PPP",
				"IBI",
				" M ",
				'P', "plateCopper",
				'B', "blockCopper",
				'M', RegistryManager.mech_core,
				'I', "ingotIron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.item_dropper,1),true,new Object[]{
				" P ",
				"I I",
				'P', RegistryManager.pipe,
				'I', "ingotIron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.large_tank,1),true,new Object[]{
				"Y Y",
				"I I",
				"YTY",
				'Y', RegistryManager.stairs_caminite_brick,
				'I', "ingotIron",
				'T', RegistryManager.block_tank}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ember_gauge,1),true,new Object[]{
				"B",
				"P",
				"C",
				'P', Items.PAPER,
				'I', "ingotIron",
				'B', Items.COMPASS,
				'C', "ingotCopper"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.item_gauge,1),true,new Object[]{
				"B",
				"P",
				"L",
				'P', Items.PAPER,
				'I', "ingotIron",
				'B', Items.COMPASS,
				'L', "ingotLead"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.fluid_gauge,1),true,new Object[]{
				"B",
				"P",
				"I",
				'P', Items.PAPER,
				'I', "ingotIron",
				'B', Items.COMPASS}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.block_lantern,4),true,new Object[]{
				"P",
				"E",
				"I",
				'E', RegistryManager.shard_ember,
				'I', "ingotIron",
				'P', "plateIron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.tinker_hammer,1),true,new Object[]{
				"IBI",
				"ISI",
				" S ",
				'B', "ingotLead",
				'I', "ingotIron",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ember_detector,1),true,new Object[]{
				" I ",
				"CRC",
				"CIC",
				'C', "ingotCopper",
				'I', "ingotIron",
				'R', "dustRedstone"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.beam_splitter,1),true,new Object[]{
				" D ",
				"CPC",
				" I ",
				'C', "ingotCopper",
				'I', "ingotIron",
				'P', "plateIron",
				'D', "ingotDawnstone"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ember_relay,4),true,new Object[]{
				" C ",
				"C C",
				" P ",
				'C', "ingotCopper",
				'P', "plateIron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.crystal_cell,1),true,new Object[]{
				" E ",
				"DED",
				"CBC",
				'C', "blockCopper",
				'B', "blockDawnstone",
				'D', "plateDawnstone",
				'E', RegistryManager.crystal_ember}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ember_jar,1),true,new Object[]{
				" C ",
				"ISI",
				" G ",
				'I', "ingotIron",
				'S', RegistryManager.shard_ember,
				'C', "ingotCopper",
				'G', "blockGlass"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ember_cartridge,1),true,new Object[]{
				"ICI",
				"GSG",
				" G ",
				'I', "ingotIron",
				'S', RegistryManager.crystal_ember,
				'C', "plateCopper",
				'G', "blockGlass"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.charger,1),true,new Object[]{
				" X ",
				"DCD",
				"IPI",
				'D', "ingotDawnstone",
				'P', "plateCopper",
				'C', "ingotCopper",
				'I', "ingotIron",
				'X', "plateIron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.axe_clockwork,1),true,new Object[]{
				"PCP",
				"ISI",
				" W ",
				'C', "plateCopper",
				'P', "plateDawnstone",
				'I', "ingotDawnstone",
				'S', RegistryManager.shard_ember,
				'W', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.pickaxe_clockwork,1),true,new Object[]{
				"ISI",
				" C ",
				" W ",
				'C', "ingotCopper",
				'I', "ingotDawnstone",
				'S', RegistryManager.shard_ember,
				'W', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.grandhammer,1),true,new Object[]{
				"BIB",
				" C ",
				" W ",
				'C', "ingotCopper",
				'I', "ingotDawnstone",
				'B', "blockDawnstone",
				'W', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.staff_ember,1),true,new Object[]{
				"SES",
				"IWI",
				" W ",
				'S', "plateSilver",
				'I', "ingotDawnstone",
				'E', RegistryManager.shard_ember,
				'W', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ignition_cannon,1),true,new Object[]{
				" DP",
				"DPI",
				"SW ",
				'I', "ingotIron",
				'D', "ingotDawnstone",
				'P', "plateDawnstone",
				'S', RegistryManager.shard_ember,
				'W', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.cinder_plinth,1),true,new Object[]{
				" P ",
				"SFS",
				"PBP",
				'P', "plateLead",
				'B', RegistryManager.block_caminite_brick,
				'S', "ingotSilver",
				'F', Blocks.FURNACE}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.alchemy_pedestal,1),true,new Object[]{
				"D D",
				"ICI",
				"SBS",
				'D', "plateDawnstone",
				'I', "ingotDawnstone",
				'B', "blockCopper",
				'S', RegistryManager.stairs_caminite_brick,
				'C', RegistryManager.crystal_ember}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.alchemy_tablet,1),true,new Object[]{
				" D ",
				"SXS",
				"BIB",
				'D', "plateDawnstone",
				'I', "ingotDawnstone",
				'B', RegistryManager.block_caminite_brick,
				'S', RegistryManager.stairs_caminite_brick,
				'X', "plateCopper"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.item_transfer,2),true,new Object[]{
				"PLP",
				"ILI",
				"I I",
				'P', "plateLead",
				'I', "ingotLead",
				'L', RegistryManager.item_pipe}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.beam_cannon,1),true,new Object[]{
				"PSP",
				"PSP",
				"IBI",
				'S', RegistryManager.crystal_ember,
				'P', "plateCopper",
				'I', "ingotDawnstone",
				'B', RegistryManager.block_caminite_brick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ashen_cloak_head,1),true,new Object[]{
				" S ",
				"C C",
				"DCD",
				'S', "string",
				'D', "ingotDawnstone",
				'C', RegistryManager.ashen_cloth}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ashen_cloak_chest,1),true,new Object[]{
				"P P",
				"CDC",
				"CDC",
				'D', "ingotDawnstone",
				'P', "plateDawnstone",
				'C', RegistryManager.ashen_cloth}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ashen_cloak_legs,1),true,new Object[]{
				"CCC",
				"D D",
				"D D",
				'D', "ingotDawnstone",
				'P', "plateDawnstone",
				'C', RegistryManager.ashen_cloth}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ashen_cloak_boots,1),true,new Object[]{
				"C C",
				"C C",
				"C C",
				'C', RegistryManager.ashen_cloth}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.sealed_planks,8),true,new Object[]{
				"PPP",
				"PSP",
				"PPP",
				'S', "slimeball",
				'P', "plankWood"}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.wrapped_sealed_planks,1),new Object[]{
				Blocks.IRON_BARS, RegistryManager.sealed_planks}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.archaic_bricks,1),true,new Object[]{
				"BB",
				"BB",
				'B', RegistryManager.archaic_brick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.archaic_light,1),true,new Object[]{
				" B ",
				"BSB",
				" B ",
				'B', RegistryManager.archaic_brick,
				'S', RegistryManager.shard_ember}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.dawnstone_anvil,1),true,new Object[]{
				"BBB",
				"cIc",
				"CCC",
				'B', "blockDawnstone",
				'I', "ingotDawnstone",
				'C', RegistryManager.block_caminite_brick,
				'c', RegistryManager.brick_caminite}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.auto_hammer,1),true,new Object[]{
				"cc ",
				"CIB",
				"cc ",
				'B', "blockIron",
				'I', "ingotIron",
				'C', "blockCopper",
				'c', RegistryManager.stairs_caminite_brick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.auto_hammer,1),true,new Object[]{
				" cc",
				"BIC",
				" cc",
				'B', "blockIron",
				'I', "ingotIron",
				'C', "blockCopper",
				'c', RegistryManager.stairs_caminite_brick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.vacuum,1),true,new Object[]{
				" LL",
				"P  ",
				" LL",
				'P', RegistryManager.item_pipe,
				'L', "ingotLead"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.vacuum,1),true,new Object[]{
				"LL ",
				"  P",
				"LL ",
				'P', RegistryManager.item_pipe,
				'L', "ingotLead"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.archaic_edge,2),true,new Object[]{
				"BBB",
				"BCB",
				"BBB",
				'B', RegistryManager.archaic_brick,
				'C', RegistryManager.shard_ember}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.archaic_tile,4),true,new Object[]{
				"BB",
				"BB",
				'B', RegistryManager.archaic_bricks}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.archaic_bricks,4),true,new Object[]{
				"BB",
				"BB",
				'B', RegistryManager.archaic_tile}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.breaker,1),true,new Object[]{
				"PIP",
				"LRL",
				"L L", 
				'P', "plateIron",
				'I', Items.IRON_INGOT,
				'L', "ingotLead",
				'R', Items.REDSTONE}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ember_injector,1),true,new Object[]{
				"S S",
				"DCD",
				"BPB", 
				'P', "plateSilver",
				'S', "ingotSilver",
				'D', "plateDawnstone",
				'B', RegistryManager.block_caminite_brick,
				'C', RegistryManager.wildfire_core}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.boiler,1),true,new Object[]{
				"CCC",
				"IFI",
				"IBI", 
				'B', "blockCopper",
				'I', Items.IRON_INGOT,
				'C', "ingotCopper",
				'F', Blocks.FURNACE}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.reactor,1),true,new Object[]{
				"CCC",
				"CWC",
				"SBS", 
				'B', RegistryManager.block_caminite_brick,
				'W', RegistryManager.wildfire_core,
				'C', "ingotCopper",
				'S', "plateSilver"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.combustor,1),true,new Object[]{
				" C ",
				"PEP",
				"CMC", 
				'M', RegistryManager.mech_core,
				'P', "plateCopper",
				'C', "ingotCopper",
				'E', RegistryManager.ember_cluster}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.catalyzer,1),true,new Object[]{
				" C ",
				"PEP",
				"CMC", 
				'M', RegistryManager.mech_core,
				'P', "plateSilver",
				'C', "ingotSilver",
				'E', RegistryManager.ember_cluster}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.codex,1),true,new Object[]{
				" B ",
				" E ",
				" B ", 
				'B', RegistryManager.archaic_brick,
				'E', RegistryManager.ancient_motive_core}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.field_chart,1),true,new Object[]{
				"BBB",
				"BCB",
				"BBB", 
				'B', RegistryManager.archaic_brick,
				'C', RegistryManager.ember_cluster}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.archaic_circuit,1),true,new Object[]{
				" B ",
				"BCB",
				" B ", 
				'B', RegistryManager.archaic_brick,
				'C', "ingotCopper"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.inferno_forge,1),true,new Object[]{
				"BPB",
				"DCD",
				"SWS", 
				'B', RegistryManager.block_dawnstone,
				'D', "ingotDawnstone",
				'C', "blockCopper",
				'W', RegistryManager.wildfire_core,
				'P', "plateIron",
				'S', RegistryManager.block_caminite_brick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.superheater,1),true,new Object[]{
				" ID",
				"PPI",
				"PP ", 
				'I', "ingotDawnstone",
				'D', "plateDawnstone",
				'P', "ingotCopper"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.resonating_bell,1),true,new Object[]{
				"IIP",
				" sI",
				"S I", 
				'I', "ingotIron",
				's', "ingotSilver",
				'P', "plateIron",
				'S', "plateSilver"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.jet_augment,1),true,new Object[]{
				"PP ",
				"IsD",
				"PP ", 
				'I', "ingotIron",
				's', RegistryManager.shard_ember,
				'P', "plateDawnstone",
				'D', "ingotDawnstone"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.caster_orb,1),true,new Object[]{
				"DCD",
				"D D",
				" P ", 
				'C', RegistryManager.crystal_ember,
				'P', "plateDawnstone",
				'D', "ingotDawnstone"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ember_pulser,1),true,new Object[]{
				"D",
				"E",
				"I", 
				'E', RegistryManager.ember_emitter,
				'I', "ingotIron",
				'D', "plateDawnstone"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ember_ring,1),true,new Object[]{
				"CN ",
				"N N",
				" N ", 
				'C', RegistryManager.ember_cluster,
				'N', "nuggetDawnstone"}).setMirrored(true));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ember_amulet,1),true,new Object[]{
				" L ",
				"L L",
				"NCN", 
				'C', RegistryManager.ember_cluster,
				'N', "nuggetDawnstone",
				'L', Items.LEATHER}).setMirrored(true));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ember_belt,1),true,new Object[]{
				"LIL",
				"L L",
				"PCP", 
				'C', RegistryManager.ember_cluster,
				'I', "ingotDawnstone",
				'P', "plateDawnstone",
				'L', Items.LEATHER}).setMirrored(true));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.caminite_lever,4),true,new Object[]{
				"S",
				"P", 
				'S', "stickWood",
				'P', new ItemStack(RegistryManager.plate_caminite)}));
		GameRegistry.addRecipe(new AshenCloakSocketRecipe());
		GameRegistry.addRecipe(new AshenCloakUnsocketRecipe());
		
		GameRegistry.addSmelting(new ItemStack(RegistryManager.ore_copper), new ItemStack(RegistryManager.ingot_copper), 0.65f);
		GameRegistry.addSmelting(new ItemStack(RegistryManager.ore_silver), new ItemStack(RegistryManager.ingot_silver), 0.35f);
		GameRegistry.addSmelting(new ItemStack(RegistryManager.ore_lead), new ItemStack(RegistryManager.ingot_lead), 0.35f);
		if (ConfigManager.enableAluminum){
			GameRegistry.addSmelting(new ItemStack(RegistryManager.ore_aluminum), new ItemStack(RegistryManager.ingot_aluminum), 0.55f);
		}
		if (ConfigManager.enableTin){
			GameRegistry.addSmelting(new ItemStack(RegistryManager.ore_tin), new ItemStack(RegistryManager.ingot_tin), 0.55f);
		}
		if (ConfigManager.enableNickel){
			GameRegistry.addSmelting(new ItemStack(RegistryManager.ore_nickel), new ItemStack(RegistryManager.ingot_nickel), 0.55f);
		}
		GameRegistry.addSmelting(new ItemStack(RegistryManager.ore_quartz), new ItemStack(Items.QUARTZ), 0.35f);

		GameRegistry.addSmelting(new ItemStack(RegistryManager.blend_caminite), new ItemStack(RegistryManager.brick_caminite), 0.35f);
		GameRegistry.addSmelting(new ItemStack(RegistryManager.plate_caminite_raw), new ItemStack(RegistryManager.plate_caminite), 0.35f);
		GameRegistry.addSmelting(new ItemStack(RegistryManager.stamp_bar_raw), new ItemStack(RegistryManager.stamp_bar), 0.35f);
		GameRegistry.addSmelting(new ItemStack(RegistryManager.stamp_plate_raw), new ItemStack(RegistryManager.stamp_plate), 0.35f);
		GameRegistry.addSmelting(new ItemStack(RegistryManager.stamp_flat_raw), new ItemStack(RegistryManager.stamp_flat), 0.35f);
		
		meltingOreRecipes.add(new ItemMeltingOreRecipe("oreIron",new FluidStack(RegistryManager.fluid_molten_iron,288)));
		meltingOreRecipes.add(new ItemMeltingOreRecipe("ingotIron",new FluidStack(RegistryManager.fluid_molten_iron,144)));
		meltingOreRecipes.add(new ItemMeltingOreRecipe("nuggetIron",new FluidStack(RegistryManager.fluid_molten_iron,16)));
		meltingOreRecipes.add(new ItemMeltingOreRecipe("plateIron",new FluidStack(RegistryManager.fluid_molten_iron,144)));

		meltingOreRecipes.add(new ItemMeltingOreRecipe("oreGold",new FluidStack(RegistryManager.fluid_molten_gold,288)));
		meltingOreRecipes.add(new ItemMeltingOreRecipe("ingotGold",new FluidStack(RegistryManager.fluid_molten_gold,144)));
		meltingOreRecipes.add(new ItemMeltingOreRecipe("nuggetGold",new FluidStack(RegistryManager.fluid_molten_gold,16)));
		meltingOreRecipes.add(new ItemMeltingOreRecipe("plateGold",new FluidStack(RegistryManager.fluid_molten_gold,144)));

		meltingOreRecipes.add(new ItemMeltingOreRecipe("oreSilver",new FluidStack(RegistryManager.fluid_molten_silver,288)));
		meltingOreRecipes.add(new ItemMeltingOreRecipe("ingotSilver",new FluidStack(RegistryManager.fluid_molten_silver,144)));
		meltingOreRecipes.add(new ItemMeltingOreRecipe("nuggetSilver",new FluidStack(RegistryManager.fluid_molten_silver,16)));
		meltingOreRecipes.add(new ItemMeltingOreRecipe("plateSilver",new FluidStack(RegistryManager.fluid_molten_silver,144)));

		meltingOreRecipes.add(new ItemMeltingOreRecipe("oreCopper",new FluidStack(RegistryManager.fluid_molten_copper,288)));
		meltingOreRecipes.add(new ItemMeltingOreRecipe("ingotCopper",new FluidStack(RegistryManager.fluid_molten_copper,144)));
		meltingOreRecipes.add(new ItemMeltingOreRecipe("nuggetCopper",new FluidStack(RegistryManager.fluid_molten_copper,16)));
		meltingOreRecipes.add(new ItemMeltingOreRecipe("plateCopper",new FluidStack(RegistryManager.fluid_molten_copper,144)));

		meltingOreRecipes.add(new ItemMeltingOreRecipe("oreLead",new FluidStack(RegistryManager.fluid_molten_lead,288)));
		meltingOreRecipes.add(new ItemMeltingOreRecipe("ingotLead",new FluidStack(RegistryManager.fluid_molten_lead,144)));
		meltingOreRecipes.add(new ItemMeltingOreRecipe("nuggetLead",new FluidStack(RegistryManager.fluid_molten_lead,16)));
		meltingOreRecipes.add(new ItemMeltingOreRecipe("plateLead",new FluidStack(RegistryManager.fluid_molten_lead,144)));

		if (ConfigManager.enableAluminum){
			meltingOreRecipes.add(new ItemMeltingOreRecipe("oreAluminum",new FluidStack(RegistryManager.fluid_molten_aluminum,288)));
			meltingOreRecipes.add(new ItemMeltingOreRecipe("ingotAluminum",new FluidStack(RegistryManager.fluid_molten_aluminum,144)));
			meltingOreRecipes.add(new ItemMeltingOreRecipe("nuggetAluminum",new FluidStack(RegistryManager.fluid_molten_aluminum,16)));
			meltingOreRecipes.add(new ItemMeltingOreRecipe("plateAluminum",new FluidStack(RegistryManager.fluid_molten_aluminum,144)));
		}

		if (ConfigManager.enableNickel){
			meltingOreRecipes.add(new ItemMeltingOreRecipe("oreNickel",new FluidStack(RegistryManager.fluid_molten_nickel,288)));
			meltingOreRecipes.add(new ItemMeltingOreRecipe("ingotNickel",new FluidStack(RegistryManager.fluid_molten_nickel,144)));
			meltingOreRecipes.add(new ItemMeltingOreRecipe("nuggetNickel",new FluidStack(RegistryManager.fluid_molten_nickel,16)));
			meltingOreRecipes.add(new ItemMeltingOreRecipe("plateNickel",new FluidStack(RegistryManager.fluid_molten_nickel,144)));
		}

		if (ConfigManager.enableTin){
			meltingOreRecipes.add(new ItemMeltingOreRecipe("oreTin",new FluidStack(RegistryManager.fluid_molten_tin,288)));
			meltingOreRecipes.add(new ItemMeltingOreRecipe("ingotTin",new FluidStack(RegistryManager.fluid_molten_tin,144)));
			meltingOreRecipes.add(new ItemMeltingOreRecipe("nuggetTin",new FluidStack(RegistryManager.fluid_molten_tin,16)));
			meltingOreRecipes.add(new ItemMeltingOreRecipe("plateTin",new FluidStack(RegistryManager.fluid_molten_tin,144)));
		}
		
		meltingOreRecipes.add(new ItemMeltingOreRecipe("ingotDawnstone",new FluidStack(RegistryManager.fluid_molten_dawnstone,144)));
		meltingOreRecipes.add(new ItemMeltingOreRecipe("nuggetDawnstone",new FluidStack(RegistryManager.fluid_molten_dawnstone,16)));
		meltingOreRecipes.add(new ItemMeltingOreRecipe("plateDawnstone",new FluidStack(RegistryManager.fluid_molten_dawnstone,144)));
		
		if (ConfigManager.enableBronze){
			meltingOreRecipes.add(new ItemMeltingOreRecipe("ingotBronze",new FluidStack(RegistryManager.fluid_molten_bronze,144)));
			meltingOreRecipes.add(new ItemMeltingOreRecipe("nuggetBronze",new FluidStack(RegistryManager.fluid_molten_bronze,16)));
			meltingOreRecipes.add(new ItemMeltingOreRecipe("plateBronze",new FluidStack(RegistryManager.fluid_molten_bronze,144)));
		}
		
		if (ConfigManager.enableElectrum){
			meltingOreRecipes.add(new ItemMeltingOreRecipe("ingotElectrum",new FluidStack(RegistryManager.fluid_molten_electrum,144)));
			meltingOreRecipes.add(new ItemMeltingOreRecipe("nuggetElectrum",new FluidStack(RegistryManager.fluid_molten_electrum,16)));
			meltingOreRecipes.add(new ItemMeltingOreRecipe("plateElectrum",new FluidStack(RegistryManager.fluid_molten_electrum,144)));
		}
		
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_iron,144),EnumStampType.TYPE_BAR,new ItemStack(Items.IRON_INGOT,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_gold,144),EnumStampType.TYPE_BAR,new ItemStack(Items.GOLD_INGOT,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_lead,144),EnumStampType.TYPE_BAR,new ItemStack(RegistryManager.ingot_lead,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_silver,144),EnumStampType.TYPE_BAR,new ItemStack(RegistryManager.ingot_silver,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_copper,144),EnumStampType.TYPE_BAR,new ItemStack(RegistryManager.ingot_copper,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_dawnstone,144),EnumStampType.TYPE_BAR,new ItemStack(RegistryManager.ingot_dawnstone,1),false,false));
		if (ConfigManager.enableAluminum){	
			stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_aluminum,144),EnumStampType.TYPE_BAR,new ItemStack(RegistryManager.ingot_aluminum,1),false,false));
		}
		if (ConfigManager.enableBronze){	
			stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_bronze,144),EnumStampType.TYPE_BAR,new ItemStack(RegistryManager.ingot_bronze,1),false,false));
		}
		if (ConfigManager.enableElectrum){	
			stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_electrum,144),EnumStampType.TYPE_BAR,new ItemStack(RegistryManager.ingot_electrum,1),false,false));
		}
		if (ConfigManager.enableNickel){	
			stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_nickel,144),EnumStampType.TYPE_BAR,new ItemStack(RegistryManager.ingot_nickel,1),false,false));
		}
		if (ConfigManager.enableTin){	
			stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_tin,144),EnumStampType.TYPE_BAR,new ItemStack(RegistryManager.ingot_tin,1),false,false));
		}
		
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_iron,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plate_iron,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_gold,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plate_gold,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_lead,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plate_lead,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_silver,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plate_silver,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_copper,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plate_copper,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_dawnstone,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plate_dawnstone,1),false,false));
		if (ConfigManager.enableAluminum){	
			stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_aluminum,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plate_aluminum,1),false,false));
		}
		if (ConfigManager.enableBronze){	
			stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_bronze,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plate_bronze,1),false,false));
		}
		if (ConfigManager.enableElectrum){	
			stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_electrum,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plate_electrum,1),false,false));
		}
		if (ConfigManager.enableNickel){	
			stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_nickel,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plate_nickel,1),false,false));
		}
		if (ConfigManager.enableTin){	
			stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_tin,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plate_tin,1),false,false));
		}
		stampingRecipes.add(new ItemWasteStampingRecipe());
		
		stampingRecipes.add(new ItemStampingRecipe(new ItemStack(RegistryManager.shard_ember,1),new FluidStack(RegistryManager.fluid_molten_iron,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.aspectus_iron,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(new ItemStack(RegistryManager.shard_ember,1),new FluidStack(RegistryManager.fluid_molten_lead,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.aspectus_lead,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(new ItemStack(RegistryManager.shard_ember,1),new FluidStack(RegistryManager.fluid_molten_silver,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.aspectus_silver,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(new ItemStack(RegistryManager.shard_ember,1),new FluidStack(RegistryManager.fluid_molten_copper,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.aspectus_copper,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(new ItemStack(RegistryManager.shard_ember,1),new FluidStack(RegistryManager.fluid_molten_dawnstone,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.aspectus_dawnstone,1),false,false));
		
		mixingRecipes.add(new FluidMixingRecipe(new FluidStack[]{new FluidStack(RegistryManager.fluid_molten_copper,4),new FluidStack(RegistryManager.fluid_molten_gold,4)}, new FluidStack(RegistryManager.fluid_molten_dawnstone,8)));
		if (ConfigManager.enableElectrum){
			mixingRecipes.add(new FluidMixingRecipe(new FluidStack[]{new FluidStack(RegistryManager.fluid_molten_silver,4),new FluidStack(RegistryManager.fluid_molten_gold,4)}, new FluidStack(RegistryManager.fluid_molten_electrum,8)));
		}
		if (ConfigManager.enableTin && ConfigManager.enableBronze){
			mixingRecipes.add(new FluidMixingRecipe(new FluidStack[]{new FluidStack(RegistryManager.fluid_molten_copper,6),new FluidStack(RegistryManager.fluid_molten_tin,2)}, new FluidStack(RegistryManager.fluid_molten_bronze,8)));
		}
	
		alchemyRecipes.add(new AlchemyRecipe(48, 64, 0, 0, 48, 64, 0, 0, 0, 0, new ItemStack(Items.QUARTZ), new ItemStack(RegistryManager.ingot_copper), new ItemStack(RegistryManager.ingot_copper), new ItemStack(RegistryManager.shard_ember), new ItemStack(RegistryManager.shard_ember), new ItemStack(RegistryManager.seed,1,2)));
		alchemyRecipes.add(new AlchemyRecipe(48, 64, 0, 0, 0, 0, 48, 64, 0, 0, new ItemStack(Items.QUARTZ), new ItemStack(RegistryManager.ingot_silver), new ItemStack(RegistryManager.ingot_silver), new ItemStack(RegistryManager.shard_ember), new ItemStack(RegistryManager.shard_ember), new ItemStack(RegistryManager.seed,1,4)));
		alchemyRecipes.add(new AlchemyRecipe(48, 64, 0, 0, 0, 0, 0, 0, 48, 64, new ItemStack(Items.QUARTZ), new ItemStack(RegistryManager.ingot_lead), new ItemStack(RegistryManager.ingot_lead), new ItemStack(RegistryManager.shard_ember), new ItemStack(RegistryManager.shard_ember), new ItemStack(RegistryManager.seed,1,3)));
		alchemyRecipes.add(new AlchemyRecipe(48, 64, 48, 64, 0, 0, 0, 0, 0, 0, new ItemStack(Items.QUARTZ), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.GOLD_INGOT), new ItemStack(RegistryManager.shard_ember), new ItemStack(RegistryManager.shard_ember), new ItemStack(RegistryManager.seed,1,1)));
		alchemyRecipes.add(new AlchemyRecipe(96, 128, 0, 0, 0, 0, 0, 0, 0, 0, new ItemStack(Items.QUARTZ), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(RegistryManager.shard_ember), new ItemStack(RegistryManager.shard_ember), new ItemStack(RegistryManager.seed,1,0)));
		alchemyRecipes.add(new AlchemyRecipe(12, 24, 0, 0, 0, 0, 0, 0, 12, 24, new ItemStack(Blocks.WOOL), new ItemStack(RegistryManager.dust_ash), new ItemStack(RegistryManager.dust_ash), new ItemStack(Items.STRING), new ItemStack(Items.STRING), new ItemStack(RegistryManager.ashen_cloth,2)));
		alchemyRecipes.add(new AlchemyRecipe(0, 0, 32, 48, 0, 0, 0, 0, 24, 40, new ItemStack(Items.DIAMOND), new ItemStack(RegistryManager.ingot_dawnstone), new ItemStack(Items.COAL), new ItemStack(Items.COAL), new ItemStack(Items.COAL), new ItemStack(RegistryManager.inflictor_gem,1)));
		alchemyRecipes.add(new AlchemyRecipe(0, 0, 64, 80, 0, 0, 0, 0, 0, 0, new ItemStack(Items.QUARTZ), new ItemStack(Items.GUNPOWDER), new ItemStack(Items.GUNPOWDER), new ItemStack(RegistryManager.shard_ember), new ItemStack(RegistryManager.shard_ember), new ItemStack(RegistryManager.glimmer_shard,1)));
		alchemyRecipes.add(new AlchemyRecipe(24, 36, 0, 0, 0, 0, 0, 0, 0, 0, new ItemStack(Items.IRON_INGOT), new ItemStack(Items.QUARTZ), new ItemStack(Items.CLAY_BALL), new ItemStack(Items.DYE,1,4), ItemStack.EMPTY, new ItemStack(RegistryManager.isolated_materia,4)));
		alchemyRecipes.add(new AlchemyRecipe(12, 18, 0, 0, 0, 0, 0, 0, 0, 0, new ItemStack(Items.CLAY_BALL), new ItemStack(Items.DYE,1,15), new ItemStack(Items.DYE,1,15), ItemStack.EMPTY, ItemStack.EMPTY, new ItemStack(RegistryManager.adhesive,6)));
		alchemyRecipes.add(new AlchemyRecipe(0, 0, 0, 0, 8, 16, 0, 0, 0, 0, new ItemStack(Items.REDSTONE), new ItemStack(RegistryManager.dust_ash), new ItemStack(RegistryManager.dust_ash), new ItemStack(Blocks.COBBLESTONE,1), new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.NETHERRACK,2)));
		alchemyRecipes.add(new AlchemyRecipe(0, 0, 0, 0, 8, 16, 0, 0, 0, 0, new ItemStack(RegistryManager.dust_ash), new ItemStack(Blocks.SAND), new ItemStack(Blocks.SAND), new ItemStack(Blocks.SAND), new ItemStack(Blocks.SAND), new ItemStack(Blocks.SOUL_SAND,4)));
		alchemyRecipes.add(new AlchemyRecipe(0, 0, 0, 0, 0, 0, 64, 96, 64, 96, new ItemStack(RegistryManager.sword_lead,1), new ItemStack(Blocks.COAL_BLOCK), new ItemStack(Blocks.OBSIDIAN), new ItemStack(RegistryManager.ingot_lead), new ItemStack(RegistryManager.ingot_lead), new ItemStack(RegistryManager.tyrfing,1)));
		alchemyRecipes.add(new AlchemyRecipe(0, 0, 24, 48, 24, 48, 0, 0, 0, 0, new ItemStack(RegistryManager.crystal_ember,1), new ItemStack(Items.GUNPOWDER), new ItemStack(RegistryManager.shard_ember), new ItemStack(RegistryManager.shard_ember), new ItemStack(RegistryManager.shard_ember), new ItemStack(RegistryManager.ember_cluster,1)));
		alchemyRecipes.add(new AlchemyRecipe(32, 48, 0, 0, 0, 0, 24, 32, 0, 0, new ItemStack(RegistryManager.ancient_motive_core,1), new ItemStack(RegistryManager.ingot_dawnstone), new ItemStack(RegistryManager.ember_cluster), new ItemStack(RegistryManager.ingot_dawnstone), new ItemStack(RegistryManager.plate_copper), new ItemStack(RegistryManager.wildfire_core,1)));
		alchemyRecipes.add(new AlchemyRecipe(0, 0, 4, 8, 0, 0, 0, 0, 0, 0, new ItemStack(RegistryManager.archaic_brick,1), new ItemStack(Blocks.SOUL_SAND), new ItemStack(Blocks.SOUL_SAND), new ItemStack(Items.CLAY_BALL), new ItemStack(Items.CLAY_BALL), new ItemStack(RegistryManager.archaic_brick,5)));
		alchemyRecipes.add(new AlchemyRecipe(0, 0, 24, 32, 0, 0, 0, 0, 0, 0, new ItemStack(RegistryManager.shard_ember,1), new ItemStack(RegistryManager.archaic_brick), new ItemStack(RegistryManager.archaic_brick), new ItemStack(RegistryManager.archaic_brick), new ItemStack(RegistryManager.archaic_brick), new ItemStack(RegistryManager.ancient_motive_core,1)));
		
		alchemyRecipes.add(new AlchemyRecipe(0, 0, 0, 0, 16, 24, 0, 0, 0, 0, 
				new ItemStack(Items.GUNPOWDER,1), 
				new ItemStack(RegistryManager.plate_iron), 
				new ItemStack(RegistryManager.plate_iron), 
				new ItemStack(RegistryManager.plate_iron), 
				new ItemStack(RegistryManager.ingot_copper), 
				new ItemStack(RegistryManager.blasting_core,1)));
		alchemyRecipes.add(new AlchemyRecipe(0, 0, 16, 32, 0, 0, 0, 0, 48, 72, 
				new ItemStack(RegistryManager.archaic_circuit,1), 
				new ItemStack(RegistryManager.archaic_brick), 
				new ItemStack(Items.COAL), 
				new ItemStack(RegistryManager.archaic_brick), 
				new ItemStack(Items.COAL), 
				new ItemStack(RegistryManager.eldritch_insignia,1)));
		alchemyRecipes.add(new AlchemyRecipe(0, 0, 0, 0, 24, 48, 0, 0, 40, 64, 
				new ItemStack(RegistryManager.plate_copper,1), 
				new ItemStack(RegistryManager.archaic_circuit), 
				new ItemStack(RegistryManager.ingot_copper), 
				new ItemStack(RegistryManager.archaic_circuit), 
				new ItemStack(RegistryManager.ingot_copper), 
				new ItemStack(RegistryManager.intelligent_apparatus,1)));
		alchemyRecipes.add(new AlchemyRecipe(0, 0, 16, 32, 0, 0, 16, 32, 0, 0, 
				new ItemStack(RegistryManager.crystal_ember,1), 
				new ItemStack(RegistryManager.plate_dawnstone), 
				new ItemStack(RegistryManager.plate_dawnstone), 
				new ItemStack(RegistryManager.plate_dawnstone), 
				new ItemStack(RegistryManager.ingot_silver), 
				new ItemStack(RegistryManager.flame_barrier,1)));
	}
	
	public static ItemStampingRecipe getStampingRecipe(ItemStack stack, FluidStack fluid, EnumStampType type){
		for (int i = 0; i < stampingRecipes.size(); i ++){
			if (stampingRecipes.get(i).matches(stack, fluid, type)){
				return stampingRecipes.get(i);
			}
		}
		return null;
	}
	
	public static ItemStampingOreRecipe getStampingOreRecipe(ItemStack stack, FluidStack fluid, EnumStampType type){
		for (int i = 0; i < stampingOreRecipes.size(); i ++){
			if (stampingOreRecipes.get(i).matches(stack, fluid, type)){
				return stampingOreRecipes.get(i);
			}
		}
		return null;
	}
	
	public static ItemMeltingRecipe getMeltingRecipe(ItemStack stack){
		for (int i = 0; i < meltingRecipes.size(); i ++){
			if (meltingRecipes.get(i).matches(stack)){
				return meltingRecipes.get(i);
			}
		}
		return null;
	}
	
	public static ItemMeltingOreRecipe getMeltingOreRecipe(ItemStack stack){
		for (int i = 0; i < meltingOreRecipes.size(); i ++){
			if (meltingOreRecipes.get(i).matches(stack)){
				return meltingOreRecipes.get(i);
			}
		}
		return null;
	}
	
	public static FluidMixingRecipe getMixingRecipe(ArrayList<FluidStack> fluids){
		for (int i = 0; i < mixingRecipes.size(); i ++){
			if (mixingRecipes.get(i).matches(fluids)){
				return mixingRecipes.get(i);
			}
		}
		return null;
	}
	
	public static AlchemyRecipe getAlchemyRecipe(ItemStack center, ItemStack stack1, ItemStack stack2, ItemStack stack3, ItemStack stack4){
		List<ItemStack> list = new ArrayList<ItemStack>();
		if (stack1 != ItemStack.EMPTY){
			list.add(stack1);
		}
		if (stack2 != ItemStack.EMPTY){
			list.add(stack2);
		}
		if (stack3 != ItemStack.EMPTY){
			list.add(stack3);
		}
		if (stack4 != ItemStack.EMPTY){
			list.add(stack4);
		}
		for (int i = 0; i < alchemyRecipes.size(); i ++){
			if (alchemyRecipes.get(i).matches(center, list)){
				return alchemyRecipes.get(i);
			}
		}
		return null;
	}
}
