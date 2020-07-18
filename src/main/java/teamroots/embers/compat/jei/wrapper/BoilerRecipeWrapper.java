package teamroots.embers.compat.jei.wrapper;

import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.api.misc.ILiquidFuel;

import java.util.List;

public class BoilerRecipeWrapper extends FluidRecipeWrapper {
    ILiquidFuel fuelHandler;
    FluidStack input;

    public BoilerRecipeWrapper(ILiquidFuel fuelHandler, FluidStack input) {
        this.fuelHandler = fuelHandler;
        this.input = input;
    }

    @Override
    public FluidStack getInput() {
        return input;
    }

    @Override
    public FluidStack getOutput() {
        return fuelHandler.getRemainder(input);
    }

    @Override
    public void addInfo(List<String> tooltip) {
        fuelHandler.addInfo(input, tooltip);
    }
}
