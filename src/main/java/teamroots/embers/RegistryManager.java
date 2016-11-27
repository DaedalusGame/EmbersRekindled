package teamroots.embers;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
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
import teamroots.embers.block.BlockAlchemyPedestal;
import teamroots.embers.block.BlockAlchemyTablet;
import teamroots.embers.block.BlockBase;
import teamroots.embers.block.BlockBeamSplitter;
import teamroots.embers.block.BlockBin;
import teamroots.embers.block.BlockCharger;
import teamroots.embers.block.BlockCinderPlinth;
import teamroots.embers.block.BlockCopperCell;
import teamroots.embers.block.BlockCrystalCell;
import teamroots.embers.block.BlockDoubleSlabBase;
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
import teamroots.embers.block.BlockKnowledgeTable;
import teamroots.embers.block.BlockLantern;
import teamroots.embers.block.BlockLargeTank;
import teamroots.embers.block.BlockMechAccessor;
import teamroots.embers.block.BlockMechCore;
import teamroots.embers.block.BlockMechEdge;
import teamroots.embers.block.BlockMixer;
import teamroots.embers.block.BlockPipe;
import teamroots.embers.block.BlockPump;
import teamroots.embers.block.BlockRelay;
import teamroots.embers.block.BlockSlabBase;
import teamroots.embers.block.BlockStairsBase;
import teamroots.embers.block.BlockStampBase;
import teamroots.embers.block.BlockStamper;
import teamroots.embers.block.BlockStoneEdge;
import teamroots.embers.block.BlockTank;
import teamroots.embers.block.BlockWallBase;
import teamroots.embers.block.IModeledBlock;
import teamroots.embers.block.MaterialUnpushable;
import teamroots.embers.block.fluid.BlockMoltenCopper;
import teamroots.embers.block.fluid.BlockMoltenDawnstone;
import teamroots.embers.block.fluid.BlockMoltenGold;
import teamroots.embers.block.fluid.BlockMoltenIron;
import teamroots.embers.block.fluid.BlockMoltenLead;
import teamroots.embers.block.fluid.BlockMoltenSilver;
import teamroots.embers.damage.DamageEmber;
import teamroots.embers.entity.EntityEmberPacket;
import teamroots.embers.entity.EntityEmberProjectile;
import teamroots.embers.entity.RenderEmberPacket;
import teamroots.embers.fluid.FluidMoltenCopper;
import teamroots.embers.fluid.FluidMoltenDawnstone;
import teamroots.embers.fluid.FluidMoltenGold;
import teamroots.embers.fluid.FluidMoltenIron;
import teamroots.embers.fluid.FluidMoltenLead;
import teamroots.embers.fluid.FluidMoltenSilver;
import teamroots.embers.item.IModeledItem;
import teamroots.embers.item.ItemAxeBase;
import teamroots.embers.item.ItemBase;
import teamroots.embers.item.ItemCinderStaff;
import teamroots.embers.item.ItemClockworkAxe;
import teamroots.embers.item.ItemClockworkPickaxe;
import teamroots.embers.item.ItemDebug;
import teamroots.embers.item.ItemEmberCartridge;
import teamroots.embers.item.ItemEmberGauge;
import teamroots.embers.item.ItemEmberJar;
import teamroots.embers.item.ItemGolemsEye;
import teamroots.embers.item.ItemGrandhammer;
import teamroots.embers.item.ItemHoeBase;
import teamroots.embers.item.ItemIgnitionCannon;
import teamroots.embers.item.ItemPickaxeBase;
import teamroots.embers.item.ItemShovelBase;
import teamroots.embers.item.ItemSwordBase;
import teamroots.embers.item.ItemTinkerHammer;
import teamroots.embers.item.block.ItemBlockSlab;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityStorage;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.tileentity.TileEntityActivatorBottom;
import teamroots.embers.tileentity.TileEntityActivatorTop;
import teamroots.embers.tileentity.TileEntityAlchemyPedestal;
import teamroots.embers.tileentity.TileEntityAlchemyPedestalRenderer;
import teamroots.embers.tileentity.TileEntityAlchemyTablet;
import teamroots.embers.tileentity.TileEntityAlchemyTabletRenderer;
import teamroots.embers.tileentity.TileEntityBeamSplitter;
import teamroots.embers.tileentity.TileEntityBin;
import teamroots.embers.tileentity.TileEntityBinRenderer;
import teamroots.embers.tileentity.TileEntityCharger;
import teamroots.embers.tileentity.TileEntityChargerRenderer;
import teamroots.embers.tileentity.TileEntityCinderPlinth;
import teamroots.embers.tileentity.TileEntityCinderPlinthRenderer;
import teamroots.embers.tileentity.TileEntityCopperCell;
import teamroots.embers.tileentity.TileEntityCrystalCell;
import teamroots.embers.tileentity.TileEntityCrystalCellRenderer;
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
import teamroots.embers.tileentity.TileEntityKnowledgeTable;
import teamroots.embers.tileentity.TileEntityKnowledgeTableRenderer;
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

public class RegistryManager {
	public static ArrayList<Block> blocks = new ArrayList<Block>();
	public static ArrayList<Item> items = new ArrayList<Item>();
	
	public static ToolMaterial toolMatCopper, toolMatSilver, toolMatLead, toolMatDawnstone;
	
	public static Block alchemyTablet, alchemyPedestal, knowledgeTable, cinderPlinth, ashenTile, stairsAshenTile, wallAshenTile, ashenTileSlab, ashenTileSlabDouble, ashenStone, ashenBrick, stairsAshenStone, wallAshenStone, ashenStoneSlab, ashenStoneSlabDouble, stairsAshenBrick, wallAshenBrick, ashenBrickSlab, ashenBrickSlabDouble, blockCaminiteBrickSlab, blockCaminiteBrickSlabDouble, charger, crystalCell, advancedEdge, emberRelay, beamSplitter, blockLantern, emberGauge, itemGauge, fluidGauge, largeTank, itemDropper, heatCoil, wallCaminiteBrick, blockDawnstone, mixer, stoneEdge, emberActivator, mechCore, stairsCaminiteBrick, mechAccessor, emberBore, mechEdge, itemPump, itemPipe, blockOven, stampBase, stamper, blockCaminiteLargeBrick, bin, copperCell, deepLine, emberEmitter, emberReceiver, blockFurnace, pump, blockCopper, blockLead, blockSilver, oreCopper, oreLead, oreSilver, blockCaminiteBrick, blockTank, pipe;
	public static Block blockMoltenAstralite, blockMoltenDawnstone, blockMoltenUmberSteel, blockMoltenGold, blockMoltenCopper, blockMoltenLead, blockMoltenSilver, blockMoltenIron;
	
	public static Fluid fluidMoltenAstralite, fluidMoltenDawnstone, fluidMoltenUmberSteel, fluidMoltenGold, fluidMoltenCopper, fluidMoltenLead, fluidMoltenSilver, fluidMoltenIron;
	
	public static Item golemsEye, dustAsh, grandhammer, pickaxeClockwork, axeClockwork, staffEmber, ignitionCannon, emberJar, emberCartridge, pickaxeCopper, axeCopper, shovelCopper, hoeCopper, swordCopper, pickaxeSilver, axeSilver, shovelSilver, hoeSilver, swordSilver, pickaxeLead, axeLead, shovelLead, hoeLead, swordLead, pickaxeDawnstone, axeDawnstone, shovelDawnstone, hoeDawnstone, swordDawnstone, debug, plateGold, plateIron, plateCaminiteRaw, stampBarRaw, stampPlateRaw, stampFlatRaw, nuggetDawnstone, plateCopper, plateLead, plateSilver, plateDawnstone, nuggetIron, ingotAstralite, ingotDawnstone, ingotUmberSteel, crystalEmber, shardEmber, stampBar, stampPlate, stampFlat, tinkerHammer, emberDetector, ingotCopper, ingotSilver, ingotLead, nuggetCopper, nuggetSilver, nuggetLead, brickCaminite, blendCaminite, plateCaminite;
	
	public static DamageSource damageEmber;
	
	public static Material unpushable;
	
	public static Biome biomeCave;
	
	public static DimensionType dimensionCave;
	
	public static WorldGenOres worldGenOres;
	
	public static void registerAll(){
		CapabilityManager.INSTANCE.register(IEmberCapability.class, new EmberCapabilityStorage(), DefaultEmberCapability.class);
		
		damageEmber = new DamageEmber();
		
		toolMatCopper = EnumHelper.addToolMaterial(Embers.MODID+":copper", 2, 181, 5.4f, 1.5f, 16);
		toolMatSilver = EnumHelper.addToolMaterial(Embers.MODID+":silver", 2, 202, 7.6f, 2.0f, 20);
		toolMatLead = EnumHelper.addToolMaterial(Embers.MODID+":lead", 2, 168, 6.0f, 2.0f, 4);
		toolMatDawnstone = EnumHelper.addToolMaterial(Embers.MODID+":dawnstone", 2, 644, 7.5f, 2.5f, 18);
		
		unpushable = new MaterialUnpushable();
		
		blocks.add(blockCopper = (new BlockBase(Material.ROCK,"blockCopper",true)).setHarvestProperties("pickaxe", 1).setHardness(1.4f).setLightOpacity(16));
		blocks.add(blockLead = (new BlockBase(Material.ROCK,"blockLead",true)).setHarvestProperties("pickaxe", 2).setHardness(1.6f).setLightOpacity(16));
		blocks.add(blockSilver = (new BlockBase(Material.ROCK,"blockSilver",true)).setHarvestProperties("pickaxe", 2).setHardness(1.6f).setLightOpacity(16));
		blocks.add(blockDawnstone = (new BlockBase(Material.ROCK,"blockDawnstone",true)).setHarvestProperties("pickaxe", 2).setHardness(1.6f).setLightLevel(0.0625f).setLightOpacity(16));
		blocks.add(oreCopper = (new BlockBase(Material.ROCK,"oreCopper",true)).setIsFullCube(true).setIsOpaqueCube(true).setHarvestProperties("pickaxe", 1).setHardness(1.8f).setLightOpacity(16));
		blocks.add(oreLead = (new BlockBase(Material.ROCK,"oreLead",true)).setIsFullCube(true).setIsOpaqueCube(true).setHarvestProperties("pickaxe", 2).setHardness(2.5f).setLightOpacity(16));
		blocks.add(oreSilver = (new BlockBase(Material.ROCK,"oreSilver",true)).setIsFullCube(true).setIsOpaqueCube(true).setHarvestProperties("pickaxe", 2).setHardness(2.5f).setLightOpacity(16));
		blocks.add(blockCaminiteBrick = (new BlockBase(Material.ROCK,"blockCaminiteBrick",true)).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));	
		blocks.add(blockCaminiteBrickSlabDouble = new BlockDoubleSlabBase(Material.WOOD,"blockCaminiteBrickSlabDouble",false).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));
		blocks.add(blockCaminiteBrickSlab = new BlockSlabBase(blockCaminiteBrickSlabDouble,"blockCaminiteBrickSlab",true).setHarvestProperties("pickaxe", 0).setIsFullCube(false).setIsOpaqueCube(false).setHardness(1.6f).setLightOpacity(1));
		((BlockDoubleSlabBase)blockCaminiteBrickSlabDouble).setSlab(blockCaminiteBrickSlab);
		items.add(new ItemBlockSlab(blockCaminiteBrickSlab, blockCaminiteBrickSlabDouble));
		blocks.add(stairsCaminiteBrick = (new BlockStairsBase(RegistryManager.blockCaminiteBrick.getDefaultState(),"stairsCaminiteBrick",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));
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
		blocks.add(charger = (new BlockCharger(Material.ROCK,"charger",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(ashenStone = (new BlockBase(Material.ROCK,"ashenStone",true)).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));	
		blocks.add(ashenStoneSlabDouble = new BlockDoubleSlabBase(Material.WOOD,"ashenStoneSlabDouble",false).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));
		blocks.add(ashenStoneSlab = new BlockSlabBase(ashenStoneSlabDouble,"ashenStoneSlab",true).setHarvestProperties("pickaxe", 0).setIsFullCube(false).setIsOpaqueCube(false).setHardness(1.6f).setLightOpacity(1));
		((BlockDoubleSlabBase)ashenStoneSlabDouble).setSlab(ashenStoneSlab);
		items.add(new ItemBlockSlab(ashenStoneSlab, ashenStoneSlabDouble));
		blocks.add(stairsAshenStone = (new BlockStairsBase(RegistryManager.ashenStone.getDefaultState(),"stairsAshenStone",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));
		blocks.add(wallAshenStone = (new BlockWallBase(RegistryManager.ashenStone,"wallAshenStone",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(ashenBrick = (new BlockBase(Material.ROCK,"ashenBrick",true)).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));	
		blocks.add(ashenBrickSlabDouble = new BlockDoubleSlabBase(Material.WOOD,"ashenBrickSlabDouble",false).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));
		blocks.add(ashenBrickSlab = new BlockSlabBase(ashenBrickSlabDouble,"ashenBrickSlab",true).setHarvestProperties("pickaxe", 0).setIsFullCube(false).setIsOpaqueCube(false).setHardness(1.6f).setLightOpacity(1));
		((BlockDoubleSlabBase)ashenBrickSlabDouble).setSlab(ashenBrickSlab);
		items.add(new ItemBlockSlab(ashenBrickSlab, ashenBrickSlabDouble));
		blocks.add(stairsAshenBrick = (new BlockStairsBase(RegistryManager.ashenBrick.getDefaultState(),"stairsAshenBrick",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));
		blocks.add(wallAshenBrick = (new BlockWallBase(RegistryManager.ashenBrick,"wallAshenBrick",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(ashenTile = (new BlockBase(Material.ROCK,"ashenTile",true)).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));	
		blocks.add(ashenTileSlabDouble = new BlockDoubleSlabBase(Material.WOOD,"ashenTileSlabDouble",false).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));
		blocks.add(ashenTileSlab = new BlockSlabBase(ashenTileSlabDouble,"ashenTileSlab",true).setHarvestProperties("pickaxe", 0).setIsFullCube(false).setIsOpaqueCube(false).setHardness(1.6f).setLightOpacity(1));
		((BlockDoubleSlabBase)ashenTileSlabDouble).setSlab(ashenTileSlab);
		items.add(new ItemBlockSlab(ashenTileSlab, ashenTileSlabDouble));
		blocks.add(stairsAshenTile = (new BlockStairsBase(RegistryManager.ashenTile.getDefaultState(),"stairsAshenTile",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));
		blocks.add(wallAshenTile = (new BlockWallBase(RegistryManager.ashenTile,"wallAshenTile",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(cinderPlinth = (new BlockCinderPlinth(Material.ROCK, "cinderPlinth",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(knowledgeTable = (new BlockKnowledgeTable(Material.WOOD, "knowledgeTable",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("axe", 0).setHardness(1.6f));
		blocks.add(alchemyPedestal = (new BlockAlchemyPedestal(Material.ROCK, "alchemyPedestal",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(alchemyTablet = (new BlockAlchemyTablet(Material.ROCK, "alchemyTablet",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		
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
		items.add(emberJar = new ItemEmberJar());
		items.add(emberCartridge = new ItemEmberCartridge());
		items.add(ignitionCannon = new ItemIgnitionCannon());
		items.add(staffEmber = new ItemCinderStaff());
		items.add(axeClockwork = new ItemClockworkAxe("axeClockwork",true));
		items.add(pickaxeClockwork = new ItemClockworkPickaxe("pickaxeClockwork",true));
		items.add(grandhammer = new ItemGrandhammer("grandhammer",true));
		items.add(dustAsh = new ItemBase("dustAsh",true));
		items.add(golemsEye = new ItemGolemsEye());
		
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
		GameRegistry.registerTileEntity(TileEntityBeamSplitter.class, Embers.MODID+":tileEntityBeamSplitter");
		GameRegistry.registerTileEntity(TileEntityRelay.class, Embers.MODID+":tileEntityRelay");
		GameRegistry.registerTileEntity(TileEntityCrystalCell.class, Embers.MODID+":tileEntityCrystalCell");
		GameRegistry.registerTileEntity(TileEntityCharger.class, Embers.MODID+":tileEntityCharger");
		GameRegistry.registerTileEntity(TileEntityCinderPlinth.class, Embers.MODID+":tileEntityCinderPlinth");
		GameRegistry.registerTileEntity(TileEntityKnowledgeTable.class, Embers.MODID+":tileEntityKnowledgeTable");
		GameRegistry.registerTileEntity(TileEntityAlchemyPedestal.class, Embers.MODID+":tileEntityAlchemyPedestal");
		GameRegistry.registerTileEntity(TileEntityAlchemyTablet.class, Embers.MODID+":tileEntityAlchemyTablet");
		
		int id = 0;
		
		EntityRegistry.registerModEntity(EntityEmberPacket.class, "emberPacket", id++, Embers.instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntityEmberProjectile.class, "emberProjectile", id++, Embers.instance, 64, 1, true);
		
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
	public static void registerColorHandlers(){
		/*Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemEmberJar.EmberChargeColorHandler(), emberJar);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemEmberCartridge.EmberChargeColorHandler(), emberCartridge);*/
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
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCharger.class, new TileEntityChargerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCinderPlinth.class, new TileEntityCinderPlinthRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityKnowledgeTable.class, new TileEntityKnowledgeTableRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAlchemyPedestal.class, new TileEntityAlchemyPedestalRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAlchemyTablet.class, new TileEntityAlchemyTabletRenderer());
		
		RenderingRegistry.registerEntityRenderingHandler(EntityEmberPacket.class, new RenderEmberPacket(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityEmberProjectile.class, new RenderEmberPacket(Minecraft.getMinecraft().getRenderManager()));
		
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
