package teamroots.embers.item.bauble;

import baubles.api.BaubleType;
import baubles.api.cap.BaublesCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemDawnstoneMail extends ItemBaubleBase {
    public ItemDawnstoneMail(String name, boolean addToTab) {
        super(name, BaubleType.BODY, addToTab);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onKnockback(LivingKnockBackEvent event) {
        if (event.getEntityLiving().hasCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null)) {
            NonNullList<ItemStack> stacks = BaublesUtil.getBaubles(event.getEntityLiving().getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null), BaubleType.BODY);
            if (stacks.get(0).getItem() == this) {
                event.setStrength(0);
            }
        }
    }
}
