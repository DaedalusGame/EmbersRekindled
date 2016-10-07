package teamroots.embers.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import teamroots.embers.RegistryManager;
import teamroots.embers.item.EnumStampType;

public class RecipeRegistry {
	public static Map<ItemStack,ItemMeltingRecipe> meltingRecipes = new HashMap<ItemStack,ItemMeltingRecipe>();
	public static Map<String,ItemMeltingOreRecipe> meltingOreRecipes = new HashMap<String,ItemMeltingOreRecipe>();
	
	public static ArrayList<ItemStampingRecipe> stampingRecipes = new ArrayList<ItemStampingRecipe>();
	public static ArrayList<ItemStampingOreRecipe> stampingOreRecipes = new ArrayList<ItemStampingOreRecipe>();
	
	public static ArrayList<FluidMixingRecipe> mixingRecipes = new ArrayList<FluidMixingRecipe>();
	
	public static void init(){
		OreDictionary.registerOre("nuggetIron", RegistryManager.nuggetIron);
		OreDictionary.registerOre("ingotCopper", RegistryManager.ingotCopper);
		OreDictionary.registerOre("ingotLead", RegistryManager.ingotLead);
		OreDictionary.registerOre("ingotSilver", RegistryManager.ingotSilver);
		OreDictionary.registerOre("ingotDawnstone", RegistryManager.ingotDawnstone);
		OreDictionary.registerOre("nuggetCopper", RegistryManager.nuggetCopper);
		OreDictionary.registerOre("nuggetLead", RegistryManager.nuggetLead);
		OreDictionary.registerOre("nuggetSilver", RegistryManager.nuggetSilver);
		OreDictionary.registerOre("nuggetDawnstone", RegistryManager.nuggetDawnstone);
		OreDictionary.registerOre("plateIron", RegistryManager.plateIron);
		OreDictionary.registerOre("plateCopper", RegistryManager.plateCopper);
		OreDictionary.registerOre("plateLead", RegistryManager.plateLead);
		OreDictionary.registerOre("plateSilver", RegistryManager.plateSilver);
		OreDictionary.registerOre("plateDawnstone", RegistryManager.plateDawnstone);
		OreDictionary.registerOre("blockCopper", RegistryManager.blockCopper);
		OreDictionary.registerOre("blockLead", RegistryManager.blockLead);
		OreDictionary.registerOre("blockSilver", RegistryManager.blockSilver);
		OreDictionary.registerOre("blockDawnstone", RegistryManager.blockDawnstone);
		OreDictionary.registerOre("oreCopper", RegistryManager.oreCopper);
		OreDictionary.registerOre("oreLead", RegistryManager.oreLead);
		OreDictionary.registerOre("oreSilver", RegistryManager.oreSilver);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.blockCopper),true,new Object[]{
				"XXX",
				"XXX",
				"XXX",
				'X', "ingotCopper"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.blockLead),true,new Object[]{
				"XXX",
				"XXX",
				"XXX",
				'X', "ingotLead"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.blockSilver),true,new Object[]{
				"XXX",
				"XXX",
				"XXX",
				'X', "ingotSilver"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.blockDawnstone),true,new Object[]{
				"XXX",
				"XXX",
				"XXX",
				'X', "ingotDawnstone"}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ingotCopper),true,new Object[]{
				"XXX",
				"XXX",
				"XXX",
				'X', "nuggetCopper"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ingotLead),true,new Object[]{
				"XXX",
				"XXX",
				"XXX",
				'X', "nuggetLead"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ingotSilver),true,new Object[]{
				"XXX",
				"XXX",
				"XXX",
				'X', "nuggetSilver"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.ingotDawnstone,9),true,new Object[]{
				"XXX",
				"XXX",
				"XXX",
				'X', "nuggetDawnstone"}));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.ingotCopper,9),new Object[]{
				"blockCopper"}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.ingotLead,9),new Object[]{
				"blockLead"}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.ingotSilver,9),new Object[]{
				"blockSilver"}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.ingotDawnstone,9),new Object[]{
				"blockDawnstone"}));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.nuggetIron,9),new Object[]{
				"ingotIron"}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.nuggetCopper,9),new Object[]{
				"ingotCopper"}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.nuggetLead,9),new Object[]{
				"ingotLead"}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.nuggetSilver,9),new Object[]{
				"ingotSilver"}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.nuggetDawnstone,9),new Object[]{
				"ingotDawnstone"}));
		
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.plateIron,1),new Object[]{
		"ingotIron","ingotIron","ingotIron","ingotIron",RegistryManager.tinkerHammer}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.plateCopper,1),new Object[]{
		"ingotCopper","ingotCopper","ingotCopper","ingotCopper",RegistryManager.tinkerHammer}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.plateSilver,1),new Object[]{
		"ingotSilver","ingotSilver","ingotSilver","ingotSilver",RegistryManager.tinkerHammer}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.plateLead,1),new Object[]{
		"ingotLead","ingotLead","ingotLead","ingotLead",RegistryManager.tinkerHammer}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.plateDawnstone,1),new Object[]{
		"ingotDawnstone","ingotDawnstone","ingotDawnstone","ingotDawnstone",RegistryManager.tinkerHammer}));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegistryManager.blendCaminite,4),new Object[]{
		Blocks.GRAVEL,new ItemStack(Items.DYE,1,15)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.plateCaminiteRaw,1),true,new Object[]{
				"XX",
				"XX",
				'X', RegistryManager.blendCaminite}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.stampBarRaw,1),true,new Object[]{
				" X ",
				"X X",
				" X ",
				'X', RegistryManager.blendCaminite}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.stampFlatRaw,1),true,new Object[]{
				"XXX",
				"X X",
				"XXX",
				'X', RegistryManager.blendCaminite}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.stampPlateRaw,1),true,new Object[]{
				"X X",
				"   ",
				"X X",
				'X', RegistryManager.blendCaminite}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.blockCaminiteBrick,1),true,new Object[]{
				"XX",
				"XX",
				'X', RegistryManager.brickCaminite}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.stairsCaminiteBrick,4),true,new Object[]{
				"X  ",
				"XX ",
				"XXX",
				'X', RegistryManager.blockCaminiteBrick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.wallCaminiteBrick,6),true,new Object[]{
				"XXX",
				"XXX",
				'X', RegistryManager.blockCaminiteBrick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.blockTank,1),true,new Object[]{
				"B B",
				"P P",
				"BIB",
				'I', "ingotIron",
				'P', "plateIron",
				'B', RegistryManager.brickCaminite}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.pipe,8),true,new Object[]{
				"IPI",
				'P', "plateIron",
				'I', "ingotIron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.pump,1),true,new Object[]{
				" R ",
				"PBP",
				" R ",
				'P', RegistryManager.pipe,
				'B', RegistryManager.plateCaminite,
				'R', "dustRedstone"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.blockFurnace,1),true,new Object[]{
				"BPB",
				"BCB",
				"IFI",
				'P', RegistryManager.plateCaminite,
				'B', RegistryManager.brickCaminite,
				'F', Blocks.FURNACE,
				'I', "ingotIron",
				'C', "ingotCopper"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.emberReceiver,4),true,new Object[]{
				"I I",
				"CPC",
				'I', "ingotIron",
				'C', "ingotCopper",
				'P', RegistryManager.plateCaminite}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.emberEmitter,4),true,new Object[]{
				" C ",
				" C ",
				"IPI",
				'I', "ingotIron",
				'C', "ingotCopper",
				'P', RegistryManager.plateCaminite}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.copperCell,1),true,new Object[]{
				"BIB",
				"ICI",
				"BIB",
				'I', "ingotIron",
				'C', "blockCopper",
				'B', RegistryManager.blockCaminiteBrick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.itemPipe,8),true,new Object[]{
				"IPI",
				'P', "plateLead",
				'I', "ingotLead"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.itemPump,1),true,new Object[]{
				" R ",
				"PBP",
				" R ",
				'P', RegistryManager.itemPipe,
				'B', RegistryManager.plateCaminite,
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
				'Y', RegistryManager.blockCaminiteBrick,
				'X', RegistryManager.brickCaminite,
				'R', "dustRedstone"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.stampBase,1),true,new Object[]{
				"I I",
				"XBX",
				'I', "ingotIron",
				'B', Items.BUCKET,
				'X', RegistryManager.blockCaminiteBrick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.emberBore,1),true,new Object[]{
				"YCY",
				"YBY",
				"III",
				'I', "ingotIron",
				'B', RegistryManager.mechCore,
				'Y', RegistryManager.stairsCaminiteBrick,
				'C', "ingotCopper"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.mechCore,1),true,new Object[]{
				"IBI",
				" P ",
				"I I",
				'I', "ingotIron",
				'P', "plateLead",
				'B', Items.COMPASS}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.mechAccessor,1),true,new Object[]{
				"SPI",
				'P', "plateIron",
				'S', RegistryManager.stairsCaminiteBrick,
				'I', "ingotIron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.emberActivator,1),true,new Object[]{
				"CCC",
				"CCC",
				"IFI",
				'C', "ingotCopper",
				'F', Blocks.FURNACE,
				'I', "plateIron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.stoneEdge,1),true,new Object[]{
				"XXX",
				"Y Y",
				"XXX",
				'Y', RegistryManager.brickCaminite,
				'X', RegistryManager.wallCaminiteBrick}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.mixer,1),true,new Object[]{
				"PPP",
				"PCP",
				"IMI",
				'P', "plateIron",
				'C', "ingotCopper",
				'M', RegistryManager.mechCore,
				'I', "ingotIron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.heatCoil,1),true,new Object[]{
				"PPP",
				"IBI",
				" M ",
				'P', "plateCopper",
				'B', "blockCopper",
				'M', RegistryManager.mechCore,
				'I', "ingotIron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.itemDropper,1),true,new Object[]{
				" P ",
				"I I",
				'P', RegistryManager.pipe,
				'I', "ingotIron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.largeTank,1),true,new Object[]{
				"Y Y",
				"I I",
				"YTY",
				'Y', RegistryManager.stairsCaminiteBrick,
				'I', "ingotIron",
				'T', RegistryManager.blockTank}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.emberGauge,1),true,new Object[]{
				"B",
				"P",
				"C",
				'P', Items.PAPER,
				'I', "ingotIron",
				'B', Items.COMPASS,
				'C', "ingotCopper"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.itemGauge,1),true,new Object[]{
				"B",
				"P",
				"L",
				'P', Items.PAPER,
				'I', "ingotIron",
				'B', Items.COMPASS,
				'L', "ingotLead"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.fluidGauge,1),true,new Object[]{
				"B",
				"P",
				"I",
				'P', Items.PAPER,
				'I', "ingotIron",
				'B', Items.COMPASS}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.blockLantern,4),true,new Object[]{
				"P",
				"E",
				"I",
				'E', RegistryManager.shardEmber,
				'I', "ingotIron",
				'P', "plateIron"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.tinkerHammer,1),true,new Object[]{
				"IBI",
				"ISI",
				" S ",
				'B', "ingotLead",
				'I', "ingotIron",
				'S', "stickWood"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegistryManager.emberDetector,1),true,new Object[]{
				" I ",
				"CRC",
				"CIC",
				'C', "ingotCopper",
				'I', "ingotIron",
				'R', "dustRedstone"}));
		
		GameRegistry.addSmelting(new ItemStack(RegistryManager.oreCopper), new ItemStack(RegistryManager.ingotCopper), 0.65f);
		GameRegistry.addSmelting(new ItemStack(RegistryManager.oreSilver), new ItemStack(RegistryManager.ingotSilver), 0.35f);
		GameRegistry.addSmelting(new ItemStack(RegistryManager.oreLead), new ItemStack(RegistryManager.ingotLead), 0.35f);

		GameRegistry.addSmelting(new ItemStack(RegistryManager.blendCaminite), new ItemStack(RegistryManager.brickCaminite), 0.35f);
		GameRegistry.addSmelting(new ItemStack(RegistryManager.plateCaminiteRaw), new ItemStack(RegistryManager.plateCaminite), 0.35f);
		GameRegistry.addSmelting(new ItemStack(RegistryManager.stampBarRaw), new ItemStack(RegistryManager.stampBar), 0.35f);
		GameRegistry.addSmelting(new ItemStack(RegistryManager.stampPlateRaw), new ItemStack(RegistryManager.stampPlate), 0.35f);
		GameRegistry.addSmelting(new ItemStack(RegistryManager.stampFlatRaw), new ItemStack(RegistryManager.stampFlat), 0.35f);
		
		meltingOreRecipes.put("oreIron", new ItemMeltingOreRecipe("oreIron",new FluidStack(RegistryManager.fluidMoltenIron,288)));
		meltingOreRecipes.put("ingotIron", new ItemMeltingOreRecipe("ingotIron",new FluidStack(RegistryManager.fluidMoltenIron,144)));
		meltingOreRecipes.put("nuggetIron", new ItemMeltingOreRecipe("nuggetIron",new FluidStack(RegistryManager.fluidMoltenIron,16)));

		meltingOreRecipes.put("oreGold", new ItemMeltingOreRecipe("oreGold",new FluidStack(RegistryManager.fluidMoltenGold,288)));
		meltingOreRecipes.put("ingotGold", new ItemMeltingOreRecipe("ingotGold",new FluidStack(RegistryManager.fluidMoltenGold,144)));
		meltingOreRecipes.put("nuggetGold", new ItemMeltingOreRecipe("nuggetGold",new FluidStack(RegistryManager.fluidMoltenGold,16)));

		meltingOreRecipes.put("oreSilver", new ItemMeltingOreRecipe("oreSilver",new FluidStack(RegistryManager.fluidMoltenSilver,288)));
		meltingOreRecipes.put("ingotSilver", new ItemMeltingOreRecipe("ingotSilver",new FluidStack(RegistryManager.fluidMoltenSilver,144)));
		meltingOreRecipes.put("nuggetSilver", new ItemMeltingOreRecipe("nuggetSilver",new FluidStack(RegistryManager.fluidMoltenSilver,16)));

		meltingOreRecipes.put("oreCopper", new ItemMeltingOreRecipe("oreCopper",new FluidStack(RegistryManager.fluidMoltenCopper,288)));
		meltingOreRecipes.put("ingotCopper", new ItemMeltingOreRecipe("ingotCopper",new FluidStack(RegistryManager.fluidMoltenCopper,144)));
		meltingOreRecipes.put("nuggetCopper", new ItemMeltingOreRecipe("nuggetCopper",new FluidStack(RegistryManager.fluidMoltenCopper,16)));

		meltingOreRecipes.put("oreLead", new ItemMeltingOreRecipe("oreLead",new FluidStack(RegistryManager.fluidMoltenLead,288)));
		meltingOreRecipes.put("ingotLead", new ItemMeltingOreRecipe("ingotLead",new FluidStack(RegistryManager.fluidMoltenLead,144)));
		meltingOreRecipes.put("nuggetLead", new ItemMeltingOreRecipe("nuggetLead",new FluidStack(RegistryManager.fluidMoltenLead,16)));
		
		meltingOreRecipes.put("ingotDawnstone", new ItemMeltingOreRecipe("ingotDawnstone",new FluidStack(RegistryManager.fluidMoltenDawnstone,144)));
		meltingOreRecipes.put("nuggetDawnstone", new ItemMeltingOreRecipe("nuggetDawnstone",new FluidStack(RegistryManager.fluidMoltenDawnstone,16)));
		
		stampingRecipes.add(new ItemStampingRecipe(null,new FluidStack(RegistryManager.fluidMoltenIron,144),EnumStampType.TYPE_BAR,new ItemStack(Items.IRON_INGOT,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(null,new FluidStack(RegistryManager.fluidMoltenGold,144),EnumStampType.TYPE_BAR,new ItemStack(Items.GOLD_INGOT,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(null,new FluidStack(RegistryManager.fluidMoltenLead,144),EnumStampType.TYPE_BAR,new ItemStack(RegistryManager.ingotLead,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(null,new FluidStack(RegistryManager.fluidMoltenSilver,144),EnumStampType.TYPE_BAR,new ItemStack(RegistryManager.ingotSilver,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(null,new FluidStack(RegistryManager.fluidMoltenCopper,144),EnumStampType.TYPE_BAR,new ItemStack(RegistryManager.ingotCopper,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(null,new FluidStack(RegistryManager.fluidMoltenDawnstone,144),EnumStampType.TYPE_BAR,new ItemStack(RegistryManager.ingotDawnstone,1),false,false));
		
		stampingRecipes.add(new ItemStampingRecipe(null,new FluidStack(RegistryManager.fluidMoltenIron,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plateIron,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(null,new FluidStack(RegistryManager.fluidMoltenLead,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plateLead,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(null,new FluidStack(RegistryManager.fluidMoltenSilver,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plateSilver,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(null,new FluidStack(RegistryManager.fluidMoltenCopper,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plateCopper,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(null,new FluidStack(RegistryManager.fluidMoltenDawnstone,144),EnumStampType.TYPE_PLATE,new ItemStack(RegistryManager.plateDawnstone,1),false,false));
		
		mixingRecipes.add(new FluidMixingRecipe(new FluidStack[]{new FluidStack(RegistryManager.fluidMoltenCopper,4),new FluidStack(RegistryManager.fluidMoltenGold,4)}, new FluidStack(RegistryManager.fluidMoltenDawnstone,16)));
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
	
	public static FluidMixingRecipe getMixingRecipe(ArrayList<FluidStack> fluids){
		for (int i = 0; i < mixingRecipes.size(); i ++){
			if (mixingRecipes.get(i).matches(fluids)){
				return mixingRecipes.get(i);
			}
		}
		return null;
	}
}
