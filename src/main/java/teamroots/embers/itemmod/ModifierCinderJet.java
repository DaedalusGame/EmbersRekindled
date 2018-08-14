package teamroots.embers.itemmod;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import teamroots.embers.SoundManager;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.api.itemmod.ModifierBase;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessagePlayerJetFX;
import teamroots.embers.util.EmberInventoryUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ModifierCinderJet extends ModifierBase {

	public ModifierCinderJet() {
		super(EnumType.ARMOR,"jet_augment",2.0,true);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public static Map<UUID, Boolean> sprinting = new HashMap<UUID, Boolean>();
	
	public float addDash(ItemStack stack){
		if (ItemModUtil.hasHeat(stack)){
			int level = ItemModUtil.getModifierLevel(stack, EmbersAPI.JET_AUGMENT);
			return (float)(0.5*(Math.atan(0.6*(level))/(1.25)));
		}
		return 0;
	}
	
	@SubscribeEvent
	public void onLivingTick(LivingUpdateEvent event){
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.world;
		if (entity instanceof EntityPlayer && !world.isRemote){
			UUID id = entity.getUniqueID();
			if (sprinting.containsKey(id)){
				if (entity.isSprinting() && !sprinting.get(id)){
					int level = ItemModUtil.getArmorModifierLevel((EntityPlayer) entity, EmbersAPI.JET_AUGMENT);
					float dashStrength = (float)(2.0*(Math.atan(0.6*(level))/(1.25)));
					if (dashStrength > 0 && entity.onGround && EmberInventoryUtil.getEmberTotal((EntityPlayer) entity) > cost){
						EmberInventoryUtil.removeEmber(((EntityPlayer) entity), cost);
						entity.velocityChanged = true;
						entity.motionX += 2.0* entity.getLookVec().x*dashStrength;
						entity.motionY += 0.4;
						entity.motionZ += 2.0* entity.getLookVec().z*dashStrength;
						entity.getEntityWorld().playSound(null,entity.posX, entity.posY, entity.posZ, SoundManager.CINDER_JET, SoundCategory.PLAYERS, 1.0f, 1.0f);
						if (!entity.getEntityWorld().isRemote){
							PacketHandler.INSTANCE.sendToAll(new MessagePlayerJetFX(entity.getUniqueID()));
						}
					}
				}
				sprinting.replace(id, entity.isSprinting());
			}
			else {
				sprinting.put(id, entity.isSprinting());
			}
		}
	}
	
}
