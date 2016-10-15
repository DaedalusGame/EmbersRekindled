package teamroots.embers;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import teamroots.embers.block.BlockActivator;
import teamroots.embers.block.BlockAdvancedEdge;
import teamroots.embers.block.BlockBase;
import teamroots.embers.block.BlockBeamSplitter;
import teamroots.embers.block.BlockBin;
import teamroots.embers.block.BlockCopperCell;
import teamroots.embers.block.BlockCrystalCell;
import teamroots.embers.block.BlockDeepLine;
import teamroots.embers.block.BlockDropper;
import teamroots.embers.block.BlockEmberBore;
import teamroots.embers.block.BlockEmberReceiver;
import teamroots.embers.block.BlockFluidGauge;
import teamroots.embers.block.BlockEmberEmitter;
import teamroots.embers.block.BlockEmberGauge;
import teamroots.embers.block.BlockFurnace;
import teamroots.embers.block.BlockHeatCoil;
import teamroots.embers.block.BlockItemGauge;
import teamroots.embers.block.BlockItemPipe;
import teamroots.embers.block.BlockItemPump;
import teamroots.embers.block.BlockLantern;
import teamroots.embers.block.BlockLargeTank;
import teamroots.embers.block.BlockMechAccessor;
import teamroots.embers.block.BlockMechCore;
import teamroots.embers.block.BlockMechEdge;
import teamroots.embers.block.BlockMixer;
import teamroots.embers.block.BlockOven;
import teamroots.embers.block.BlockPipe;
import teamroots.embers.block.BlockPump;
import teamroots.embers.block.BlockRelay;
import teamroots.embers.block.BlockStairsBase;
import teamroots.embers.block.BlockStampBase;
import teamroots.embers.block.BlockStamper;
import teamroots.embers.block.BlockStoneEdge;
import teamroots.embers.block.BlockTEBase;
import teamroots.embers.block.BlockTank;
import teamroots.embers.block.BlockWallBase;
import teamroots.embers.block.IModeledBlock;
import teamroots.embers.block.MaterialUnpushable;
import teamroots.embers.block.fluid.BlockMoltenAstralite;
import teamroots.embers.block.fluid.BlockMoltenCopper;
import teamroots.embers.block.fluid.BlockMoltenDawnstone;
import teamroots.embers.block.fluid.BlockMoltenGold;
import teamroots.embers.block.fluid.BlockMoltenIron;
import teamroots.embers.block.fluid.BlockMoltenLead;
import teamroots.embers.block.fluid.BlockMoltenSilver;
import teamroots.embers.block.fluid.BlockMoltenUmberSteel;
import teamroots.embers.entity.EntityEmberPacket;
import teamroots.embers.entity.RenderEmberPacket;
import teamroots.embers.fluid.FluidMoltenAstralite;
import teamroots.embers.fluid.FluidMoltenCopper;
import teamroots.embers.fluid.FluidMoltenDawnstone;
import teamroots.embers.fluid.FluidMoltenGold;
import teamroots.embers.fluid.FluidMoltenIron;
import teamroots.embers.fluid.FluidMoltenLead;
import teamroots.embers.fluid.FluidMoltenSilver;
import teamroots.embers.fluid.FluidMoltenUmberSteel;
import teamroots.embers.item.IModeledItem;
import teamroots.embers.item.ItemAxeBase;
import teamroots.embers.item.ItemBase;
import teamroots.embers.item.ItemDebug;
import teamroots.embers.item.ItemEmberGauge;
import teamroots.embers.item.ItemHoeBase;
import teamroots.embers.item.ItemPickaxeBase;
import teamroots.embers.item.ItemShovelBase;
import teamroots.embers.item.ItemSwordBase;
import teamroots.embers.item.ItemTinkerHammer;
import teamroots.embers.particle.ParticleRenderer;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityStorage;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.tileentity.TileEntityActivatorBottom;
import teamroots.embers.tileentity.TileEntityActivatorTop;
import teamroots.embers.tileentity.TileEntityBeamSplitter;
import teamroots.embers.tileentity.TileEntityBin;
import teamroots.embers.tileentity.TileEntityBinRenderer;
import teamroots.embers.tileentity.TileEntityCopperCell;
import teamroots.embers.tileentity.TileEntityCrystalCell;
import teamroots.embers.tileentity.TileEntityCrystalCellRenderer;
import teamroots.embers.tileentity.TileEntityDeepLine;
import teamroots.embers.tileentity.TileEntityDropper;
import teamroots.embers.tileentity.TileEntityEmberBore;
import teamroots.embers.tileentity.TileEntityEmberBoreRenderer;
import teamroots.embers.tileentity.TileEntityEmitter;
import teamroots.embers.tileentity.TileEntityEmitterRenderer;
import teamroots.embers.tileentity.TileEntityFurnaceBottom;
import teamroots.embers.tileentity.TileEntityFurnaceTop;
import teamroots.embers.tileentity.TileEntityFurnaceTopRenderer;
import teamroots.embers.tileentity.TileEntityHeatCoil;
import teamroots.embers.tileentity.TileEntityHeatCoilRenderer;
import teamroots.embers.tileentity.TileEntityItemPipe;
import teamroots.embers.tileentity.TileEntityItemPipeRenderer;
import teamroots.embers.tileentity.TileEntityItemPump;
import teamroots.embers.tileentity.TileEntityItemPumpRenderer;
import teamroots.embers.tileentity.TileEntityLantern;
import teamroots.embers.tileentity.TileEntityLargeTank;
import teamroots.embers.tileentity.TileEntityLargeTankRenderer;
import teamroots.embers.tileentity.TileEntityMechAccessor;
import teamroots.embers.tileentity.TileEntityMechCore;
import teamroots.embers.tileentity.TileEntityMixerBottom;
import teamroots.embers.tileentity.TileEntityMixerTop;
import teamroots.embers.tileentity.TileEntityOven;
import teamroots.embers.tileentity.TileEntityOvenRenderer;
import teamroots.embers.tileentity.TileEntityPipe;
import teamroots.embers.tileentity.TileEntityPipeRenderer;
import teamroots.embers.tileentity.TileEntityPump;
import teamroots.embers.tileentity.TileEntityPumpRenderer;
import teamroots.embers.tileentity.TileEntityReceiver;
import teamroots.embers.tileentity.TileEntityRelay;
import teamroots.embers.tileentity.TileEntityStampBase;
import teamroots.embers.tileentity.TileEntityStampBaseRenderer;
import teamroots.embers.tileentity.TileEntityStamper;
import teamroots.embers.tileentity.TileEntityStamperRenderer;
import teamroots.embers.tileentity.TileEntityTank;
import teamroots.embers.tileentity.TileEntityTankRenderer;
import teamroots.embers.world.WorldGenOres;
import teamroots.embers.world.dimension.BiomeCave;
import teamroots.embers.world.dimension.CaveProvider;

public class RegistryManager {
	public static ArrayList<Block> blocks = new ArrayList<Block>();
	public static ArrayList<Item> items = new ArrayList<Item>();
	
	public static ToolMaterial toolMatCopper, toolMatSilver, toolMatLead, toolMatDawnstone;
	
	public static Block crystalCell, advancedEdge, emberRelay, beamSplitter, blockLantern, emberGauge, itemGauge, fluidGauge, largeTank, itemDropper, heatCoil, wallCaminiteBrick, blockDawnstone, mixer, stoneEdge, emberActivator, mechCore, stairsCaminiteBrick, mechAccessor, emberBore, mechEdge, itemPump, itemPipe, blockOven, stampBase, stamper, blockCaminiteLargeBrick, bin, copperCell, deepLine, emberEmitter, emberReceiver, blockFurnace, pump, blockCopper, blockLead, blockSilver, oreCopper, oreLead, oreSilver, blockCaminiteBrick, blockTank, pipe;
	public static Block blockMoltenAstralite, blockMoltenDawnstone, blockMoltenUmberSteel, blockMoltenGold, blockMoltenCopper, blockMoltenLead, blockMoltenSilver, blockMoltenIron;
	
	public static Fluid fluidMoltenAstralite, fluidMoltenDawnstone, fluidMoltenUmberSteel, fluidMoltenGold, fluidMoltenCopper, fluidMoltenLead, fluidMoltenSilver, fluidMoltenIron;
	
	public static Item pickaxeCopper, axeCopper, shovelCopper, hoeCopper, swordCopper, pickaxeSilver, axeSilver, shovelSilver, hoeSilver, swordSilver, pickaxeLead, axeLead, shovelLead, hoeLead, swordLead, pickaxeDawnstone, axeDawnstone, shovelDawnstone, hoeDawnstone, swordDawnstone, debug, plateGold, plateIron, plateCaminiteRaw, stampBarRaw, stampPlateRaw, stampFlatRaw, nuggetDawnstone, plateCopper, plateLead, plateSilver, plateDawnstone, nuggetIron, ingotAstralite, ingotDawnstone, ingotUmberSteel, crystalEmber, shardEmber, stampBar, stampPlate, stampFlat, tinkerHammer, emberDetector, ingotCopper, ingotSilver, ingotLead, nuggetCopper, nuggetSilver, nuggetLead, brickCaminite, blendCaminite, plateCaminite;
	
	public static Material unpushable;
	
	public static Biome biomeCave;
	
	public static DimensionType dimensionCave;
	
	public static WorldGenOres worldGenOres;
	
	public static void registerAll(){
		CapabilityManager.INSTANCE.register(IEmberCapability.class, new EmberCapabilityStorage(), DefaultEmberCapability.class);
		
		toolMatCopper = EnumHelper.addToolMaterial(Embers.MODID+":copper", 2, 181, 5.4f, 1.5f, 16);
		toolMatSilver = EnumHelper.addToolMaterial(Embers.MODID+":silver", 2, 202, 7.6f, 2.0f, 20);
		toolMatLead = EnumHelper.addToolMaterial(Embers.MODID+":lead", 2, 168, 6.0f, 2.0f, 4);
		toolMatDawnstone = EnumHelper.addToolMaterial(Embers.MODID+":dawnstone", 2, 644, 7.5f, 2.5f, 18);
		
		unpushable = new MaterialUnpushable();
		
		blocks.add(blockCopper = (new BlockBase(Material.ROCK,"blockCopper",true)).setHarvestProperties("pickaxe", 1).setHardness(1.4f));
		blocks.add(blockLead = (new BlockBase(Material.ROCK,"blockLead",true)).setHarvestProperties("pickaxe", 2).setHardness(1.6f));
		blocks.add(blockSilver = (new BlockBase(Material.ROCK,"blockSilver",true)).setHarvestProperties("pickaxe", 2).setHardness(1.6f));
		blocks.add(blockDawnstone = (new BlockBase(Material.ROCK,"blockDawnstone",true)).setHarvestProperties("pickaxe", 2).setHardness(1.6f).setLightLevel(0.0625f));
		blocks.add(oreCopper = (new BlockBase(Material.ROCK,"oreCopper",true)).setHarvestProperties("pickaxe", 1).setHardness(1.8f));
		blocks.add(oreLead = (new BlockBase(Material.ROCK,"oreLead",true)).setHarvestProperties("pickaxe", 2).setHardness(2.5f));
		blocks.add(oreSilver = (new BlockBase(Material.ROCK,"oreSilver",true)).setHarvestProperties("pickaxe", 2).setHardness(2.5f));
		blocks.add(blockCaminiteBrick = (new BlockBase(Material.ROCK,"blockCaminiteBrick",true)).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(stairsCaminiteBrick = (new BlockStairsBase(RegistryManager.blockCaminiteBrick.getDefaultState(),"stairsCaminiteBrick",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(wallCaminiteBrick = (new BlockWallBase(RegistryManager.blockCaminiteBrick,"wallCaminiteBrick",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		//blocks.add(blockCaminiteLargeBrick = (new BlockBase(Material.ROCK,"blockCaminiteLargeBrick",true)).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(blockTank = (new BlockTank(Material.ROCK,"blockTank",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(pipe = (new BlockPipe(Material.ROCK,"pipe",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(pump = (new BlockPump(Material.ROCK,"pump",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(blockFurnace = (new BlockFurnace(Material.ROCK,"blockFurnace",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(emberReceiver = (new BlockEmberReceiver(Material.ROCK,"emberReceiver",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(0.6f));
		blocks.add(emberEmitter = (new BlockEmberEmitter(Material.ROCK,"emberEmitter",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(0.6f));
		blocks.add(copperCell = (new BlockCopperCell(Material.ROCK,"copperCell",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.4f));
		blocks.add(itemPipe = (new BlockItemPipe(Material.ROCK,"itemPipe",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(itemPump = (new BlockItemPump(Material.ROCK,"itemPump",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(bin = (new BlockBin(Material.ROCK,"bin",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(stamper = (new BlockStamper(Material.ROCK,"stamper",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(stampBase = (new BlockStampBase(Material.ROCK,"stamperBase",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(mechEdge = (new BlockMechEdge(unpushable,"mechEdge",false)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(emberBore = (new BlockEmberBore(Material.ROCK,"emberBore",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(mechAccessor = (new BlockMechAccessor(Material.ROCK,"mechAccessor",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(mechCore = (new BlockMechCore(Material.ROCK,"mechCore",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(emberActivator = (new BlockActivator(Material.ROCK,"emberActivator",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(stoneEdge = (new BlockStoneEdge(unpushable,"stoneEdge",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(mixer = (new BlockMixer(Material.ROCK,"mixer",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(heatCoil = (new BlockHeatCoil(Material.ROCK,"heatCoil",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(itemDropper = (new BlockDropper(Material.ROCK,"itemDropper",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(largeTank = (new BlockLargeTank(Material.ROCK,"largeTank",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(emberGauge = (new BlockEmberGauge(Material.ROCK,"emberGauge",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(itemGauge = (new BlockItemGauge(Material.ROCK,"itemGauge",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(fluidGauge = (new BlockFluidGauge(Material.ROCK,"fluidGauge",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(blockLantern = (new BlockLantern(Material.ROCK,"blockLantern",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f).setLightLevel(1.0f));
		blocks.add(beamSplitter = (new BlockBeamSplitter(Material.ROCK,"beamSplitter",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(emberRelay = (new BlockRelay(Material.ROCK,"emberRelay",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(advancedEdge = (new BlockAdvancedEdge(unpushable,"advancedEdge",false)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(crystalCell = (new BlockCrystalCell(Material.ROCK,"crystalCell",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		
		/*FluidRegistry.registerFluid(fluidMoltenUmberSteel = new FluidMoltenUmberSteel());
		blocks.add(blockMoltenUmberSteel = (new BlockMoltenUmberSteel("moltenUmberSteel",false)));
		FluidRegistry.addBucketForFluid(fluidMoltenUmberSteel);
		
		FluidRegistry.registerFluid(fluidMoltenAstralite = new FluidMoltenAstralite());
		blocks.add(blockMoltenAstralite = (new BlockMoltenAstralite("moltenAstralite",false)));
		FluidRegistry.addBucketForFluid(fluidMoltenAstralite);*/
		
		items.add(ingotCopper = new ItemBase("ingotCopper",true));
		items.add(ingotLead = new ItemBase("ingotLead",true));
		items.add(ingotSilver = new ItemBase("ingotSilver",true));
		items.add(ingotDawnstone = new ItemBase("ingotDawnstone",true));
		items.add(nuggetIron = new ItemBase("nuggetIron",true));
		items.add(nuggetCopper = new ItemBase("nuggetCopper",true));
		items.add(nuggetLead = new ItemBase("nuggetLead",true));
		items.add(nuggetSilver = new ItemBase("nuggetSilver",true));
		items.add(nuggetDawnstone = new ItemBase("nuggetDawnstone",true));
		items.add(plateCopper = new ItemBase("plateCopper",true));
		items.add(plateLead = new ItemBase("plateLead",true));
		items.add(plateSilver = new ItemBase("plateSilver",true));
		items.add(plateDawnstone = new ItemBase("plateDawnstone",true));
		items.add(plateIron = new ItemBase("plateIron",true));
		items.add(plateGold = new ItemBase("plateGold",true));
		items.add(brickCaminite = new ItemBase("brickCaminite",true));
		items.add(blendCaminite = new ItemBase("blendCaminite",true));
		items.add(plateCaminite = new ItemBase("plateCaminite",true));
		items.add(plateCaminiteRaw = new ItemBase("plateCaminiteRaw",true));
		items.add(tinkerHammer = new ItemTinkerHammer());
		items.add(stampBar = new ItemBase("stampBar",true));
		items.add(stampFlat = new ItemBase("stampFlat",true));
		items.add(stampPlate = new ItemBase("stampPlate",true));
		items.add(stampBarRaw = new ItemBase("stampBarRaw",true));
		items.add(stampFlatRaw = new ItemBase("stampFlatRaw",true));
		items.add(stampPlateRaw = new ItemBase("stampPlateRaw",true));
		items.add(emberDetector = new ItemEmberGauge());
		items.add(shardEmber = new ItemBase("shardEmber",true));
		items.add(crystalEmber = new ItemBase("crystalEmber",true));
		items.add(debug = new ItemDebug());
		items.add(pickaxeCopper = new ItemPickaxeBase(toolMatCopper,"pickaxeCopper",true));
		items.add(axeCopper = new ItemAxeBase(toolMatCopper,"axeCopper",true));
		items.add(shovelCopper = new ItemShovelBase(toolMatCopper,"shovelCopper",true));
		items.add(hoeCopper = new ItemHoeBase(toolMatCopper,"hoeCopper",true));
		items.add(swordCopper = new ItemSwordBase(toolMatCopper,"swordCopper",true));
		items.add(pickaxeSilver = new ItemPickaxeBase(toolMatSilver,"pickaxeSilver",true));
		items.add(axeSilver = new ItemAxeBase(toolMatSilver,"axeSilver",true));
		items.add(shovelSilver = new ItemShovelBase(toolMatSilver,"shovelSilver",true));
		items.add(hoeSilver = new ItemHoeBase(toolMatSilver,"hoeSilver",true));
		items.add(swordSilver = new ItemSwordBase(toolMatSilver,"swordSilver",true));
		items.add(pickaxeLead = new ItemPickaxeBase(toolMatLead,"pickaxeLead",true));
		items.add(axeLead = new ItemAxeBase(toolMatLead,"axeLead",true));
		items.add(shovelLead = new ItemShovelBase(toolMatLead,"shovelLead",true));
		items.add(hoeLead = new ItemHoeBase(toolMatLead,"hoeLead",true));
		items.add(swordLead = new ItemSwordBase(toolMatLead,"swordLead",true));
		items.add(pickaxeDawnstone = new ItemPickaxeBase(toolMatDawnstone,"pickaxeDawnstone",true));
		items.add(axeDawnstone = new ItemAxeBase(toolMatDawnstone,"axeDawnstone",true));
		items.add(shovelDawnstone = new ItemShovelBase(toolMatDawnstone,"shovelDawnstone",true));
		items.add(hoeDawnstone = new ItemHoeBase(toolMatDawnstone,"hoeDawnstone",true));
		items.add(swordDawnstone = new ItemSwordBase(toolMatDawnstone,"swordDawnstone",true));
		
		toolMatCopper.setRepairItem(new ItemStack(ingotCopper));
		toolMatSilver.setRepairItem(new ItemStack(ingotSilver));
		toolMatLead.setRepairItem(new ItemStack(ingotLead));
		toolMatDawnstone.setRepairItem(new ItemStack(ingotDawnstone));
		/*
		items.add(ingotAstralite = new ItemBase("ingotAstralite",true));
		items.add(ingotUmberSteel = new ItemBase("ingotUmberSteel",true));*/
		
		registerFluids();
		
		GameRegistry.registerTileEntity(TileEntityTank.class, Embers.MODID+":tileEntityTank");
		GameRegistry.registerTileEntity(TileEntityPipe.class, Embers.MODID+":tileEntityPipe");
		GameRegistry.registerTileEntity(TileEntityPump.class, Embers.MODID+":tileEntityPump");
		GameRegistry.registerTileEntity(TileEntityFurnaceTop.class, Embers.MODID+":tileEntityFurnaceTop");
		GameRegistry.registerTileEntity(TileEntityFurnaceBottom.class, Embers.MODID+":tileEntityFurnaceBottom");
		GameRegistry.registerTileEntity(TileEntityEmitter.class, Embers.MODID+":tileEntityEmitter");
		GameRegistry.registerTileEntity(TileEntityReceiver.class, Embers.MODID+":tileEntityReceiver");
		GameRegistry.registerTileEntity(TileEntityDeepLine.class, Embers.MODID+":tileEntityDeepLine");
		GameRegistry.registerTileEntity(TileEntityCopperCell.class, Embers.MODID+":tileEntityCopperCell");
		GameRegistry.registerTileEntity(TileEntityOven.class, Embers.MODID+":tileEntityOven");
		GameRegistry.registerTileEntity(TileEntityItemPipe.class, Embers.MODID+":tileEntityItemPipe");
		GameRegistry.registerTileEntity(TileEntityItemPump.class, Embers.MODID+":tileEntityItemPump");
		GameRegistry.registerTileEntity(TileEntityBin.class, Embers.MODID+":tileEntityBin");
		GameRegistry.registerTileEntity(TileEntityStamper.class, Embers.MODID+":tileEntityStamper");
		GameRegistry.registerTileEntity(TileEntityStampBase.class, Embers.MODID+":tileEntityStampBase");
		GameRegistry.registerTileEntity(TileEntityEmberBore.class, Embers.MODID+":tileEntityEmberBore");
		GameRegistry.registerTileEntity(TileEntityMechAccessor.class, Embers.MODID+":tileEntityMechAccessor");
		GameRegistry.registerTileEntity(TileEntityMechCore.class, Embers.MODID+":tileEntityMechCore");
		GameRegistry.registerTileEntity(TileEntityActivatorTop.class, Embers.MODID+":tileEntityActivatorTop");
		GameRegistry.registerTileEntity(TileEntityActivatorBottom.class, Embers.MODID+":tileEntityActivatorBottom");
		GameRegistry.registerTileEntity(TileEntityMixerTop.class, Embers.MODID+":tileEntityMixerTop");
		GameRegistry.registerTileEntity(TileEntityMixerBottom.class, Embers.MODID+":tileEntityMixerBottom");
		GameRegistry.registerTileEntity(TileEntityHeatCoil.class, Embers.MODID+":tileEntityHeatCoil");
		GameRegistry.registerTileEntity(TileEntityDropper.class, Embers.MODID+":tileEntityDropper");
		GameRegistry.registerTileEntity(TileEntityLargeTank.class, Embers.MODID+":tileEntityLargeTank");
		GameRegistry.registerTileEntity(TileEntityLantern.class, Embers.MODID+":tileEntityLantern");
		GameRegistry.registerTileEntity(TileEntityBeamSplitter.class, Embers.MODID+":tileEntityBeamSplitter");
		GameRegistry.registerTileEntity(TileEntityRelay.class, Embers.MODID+":tileEntityRelay");
		GameRegistry.registerTileEntity(TileEntityCrystalCell.class, Embers.MODID+":tileEntityCrystalCell");
		
		int id = 0;
		
		EntityRegistry.registerModEntity(EntityEmberPacket.class, "emberPacket", id++, Embers.instance, 64, 1, true);
		
		worldGenOres = new WorldGenOres();
		GameRegistry.registerWorldGenerator(worldGenOres, 1);
		
		OreDictionary.registerOre("nuggetIron", nuggetIron);
		
		//GameRegistry.register(biomeCave = new BiomeCave());
		
		//dimensionCave = DimensionType.register("cave", "cave", 90, CaveProvider.class, false);
		//BiomeManager.addBiome(BiomeType.DESERT, new BiomeEntry(biomeCave, 10000));
	}
	
	public static void registerFluids(){
		FluidRegistry.registerFluid(fluidMoltenIron = new FluidMoltenIron());
		blocks.add(blockMoltenIron = (new BlockMoltenIron("moltenIron",false)));
		FluidRegistry.addBucketForFluid(fluidMoltenIron);
		
		FluidRegistry.registerFluid(fluidMoltenGold = new FluidMoltenGold());
		blocks.add(blockMoltenGold = (new BlockMoltenGold("moltenGold",false)));
		FluidRegistry.addBucketForFluid(fluidMoltenGold);
		
		FluidRegistry.registerFluid(fluidMoltenLead = new FluidMoltenLead());
		blocks.add(blockMoltenLead = (new BlockMoltenLead("moltenLead",false)));
		FluidRegistry.addBucketForFluid(fluidMoltenLead);
		
		FluidRegistry.registerFluid(fluidMoltenCopper = new FluidMoltenCopper());
		blocks.add(blockMoltenCopper = (new BlockMoltenCopper("moltenCopper",false)));
		FluidRegistry.addBucketForFluid(fluidMoltenCopper);
		
		FluidRegistry.registerFluid(fluidMoltenSilver = new FluidMoltenSilver());
		blocks.add(blockMoltenSilver = (new BlockMoltenSilver("moltenSilver",false)));
		FluidRegistry.addBucketForFluid(fluidMoltenSilver);
		
		
		FluidRegistry.registerFluid(fluidMoltenDawnstone = new FluidMoltenDawnstone());
		blocks.add(blockMoltenDawnstone = (new BlockMoltenDawnstone("moltenDawnstone",false)));
		FluidRegistry.addBucketForFluid(fluidMoltenDawnstone);
	}
	
	@SideOnly(Side.CLIENT)
    public static void registerRendering(){
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTank.class, new TileEntityTankRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPipe.class, new TileEntityPipeRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPump.class, new TileEntityPumpRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFurnaceTop.class, new TileEntityFurnaceTopRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEmitter.class, new TileEntityEmitterRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityOven.class, new TileEntityOvenRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityItemPipe.class, new TileEntityItemPipeRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityItemPump.class, new TileEntityItemPumpRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBin.class, new TileEntityBinRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStamper.class, new TileEntityStamperRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStampBase.class, new TileEntityStampBaseRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEmberBore.class, new TileEntityEmberBoreRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHeatCoil.class, new TileEntityHeatCoilRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLargeTank.class, new TileEntityLargeTankRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrystalCell.class, new TileEntityCrystalCellRenderer());
		
		RenderingRegistry.registerEntityRenderingHandler(EntityEmberPacket.class, new RenderEmberPacket(Minecraft.getMinecraft().getRenderManager()));
		
		for (int i = 0; i < blocks.size(); i ++){
			if (blocks.get(i) instanceof IModeledBlock){
				((IModeledBlock)blocks.get(i)).initModel();
			}
		}
		for (int i = 0; i < items.size(); i ++){
			if (items.get(i) instanceof IModeledItem){
				((IModeledItem)items.get(i)).initModel();
			}
		}
	}
}
