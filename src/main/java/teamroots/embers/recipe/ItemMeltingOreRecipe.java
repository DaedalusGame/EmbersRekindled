package teamroots.embers.recipe;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreIngredient;

@Deprecated
public class ItemMeltingOreRecipe extends ItemMeltingRecipe {
	@Deprecated
	public ItemMeltingOreRecipe(String oreDict, FluidStack fluid){
		super(new OreIngredient(oreDict),fluid);
	}
}
