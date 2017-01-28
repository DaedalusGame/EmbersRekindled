package teamroots.embers.compat.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class AlchemyRecipeHandler implements IRecipeHandler<AlchemyRecipeWrapper>{

	@Override
	public Class<AlchemyRecipeWrapper> getRecipeClass() {
		return AlchemyRecipeWrapper.class;
	}
	
	public String getRecipeCategoryUid() {
		return "embers.alchemy";
	}

	@Override
	public String getRecipeCategoryUid(AlchemyRecipeWrapper recipe) {
		return getRecipeCategoryUid();
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(AlchemyRecipeWrapper recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(AlchemyRecipeWrapper recipe) {
		return true;
	}

}
