package teamroots.embers.compat.jei;

import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class StampRecipeCategory extends BlankRecipeCategory {
    private final IDrawable background;
    private final String name;
    
    public static ResourceLocation texture = new ResourceLocation("embers:textures/gui/jei_stamp.png");

    public StampRecipeCategory(IGuiHelper helper){
    	
        this.background = helper.createDrawable(texture, 0, 0, 108, 128);
        this.name = I18n.format("embers.jei.recipe.stamp");
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
	public String getUid() {
		return "embers.stamp";
	}

	@Override
	public void setRecipe(IRecipeLayout layout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup stacks = layout.getItemStacks();
        
        stacks.init(0, true, 7, 27);
        stacks.init(1, true, 46, 6);
        stacks.init(2, false, 83, 27);
        
        if (ingredients.getInputs(ItemStack.class) instanceof List){
        	if (ingredients.getInputs(ItemStack.class).size() == 2){
        		stacks.set(0, ingredients.getInputs(ItemStack.class).get(0));
        		stacks.set(1, ingredients.getInputs(ItemStack.class).get(1));
        	}
        }
        stacks.set(2,ingredients.getOutputs(ItemStack.class).get(0));
        
        IGuiFluidStackGroup fluid = layout.getFluidStacks();
        fluid.init(3, true, 47, 48, 16, 32, 1500, true, null);
        fluid.set(3, ingredients.getInputs(FluidStack.class).get(0));
	}
}
