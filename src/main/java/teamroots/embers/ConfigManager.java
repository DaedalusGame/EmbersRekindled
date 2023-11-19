package teamroots.embers;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Loader;
import teamroots.embers.compat.BaublesIntegration;
import teamroots.embers.compat.MysticalMechanicsIntegration;
import teamroots.embers.config.ConfigMain;
import teamroots.embers.item.ItemCinderStaff;
import teamroots.embers.item.ItemIgnitionCannon;
import teamroots.embers.tileentity.*;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    //MACHINES
    public static HashSet<Integer> emberBoreGraylist = new HashSet<>();
    public static boolean emberBoreIsWhiteList;

    @Deprecated
    public static List<Integer> orespawnBlacklist = new ArrayList<>();
    @Deprecated
    public static List<Integer> smallRuinBlacklist = new ArrayList<>();

    public static float emberBoreSpeedMod;
    public static int emberBoreMaxYLevel;

    //COMPAT
    public static boolean enableNickel, enableTin, enableAluminum, enableBronze, enableElectrum;
    public static int nickelVeinSize, nickelMinY, nickelMaxY, nickelVeinsPerChunk,
            tinVeinSize, tinMinY, tinMaxY, tinVeinsPerChunk,
            aluminumVeinSize, aluminumMinY, aluminumMaxY, aluminumVeinsPerChunk;
    //public static boolean enableBaublesIntegration;
    //public static boolean enableMysticalMechanicsIntegration;
    //public static boolean enableJeiCheat;

    //MISC
    public static boolean pvpEverybodyIsEnemy;
    public static boolean codexCategoryIsProgress;
    public static boolean codexEntryIsProgress;

    //CLIENT
    //public static boolean enableParticleCollisions;
    //public static boolean enableParticles;

    //PARAMETERS
    public static int melterOreAmount;
    public static int stampPlateAmount;
    public static int stampAspectusAmount;
    public static int stampGearAmount;
    public static int reservoirCapacity;
    public static int miniBoilerCapacity;
    public static float miniBoilerHeatMultiplier;
    public static boolean miniBoilerCanExplode;
    public static int geoSeparatorCapacity;
    public static double ancientGolemKnockbackResistance;

    static Pattern damageRatePattern = Pattern.compile("(\\w+):(\\d+(?:\\.\\d+|))");

    public static String[] defaultScaleDamagePasses = new String[]{
            "drown:1.0",
            "starve:1.0",
    };
    public static String[] defaultScaleDamageRates = new String[]{

    };

    public static Map<String, Double> scaleDamagePasses = new HashMap<>();
    public static Map<String, Double> scaleDamageRates = new HashMap<>();

    public static boolean isBaublesIntegrationEnabled() {
        return ConfigMain.COMPAT_CATEGORY.enableBaublesIntegration && Loader.isModLoaded("baubles") || ConfigMain.COMPAT_CATEGORY.enableBaublesIntegration;
    }

    public static boolean isMysticalMechanicsIntegrationEnabled() {
        return ConfigMain.COMPAT_CATEGORY.enableMysticalMechanicsIntegration && Loader.isModLoaded("mysticalmechanics") || ConfigMain.COMPAT_CATEGORY.forceMysticalMechanicsIntegration;
    }

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

        //enableJeiCheat = config.getBoolean("enableJeiCheat", "compat", false, "If true, JEI will show the exact amount of Ash needed in an Alchemy recipe.");
        //enableBaublesIntegration = config.getBoolean("enableBaubles", "compat", true, "If true, Embers will register items, blocks and recipes providing Baubles integration.");
        //enableMysticalMechanicsIntegration = config.getBoolean("enableMysticalMechanics", "compat", true, "If true, Embers will register items, blocks and recipes providing Mystical Mechanics integration.");

        //pvpEverybodyIsEnemy = config.getBoolean("everybodyIsAnEnemy", "misc", false, "If true, Embers homing projectiles will go for neutral players.");
        //codexCategoryIsProgress = config.getBoolean("codexCategoryIsProgress", "misc", true, "Codex category is shut. Progression is open.");
        //codexEntryIsProgress = config.getBoolean("codexEntryIsProgress", "misc", true, "Codex entry is shut and hide. Progression is open and show.");

        //enableParticleCollisions = config.getBoolean("enableParticleCollisions", "client", true, "Whether or not particles should collide with blocks. Disabling this might significantly improve performance.");
        //enableParticles = config.getBoolean("enableParticles", "client", true, "Whether or not particles are enabled. Disabling this will change the gameplay experience but significantly improve performance.");

        //DefaultEmberCapability.allAcceptVolatile = loadBoolean("parameters.allAcceptVolatile", false, "Whether ember conduits can attach to any ember consumer/producer");

        config.addCustomCategoryComment("parameters", "Settings for machine/item/misc parameters");

        //Melter
        //melterOreAmount = loadInteger("parameters.melter.oreAmount", 144, "How many mb of fluid are obtained per ore output in the melter. This is multiplied by the amount of output a melter would produce, so by default 144mb * 2 ingots.");
        //TileEntityFurnaceBottom.PROCESS_TIME = loadInteger("parameters.melter.processTime",  TileEntityFurnaceBottom.PROCESS_TIME, "The time in ticks it takes to process one recipe.");
        //TileEntityFurnaceBottom.EMBER_COST = loadDouble("parameters.melter.cost",  TileEntityFurnaceBottom.EMBER_COST, "The ember cost per tick.");

        //Geo Seperator
        //geoSeparatorCapacity = loadInteger("parameters.geoSeparator.capacity", Fluid.BUCKET_VOLUME, "How much fluid (in mb) fits into a Geologic Seperator");

        //Stamper
        //stampPlateAmount = loadInteger("parameters.stamper.plateAmount", 1, "How many ingots are required to make one plate in the stamper.");
        //stampAspectusAmount = loadInteger("parameters.stamper.aspectusAmount",  1, "How many ingots are required to make one aspectus in the stamper.");
        //stampGearAmount = loadInteger("parameters.stamper.gearAmount",  2, "How many ingots are required to make one gear in the stamper.");
        //TileEntityStampBase.capacity = loadInteger("parameters.stamper.capacity",  TileEntityStampBase.capacity,  "How much fluid (in mb) fits into the Stamp Base.");

        //Ember Bore
        for (String s : loadStringList("parameters.emberBore.blacklist", new String[]{}, "A list of all dimension IDs in which Embers Ember Bore will not mine.")) {
            emberBoreGraylist.add(Integer.valueOf(s));
        }
        emberBoreIsWhiteList = loadBoolean("parameters.emberBore.isWhiteList", false, "Whether the Ember Bore blacklist is a whitelist.");
        emberBoreMaxYLevel = loadInteger("parameters.emberBore.yMax", 7, "The maximum y-level at which the Ember Bore can mine ember.");
        emberBoreSpeedMod = loadFloat("parameters.emberBore.speedMod", 1, "The speed modifier of the Ember Bore before upgrades.");
        TileEntityEmberBore.BORE_TIME = loadInteger("parameters.emberBore.processTime", TileEntityEmberBore.BORE_TIME, "The time in ticks it takes to try one dig attempt.");
        TileEntityEmberBore.FUEL_CONSUMPTION = loadDouble("parameters.emberBore.fuelCost", TileEntityEmberBore.FUEL_CONSUMPTION, "The amount of fuel consumed each tick");

        //Charger
        TileEntityCharger.MAX_TRANSFER = loadDouble("parameters.charger.transfer", TileEntityCharger.MAX_TRANSFER, "How much ember is transferred between item and charger per tick");

        //Cinder Plinth
        TileEntityCinderPlinth.PROCESS_TIME = loadInteger("parameters.cinderPlinth.processTime", TileEntityCinderPlinth.PROCESS_TIME, "The time in ticks it takes to process one item.");
        TileEntityCinderPlinth.EMBER_COST = loadDouble("parameters.cinderPlinth.cost", TileEntityCinderPlinth.EMBER_COST, "The ember cost per tick.");

        //Dawnstone Anvil
        TileEntityDawnstoneAnvil.MAX_HITS = loadInteger("parameters.dawnstoneAnvil.maxHits", TileEntityDawnstoneAnvil.MAX_HITS, "Number of hammer hits it takes to finish one process");

        //Inferno Forge
        //TileEntityInfernoForge.PROCESS_TIME = loadInteger("parameters.infernoForge.processTime", TileEntityInfernoForge.PROCESS_TIME, "The time in ticks it takes to process one item.");
        //TileEntityInfernoForge.EMBER_COST = loadDouble("parameters.infernoForge.cost", TileEntityInfernoForge.EMBER_COST, "The ember cost per tick.");
        //TileEntityInfernoForge.MAX_LEVEL = loadInteger("parameters.infernoForge.maxLevel", TileEntityInfernoForge.MAX_LEVEL, "The maximum augment level that can be reforged to.");
        //TileEntityInfernoForge.MAX_CRYSTAL_VALUE = loadDouble("parameters.infernoForge.maxCrystalValue", TileEntityInfernoForge.MAX_CRYSTAL_VALUE, "The maximum amount of ember items that can be placed in the forge, in ember energy. Ember clusters are worth 3600 ember, and so the default value is 32 clusters worth.");
        //TileEntityInfernoForge.CHANCE_MIDPOINT = loadDouble("parameters.infernoForge.chanceMidPoint", TileEntityInfernoForge.CHANCE_MIDPOINT, "At exactly this amount of ember items, the chance to successfully reforge is exactly 50%. The default value is 4 clusters worth.");
        //Tank
        TileEntityTank.capacity = loadInteger("parameters.tank.capacity", TileEntityTank.capacity, "How much fluid (in mb) fits into the Fluid Vessel.");

        //Reservoir
        reservoirCapacity = loadInteger("parameters.reservoir.capacity", Fluid.BUCKET_VOLUME * 40, "How much fluid (in mb) fits into each Caminite Ring on a Reservoir.");

        //Mini Boiler
        miniBoilerCapacity = loadInteger("parameters.miniBoiler.capacity", Fluid.BUCKET_VOLUME * 16, "How much fluid (in mb) fits into a mini boiler.");
        miniBoilerHeatMultiplier = loadFloat("parameters.miniBoiler.heatMultiplier", 1.0f, "How efficient, heat-wise, the mini boiler is at making steam.");
        miniBoilerCanExplode = loadBoolean("parameters.miniBoiler.canExplode", true, "Whether or not the mini boiler should explode when at maximum steam pressure.");

        //Blazing Ray
        ItemIgnitionCannon.EMBER_COST = loadDouble("parameters.blazingRay.cost", ItemIgnitionCannon.EMBER_COST, "Ember used up by each shot.");
        ItemIgnitionCannon.MAX_CHARGE = loadDouble("parameters.blazingRay.charge", ItemIgnitionCannon.MAX_CHARGE, "Time in ticks to fully charge.");
        ItemIgnitionCannon.COOLDOWN = loadInteger("parameters.blazingRay.cooldown", ItemIgnitionCannon.COOLDOWN, "Cooldown in ticks between each shot.");
        ItemIgnitionCannon.DAMAGE = loadFloat("parameters.blazingRay.damage", ItemIgnitionCannon.DAMAGE, "Damage dealt by one shot.");
        ItemIgnitionCannon.MAX_DISTANCE = loadFloat("parameters.blazingRay.distance", ItemIgnitionCannon.MAX_DISTANCE, "Maximum shot distance.");
        ItemIgnitionCannon.MAX_SPREAD = loadDouble("parameters.blazingRay.spread", ItemIgnitionCannon.MAX_SPREAD, "Maximum spread.");

        //Cinder Staff
        ItemCinderStaff.EMBER_COST = loadDouble("parameters.cinderStaff.cost", ItemCinderStaff.EMBER_COST, "Ember used up by each shot.");
        ItemCinderStaff.MAX_CHARGE = loadDouble("parameters.cinderStaff.charge", ItemCinderStaff.MAX_CHARGE, "Time in ticks to fully charge.");
        ItemCinderStaff.COOLDOWN = loadInteger("parameters.cinderStaff.cooldown", ItemCinderStaff.COOLDOWN, "Cooldown in ticks between each shot.");
        ItemCinderStaff.DAMAGE = loadFloat("parameters.cinderStaff.damage", ItemCinderStaff.DAMAGE, "Damage dealt by one shot.");
        ItemCinderStaff.SIZE = loadFloat("parameters.cinderStaff.size", ItemCinderStaff.SIZE, "Size of the projectile.");
        ItemCinderStaff.AOE_SIZE = loadFloat("parameters.cinderStaff.aoe", ItemCinderStaff.AOE_SIZE, "Area of Effect on impact.");
        ItemCinderStaff.LIFETIME = loadInteger("parameters.cinderStaff.lifetime", ItemCinderStaff.LIFETIME, "Lifetime in ticks of projectile.");

        //Shifting Scales
        scaleDamagePasses.clear();
        for (String pair : loadStringList("parameters.shiftingScales.damagePasses", defaultScaleDamagePasses, "Syntax is 'damagetype:rate'. Determines which damage types are partially unaffected by the shifting scales augment.")) {
            Matcher matcher = damageRatePattern.matcher(pair);
            if (matcher.matches()) {
                scaleDamagePasses.put(matcher.group(1), Double.parseDouble(matcher.group(2)));
            }
        }

        scaleDamageRates.clear();
        for (String pair : loadStringList("parameters.shiftingScales.damageRates", defaultScaleDamageRates, "Syntax is 'damagetype:rate'. Specifies a separate damage rate for depleting the scales.")) {
            Matcher matcher = damageRatePattern.matcher(pair);
            if (matcher.matches()) {
                scaleDamageRates.put(matcher.group(1), Double.parseDouble(matcher.group(2)));
            }
        }

        if (isMysticalMechanicsIntegrationEnabled())
            MysticalMechanicsIntegration.loadConfig();
        if (isBaublesIntegrationEnabled())
            BaublesIntegration.loadConfig();

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

    public static boolean isEmberBoreEnabled(int dimension) {
        return emberBoreGraylist.contains(dimension) == emberBoreIsWhiteList;
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
