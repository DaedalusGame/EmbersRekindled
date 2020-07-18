package teamroots.embers.compat.jei.wrapper;

import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.recipe.FluidReactionRecipe;

import java.util.List;

public class ReactionChamberRecipeWrapper extends FluidRecipeWrapper {
    FluidReactionRecipe recipe;

    public ReactionChamberRecipeWrapper(FluidReactionRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public FluidStack getInput() {
        return recipe.getInput();
    }

    @Override
    public FluidStack getOutput() {
        return recipe.getOutput();
    }

    @Override
    public void addInfo(List<String> tooltip) {
        //NOOP
    }
}
