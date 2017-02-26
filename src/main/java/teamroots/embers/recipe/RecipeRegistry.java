package teamroots.embers.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import teamroots.embers.RegistryManager;
import teamroots.embers.item.EnumStampType;

public class RecipeRegistry {
	public static ArrayList<ItemMeltingRecipe> meltingRecipes = new ArrayList<ItemMeltingRecipe>();
	public static ArrayList<ItemMeltingOreRecipe> meltingOreRecipes = new ArrayList<ItemMeltingOreRecipe>();
	
	public static ArrayList<ItemStampingRecipe> stampingRecipes = new ArrayList<ItemStampingRecipe>();
	public static ArrayList<ItemStampingOreRecipe> stampingOreRecipes = new ArrayList<ItemStampingOreRecipe>();
	
	public static ArrayList<FluidMixingRecipe> mixingRecipes = new ArrayList<FluidMixingRecipe>();
	
	public static ArrayList<AlchemyRecipe> alchemyRecipes = new ArrayList<AlchemyRecipe>();
	
	public static void init(){
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
		OreDictionary.registerOre("slimeball", RegistryManager.adhesive);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.block_copper),true,new Object[]{
				"XXX",
				"XXX",
				"XXX",
				'X', "ingotCopper"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.block_lead),true,new Object[]{
				"XXX",
				"XXX",
				"XXX",
				'X', "ingotLead"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.block_silver),true,new Object[]{
				"XXX",
				"XXX",
				"XXX",
				'X', "ingotSilver"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.block_dawnstone),true,new Object[]{
				"XXX",
				"XXX",
				"XXX",
				'X', "ingotDawnstone"}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ingot_copper),true,new Object[]{
				"XXX",
				"XXX",
				"XXX",
				'X', "nuggetCopper"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ingot_lead),true,new Object[]{
				"XXX",
				"XXX",
				"XXX",
				'X', "nuggetLead"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ingot_silver),true,new Object[]{
				"XXX",
				"XXX",
				"XXX",
				'X', "nuggetSilver"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ingot_dawnstone),true,new Object[]{
				"XXX",
				"XXX",
				"XXX",
				'X', "nuggetDawnstone"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.crystal_ember,1),true,new Object[]{
				"XXX",
				"XXX",
				'X', RegistryManager.shard_ember}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.shard_ember,6),new Object[]{
				RegistryManager.crystal_ember}));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.ingot_copper,9),new Object[]{
				"blockCopper"}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.ingot_lead,9),new Object[]{
				"blockLead"}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.ingot_silver,9),new Object[]{
				"blockSilver"}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.ingot_dawnstone,9),new Object[]{
				"blockDawnstone"}));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.nugget_iron,9),new Object[]{
				"ingotIron"}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.nugget_copper,9),new Object[]{
				"ingotCopper"}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.nugget_lead,9),new Object[]{
				"ingotLead"}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.nugget_silver,9),new Object[]{
				"ingotSilver"}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.nugget_dawnstone,9),new Object[]{
				"ingotDawnstone"}));
		

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.plate_gold,1),new Object[]{
		"ingotGold","ingotGold","ingotGold","ingotGold",RegistryManager.tinker_hammer}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.plate_iron,1),new Object[]{
		"ingotIron","ingotIron","ingotIron","ingotIron",RegistryManager.tinker_hammer}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.plate_copper,1),new Object[]{
		"ingotCopper","ingotCopper","ingotCopper","ingotCopper",RegistryManager.tinker_hammer}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.plate_silver,1),new Object[]{
		"ingotSilver","ingotSilver","ingotSilver","ingotSilver",RegistryManager.tinker_hammer}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.plate_lead,1),new Object[]{
		"ingotLead","ingotLead","ingotLead","ingotLead",RegistryManager.tinker_hammer}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.plate_dawnstone,1),new Object[]{
		"ingotDawnstone","ingotDawnstone","ingotDawnstone","ingotDawnstone",RegistryManager.tinker_hammer}));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.blend_caminite,4),new Object[]{
		Items.CLAY_BALL, Items.CLAY_BALL, Items.CLAY_BALL,new ItemStack(Items.DYE,1,15)}));
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
				'A', RegistryManager.dust_ash}));
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
				'A', RegistryManager.dust_ash}));
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
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.sword_copper,1),true,new Object[]{
				" C ",
				" C ",
				" S ",
				'C', "ingotCopper",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.pickaxe_copper,1),true,new Object[]{
				"CCC",
				" S ",
				" S ",
				'C', "ingotCopper",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.shovel_copper,1),true,new Object[]{
				" C ",
				" S ",
				" S ",
				'C', "ingotCopper",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.axe_copper,1),true,new Object[]{
				" CC",
				" SC",
				" S ",
				'C', "ingot_copper",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.axe_copper,1),true,new Object[]{
				"CC ",
				"CS ",
				" S ",
				'C', "ingotCopper",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.hoe_copper,1),true,new Object[]{
				" CC",
				" S ",
				" S ",
				'C', "ingotCopper",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.hoe_copper,1),true,new Object[]{
				"CC ",
				" S ",
				" S ",
				'C', "ingotCopper",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.sword_dawnstone,1),true,new Object[]{
				" C ",
				" C ",
				" S ",
				'C', "ingotDawnstone",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.pickaxe_dawnstone,1),true,new Object[]{
				"CCC",
				" S ",
				" S ",
				'C', "ingotDawnstone",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.shovel_dawnstone,1),true,new Object[]{
				" C ",
				" S ",
				" S ",
				'C', "ingotDawnstone",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.axe_dawnstone,1),true,new Object[]{
				" CC",
				" SC",
				" S ",
				'C', "ingotDawnstone",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.axe_dawnstone,1),true,new Object[]{
				"CC ",
				"CS ",
				" S ",
				'C', "ingotDawnstone",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.hoe_dawnstone,1),true,new Object[]{
				" CC",
				" S ",
				" S ",
				'C', "ingotDawnstone",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.hoe_dawnstone,1),true,new Object[]{
				"CC ",
				" S ",
				" S ",
				'C', "ingotDawnstone",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.sword_silver,1),true,new Object[]{
				" C ",
				" C ",
				" S ",
				'C', "ingotSilver",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.pickaxe_silver,1),true,new Object[]{
				"CCC",
				" S ",
				" S ",
				'C', "ingotSilver",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.shovel_silver,1),true,new Object[]{
				" C ",
				" S ",
				" S ",
				'C', "ingotSilver",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.axe_silver,1),true,new Object[]{
				" CC",
				" SC",
				" S ",
				'C', "ingotSilver",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.axe_silver,1),true,new Object[]{
				"CC ",
				"CS ",
				" S ",
				'C', "ingotSilver",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.hoe_silver,1),true,new Object[]{
				" CC",
				" S ",
				" S ",
				'C', "ingotSilver",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.hoe_silver,1),true,new Object[]{
				"CC ",
				" S ",
				" S ",
				'C', "ingotSilver",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.sword_lead,1),true,new Object[]{
				" C ",
				" C ",
				" S ",
				'C', "ingotLead",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.pickaxe_lead,1),true,new Object[]{
				"CCC",
				" S ",
				" S ",
				'C', "ingotLead",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.shovel_lead,1),true,new Object[]{
				" C ",
				" S ",
				" S ",
				'C', "ingotLead",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.axe_lead,1),true,new Object[]{
				" CC",
				" SC",
				" S ",
				'C', "ingotLead",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.axe_lead,1),true,new Object[]{
				"CC ",
				"CS ",
				" S ",
				'C', "ingotLead",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.hoe_lead,1),true,new Object[]{
				" CC",
				" S ",
				" S ",
				'C', "ingotLead",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.hoe_lead,1),true,new Object[]{
				"CC ",
				" S ",
				" S ",
				'C', "ingotLead",
				'S', "stickWood"}));
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
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.knowledge_table,1),true,new Object[]{
				" E ",
				"PPP",
				"L L",
				'P', "plankWood",
				'L', "logWood",
				'E', RegistryManager.golems_eye}));
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
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.glimmer_lamp,1),true,new Object[]{
				" P ",
				"IGI",
				" P ",
				'G', RegistryManager.glimmer_shard,
				'I', "ingotIron",
				'P', "plateIron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.sealed_planks,8),true,new Object[]{
				"PPP",
				"PSP",
				"PPP",
				'S', "slimeball",
				'P', "plankWood"}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.wrapped_sealed_planks,1),new Object[]{
				Blocks.IRON_BARS, RegistryManager.sealed_planks}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.golems_eye,1),true,new Object[]{
				"BCB",
				'B', RegistryManager.archaic_brick,
				'C', RegistryManager.ancient_motive_core}));
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
				'L', RegistryManager.ingot_lead}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.vacuum,1),true,new Object[]{
				"LL ",
				"  P",
				"LL ",
				'P', RegistryManager.item_pipe,
				'L', RegistryManager.ingot_lead}));
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
				'P', RegistryManager.plate_iron,
				'I', Items.IRON_INGOT,
				'L', RegistryManager.ingot_lead,
				'R', Items.REDSTONE}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ember_injector,1),true,new Object[]{
				"S S",
				"DCD",
				"BPB", 
				'P', RegistryManager.plate_silver,
				'S', RegistryManager.ingot_silver,
				'D', RegistryManager.plate_dawnstone,
				'B', RegistryManager.block_caminite_brick,
				'C', RegistryManager.wildfire_core}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.boiler,1),true,new Object[]{
				"CCC",
				"IFI",
				"IBI", 
				'B', RegistryManager.block_copper,
				'I', Items.IRON_INGOT,
				'C', RegistryManager.ingot_copper,
				'F', Blocks.FURNACE}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.reactor,1),true,new Object[]{
				"CCC",
				"CWC",
				"SBS", 
				'B', RegistryManager.block_caminite_brick,
				'W', RegistryManager.wildfire_core,
				'C', RegistryManager.ingot_copper,
				'S', RegistryManager.plate_silver}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.combustor,1),true,new Object[]{
				" C ",
				"PEP",
				"CMC", 
				'M', RegistryManager.mech_core,
				'P', RegistryManager.plate_copper,
				'C', RegistryManager.ingot_copper,
				'E', RegistryManager.ember_cluster}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.catalyzer,1),true,new Object[]{
				" C ",
				"PEP",
				"CMC", 
				'M', RegistryManager.mech_core,
				'P', RegistryManager.plate_silver,
				'C', RegistryManager.ingot_silver,
				'E', RegistryManager.ember_cluster}));
		GameRegistry.addRecipe(new AshenCloakSocketRecipe());
		GameRegistry.addRecipe(new AshenCloakUnsocketRecipe());
		
		GameRegistry.addSmelting(new ItemStack(RegistryManager.ore_copper), new ItemStack(RegistryManager.ingot_copper), 0.65f);
		GameRegistry.addSmelting(new ItemStack(RegistryManager.ore_silver), new ItemStack(RegistryManager.ingot_silver), 0.35f);
		GameRegistry.addSmelting(new ItemStack(RegistryManager.ore_lead), new ItemStack(RegistryManager.ingot_lead), 0.35f);
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
		
		meltingOreRecipes.add(new ItemMeltingOreRecipe("ingotDawnstone",new FluidStack(RegistryManager.fluid_molten_dawnstone,144)));
		meltingOreRecipes.add(new ItemMeltingOreRecipe("nuggetDawnstone",new FluidStack(RegistryManager.fluid_molten_dawnstone,16)));
		meltingOreRecipes.add(new ItemMeltingOreRecipe("plateDawnstone",new FluidStack(RegistryManager.fluid_molten_dawnstone,144)));
		
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_iron,144),EnumStampType.TYPE_BAR,new ItemStack(Items.IRON_INGOT,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_gold,144),EnumStampType.TYPE_BAR,new ItemStack(Items.GOLD_INGOT,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_lead,144),EnumStampType.TYPE_BAR,new ItemStack(RegistryManager.ingot_lead,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_silver,144),EnumStampType.TYPE_BAR,new ItemStack(RegistryManager.ingot_silver,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_copper,144),EnumStampType.TYPE_BAR,new ItemStack(RegistryManager.ingot_copper,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_dawnstone,144),EnumStampType.TYPE_BAR,new ItemStack(RegistryManager.ingot_dawnstone,1),false,false));
		
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_iron,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plate_iron,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_gold,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plate_gold,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_lead,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plate_lead,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_silver,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plate_silver,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_copper,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plate_copper,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(ItemStack.EMPTY,new FluidStack(RegistryManager.fluid_molten_dawnstone,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plate_dawnstone,1),false,false));
		stampingRecipes.add(new ItemWasteStampingRecipe());
		
		stampingRecipes.add(new ItemStampingRecipe(new ItemStack(RegistryManager.shard_ember,1),new FluidStack(RegistryManager.fluid_molten_iron,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.aspectus_iron,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(new ItemStack(RegistryManager.shard_ember,1),new FluidStack(RegistryManager.fluid_molten_lead,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.aspectus_lead,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(new ItemStack(RegistryManager.shard_ember,1),new FluidStack(RegistryManager.fluid_molten_silver,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.aspectus_silver,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(new ItemStack(RegistryManager.shard_ember,1),new FluidStack(RegistryManager.fluid_molten_copper,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.aspectus_copper,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(new ItemStack(RegistryManager.shard_ember,1),new FluidStack(RegistryManager.fluid_molten_dawnstone,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.aspectus_dawnstone,1),false,false));
		
		mixingRecipes.add(new FluidMixingRecipe(new FluidStack[]{new FluidStack(RegistryManager.fluid_molten_copper,4),new FluidStack(RegistryManager.fluid_molten_gold,4)}, new FluidStack(RegistryManager.fluid_molten_dawnstone,16)));
	
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
