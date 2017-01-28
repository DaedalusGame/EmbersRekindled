package teamroots.embers.recipe;

import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.RegistryManager;
import teamroots.embers.item.EnumStampType;
import teamroots.embers.item.ItemAlchemicWaste;

public class ItemWasteStampingRecipe extends ItemStampingRecipe{
	Random random = new Random();
	public ItemWasteStampingRecipe(){
		super(new ItemStack(RegistryManager.alchemic_waste,1),null,EnumStampType.TYPE_FLAT,new ItemStack(RegistryManager.dust_ash,1),false,false);
	}
	
	public boolean matches(ItemStack stack, FluidStack fluid, EnumStampType type){
		boolean matchesItem = false;
		if (stack == ItemStack.EMPTY && this.stack == ItemStack.EMPTY){
			matchesItem = true;
		}
		else if (this.stack != ItemStack.EMPTY && stack != ItemStack.EMPTY){
			if (this.matchNBT){
				matchesItem = this.stack.getItem().equals(stack.getItem()) && this.stack.getMetadata() == stack.getMetadata() && ItemStack.areItemStackTagsEqual(this.stack, stack);
			}
			else if (this.matchMetadata){
				matchesItem = this.stack.getItem().equals(stack.getItem()) && this.stack.getMetadata() == stack.getMetadata();
			}
			else {
				matchesItem = this.stack.getItem().equals(stack.getItem());
			}
		}
		boolean matchesFluid = fluid == null && this.fluid == null;
		if (fluid != null && this.fluid != null){
			if (fluid.getFluid().getName().compareTo(this.fluid.getFluid().getName()) == 0 && fluid.amount >= this.fluid.amount){
				matchesFluid = true;
			}
		}
		return matchesItem && matchesFluid && type == this.type;
	}
	
	@Override
	public ItemStack getResult(ItemStack input, FluidStack fluid, EnumStampType type){
		if (input.hasTagCompound() && input.getItem() instanceof ItemAlchemicWaste){
			int ash = input.getTagCompound().getInteger("totalAsh");
			return new ItemStack(RegistryManager.dust_ash,(ash*4)/5);
		}
		return new ItemStack(RegistryManager.dust_ash,1);
	}
}
