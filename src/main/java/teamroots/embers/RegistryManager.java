package teamroots.embers;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.block.*;
import teamroots.embers.compat.BaublesIntegration;
import teamroots.embers.compat.MysticalMechanicsIntegration;
import teamroots.embers.damage.DamageEmber;
import teamroots.embers.entity.*;
import teamroots.embers.fluid.*;
import teamroots.embers.item.*;
import teamroots.embers.item.block.ItemBlockSlab;
import teamroots.embers.itemmod.*;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityStorage;
import teamroots.embers.research.capability.DefaultResearchCapability;
import teamroots.embers.research.capability.IResearchCapability;
import teamroots.embers.tileentity.*;
import teamroots.embers.upgrade.UpgradeCatalyticPlug;
import teamroots.embers.util.*;
import teamroots.embers.world.WorldGenOres;
import teamroots.embers.world.WorldGenSmallRuin;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public class RegistryManager {
	public static ArrayList<Block> blocks = new ArrayList<Block>();
	public static ArrayList<Item> items = new ArrayList<Item>();
	
	public static ToolMaterial tool_mat_tyrfing, tool_mat_copper, tool_mat_silver, tool_mat_lead, tool_mat_dawnstone;
	public static ToolMaterial tool_mat_aluminum, tool_mat_bronze, tool_mat_tin, tool_mat_electrum, tool_mat_nickel;
	public static ArmorMaterial armor_mat_ashen_cloak;
	
	public static Block mechanical_pump, creative_ember_source, caminite_lever, inferno_forge, inferno_forge_edge, ember_pulser, field_chart, catalyzer, combustor, reactor, archaic_tile, archaic_edge, wrapped_sealed_planks, structure_marker, boiler, ember_injector, breaker, vacuum, sealed_planks, ore_quartz, auto_hammer, dawnstone_anvil, archaic_light, archaic_bricks, glow, beam_cannon, item_transfer, alchemy_tablet, alchemy_pedestal, knowledge_table, cinder_plinth, ashen_tile, stairs_ashen_tile, wall_ashen_tile, ashen_tile_slab, ashen_tile_slab_double, ashen_stone, ashen_brick, stairs_ashen_stone, wall_ashen_stone, ashen_stone_slab, ashen_stone_slab_double, stairs_ashen_brick, wall_ashen_brick, ashen_brick_slab, ashen_brick_slab_double, block_caminite_brick_slab, block_caminite_brick_slab_double, charger, crystal_cell, advanced_edge, ember_relay, beam_splitter, block_lantern, ember_gauge, item_gauge, fluid_gauge, large_tank, item_dropper, heat_coil, wall_caminite_brick, block_dawnstone, mixer, stone_edge, ember_activator, mech_core, stairs_caminite_brick, mech_accessor, ember_bore, mech_edge, item_pump, item_pipe, block_oven, stamp_base, stamper, block_caminite_large_brick, bin, copper_cell, deep_line, ember_emitter, ember_receiver, block_furnace, pump, block_copper, block_lead, block_silver, block_mithril, ore_copper, ore_lead, ore_silver, block_caminite_brick, block_tank, pipe;
	@Deprecated
	public static Block seed;
	public static Block block_molten_dawnstone, block_molten_gold, block_molten_copper, block_molten_lead, block_molten_silver, block_molten_iron,
						block_molten_aluminum, block_molten_tin, block_molten_bronze, block_molten_electrum, block_molten_nickel;
	public static Block ore_nickel, block_nickel;
	public static Block ore_aluminum, block_aluminum;
	public static Block ore_tin, block_tin;
	public static Block block_bronze;
	public static Block block_electrum;
	public static Block catalytic_plug;
	public static Block ember_funnel;
	public static Block block_alchemical_redstone;
	public static Block mini_boiler;
	public static Block fluid_transfer;
	public static Block ember_siphon;
	public static Block stirling;
	public static Block clockwork_attenuator;
	public static Block seed_iron, seed_gold, seed_copper, seed_silver, seed_lead, seed_tin, seed_aluminum, seed_nickel, seed_dawnstone;
	public static Block archaic_mech_edge;
	public static Block archaic_geysir;
	public static Block stone_valve;
	public static Block geo_separator;
	
	public static Fluid fluid_steam, fluid_molten_dawnstone, fluid_molten_gold, fluid_molten_copper, fluid_molten_lead, fluid_molten_silver, fluid_molten_iron,
						fluid_molten_aluminum, fluid_molten_tin, fluid_molten_bronze, fluid_molten_electrum, fluid_molten_nickel, fluid_alchemical_redstone;
	
	public static Item archaic_circuit, flame_barrier, eldritch_insignia, intelligent_apparatus, caster_orb, resonating_bell, superheater, jet_augment, blasting_core, codex, wildfire_core, ember_cluster, adhesive, tyrfing, isolated_materia, archaic_brick, ancient_motive_core, ashen_cloth, glimmer_shard, glimmer_lamp, inflictor_gem, ashen_cloak_head, ashen_cloak_chest, ashen_cloak_legs, ashen_cloak_boots, aster, shard_aster, alchemic_waste, aspectus_iron, aspectus_copper, aspectus_dawnstone, aspectus_lead, aspectus_silver, golems_eye, dust_ash, grandhammer, pickaxe_clockwork, axe_clockwork, staff_ember, ignition_cannon, ember_jar, ember_cartridge, pickaxe_copper, axe_copper, shovel_copper, hoe_copper, sword_copper, pickaxe_silver, axe_silver, shovel_silver, hoe_silver, sword_silver, pickaxe_lead, axe_lead, shovel_lead, hoe_lead, sword_lead, pickaxe_dawnstone, axe_dawnstone, shovel_dawnstone, hoe_dawnstone, sword_dawnstone, debug, plate_gold, plate_iron, plate_caminite_raw, plate_mithril, stamp_bar_raw, stamp_plate_raw, stamp_flat_raw, nugget_dawnstone, plate_copper, plate_lead, plate_silver, plate_dawnstone, nugget_mithril, ingot_astralite, ingot_dawnstone, ingot_umber_steel, ingot_mithril, crystal_ember, shard_ember, stamp_bar, stamp_plate, stamp_flat, tinker_hammer, ember_detector, ingot_copper, ingot_silver, ingot_lead, nugget_copper, nugget_silver, nugget_lead, brick_caminite, blend_caminite, plate_caminite;
	public static Item ingot_nickel, nugget_nickel, plate_nickel, pickaxe_nickel, axe_nickel, shovel_nickel, hoe_nickel, sword_nickel;
	public static Item ingot_aluminum, nugget_aluminum, plate_aluminum, pickaxe_aluminum, axe_aluminum, shovel_aluminum, hoe_aluminum, sword_aluminum;
	public static Item ingot_tin, nugget_tin, plate_tin, pickaxe_tin, axe_tin, shovel_tin, hoe_tin, sword_tin;
	public static Item ingot_bronze, nugget_bronze, plate_bronze, pickaxe_bronze, axe_bronze, shovel_bronze, hoe_bronze, sword_bronze;
	public static Item ingot_electrum, nugget_electrum, plate_electrum, pickaxe_electrum, axe_electrum, shovel_electrum, hoe_electrum, sword_electrum;
	public static Item dust_ember;
	public static Item dust_metallurgic;
	public static Item diffraction_barrel;
	public static Item focal_lens;
	public static Item stamp_gear, stamp_gear_raw;
	public static Item tinker_lens, anti_tinker_lens;
	public static Item winding_gears;
	public static Item shifting_scales;
	//public static Item core_stone;
	public static Item creative_heat;

	public static DamageSource damage_ember;
	
	public static Material unpushable;
	
	public static Biome biome_cave;
	
	public static DimensionType dimension_cave;
	
	public static WorldGenOres world_gen_ores;
	
	public static IWorldGenerator world_gen_small_ruin;

	private static BlockSeedNew createSimpleSeed(Material material, String name, ResourceLocation texture, BiFunction<TileEntitySeedNew,Integer,ItemStack> nuggetGenerator) {
		return new BlockSeedNew(material,name,true) {
			@Override
			public ResourceLocation getTexture(TileEntitySeedNew tile) {
				return texture;
			}

			@Override
			public ItemStack[] getNuggetDrops(TileEntitySeedNew tile, int n) {
				return IntStream.range(0,n).mapToObj(i -> nuggetGenerator.apply(tile,n)).toArray(ItemStack[]::new);
			}
		};
	}
	
	public static void registerAll(){
		registerCapabilities();

		damage_ember = new DamageEmber();
		
		tool_mat_copper = EnumHelper.addToolMaterial(Embers.MODID+":copper", 2, 181, 5.4f, 1.5f, 16);
		tool_mat_silver = EnumHelper.addToolMaterial(Embers.MODID+":silver", 2, 202, 7.6f, 2.0f, 20);
		tool_mat_lead = EnumHelper.addToolMaterial(Embers.MODID+":lead", 2, 168, 6.0f, 2.0f, 4);
		tool_mat_dawnstone = EnumHelper.addToolMaterial(Embers.MODID+":dawnstone", 2, 644, 7.5f, 2.5f, 18);
		tool_mat_tyrfing = EnumHelper.addToolMaterial(Embers.MODID+":tyrfing", 2, 512, 7.5f, 0.0f, 24);
		
		armor_mat_ashen_cloak = EnumHelper.addArmorMaterial(Embers.MODID+":ashen_cloak", Embers.MODID+":ashen_cloak", 19, new int[]{3,5,7,3}, 18, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);
		
		unpushable = new MaterialUnpushable();
		
		blocks.add(block_copper = (new BlockBase(Material.ROCK,"block_copper",true)).setBeaconBase(true).setHarvestProperties("pickaxe", 1).setHardness(1.4f).setLightOpacity(16).setCreativeTab(Embers.resource_tab));
		blocks.add(block_lead = (new BlockBase(Material.ROCK,"block_lead",true)).setBeaconBase(true).setHarvestProperties("pickaxe", 2).setHardness(1.6f).setLightOpacity(16).setCreativeTab(Embers.resource_tab));
		blocks.add(block_silver = (new BlockBase(Material.ROCK,"block_silver",true)).setBeaconBase(true).setHarvestProperties("pickaxe", 2).setHardness(1.6f).setLightOpacity(16).setCreativeTab(Embers.resource_tab));
		blocks.add(block_dawnstone = (new BlockBase(Material.ROCK,"block_dawnstone",true)).setBeaconBase(true).setHarvestProperties("pickaxe", 2).setHardness(1.6f).setLightLevel(0.0625f).setLightOpacity(16).setCreativeTab(Embers.resource_tab));
		// Gloomshroud - Add Mithril Block. May need property adjustment.
		//blocks.add(block_mithril = (new BlockBase(Material.ROCK, "block_mithril", true)).setHarvestProperties("pickaxe", 2).setHardness(1.6f).setLightLevel(0.0625f).setLightOpacity(16).setCreativeTab(Embers.resource_tab));
		blocks.add(ore_copper = (new BlockBase(Material.ROCK,"ore_copper",true)).setIsFullCube(true).setIsOpaqueCube(true).setHarvestProperties("pickaxe", 1).setHardness(1.8f).setLightOpacity(16).setCreativeTab(Embers.resource_tab));
		blocks.add(ore_lead = (new BlockBase(Material.ROCK,"ore_lead",true)).setIsFullCube(true).setIsOpaqueCube(true).setHarvestProperties("pickaxe", 2).setHardness(2.5f).setLightOpacity(16).setCreativeTab(Embers.resource_tab));
		blocks.add(ore_silver = (new BlockBase(Material.ROCK,"ore_silver",true)).setIsFullCube(true).setIsOpaqueCube(true).setHarvestProperties("pickaxe", 2).setHardness(2.5f).setLightOpacity(16).setCreativeTab(Embers.resource_tab));
		blocks.add(block_caminite_brick = (new BlockBase(Material.ROCK,"block_caminite_brick",true)).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));	
		blocks.add(block_caminite_brick_slab_double = new BlockDoubleSlabBase(Material.WOOD,"block_caminite_brick_slab_double",false).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));
		blocks.add(block_caminite_brick_slab = new BlockSlabBase(block_caminite_brick_slab_double,"block_caminite_brick_slab",true).setHarvestProperties("pickaxe", 0).setIsFullCube(false).setIsOpaqueCube(false).setHardness(1.6f).setLightOpacity(1));
		((BlockDoubleSlabBase)block_caminite_brick_slab_double).setSlab(block_caminite_brick_slab);
		items.add(new ItemBlockSlab(block_caminite_brick_slab, block_caminite_brick_slab_double));
		blocks.add(stairs_caminite_brick = (new BlockStairsBase(RegistryManager.block_caminite_brick.getDefaultState(),"stairs_caminite_brick",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));
		blocks.add(wall_caminite_brick = (new BlockWallBase(RegistryManager.block_caminite_brick,"wall_caminite_brick",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		//blocks.add(blockCaminiteLargeBrick = (new BlockBase(Material.ROCK,"blockCaminiteLargeBrick",true)).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(block_tank = (new BlockTank(Material.ROCK,"block_tank",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(pipe = (new BlockFluidPipe(Material.ROCK,"pipe",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(pump = (new BlockFluidExtractor(Material.ROCK,"pump",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(block_furnace = (new BlockFurnace(Material.ROCK,"block_furnace",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(ember_receiver = (new BlockEmberReceiver(Material.ROCK,"ember_receiver",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(0.6f));
		blocks.add(ember_emitter = (new BlockEmberEmitter(Material.ROCK,"ember_emitter",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(0.6f));
		blocks.add(copper_cell = (new BlockCopperCell(Material.ROCK,"copper_cell",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.4f));
		blocks.add(item_pipe = (new BlockItemPipe(Material.ROCK,"item_pipe",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(item_pump = (new BlockItemExtractor(Material.ROCK,"item_pump",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(bin = (new BlockBin(Material.ROCK,"bin",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(stamper = (new BlockStamper(Material.ROCK,"stamper",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(stamp_base = (new BlockStampBase(Material.ROCK,"stamper_base",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(mech_edge = (new BlockMechEdge(unpushable,"mech_edge",false)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(ember_bore = (new BlockEmberBore(Material.ROCK,"ember_bore",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(mech_accessor = (new BlockMechAccessor(Material.ROCK,"mech_accessor",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(mech_core = (new BlockMechCore(Material.ROCK,"mech_core",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(ember_activator = (new BlockActivator(Material.ROCK,"ember_activator",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(stone_edge = (new BlockStoneEdge(unpushable,"stone_edge",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(mixer = (new BlockMixer(Material.ROCK,"mixer",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(heat_coil = (new BlockHeatCoil(Material.ROCK,"heat_coil",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(item_dropper = (new BlockDropper(Material.ROCK,"item_dropper",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(large_tank = (new BlockLargeTank(Material.ROCK,"large_tank",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(ember_gauge = (new BlockEmberGauge(Material.ROCK,"ember_gauge",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		//blocks.add(item_gauge = (new BlockItemGauge(Material.ROCK,"item_gauge",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(fluid_gauge = (new BlockFluidGauge(Material.ROCK,"fluid_gauge",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(block_lantern = (new BlockLantern(Material.ROCK,"block_lantern",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f).setLightLevel(1.0f));
		blocks.add(beam_splitter = (new BlockBeamSplitter(Material.ROCK,"beam_splitter",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(ember_relay = (new BlockRelay(Material.ROCK,"ember_relay",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(advanced_edge = (new BlockAdvancedEdge(unpushable,"advanced_edge",false)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(crystal_cell = (new BlockCrystalCell(Material.ROCK,"crystal_cell",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(charger = (new BlockCharger(Material.ROCK,"charger",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(ashen_stone = (new BlockBase(Material.ROCK,"ashen_stone",true)).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));	
		blocks.add(ashen_stone_slab_double = new BlockDoubleSlabBase(Material.WOOD,"ashen_stone_slab_double",false).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));
		blocks.add(ashen_stone_slab = new BlockSlabBase(ashen_stone_slab_double,"ashen_stone_slab",true).setHarvestProperties("pickaxe", 0).setIsFullCube(false).setIsOpaqueCube(false).setHardness(1.6f).setLightOpacity(1));
		((BlockDoubleSlabBase)ashen_stone_slab_double).setSlab(ashen_stone_slab);
		items.add(new ItemBlockSlab(ashen_stone_slab, ashen_stone_slab_double));
		blocks.add(stairs_ashen_stone = (new BlockStairsBase(RegistryManager.ashen_stone.getDefaultState(),"stairs_ashen_stone",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));
		blocks.add(wall_ashen_stone = (new BlockWallBase(RegistryManager.ashen_stone,"wall_ashen_stone",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(ashen_brick = (new BlockBase(Material.ROCK,"ashen_brick",true)).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));	
		blocks.add(ashen_brick_slab_double = new BlockDoubleSlabBase(Material.WOOD,"ashen_brick_slab_double",false).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));
		blocks.add(ashen_brick_slab = new BlockSlabBase(ashen_brick_slab_double,"ashen_brick_slab",true).setHarvestProperties("pickaxe", 0).setIsFullCube(false).setIsOpaqueCube(false).setHardness(1.6f).setLightOpacity(1));
		((BlockDoubleSlabBase)ashen_brick_slab_double).setSlab(ashen_brick_slab);
		items.add(new ItemBlockSlab(ashen_brick_slab, ashen_brick_slab_double));
		blocks.add(stairs_ashen_brick = (new BlockStairsBase(RegistryManager.ashen_brick.getDefaultState(),"stairs_ashen_brick",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));
		blocks.add(wall_ashen_brick = (new BlockWallBase(RegistryManager.ashen_brick,"wall_ashen_brick",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(ashen_tile = (new BlockBase(Material.ROCK,"ashen_tile",true)).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));	
		blocks.add(ashen_tile_slab_double = new BlockDoubleSlabBase(Material.WOOD,"ashen_tile_slab_double",false).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));
		blocks.add(ashen_tile_slab = new BlockSlabBase(ashen_tile_slab_double,"ashen_tile_slab",true).setHarvestProperties("pickaxe", 0).setIsFullCube(false).setIsOpaqueCube(false).setHardness(1.6f).setLightOpacity(1));
		((BlockDoubleSlabBase)ashen_tile_slab_double).setSlab(ashen_tile_slab);
		items.add(new ItemBlockSlab(ashen_tile_slab, ashen_tile_slab_double));
		blocks.add(stairs_ashen_tile = (new BlockStairsBase(RegistryManager.ashen_tile.getDefaultState(),"stairs_ashen_tile",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));
		blocks.add(wall_ashen_tile = (new BlockWallBase(RegistryManager.ashen_tile,"wall_ashen_tile",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(cinder_plinth = (new BlockCinderPlinth(Material.ROCK, "cinder_plinth",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(alchemy_pedestal = (new BlockAlchemyPedestal(Material.ROCK, "alchemy_pedestal",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(alchemy_tablet = (new BlockAlchemyTablet(Material.ROCK, "alchemy_tablet",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(item_transfer = (new BlockItemTransfer(Material.ROCK, "item_transfer",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(beam_cannon = (new BlockBeamCannon(Material.ROCK,"beam_cannon",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(glow = (new BlockGlow(Material.CIRCUITS,"glow",false)).setIsFullCube(false).setIsOpaqueCube(false).setHardness(0.0f).setLightLevel(1.0f));
		blocks.add(archaic_bricks = (new BlockBase(Material.ROCK,"archaic_bricks",true)).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));	
		blocks.add(archaic_edge = (new BlockBase(Material.ROCK,"archaic_edge",true)).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));	
		blocks.add(archaic_tile = (new BlockBase(Material.ROCK,"archaic_tile",true)).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(16));	
		blocks.add(archaic_light = (new BlockArchaicLight(Material.ROCK,"archaic_light",true)).setHarvestProperties("pickaxe", 0).setHardness(1.6f).setLightOpacity(0).setLightLevel(1.0f));	
		blocks.add(dawnstone_anvil = (new BlockDawnstoneAnvil(Material.ANVIL,"dawnstone_anvil",true)).setHarvestProperties("pickaxe", 1).setIsFullCube(false).setIsOpaqueCube(false).setHardness(1.6f).setLightOpacity(0));	
		blocks.add(auto_hammer = (new BlockAutoHammer(Material.ROCK,"auto_hammer",true)).setHarvestProperties("pickaxe", 0).setIsFullCube(false).setIsOpaqueCube(false).setHardness(1.6f).setLightOpacity(0));	
		blocks.add(ore_quartz = (new BlockQuartzOre(Material.ROCK,"ore_quartz",true)).setIsFullCube(true).setIsOpaqueCube(true).setHarvestProperties("pickaxe", 2).setHardness(1.9f).setLightOpacity(16));
		blocks.add(sealed_planks = (new BlockBase(Material.WOOD,"sealed_planks",true)).setIsFullCube(true).setIsOpaqueCube(true).setHarvestProperties("axe", -1).setHardness(2.5f).setLightOpacity(16));
		blocks.add(wrapped_sealed_planks = (new BlockBase(Material.WOOD,"wrapped_sealed_planks",true)).setIsFullCube(true).setIsOpaqueCube(true).setHarvestProperties("axe", -1).setHardness(3.1f).setLightOpacity(16));
		blocks.add(vacuum = (new BlockVacuum(Material.ROCK,"vacuum",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(breaker = (new BlockBreaker(Material.ROCK,"breaker",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(seed = (new BlockSeed(Material.ROCK,"seed",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(ember_injector = (new BlockEmberInjector(Material.ROCK,"ember_injector",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(boiler = (new BlockBoiler(Material.ROCK,"boiler",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(structure_marker = (new BlockStructureMarker()).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(reactor = (new BlockReactor(Material.ROCK,"reactor",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(combustor = (new BlockCombustor(Material.ROCK,"combustor",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(catalyzer = (new BlockCatalyzer(Material.ROCK,"catalyzer",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(field_chart = (new BlockFieldChart(Material.ROCK,"field_chart",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(ember_pulser = (new BlockEmberPulser(Material.ROCK,"ember_pulser",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(inferno_forge_edge = (new BlockInfernoForgeEdge(unpushable,"inferno_forge_edge",false)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(inferno_forge = (new BlockInfernoForge(Material.ROCK,"inferno_forge",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(caminite_lever = (new BlockCaminiteLever("caminite_lever",true)).setHardness(0.75f));
		//blocks.add(axle_iron = (new BlockAxle(Material.ROCK,"axle_iron",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		//blocks.add(creative_mech_source = (new BlockCreativeMechSource(Material.ROCK,"creative_mech_source",true)).setIsFullCube(true).setIsOpaqueCube(true).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(creative_ember_source = (new BlockCreativeEmberSource(Material.ROCK,"creative_ember_source",true)).setIsFullCube(true).setIsOpaqueCube(true).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		//blocks.add(gearbox_frame = (new BlockGearbox(Material.ROCK,"gearbox_frame",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(mechanical_pump = (new BlockPump(Material.ROCK,"mechanical_pump",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(catalytic_plug = (new BlockCatalyticPlug(Material.ROCK,"catalytic_plug",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(ember_funnel = (new BlockEmberFunnel(Material.IRON,"ember_funnel",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(mini_boiler = (new BlockMiniBoiler(Material.IRON,"mini_boiler",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(fluid_transfer = (new BlockFluidTransfer(Material.ROCK, "fluid_transfer",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(ember_siphon = (new BlockEmberSiphon(Material.ROCK, "ember_siphon",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(stirling = (new BlockStirling(Material.ROCK, "stirling",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(clockwork_attenuator = (new BlockClockworkAttenuator(Material.ROCK, "clockwork_attenuator",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));
		blocks.add(seed_iron = createSimpleSeed(Material.ROCK, "seed_iron", new ResourceLocation(Embers.MODID + ":textures/blocks/material_iron.png"),(tile,i) -> new ItemStack(Items.IRON_NUGGET)));
		blocks.add(seed_gold = createSimpleSeed(Material.ROCK, "seed_gold", new ResourceLocation(Embers.MODID + ":textures/blocks/material_gold.png"),(tile,i) -> new ItemStack(Items.GOLD_NUGGET)));
		blocks.add(seed_copper = createSimpleSeed(Material.ROCK, "seed_copper", new ResourceLocation(Embers.MODID + ":textures/blocks/material_copper.png"),(tile,i) -> new ItemStack(RegistryManager.nugget_copper)));
		blocks.add(seed_silver = createSimpleSeed(Material.ROCK, "seed_silver", new ResourceLocation(Embers.MODID + ":textures/blocks/material_silver.png"),(tile,i) -> new ItemStack(RegistryManager.nugget_silver)));
		blocks.add(seed_lead = createSimpleSeed(Material.ROCK, "seed_lead", new ResourceLocation(Embers.MODID + ":textures/blocks/material_lead.png"),(tile,i) -> new ItemStack(RegistryManager.nugget_lead)));
		blocks.add(seed_dawnstone = createSimpleSeed(Material.ROCK, "seed_dawnstone", new ResourceLocation(Embers.MODID + ":textures/blocks/material_dawnstone.png"),(tile,i) -> new ItemStack(RegistryManager.nugget_dawnstone)));
		blocks.add(stone_valve = (new BlockStoneValve(unpushable,"stone_valve",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(archaic_geysir = (new BlockArchaicGeysir(Material.ROCK,"archaic_geysir",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(archaic_mech_edge = (new BlockMechEdge(unpushable,"archaic_mech_edge",false)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
		blocks.add(geo_separator = (new BlockGeoSeparator(Material.IRON,"geo_separator",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.6f));

		if (ConfigManager.enableAluminum){
			blocks.add(block_aluminum = (new BlockBase(Material.ROCK,"block_aluminum",true)).setBeaconBase(true).setHarvestProperties("pickaxe", 1).setHardness(1.6f).setLightOpacity(16).setCreativeTab(Embers.resource_tab));
			blocks.add(ore_aluminum = (new BlockBase(Material.ROCK,"ore_aluminum",true)).setIsFullCube(true).setIsOpaqueCube(true).setHarvestProperties("pickaxe", 1).setHardness(1.6f).setLightOpacity(16).setCreativeTab(Embers.resource_tab));
			blocks.add(seed_aluminum = createSimpleSeed(Material.ROCK, "seed_aluminum", new ResourceLocation(Embers.MODID + ":textures/blocks/material_aluminum.png"),(tile,i) -> new ItemStack(RegistryManager.nugget_aluminum)));
		}
		
		if (ConfigManager.enableBronze){
			blocks.add(block_bronze = (new BlockBase(Material.ROCK,"block_bronze",true)).setBeaconBase(true).setHarvestProperties("pickaxe", 1).setHardness(2.3f).setLightOpacity(16).setCreativeTab(Embers.resource_tab));
		}
		
		if (ConfigManager.enableElectrum){
			blocks.add(block_electrum = (new BlockBase(Material.ROCK,"block_electrum",true)).setBeaconBase(true).setHarvestProperties("pickaxe", 1).setHardness(1.6f).setLightOpacity(16).setCreativeTab(Embers.resource_tab));
		}
		
		if (ConfigManager.enableNickel){
			blocks.add(block_nickel = (new BlockBase(Material.ROCK,"block_nickel",true)).setBeaconBase(true).setHarvestProperties("pickaxe", 1).setHardness(2.2f).setLightOpacity(16).setCreativeTab(Embers.resource_tab));
			blocks.add(ore_nickel = (new BlockBase(Material.ROCK,"ore_nickel",true)).setIsFullCube(true).setIsOpaqueCube(true).setHarvestProperties("pickaxe", 1).setHardness(2.2f).setLightOpacity(16).setCreativeTab(Embers.resource_tab));
			blocks.add(seed_nickel = createSimpleSeed(Material.ROCK, "seed_nickel", new ResourceLocation(Embers.MODID + ":textures/blocks/material_nickel.png"),(tile,i) -> new ItemStack(RegistryManager.nugget_nickel)));
		}
		
		if (ConfigManager.enableTin){
			blocks.add(block_tin = (new BlockBase(Material.ROCK,"block_tin",true)).setBeaconBase(true).setHarvestProperties("pickaxe", 1).setHardness(1.3f).setLightOpacity(16).setCreativeTab(Embers.resource_tab));
			blocks.add(ore_tin = (new BlockBase(Material.ROCK,"ore_tin",true)).setIsFullCube(true).setIsOpaqueCube(true).setHarvestProperties("pickaxe", 1).setHardness(1.3f).setLightOpacity(16).setCreativeTab(Embers.resource_tab));
			blocks.add(seed_tin = createSimpleSeed(Material.ROCK, "seed_tin", new ResourceLocation(Embers.MODID + ":textures/blocks/material_tin.png"),(tile,i) -> new ItemStack(RegistryManager.nugget_tin)));
		}
		
		items.add(ingot_copper = new ItemBase("ingot_copper",true).setCreativeTab(Embers.resource_tab));
		items.add(ingot_lead = new ItemBase("ingot_lead",true).setCreativeTab(Embers.resource_tab));
		items.add(ingot_silver = new ItemBase("ingot_silver",true).setCreativeTab(Embers.resource_tab));
		items.add(ingot_dawnstone = new ItemBase("ingot_dawnstone",true).setCreativeTab(Embers.resource_tab));
		// Gloomshroud - Add Mithril Ingot
		//items.add(ingot_mithril = new ItemBase("ingot_mithril", true).setCreativeTab(Embers.resource_tab));
		//items.add(nugget_iron = new ItemBase("nugget_iron",true).setCreativeTab(Embers.resource_tab));
		items.add(nugget_copper = new ItemBase("nugget_copper",true).setCreativeTab(Embers.resource_tab));
		items.add(nugget_lead = new ItemBase("nugget_lead",true).setCreativeTab(Embers.resource_tab));
		items.add(nugget_silver = new ItemBase("nugget_silver",true).setCreativeTab(Embers.resource_tab));
		items.add(nugget_dawnstone = new ItemBase("nugget_dawnstone",true).setCreativeTab(Embers.resource_tab));
		// Gloomshroud - Add Mithril Nugget
		//items.add(nugget_mithril = new ItemBase("nugget_mithril", true).setCreativeTab(Embers.resource_tab));
		items.add(plate_copper = new ItemBase("plate_copper",true).setCreativeTab(Embers.resource_tab));
		items.add(plate_lead = new ItemBase("plate_lead",true).setCreativeTab(Embers.resource_tab));
		items.add(plate_silver = new ItemBase("plate_silver",true).setCreativeTab(Embers.resource_tab));
		items.add(plate_dawnstone = new ItemBase("plate_dawnstone",true).setCreativeTab(Embers.resource_tab));
		items.add(plate_iron = new ItemBase("plate_iron",true).setCreativeTab(Embers.resource_tab));
		items.add(plate_gold = new ItemBase("plate_gold",true).setCreativeTab(Embers.resource_tab));
		// Gloomshroud - Add Mithril Plate
		//items.add(plate_mithril = new ItemBase("plate_mithril", true).setCreativeTab(Embers.resource_tab));
		items.add(brick_caminite = new ItemBase("brick_caminite",true));
		items.add(blend_caminite = new ItemBase("blend_caminite",true));
		items.add(plate_caminite = new ItemBase("plate_caminite",true));
		items.add(plate_caminite_raw = new ItemBase("plate_caminite_raw",true));
		items.add(tinker_hammer = new ItemTinkerHammer());
		items.add(stamp_bar = new ItemBase("stamp_bar",true));
		items.add(stamp_flat = new ItemBase("stamp_flat",true));
		items.add(stamp_plate = new ItemBase("stamp_plate",true));
		items.add(stamp_gear = new ItemBase("stamp_gear",true));
		items.add(stamp_bar_raw = new ItemBase("stamp_bar_raw",true));
		items.add(stamp_flat_raw = new ItemBase("stamp_flat_raw",true));
		items.add(stamp_plate_raw = new ItemBase("stamp_plate_raw",true));
		items.add(stamp_gear_raw = new ItemBase("stamp_gear_raw",true));
		items.add(ember_detector = new ItemEmberGauge());
		items.add(dust_ember = new ItemBase("dust_ember",true));
		items.add(shard_ember = new ItemBase("shard_ember",true));
		items.add(crystal_ember = new ItemBase("crystal_ember",true));
		items.add(pickaxe_copper = new ItemPickaxeBase(tool_mat_copper,"pickaxe_copper",true).setCreativeTab(Embers.resource_tab));
		items.add(axe_copper = new ItemAxeBase2(tool_mat_copper,"axe_copper",true,8.5f,0.9f).setCreativeTab(Embers.resource_tab));
		items.add(shovel_copper = new ItemShovelBase(tool_mat_copper,"shovel_copper",true).setCreativeTab(Embers.resource_tab));
		items.add(hoe_copper = new ItemHoeBase(tool_mat_copper,"hoe_copper",true).setCreativeTab(Embers.resource_tab));
		items.add(sword_copper = new ItemSwordBase(tool_mat_copper,"sword_copper",true).setCreativeTab(Embers.resource_tab));
		items.add(pickaxe_silver = new ItemPickaxeBase(tool_mat_silver,"pickaxe_silver",true).setCreativeTab(Embers.resource_tab));
		items.add(axe_silver = new ItemAxeBase2(tool_mat_silver,"axe_silver",true,9f,1.0f).setCreativeTab(Embers.resource_tab));
		items.add(shovel_silver = new ItemShovelBase(tool_mat_silver,"shovel_silver",true).setCreativeTab(Embers.resource_tab));
		items.add(hoe_silver = new ItemHoeBase(tool_mat_silver,"hoe_silver",true).setCreativeTab(Embers.resource_tab));
		items.add(sword_silver = new ItemSwordBase(tool_mat_silver,"sword_silver",true).setCreativeTab(Embers.resource_tab));
		items.add(pickaxe_lead = new ItemPickaxeBase(tool_mat_lead,"pickaxe_lead",true).setCreativeTab(Embers.resource_tab));
		items.add(axe_lead = new ItemAxeBase2(tool_mat_lead,"axe_lead",true,9f,0.9f).setCreativeTab(Embers.resource_tab));
		items.add(shovel_lead = new ItemShovelBase(tool_mat_lead,"shovel_lead",true).setCreativeTab(Embers.resource_tab));
		items.add(hoe_lead = new ItemHoeBase(tool_mat_lead,"hoe_lead",true).setCreativeTab(Embers.resource_tab));
		items.add(sword_lead = new ItemSwordBase(tool_mat_lead,"sword_lead",true).setCreativeTab(Embers.resource_tab));
		items.add(pickaxe_dawnstone = new ItemPickaxeBase(tool_mat_dawnstone,"pickaxe_dawnstone",true).setCreativeTab(Embers.resource_tab));
		items.add(axe_dawnstone = new ItemAxeBase2(tool_mat_dawnstone,"axe_dawnstone",true,9.5f,1f).setCreativeTab(Embers.resource_tab));
		items.add(shovel_dawnstone = new ItemShovelBase(tool_mat_dawnstone,"shovel_dawnstone",true).setCreativeTab(Embers.resource_tab));
		items.add(hoe_dawnstone = new ItemHoeBase(tool_mat_dawnstone,"hoe_dawnstone",true).setCreativeTab(Embers.resource_tab));
		items.add(sword_dawnstone = new ItemSwordBase(tool_mat_dawnstone,"sword_dawnstone",true).setCreativeTab(Embers.resource_tab));
		items.add(ember_jar = new ItemEmberJar());
		items.add(ember_cartridge = new ItemEmberCartridge());
		items.add(ignition_cannon = new ItemIgnitionCannon());
		items.add(staff_ember = new ItemCinderStaff());
		items.add(axe_clockwork = new ItemClockworkAxe("axe_clockwork",true));
		items.add(pickaxe_clockwork = new ItemClockworkPickaxe("pickaxe_clockwork",true));
		items.add(grandhammer = new ItemGrandhammer("grandhammer",true));
		items.add(dust_ash = new ItemBase("dust_ash",true));
		items.add(aspectus_iron = new ItemBase("aspectus_iron",true));
		items.add(aspectus_copper = new ItemBase("aspectus_copper",true));
		items.add(aspectus_lead = new ItemBase("aspectus_lead",true));
		items.add(aspectus_silver = new ItemBase("aspectus_silver",true));
		items.add(aspectus_dawnstone = new ItemBase("aspectus_dawnstone",true));
		items.add(alchemic_waste = new ItemAlchemicWaste());
		//items.add(shardAster = new ItemBase("shardAster",true));
		items.add(ashen_cloak_head = new ItemAshenCloak(armor_mat_ashen_cloak, 3, EntityEquipmentSlot.HEAD));
		items.add(ashen_cloak_chest = new ItemAshenCloak(armor_mat_ashen_cloak, 7, EntityEquipmentSlot.CHEST));
		items.add(ashen_cloak_legs = new ItemAshenCloak(armor_mat_ashen_cloak, 5, EntityEquipmentSlot.LEGS));
		items.add(ashen_cloak_boots = new ItemAshenCloak(armor_mat_ashen_cloak, 3, EntityEquipmentSlot.FEET));
		items.add(inflictor_gem = new ItemInflictorGem());
		items.add(glimmer_shard = new ItemGlimmerShard());
		items.add(glimmer_lamp = new ItemGlimmerLamp());
		items.add(ashen_cloth = new ItemBase("ashen_cloth",true));
		items.add(archaic_brick = new ItemBase("archaic_brick",true));
		items.add(ancient_motive_core = new ItemBase("ancient_motive_core",true));
		items.add(isolated_materia = new ItemBase("isolated_materia",true));
		items.add(tyrfing = new ItemTyrfing(tool_mat_tyrfing,"tyrfing",true));
		items.add(adhesive = new ItemBase("adhesive",true));
		items.add(ember_cluster = new ItemBase("ember_cluster",true));
		items.add(wildfire_core = new ItemBase("wildfire_core",true));
		items.add(codex = new ItemCodex());
		items.add(superheater = new ItemBase("superheater",true));
		items.add(jet_augment = new ItemBase("jet_augment",true));
		items.add(blasting_core = new ItemBase("blasting_core",true));
		items.add(caster_orb = new ItemBase("caster_orb",true));
		items.add(resonating_bell = new ItemBase("resonating_bell",true));
		items.add(flame_barrier = new ItemBase("flame_barrier",true));
		items.add(eldritch_insignia = new ItemBase("eldritch_insignia",true));
		items.add(intelligent_apparatus = new ItemBase("intelligent_apparatus",true));
		items.add(archaic_circuit = new ItemBase("archaic_circuit",true));

		items.add(dust_metallurgic = new ItemMetallurgicDust("dust_metallurgic",true));
		items.add(diffraction_barrel = new ItemBase("diffraction_barrel",true));
		items.add(focal_lens = new ItemBase("focal_lens",true));
		items.add(tinker_lens = new ItemTinkerLens("tinker_lens",true));
		items.add(anti_tinker_lens = new ItemBase("anti_tinker_lens",true));
		items.add(shifting_scales = new ItemBase("shifting_scales",true));
		items.add(winding_gears = new ItemBase("winding_gears",true));
		//items.add(core_stone = new ItemBase("core_stone",true));
		items.add(creative_heat = new ItemHidden("creative_heat"));
		
		if (ConfigManager.enableAluminum){
			tool_mat_aluminum = EnumHelper.addToolMaterial(Embers.MODID+":aluminum", 2, 220, 5.2f, 1.5f, 14);
			items.add(ingot_aluminum = new ItemBase("ingot_aluminum",true).setCreativeTab(Embers.resource_tab));
			items.add(plate_aluminum = new ItemBase("plate_aluminum",true).setCreativeTab(Embers.resource_tab));
			items.add(nugget_aluminum = new ItemBase("nugget_aluminum",true).setCreativeTab(Embers.resource_tab));
			items.add(pickaxe_aluminum = new ItemPickaxeBase(tool_mat_aluminum, "pickaxe_aluminum",true).setCreativeTab(Embers.resource_tab));
			items.add(axe_aluminum = new ItemAxeBase(tool_mat_aluminum, "axe_aluminum",true).setCreativeTab(Embers.resource_tab));
			items.add(shovel_aluminum = new ItemShovelBase(tool_mat_aluminum, "shovel_aluminum",true).setCreativeTab(Embers.resource_tab));
			items.add(sword_aluminum = new ItemSwordBase(tool_mat_aluminum, "sword_aluminum",true).setCreativeTab(Embers.resource_tab));
			items.add(hoe_aluminum = new ItemHoeBase(tool_mat_aluminum, "hoe_aluminum",true).setCreativeTab(Embers.resource_tab));
			
			tool_mat_aluminum.setRepairItem(new ItemStack(ingot_aluminum));
		}
		
		if (ConfigManager.enableBronze){
			tool_mat_bronze = EnumHelper.addToolMaterial(Embers.MODID+":bronze", 2, 510, 6.5f, 2.0f, 20);
			items.add(ingot_bronze = new ItemBase("ingot_bronze",true).setCreativeTab(Embers.resource_tab));
			items.add(plate_bronze = new ItemBase("plate_bronze",true).setCreativeTab(Embers.resource_tab));
			items.add(nugget_bronze = new ItemBase("nugget_bronze",true).setCreativeTab(Embers.resource_tab));
			items.add(pickaxe_bronze = new ItemPickaxeBase(tool_mat_bronze, "pickaxe_bronze",true).setCreativeTab(Embers.resource_tab));
			items.add(axe_bronze = new ItemAxeBase(tool_mat_bronze, "axe_bronze",true).setCreativeTab(Embers.resource_tab));
			items.add(shovel_bronze = new ItemShovelBase(tool_mat_bronze, "shovel_bronze",true).setCreativeTab(Embers.resource_tab));
			items.add(sword_bronze = new ItemSwordBase(tool_mat_bronze, "sword_bronze",true).setCreativeTab(Embers.resource_tab));
			items.add(hoe_bronze = new ItemHoeBase(tool_mat_bronze, "hoe_bronze",true).setCreativeTab(Embers.resource_tab));
			
			tool_mat_bronze.setRepairItem(new ItemStack(ingot_bronze));
		}
		
		if (ConfigManager.enableElectrum){
			tool_mat_electrum = EnumHelper.addToolMaterial(Embers.MODID+":electrum", 2, 71, 10.8f, 1.0f, 30);
			items.add(ingot_electrum = new ItemBase("ingot_electrum",true).setCreativeTab(Embers.resource_tab));
			items.add(plate_electrum = new ItemBase("plate_electrum",true).setCreativeTab(Embers.resource_tab));
			items.add(nugget_electrum = new ItemBase("nugget_electrum",true).setCreativeTab(Embers.resource_tab));
			items.add(pickaxe_electrum = new ItemPickaxeBase(tool_mat_electrum, "pickaxe_electrum",true).setCreativeTab(Embers.resource_tab));
			items.add(axe_electrum = new ItemAxeBase(tool_mat_electrum, "axe_electrum",true).setCreativeTab(Embers.resource_tab));
			items.add(shovel_electrum = new ItemShovelBase(tool_mat_electrum, "shovel_electrum",true).setCreativeTab(Embers.resource_tab));
			items.add(sword_electrum = new ItemSwordBase(tool_mat_electrum, "sword_electrum",true).setCreativeTab(Embers.resource_tab));
			items.add(hoe_electrum = new ItemHoeBase(tool_mat_electrum, "hoe_electrum",true).setCreativeTab(Embers.resource_tab));
			
			tool_mat_electrum.setRepairItem(new ItemStack(ingot_electrum));
		}
		
		if (ConfigManager.enableNickel){
			tool_mat_nickel = EnumHelper.addToolMaterial(Embers.MODID+":nickel", 2, 331, 6.4f, 2.0f, 18);
			items.add(ingot_nickel = new ItemBase("ingot_nickel",true).setCreativeTab(Embers.resource_tab));
			items.add(plate_nickel = new ItemBase("plate_nickel",true).setCreativeTab(Embers.resource_tab));
			items.add(nugget_nickel = new ItemBase("nugget_nickel",true).setCreativeTab(Embers.resource_tab));
			items.add(pickaxe_nickel = new ItemPickaxeBase(tool_mat_nickel, "pickaxe_nickel",true).setCreativeTab(Embers.resource_tab));
			items.add(axe_nickel = new ItemAxeBase(tool_mat_nickel, "axe_nickel",true).setCreativeTab(Embers.resource_tab));
			items.add(shovel_nickel = new ItemShovelBase(tool_mat_nickel, "shovel_nickel",true).setCreativeTab(Embers.resource_tab));
			items.add(sword_nickel = new ItemSwordBase(tool_mat_nickel, "sword_nickel",true).setCreativeTab(Embers.resource_tab));
			items.add(hoe_nickel = new ItemHoeBase(tool_mat_nickel, "hoe_nickel",true).setCreativeTab(Embers.resource_tab));
			
			tool_mat_nickel.setRepairItem(new ItemStack(ingot_nickel));
		}
		
		if (ConfigManager.enableTin){
			tool_mat_tin = EnumHelper.addToolMaterial(Embers.MODID+":tin", 1, 145, 4.9f, 1.0f, 12);
			items.add(ingot_tin = new ItemBase("ingot_tin",true).setCreativeTab(Embers.resource_tab));
			items.add(plate_tin = new ItemBase("plate_tin",true).setCreativeTab(Embers.resource_tab));
			items.add(nugget_tin = new ItemBase("nugget_tin",true).setCreativeTab(Embers.resource_tab));
			items.add(pickaxe_tin = new ItemPickaxeBase(tool_mat_tin, "pickaxe_tin",true).setCreativeTab(Embers.resource_tab));
			items.add(axe_tin = new ItemAxeBase(tool_mat_tin, "axe_tin",true).setCreativeTab(Embers.resource_tab));
			items.add(shovel_tin = new ItemShovelBase(tool_mat_tin, "shovel_tin",true).setCreativeTab(Embers.resource_tab));
			items.add(sword_tin = new ItemSwordBase(tool_mat_tin, "sword_tin",true).setCreativeTab(Embers.resource_tab));
			items.add(hoe_tin = new ItemHoeBase(tool_mat_tin, "hoe_tin",true).setCreativeTab(Embers.resource_tab));
			
			tool_mat_tin.setRepairItem(new ItemStack(ingot_tin));
		}
		
		tool_mat_copper.setRepairItem(new ItemStack(ingot_copper));
		tool_mat_silver.setRepairItem(new ItemStack(ingot_silver));
		tool_mat_lead.setRepairItem(new ItemStack(ingot_lead));
		tool_mat_dawnstone.setRepairItem(new ItemStack(ingot_dawnstone));
		tool_mat_tyrfing.setRepairItem(new ItemStack(dust_ash));
		
		armor_mat_ashen_cloak.repairMaterial = new ItemStack(ashen_cloth,1);
		
		/*
		items.add(ingotAstralite = new ItemBase("ingotAstralite",true));
		items.add(ingotUmberSteel = new ItemBase("ingotUmberSteel",true));*/
		
		registerFluids();
		registerTileEntities();
		registerEntities();
		registerItemModifiers();
		
		List<BiomeEntry> biomeEntries = new ArrayList<BiomeEntry>();
		biomeEntries.addAll(BiomeManager.getBiomes(BiomeType.COOL));
		biomeEntries.addAll(BiomeManager.getBiomes(BiomeType.DESERT));
		biomeEntries.addAll(BiomeManager.getBiomes(BiomeType.ICY));
		biomeEntries.addAll(BiomeManager.getBiomes(BiomeType.WARM));
		List<Biome> biomes = new ArrayList<Biome>();
		for (BiomeEntry b : biomeEntries){
			biomes.add(b.biome);
		}
		biomes.addAll(BiomeManager.oceanBiomes);
		
		EntityRegistry.addSpawn(EntityAncientGolem.class, ConfigManager.ancientGolemSpawnWeight, 1, 1, EnumCreatureType.MONSTER, biomes.toArray(new Biome[biomes.size()]));
		
		world_gen_ores = new WorldGenOres();
		GameRegistry.registerWorldGenerator(world_gen_ores, 1);
		int weight = 400;
		GameRegistry.registerWorldGenerator(world_gen_small_ruin = new WorldGenSmallRuin(), weight ++);

		//GameRegistry.register(biomeCave = new BiomeCave());
		
		//dimensionCave = DimensionType.register("cave", "cave", 90, CaveProvider.class, false);
		//BiomeManager.addBiome(BiomeType.DESERT, new BiomeEntry(biomeCave, 10000));

		MinecraftForge.EVENT_BUS.register(EmbersFuelHandler.class);

		if(ConfigManager.isBaublesIntegrationEnabled())
			BaublesIntegration.registerAll();
		if(ConfigManager.isMysticalMechanicsIntegrationEnabled())
			MysticalMechanicsIntegration.registerAll();

		UpgradeCatalyticPlug.registerBlacklistedTile(TileEntityBeamCannon.class);
	}

	private static void registerEntities() {
		int id = 0;

		EntityRegistry.registerModEntity(new ResourceLocation(Embers.MODID+":ember_packet"),EntityEmberPacket.class, "ember_packet", id++, Embers.instance, 64, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Embers.MODID+":ember_projectile"),EntityEmberProjectile.class, "ember_projectile", id++, Embers.instance, 64, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Embers.MODID+":ancient_golem"),EntityAncientGolem.class, "ancient_golem", id++, Embers.instance, 64, 1, true);
		EntityRegistry.registerEgg(new ResourceLocation(Embers.MODID+":ancient_golem"), Misc.intColor(48, 38, 35), Misc.intColor(79, 66, 61));
		EntityRegistry.registerModEntity(new ResourceLocation(Embers.MODID+":ember_light"),EntityEmberLight.class, "ember_light", id++, Embers.instance, 64, 1, true);
	}

	private static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityTank.class, Embers.MODID+":tile_entity_tank");
		GameRegistry.registerTileEntity(TileEntityFluidPipe.class, Embers.MODID+":tile_entity_pipe");
		GameRegistry.registerTileEntity(TileEntityFluidExtractor.class, Embers.MODID+":tile_entity_pump");
		GameRegistry.registerTileEntity(TileEntityFurnaceTop.class, Embers.MODID+":tile_entity_furnace_top");
		GameRegistry.registerTileEntity(TileEntityFurnaceBottom.class, Embers.MODID+":tile_entity_furnace_bottom");
		GameRegistry.registerTileEntity(TileEntityEmitter.class, Embers.MODID+":tile_entity_emitter");
		GameRegistry.registerTileEntity(TileEntityReceiver.class, Embers.MODID+":tile_entity_receiver");
		GameRegistry.registerTileEntity(TileEntityCopperCell.class, Embers.MODID+":tile_entity_copper_cell");
		GameRegistry.registerTileEntity(TileEntityItemPipe.class, Embers.MODID+":tile_entity_item_pipe");
		GameRegistry.registerTileEntity(TileEntityItemExtractor.class, Embers.MODID+":tile_entity_item_pump");
		GameRegistry.registerTileEntity(TileEntityBin.class, Embers.MODID+":tile_entity_bin");
		GameRegistry.registerTileEntity(TileEntityStamper.class, Embers.MODID+":tile_entity_stamper");
		GameRegistry.registerTileEntity(TileEntityStampBase.class, Embers.MODID+":tile_entity_stamp_base");
		GameRegistry.registerTileEntity(TileEntityEmberBore.class, Embers.MODID+":tile_entity_ember_bore");
		GameRegistry.registerTileEntity(TileEntityMechAccessor.class, Embers.MODID+":tile_entity_mech_accessor");
		GameRegistry.registerTileEntity(TileEntityMechCore.class, Embers.MODID+":tile_entity_mech_core");
		GameRegistry.registerTileEntity(TileEntityActivatorTop.class, Embers.MODID+":tile_entity_activator_top");
		GameRegistry.registerTileEntity(TileEntityActivatorBottom.class, Embers.MODID+":tile_entity_activator_bottom");
		GameRegistry.registerTileEntity(TileEntityMixerTop.class, Embers.MODID+":tile_entity_mixer_top");
		GameRegistry.registerTileEntity(TileEntityMixerBottom.class, Embers.MODID+":tile_entity_mixer_bottom");
		GameRegistry.registerTileEntity(TileEntityHeatCoil.class, Embers.MODID+":tile_entity_heat_coil");
		GameRegistry.registerTileEntity(TileEntityDropper.class, Embers.MODID+":tile_entity_dropper");
		GameRegistry.registerTileEntity(TileEntityLargeTank.class, Embers.MODID+":tile_entity_large_tank");
		GameRegistry.registerTileEntity(TileEntityBeamSplitter.class, Embers.MODID+":tile_entity_beam_splitter");
		GameRegistry.registerTileEntity(TileEntityRelay.class, Embers.MODID+":tile_entity_relay");
		GameRegistry.registerTileEntity(TileEntityCrystalCell.class, Embers.MODID+":tile_entity_crystal_cell");
		GameRegistry.registerTileEntity(TileEntityCharger.class, Embers.MODID+":tile_entity_charger");
		GameRegistry.registerTileEntity(TileEntityCinderPlinth.class, Embers.MODID+":tile_entity_cinder_plinth");
		GameRegistry.registerTileEntity(TileEntityKnowledgeTable.class, Embers.MODID+":tile_entity_knowledge_table");
		GameRegistry.registerTileEntity(TileEntityAlchemyPedestal.class, Embers.MODID+":tile_entity_alchemy_pedestal");
		GameRegistry.registerTileEntity(TileEntityAlchemyTablet.class, Embers.MODID+":tile_entity_alchemy_tablet");
		GameRegistry.registerTileEntity(TileEntityItemTransfer.class, Embers.MODID+":tile_entity_item_transfer");
		GameRegistry.registerTileEntity(TileEntityBeamCannon.class, Embers.MODID+":tile_entity_beam_cannon");
		GameRegistry.registerTileEntity(TileEntityDawnstoneAnvil.class, Embers.MODID+":tile_entity_dawnstone_anvil");
		GameRegistry.registerTileEntity(TileEntityAutoHammer.class, Embers.MODID+":tile_entity_auto_hammer");
		GameRegistry.registerTileEntity(TileEntityItemVacuum.class, Embers.MODID+":tile_entity_vacuum");
		GameRegistry.registerTileEntity(TileEntityBreaker.class, Embers.MODID+":tile_entity_breaker");
		GameRegistry.registerTileEntity(TileEntitySeed.class, Embers.MODID+":tile_entity_seed");
		GameRegistry.registerTileEntity(TileEntitySeedNew.class, Embers.MODID+":tile_entity_seed_new");
		GameRegistry.registerTileEntity(TileEntityEmberInjector.class, Embers.MODID+":tile_entity_ember_injector");
		GameRegistry.registerTileEntity(TileEntityBoilerBottom.class, Embers.MODID+":tile_entity_boiler_bottom");
		GameRegistry.registerTileEntity(TileEntityBoilerTop.class, Embers.MODID+":tile_entity_boiler_top");
		GameRegistry.registerTileEntity(TileEntityReactor.class, Embers.MODID+":tile_entity_reactor");
		GameRegistry.registerTileEntity(TileEntityCombustor.class, Embers.MODID+":tile_entity_combustor");
		GameRegistry.registerTileEntity(TileEntityCatalyzer.class, Embers.MODID+":tile_entity_catalyzer");
		GameRegistry.registerTileEntity(TileEntityFieldChart.class, Embers.MODID+":tile_entity_field_chart");
		GameRegistry.registerTileEntity(TileEntityPulser.class, Embers.MODID+":tile_entity_pulser");
		GameRegistry.registerTileEntity(TileEntityInfernoForge.class, Embers.MODID+":tile_entity_inferno_forge");
		GameRegistry.registerTileEntity(TileEntityInfernoForgeOpening.class, Embers.MODID+":tile_entity_inferno_forge_opening");
		GameRegistry.registerTileEntity(TileEntityCreativeEmberSource.class, Embers.MODID+":tile_entity_creative_ember_source");
		GameRegistry.registerTileEntity(TileEntityPumpBottom.class, Embers.MODID+":tile_entity_pump_bottom");
		GameRegistry.registerTileEntity(TileEntityPumpTop.class, Embers.MODID+":tile_entity_pump_top");
		GameRegistry.registerTileEntity(TileEntityCatalyticPlug.class, Embers.MODID+":tile_entity_catalytic_plug");
		GameRegistry.registerTileEntity(TileEntityEmberFunnel.class, Embers.MODID+":tile_entity_ember_funnel");
		GameRegistry.registerTileEntity(TileEntityMiniBoiler.class, Embers.MODID+":tile_entity_mini_boiler");
		GameRegistry.registerTileEntity(TileEntityEmberGauge.class, Embers.MODID+":tile_entity_ember_gauge");
		GameRegistry.registerTileEntity(TileEntityFluidGauge.class, Embers.MODID+":tile_entity_fluid_gauge");
		//GameRegistry.registerTileEntity(TileEntityItemGauge.class, Embers.MODID+":tile_entity_item_gauge");
		GameRegistry.registerTileEntity(TileEntityFluidTransfer.class, Embers.MODID+":tile_entity_fluid_transfer");
		GameRegistry.registerTileEntity(TileEntityStirling.class, Embers.MODID+":tile_entity_stirling");
		GameRegistry.registerTileEntity(TileEntityEmberSiphon.class, Embers.MODID+":tile_entity_ember_siphon");
		GameRegistry.registerTileEntity(TileEntityClockworkAttenuator.class, Embers.MODID+":tile_entity_clockwork_attenuator");
		GameRegistry.registerTileEntity(TileEntityArchaicGeysir.class, Embers.MODID+":tile_entity_archaic_geysir");
		GameRegistry.registerTileEntity(TileEntityStoneValve.class, Embers.MODID+":tile_entity_stone_valve");
		GameRegistry.registerTileEntity(TileEntityGeoSeparator.class, Embers.MODID+":tile_entity_geo_separator");
	}

	private static void registerCapabilities() {
		CapabilityManager.INSTANCE.register(IEmberCapability.class, new EmberCapabilityStorage(), DefaultEmberCapability.class);
		CapabilityManager.INSTANCE.register(IUpgradeProvider.class, new Capability.IStorage<IUpgradeProvider>() {
			@Nullable
			@Override
			public NBTBase writeNBT(Capability<IUpgradeProvider> capability, IUpgradeProvider instance, EnumFacing side) {
				return null;
			}

			@Override
			public void readNBT(Capability<IUpgradeProvider> capability, IUpgradeProvider instance, EnumFacing side, NBTBase nbt) {
				//NOOP
			}
		}, () -> {
			return new DefaultUpgradeProvider("none", null);
		});
		CapabilityManager.INSTANCE.register(IResearchCapability.class, new Capability.IStorage<IResearchCapability>() {
			@Nullable
			@Override
			public NBTBase writeNBT(Capability<IResearchCapability> capability, IResearchCapability instance, EnumFacing side) {
				NBTTagCompound compound = new NBTTagCompound();
				instance.writeToNBT(compound);
				return compound;
			}

			@Override
			public void readNBT(Capability<IResearchCapability> capability, IResearchCapability instance, EnumFacing side, NBTBase nbt) {
				NBTTagCompound compound = (NBTTagCompound) nbt;
				instance.readFromNBT(compound);
			}
		}, DefaultResearchCapability::new);
	}

	public static void registerItemModifiers(){
		EmbersAPI.CORE = new ModifierCore();
		EmbersAPI.SUPERHEATER = new ModifierSuperheater();
		EmbersAPI.JET_AUGMENT = new ModifierCinderJet();
		EmbersAPI.CASTER_ORB = new ModifierCasterOrb();
		EmbersAPI.RESONATING_BELL = new ModifierResonatingBell();
		EmbersAPI.BLASTING_CORE = new ModifierBlastingCore();
		EmbersAPI.FLAME_BARRIER = new ModifierFlameBarrier();
		EmbersAPI.ELDRITCH_INSIGNIA = new ModifierEldritchInsignia();
		EmbersAPI.INTELLIGENT_APPARATUS = new ModifierIntelligentApparatus();
		EmbersAPI.DIFFRACTION = new ModifierDiffraction();
		EmbersAPI.FOCAL_LENS = new ModifierFocalLens();
		EmbersAPI.TINKER_LENS = new ModifierTinkerLens("tinker_lens",false);
		EmbersAPI.ANTI_TINKER_LENS = new ModifierTinkerLens("anti_tinker_lens",true);
		EmbersAPI.SHIFTING_SCALES = new ModifierShiftingScales();
		EmbersAPI.WINDING_GEARS = new ModifierWindingGears();

		EmbersAPI.registerModifier(ancient_motive_core, EmbersAPI.CORE);
		EmbersAPI.registerModifier(superheater, EmbersAPI.SUPERHEATER);
		EmbersAPI.registerModifier(jet_augment, EmbersAPI.JET_AUGMENT);
		EmbersAPI.registerModifier(caster_orb, EmbersAPI.CASTER_ORB);
		EmbersAPI.registerModifier(resonating_bell, EmbersAPI.RESONATING_BELL);
		EmbersAPI.registerModifier(blasting_core, EmbersAPI.BLASTING_CORE);
		EmbersAPI.registerModifier(flame_barrier, EmbersAPI.FLAME_BARRIER);
		EmbersAPI.registerModifier(eldritch_insignia, EmbersAPI.ELDRITCH_INSIGNIA);
		EmbersAPI.registerModifier(intelligent_apparatus, EmbersAPI.INTELLIGENT_APPARATUS);
		EmbersAPI.registerModifier(diffraction_barrel, EmbersAPI.DIFFRACTION);
		EmbersAPI.registerModifier(focal_lens, EmbersAPI.FOCAL_LENS);
		EmbersAPI.registerModifier(tinker_lens, EmbersAPI.TINKER_LENS);
		EmbersAPI.registerModifier(anti_tinker_lens, EmbersAPI.ANTI_TINKER_LENS);
		EmbersAPI.registerModifier(shifting_scales, EmbersAPI.SHIFTING_SCALES);
		EmbersAPI.registerModifier(winding_gears, EmbersAPI.WINDING_GEARS);
	}

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event){
		for (Block b : blocks){
			event.getRegistry().register(b);
		}
	}
	
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event){
		for (Item i : items){
			event.getRegistry().register(i);
		}
		for (Block b : blocks){
			if (b instanceof IBlock){
				Item itemBlock = ((IBlock) b).getItemBlock();
				if(itemBlock != null)
					event.getRegistry().register(itemBlock);
			}
		}
	}
	
	public static void registerFluids(){
		FluidRegistry.registerFluid(fluid_steam = new FluidSteam());

		FluidRegistry.registerFluid(fluid_molten_iron = new FluidMoltenIron());
		blocks.add(block_molten_iron = (new BlockMolten(fluid_molten_iron)));
		FluidRegistry.addBucketForFluid(fluid_molten_iron);
		
		FluidRegistry.registerFluid(fluid_molten_gold = new FluidMoltenGold());
		blocks.add(block_molten_gold = (new BlockMolten(fluid_molten_gold)));
		FluidRegistry.addBucketForFluid(fluid_molten_gold);
		
		FluidRegistry.registerFluid(fluid_molten_lead = new FluidMoltenLead());
		blocks.add(block_molten_lead = (new BlockMolten(fluid_molten_lead)));
		FluidRegistry.addBucketForFluid(fluid_molten_lead);
		
		FluidRegistry.registerFluid(fluid_molten_copper = new FluidMoltenCopper());
		blocks.add(block_molten_copper = (new BlockMolten(fluid_molten_copper)));
		FluidRegistry.addBucketForFluid(fluid_molten_copper);
		
		FluidRegistry.registerFluid(fluid_molten_silver = new FluidMoltenSilver());
		blocks.add(block_molten_silver = (new BlockMolten(fluid_molten_silver)));
		FluidRegistry.addBucketForFluid(fluid_molten_silver);
		
		FluidRegistry.registerFluid(fluid_molten_dawnstone = new FluidMoltenDawnstone());
		blocks.add(block_molten_dawnstone = (new BlockMolten(fluid_molten_dawnstone)));
		FluidRegistry.addBucketForFluid(fluid_molten_dawnstone);
		
		FluidRegistry.registerFluid(fluid_molten_tin = new FluidMoltenTin());
		blocks.add(block_molten_tin = (new BlockMolten(fluid_molten_tin)));
		FluidRegistry.addBucketForFluid(fluid_molten_tin);
		
		FluidRegistry.registerFluid(fluid_molten_aluminum = new FluidMoltenAluminum());
		blocks.add(block_molten_aluminum = (new BlockMolten(fluid_molten_aluminum)));
		FluidRegistry.addBucketForFluid(fluid_molten_aluminum);
		
		FluidRegistry.registerFluid(fluid_molten_nickel = new FluidMoltenNickel());
		blocks.add(block_molten_nickel = (new BlockMolten(fluid_molten_nickel)));
		FluidRegistry.addBucketForFluid(fluid_molten_nickel);
		
		FluidRegistry.registerFluid(fluid_molten_bronze = new FluidMoltenBronze());
		blocks.add(block_molten_bronze = (new BlockMolten(fluid_molten_bronze)));
		FluidRegistry.addBucketForFluid(fluid_molten_bronze);
		
		FluidRegistry.registerFluid(fluid_molten_electrum = new FluidMoltenElectrum());
		blocks.add(block_molten_electrum = (new BlockMolten(fluid_molten_electrum)));
		FluidRegistry.addBucketForFluid(fluid_molten_electrum);

		FluidRegistry.registerFluid(fluid_alchemical_redstone = new FluidMoltenMetal("alchemical_redstone","alchemic_slurry"));
		blocks.add(block_alchemical_redstone = (new BlockMolten(fluid_alchemical_redstone)));
		FluidRegistry.addBucketForFluid(fluid_alchemical_redstone);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerColorHandlers(){
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemEmberJar.ColorHandler(), ember_jar);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemEmberCartridge.ColorHandler(), ember_cartridge);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemTyrfing.ColorHandler(), tyrfing);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerEntityRendering(){
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTank.class, new TileEntityTankRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidPipe.class, new TileEntityFluidPipeRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidExtractor.class, new TileEntityFluidExtractorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFurnaceTop.class, new TileEntityFurnaceTopRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEmitter.class, new TileEntityEmitterRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityItemPipe.class, new TileEntityItemPipeRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityItemExtractor.class, new TileEntityItemExtractorRenderer());
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
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityItemTransfer.class, new TileEntityItemTransferRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBeamCannon.class, new TileEntityBeamCannonRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDawnstoneAnvil.class, new TileEntityDawnstoneAnvilRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoHammer.class, new TileEntityAutoHammerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBreaker.class, new TileEntityBreakerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySeed.class, new TileEntitySeedRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySeedNew.class, new TileEntitySeedNewRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFieldChart.class, new TileEntityFieldChartRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPulser.class, new TileEntityPulserRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInfernoForgeOpening.class, new TileEntityInfernoForgeOpeningRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPumpBottom.class, new TileEntityPumpRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidTransfer.class, new TileEntityFluidTransferRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGeoSeparator.class, new TileEntityGeoSeparatorRenderer());
		
		RenderingRegistry.registerEntityRenderingHandler(EntityEmberPacket.class, new RenderEmberPacket(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityEmberProjectile.class, new RenderEmberPacket(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityAncientGolem.class, new RenderAncientGolem.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityEmberLight.class, new RenderEmberPacket(Minecraft.getMinecraft().getRenderManager()));
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
    public void registerRendering(ModelRegistryEvent event){
		
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
