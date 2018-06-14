package teamroots.embers.research;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import teamroots.embers.RegistryManager;
import teamroots.embers.item.IEmberItem;

public class ResearchManager {
	public static List<ResearchCategory> researches = new ArrayList<ResearchCategory>();
	
	public static ResearchBase dials, boiler, ores, hammer, ancient_golem, gauge, caminite, bore, crystals, activator, pipes, tank, bin,//WORLD
							   copper_cell, emitters, dawnstone, dropper, melter, stamper, mixer, breaker, hearth_coil, access, reservoir, vacuum, transfer, //MECHANISMS
							   ember_ejector, beam_cannon, pulser, splitter, dawnstone_anvil, autohammer, crystal_cell, cinder_staff, clockwork_tools, blazing_ray, charger, jars, alchemy, cinder_plinth, aspecti, //METALLURGY
							   tyrfing, waste, wildfire, cluster, combustor, catalyzer, reactor, injector, ashen_cloak, inflictor, materia, misc_alchemy, adhesive, field_chart, //ALCHEMY
							   modifiers, inferno_forge, heat, superheater, cinder_jet, caster_orb, resonating_bell, eldritch_insignia, blasting_core, intelligent_apparatus, flame_barrier //SMITHING
	;

	public static void initResearches(){
		//WORLD
		ores = new ResearchBase("ores", new ItemStack(RegistryManager.ore_copper), 0, 7);
		hammer = new ResearchBase("hammer", new ItemStack(RegistryManager.tinker_hammer), 0, 3).addAncestor(ores);
		ancient_golem = new ResearchBase("ancient_golem", new ItemStack(RegistryManager.ancient_motive_core), 0, 0);
		gauge = new ResearchBase("gauge", new ItemStack(RegistryManager.ember_detector), 4, 3).addAncestor(ores);
		caminite = new ResearchBase("caminite", new ItemStack(RegistryManager.brick_caminite), 6, 7);
		tank = new ResearchBase("tank", new ItemStack(RegistryManager.block_tank), 3, 7).addAncestor(caminite);
		bore = new ResearchBase("bore", new ItemStack(RegistryManager.ember_bore), 9, 0).addAncestor(hammer).addAncestor(caminite);
		crystals = new ResearchBase("crystals", new ItemStack(RegistryManager.crystal_ember), 12,3).addAncestor(bore);
		activator = new ResearchBase("activator", new ItemStack(RegistryManager.ember_activator), 9, 5).addAncestor(crystals);
		boiler = new ResearchBase("boiler", new ItemStack(RegistryManager.boiler), 9, 7).addAncestor(activator);
		pipes = new ResearchBase("pipes", new ItemStack(RegistryManager.pump), 3, 0).addAncestor(ores);
		bin = new ResearchBase("bin", new ItemStack(RegistryManager.bin), 12, 7);
		dials = new ResearchBase("dials", new ItemStack(RegistryManager.ember_gauge), 5, 5).addAncestor(ores);
		
		//MECHANISMS
		emitters = new ResearchBase("emitters", new ItemStack(RegistryManager.ember_emitter), 0, 2);
		melter = new ResearchBase("melter", new ItemStack(RegistryManager.block_furnace), 2, 0).addAncestor(emitters);
		stamper = new ResearchBase("stamper", new ItemStack(RegistryManager.stamper), 2, 4).addAncestor(melter).addAncestor(emitters);
		access = new ResearchBase("access",new ItemStack(RegistryManager.mech_core),7,5).addAncestor(stamper);
		hearth_coil = new ResearchBase("hearth_coil",new ItemStack(RegistryManager.heat_coil),10,1).addAncestor(access);
		mixer = new ResearchBase("mixer",new ItemStack(RegistryManager.mixer),5,2).addAncestor(stamper).addAncestor(melter);
		reservoir = new ResearchBase("reservoir",new ItemStack(RegistryManager.large_tank),8,0).addAncestor(access);
		transfer = new ResearchBase("transfer", new ItemStack(RegistryManager.item_transfer),0,7);
		vacuum = new ResearchBase("vacuum", new ItemStack(RegistryManager.vacuum),8,7);
		breaker = new ResearchBase("breaker", new ItemStack(RegistryManager.breaker),4,7).addAncestor(stamper);
		dropper = new ResearchBase("dropper", new ItemStack(RegistryManager.item_dropper),12,7);
		dawnstone = new ResearchBase("dawnstone", new ItemStack(RegistryManager.ingot_dawnstone), 11, 4).addAncestor(mixer);
		copper_cell = new ResearchBase("copper_cell", new ItemStack(RegistryManager.copper_cell), 0, 5).addAncestor(emitters);
		
		//METALLURGY
		ember_ejector = new ResearchBase("ember_ejector", new ItemStack(RegistryManager.ember_pulser), 0, 3.5);
		dawnstone_anvil = new ResearchBase("dawnstone_anvil", new ItemStack(RegistryManager.dawnstone_anvil), 3, 7);
		autohammer = new ResearchBase("autohammer", new ItemStack(RegistryManager.auto_hammer), 7, 7).addAncestor(dawnstone_anvil);
		crystal_cell = new ResearchBase("crystal_cell", new ItemStack(RegistryManager.crystal_cell), 0, 1);
		charger = new ResearchBase("charger", new ItemStack(RegistryManager.charger), 3, 0);
		ItemStack fullJar = new ItemStack(RegistryManager.ember_jar);
		fullJar.setTagCompound(new NBTTagCompound());
		((IEmberItem)RegistryManager.ember_jar).setEmberCapacity(fullJar, 1000.0);
		((IEmberItem)RegistryManager.ember_jar).setEmber(fullJar, ((IEmberItem)RegistryManager.ember_jar).getEmberCapacity(fullJar));
		jars = new ResearchBase("jars", fullJar, 6, 1).addAncestor(charger);
		clockwork_tools = new ResearchBase("clockwork_tools", new ItemStack(RegistryManager.axe_clockwork), 2, 2).addAncestor(jars);
		pulser = new ResearchBase("pulser", new ItemStack(RegistryManager.ember_pulser), 2, 5).addAncestor(crystal_cell);
		splitter = new ResearchBase("splitter", new ItemStack(RegistryManager.beam_splitter), 0, 6).addAncestor(pulser);
		cinder_staff = new ResearchBase("cinder_staff", new ItemStack(RegistryManager.staff_ember), 5, 5).addAncestor(jars);
		blazing_ray = new ResearchBase("blazing_ray", new ItemStack(RegistryManager.ignition_cannon), 8, 4).addAncestor(jars);
		aspecti = new ResearchBase("aspecti", new ItemStack(RegistryManager.aspectus_dawnstone), 12, 1);
		cinder_plinth = new ResearchBase("cinder_plinth", new ItemStack(RegistryManager.cinder_plinth), 9, 0);
		beam_cannon = new ResearchBase("beam_cannon", new ItemStack(RegistryManager.beam_cannon), 12, 7);
		alchemy = new ResearchBase("alchemy", new ItemStack(RegistryManager.alchemy_tablet), 9, 6).addAncestor(cinder_plinth).addAncestor(aspecti).addAncestor(beam_cannon);
		
		//TRANSMUTATION
		waste = new ResearchBase("waste", new ItemStack(RegistryManager.alchemic_waste), 6, 0);
		misc_alchemy = new ResearchBase("misc_alchemy", new ItemStack(Blocks.NETHERRACK), 0, 1).addAncestor(waste);
		materia = new ResearchBase("materia", new ItemStack(RegistryManager.isolated_materia), 6, 3).addAncestor(waste);
		adhesive = new ResearchBase("adhesive", new ItemStack(RegistryManager.adhesive), 12, 1).addAncestor(waste);
		cluster = new ResearchBase("cluster", new ItemStack(RegistryManager.ember_cluster), 3, 2).addAncestor(waste);
		ashen_cloak = new ResearchBase("ashen_cloak", new ItemStack(RegistryManager.ashen_cloak_chest), 9, 2).addAncestor(waste);
		field_chart = new ResearchBase("field_chart", new ItemStack(RegistryManager.field_chart), 0, 3).addAncestor(cluster);
		wildfire = new ResearchBase("wildfire", new ItemStack(RegistryManager.wildfire_core), 1, 5).addAncestor(cluster);
		inflictor = new ResearchBase("inflictor", new ItemStack(RegistryManager.inflictor_gem), 11, 5).addAncestor(ashen_cloak);
		injector = new ResearchBase("injector", new ItemStack(RegistryManager.ember_injector), 0, 7).addAncestor(wildfire);
		combustor = new ResearchBase("combustor", new ItemStack(RegistryManager.combustor), 6, 5).addAncestor(wildfire);
		catalyzer = new ResearchBase("catalyzer", new ItemStack(RegistryManager.catalyzer), 5, 7).addAncestor(wildfire);
		reactor = new ResearchBase("reactor", new ItemStack(RegistryManager.reactor), 9, 7).addAncestor(combustor).addAncestor(catalyzer);
		tyrfing = new ResearchBase("tyrfing", new ItemStack(RegistryManager.tyrfing), 8, 4).addAncestor(waste);
		
		//SMITHING
		modifiers = new ResearchBase("modifiers", new ItemStack(RegistryManager.ancient_motive_core), 5, 7);
		heat = new ResearchBase("heat", new ItemStack(RegistryManager.crystal_ember), 7, 7);
		inferno_forge = new ResearchBase("inferno_forge", new ItemStack(RegistryManager.inferno_forge), 6, 4).addAncestor(heat);
		superheater = new ResearchBase("superheater", new ItemStack(RegistryManager.superheater), 1, 7).addAncestor(inferno_forge);
		cinder_jet = new ResearchBase("cinder_jet", new ItemStack(RegistryManager.jet_augment), 0, 5).addAncestor(inferno_forge);
		blasting_core = new ResearchBase("blasting_core", new ItemStack(RegistryManager.blasting_core), 0, 3).addAncestor(inferno_forge);
		caster_orb = new ResearchBase("caster_orb", new ItemStack(RegistryManager.caster_orb), 1, 1).addAncestor(inferno_forge);
		flame_barrier = new ResearchBase("flame_barrier", new ItemStack(RegistryManager.flame_barrier), 11, 1).addAncestor(inferno_forge);
		eldritch_insignia = new ResearchBase("eldritch_insignia", new ItemStack(RegistryManager.eldritch_insignia), 12, 3).addAncestor(inferno_forge);
		intelligent_apparatus = new ResearchBase("intelligent_apparatus", new ItemStack(RegistryManager.intelligent_apparatus), 12, 5).addAncestor(inferno_forge);
		resonating_bell = new ResearchBase("resonating_bell", new ItemStack(RegistryManager.resonating_bell), 11, 7).addAncestor(inferno_forge);
		
		researches.add(new ResearchCategory("world", 16)
				.addResearch(ores)
				.addResearch(hammer)
				.addResearch(ancient_golem)
				.addResearch(gauge)
				.addResearch(caminite)
				.addResearch(bore)
				.addResearch(tank)
				.addResearch(pipes)
				.addResearch(bin)
				.addResearch(crystals)
				.addResearch(activator)
				.addResearch(boiler)
				.addResearch(dials));
		researches.add(new ResearchCategory("mechanisms", 32)
				.addResearch(melter)
				.addResearch(stamper)
				.addResearch(hearth_coil)
				.addResearch(mixer)
				.addResearch(access)
				.addResearch(reservoir)
				.addResearch(transfer)
				.addResearch(vacuum)
				.addResearch(dropper)
				.addResearch(breaker)
				.addResearch(dawnstone)
				.addResearch(emitters)
				.addResearch(copper_cell));
		researches.add(new ResearchCategory("metallurgy", 48)
				.addResearch(splitter)
				.addResearch(pulser)
				.addResearch(dawnstone_anvil)
				.addResearch(autohammer)
				.addResearch(crystal_cell)
				.addResearch(charger)
				.addResearch(jars)
				.addResearch(clockwork_tools)
				.addResearch(cinder_staff)
				.addResearch(blazing_ray)
				.addResearch(cinder_plinth)
				.addResearch(aspecti)
				.addResearch(alchemy)
				.addResearch(beam_cannon));
		researches.add(new ResearchCategory("alchemy", 64)
				.addResearch(waste)
				.addResearch(misc_alchemy)
				.addResearch(adhesive)
				.addResearch(cluster)
				.addResearch(ashen_cloak)
				.addResearch(inflictor)
				.addResearch(field_chart)
				.addResearch(wildfire)
				.addResearch(injector)
				.addResearch(reactor)
				.addResearch(combustor)
				.addResearch(catalyzer)
				.addResearch(materia)
				.addResearch(tyrfing));
		researches.add(new ResearchCategory("smithing", 80)
				.addResearch(modifiers)
				.addResearch(heat)
				.addResearch(inferno_forge)
				.addResearch(superheater)
				.addResearch(cinder_jet)
				.addResearch(blasting_core)
				.addResearch(caster_orb)
				.addResearch(eldritch_insignia)
				.addResearch(intelligent_apparatus)
				.addResearch(resonating_bell)
				.addResearch(flame_barrier));
		//researches.add(new ResearchCategory("materia", 80));
		//researches.add(new ResearchCategory("core", 96));
	}
}
