package teamroots.embers.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;
import teamroots.embers.RegistryManager;

public class EmbersFuelHandler implements IFuelHandler {

	@Override
	public int getBurnTime(ItemStack fuel) {
		if (fuel.getItem() == RegistryManager.dust_ash){
			return 200;
		}
		return 0;
	}
}
