package teamroots.embers;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class ConfigManager {

	public static Configuration config;

	//ORES
	public static int copperVeinSize, copperMinY, copperMaxY, copperVeinsPerChunk,
					leadVeinSize, leadMinY, leadMaxY, leadVeinsPerChunk,
					silverVeinSize, silverMinY, silverMaxY, silverVeinsPerChunk;

	public static void init(File configFile)
	{
		if(config == null)
		{
			config = new Configuration(configFile);
			load();
		}
	}
	
	public static void load()
	{
		config.addCustomCategoryComment("ores", "Settings related to ore generation.");
		
		copperVeinSize = config.getInt("copperVeinSize", "ores", 12, 0, 255, "Maximum size of a copper ore vein (in blocks)");
		copperMinY = config.getInt("copperMinY", "ores", 0, 0, 255, "Minimum height over which copper ore will spawn.");
		copperMaxY = config.getInt("copperMaxY", "ores", 64, 0, 255, "Maximum height under which copper ore will spawn.");
		copperVeinsPerChunk = config.getInt("copperVeinsPerChunk", "ores", 6, 0, 255, "Number of attempts to spawn copper ore the world generator will make for each chunk.");
		
		leadVeinSize = config.getInt("leadVeinSize", "ores", 8, 0, 255, "Maximum size of a lead ore vein (in blocks)");
		leadMinY = config.getInt("leadMinY", "ores", 0, 0, 255, "Minimum height over which lead ore will spawn.");
		leadMaxY = config.getInt("leadMaxY", "ores", 28, 0, 255, "Maximum height under which lead ore will spawn.");
		leadVeinsPerChunk = config.getInt("leadVeinsPerChunk", "ores", 4, 0, 255, "Number of attempts to spawn lead ore the world generator will make for each chunk.");
		
		silverVeinSize = config.getInt("silverVeinSize", "ores", 6, 0, 255, "Maximum size of a silver ore vein (in blocks)");
		silverMinY = config.getInt("silverMinY", "ores", 0, 0, 255, "Minimum height over which silver ore will spawn.");
		silverMaxY = config.getInt("silverMaxY", "ores", 28, 0, 255, "Maximum height under which silver ore will spawn.");
		silverVeinsPerChunk = config.getInt("silverVeinsPerChunk", "ores", 4, 0, 255, "Number of attempts to spawn silver ore the world generator will make for each chunk.");
		
		if (config.hasChanged())
		{
			config.save();
		}
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.getModID().equalsIgnoreCase(Embers.MODID))
		{
			load();
		}
	}
}
