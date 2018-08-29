package teamroots.embers.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.power.IEmberCapability;

public class TileEntityEmberGauge extends TileEntityBaseGauge {
    @Override
    public int calculateComparatorValue(TileEntity tileEntity, EnumFacing facing) {
        if(tileEntity.hasCapability(EmbersCapabilities.EMBER_CAPABILITY,facing)) {
            IEmberCapability capability = tileEntity.getCapability(EmbersCapabilities.EMBER_CAPABILITY,facing);
            double fill = capability.getEmber() / capability.getEmberCapacity();
            return fill > 0 ? (int) (1 + fill * 14) : 0;
        }
        return 0;
    }
}
