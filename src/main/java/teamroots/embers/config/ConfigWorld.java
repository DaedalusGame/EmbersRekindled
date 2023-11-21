package teamroots.embers.config;

import net.minecraftforge.common.config.Config;
import teamroots.embers.Embers;

@Config(modid = Embers.MODID, category = "world", name = Embers.CFG_FOLDER + "world")
@Config.LangKey("cfg.embers.world")
public class ConfigWorld {

    @Config.Name("Ore Generation")
    @Config.LangKey("cfg.embers.world.ore")
    @Config.Comment("Options about the ore generation")
    public static final OreCategory ORE = new OreCategory();

    @Config.Name("Small Ruin")
    @Config.LangKey("cfg.embers.world.small_ruin")
    @Config.Comment("Options about the small ruin generation")
    public static final SmallRuinCategory SMALL_RUIN = new SmallRuinCategory();

    public static class SmallRuinCategory {
        @Config.RequiresMcRestart
        @Config.Name("Dimension Blacklist")
        @Config.Comment("A list of all dimension IDs in which Embers small ruin generation is prohibited.")
        public int[] blacklist = new int[]{
                0
        };

        @Config.RequiresMcRestart
        @Config.Name("Is Whitelist?")
        @Config.Comment("Whether the small ruin generation blacklist is a whitelist.")
        public boolean isWhiteList = true;

        @Config.RequiresMcRestart
        @Config.Name("Chance")
        @Config.Comment({
                "Spawning frequency of the small ruin structure.",
                "A value of 0 will prevent spawning altogether."
        })
        public int chance = 5;
    }

    public static class OreCategory {
        @Config.RequiresMcRestart
        @Config.Name("Ore Generation Blacklist")
        @Config.Comment("A list of all dimension IDs in which no Embers Ores will generate.")
        public int[] blacklist = new int[]{
                1,
                -1
        };

        @Config.RequiresMcRestart
        @Config.Name("Is Whitelist?")
        @Config.Comment("Whether the Ore Generation blacklist is a whitelist.")
        public boolean isWhitelist = false;

        @Config.Name("Aluminum Ore")
        @Config.LangKey("cfg.embers.world.ore_aluminum")
        @Config.Comment("World generation for Aluminum Ore")
        public final Ore ALUMINUM = new Ore(6, 4, 0, 56);

        @Config.Name("Copper Ore")
        @Config.LangKey("cfg.embers.world.ore_copper")
        @Config.Comment("World generation for Copper Ore")
        public final Ore COPPER = new Ore(12, 6, 0, 64);

        @Config.Name("Lead Ore")
        @Config.LangKey("cfg.embers.world.ore_lead")
        @Config.Comment("World generation for Lead Ore")
        public final Ore LEAD = new Ore(4, 6, 0, 28);

        @Config.Name("Nickel Ore")
        @Config.LangKey("cfg.embers.world.ore_nickel")
        @Config.Comment("World generation for Nickel Ore")
        public final Ore NICKEL = new Ore(6, 4, 0, 24);

        @Config.Name("Quartz Ore")
        @Config.LangKey("cfg.embers.world.ore_quartz")
        @Config.Comment("World generation for Quartz Ore")
        public final Ore QUARTZ = new Ore(8, 4, 0, 18);

        @Config.Name("Silver Ore")
        @Config.LangKey("cfg.embers.world.ore_silver")
        @Config.Comment("World generation for Silver Ore")
        public final Ore SILVER = new Ore(6, 4, 0, 28);

        @Config.Name("Tin Ore")
        @Config.LangKey("cfg.embers.world.ore_tin")
        @Config.Comment("World generation for Tin Ore")
        public final Ore TIN = new Ore(6, 6, 0, 48);

        public static class Ore {
            @Config.RequiresMcRestart
            @Config.Name("Should generate?")
            @Config.Comment("If the ore should generate at all.")
            public boolean generate = true;

            @Config.RequiresMcRestart
            @Config.Name("Vein Size")
            @Config.Comment("Maximum size of this ore vein (in blocks)")
            public int veinSize;

            @Config.RequiresMcRestart
            @Config.Name("Veins per Chunk")
            @Config.Comment("Number of attempts to spawn this ore the world generator will make for each chunk.")
            public int veinPerChunk;

            @Config.RequiresMcRestart
            @Config.Name("min Y value")
            @Config.Comment("Minimum height over which this ore will spawn.")
            public int minY;

            @Config.RequiresMcRestart
            @Config.Name("max Y value")
            @Config.Comment("Maximum height under which this ore will spawn.")
            public int maxY;

            private Ore(int veinSize, int veinPerChunk, int minY, int maxY) {
                this.veinSize = veinSize;
                this.veinPerChunk = veinPerChunk;
                this.minY = minY;
                this.maxY = maxY;
            }
        }
    }
}
