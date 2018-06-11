package teamroots.embers.item.bauble;

import baubles.api.BaubleType;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class BaublesUtil {
	public static NonNullList<ItemStack> getBaubles(IBaublesItemHandler items, BaubleType type){
		NonNullList<ItemStack> stacks = NonNullList.create();
		if (type == BaubleType.AMULET){
			stacks.add(items.getStackInSlot(0));
		}
		if (type == BaubleType.RING){
			stacks.add(items.getStackInSlot(1));
			stacks.add(items.getStackInSlot(2));
		}
		if (type == BaubleType.BELT){
			stacks.add(items.getStackInSlot(3));
		}
		if (type == BaubleType.HEAD){
			stacks.add(items.getStackInSlot(4));
		}
		if (type == BaubleType.BODY){
			stacks.add(items.getStackInSlot(5));
		}
		if (type == BaubleType.CHARM){
			stacks.add(items.getStackInSlot(6));
		}
		if (type == BaubleType.TRINKET){
			stacks.add(items.getStackInSlot(0));
			stacks.add(items.getStackInSlot(1));
			stacks.add(items.getStackInSlot(2));
			stacks.add(items.getStackInSlot(3));
			stacks.add(items.getStackInSlot(4));
			stacks.add(items.getStackInSlot(5));
			stacks.add(items.getStackInSlot(6));
		}
		return stacks;
	}
}
