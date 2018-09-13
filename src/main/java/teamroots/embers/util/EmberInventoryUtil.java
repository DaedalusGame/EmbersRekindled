package teamroots.embers.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import teamroots.embers.ConfigManager;
import teamroots.embers.api.event.EmberRemoveEvent;
import teamroots.embers.compat.BaublesIntegration;
import teamroots.embers.api.item.IEmberItem;
import teamroots.embers.api.item.IHeldEmberCell;
import teamroots.embers.api.item.IInventoryEmberCell;

public class EmberInventoryUtil {
	public static double getEmberCapacityTotal(EntityPlayer player){
		double amount = 0;
		for (int i = 0; i < 36; i ++){
			if (!player.inventory.getStackInSlot(i).isEmpty() && player.inventory.getStackInSlot(i).getItem() instanceof IInventoryEmberCell && player.inventory.getStackInSlot(i).getItem() instanceof IEmberItem) {
				amount += ((IEmberItem) player.inventory.getStackInSlot(i).getItem()).getEmberCapacity(player.inventory.getStackInSlot(i));
			}
		}
		if (!player.getHeldItem(EnumHand.OFF_HAND).isEmpty() && player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof IHeldEmberCell && player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof IEmberItem) {
			amount += ((IEmberItem) player.getHeldItem(EnumHand.OFF_HAND).getItem()).getEmberCapacity(player.getHeldItem(EnumHand.OFF_HAND));
		}
		if (!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IHeldEmberCell && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IEmberItem) {
			amount += ((IEmberItem) player.getHeldItem(EnumHand.MAIN_HAND).getItem()).getEmberCapacity(player.getHeldItem(EnumHand.MAIN_HAND));
		}
		if (ConfigManager.isBaublesIntegrationEnabled()) {
			amount += BaublesIntegration.getEmberCapacityTotal(player);
		}
		return amount;
	}
	
	public static double getEmberTotal(EntityPlayer player){
		double amount = 0;
		for (int i = 0; i < 36; i ++){
			if (!player.inventory.getStackInSlot(i).isEmpty() && player.inventory.getStackInSlot(i).getItem() instanceof IInventoryEmberCell && player.inventory.getStackInSlot(i).getItem() instanceof IEmberItem) {
				amount += ((IEmberItem) player.inventory.getStackInSlot(i).getItem()).getEmber(player.inventory.getStackInSlot(i));
			}
		}
		if (!player.getHeldItem(EnumHand.OFF_HAND).isEmpty() && player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof IHeldEmberCell && player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof IEmberItem) {
			amount += ((IEmberItem) player.getHeldItem(EnumHand.OFF_HAND).getItem()).getEmber(player.getHeldItem(EnumHand.OFF_HAND));
		}
		if (!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IHeldEmberCell && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IEmberItem) {
			amount += ((IEmberItem) player.getHeldItem(EnumHand.MAIN_HAND).getItem()).getEmber(player.getHeldItem(EnumHand.MAIN_HAND));
		}
		if (ConfigManager.isBaublesIntegrationEnabled()) {
			amount += BaublesIntegration.getEmberTotal(player);
		}
		return amount;
	}
	
	public static void removeEmber(EntityPlayer player, double amount){
		EmberRemoveEvent event = new EmberRemoveEvent(player,amount);
		MinecraftForge.EVENT_BUS.post(event);
		double temp = event.getFinal();

		if (!player.getHeldItem(EnumHand.OFF_HAND).isEmpty() && player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof IHeldEmberCell && player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof IEmberItem) {
			temp -= ((IEmberItem) player.getHeldItem(EnumHand.OFF_HAND).getItem()).removeAmount(player.getHeldItem(EnumHand.OFF_HAND), temp, true);
		}
		if (!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IHeldEmberCell && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IEmberItem) {
			temp -= ((IEmberItem) player.getHeldItem(EnumHand.MAIN_HAND).getItem()).removeAmount(player.getHeldItem(EnumHand.MAIN_HAND), temp, true);
		}
		if (ConfigManager.isBaublesIntegrationEnabled()) {
			temp = BaublesIntegration.removeEmber(player, temp);
		}
		for (int i = 0; i < 36; i ++){
			if (!player.inventory.getStackInSlot(i).isEmpty() && player.inventory.getStackInSlot(i).getItem() instanceof IInventoryEmberCell && player.inventory.getStackInSlot(i).getItem() instanceof IEmberItem) {
				temp -= ((IEmberItem) player.inventory.getStackInSlot(i).getItem()).removeAmount(player.inventory.getStackInSlot(i), temp, true);
			}
		}
	}
}
