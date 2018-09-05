package teamroots.embers.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.api.tile.IExtraDialInformation;
import teamroots.embers.block.BlockFluidGauge;

public class TileEntityFluidGauge extends TileEntityBaseGauge {
    @Override
    public int calculateComparatorValue(TileEntity tileEntity, EnumFacing facing) {
        int comparatorValue = 0;
        if(tileEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,facing)) {
            IFluidHandler handler = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,facing);
            int totalCapacity = 0;
            int totalFluid = 0;
            for (IFluidTankProperties property : handler.getTankProperties()) {
                FluidStack contents = property.getContents();
                totalCapacity += property.getCapacity();
                if(contents != null)
                    totalFluid += contents.amount;
            }
            double fill = totalFluid / (double)totalCapacity;
            comparatorValue = fill > 0 ? (int) (1 + fill * 14) : 0;
        }
        if(tileEntity instanceof IExtraDialInformation)
            comparatorValue = ((IExtraDialInformation) tileEntity).getComparatorData(facing,comparatorValue,getDialType());
        return comparatorValue;
    }

    @Override
    public String getDialType() {
        return BlockFluidGauge.DIAL_TYPE;
    }
}
