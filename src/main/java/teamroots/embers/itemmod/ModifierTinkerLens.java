package teamroots.embers.itemmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.event.InfoGogglesEvent;
import teamroots.embers.api.item.IInfoGoggles;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.api.itemmod.ModifierBase;

public class ModifierTinkerLens extends ModifierBase {
    boolean inverted;

    public ModifierTinkerLens(String name,boolean inverted) {
        super(EnumType.HELMET, name, 0.0, false);
        MinecraftForge.EVENT_BUS.register(this);
        this.inverted = inverted;
    }

    @Override
    public boolean canApplyTo(ItemStack stack) {
        return super.canApplyTo(stack) && inverted == (stack.getItem() instanceof IInfoGoggles) && !hasTinkerLens(stack);
    }

    public static boolean hasTinkerLens(ItemStack stack) {
        return ItemModUtil.hasModifier(stack, EmbersAPI.TINKER_LENS) || ItemModUtil.hasModifier(stack, EmbersAPI.ANTI_TINKER_LENS);
    }

    @SubscribeEvent
    public void shouldShowInfo(InfoGogglesEvent event) {
        EntityPlayer player = event.getPlayer();
        int level = ItemModUtil.getArmorModifierLevel(player, this);
        if (level > 0)
            event.setShouldDisplay(!inverted);
    }
}
