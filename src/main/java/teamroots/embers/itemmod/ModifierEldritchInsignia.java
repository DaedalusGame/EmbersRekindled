package teamroots.embers.itemmod;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import teamroots.embers.RegistryManager;
import teamroots.embers.api.itemmod.ModifierBase;
import teamroots.embers.util.ItemModUtil;

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
					|| event.getEntityLiving().getLastDamageSource() != null && event.getEntityLiving().getLastDamageSource().getTrueSource() == null
					|| event.getEntityLiving().getLastDamageSource() != null && event.getEntityLiving().getLastDamageSource().getTrueSource() != null && event.getEntityLiving().getLastDamageSource().getTrueSource().getUniqueID().compareTo(event.getTarget().getUniqueID()) != 0
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
