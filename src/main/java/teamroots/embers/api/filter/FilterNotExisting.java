package teamroots.embers.api.filter;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

public class FilterNotExisting extends FilterExisting {
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation("embers", "not_existing");

    @Override
    public ResourceLocation getType() {
        return RESOURCE_LOCATION;
    }

    @Override
    public boolean acceptsItem(ItemStack stack, IItemHandler itemHandler) {
        return !super.acceptsItem(stack, itemHandler);
    }

    @Override
    public String formatFilter() {
        return I18n.format("embers.filter.not_existing");
    }
}
