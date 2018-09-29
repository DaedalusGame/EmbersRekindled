package teamroots.embers.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import teamroots.embers.api.item.IInfoGoggles;

public class ItemTinkerLens extends ItemBase implements IInfoGoggles {
    public ItemTinkerLens(String name, boolean addToTab) {
        super(name, addToTab);
    }

    @Override
    public boolean shouldDisplayInfo(EntityPlayer player, ItemStack stack, EntityEquipmentSlot slot) {
        return true;
    }
}
