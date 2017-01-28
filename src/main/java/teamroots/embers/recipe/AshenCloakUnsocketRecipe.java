package teamroots.embers.recipe;

import java.util.ArrayList;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import teamroots.embers.RegistryManager;
import teamroots.embers.item.ItemAshenCloak;
import teamroots.embers.item.ItemInflictorGem;

public class AshenCloakUnsocketRecipe implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		boolean has_cloak = false;
		boolean moreThanOne_cloak = false;
		for (int i = 0; i < inv.getSizeInventory(); i ++){
			if (inv.getStackInSlot(i) != ItemStack.EMPTY){
				if (inv.getStackInSlot(i).getItem() == RegistryManager.ashen_cloak_chest && inv.getStackInSlot(i).getTagCompound() != null){
					if (inv.getStackInSlot(i).getTagCompound().hasKey("gem1") ||
							inv.getStackInSlot(i).getTagCompound().hasKey("gem2") ||
							inv.getStackInSlot(i).getTagCompound().hasKey("gem3") ||
							inv.getStackInSlot(i).getTagCompound().hasKey("gem4") ||
							inv.getStackInSlot(i).getTagCompound().hasKey("gem5") ||
							inv.getStackInSlot(i).getTagCompound().hasKey("gem6") ||
							inv.getStackInSlot(i).getTagCompound().hasKey("gem7")){
						if (!has_cloak && !moreThanOne_cloak){
							has_cloak = true;
						}
						else if (has_cloak){
							has_cloak = false;
							moreThanOne_cloak = true;
						}
					}
				}
				else {
					if (inv.getStackInSlot(i) != ItemStack.EMPTY){
						return false;
					}
				}
			}
		}
		return has_cloak;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack capeStack = ItemStack.EMPTY;
		for (int i = 0; i < inv.getSizeInventory(); i ++){
			if (inv.getStackInSlot(i) != ItemStack.EMPTY){
				if (inv.getStackInSlot(i).getItem() == RegistryManager.ashen_cloak_chest){
					capeStack = inv.getStackInSlot(i).copy();
				}
			}
		}
		if (capeStack != ItemStack.EMPTY){
			for (int i = 1; i < 8; i ++){
				if (capeStack.getTagCompound().hasKey("gem"+i)){
					capeStack.getTagCompound().removeTag("gem"+i);
				}
			}
		}
		return capeStack;
	}

	@Override
	public int getRecipeSize() {
		return 3;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(RegistryManager.ashen_cloak_chest,1);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> gems = NonNullList.create();
		for (int i = 0; i < inv.getSizeInventory(); i ++){
			if (inv.getStackInSlot(i) != ItemStack.EMPTY){
				if (inv.getStackInSlot(i).getItem() == RegistryManager.ashen_cloak_chest){
					for (int j = 1; j < 8; j ++){
						if (inv.getStackInSlot(i).getTagCompound().hasKey("gem"+j)){
							gems.add(new ItemStack(inv.getStackInSlot(i).getTagCompound().getCompoundTag("gem"+j)));
						}
					}
				}
			}
		}
		inv.clear();
		return gems;
	}

}
