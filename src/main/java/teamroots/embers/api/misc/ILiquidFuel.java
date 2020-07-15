package teamroots.embers.api.misc;

import net.minecraftforge.fluids.FluidStack;

import java.awt.*;

public interface ILiquidFuel {
    boolean matches(FluidStack stack);

    FluidStack getRemainder(FluidStack stack);

    double getPower(FluidStack stack);

    int getTime(FluidStack stack);

    Color getBurnColor(FluidStack stack);
}
