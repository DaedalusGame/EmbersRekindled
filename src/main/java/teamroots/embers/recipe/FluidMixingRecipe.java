package teamroots.embers.recipe;

import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class FluidMixingRecipe {
	public ArrayList<FluidStack> inputs = new ArrayList<>();
	public FluidStack output = null;
	public FluidMixingRecipe(FluidStack[] inputs, FluidStack output){
		Collections.addAll(this.inputs, inputs);
		this.output = output;
	}
	
	public FluidStack getResult(ArrayList<FluidStack> fluids){
		return output;
	}
	
	public boolean matches(ArrayList<FluidStack> test){
		ArrayList<FluidStack> checkInputs = inputs.stream().collect(Collectors.toCollection(ArrayList::new));
		for (FluidStack aTest : test) {
			boolean doContinue = true;
			for (int j = 0; j < checkInputs.size() && doContinue; j++) {
				if (aTest.getFluid().getName().compareTo(checkInputs.get(j).getFluid().getName()) == 0 && aTest.amount >= checkInputs.get(j).amount) {
					checkInputs.remove(j);
					doContinue = false;
				}
			}
		}
		return checkInputs.size() == 0;
	}
}
