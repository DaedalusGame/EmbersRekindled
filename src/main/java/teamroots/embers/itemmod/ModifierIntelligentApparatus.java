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
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
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

public class ModifierIntelligentApparatus extends ModifierBase {

	public ModifierIntelligentApparatus() {
		super(EnumType.ARMOR,"intelligent_apparatus",4.0,true);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onXPDrop(LivingExperienceDropEvent event){
		if (event.getAttackingPlayer() != null){
			EntityPlayer player = (EntityPlayer)event.getAttackingPlayer();
			ItemStack s = player.getHeldItemMainhand();
			if (!s.isEmpty()){
				if (ItemModUtil.hasHeat(s)){
					int level = ItemModUtil.getModifierLevel(s, ItemModUtil.modifierRegistry.get(RegistryManager.superheater).name);
					if (level > 0 && EmberInventoryUtil.getEmberTotal(player) >= cost){
						EmberInventoryUtil.removeEmber(player, cost);
						event.setDroppedExperience(event.getDroppedExperience()*level);
					}
				}
			}
		}
	}
}
