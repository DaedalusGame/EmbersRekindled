package teamroots.embers;

import net.minecraftforge.common.config.Configuration;

public class ConfigManager {

    public static Configuration config;

    //COMPAT
    public static boolean enableNickel, enableTin, enableAluminum, enableBronze, enableElectrum;

    public static void load() {
        config.addCustomCategoryComment("ores", "Settings related to ore generation.");

        config.addCustomCategoryComment("mobs", "Settings related to ore generation.");

        config.addCustomCategoryComment("structures", "Settings related to structure generation.");

        config.addCustomCategoryComment("compat", "Settings related to compatibility with other mods.");

        enableNickel = config.getBoolean("enableNickel", "compat", true, "If true, Embers will register items, blocks, and recipes providing support for other mods' nickel.");
        enableTin = config.getBoolean("enableTin", "compat", true, "If true, Embers will register items, blocks, and recipes providing support for other mods' tin.");
        enableAluminum = config.getBoolean("enableAluminum", "compat", true, "If true, Embers will register items, blocks, and recipes providing support for other mods' aluminum.");
        enableBronze = config.getBoolean("enableBronze", "compat", true, "If true, Embers will register items, blocks, and recipes providing support for other mods' bronze.");
        enableElectrum = config.getBoolean("enableElectrum", "compat", true, "If true, Embers will register items, blocks, and recipes providing support for other mods' electrum.");

    }


}
