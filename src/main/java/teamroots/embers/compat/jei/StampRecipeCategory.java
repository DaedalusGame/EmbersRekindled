package teamroots.embers.compat.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class StampRecipeCategory extends BlankRecipeCategory {
    private final IDrawable background;
    private final String name;
    
    public static ResourceLocation texture = new ResourceLocation("textures/gui/jei/stamp");

    public StampRecipeCategory(IGuiHelper helper){
    	
        this.background = helper.createDrawable(texture, 0, 0, 256, 256);
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
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		
	}
}
