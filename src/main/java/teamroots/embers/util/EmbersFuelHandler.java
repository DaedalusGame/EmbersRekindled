package teamroots.embers.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import teamroots.embers.RegistryManager;

public class EmbersFuelHandler {
	@SubscribeEvent
	public static void handleFuel(FurnaceFuelBurnTimeEvent event) {
		ItemStack stack = event.getItemStack();
		if (stack.getItem() == RegistryManager.dust_ash)
			event.setBurnTime(200);
		if (stack.getItem() == RegistryManager.dust_ember)
			event.setBurnTime(1600);
	}
}
