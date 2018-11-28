package teamroots.embers.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import teamroots.embers.api.tile.IExtraDialInformation;
import teamroots.embers.block.BlockItemGauge;

public class TileEntityItemGauge extends TileEntityBaseGauge {
    @Override
    public int calculateComparatorValue(TileEntity tileEntity, EnumFacing facing) {
        int comparatorValue = 0;
        if(tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,facing)) {
            IItemHandler handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,facing);

            int i = 0;
            float f = 0.0F;

            for (int j = 0; j < handler.getSlots(); ++j)
            {
                ItemStack itemstack = handler.getStackInSlot(j);

                if (!itemstack.isEmpty())
                {
                    f += (float)itemstack.getCount() / (float)Math.min(handler.getSlotLimit(j), itemstack.getMaxStackSize());
                    ++i;
                }
            }

            f = f / (float)handler.getSlots();
            comparatorValue = MathHelper.floor(f * 14.0F) + (i > 0 ? 1 : 0);
        }
        if(tileEntity instanceof IExtraDialInformation)
            comparatorValue = ((IExtraDialInformation) tileEntity).getComparatorData(facing,comparatorValue,getDialType());
        return comparatorValue;
    }

    @Override
    public String getDialType() {
        return BlockItemGauge.DIAL_TYPE;
    }
}
