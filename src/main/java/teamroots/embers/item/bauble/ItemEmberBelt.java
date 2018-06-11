package teamroots.embers.item.bauble;

import baubles.api.BaubleType;
import baubles.api.cap.BaublesCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import teamroots.embers.event.EmberRemoveEvent;

public class ItemEmberBelt extends ItemBaubleBase {

	public ItemEmberBelt(String name, boolean addToTab) {
		super(name, BaubleType.BELT, addToTab);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onTake(EmberRemoveEvent event){
		if (event.getPlayer().hasCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null)){
			NonNullList<ItemStack> stacks = BaublesUtil.getBaubles(event.getPlayer().getCapability(BaublesCapabilities.CAPABILITY_BAUBLES,null), BaubleType.BELT);
			if (stacks.get(0).getItem() == this){
				event.addReduction(0.25);
			}
		}
	}
	
}
