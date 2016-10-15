package teamroots.embers.compat.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class MelterRecipeHandler implements IRecipeHandler<MeltingRecipeWrapper>{

	@Override
	public Class<MeltingRecipeWrapper> getRecipeClass() {
		return MeltingRecipeWrapper.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return "embers.melter";
	}

	@Override
	public String getRecipeCategoryUid(MeltingRecipeWrapper recipe) {
		return getRecipeCategoryUid();
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(MeltingRecipeWrapper recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(MeltingRecipeWrapper recipe) {
		return true;
	}

}
