package teamroots.embers.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.item.EnumStampType;
import teamroots.embers.util.IHasSize;

import java.util.List;

public class ItemStampingRecipe {
	@Deprecated
	protected ItemStack stack = ItemStack.EMPTY;
	@Deprecated
	protected EnumStampType type = EnumStampType.TYPE_NULL;
	@Deprecated
	boolean matchMetadata = false;
	@Deprecated
	boolean matchNBT = false;

	public Ingredient input;
	public FluidStack fluid = null;
	public Ingredient stamp;
	public ItemStack result = ItemStack.EMPTY;
	boolean exactMatch = false;

	@Deprecated
	public ItemStampingRecipe(ItemStack input, FluidStack fluid, EnumStampType stamp, ItemStack result, boolean meta, boolean nbt){
		this(Ingredient.fromStacks(input),fluid,Ingredient.fromStacks(EnumStampType.getStack(stamp)),result);
	}

	public ItemStampingRecipe(Ingredient input, FluidStack fluid, Ingredient stamp, ItemStack result) {
		this.input = input;
		this.fluid = fluid;
		this.stamp = stamp;
		this.result = result;
	}

	public ItemStampingRecipe setFluidMatch(boolean exact) {
		exactMatch = exact;
		return this;
	}

	public int getInputConsumed() {
		return input instanceof IHasSize ? ((IHasSize) input).getSize() : 1;
	}

	@Deprecated
	public EnumStampType getStamp(){
		return type;
	}

	@Deprecated
	public ItemStack getStack(){
		return stack;
	}
	
	public FluidStack getFluid(){
		return fluid;
	}

	public List<ItemStack> getInputs()
	{
		return Lists.newArrayList(input.getMatchingStacks());
	}

	public List<ItemStack> getStamps() {
		return Lists.newArrayList(stamp.getMatchingStacks());
	}

	public List<ItemStack> getOutputs() { return Lists.newArrayList(result); }

	@Deprecated
	public boolean matches(ItemStack input, FluidStack fluid, EnumStampType type){
		return matches(input,fluid,EnumStampType.getStack(type));
	}

	public boolean matches(ItemStack item, FluidStack fluid, ItemStack stamp) {
		FluidStack inputFluid = this.fluid;
		boolean hasEnoughFluid = inputFluid == null || (fluid != null && fluid.amount >= inputFluid.amount);
		boolean fluidMatches = inputFluid == null || (fluid != null && (exactMatch ? inputFluid.isFluidEqual(fluid) : inputFluid.getFluid() == fluid.getFluid()));
		return this.input.apply(item) && this.stamp.apply(stamp) && fluidMatches && hasEnoughFluid;
	}

	public ItemStack getResult(TileEntity tile, ItemStack input, FluidStack fluid, ItemStack stamp) {
		return getResult(input,fluid,EnumStampType.getType(stamp));
	}

	@Deprecated
	public ItemStack getResult(ItemStack input, FluidStack fluid, EnumStampType type){
		return result.copy();
	}
}
