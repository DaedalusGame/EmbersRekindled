package teamroots.embers.itemmod;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.api.itemmod.ModifierBase;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageCasterOrb;
import teamroots.embers.util.EmberInventoryUtil;

import java.util.HashMap;
import java.util.UUID;

public class ModifierCasterOrb extends ModifierBase {

	public ModifierCasterOrb() {
		super(EnumType.TOOL,"caster_orb",2.0,true);
		MinecraftForge.EVENT_BUS.register(this);
	}
	public static float prevCooledStrength = 0;
	public static float cooldownTicks = 0;
	public static HashMap<UUID,Float> cooldownTicksServer = new HashMap<>();

	public static void setCooldown(UUID uuid, float ticks) {
		cooldownTicksServer.put(uuid,ticks);
	}

	public static boolean hasCooldown(UUID uuid) {
		return cooldownTicksServer.getOrDefault(uuid,0.0f) > 0;
	}

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event){
		if (event.phase == TickEvent.Phase.START) {
			for (UUID uuid : cooldownTicksServer.keySet()) {
				Float ticks = cooldownTicksServer.get(uuid) - 1;
				cooldownTicksServer.put(uuid, ticks);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event){
		if (event.phase == TickEvent.Phase.START){
			if (Minecraft.getMinecraft().player != null){
				prevCooledStrength = Minecraft.getMinecraft().player.getCooledAttackStrength(0);
			}
			if (cooldownTicks > 0){
				cooldownTicks --;
			}
		}
	}
	
	@SubscribeEvent
	public void onSwing(PlayerInteractEvent.LeftClickBlock event){
		EntityPlayer player = event.getEntityPlayer();
		World world = event.getWorld();
		ItemStack heldStack = event.getItemStack();
		tryShoot(player, world, heldStack);
	}
	
	@SubscribeEvent
	public void onSwing(PlayerInteractEvent.LeftClickEmpty event){
		EntityPlayer player = event.getEntityPlayer();
		World world = event.getWorld();
		ItemStack heldStack = event.getItemStack();
		tryShoot(player, world, heldStack);
	}

	private void tryShoot(EntityPlayer player, World world, ItemStack heldStack) {
		if (prevCooledStrength == 1.0f){
			if (ItemModUtil.hasHeat(heldStack)){
				int level = ItemModUtil.getModifierLevel(heldStack, EmbersAPI.CASTER_ORB);
				if (world.isRemote && level > 0 && EmberInventoryUtil.getEmberTotal(player) > cost && cooldownTicks == 0){
					PacketHandler.INSTANCE.sendToServer(new MessageCasterOrb(player.getLookVec().x, player.getLookVec().y, player.getLookVec().z));
					cooldownTicks = 20;
				}
			}
		}
	}
}
