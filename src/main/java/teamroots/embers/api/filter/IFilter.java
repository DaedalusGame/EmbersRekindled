package teamroots.embers.api.filter;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

public interface IFilter {
    ResourceLocation getType();

    boolean acceptsItem(ItemStack stack);

    default boolean acceptsItem(ItemStack stack, IItemHandler handler) {
        return acceptsItem(stack);
    }

    String formatFilter();

    NBTTagCompound writeToNBT(NBTTagCompound tag);

    void readFromNBT(NBTTagCompound tag);
}
