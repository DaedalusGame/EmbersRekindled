package teamroots.embers.item.bauble;

import baubles.api.BaubleType;
import baubles.api.cap.BaublesCapabilities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import teamroots.embers.RegistryManager;

public class ItemNonbelieverAmulet extends ItemBaubleBase {
    public ItemNonbelieverAmulet(String name, boolean addToTab) {
        super(name, BaubleType.AMULET, addToTab);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onDamage(LivingDamageEvent event) {
        EntityLivingBase killer = event.getEntityLiving();
        DamageSource source = event.getSource();

        if(!source.isMagicDamage())
            return;

        if (killer.hasCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null)) {
            NonNullList<ItemStack> stacks = BaublesUtil.getBaubles(killer.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null), BaubleType.AMULET);
            if (stacks.get(0).getItem() == this) {
                if(event.getAmount() > 0.5f)
                    event.setAmount(Math.max(event.getAmount()*0.1f,0.5f));
            }
        }
    }
}
