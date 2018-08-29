package teamroots.embers.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.power.IEmberCapability;

public class TileEntityFluidGauge extends TileEntityBaseGauge {
    @Override
    public int calculateComparatorValue(TileEntity tileEntity, EnumFacing facing) {
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
            return fill > 0 ? (int) (1 + fill * 14) : 0;
        }
        return 0;
    }
}
