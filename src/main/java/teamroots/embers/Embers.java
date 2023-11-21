package teamroots.embers;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.proxy.CommonProxy;
import teamroots.embers.recipe.RecipeRegistry;
import teamroots.embers.research.ResearchManager;

@Mod(modid = Embers.MODID, name = Embers.MODNAME, dependencies = Embers.DEPENDENCIES/*,  guiFactory = Embers.GUI_FACTORY*/)
public class Embers {
	public static final String MODID = "embers";
	public static final String MODNAME = "Embers";
	public static final String CFG_FOLDER = "Embers/";
	public static final String DEPENDENCIES = "after:mysticalmechanics";
	public static final String GUI_FACTORY = "teamroots.embers.gui.GuiFactory";

	@SidedProxy(clientSide = "teamroots.embers.proxy.ClientProxy",serverSide = "teamroots.embers.proxy.ServerProxy")
    public static CommonProxy proxy;
	
	public static CreativeTabs tab = new CreativeTabs("embers") {
    	@Override
    	public String getTabLabel(){
    		return "embers";
    	}
		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem(){
			return new ItemStack(RegistryManager.crystal_ember,1);
		}
	};
	
	public static CreativeTabs resource_tab = new CreativeTabs("embers_resources") {
    	@Override
    	public String getTabLabel(){
    		return "embers_resources";
    	}
		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem(){
			return new ItemStack(RegistryManager.ingot_dawnstone,1);
		}
	};
	
    @Instance("embers")
    public static Embers instance;

	static {
		FluidRegistry.enableUniversalBucket();
	}

	@EventHandler
	public void constructing(FMLConstructionEvent event) {
		proxy.constructing(event);
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		MinecraftForge.EVENT_BUS.register(new EventManager());
		MinecraftForge.EVENT_BUS.register(new RegistryManager());
		MinecraftForge.EVENT_BUS.register(new RecipeRegistry());
		MinecraftForge.EVENT_BUS.register(new ResearchManager());
		proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		proxy.postInit(event);
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event){
		//event.registerServerCommand(new CommandEmberFill());
	}
}
