package teamroots.embers.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemMeltingOreRecipe {
	private String ore = "";
	private FluidStack fluid = null;
	public ItemMeltingOreRecipe(String oreDict, FluidStack fluid){
		this.ore = oreDict;
		this.fluid = fluid;
	}
	
	public String getOreName(){
		return ore;
	}
	
	public FluidStack getFluid(){
		return fluid;
	}
	
	public boolean matches(ItemStack stack){
		System.out.println("Melter Check!");
		return OreDictionary.containsMatch(false, OreDictionary.getOres(ore), stack);
	}
}
