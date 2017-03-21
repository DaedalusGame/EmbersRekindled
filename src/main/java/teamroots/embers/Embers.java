package teamroots.embers;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.proxy.CommonProxy;

@Mod(modid = Embers.MODID, name = Embers.MODNAME, version = Embers.VERSION, dependencies = Embers.DEPENDENCIES)
public class Embers {
	public static final String MODID = "embers";
	public static final String MODNAME = "Embers";
	public static final String VERSION = "0.206";
	public static final String DEPENDENCIES = "";
	
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
	
    @Instance("embers")
    public static Embers instance;

	static {
		FluidRegistry.enableUniversalBucket();
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		MinecraftForge.EVENT_BUS.register(new EventManager());
		MinecraftForge.EVENT_BUS.register(new ConfigManager());
        ConfigManager.init(event.getSuggestedConfigurationFile());
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
