package teamroots.embers.recipe;

import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.util.FluidUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class FluidMixingRecipe {
	public ArrayList<FluidStack> inputs = new ArrayList<>();
	public FluidStack output;

	double powerRatio = 0.5;

	public FluidMixingRecipe(FluidStack[] inputs, FluidStack output){
		Collections.addAll(this.inputs, inputs);
		this.output = output;
	}

	public FluidMixingRecipe(FluidStack[] inputs, FluidStack output, double powerRatio) {
		this(inputs, output);
		this.powerRatio = powerRatio;
	}

	public double getPowerRatio() {
		return powerRatio;
	}

	public FluidStack getResult(ArrayList<FluidStack> fluids){
		return output.copy();
	}
	
	public boolean matches(ArrayList<FluidStack> test){
		ArrayList<FluidStack> checkInputs = new ArrayList<>(inputs);
		for (FluidStack aTest : test) {
			boolean doContinue = true;
			for (int j = 0; j < checkInputs.size() && doContinue; j++) {
				if (FluidUtil.areFluidsEqual(aTest.getFluid(),checkInputs.get(j).getFluid()) && aTest.amount >= checkInputs.get(j).amount) {
					checkInputs.remove(j);
					doContinue = false;
				}
			}
		}
		return checkInputs.size() == 0;
	}
}
