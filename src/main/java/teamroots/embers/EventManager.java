package teamroots.embers;

import java.util.List;
import java.util.Random;

import javax.swing.colorchooser.ColorSelectionModel;

import org.fusesource.jansi.Ansi.Color;
import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.terraingen.ChunkGeneratorEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.block.IDial;
import teamroots.embers.item.ItemEmberGauge;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageEmberData;
import teamroots.embers.network.message.MessageEmberDataRequest;
import teamroots.embers.network.message.MessageEmberGeneration;
import teamroots.embers.proxy.ClientProxy;
import teamroots.embers.util.FluidTextureUtil;
import teamroots.embers.util.Misc;
import teamroots.embers.util.RenderUtil;
import teamroots.embers.util.Vec2i;
import teamroots.embers.world.EmberWorldData;

public class EventManager {
	double gaugeAngle = 0;
	public static boolean hasRenderedParticles = false;
	Random random = new Random();
	
	static EntityPlayer clientPlayer = null;
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onTextureStitchPre(TextureStitchEvent.Pre event){
		FluidTextureUtil.initTextures(event.getMap());
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onTextureStitch(TextureStitchEvent event){
		ResourceLocation particleGlow = new ResourceLocation("embers:entity/particleMote");
		event.getMap().registerSprite(particleGlow);
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event){
		EmberWorldData.get(event.getWorld());
	}
	
	@SubscribeEvent
	public void onChunkGeneration(ChunkEvent.Load event){
		EmberWorldData data = EmberWorldData.get(event.getWorld());
		if (random.nextInt(20) == 0 && !event.getWorld().isRemote){
			if (!data.emberData.containsKey(""+event.getChunk().xPosition+" "+event.getChunk().zPosition)){
				Biome biome = event.getWorld().getBiomeProvider().getBiomeGenerator(new BlockPos(event.getChunk().xPosition*16,64,event.getChunk().zPosition*16));
				int baseAmount = 8000;
				int bonusAmount = 8000;
				double mult = 1;
				if (Misc.isHills(biome)){
					mult = 2;
				}
				if (Misc.isExtremeHills(biome)){
					mult = 4;
				}
				baseAmount *= mult;
				bonusAmount *= mult;
				double value = 4.0*(baseAmount+random.nextDouble()*bonusAmount);
				data.emberData.put(""+event.getChunk().xPosition+" "+event.getChunk().zPosition, value);
				data.markDirty();
				PacketHandler.INSTANCE.sendToAll(new MessageEmberGeneration(""+event.getChunk().xPosition+" "+event.getChunk().zPosition, value));
			}
		}
		else {
			if (!data.emberData.containsKey(""+event.getChunk().xPosition+" "+event.getChunk().zPosition)){
				data.emberData.put(""+event.getChunk().xPosition+" "+event.getChunk().zPosition, 0.0);
				data.markDirty();
				PacketHandler.INSTANCE.sendToAll(new MessageEmberGeneration(""+event.getChunk().xPosition+" "+event.getChunk().zPosition, 0.0));
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPlayerLogin(EntityJoinWorldEvent event){
		if (event.getEntity() instanceof EntityPlayer && event.getWorld().isRemote){
			PacketHandler.INSTANCE.sendToServer(new MessageEmberDataRequest(((EntityPlayer)event.getEntity()).getUniqueID()));
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGameOverlayRender(RenderGameOverlayEvent.Post e){
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		boolean showBar = false;

		int w = e.getResolution().getScaledWidth();
		int h = e.getResolution().getScaledHeight();
		
		int x = w/2;
		int y = h/2 - 20;
		if (player.getHeldItemMainhand() != null){
			if (player.getHeldItemMainhand().getItem() instanceof ItemEmberGauge){
				showBar = true;
			}
		}
		if (player.getHeldItemOffhand() != null){
			if (player.getHeldItemOffhand().getItem() instanceof ItemEmberGauge){
				showBar = true;
			}
		}
		if (showBar){
			World world = player.getEntityWorld();
			if (e.getType() == ElementType.TEXT){
				GlStateManager.disableDepth();
				GlStateManager.disableCull();
				GlStateManager.pushMatrix();
				Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("embers:textures/gui/emberMeterOverlay.png"));
				
				Tessellator tess = Tessellator.getInstance();
				VertexBuffer b = tess.getBuffer();
				GlStateManager.color(1f, 1f, 1f, 1f);
				
				int offsetX = 0;
				
				b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				RenderUtil.drawQuadGui(b, x-16, y+16, x+16, y+16, x+16, y-16, x-16, y-16, 0, 0, 1, 1);
				tess.draw();
				
				double angle = 195.0;
				EmberWorldData data = EmberWorldData.get(world);
				if (data != null && player != null){
					if (data.emberData != null){
						if (data.emberData.containsKey(""+((int)player.posX) / 16 + " " + ((int)player.posZ) / 16)){
							double value = data.emberData.get(""+((int)player.posX) / 16 + " " + ((int)player.posZ) / 16);
							double ratio = value/256000.0;
							if (gaugeAngle == 0){
								gaugeAngle = 165.0+210.0*ratio;
							}
							else {
								gaugeAngle = gaugeAngle*0.99+0.01*(165.0+210.0*ratio);
							}
						}
					}
				}
				
				Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("embers:textures/gui/emberMeterPointer.png"));
				GlStateManager.translate(x, y, 0);
				GlStateManager.rotate((float)gaugeAngle, 0, 0, 1);
				b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				RenderUtil.drawQuadGui(b, -2.5f, 13.5f, 13.5f, 13.5f, 13.5f, -2.5f, -2.5f, -2.5f, 0, 0, 1, 1);
				tess.draw();
				
				GlStateManager.popMatrix();
				GlStateManager.enableCull();
				GlStateManager.enableDepth();
			}
		}
		World world = player.getEntityWorld();
		
		RayTraceResult result = player.rayTrace(4.0, e.getPartialTicks());
		if (result.typeOfHit == RayTraceResult.Type.BLOCK){
			IBlockState state = world.getBlockState(result.getBlockPos());
			if (state.getBlock() instanceof IDial){
				List<String> text = ((IDial)state.getBlock()).getDisplayInfo(world, result.getBlockPos(), state);
				for (int i = 0; i < text.size(); i ++){
					Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text.get(i), x-Minecraft.getMinecraft().fontRendererObj.getStringWidth(text.get(i))/2, y+40+11*i, 0xFFFFFF);
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onTick(TickEvent.ClientTickEvent event){
		if (event.side == Side.CLIENT){
			((ClientProxy)Embers.proxy).particleRenderer.updateParticles();
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRenderAfterWorld(RenderWorldLastEvent event){
		if (Embers.proxy instanceof ClientProxy){
			((ClientProxy)Embers.proxy).particleRenderer.renderParticles(clientPlayer, event.getPartialTicks());
		}
	}
}
