package teamroots.embers.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public class ItemMeltingRecipe {
	@Deprecated
	private ItemStack stack = ItemStack.EMPTY;
	@Deprecated
	boolean matchMetadata = false;
	@Deprecated
	boolean matchNBT = false;

	public Ingredient input;
	public FluidStack fluid;

	//Binary compat
	@Deprecated
	public ItemMeltingRecipe(ItemStack stack, FluidStack fluid, boolean meta, boolean nbt){
		this(Ingredient.fromStacks(stack),fluid);
	}

	public ItemMeltingRecipe(Ingredient input, FluidStack fluid) {
		this.input = input;
		this.fluid = fluid;
	}

	@Deprecated
	public ItemStack getStack(){
		return stack;
	}

	public Ingredient getInput() {
		return input;
	}

	public FluidStack getFluid(){
		return fluid;
	}
	
	public boolean matches(ItemStack stack){
		return input.apply(stack);
	}
}
