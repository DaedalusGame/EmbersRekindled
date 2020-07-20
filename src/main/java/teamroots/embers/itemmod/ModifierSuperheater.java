package teamroots.embers.itemmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import teamroots.embers.SoundManager;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.api.itemmod.ModifierBase;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageSuperheatFX;
import teamroots.embers.util.EmberInventoryUtil;

import java.util.List;

public class ModifierSuperheater extends ModifierBase {

	public ModifierSuperheater() {
		super(EnumType.TOOL,"superheater",2.0,true);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onDrops(HarvestDropsEvent event){
		if (event.getHarvester() instanceof EntityPlayer){
			if (!event.getHarvester().getHeldItem(EnumHand.MAIN_HAND).isEmpty()){
				ItemStack s = event.getHarvester().getHeldItem(EnumHand.MAIN_HAND);
				if (ItemModUtil.hasHeat(s)){
					if (ItemModUtil.getModifierLevel(s, EmbersAPI.SUPERHEATER) > 0 && EmberInventoryUtil.getEmberTotal(event.getHarvester()) >= cost){
						if (!event.getWorld().isRemote){
							PacketHandler.INSTANCE.sendToAll(new MessageSuperheatFX(event.getPos().getX()+0.5,event.getPos().getY()+0.5,event.getPos().getZ()+0.5));
						}
						event.getWorld().playSound(null,event.getPos(),SoundManager.FIREBALL_HIT, SoundCategory.PLAYERS, 0.5f, event.getWorld().rand.nextFloat()*0.5f + 0.2f);
						EmberInventoryUtil.removeEmber(event.getHarvester(), cost);
						List<ItemStack> stacks = event.getDrops();
						for (int i = 0; i < stacks.size(); i ++){
							ItemStack stack = FurnaceRecipes.instance().getSmeltingResult(stacks.get(i)).copy();
							if (!stack.isEmpty()){
								stacks.add(stack);
								stacks.set(i, ItemStack.EMPTY);
							}
						}
					}
				}
			}
		}
	}

	private double getBurnBonus(double resonance) {
		if(resonance > 1)
			return 1 + (resonance - 1) * 0.5;
		else
			return resonance;
	}

	private double getDamageBonus(double resonance) {
		if(resonance > 1)
			return 1 + (resonance - 1) * 1.0;
		else
			return resonance;
	}
	
	@SubscribeEvent
	public void onHit(LivingHurtEvent event){
		if (event.getSource().getTrueSource() instanceof EntityPlayer){
			EntityPlayer damager = (EntityPlayer)event.getSource().getTrueSource();
			ItemStack s = damager.getHeldItemMainhand();
			if (!s.isEmpty()){
				if (ItemModUtil.hasHeat(s)){
					int level = ItemModUtil.getModifierLevel(s, EmbersAPI.SUPERHEATER);
					if (level > 0 && EmberInventoryUtil.getEmberTotal(damager) >= cost){
						double resonance = EmbersAPI.getEmberEfficiency(s);

						int burnTime = (int) (Math.pow(2, level - 1) * 5 * getBurnBonus(resonance));
						float extraDamage = (float) (level * getDamageBonus(resonance));

						event.getEntityLiving().setFire(burnTime);
						if (!damager.world.isRemote){
							PacketHandler.INSTANCE.sendToAll(new MessageSuperheatFX(event.getEntity().posX,event.getEntity().posY+event.getEntity().height/2.0,event.getEntity().posZ));
						}
						EmberInventoryUtil.removeEmber(damager, cost);
						event.setAmount(event.getAmount() + extraDamage);
					}
				}
			}
		}
	}
	
}
