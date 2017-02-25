package teamroots.embers.research;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import teamroots.embers.RegistryManager;

public class ResearchManager {
	public static Map<String, ResearchBase> researches = new HashMap<String, ResearchBase>();
	
	public static void initResearches(){
		researches.put(RegistryManager.ore_copper.getRegistryName().toString(), 
				new ResearchBase("ore_copper", new ItemStack(RegistryManager.ore_copper))
				);
		researches.put(RegistryManager.ore_lead.getRegistryName().toString(), 
				new ResearchBase("ore_lead", new ItemStack(RegistryManager.ore_lead))
				);
		researches.put(RegistryManager.ore_silver.getRegistryName().toString(), 
				new ResearchBase("ore_silver", new ItemStack(RegistryManager.ore_silver))
				);
		
		researches.put(RegistryManager.ingot_copper.getRegistryName().toString(), 
				new ResearchBase("ingot_copper", new ItemStack(RegistryManager.ingot_copper))
				);
		researches.put(RegistryManager.ingot_lead.getRegistryName().toString(), 
				new ResearchBase("ingot_lead", new ItemStack(RegistryManager.ingot_lead))
				);
		researches.put(RegistryManager.ingot_silver.getRegistryName().toString(), 
				new ResearchBase("ingot_silver", new ItemStack(RegistryManager.ingot_silver))
				);
		researches.put(RegistryManager.ingot_dawnstone.getRegistryName().toString(), 
				new ResearchBase("ingot_dawnstone", new ItemStack(RegistryManager.ingot_dawnstone))
				);
		
		researches.put(RegistryManager.nugget_copper.getRegistryName().toString(), 
				new ResearchBase("nugget_copper", new ItemStack(RegistryManager.nugget_copper))
				);
		researches.put(RegistryManager.nugget_lead.getRegistryName().toString(), 
				new ResearchBase("nugget_lead", new ItemStack(RegistryManager.nugget_lead))
				);
		researches.put(RegistryManager.nugget_silver.getRegistryName().toString(), 
				new ResearchBase("nugget_silver", new ItemStack(RegistryManager.nugget_silver))
				);
		researches.put(RegistryManager.nugget_dawnstone.getRegistryName().toString(), 
				new ResearchBase("nugget_dawnstone", new ItemStack(RegistryManager.nugget_dawnstone))
				);
		researches.put(RegistryManager.nugget_iron.getRegistryName().toString(), 
				new ResearchBase("nugget_iron", new ItemStack(RegistryManager.nugget_iron))
				);
		
		researches.put(RegistryManager.plate_copper.getRegistryName().toString(), 
				new ResearchBase("plate_copper", new ItemStack(RegistryManager.plate_copper))
				);
		researches.put(RegistryManager.plate_lead.getRegistryName().toString(), 
				new ResearchBase("plate_lead", new ItemStack(RegistryManager.plate_lead))
				);
		researches.put(RegistryManager.plate_silver.getRegistryName().toString(), 
				new ResearchBase("plate_silver", new ItemStack(RegistryManager.plate_silver))
				);
		researches.put(RegistryManager.plate_dawnstone.getRegistryName().toString(), 
				new ResearchBase("plate_dawnstone", new ItemStack(RegistryManager.plate_dawnstone))
				);
		researches.put(RegistryManager.plate_iron.getRegistryName().toString(), 
				new ResearchBase("plate_iron", new ItemStack(RegistryManager.plate_iron))
				);
		researches.put(RegistryManager.plate_gold.getRegistryName().toString(), 
				new ResearchBase("plate_gold", new ItemStack(RegistryManager.plate_gold))
				);
		
		researches.put(RegistryManager.block_copper.getRegistryName().toString(), 
				new ResearchBase("block_copper", new ItemStack(RegistryManager.block_copper))
				);
		researches.put(RegistryManager.block_lead.getRegistryName().toString(), 
				new ResearchBase("block_lead", new ItemStack(RegistryManager.block_lead))
				);
		researches.put(RegistryManager.block_silver.getRegistryName().toString(), 
				new ResearchBase("block_silver", new ItemStack(RegistryManager.block_silver))
				);
		researches.put(RegistryManager.block_dawnstone.getRegistryName().toString(), 
				new ResearchBase("block_dawnstone", new ItemStack(RegistryManager.block_dawnstone))
				);

		researches.put(RegistryManager.shovel_copper.getRegistryName().toString(), 
				new ResearchBase("shovel_copper", new ItemStack(RegistryManager.shovel_copper))
				);
		researches.put(RegistryManager.shovel_lead.getRegistryName().toString(), 
				new ResearchBase("shovel_lead", new ItemStack(RegistryManager.shovel_lead))
				);
		researches.put(RegistryManager.shovel_silver.getRegistryName().toString(), 
				new ResearchBase("shovel_silver", new ItemStack(RegistryManager.shovel_silver))
				);
		researches.put(RegistryManager.shovel_dawnstone.getRegistryName().toString(), 
				new ResearchBase("shovel_dawnstone", new ItemStack(RegistryManager.shovel_dawnstone))
				);

		researches.put(RegistryManager.hoe_copper.getRegistryName().toString(), 
				new ResearchBase("hoe_copper", new ItemStack(RegistryManager.hoe_copper))
				);
		researches.put(RegistryManager.hoe_lead.getRegistryName().toString(), 
				new ResearchBase("hoe_lead", new ItemStack(RegistryManager.hoe_lead))
				);
		researches.put(RegistryManager.hoe_silver.getRegistryName().toString(), 
				new ResearchBase("hoe_silver", new ItemStack(RegistryManager.hoe_silver))
				);
		researches.put(RegistryManager.hoe_dawnstone.getRegistryName().toString(), 
				new ResearchBase("hoe_dawnstone", new ItemStack(RegistryManager.hoe_dawnstone))
				);

		researches.put(RegistryManager.sword_copper.getRegistryName().toString(), 
				new ResearchBase("sword_copper", new ItemStack(RegistryManager.sword_copper))
				);
		researches.put(RegistryManager.sword_lead.getRegistryName().toString(), 
				new ResearchBase("sword_lead", new ItemStack(RegistryManager.sword_lead))
				);
		researches.put(RegistryManager.sword_silver.getRegistryName().toString(), 
				new ResearchBase("sword_silver", new ItemStack(RegistryManager.sword_silver))
				);
		researches.put(RegistryManager.sword_dawnstone.getRegistryName().toString(), 
				new ResearchBase("sword_dawnstone", new ItemStack(RegistryManager.sword_dawnstone))
				);

		researches.put(RegistryManager.axe_copper.getRegistryName().toString(), 
				new ResearchBase("axe_copper", new ItemStack(RegistryManager.axe_copper))
				);
		researches.put(RegistryManager.axe_lead.getRegistryName().toString(), 
				new ResearchBase("axe_lead", new ItemStack(RegistryManager.axe_lead))
				);
		researches.put(RegistryManager.axe_silver.getRegistryName().toString(), 
				new ResearchBase("axe_silver", new ItemStack(RegistryManager.axe_silver))
				);
		researches.put(RegistryManager.axe_dawnstone.getRegistryName().toString(), 
				new ResearchBase("axe_dawnstone", new ItemStack(RegistryManager.axe_dawnstone))
				);

		researches.put(RegistryManager.pickaxe_copper.getRegistryName().toString(), 
				new ResearchBase("pickaxe_copper", new ItemStack(RegistryManager.pickaxe_copper))
				);
		researches.put(RegistryManager.pickaxe_lead.getRegistryName().toString(), 
				new ResearchBase("pickaxe_lead", new ItemStack(RegistryManager.pickaxe_lead))
				);
		researches.put(RegistryManager.pickaxe_silver.getRegistryName().toString(), 
				new ResearchBase("pickaxe_silver", new ItemStack(RegistryManager.pickaxe_silver))
				);
		researches.put(RegistryManager.pickaxe_dawnstone.getRegistryName().toString(), 
				new ResearchBase("pickaxe_dawnstone", new ItemStack(RegistryManager.pickaxe_dawnstone))
				);
		
		researches.put(RegistryManager.blend_caminite.getRegistryName().toString(), 
				new ResearchBase("blend_caminite", new ItemStack(RegistryManager.blend_caminite))
				);
		researches.put(RegistryManager.brick_caminite.getRegistryName().toString(), 
				new ResearchBase("brick_caminite", new ItemStack(RegistryManager.brick_caminite))
				);
		researches.put(RegistryManager.plate_caminite_raw.getRegistryName().toString(), 
				new ResearchBase("plate_caminite_raw", new ItemStack(RegistryManager.plate_caminite_raw))
				);
		researches.put(RegistryManager.plate_caminite.getRegistryName().toString(), 
				new ResearchBase("plate_caminite", new ItemStack(RegistryManager.plate_caminite))
				);
		researches.put(RegistryManager.stamp_bar_raw.getRegistryName().toString(), 
				new ResearchBase("stamp_bar_raw", new ItemStack(RegistryManager.stamp_bar_raw))
				);
		researches.put(RegistryManager.stamp_bar.getRegistryName().toString(), 
				new ResearchBase("stamp_bar", new ItemStack(RegistryManager.stamp_bar))
				);
		researches.put(RegistryManager.stamp_plate_raw.getRegistryName().toString(), 
				new ResearchBase("stamp_plate_raw", new ItemStack(RegistryManager.stamp_plate_raw))
				);
		researches.put(RegistryManager.stamp_plate.getRegistryName().toString(), 
				new ResearchBase("stamp_plate", new ItemStack(RegistryManager.stamp_plate))
				);
		researches.put(RegistryManager.stamp_flat_raw.getRegistryName().toString(), 
				new ResearchBase("stamp_flat_raw", new ItemStack(RegistryManager.stamp_flat_raw))
				);
		researches.put(RegistryManager.stamp_flat.getRegistryName().toString(), 
				new ResearchBase("stamp_flat", new ItemStack(RegistryManager.stamp_flat))
				);
		researches.put(RegistryManager.block_caminite_brick.getRegistryName().toString(), 
				new ResearchBase("block_caminite_brick", new ItemStack(RegistryManager.block_caminite_brick))
				);
		researches.put(RegistryManager.stairs_caminite_brick.getRegistryName().toString(), 
				new ResearchBase("stairs_caminite_brick", new ItemStack(RegistryManager.stairs_caminite_brick))
				);
		researches.put(RegistryManager.block_caminite_brick_slab.getRegistryName().toString(), 
				new ResearchBase("block_caminite_brick_slab", new ItemStack(RegistryManager.block_caminite_brick_slab))
				);
		researches.put(RegistryManager.wall_caminite_brick.getRegistryName().toString(), 
				new ResearchBase("wall_caminite_brick", new ItemStack(RegistryManager.wall_caminite_brick))
				);
		researches.put(RegistryManager.ember_detector.getRegistryName().toString(), 
				new ResearchBase("ember_detector", new ItemStack(RegistryManager.ember_detector))
				);
		researches.put(RegistryManager.block_tank.getRegistryName().toString(), 
				new ResearchBase("block_tank", new ItemStack(RegistryManager.block_tank))
				);
		researches.put(RegistryManager.pipe.getRegistryName().toString(), 
				new ResearchBase("pipe", new ItemStack(RegistryManager.pipe))
				);
		researches.put(RegistryManager.pump.getRegistryName().toString(), 
				new ResearchBase("pump", new ItemStack(RegistryManager.pump))
				);
		researches.put(RegistryManager.block_furnace.getRegistryName().toString(), 
				new ResearchBase("block_furnace", new ItemStack(RegistryManager.block_furnace))
				);
		researches.put(RegistryManager.ember_receiver.getRegistryName().toString(), 
				new ResearchBase("ember_receiver", new ItemStack(RegistryManager.ember_receiver))
				);
		researches.put(RegistryManager.ember_emitter.getRegistryName().toString(), 
				new ResearchBase("ember_emitter", new ItemStack(RegistryManager.ember_emitter))
				);
		researches.put(RegistryManager.tinker_hammer.getRegistryName().toString(), 
				new ResearchBase("tinker_hammer", new ItemStack(RegistryManager.tinker_hammer))
				);
		researches.put(RegistryManager.copper_cell.getRegistryName().toString(), 
				new ResearchBase("copper_cell", new ItemStack(RegistryManager.copper_cell))
				);
		researches.put(RegistryManager.item_pipe.getRegistryName().toString(), 
				new ResearchBase("item_pipe", new ItemStack(RegistryManager.item_pipe))
				);
		researches.put(RegistryManager.item_pump.getRegistryName().toString(), 
				new ResearchBase("item_pump", new ItemStack(RegistryManager.item_pump))
				);
		researches.put(RegistryManager.bin.getRegistryName().toString(), 
				new ResearchBase("bin", new ItemStack(RegistryManager.bin))
				);
		researches.put(RegistryManager.stamper.getRegistryName().toString(), 
				new ResearchBase("stamper", new ItemStack(RegistryManager.stamper))
				);
		researches.put(RegistryManager.stamp_base.getRegistryName().toString(), 
				new ResearchBase("stamp_base", new ItemStack(RegistryManager.stamp_base))
				);
		researches.put(RegistryManager.ember_bore.getRegistryName().toString(), 
				new ResearchBase("ember_bore", new ItemStack(RegistryManager.ember_bore))
				);
		researches.put(RegistryManager.mech_accessor.getRegistryName().toString(), 
				new ResearchBase("mech_accessor", new ItemStack(RegistryManager.mech_accessor))
				);
		researches.put(RegistryManager.mech_core.getRegistryName().toString(), 
				new ResearchBase("mech_core", new ItemStack(RegistryManager.mech_core))
				);
		researches.put(RegistryManager.ember_activator.getRegistryName().toString(), 
				new ResearchBase("ember_activator", new ItemStack(RegistryManager.ember_activator))
				);
		researches.put(RegistryManager.mixer.getRegistryName().toString(), 
				new ResearchBase("mixer", new ItemStack(RegistryManager.mixer))
				);
		researches.put(RegistryManager.heat_coil.getRegistryName().toString(), 
				new ResearchBase("heat_coil", new ItemStack(RegistryManager.heat_coil))
				);
		researches.put(RegistryManager.item_dropper.getRegistryName().toString(), 
				new ResearchBase("item_dropper", new ItemStack(RegistryManager.item_dropper))
				);
		researches.put(RegistryManager.large_tank.getRegistryName().toString(), 
				new ResearchBase("large_tank", new ItemStack(RegistryManager.large_tank))
				);
		researches.put(RegistryManager.ember_gauge.getRegistryName().toString(), 
				new ResearchBase("ember_gauge", new ItemStack(RegistryManager.ember_gauge))
				);
		/*researches.put(RegistryManager.item_gauge.getRegistryName().toString(), 
				new ResearchBase("item_gauge", new ItemStack(RegistryManager.item_gauge))
				);*/
		researches.put(RegistryManager.fluid_gauge.getRegistryName().toString(), 
				new ResearchBase("fluid_gauge", new ItemStack(RegistryManager.fluid_gauge))
				);
		researches.put(RegistryManager.block_lantern.getRegistryName().toString(), 
				new ResearchBase("block_lantern", new ItemStack(RegistryManager.block_lantern))
				);
		researches.put(RegistryManager.beam_splitter.getRegistryName().toString(), 
				new ResearchBase("beam_splitter", new ItemStack(RegistryManager.beam_splitter))
				);
		researches.put(RegistryManager.ember_relay.getRegistryName().toString(), 
				new ResearchBase("ember_relay", new ItemStack(RegistryManager.ember_relay))
				);
		researches.put(RegistryManager.crystal_cell.getRegistryName().toString(), 
				new ResearchBase("crystal_cell", new ItemStack(RegistryManager.crystal_cell))
				);
		researches.put(RegistryManager.charger.getRegistryName().toString(), 
				new ResearchBase("charger", new ItemStack(RegistryManager.charger))
				);
		researches.put(RegistryManager.cinder_plinth.getRegistryName().toString(), 
				new ResearchBase("cinder_plinth", new ItemStack(RegistryManager.cinder_plinth))
				);
		researches.put(RegistryManager.stone_edge.getRegistryName().toString(), 
				new ResearchBase("stone_edge", new ItemStack(RegistryManager.stone_edge))
				);
		researches.put(RegistryManager.shard_ember.getRegistryName().toString(), 
				new ResearchBase("shard_ember", new ItemStack(RegistryManager.shard_ember))
				);
		researches.put(RegistryManager.crystal_ember.getRegistryName().toString(), 
				new ResearchBase("crystal_ember", new ItemStack(RegistryManager.crystal_ember))
				);
		researches.put(RegistryManager.ember_jar.getRegistryName().toString(), 
				new ResearchBase("ember_jar", new ItemStack(RegistryManager.ember_jar))
				);
		researches.put(RegistryManager.ember_cartridge.getRegistryName().toString(), 
				new ResearchBase("ember_cartridge", new ItemStack(RegistryManager.ember_cartridge))
				);
		researches.put(RegistryManager.pickaxe_clockwork.getRegistryName().toString(), 
				new ResearchBase("pickaxe_clockwork", new ItemStack(RegistryManager.pickaxe_clockwork))
				);
		researches.put(RegistryManager.axe_clockwork.getRegistryName().toString(), 
				new ResearchBase("axe_clockwork", new ItemStack(RegistryManager.axe_clockwork))
				);
		researches.put(RegistryManager.ignition_cannon.getRegistryName().toString(), 
				new ResearchBase("ignition_cannon", new ItemStack(RegistryManager.ignition_cannon))
				);
		researches.put(RegistryManager.staff_ember.getRegistryName().toString(), 
				new ResearchBase("staff_ember", new ItemStack(RegistryManager.staff_ember))
				);
		researches.put(RegistryManager.grandhammer.getRegistryName().toString(), 
				new ResearchBase("grandhammer", new ItemStack(RegistryManager.grandhammer))
				);
		researches.put(RegistryManager.dust_ash.getRegistryName().toString(), 
				new ResearchBase("dust_ash", new ItemStack(RegistryManager.dust_ash))
				);
		researches.put(RegistryManager.alchemy_pedestal.getRegistryName().toString(), 
				new ResearchBase("alchemy_pedestal", new ItemStack(RegistryManager.alchemy_pedestal))
				);
		researches.put(RegistryManager.alchemy_tablet.getRegistryName().toString(), 
				new ResearchBase("alchemy_tablet", new ItemStack(RegistryManager.alchemy_tablet))
				);
		researches.put(RegistryManager.item_transfer.getRegistryName().toString(), 
				new ResearchBase("item_transfer", new ItemStack(RegistryManager.item_transfer))
				);
		researches.put(RegistryManager.beam_cannon.getRegistryName().toString(), 
				new ResearchBase("beam_cannon", new ItemStack(RegistryManager.beam_cannon))
				);
		researches.put(RegistryManager.aspectus_iron.getRegistryName().toString(), 
				new ResearchBase("aspectus_iron", new ItemStack(RegistryManager.aspectus_iron))
				);
		researches.put(RegistryManager.aspectus_copper.getRegistryName().toString(), 
				new ResearchBase("aspectus_copper", new ItemStack(RegistryManager.aspectus_copper))
				);
		researches.put(RegistryManager.aspectus_lead.getRegistryName().toString(), 
				new ResearchBase("aspectus_lead", new ItemStack(RegistryManager.aspectus_lead))
				);
		researches.put(RegistryManager.aspectus_silver.getRegistryName().toString(), 
				new ResearchBase("aspectus_silver", new ItemStack(RegistryManager.aspectus_silver))
				);
		researches.put(RegistryManager.aspectus_dawnstone.getRegistryName().toString(), 
				new ResearchBase("aspectus_dawnstone", new ItemStack(RegistryManager.aspectus_dawnstone))
				);
		researches.put(RegistryManager.alchemic_waste.getRegistryName().toString(), 
				new ResearchBase("alchemic_waste", new ItemStack(RegistryManager.alchemic_waste))
				);
		researches.put(RegistryManager.ashen_cloth.getRegistryName().toString(), 
				new ResearchBase("ashen_cloth", new ItemStack(RegistryManager.ashen_cloth))
				);
		researches.put(RegistryManager.ashen_cloak_head.getRegistryName().toString(), 
				new ResearchBase("ashen_cloak_head", new ItemStack(RegistryManager.ashen_cloak_head))
				);
		researches.put(RegistryManager.ashen_cloak_chest.getRegistryName().toString(), 
				new ResearchBase("ashen_cloak_chest", new ItemStack(RegistryManager.ashen_cloak_chest))
				);
		researches.put(RegistryManager.ashen_cloak_legs.getRegistryName().toString(), 
				new ResearchBase("ashen_cloak_legs", new ItemStack(RegistryManager.ashen_cloak_legs))
				);
		researches.put(RegistryManager.ashen_cloak_boots.getRegistryName().toString(), 
				new ResearchBase("ashen_cloak_boots", new ItemStack(RegistryManager.ashen_cloak_boots))
				);
		researches.put(RegistryManager.inflictor_gem.getRegistryName().toString(), 
				new ResearchBase("inflictor_gem", new ItemStack(RegistryManager.inflictor_gem))
				);
	}
}
