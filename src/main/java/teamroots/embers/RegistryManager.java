package teamroots.embers;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.block.BlockBase;
import teamroots.embers.block.BlockBin;
import teamroots.embers.block.BlockCopperCell;
import teamroots.embers.block.BlockDeepLine;
import teamroots.embers.block.BlockEmberReceiver;
import teamroots.embers.block.BlockEmberEmitter;
import teamroots.embers.block.BlockFurnace;
import teamroots.embers.block.BlockItemPipe;
import teamroots.embers.block.BlockItemPump;
import teamroots.embers.block.BlockOven;
import teamroots.embers.block.BlockPipe;
import teamroots.embers.block.BlockPump;
import teamroots.embers.block.BlockStampBase;
import teamroots.embers.block.BlockStamper;
import teamroots.embers.block.BlockTEBase;
import teamroots.embers.block.BlockTank;
import teamroots.embers.block.IBlockModel;
import teamroots.embers.block.fluid.BlockMoltenCopper;
import teamroots.embers.block.fluid.BlockMoltenGold;
import teamroots.embers.block.fluid.BlockMoltenIron;
import teamroots.embers.block.fluid.BlockMoltenLead;
import teamroots.embers.block.fluid.BlockMoltenSilver;
import teamroots.embers.entity.EntityEmberPacket;
import teamroots.embers.entity.RenderEmberPacket;
import teamroots.embers.fluid.FluidMoltenCopper;
import teamroots.embers.fluid.FluidMoltenGold;
import teamroots.embers.fluid.FluidMoltenIron;
import teamroots.embers.fluid.FluidMoltenLead;
import teamroots.embers.fluid.FluidMoltenSilver;
import teamroots.embers.item.ItemBase;
import teamroots.embers.item.ItemEmberGauge;
import teamroots.embers.item.ItemTinkerHammer;
import teamroots.embers.particle.ParticleRenderer;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityStorage;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.tileentity.TileEntityBin;
import teamroots.embers.tileentity.TileEntityBinRenderer;
import teamroots.embers.tileentity.TileEntityCopperCell;
import teamroots.embers.tileentity.TileEntityDeepLine;
import teamroots.embers.tileentity.TileEntityEmitter;
import teamroots.embers.tileentity.TileEntityEmitterRenderer;
import teamroots.embers.tileentity.TileEntityFurnaceBottom;
import teamroots.embers.tileentity.TileEntityFurnaceTop;
import teamroots.embers.tileentity.TileEntityFurnaceTopRenderer;
import teamroots.embers.tileentity.TileEntityItemPipe;
import teamroots.embers.tileentity.TileEntityItemPipeRenderer;
import teamroots.embers.tileentity.TileEntityItemPump;
import teamroots.embers.tileentity.TileEntityItemPumpRenderer;
import teamroots.embers.tileentity.TileEntityOven;
import teamroots.embers.tileentity.TileEntityOvenRenderer;
import teamroots.embers.tileentity.TileEntityPipe;
import teamroots.embers.tileentity.TileEntityPipeRenderer;
import teamroots.embers.tileentity.TileEntityPump;
import teamroots.embers.tileentity.TileEntityPumpRenderer;
import teamroots.embers.tileentity.TileEntityReceiver;
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
	
	public static Block stampBase, stamper, blockCaminiteLargeBrick, bin, copperCell, deepLine, emberEmitter, emberReceiver, blockFurnace, pump, blockCopper, blockLead, blockSilver, oreCopper, oreLead, oreSilver, blockCaminiteBrick, blockTank, pipe;
	public static Block itemPump, itemPipe, blockOven, blockMoltenGold, blockMoltenCopper, blockMoltenLead, blockMoltenSilver, blockMoltenIron;
	
	public static Fluid fluidMoltenGold, fluidMoltenCopper, fluidMoltenLead, fluidMoltenSilver, fluidMoltenIron;
	
	public static Item tinkerHammer, emberGauge, ingotCopper, ingotSilver, ingotLead, nuggetCopper, nuggetSilver, nuggetLead, brickCaminite, blendCaminite, plateCaminite;
	
	public static WorldGenOres worldGenOres;
	
	public static void registerAll(){
		CapabilityManager.INSTANCE.register(IEmberCapability.class, new EmberCapabilityStorage(), DefaultEmberCapability.class);
		
		blocks.add(blockCopper = (new BlockBase(Material.ROCK,"blockCopper",true)).setHarvestProperties("pickaxe", 1).setHardness(1.4f));
		blocks.add(blockLead = (new BlockBase(Material.ROCK,"blockLead",true)).setHarvestProperties("pickaxe", 2).setHardness(1.6f));
		blocks.add(blockSilver = (new BlockBase(Material.ROCK,"blockSilver",true)).setHarvestProperties("pickaxe", 2).setHardness(1.6f));
		blocks.add(oreCopper = (new BlockBase(Material.ROCK,"oreCopper",true)).setHarvestProperties("pickaxe", 1).setHardness(1.0f));
		blocks.add(oreLead = (new BlockBase(Material.ROCK,"oreLead",true)).setHarvestProperties("pickaxe", 2).setHardness(1.2f));
		blocks.add(oreSilver = (new BlockBase(Material.ROCK,"oreSilver",true)).setHarvestProperties("pickaxe", 2).setHardness(1.2f));
		blocks.add(blockCaminiteBrick = (new BlockBase(Material.ROCK,"blockCaminiteBrick",true)).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(blockCaminiteLargeBrick = (new BlockBase(Material.ROCK,"blockCaminiteLargeBrick",true)).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(blockTank = (new BlockTank(Material.ROCK,"blockTank",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(pipe = (new BlockPipe(Material.ROCK,"pipe",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(pump = (new BlockPump(Material.ROCK,"pump",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(blockFurnace = (new BlockFurnace(Material.ROCK,"blockFurnace",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(emberReceiver = (new BlockEmberReceiver(Material.ROCK,"emberReceiver",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(0.6f));
		blocks.add(emberEmitter = (new BlockEmberEmitter(Material.ROCK,"emberEmitter",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(0.6f));
		blocks.add(deepLine = (new BlockDeepLine(Material.ROCK,"deepLine",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.4f));
		blocks.add(copperCell = (new BlockCopperCell(Material.ROCK,"copperCell",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.4f));
		blocks.add(blockOven = (new BlockOven(Material.ROCK,"blockOven",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.4f));
		blocks.add(itemPipe = (new BlockItemPipe(Material.ROCK,"itemPipe",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(itemPump = (new BlockItemPump(Material.ROCK,"itemPump",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(bin = (new BlockBin(Material.ROCK,"bin",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(stamper = (new BlockStamper(Material.ROCK,"stamper",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(stampBase = (new BlockStampBase(Material.ROCK,"stamperBase",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		
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
		
		items.add(ingotCopper = new ItemBase("ingotCopper",true));
		items.add(ingotLead = new ItemBase("ingotLead",true));
		items.add(ingotSilver = new ItemBase("ingotSilver",true));
		items.add(nuggetCopper = new ItemBase("nuggetCopper",true));
		items.add(nuggetLead = new ItemBase("nuggetLead",true));
		items.add(nuggetSilver = new ItemBase("nuggetSilver",true));
		items.add(brickCaminite = new ItemBase("brickCaminite",true));
		items.add(blendCaminite = new ItemBase("blendCaminite",true));
		items.add(plateCaminite = new ItemBase("plateCaminite",true));
		items.add(emberGauge = new ItemEmberGauge());
		items.add(tinkerHammer = new ItemTinkerHammer());
		
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
		
		int id = 0;
		
		EntityRegistry.registerModEntity(EntityEmberPacket.class, "emberPacket", id++, Embers.instance, 64, 3, true);
		
		worldGenOres = new WorldGenOres();
		GameRegistry.registerWorldGenerator(worldGenOres, 1);
		
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
		
		RenderingRegistry.registerEntityRenderingHandler(EntityEmberPacket.class, new RenderEmberPacket(Minecraft.getMinecraft().getRenderManager()));
		
		for (int i = 0; i < blocks.size(); i ++){
			if (blocks.get(i) instanceof IBlockModel){
				((IBlockModel)blocks.get(i)).initModel();
			}
		}
		for (int i = 0; i < items.size(); i ++){
			if (items.get(i) instanceof ItemBase){
				((ItemBase)items.get(i)).initModel();
			}
		}
	}
}
