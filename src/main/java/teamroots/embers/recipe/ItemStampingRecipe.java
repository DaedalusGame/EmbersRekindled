package teamroots.embers.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.item.EnumStampType;
import teamroots.embers.util.IHasSize;

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

	@Deprecated
	public boolean matches(ItemStack input, FluidStack fluid, EnumStampType type){
		return matches(input,fluid,EnumStampType.getStack(type));
	}

	public boolean matches(ItemStack input, FluidStack fluid, ItemStack stamp) {
		return this.input.apply(input) && this.stamp.apply(stamp) && (fluid == null || ((exactMatch ? fluid.isFluidEqual(fluid) : fluid.getFluid() == fluid.getFluid())));
	}

	public ItemStack getResult(TileEntity tile, ItemStack input, FluidStack fluid, ItemStack stamp) {
		return getResult(input,fluid,EnumStampType.getType(stamp));
	}

	@Deprecated
	public ItemStack getResult(ItemStack input, FluidStack fluid, EnumStampType type){
		return result.copy();
	}
}
