package teamroots.embers.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import teamroots.embers.item.EnumStampType;

public class ItemStampingOreRecipe {
	private String ore = "";
	private FluidStack fluid = null;
	private ItemStack result = null;
	private EnumStampType type = EnumStampType.TYPE_NULL;
	boolean matchMetadata = false;
	boolean matchNBT = false;
	public ItemStampingOreRecipe(String ore, FluidStack fluid, EnumStampType type, ItemStack result, boolean meta, boolean nbt){
		this.ore = ore;
		this.fluid = fluid;
		this.type = type;
		this.result = result;
		this.matchMetadata = meta;
		this.matchNBT = nbt;
	}
	
	public EnumStampType getStamp(){
		return type;
	}
	
	public String getOre(){
		return ore;
	}
	
	public FluidStack getFluid(){
		return fluid;
	}
	
	public boolean matches(ItemStack stack, FluidStack fluid, EnumStampType type){
		boolean matchesItem = OreDictionary.containsMatch(false, OreDictionary.getOres(ore), stack);
		boolean matchesFluid = false;
		if (fluid.getFluid().getName().compareTo(this.fluid.getFluid().getName()) == 0 && fluid.amount >= this.fluid.amount){
			matchesFluid = true;
		}
		return matchesItem && matchesFluid && type == this.type;
	}
	
	public ItemStack getResult(ItemStack input, FluidStack fluid, EnumStampType type){
		return result;
	}
}
