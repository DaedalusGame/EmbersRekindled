package teamroots.embers.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.util.IHasSize;

import java.util.List;

public class ItemMeltingRecipe {
	@Deprecated
	private ItemStack stack = ItemStack.EMPTY;
	@Deprecated
	boolean matchMetadata = false;
	@Deprecated
	boolean matchNBT = false;

	public Ingredient input;
	public FluidStack fluid;
	public FluidStack bonus; //for geologic separator

	//Binary compat
	@Deprecated
	public ItemMeltingRecipe(ItemStack stack, FluidStack fluid, boolean meta, boolean nbt){
		this(Ingredient.fromStacks(stack),fluid);
	}

	public ItemMeltingRecipe(Ingredient input, FluidStack fluid) {
		this.input = input;
		this.fluid = fluid;
	}

	public int getInputConsumed() {
		return input instanceof IHasSize ? ((IHasSize) input).getSize() : 1;
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

	public FluidStack getBonusOutput(){
		return bonus;
	}


	public List<ItemStack> getInputs()
	{
		return Lists.newArrayList(input.getMatchingStacks());
	}

	public boolean matches(ItemStack stack){
		return input.apply(stack);
	}

	public FluidStack getResult(TileEntity tile, ItemStack input){
		return fluid.copy();
	}

	public ItemMeltingRecipe addBonusOutput(FluidStack fluid) {
		bonus = fluid;
		return this;
	}
}
