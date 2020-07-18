package teamroots.embers.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.plugins.vanilla.ingredients.FluidStackRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.Embers;
import teamroots.embers.compat.jei.wrapper.FluidRecipeWrapper;
import teamroots.embers.compat.jei.wrapper.MeltingRecipeWrapper;

import java.util.List;

public abstract class FluidRecipeCategory implements IRecipeCategory<FluidRecipeWrapper> {
    private final IDrawable background;

    public static ResourceLocation texture = new ResourceLocation("embers:textures/gui/jei_boiler.png");

    public FluidRecipeCategory(IGuiHelper helper){
    	
        this.background = helper.createDrawable(texture, 0, 0, 125, 29);
    }

    @Override
    public IDrawable getBackground(){
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, FluidRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiFluidStackGroup fluid = recipeLayout.getFluidStacks();
        FluidStack input = recipeWrapper.getInput();
        FluidStack output = recipeWrapper.getOutput();
        fluid.addTooltipCallback((slotIndex, isInput, ingredient, tooltip) -> {
            if(slotIndex == 2) {
                recipeWrapper.addInfo(tooltip);
            }
        });
        int capacity = Math.max(getAmount(input), getAmount(output));
        fluid.init(1, true, 26, 6, 16, 16, capacity, false, null);
        fluid.init(2, false, 84, 6, 16, 16, capacity, false, null);

        fluid.set(1, input);
        fluid.set(2, output);

    }

    private int getAmount(FluidStack stack) {
        return stack != null ? stack.amount : 0;
    }

	@Override
	public String getModName() {
		return Embers.MODID;
	}
}
