package teamroots.embers.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.RegistryManager;
import teamroots.embers.item.ItemAlchemicWaste;

import java.util.Random;

public class ItemWasteStampingRecipe extends ItemStampingRecipe{
	Random random = new Random();
	public ItemWasteStampingRecipe(){
		super(Ingredient.fromItem(RegistryManager.alchemic_waste), null, Ingredient.fromItem(RegistryManager.stamp_flat),new ItemStack(RegistryManager.dust_ash,1));
	}

	@Override
	public ItemStack getResult(TileEntity tile, ItemStack input, FluidStack fluid, ItemStack stamp) {
		if (input.hasTagCompound() && input.getItem() instanceof ItemAlchemicWaste){
			int ash = input.getTagCompound().getInteger("totalAsh");
			return new ItemStack(RegistryManager.dust_ash,(ash*4)/5);
		}
		return new ItemStack(RegistryManager.dust_ash,1);
	}
}
