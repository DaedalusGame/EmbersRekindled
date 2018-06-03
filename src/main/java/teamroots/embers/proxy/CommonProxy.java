package teamroots.embers.proxy;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;
import teamroots.embers.gui.GuiHandler;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.recipe.RecipeRegistry;
import teamroots.embers.reflection.Fields;
import teamroots.embers.research.ResearchManager;
import teamroots.embers.util.EmberGenUtil;
import teamroots.embers.util.ItemModUtil;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event){
		Fields.init();
		PacketHandler.registerMessages();
		RegistryManager.registerAll();
		EmberGenUtil.init();
		ItemModUtil.init();
	}
	
	public void init(FMLInitializationEvent event){
		//NOOP
	}
	
	public void postInit(FMLPostInitializationEvent event){
		ResearchManager.initResearches();
		NetworkRegistry.INSTANCE.registerGuiHandler(Embers.instance, new GuiHandler());
		RecipeRegistry.mergeOreRecipes();
	}

	public void playItemSound(EntityLivingBase entity, Item item, SoundEvent soundIn, SoundCategory categoryIn, boolean repeat, float volume, float pitch) {
		//NOOP
	}
}
