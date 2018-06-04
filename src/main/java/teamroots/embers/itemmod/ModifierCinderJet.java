package teamroots.embers.itemmod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import teamroots.embers.RegistryManager;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessagePlayerJetFX;
import teamroots.embers.network.message.MessageSuperheatFX;
import teamroots.embers.util.EmberInventoryUtil;
import teamroots.embers.util.ItemModUtil;

public class ModifierCinderJet extends ModifierBase {

	public ModifierCinderJet() {
		super(EnumType.ARMOR,"jet_augment",2.0,true);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public static Map<UUID, Boolean> sprinting = new HashMap<UUID, Boolean>();
	
	public float addDash(ItemStack stack){
		if (ItemModUtil.hasHeat(stack)){
			int level = ItemModUtil.getModifierLevel(stack, ItemModUtil.modifierRegistry.get(RegistryManager.jet_augment).name);
			return (float)(0.5*(Math.atan(0.6*(level))/(1.25)));
		}
		return 0;
	}
	
	@SubscribeEvent
	public void onLivingTick(LivingUpdateEvent event){
		if (event.getEntity() instanceof EntityPlayer && !event.getEntity().world.isRemote){
			UUID id = event.getEntity().getUniqueID();
			if (sprinting.containsKey(id)){
				if (event.getEntity().isSprinting() && !sprinting.get(id)){
					int level = ItemModUtil.getArmorMod((EntityPlayer)event.getEntity(), ItemModUtil.modifierRegistry.get(RegistryManager.jet_augment).name);
					float dashStrength = (float)(2.0*(Math.atan(0.6*(level))/(1.25)));
					if (dashStrength > 0 && event.getEntityLiving().onGround && EmberInventoryUtil.getEmberTotal((EntityPlayer)event.getEntity()) > cost){
						EmberInventoryUtil.removeEmber(((EntityPlayer)event.getEntity()), cost);
						event.getEntityLiving().velocityChanged = true;
						event.getEntityLiving().motionX += 2.0*event.getEntityLiving().getLookVec().x*dashStrength;
						event.getEntityLiving().motionY += 0.4;
						event.getEntityLiving().motionZ += 2.0*event.getEntityLiving().getLookVec().z*dashStrength;
						if (!event.getEntity().getEntityWorld().isRemote){
							PacketHandler.INSTANCE.sendToAll(new MessagePlayerJetFX(event.getEntity().getUniqueID()));
						}
					}
				}
				sprinting.replace(id, event.getEntity().isSprinting());
			}
			else {
				sprinting.put(id, event.getEntity().isSprinting());
			}
		}
	}
	
}
