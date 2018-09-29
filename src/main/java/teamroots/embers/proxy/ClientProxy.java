package teamroots.embers.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.Locale;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import teamroots.embers.ConfigManager;
import teamroots.embers.RegistryManager;
import teamroots.embers.api.item.IInfoGoggles;
import teamroots.embers.compat.BaublesIntegration;
import teamroots.embers.compat.MysticalMechanicsIntegration;
import teamroots.embers.model.ModelManager;
import teamroots.embers.particle.ParticleRenderer;
import teamroots.embers.util.DecimalFormats;
import teamroots.embers.util.sound.ItemUseSound;
import teamroots.embers.util.sound.MachineSound;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

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

		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(new DecimalFormats());
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event){
		super.postInit(event);
	}

	@Override
	public boolean isPlayerWearingGoggles() {
		EntityPlayer player = Minecraft.getMinecraft().player;

		ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		Item helmetItem = helmet.getItem();
		if(helmetItem instanceof IInfoGoggles && ((IInfoGoggles) helmetItem).shouldDisplayInfo(player,helmet))
			return true;
		//TODO: modifier
		return false;
	}

	@Override
	public void playItemSound(EntityLivingBase entity, Item item, SoundEvent soundIn, SoundCategory categoryIn, boolean repeat, float volume, float pitch) {
		Minecraft.getMinecraft().getSoundHandler().playSound(new ItemUseSound(entity,item,soundIn,categoryIn,repeat,volume,pitch));
	}

	@Override
	public void playMachineSound(TileEntity tile, int id, SoundEvent soundIn, SoundCategory categoryIn, boolean repeat, float volume, float pitch, float xIn, float yIn, float zIn) {
		Minecraft.getMinecraft().getSoundHandler().playSound(new MachineSound(tile,id,soundIn,categoryIn,repeat,volume,pitch,xIn,yIn,zIn));
	}

	@Override
	public DecimalFormat getDecimalFormat(String key) {
		return DecimalFormats.getDecimalFormat(key);
	}

	@Override
	public String formatLocalize(String translationKey, Object... parameters) {
		return I18n.format(translationKey,parameters);
	}
}
