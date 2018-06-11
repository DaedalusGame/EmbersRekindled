package teamroots.embers.compat.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class MixingRecipeHandler implements IRecipeHandler<MixingRecipeWrapper>{

	@Override
	public Class<MixingRecipeWrapper> getRecipeClass() {
		return MixingRecipeWrapper.class;
	}

	public String getRecipeCategoryUid() {
		return "embers.mixer";
	}

	@Override
	public String getRecipeCategoryUid(MixingRecipeWrapper recipe) {
		return getRecipeCategoryUid();
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(MixingRecipeWrapper recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(MixingRecipeWrapper recipe) {
		return true;
	}

}
