package teamroots.embers.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import teamroots.embers.ConfigManager;
import teamroots.embers.RegistryManager;
import teamroots.embers.compat.BaublesIntegration;
import teamroots.embers.compat.MysticalMechanicsIntegration;
import teamroots.embers.model.ModelManager;
import teamroots.embers.particle.ParticleRenderer;
import teamroots.embers.util.sound.ItemUseSound;
import teamroots.embers.util.sound.MachineSound;

public class ClientProxy extends CommonProxy{

	public static ParticleRenderer particleRenderer = new ParticleRenderer();
	
	@Override
	public void preInit(FMLPreInitializationEvent event){
		super.preInit(event);
		ModelManager.init();
		RegistryManager.registerEntityRendering();
	}
	
	@Override
	public void init(FMLInitializationEvent event){
		super.init(event);
		RegistryManager.registerColorHandlers();
		if(ConfigManager.isBaublesIntegrationEnabled())
			BaublesIntegration.registerClientSide();
		if(ConfigManager.isMysticalMechanicsIntegrationEnabled())
			MysticalMechanicsIntegration.registerClientSide();
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event){
		super.postInit(event);
	}

	@Override
	public void playItemSound(EntityLivingBase entity, Item item, SoundEvent soundIn, SoundCategory categoryIn, boolean repeat, float volume, float pitch) {
		Minecraft.getMinecraft().getSoundHandler().playSound(new ItemUseSound(entity,item,soundIn,categoryIn,repeat,volume,pitch));
	}

	@Override
	public void playMachineSound(TileEntity tile, int id, SoundEvent soundIn, SoundCategory categoryIn, boolean repeat, float volume, float pitch, float xIn, float yIn, float zIn) {
		Minecraft.getMinecraft().getSoundHandler().playSound(new MachineSound(tile,id,soundIn,categoryIn,repeat,volume,pitch,xIn,yIn,zIn));
	}
}
