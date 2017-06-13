package teamroots.embers.itemmod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.RegistryManager;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessagePlayerJetFX;
import teamroots.embers.network.message.MessageRemovePlayerEmber;
import teamroots.embers.network.message.MessageSpawnEmberProj;
import teamroots.embers.network.message.MessageSuperheatFX;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.util.EmberInventoryUtil;
import teamroots.embers.util.ItemModUtil;
import teamroots.embers.util.Misc;

public class ModifierEldritchInsignia extends ModifierBase {

	public ModifierEldritchInsignia() {
		super(EnumType.ARMOR,"eldritch_insignia",0.0,true);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onEntityTarget(LivingSetAttackTargetEvent event){
		if (event.getTarget() instanceof EntityPlayer){
			int level = ItemModUtil.getArmorMod((EntityPlayer)event.getTarget(), ItemModUtil.modifierRegistry.get(RegistryManager.eldritch_insignia).name);
			if ((event.getEntityLiving().getLastDamageSource() == null 
					|| event.getEntityLiving().getLastDamageSource() != null && event.getEntityLiving().getLastDamageSource().getEntity() == null
					|| event.getEntityLiving().getLastDamageSource() != null && event.getEntityLiving().getLastDamageSource().getEntity() != null && event.getEntityLiving().getLastDamageSource().getEntity().getUniqueID().compareTo(event.getTarget().getUniqueID()) != 0
					) 
					&& event.getEntity().getEntityId() % (3+level) >= 2){
				if (level > 0 && !(event.getEntityLiving() instanceof EntityPlayer)/* || event.getEntityLiving() instanceof EntityPlayer && ((EntityPlayer)event.getEntityLiving()).getGameProfile().getName().compareToIgnoreCase("yrsegal") == 0*/){
					((EntityLiving)event.getEntityLiving()).setAttackTarget(null);
					//EmberInventoryUtil.removeEmber((EntityPlayer)event.getTarget(), cost);
				}
			}
		}
	}
}
