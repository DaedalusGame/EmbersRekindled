package teamroots.embers.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import teamroots.embers.item.IEmberItem;
import teamroots.embers.item.IHeldEmberCell;
import teamroots.embers.item.IInventoryEmberCell;
import teamroots.embers.power.EmberCapabilityProvider;

public class EmberInventoryUtil {
	public static double getEmberCapacityTotal(EntityPlayer player){
		double amount = 0;
		for (int i = 0; i < 36; i ++){
			if (player.inventory.getStackInSlot(i) != ItemStack.EMPTY){
				if (player.inventory.getStackInSlot(i).getItem() instanceof IInventoryEmberCell && player.inventory.getStackInSlot(i).getItem() instanceof IEmberItem){
					amount += ((IEmberItem)player.inventory.getStackInSlot(i).getItem()).getEmber(player.inventory.getStackInSlot(i));
				}
			}
		}
		if (player.getHeldItem(EnumHand.OFF_HAND) != ItemStack.EMPTY){
			if (player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof IHeldEmberCell && player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof IEmberItem){
				amount += ((IEmberItem)player.getHeldItem(EnumHand.OFF_HAND).getItem()).getEmberCapacity(player.getHeldItem(EnumHand.OFF_HAND));
			}
		}
		if (player.getHeldItem(EnumHand.MAIN_HAND) != ItemStack.EMPTY){
			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IHeldEmberCell && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IEmberItem){
				amount += ((IEmberItem)player.getHeldItem(EnumHand.MAIN_HAND).getItem()).getEmberCapacity(player.getHeldItem(EnumHand.MAIN_HAND));
			}
		}
		return amount;
	}
	
	public static double getEmberTotal(EntityPlayer player){
		double amount = 0;
		for (int i = 0; i < 36; i ++){
			if (player.inventory.getStackInSlot(i) != ItemStack.EMPTY){
				if (player.inventory.getStackInSlot(i).getItem() instanceof IInventoryEmberCell && player.inventory.getStackInSlot(i).getItem() instanceof IEmberItem){
					amount += ((IEmberItem)player.inventory.getStackInSlot(i).getItem()).getEmber(player.inventory.getStackInSlot(i));
				}
			}
		}
		if (player.getHeldItem(EnumHand.OFF_HAND) != ItemStack.EMPTY){
			if (player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof IHeldEmberCell && player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof IEmberItem){
				amount += ((IEmberItem)player.getHeldItem(EnumHand.OFF_HAND).getItem()).getEmber(player.getHeldItem(EnumHand.OFF_HAND));
			}
		}
		if (player.getHeldItem(EnumHand.MAIN_HAND) != ItemStack.EMPTY){
			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IHeldEmberCell && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IEmberItem){
				amount += ((IEmberItem)player.getHeldItem(EnumHand.MAIN_HAND).getItem()).getEmber(player.getHeldItem(EnumHand.MAIN_HAND));
			}
		}
		return amount;
	}
	
	public static void removeEmber(EntityPlayer player, double amount){
		double temp = amount;
		
		if (player.getHeldItem(EnumHand.OFF_HAND) != ItemStack.EMPTY){
			if (player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof IHeldEmberCell && player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof IEmberItem){
				temp -= ((IEmberItem)player.getHeldItem(EnumHand.OFF_HAND).getItem()).removeAmount(player.getHeldItem(EnumHand.OFF_HAND),temp,true);
			}
		}
		if (player.getHeldItem(EnumHand.MAIN_HAND) != ItemStack.EMPTY){
			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IHeldEmberCell && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IEmberItem){
				temp -= ((IEmberItem)player.getHeldItem(EnumHand.MAIN_HAND).getItem()).removeAmount(player.getHeldItem(EnumHand.MAIN_HAND),temp,true);
			}
		}
		for (int i = 0; i < 36; i ++){
			if (player.inventory.getStackInSlot(i) != ItemStack.EMPTY){
				if (player.inventory.getStackInSlot(i).getItem() instanceof IInventoryEmberCell && player.inventory.getStackInSlot(i).getItem() instanceof IEmberItem){
					temp -= ((IEmberItem)player.inventory.getStackInSlot(i).getItem()).removeAmount(player.inventory.getStackInSlot(i),temp,true);
				}
			}
		}
	}
}
