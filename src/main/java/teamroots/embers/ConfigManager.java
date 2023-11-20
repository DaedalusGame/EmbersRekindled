package teamroots.embers;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ConfigManager {

    public static Configuration config;

    //ORES
    public static int
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
