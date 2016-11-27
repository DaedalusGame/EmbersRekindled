package teamroots.embers.research;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import teamroots.embers.RegistryManager;

public class ResearchManager {
	public static Map<String, ResearchBase> researches = new HashMap<String, ResearchBase>();
	
	public static void initResearches(){
		researches.put(RegistryManager.oreCopper.getRegistryName().toString(), 
				new ResearchBase("oreCopper", new ItemStack(RegistryManager.oreCopper))
				);
		researches.put(RegistryManager.oreLead.getRegistryName().toString(), 
				new ResearchBase("oreLead", new ItemStack(RegistryManager.oreLead))
				);
		researches.put(RegistryManager.oreSilver.getRegistryName().toString(), 
				new ResearchBase("oreSilver", new ItemStack(RegistryManager.oreSilver))
				);
		
		researches.put(RegistryManager.ingotCopper.getRegistryName().toString(), 
				new ResearchBase("ingotCopper", new ItemStack(RegistryManager.ingotCopper))
				);
		researches.put(RegistryManager.ingotLead.getRegistryName().toString(), 
				new ResearchBase("ingotLead", new ItemStack(RegistryManager.ingotLead))
				);
		researches.put(RegistryManager.ingotSilver.getRegistryName().toString(), 
				new ResearchBase("ingotSilver", new ItemStack(RegistryManager.ingotSilver))
				);
		researches.put(RegistryManager.ingotDawnstone.getRegistryName().toString(), 
				new ResearchBase("ingotDawnstone", new ItemStack(RegistryManager.ingotDawnstone))
				);
		
		researches.put(RegistryManager.nuggetCopper.getRegistryName().toString(), 
				new ResearchBase("nuggetCopper", new ItemStack(RegistryManager.nuggetCopper))
				);
		researches.put(RegistryManager.nuggetLead.getRegistryName().toString(), 
				new ResearchBase("nuggetLead", new ItemStack(RegistryManager.nuggetLead))
				);
		researches.put(RegistryManager.nuggetSilver.getRegistryName().toString(), 
				new ResearchBase("nuggetSilver", new ItemStack(RegistryManager.nuggetSilver))
				);
		researches.put(RegistryManager.nuggetDawnstone.getRegistryName().toString(), 
				new ResearchBase("nuggetDawnstone", new ItemStack(RegistryManager.nuggetDawnstone))
				);
		researches.put(RegistryManager.nuggetIron.getRegistryName().toString(), 
				new ResearchBase("nuggetIron", new ItemStack(RegistryManager.nuggetIron))
				);
		
		researches.put(RegistryManager.plateCopper.getRegistryName().toString(), 
				new ResearchBase("plateCopper", new ItemStack(RegistryManager.plateCopper))
				);
		researches.put(RegistryManager.plateLead.getRegistryName().toString(), 
				new ResearchBase("plateLead", new ItemStack(RegistryManager.plateLead))
				);
		researches.put(RegistryManager.plateSilver.getRegistryName().toString(), 
				new ResearchBase("plateSilver", new ItemStack(RegistryManager.plateSilver))
				);
		researches.put(RegistryManager.plateDawnstone.getRegistryName().toString(), 
				new ResearchBase("plateDawnstone", new ItemStack(RegistryManager.plateDawnstone))
				);
		researches.put(RegistryManager.plateIron.getRegistryName().toString(), 
				new ResearchBase("plateIron", new ItemStack(RegistryManager.plateIron))
				);
		researches.put(RegistryManager.plateGold.getRegistryName().toString(), 
				new ResearchBase("plateGold", new ItemStack(RegistryManager.plateGold))
				);
		
		researches.put(RegistryManager.blockCopper.getRegistryName().toString(), 
				new ResearchBase("blockCopper", new ItemStack(RegistryManager.blockCopper))
				);
		researches.put(RegistryManager.blockLead.getRegistryName().toString(), 
				new ResearchBase("blockLead", new ItemStack(RegistryManager.blockLead))
				);
		researches.put(RegistryManager.blockSilver.getRegistryName().toString(), 
				new ResearchBase("blockSilver", new ItemStack(RegistryManager.blockSilver))
				);
		researches.put(RegistryManager.blockDawnstone.getRegistryName().toString(), 
				new ResearchBase("blockDawnstone", new ItemStack(RegistryManager.blockDawnstone))
				);

		researches.put(RegistryManager.shovelCopper.getRegistryName().toString(), 
				new ResearchBase("shovelCopper", new ItemStack(RegistryManager.shovelCopper))
				);
		researches.put(RegistryManager.shovelLead.getRegistryName().toString(), 
				new ResearchBase("shovelLead", new ItemStack(RegistryManager.shovelLead))
				);
		researches.put(RegistryManager.shovelSilver.getRegistryName().toString(), 
				new ResearchBase("shovelSilver", new ItemStack(RegistryManager.shovelSilver))
				);
		researches.put(RegistryManager.shovelDawnstone.getRegistryName().toString(), 
				new ResearchBase("shovelDawnstone", new ItemStack(RegistryManager.shovelDawnstone))
				);

		researches.put(RegistryManager.hoeCopper.getRegistryName().toString(), 
				new ResearchBase("hoeCopper", new ItemStack(RegistryManager.hoeCopper))
				);
		researches.put(RegistryManager.hoeLead.getRegistryName().toString(), 
				new ResearchBase("hoeLead", new ItemStack(RegistryManager.hoeLead))
				);
		researches.put(RegistryManager.hoeSilver.getRegistryName().toString(), 
				new ResearchBase("hoeSilver", new ItemStack(RegistryManager.hoeSilver))
				);
		researches.put(RegistryManager.hoeDawnstone.getRegistryName().toString(), 
				new ResearchBase("hoeDawnstone", new ItemStack(RegistryManager.hoeDawnstone))
				);

		researches.put(RegistryManager.swordCopper.getRegistryName().toString(), 
				new ResearchBase("swordCopper", new ItemStack(RegistryManager.swordCopper))
				);
		researches.put(RegistryManager.swordLead.getRegistryName().toString(), 
				new ResearchBase("swordLead", new ItemStack(RegistryManager.swordLead))
				);
		researches.put(RegistryManager.swordSilver.getRegistryName().toString(), 
				new ResearchBase("swordSilver", new ItemStack(RegistryManager.swordSilver))
				);
		researches.put(RegistryManager.swordDawnstone.getRegistryName().toString(), 
				new ResearchBase("swordDawnstone", new ItemStack(RegistryManager.swordDawnstone))
				);

		researches.put(RegistryManager.axeCopper.getRegistryName().toString(), 
				new ResearchBase("axeCopper", new ItemStack(RegistryManager.axeCopper))
				);
		researches.put(RegistryManager.axeLead.getRegistryName().toString(), 
				new ResearchBase("axeLead", new ItemStack(RegistryManager.axeLead))
				);
		researches.put(RegistryManager.axeSilver.getRegistryName().toString(), 
				new ResearchBase("axeSilver", new ItemStack(RegistryManager.axeSilver))
				);
		researches.put(RegistryManager.axeDawnstone.getRegistryName().toString(), 
				new ResearchBase("axeDawnstone", new ItemStack(RegistryManager.axeDawnstone))
				);

		researches.put(RegistryManager.pickaxeCopper.getRegistryName().toString(), 
				new ResearchBase("pickaxeCopper", new ItemStack(RegistryManager.pickaxeCopper))
				);
		researches.put(RegistryManager.pickaxeLead.getRegistryName().toString(), 
				new ResearchBase("pickaxeLead", new ItemStack(RegistryManager.pickaxeLead))
				);
		researches.put(RegistryManager.pickaxeSilver.getRegistryName().toString(), 
				new ResearchBase("pickaxeSilver", new ItemStack(RegistryManager.pickaxeSilver))
				);
		researches.put(RegistryManager.pickaxeDawnstone.getRegistryName().toString(), 
				new ResearchBase("pickaxeDawnstone", new ItemStack(RegistryManager.pickaxeDawnstone))
				);
		
		researches.put(RegistryManager.blendCaminite.getRegistryName().toString(), 
				new ResearchBase("blendCaminite", new ItemStack(RegistryManager.blendCaminite))
				);
		researches.put(RegistryManager.brickCaminite.getRegistryName().toString(), 
				new ResearchBase("brickCaminite", new ItemStack(RegistryManager.brickCaminite))
				);
		researches.put(RegistryManager.plateCaminiteRaw.getRegistryName().toString(), 
				new ResearchBase("plateCaminiteRaw", new ItemStack(RegistryManager.plateCaminiteRaw))
				);
		researches.put(RegistryManager.plateCaminite.getRegistryName().toString(), 
				new ResearchBase("plateCaminite", new ItemStack(RegistryManager.plateCaminite))
				);
		researches.put(RegistryManager.stampBarRaw.getRegistryName().toString(), 
				new ResearchBase("stampBarRaw", new ItemStack(RegistryManager.stampBarRaw))
				);
		researches.put(RegistryManager.stampBar.getRegistryName().toString(), 
				new ResearchBase("stampBar", new ItemStack(RegistryManager.stampBar))
				);
		researches.put(RegistryManager.stampPlateRaw.getRegistryName().toString(), 
				new ResearchBase("stampPlateRaw", new ItemStack(RegistryManager.stampPlateRaw))
				);
		researches.put(RegistryManager.stampPlate.getRegistryName().toString(), 
				new ResearchBase("stampPlate", new ItemStack(RegistryManager.stampPlate))
				);
		researches.put(RegistryManager.stampFlatRaw.getRegistryName().toString(), 
				new ResearchBase("stampFlatRaw", new ItemStack(RegistryManager.stampFlatRaw))
				);
		researches.put(RegistryManager.stampFlat.getRegistryName().toString(), 
				new ResearchBase("stampFlat", new ItemStack(RegistryManager.stampFlat))
				);
		researches.put(RegistryManager.blockCaminiteBrick.getRegistryName().toString(), 
				new ResearchBase("blockCaminiteBrick", new ItemStack(RegistryManager.blockCaminiteBrick))
				);
		researches.put(RegistryManager.stairsCaminiteBrick.getRegistryName().toString(), 
				new ResearchBase("stairsCaminiteBrick", new ItemStack(RegistryManager.stairsCaminiteBrick))
				);
		researches.put(RegistryManager.blockCaminiteBrickSlab.getRegistryName().toString(), 
				new ResearchBase("blockCaminiteBrickSlab", new ItemStack(RegistryManager.blockCaminiteBrickSlab))
				);
		researches.put(RegistryManager.wallCaminiteBrick.getRegistryName().toString(), 
				new ResearchBase("wallCaminiteBrick", new ItemStack(RegistryManager.wallCaminiteBrick))
				);
		researches.put(RegistryManager.emberDetector.getRegistryName().toString(), 
				new ResearchBase("emberDetector", new ItemStack(RegistryManager.emberDetector))
				);
		researches.put(RegistryManager.blockTank.getRegistryName().toString(), 
				new ResearchBase("blockTank", new ItemStack(RegistryManager.blockTank))
				);
		researches.put(RegistryManager.pipe.getRegistryName().toString(), 
				new ResearchBase("pipe", new ItemStack(RegistryManager.pipe))
				);
		researches.put(RegistryManager.pump.getRegistryName().toString(), 
				new ResearchBase("pump", new ItemStack(RegistryManager.pump))
				);
		researches.put(RegistryManager.blockFurnace.getRegistryName().toString(), 
				new ResearchBase("blockFurnace", new ItemStack(RegistryManager.blockFurnace))
				);
		researches.put(RegistryManager.emberReceiver.getRegistryName().toString(), 
				new ResearchBase("emberReceiver", new ItemStack(RegistryManager.emberReceiver))
				);
		researches.put(RegistryManager.emberEmitter.getRegistryName().toString(), 
				new ResearchBase("emberEmitter", new ItemStack(RegistryManager.emberEmitter))
				);
		researches.put(RegistryManager.tinkerHammer.getRegistryName().toString(), 
				new ResearchBase("tinkerHammer", new ItemStack(RegistryManager.tinkerHammer))
				);
		researches.put(RegistryManager.copperCell.getRegistryName().toString(), 
				new ResearchBase("copperCell", new ItemStack(RegistryManager.copperCell))
				);
		researches.put(RegistryManager.itemPipe.getRegistryName().toString(), 
				new ResearchBase("itemPipe", new ItemStack(RegistryManager.itemPipe))
				);
		researches.put(RegistryManager.itemPump.getRegistryName().toString(), 
				new ResearchBase("itemPump", new ItemStack(RegistryManager.itemPump))
				);
		researches.put(RegistryManager.bin.getRegistryName().toString(), 
				new ResearchBase("bin", new ItemStack(RegistryManager.bin))
				);
		researches.put(RegistryManager.stamper.getRegistryName().toString(), 
				new ResearchBase("stamper", new ItemStack(RegistryManager.stamper))
				);
		researches.put(RegistryManager.stampBase.getRegistryName().toString(), 
				new ResearchBase("stampBase", new ItemStack(RegistryManager.stampBase))
				);
		researches.put(RegistryManager.emberBore.getRegistryName().toString(), 
				new ResearchBase("emberBore", new ItemStack(RegistryManager.emberBore))
				);
		researches.put(RegistryManager.mechAccessor.getRegistryName().toString(), 
				new ResearchBase("mechAccessor", new ItemStack(RegistryManager.mechAccessor))
				);
		researches.put(RegistryManager.mechCore.getRegistryName().toString(), 
				new ResearchBase("mechCore", new ItemStack(RegistryManager.mechCore))
				);
		researches.put(RegistryManager.emberActivator.getRegistryName().toString(), 
				new ResearchBase("emberActivator", new ItemStack(RegistryManager.emberActivator))
				);
		researches.put(RegistryManager.mixer.getRegistryName().toString(), 
				new ResearchBase("mixer", new ItemStack(RegistryManager.mixer))
				);
		researches.put(RegistryManager.heatCoil.getRegistryName().toString(), 
				new ResearchBase("heatCoil", new ItemStack(RegistryManager.heatCoil))
				);
		researches.put(RegistryManager.itemDropper.getRegistryName().toString(), 
				new ResearchBase("itemDropper", new ItemStack(RegistryManager.itemDropper))
				);
		researches.put(RegistryManager.largeTank.getRegistryName().toString(), 
				new ResearchBase("largeTank", new ItemStack(RegistryManager.largeTank))
				);
		researches.put(RegistryManager.emberGauge.getRegistryName().toString(), 
				new ResearchBase("emberGauge", new ItemStack(RegistryManager.emberGauge))
				);
		researches.put(RegistryManager.itemGauge.getRegistryName().toString(), 
				new ResearchBase("itemGauge", new ItemStack(RegistryManager.itemGauge))
				);
		researches.put(RegistryManager.fluidGauge.getRegistryName().toString(), 
				new ResearchBase("fluidGauge", new ItemStack(RegistryManager.fluidGauge))
				);
		researches.put(RegistryManager.blockLantern.getRegistryName().toString(), 
				new ResearchBase("blockLantern", new ItemStack(RegistryManager.blockLantern))
				);
		researches.put(RegistryManager.beamSplitter.getRegistryName().toString(), 
				new ResearchBase("beamSplitter", new ItemStack(RegistryManager.beamSplitter))
				);
		researches.put(RegistryManager.emberRelay.getRegistryName().toString(), 
				new ResearchBase("emberRelay", new ItemStack(RegistryManager.emberRelay))
				);
		researches.put(RegistryManager.crystalCell.getRegistryName().toString(), 
				new ResearchBase("crystalCell", new ItemStack(RegistryManager.crystalCell))
				);
		researches.put(RegistryManager.charger.getRegistryName().toString(), 
				new ResearchBase("charger", new ItemStack(RegistryManager.charger))
				);
		researches.put(RegistryManager.cinderPlinth.getRegistryName().toString(), 
				new ResearchBase("cinderPlinth", new ItemStack(RegistryManager.cinderPlinth))
				);
		researches.put(RegistryManager.stoneEdge.getRegistryName().toString(), 
				new ResearchBase("stoneEdge", new ItemStack(RegistryManager.stoneEdge))
				);
		researches.put(RegistryManager.shardEmber.getRegistryName().toString(), 
				new ResearchBase("shardEmber", new ItemStack(RegistryManager.shardEmber))
				);
		researches.put(RegistryManager.crystalEmber.getRegistryName().toString(), 
				new ResearchBase("crystalEmber", new ItemStack(RegistryManager.crystalEmber))
				);
		researches.put(RegistryManager.emberJar.getRegistryName().toString(), 
				new ResearchBase("emberJar", new ItemStack(RegistryManager.emberJar))
				);
		researches.put(RegistryManager.emberCartridge.getRegistryName().toString(), 
				new ResearchBase("emberCartridge", new ItemStack(RegistryManager.emberCartridge))
				);
		researches.put(RegistryManager.pickaxeClockwork.getRegistryName().toString(), 
				new ResearchBase("pickaxeClockwork", new ItemStack(RegistryManager.pickaxeClockwork))
				);
		researches.put(RegistryManager.axeClockwork.getRegistryName().toString(), 
				new ResearchBase("axeClockwork", new ItemStack(RegistryManager.axeClockwork))
				);
		researches.put(RegistryManager.ignitionCannon.getRegistryName().toString(), 
				new ResearchBase("ignitionCannon", new ItemStack(RegistryManager.ignitionCannon))
				);
		researches.put(RegistryManager.staffEmber.getRegistryName().toString(), 
				new ResearchBase("staffEmber", new ItemStack(RegistryManager.staffEmber))
				);
		researches.put(RegistryManager.grandhammer.getRegistryName().toString(), 
				new ResearchBase("grandhammer", new ItemStack(RegistryManager.grandhammer))
				);
		researches.put(RegistryManager.dustAsh.getRegistryName().toString(), 
				new ResearchBase("dustAsh", new ItemStack(RegistryManager.dustAsh))
				);
	}
}
