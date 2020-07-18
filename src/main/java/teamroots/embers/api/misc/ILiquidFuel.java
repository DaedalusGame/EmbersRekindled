package teamroots.embers.api.misc;

import net.minecraftforge.fluids.FluidStack;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

public interface ILiquidFuel {
    default List<FluidStack> getMatchingFluids() {
        return Collections.emptyList();
    }

    default void addInfo(FluidStack input, List<String> tooltip) {
        //NOOP
    }

    boolean matches(FluidStack stack);

    FluidStack getRemainder(FluidStack stack);

    double getPower(FluidStack stack);

    int getTime(FluidStack stack);

    Color getBurnColor(FluidStack stack);
}
