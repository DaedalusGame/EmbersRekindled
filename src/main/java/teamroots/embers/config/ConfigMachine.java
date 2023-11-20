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
    @Config.LangKey("cfg.embers.machine.inferno_forge")
    @Config.Comment("Options about the Inferno Forge")
    public static final InfernoForgeCategory INFERNO_FORGE_CATEGORY = new InfernoForgeCategory();

    @Config.Name("Geo Separator")
    @Config.LangKey("cfg.embers.machine.geo_separator")
    @Config.Comment("Options about the Geo Separator")
    public static final GeoSeparatorCategory GEO_SEPARATOR_CATEGORY = new GeoSeparatorCategory();

    @Config.Name("Dawnstone Anvil")
    @Config.LangKey("cfg.embers.machine.dawnstone_anvil")
    @Config.Comment("Options about the Dawnstone Anvil")
    public static final DawnStoneAnvilCategory DAWN_STONE_ANVIL_CATEGORY = new DawnStoneAnvilCategory();

    @Config.Name("Charger")
    @Config.LangKey("cfg.embers.machine.charger")
    @Config.Comment("Options about the Charger")
    public static final ChargerCategory CHARGER_CATEGORY = new ChargerCategory();

    @Config.Name("Cinder Plinth")
    @Config.LangKey("cfg.embers.machine.cinder_plinth")
    @Config.Comment("Options about the Cinder Plinth")
    public static final CinderPlinthCategory CINDER_PLINTH_CATEGORY = new CinderPlinthCategory();

    @Config.Name("Fluid Vessel")
    @Config.LangKey("cfg.embers.machine.tank")
    @Config.Comment("Options about the Fluid Vessel")
    public static final TankCategory TANK_CATEGORY = new TankCategory();

    @Config.Name("Reservoir")
    @Config.LangKey("cfg.embers.machine.reservoir")
    @Config.Comment("Options about the Reservoir")
    public static final ReservoirCategory RESERVOIR_CATEGORY = new ReservoirCategory();

    @Config.Name("Mini Boiler")
    @Config.LangKey("cfg.embers.machine.mini_boiler")
    @Config.Comment("Options about the Mini Boiler")
    public static final MiniBoilerCategory MINI_BOILER_CATEGORY = new MiniBoilerCategory();

    @Config.Name("Ember Bore")
    @Config.LangKey("cfg.embers.machine.ember_bore")
    @Config.Comment("Options about the Ember Bore")
    public static final EmberBoreCategory EMBER_BORE_CATEGORY = new EmberBoreCategory();


    @Config.RequiresMcRestart
    @Config.Name("Ingot to Fluid ratio")
    @Config.Comment("What is the liquid equivalent to an ingot in mb?")
    public static int ingotFluidAmount = 144;

    @Config.RequiresMcRestart
    @Config.Name("Nugget to Fluid ratio")
    @Config.Comment("What is the liquid equivalent to a nugget in mb?")
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

    public static class ChargerCategory {
        @Config.RequiresMcRestart
        @Config.Name("Max Transfer Rate")
        @Config.Comment("How much ember is transferred between item and charger per tick?")
        public double maxTransfer = 10.0;
    }

    public static class CinderPlinthCategory {
        @Config.RequiresMcRestart
        @Config.Name("Process Time")
        @Config.Comment("The time in ticks it takes to process one item")
        public int processTime = 40;

        @Config.RequiresMcRestart
        @Config.Name("Ember Cost")
        @Config.Comment("The ember cost per tick")
        public double emberCost = 0.5;
    }

    public static class TankCategory {
        @Config.RequiresMcRestart
        @Config.Name("Capacity")
        @Config.Comment("How much fluid (in mb) fits into the Fluid Vessel?")
        public int capacity = 16000;
    }

    public static class ReservoirCategory {
        @Config.RequiresMcRestart
        @Config.Name("Capacity")
        @Config.Comment("How much fluid (in mb) fits into each Caminite Ring on a Reservoir?")
        public int capacity = 40000;
    }

    public static class MiniBoilerCategory {
        @Config.RequiresMcRestart
        @Config.Name("Capacity")
        @Config.Comment("How much fluid (in mb) fits into a mini boiler?")
        public int capacity = 16000;

        @Config.RequiresMcRestart
        @Config.Name("Heat Multiplier")
        @Config.Comment("How efficient, heat-wise, the mini boiler is at making steam?")
        public float heatMultiplier = 1.0f;

        @Config.RequiresMcRestart
        @Config.Name("Can Explode?")
        @Config.Comment("Whether or not the mini boiler should explode when at maximum steam pressure")
        public boolean canExplode = true;
    }

    public static class EmberBoreCategory {
        @Config.RequiresMcRestart
        @Config.Name("Dimension Blacklist")
        @Config.Comment("A list of all dimension IDs in which Embers Ember Bore will not mine.")
        public int[] blacklist = new int[]{};

        @Config.RequiresMcRestart
        @Config.Name("Is Whitelist?")
        @Config.Comment("Whether the Ember Bore blacklist is a whitelist.")
        public boolean isWhiteList = false;

        @Config.RequiresMcRestart
        @Config.Name("Max Y-Height")
        @Config.Comment("The maximum y-level at which the Ember Bore can mine ember.")
        public int yMax = 7;

        @Config.RequiresMcRestart
        @Config.Name("Speed Modifier")
        @Config.Comment("The speed modifier of the Ember Bore before upgrades.")
        public float speedMod = 1.0f;

        @Config.RequiresMcRestart
        @Config.Name("Process Time")
        @Config.Comment("The time in ticks it takes to try one dig attempt.")
        public int processTime = 200;

        @Config.RequiresMcRestart
        @Config.Name("Fuel Cost")
        @Config.Comment("The amount of fuel consumed each tick")
        public double fuelCost = 3.0;
    }
}
