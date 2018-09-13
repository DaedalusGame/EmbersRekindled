package teamroots.embers.item.bauble;

import baubles.api.BaubleType;
import baubles.api.cap.BaublesCapabilities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import teamroots.embers.RegistryManager;

public class ItemAshenAmulet extends ItemBaubleBase {
    public ItemAshenAmulet(String name, boolean addToTab) {
        super(name, BaubleType.AMULET, addToTab);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onMobDrop(LivingDropsEvent event) {
        EntityLivingBase killer = null;
        DamageSource source = event.getSource();

        if(source.getImmediateSource() instanceof EntityLivingBase)
            killer = (EntityLivingBase) source.getImmediateSource();
        else if(source.getTrueSource() instanceof EntityLivingBase)
            killer = (EntityLivingBase) source.getTrueSource();

        if (killer != null && killer.hasCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null)) {
            NonNullList<ItemStack> stacks = BaublesUtil.getBaubles(killer.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null), BaubleType.AMULET);
            if (stacks.get(0).getItem() == this) {
                event.getDrops().forEach(entity -> entity.setItem(new ItemStack(RegistryManager.dust_ash, entity.getItem().getCount())));
            }
        }
    }

    @SubscribeEvent
    public void onBlockDrop(BlockEvent.HarvestDropsEvent event) {
        EntityPlayer harvester = event.getHarvester();

        if (harvester != null && harvester.hasCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null)) {
            NonNullList<ItemStack> stacks = BaublesUtil.getBaubles(harvester.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null), BaubleType.AMULET);
            if (stacks.get(0).getItem() == this) {
                event.getDrops().replaceAll(stack -> new ItemStack(RegistryManager.dust_ash,stack.getCount()));}
        }
    }
}
