package teamroots.embers.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import teamroots.embers.RegistryManager;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.recipe.RecipeRegistry;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event){
		PacketHandler.registerMessages();
		RegistryManager.registerAll();
		RecipeRegistry.init();
	}
	
	public void init(FMLInitializationEvent event){
		
	}
	
	public void postInit(FMLPostInitializationEvent event){
		
	}
}
