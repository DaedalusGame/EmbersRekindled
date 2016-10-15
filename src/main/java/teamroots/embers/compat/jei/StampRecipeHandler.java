package teamroots.embers.compat.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class StampRecipeHandler implements IRecipeHandler<StampingRecipeWrapper>{

	@Override
	public Class<StampingRecipeWrapper> getRecipeClass() {
		return StampingRecipeWrapper.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return "embers.stamp";
	}

	@Override
	public String getRecipeCategoryUid(StampingRecipeWrapper recipe) {
		return getRecipeCategoryUid();
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(StampingRecipeWrapper recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(StampingRecipeWrapper recipe) {
		return true;
	}

}
