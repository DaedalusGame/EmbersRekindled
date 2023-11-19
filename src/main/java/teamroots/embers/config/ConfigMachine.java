package teamroots.embers.config;

import net.minecraftforge.common.config.Config;
import teamroots.embers.Embers;

@Config(modid = Embers.MODID, category = "machine", name = Embers.CFG_FOLDER + "machine")
@Config.LangKey("cfg.embers.machine")
public class ConfigMachine {

    @Config.Name("Ember Conduit")
    @Config.LangKey("cfg.embers.machine.ember_conduit")
    @Config.Comment("Options about the Ember Conduit")
    public static final EmberConduitCategory EMBER_CONDUIT_CATEGORY = new EmberConduitCategory();

    @Config.Name("Melter")
    @Config.LangKey("cfg.embers.machine.melter")
    @Config.Comment("Options about the Melter")
    public static final MelterCategory MELTER_CATEGORY = new MelterCategory();

    @Config.Name("Stamper")
    @Config.LangKey("cfg.embers.machine.stamper")
    @Config.Comment("Options about the Stamper")
    public static final StamperCategory STAMPER_CATEGORY = new StamperCategory();

    @Config.Name("Inferno Forge")
    @Config.LangKey("cfg.embers.machine.infernoforge")
    @Config.Comment("Options about the Inferno Forge")
    public static final InfernoForgeCategory INFERNO_FORGE_CATEGORY = new InfernoForgeCategory();

    @Config.Name("Geo Separator")
    @Config.LangKey("cfg.embers.machine.geoseparator")
    @Config.Comment("Options about the Geo Separator")
    public static final GeoSeparatorCategory GEO_SEPARATOR_CATEGORY = new GeoSeparatorCategory();

    @Config.Name("Dawnstone Anvil")
    @Config.LangKey("cfg.embers.machine.dawnstoneanvil")
    @Config.Comment("Options about the Dawnstone Anvil")
    public static final DawnStoneAnvilCategory DAWN_STONE_ANVIL_CATEGORY = new DawnStoneAnvilCategory();


    @Config.RequiresMcRestart
    @Config.Name("Ingot to Fluid ratio")
    @Config.Comment("What is the liquid equivalent to an ingot in mb?")
    public static int ingotFluidAmount = 144;

    @Config.RequiresMcRestart
    @Config.Name("Nugget to Fluid ratio")
    @Config.Comment("What is the liquid equivalent to an nugget in mb?")
    public static int nuggetFluidAmount = 16;

    public static class EmberConduitCategory {
        @Config.RequiresMcRestart
        @Config.Name("All machines connect to Ember Conduits")
        @Config.Comment("Whether ember conduits can attach to any ember consumer/producer")
        public boolean allAcceptVolatile = false;
    }

    public static class MelterCategory {
        @Config.RequiresMcRestart
        @Config.Name("Ore Melting Amount")
        @Config.Comment({
                "How many mb of fluid are obtained per ore output in the melter?",
                "This is multiplied by the amount of output a melter would produce, so by default 288mb = 2 ingots."
        })
        public int melterOreAmount = 288;

        @Config.RequiresMcRestart
        @Config.Name("Processing Time")
        @Config.Comment("The time in ticks it takes to process one recipe.")
        public int processTime = 200;

        @Config.RequiresMcRestart
        @Config.Name("Ember Cost Multiplier")
        @Config.Comment("The ember cost per tick.")
        public double emberCost = 1.0;
    }

    public static class StamperCategory {
        @Config.RequiresMcRestart
        @Config.Name("Ingot to Plate ratio")
        @Config.Comment("How many ingots are required to make one plate in the stamper?")
        public int stampPlateAmount = 1;

        @Config.RequiresMcRestart
        @Config.Name("Ingot to Aspectus ratio")
        @Config.Comment("How many ingots are required to make one aspectus in the stamper?")
        public int stampAspectusAmount = 1;

        @Config.RequiresMcRestart
        @Config.Name("Ingot to Gear ratio")
        @Config.Comment("How many ingots are required to make one gear in the stamper?")
        public int stampGearAmount = 2;

        @Config.RequiresMcRestart
        @Config.Name("Stamper Capacity")
        @Config.Comment("How much fluid (in mb) fits into the Stamp Base?")
        public int capacity = 1500;
    }

    public static class InfernoForgeCategory {
        @Config.RequiresMcRestart
        @Config.Name("Process Time")
        @Config.Comment("The time in ticks it takes to process one item.")
        public int processTime = 200;
        @Config.RequiresMcRestart
        @Config.Name("Ember Cost")
        @Config.Comment("The ember cost per tick.")
        public double emberCost = 16.0;
        @Config.RequiresMcRestart
        @Config.Name("Max Level")
        @Config.Comment("The maximum augment level that can be reforged to.")
        public int maxLevel = 5;
        @Config.RequiresMcRestart
        @Config.Name("Max Crystal Value")
        @Config.Comment("The maximum amount of ember items that can be placed in the forge, in ember energy.")
        public double maxCrystalValue = 115200.0;
        @Config.RequiresMcRestart
        @Config.Name("Chance Midpoint")
        @Config.Comment("At exactly this amount of ember items, the chance to successfully reforge is exactly 50%.")
        public double chanceMidpoint = 14400.0;
        @Config.RequiresMcRestart
        @Config.Name("Forge Capacity")
        @Config.Comment("How much ember fits into the Inferno Forge?")
        public int capacity = 32000;
    }

    public static class GeoSeparatorCategory {
        @Config.RequiresMcRestart
        @Config.Name("Separator Capacity")
        @Config.Comment("How much fluid (in mb) fits into a Geologic Separator?")
        public int capacity = 1000;
    }

    public static class DawnStoneAnvilCategory {
        @Config.RequiresMcRestart
        @Config.Name("Hits per process")
        @Config.Comment("Number of hammer hits it takes to finish one process")
        public int maxHits = 40;
    }
}
