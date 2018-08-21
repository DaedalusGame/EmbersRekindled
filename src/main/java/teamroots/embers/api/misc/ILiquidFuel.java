package teamroots.embers.api.misc;

import net.minecraftforge.fluids.FluidStack;

public interface ILiquidFuel {
    boolean matches(FluidStack stack);

    FluidStack getRemainder(FluidStack stack);

    double getPower(FluidStack stack);
}
