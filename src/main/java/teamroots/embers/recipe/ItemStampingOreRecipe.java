package teamroots.embers.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import teamroots.embers.item.EnumStampType;

@Deprecated
public class ItemStampingOreRecipe extends ItemStampingRecipe {
	String ore;

	public ItemStampingOreRecipe(String ore, FluidStack fluid, EnumStampType type, ItemStack result, boolean meta, boolean nbt){
		super(new OreIngredient(ore),fluid, Ingredient.fromStacks(EnumStampType.getStack(type)), result);
		this.ore = ore;
	}
	
	public String getOre(){
		return ore;
	}
}
