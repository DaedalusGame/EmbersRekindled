package teamroots.embers;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ConfigManager {

    public static Configuration config;

    //ORES
    public static int copperVeinSize, copperMinY, copperMaxY, copperVeinsPerChunk,
            leadVeinSize, leadMinY, leadMaxY, leadVeinsPerChunk,
            silverVeinSize, silverMinY, silverMaxY, silverVeinsPerChunk,
            quartzVeinSize, quartzMinY, quartzMaxY, quartzVeinsPerChunk,
            ancientGolemSpawnWeight;
    public static HashSet<Integer> orespawnGraylist = new HashSet<>();
    public static boolean orespawnIsWhiteList;

    //STRUCTURES
    public static int smallRuinChance;
    public static HashSet<Integer> smallRuinGraylist = new HashSet<>();
    public static boolean smallRuinIsWhiteList;


    @Deprecated
    public static List<Integer> orespawnBlacklist = new ArrayList<>();
    @Deprecated
    public static List<Integer> smallRuinBlacklist = new ArrayList<>();


    //COMPAT
    public static boolean enableNickel, enableTin, enableAluminum, enableBronze, enableElectrum;
    public static int nickelVeinSize, nickelMinY, nickelMaxY, nickelVeinsPerChunk,
            tinVeinSize, tinMinY, tinMaxY, tinVeinsPerChunk,
            aluminumVeinSize, aluminumMinY, aluminumMaxY, aluminumVeinsPerChunk;

    public static double ancientGolemKnockbackResistance;

    public static void init(File configFile) {
        if (config == null) {
            //config = new Configuration(configFile);
            //load();
        }
    }

    public static void load() {
        config.addCustomCategoryComment("ores", "Settings related to ore generation.");

        for (String s : config.getStringList("oreBlacklist", "ores", new String[]{"-1", "1"}, "A list of all dimension IDs in which Embers orespawn is prohibited. Embers ores will spawn in any dimension not on this list, but only in vanilla stone.")) {
            orespawnGraylist.add(Integer.valueOf(s));
        }
        orespawnIsWhiteList = config.getBoolean("oreBlacklistIsWhitelist", "ores", false, "Whether the orespawn blacklist is a whitelist.");

        copperVeinSize = config.getInt("copperVeinSize", "ores", 12, 0, 255, "Maximum size of a copper ore vein (in blocks)");
        copperMinY = config.getInt("copperMinY", "ores", 0, 0, 254, "Minimum height over which copper ore will spawn.");
        copperMaxY = config.getInt("copperMaxY", "ores", 64, 1, 255, "Maximum height under which copper ore will spawn.");
        copperVeinsPerChunk = config.getInt("copperVeinsPerChunk", "ores", 6, 0, 255, "Number of attempts to spawn copper ore the world generator will make for each chunk.");

        leadVeinSize = config.getInt("leadVeinSize", "ores", 8, 0, 255, "Maximum size of a lead ore vein (in blocks)");
        leadMinY = config.getInt("leadMinY", "ores", 0, 0, 254, "Minimum height over which lead ore will spawn.");
        leadMaxY = config.getInt("leadMaxY", "ores", 28, 1, 255, "Maximum height under which lead ore will spawn.");
        leadVeinsPerChunk = config.getInt("leadVeinsPerChunk", "ores", 4, 0, 255, "Number of attempts to spawn lead ore the world generator will make for each chunk.");

        silverVeinSize = config.getInt("silverVeinSize", "ores", 6, 0, 255, "Maximum size of a silver ore vein (in blocks)");
        silverMinY = config.getInt("silverMinY", "ores", 0, 0, 254, "Minimum height over which silver ore will spawn.");
        silverMaxY = config.getInt("silverMaxY", "ores", 28, 1, 255, "Maximum height under which silver ore will spawn.");
        silverVeinsPerChunk = config.getInt("silverVeinsPerChunk", "ores", 4, 0, 255, "Number of attempts to spawn silver ore the world generator will make for each chunk.");

        quartzVeinSize = config.getInt("quartzVeinSize", "ores", 8, 0, 255, "Maximum size of a quartz ore vein (in blocks)");
        quartzMinY = config.getInt("quartzMinY", "ores", 0, 0, 254, "Minimum height over which quartz ore will spawn.");
        quartzMaxY = config.getInt("quartzMaxY", "ores", 18, 1, 255, "Maximum height under which quartz ore will spawn.");
        quartzVeinsPerChunk = config.getInt("quartzVeinsPerChunk", "ores", 4, 0, 255, "Number of attempts to spawn quartz ore the world generator will make for each chunk.");

        config.addCustomCategoryComment("mobs", "Settings related to ore generation.");

        ancientGolemSpawnWeight = config.getInt("ancientGolemSpawnWeight", "mobs", 25, 0, 32767, "Spawning weight of the Ancient Golem mob. Higher values make golems spawn more frequently.");
        ancientGolemKnockbackResistance = config.getFloat("ancientGolemKnockbackResistance", "mobs", 1.0f, 0.0f, 1.0f, "How much knockback resistance Ancient Golems have.");

        config.addCustomCategoryComment("structures", "Settings related to structure generation.");

        smallRuinChance = config.getInt("smallRuinChance", "structures", 5, 0, 32767, "Spawning frequency of the small ruin structure. A value of 0 will prevent spawning altogether.");

        for (String s : config.getStringList("smallRuinBlacklist", "structures", new String[]{"0"}, "A list of all dimension IDs in which Embers small ruin generation is prohibited.")) {
            smallRuinGraylist.add(Integer.valueOf(s));
        }
        smallRuinIsWhiteList = config.getBoolean("smallRuinBlacklistIsWhitelist", "structures", true, "Whether the small ruin generation blacklist is a whitelist.");

        config.addCustomCategoryComment("compat", "Settings related to compatibility with other mods.");

        enableNickel = config.getBoolean("enableNickel", "compat", true, "If true, Embers will register items, blocks, and recipes providing support for other mods' nickel.");
        enableTin = config.getBoolean("enableTin", "compat", true, "If true, Embers will register items, blocks, and recipes providing support for other mods' tin.");
        enableAluminum = config.getBoolean("enableAluminum", "compat", true, "If true, Embers will register items, blocks, and recipes providing support for other mods' aluminum.");
        enableBronze = config.getBoolean("enableBronze", "compat", true, "If true, Embers will register items, blocks, and recipes providing support for other mods' bronze.");
        enableElectrum = config.getBoolean("enableElectrum", "compat", true, "If true, Embers will register items, blocks, and recipes providing support for other mods' electrum.");

        aluminumVeinSize = config.getInt("aluminumVeinSize", "compat", 6, 0, 255, "Maximum size of a aluminum ore vein (in blocks)");
        aluminumMinY = config.getInt("aluminumMinY", "compat", 0, 0, 254, "Minimum height over which aluminum ore will spawn.");
        aluminumMaxY = config.getInt("aluminumMaxY", "compat", 58, 1, 255, "Maximum height under which aluminum ore will spawn.");
        aluminumVeinsPerChunk = config.getInt("aluminumVeinsPerChunk", "compat", 4, 0, 255, "Number of attempts to spawn aluminum ore the world generator will make for each chunk.");

        nickelVeinSize = config.getInt("nickelVeinSize", "compat", 6, 0, 255, "Maximum size of a nickel ore vein (in blocks)");
        nickelMinY = config.getInt("nickelMinY", "compat", 0, 0, 254, "Minimum height over which nickel ore will spawn.");
        nickelMaxY = config.getInt("nickelMaxY", "compat", 24, 1, 255, "Maximum height under which nickel ore will spawn.");
        nickelVeinsPerChunk = config.getInt("nickelVeinsPerChunk", "compat", 4, 0, 255, "Number of attempts to spawn nickel ore the world generator will make for each chunk.");

        tinVeinSize = config.getInt("tinVeinSize", "compat", 6, 0, 255, "Maximum size of a tin ore vein (in blocks)");
        tinMinY = config.getInt("tinMinY", "compat", 0, 0, 254, "Minimum height over which tin ore will spawn.");
        tinMaxY = config.getInt("tinMaxY", "compat", 48, 1, 255, "Maximum height under which tin ore will spawn.");
        tinVeinsPerChunk = config.getInt("tinVeinsPerChunk", "compat", 6, 0, 255, "Number of attempts to spawn tin ore the world generator will make for each chunk.");

        if (config.hasChanged()) {
            config.save();
        }
    }

    private static String[] splitName(String toSplit) {
        String[] splitString = new String[2];
        int i = toSplit.lastIndexOf(".");

        if (i >= 0) {
            splitString[1] = toSplit.substring(i + 1);

            if (i > 1) {
                splitString[0] = toSplit.substring(0, i);
            }
        }

        return splitString;
    }

    public static double loadDouble(String name, double defaultValue, String comment) {
        String[] splitName = splitName(name);
        return config.getFloat(splitName[1], splitName[0], (float) defaultValue, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, comment);
    }

    public static float loadFloat(String name, float defaultValue, String comment) {
        String[] splitName = splitName(name);
        return config.getFloat(splitName[1], splitName[0], defaultValue, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, comment);
    }

    public static int loadInteger(String name, int defaultValue, String comment) {
        String[] splitName = splitName(name);
        return config.getInt(splitName[1], splitName[0], defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE, comment);
    }

    public static boolean loadBoolean(String name, boolean defaultValue, String comment) {
        String[] splitName = splitName(name);
        return config.getBoolean(splitName[1], splitName[0], defaultValue, comment);
    }

    public static String[] loadStringList(String name, String[] defaultValue, String comment) {
        String[] splitName = splitName(name);
        return config.getStringList(splitName[1], splitName[0], defaultValue, comment);
    }

    public static boolean isSmallRuinEnabled(int dimension) {
        return !(smallRuinGraylist.contains(dimension) != smallRuinIsWhiteList || smallRuinBlacklist.contains(dimension));
    }

    public static boolean isOreSpawnEnabled(int dimension) {
        return !(orespawnGraylist.contains(dimension) != orespawnIsWhiteList || orespawnBlacklist.contains(dimension));
    }

    /*	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.getModID().equalsIgnoreCase(Embers.MODID))
		{
			load();
		}
	}*/
}
