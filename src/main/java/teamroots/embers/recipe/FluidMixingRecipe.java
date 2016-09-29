package teamroots.embers.recipe;

import java.util.ArrayList;

import net.minecraftforge.fluids.FluidStack;

public class FluidMixingRecipe {
	public ArrayList<FluidStack> inputs = new ArrayList<FluidStack>();
	public FluidStack output = null;
	public FluidMixingRecipe(FluidStack[] inputs, FluidStack output){
		for (int i = 0; i < inputs.length; i ++){
			this.inputs.add(inputs[i]);
		}
		this.output = output;
	}
	
	public FluidStack getResult(ArrayList<FluidStack> fluids){
		for (int i = 0; i < fluids.size(); i ++){
			boolean doContinue = true;
			for (int j = 0; j < inputs.size() && doContinue; j ++){
				doContinue = false;
				fluids.get(i).amount -= inputs.get(j).amount;
			}
		}
		return output;
	}
	
	public boolean matches(ArrayList<FluidStack> test){
		ArrayList<FluidStack> checkInputs = new ArrayList<FluidStack>();
		for (int i = 0; i < inputs.size(); i ++){
			checkInputs.add(inputs.get(i));
		}
		for (int i = 0; i < test.size(); i ++){
			boolean doContinue = true;
			for (int j = 0; j < checkInputs.size() && doContinue; j ++){
				if (test.get(i).getFluid() == checkInputs.get(j).getFluid() && test.get(i).amount >= checkInputs.get(j).amount){
					checkInputs.remove(j);
					doContinue = false;
				}
			}
		}
		return checkInputs.size() == 0;
	}
}
