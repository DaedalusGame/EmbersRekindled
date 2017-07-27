package teamroots.embers.itemmod;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import teamroots.embers.RegistryManager;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageSuperheatFX;
import teamroots.embers.util.EmberInventoryUtil;
import teamroots.embers.util.ItemModUtil;

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
					if (ItemModUtil.getModifierLevel(s, ItemModUtil.modifierRegistry.get(RegistryManager.superheater).name) > 0 && EmberInventoryUtil.getEmberTotal(event.getHarvester()) >= cost){
						if (!event.getWorld().isRemote){
							PacketHandler.INSTANCE.sendToAll(new MessageSuperheatFX(event.getPos().getX()+0.5,event.getPos().getY()+0.5,event.getPos().getZ()+0.5));
						}
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
	
	@SubscribeEvent
	public void onHit(LivingHurtEvent event){
		if (event.getSource().getTrueSource() instanceof EntityPlayer){
			EntityPlayer damager = (EntityPlayer)event.getSource().getTrueSource();
			ItemStack s = damager.getHeldItemMainhand();
			if (!s.isEmpty()){
				if (ItemModUtil.hasHeat(s)){
					int superheatLevel = ItemModUtil.getModifierLevel(s, ItemModUtil.modifierRegistry.get(RegistryManager.superheater).name);
					if (superheatLevel > 0 && EmberInventoryUtil.getEmberTotal(damager) >= cost){
						event.getEntityLiving().setFire(1);
						if (!damager.world.isRemote){
							PacketHandler.INSTANCE.sendToAll(new MessageSuperheatFX(event.getEntity().posX,event.getEntity().posY+event.getEntity().height/2.0,event.getEntity().posZ));
						}
						EmberInventoryUtil.removeEmber(damager, cost);
						event.setAmount(event.getAmount()+superheatLevel);
					}
				}
			}
		}
	}
	
}
