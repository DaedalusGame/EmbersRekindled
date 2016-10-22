package teamroots.embers.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import teamroots.embers.RegistryManager;
import teamroots.embers.item.IHeldEmberCell;
import teamroots.embers.item.IInventoryEmberCell;
import teamroots.embers.power.EmberCapabilityProvider;

public class EmberInventoryUtil {
	public static double getEmberCapacityTotal(EntityPlayer player){
		double amount = 0;
		for (int i = 0; i < 36; i ++){
			if (player.inventory.getStackInSlot(i) != null){
				if (player.inventory.getStackInSlot(i).getItem() instanceof IInventoryEmberCell){
					amount += player.inventory.getStackInSlot(i).getCapability(EmberCapabilityProvider.emberCapability, null).getEmberCapacity();
				}
			}
		}
		if (player.getHeldItem(EnumHand.OFF_HAND) != null){
			if (player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof IHeldEmberCell){
				amount += player.getHeldItem(EnumHand.OFF_HAND).getCapability(EmberCapabilityProvider.emberCapability, null).getEmberCapacity();
			}
		}
		if (player.getHeldItem(EnumHand.MAIN_HAND) != null){
			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IHeldEmberCell){
				amount += player.getHeldItem(EnumHand.MAIN_HAND).getCapability(EmberCapabilityProvider.emberCapability, null).getEmberCapacity();
			}
		}
		return amount;
	}
	
	public static double getEmberTotal(EntityPlayer player){
		double amount = 0;
		for (int i = 0; i < 36; i ++){
			if (player.inventory.getStackInSlot(i) != null){
				if (player.inventory.getStackInSlot(i).getItem() instanceof IInventoryEmberCell){
					amount += player.inventory.getStackInSlot(i).getCapability(EmberCapabilityProvider.emberCapability, null).getEmber();
				}
			}
		}
		if (player.getHeldItem(EnumHand.OFF_HAND) != null){
			if (player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof IHeldEmberCell){
				amount += player.getHeldItem(EnumHand.OFF_HAND).getCapability(EmberCapabilityProvider.emberCapability, null).getEmber();
			}
		}
		if (player.getHeldItem(EnumHand.MAIN_HAND) != null){
			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IHeldEmberCell){
				amount += player.getHeldItem(EnumHand.MAIN_HAND).getCapability(EmberCapabilityProvider.emberCapability, null).getEmber();
			}
		}
		return amount;
	}
	
	public static void removeEmber(EntityPlayer player, double amount){
		double temp = amount;

		if (player.getHeldItem(EnumHand.OFF_HAND) != null && temp > 0){
			if (player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof IHeldEmberCell){
				temp -= player.getHeldItem(EnumHand.OFF_HAND).getCapability(EmberCapabilityProvider.emberCapability, null).removeAmount(temp, true);
			}
		}
		if (player.getHeldItem(EnumHand.MAIN_HAND) != null && temp > 0){
			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IHeldEmberCell){
				temp -= player.getHeldItem(EnumHand.MAIN_HAND).getCapability(EmberCapabilityProvider.emberCapability, null).removeAmount(temp, true);
			}
		}
		for (int i = 0; i < 36 && temp > 0; i ++){
			if (player.inventory.getStackInSlot(i) != null){
				if (player.inventory.getStackInSlot(i).getItem() instanceof IInventoryEmberCell){
					temp -= player.inventory.getStackInSlot(i).getCapability(EmberCapabilityProvider.emberCapability, null).removeAmount(temp, true);
				}
			}
		}
	}
}
