package teamroots.embers.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ItemMeltingRecipe {
	private ItemStack stack = ItemStack.EMPTY;
	private FluidStack fluid = null;
	boolean matchMetadata = false;
	boolean matchNBT = false;
	public ItemMeltingRecipe(ItemStack stack, FluidStack fluid, boolean meta, boolean nbt){
		this.stack = stack;
		this.fluid = fluid;
		this.matchMetadata = meta;
		this.matchNBT = nbt;
	}
	
	public ItemStack getStack(){
		return stack;
	}
	
	public FluidStack getFluid(){
		return fluid;
	}
	
	public boolean matches(ItemStack stack){
		if (this.matchNBT){
			return this.stack.getItem().equals(stack.getItem()) && this.stack.getMetadata() == stack.getMetadata() && ItemStack.areItemStackTagsEqual(this.stack, stack);
		}
		if (this.matchMetadata){
			return this.stack.getItem().equals(stack.getItem()) && this.stack.getMetadata() == stack.getMetadata();
		}
		return this.stack.getItem().equals(stack.getItem());
	}
}
