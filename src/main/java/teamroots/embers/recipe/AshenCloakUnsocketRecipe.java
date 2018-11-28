package teamroots.embers.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import teamroots.embers.RegistryManager;
import teamroots.embers.api.item.IInflictorGemHolder;

import java.util.Collections;

public class AshenCloakUnsocketRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		ItemStack cloak = ItemStack.EMPTY;
		int cloaks = 0;
		if (inv.getSizeInventory() > 4){
			for (int i = 0; i < inv.getSizeInventory(); i ++){
				ItemStack stack = inv.getStackInSlot(i);
				if (!stack.isEmpty()){
					if (stack.getItem() instanceof IInflictorGemHolder){
						if (((IInflictorGemHolder) stack.getItem()).getAttachedGemCount(stack) > 0){
							cloak = stack;
						}
						cloaks++;
					}
					else {
						return false;
					}
				}
			}
		}
		return !cloak.isEmpty() && cloaks == 1;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack capeStack = ItemStack.EMPTY;
		for (int i = 0; i < inv.getSizeInventory(); i ++){
			if (!inv.getStackInSlot(i).isEmpty() && inv.getStackInSlot(i).getItem() instanceof IInflictorGemHolder) {
				capeStack = inv.getStackInSlot(i).copy();
			}
		}
		if (!capeStack.isEmpty()){
			((IInflictorGemHolder)capeStack.getItem()).clearGems(capeStack);
		}
		return capeStack;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(RegistryManager.ashen_cloak_chest,1);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> gems = NonNullList.create();
		for (int i = 0; i < inv.getSizeInventory(); i ++){
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()){
				if (stack.getItem() instanceof IInflictorGemHolder){
					Collections.addAll(gems, ((IInflictorGemHolder) stack.getItem()).getAttachedGems(stack));
				}
			}
		}
		//inv.clear();
		return gems;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 1;
	}

}
