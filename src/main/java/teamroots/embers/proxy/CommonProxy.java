package teamroots.embers.proxy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;
import teamroots.embers.apiimpl.EmbersAPIImpl;
import teamroots.embers.compat.BaublesIntegration;
import teamroots.embers.compat.MysticalMechanicsIntegration;
import teamroots.embers.compat.thaumcraft.ThaumcraftIntegration;
import teamroots.embers.gui.GuiHandler;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.recipe.RecipeRegistry;
import teamroots.embers.reflection.Fields;
import teamroots.embers.research.ResearchManager;
import teamroots.embers.tileentity.*;
import teamroots.embers.util.CompatUtil;
import teamroots.embers.util.OreTransmutationUtil;

import java.awt.*;
import java.text.DecimalFormat;

public class CommonProxy {
	public void constructing(FMLConstructionEvent event){
		EmbersAPIImpl.init();
	}

	public void preInit(FMLPreInitializationEvent event){
		Fields.init();
		PacketHandler.registerMessages();
		RegistryManager.registerAll();
		if(Loader.isModLoaded("thaumcraft"))
			MinecraftForge.EVENT_BUS.register(ThaumcraftIntegration.class);
	}
	
	public void init(FMLInitializationEvent event){
		if(CompatUtil.isBaublesIntegrationEnabled())
			BaublesIntegration.init();
		if(CompatUtil.isMysticalMechanicsIntegrationEnabled())
			MysticalMechanicsIntegration.init();
	}
	
	public void postInit(FMLPostInitializationEvent event){
		TileEntityMechAccessor.registerAccessibleTile(TileEntityMechCore.class);
		TileEntityMechAccessor.registerAccessibleTile(TileEntityMixerBottom.class);
		TileEntityMechAccessor.registerAccessibleTile(TileEntityMixerTop.class);
		TileEntityMechAccessor.registerAccessibleTile(TileEntityActivatorBottom.class);
		TileEntityMechAccessor.registerAccessibleTile(TileEntityFurnaceBottom.class);
		TileEntityMechAccessor.registerAccessibleTile(TileEntityFurnaceTop.class);
		TileEntityMechAccessor.registerAccessibleTile(TileEntityBoilerBottom.class);
		TileEntityMechAccessor.registerAccessibleTile(TileEntityReactor.class);

		ResearchManager.initResearches();
		NetworkRegistry.INSTANCE.registerGuiHandler(Embers.instance, new GuiHandler());
		RecipeRegistry.mergeOreRecipes();

		OreTransmutationUtil.init(); //oof
	}

	public EntityPlayer getClientPlayer() {
		return null;
	}

	public boolean isPlayerWearingGoggles() {
		return false;
	}

	public DecimalFormat getDecimalFormat(String key) { return null; }

    public String formatLocalize(String translationKey, Object... parameters) {
		return null;
	}

	public void playItemSound(EntityLivingBase entity, Item item, SoundEvent soundIn, SoundCategory categoryIn, boolean repeat, float volume, float pitch) {
		//NOOP
	}

	public void playMachineSound(TileEntity tile, int id, SoundEvent soundIn, SoundCategory categoryIn, boolean repeat, float volume, float pitch, float xIn, float yIn, float zIn) {
		//NOOP
	}

	//Particles
	public void spawnParticleGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime) {
		//NOOP
	}

	public void spawnParticleGlowThroughBlocks(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime) {
		//NOOP
	}

	public void spawnParticleGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float scale, int lifetime) {
		//NOOP
	}

	public void spawnParticleLineGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float scale, int lifetime) {
		//NOOP
	}

	public void spawnParticleTyrfing(World world, float x, float y, float z, float vx, float vy, float vz, float scale, int lifetime) {
		//NOOP
	}

	public void spawnParticleStar(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float scale, int lifetime) {
		//NOOP
	}

	public void spawnParticleSpark(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float scale, int lifetime) {
		//NOOP
	}

	public void spawnParticleSmoke(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime) {
		//NOOP
	}

	public void spawnParticleVapor(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scaleMin, float scaleMax, int lifetime) {
		//NOOP
	}

	public void spawnParticlePipeFlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime) {
		//NOOP
	}

	public void spawnParticleAsh(World world, Entity entity, int lifetime) {
		//NOOP
	}

	public void spawnParticleAsh(World world, AxisAlignedBB aabb, int lifetime) {
		//NOOP
	}

	public void spawnFireBlast(World world, double x, double y, double z, Color color, float scale, int lifetime) {
		//NOOP
	}
}
