package teamroots.embers.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;
import teamroots.embers.gui.GuiHandler;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.recipe.RecipeRegistry;
import teamroots.embers.research.ResearchManager;
import teamroots.embers.util.EmberGenUtil;
import teamroots.embers.util.ItemModUtil;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event){
		PacketHandler.registerMessages();
		RegistryManager.registerAll();
		EmberGenUtil.init();
		ItemModUtil.init();
		RecipeRegistry.init();
		ResearchManager.initResearches();
	}
	
	public void init(FMLInitializationEvent event){
		
	}
	
	public void postInit(FMLPostInitializationEvent event){
		NetworkRegistry.INSTANCE.registerGuiHandler(Embers.instance, new GuiHandler());
	}
}
