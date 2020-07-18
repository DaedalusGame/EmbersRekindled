package teamroots.embers.recipe;

import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.util.FluidUtil;

import java.awt.*;

public class FluidReactionRecipe {
    public FluidStack input;
    public FluidStack output;
    public Color color;

    public FluidReactionRecipe(FluidStack input, FluidStack output, Color color) {
        this.input = input;
        this.output = output;
        this.color = color;
    }

    public boolean matches(FluidStack test) {
        return test != null && FluidUtil.areFluidsEqual(test.getFluid(), input.getFluid()) && test.amount >= input.amount;
    }

    public int getTimes(FluidStack input, FluidStack output) {
        return Math.min(input.amount / this.input.amount, output.amount / this.output.amount);
    }

    public FluidStack getResult(FluidStack input) {
        FluidStack copy = output.copy();
        copy.amount *= input.amount;
        return copy;
    }

    public Color getColor() {
        return color;
    }

    public FluidStack getInput() {
        return input;
    }

    public FluidStack getOutput() {
        return output;
    }
}
