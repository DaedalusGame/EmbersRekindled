package teamroots.embers.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.Locale;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import teamroots.embers.ConfigManager;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;
import teamroots.embers.api.event.InfoGogglesEvent;
import teamroots.embers.api.item.IInfoGoggles;
import teamroots.embers.compat.BaublesIntegration;
import teamroots.embers.compat.MysticalMechanicsIntegration;
import teamroots.embers.model.ModelManager;
import teamroots.embers.particle.*;
import teamroots.embers.util.DecimalFormats;
import teamroots.embers.util.FluidColorHelper;
import teamroots.embers.util.sound.ItemUseSound;
import teamroots.embers.util.sound.MachineSound;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Pattern;

public class ClientProxy extends CommonProxy{

	public static ParticleRenderer particleRenderer = new ParticleRenderer();
	public static Random random = new Random();
	static int particleCounter;


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
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(new FluidColorHelper());
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event){
		super.postInit(event);
	}

	@Override
	public boolean isPlayerWearingGoggles() {
		EntityPlayer player = Minecraft.getMinecraft().player;

		boolean shouldDisplay = isGoggles(player, EntityEquipmentSlot.HEAD) || isGoggles(player, EntityEquipmentSlot.MAINHAND) || isGoggles(player, EntityEquipmentSlot.OFFHAND);
		InfoGogglesEvent event = new InfoGogglesEvent(player,shouldDisplay);
		MinecraftForge.EVENT_BUS.post(event);
		return event.shouldDisplay();
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().player;
	}

	private boolean isGoggles(EntityPlayer player, EntityEquipmentSlot slot) {
		ItemStack stack = player.getItemStackFromSlot(slot);
		Item item = stack.getItem();
		return item instanceof IInfoGoggles && ((IInfoGoggles) item).shouldDisplayInfo(player, stack, slot);
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

	//Particles
	@Override
	public void spawnParticleGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime) {
		particleCounter += random.nextInt(3);
		if (particleCounter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
			ClientProxy.particleRenderer.addParticle(new ParticleGlow(world, x, y, z, vx, vy, vz, r, g, b, a, scale, lifetime));
		}
	}

	@Override
	public void spawnParticleGlowThroughBlocks(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime) {
		particleCounter += random.nextInt(3);
		if (particleCounter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
			ClientProxy.particleRenderer.addParticle(new ParticleGlowThroughBlocks(world, x, y, z, vx, vy, vz, r, g, b, a, scale, lifetime));
		}
	}

	@Override
	public void spawnParticleGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float scale, int lifetime) {
		particleCounter += random.nextInt(3);
		if (particleCounter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
			ClientProxy.particleRenderer.addParticle(new ParticleGlow(world, x, y, z, vx, vy, vz, r, g, b, 1.0f, scale, lifetime));
		}
	}

	@Override
	public void spawnParticleLineGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float scale, int lifetime) {
		particleCounter += random.nextInt(3);
		if (particleCounter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
			ClientProxy.particleRenderer.addParticle(new ParticleLineGlow(world, x, y, z, vx, vy, vz, r, g, b, scale, lifetime));
		}
	}

	@Override
	public void spawnParticleTyrfing(World world, float x, float y, float z, float vx, float vy, float vz, float scale, int lifetime) {
		particleCounter += random.nextInt(3);
		if (particleCounter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
			ClientProxy.particleRenderer.addParticle(new ParticleTyrfing(world, x, y, z, vx, vy, vz, scale, lifetime));
		}
	}

	@Override
	public void spawnParticleStar(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float scale, int lifetime) {
		particleCounter += random.nextInt(3);
		if (particleCounter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
			ClientProxy.particleRenderer.addParticle(new ParticleStar(world, x, y, z, vx, vy, vz, r, g, b, scale, lifetime));
		}
	}

	@Override
	public void spawnParticleSpark(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float scale, int lifetime) {
		particleCounter += random.nextInt(3);
		if (particleCounter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
			ClientProxy.particleRenderer.addParticle(new ParticleSpark(world, x, y, z, vx, vy, vz, r, g, b, scale, lifetime));
		}
	}

	@Override
	public void spawnParticleSmoke(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime) {
		particleCounter += random.nextInt(3);
		if (particleCounter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
			ClientProxy.particleRenderer.addParticle(new ParticleSmoke(world, x, y, z, vx, vy, vz, r, g, b, a, scale, lifetime));
		}
	}

	@Override
	public void spawnParticleVapor(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scaleMin, float scaleMax, int lifetime) {
		particleCounter += random.nextInt(3);
		if (particleCounter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
			ClientProxy.particleRenderer.addParticle(new ParticleVapor(world, x, y, z, vx, vy, vz, r, g, b, a, scaleMin, scaleMax, lifetime));
		}
	}

	@Override
	public void spawnParticlePipeFlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime) {
		particleCounter += random.nextInt(3);
		if (particleCounter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
			ClientProxy.particleRenderer.addParticle(new ParticlePipeFlow(world, x, y, z, vx, vy, vz, r, g, b, a, scale, lifetime));
		}
	}

	@Override
	public void spawnParticleAsh(World world, Entity entity, int lifetime) {
		spawnParticleAsh(world,entity.getEntityBoundingBox(),lifetime);
	}

	@Override
	public void spawnParticleAsh(World world, AxisAlignedBB aabb, int lifetime) {
		ClientProxy.particleRenderer.addParticle(new ParticleAsh(world, aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, lifetime));
	}

	@Override
	public void spawnFireBlast(World world, double x, double y, double z, Color color, float scale, int lifetime)
	{
		ClientProxy.particleRenderer.addParticle(new ParticleFireBlast(world, x, y, z, color, scale, lifetime));
	}
}
