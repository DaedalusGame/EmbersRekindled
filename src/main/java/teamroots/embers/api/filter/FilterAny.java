package teamroots.embers.api.filter;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class FilterAny implements IFilter {
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation("embers", "any");

    @Override
    public ResourceLocation getType() {
        return RESOURCE_LOCATION;
    }

    @Override
    public boolean acceptsItem(ItemStack stack) {
        return true;
    }

    @Override
    public String formatFilter() {
        return I18n.format("embers.filter.any");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("type",getType().toString());
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
    }
}
