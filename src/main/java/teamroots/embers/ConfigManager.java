package teamroots.embers;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigManager {

    public static Configuration config;

    //ORES
    public static int
            ancientGolemSpawnWeight;


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

        config.addCustomCategoryComment("mobs", "Settings related to ore generation.");

        ancientGolemSpawnWeight = config.getInt("ancientGolemSpawnWeight", "mobs", 25, 0, 32767, "Spawning weight of the Ancient Golem mob. Higher values make golems spawn more frequently.");
        ancientGolemKnockbackResistance = config.getFloat("ancientGolemKnockbackResistance", "mobs", 1.0f, 0.0f, 1.0f, "How much knockback resistance Ancient Golems have.");

        config.addCustomCategoryComment("structures", "Settings related to structure generation.");

        config.addCustomCategoryComment("compat", "Settings related to compatibility with other mods.");

        enableNickel = config.getBoolean("enableNickel", "compat", true, "If true, Embers will register items, blocks, and recipes providing support for other mods' nickel.");
        enableTin = config.getBoolean("enableTin", "compat", true, "If true, Embers will register items, blocks, and recipes providing support for other mods' tin.");
        enableAluminum = config.getBoolean("enableAluminum", "compat", true, "If true, Embers will register items, blocks, and recipes providing support for other mods' aluminum.");
        enableBronze = config.getBoolean("enableBronze", "compat", true, "If true, Embers will register items, blocks, and recipes providing support for other mods' bronze.");
        enableElectrum = config.getBoolean("enableElectrum", "compat", true, "If true, Embers will register items, blocks, and recipes providing support for other mods' electrum.");

    }


}
