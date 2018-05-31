package teamroots.embers.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.Embers;
import teamroots.embers.compat.jei.wrapper.MixingRecipeWrapper;

import java.util.List;

public class MixingRecipeCategory implements IRecipeCategory<MixingRecipeWrapper> {
    private final IDrawable background;
    private final String name;
    
    public static ResourceLocation texture = new ResourceLocation("embers:textures/gui/jei_mixer.png");

    public MixingRecipeCategory(IGuiHelper helper){
    	
        this.background = helper.createDrawable(texture, 0, 0, 108, 128);
        this.name = I18n.format("embers.jei.recipe.mixer");
    }

    @Override
    public String getTitle()
    {
        return name;
    }

    @Override
    public IDrawable getBackground(){
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout layout, MixingRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiFluidStackGroup fluid = layout.getFluidStacks();
        fluid.init(0, true, 46, 7, 16, 32, 16, true, null);
        fluid.init(1, true, 8, 46, 16, 32, 16, true, null);
        fluid.init(2, true, 46, 84, 16, 32, 16, true, null);
        fluid.init(3, false, 84, 46, 16, 32, 16, true, null);

        int size = ingredients.getInputs(FluidStack.class).size();
        if (size >= 1){
            fluid.set(0, ingredients.getInputs(FluidStack.class).get(0));
        }
        if (size >= 2){
            fluid.set(1, ingredients.getInputs(FluidStack.class).get(1));
        }
        if (size >= 3){
            fluid.set(2, ingredients.getInputs(FluidStack.class).get(2));
        }
        fluid.set(3, ingredients.getOutputs(FluidStack.class).get(0));
    }

    @Override
	public String getUid() {
		return "embers.mixer";
	}

	@Override
	public String getModName() {
		return Embers.MODID;
	}
}
