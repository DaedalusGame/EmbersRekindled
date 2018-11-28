package teamroots.embers.recipe;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import teamroots.embers.RegistryManager;
import teamroots.embers.api.item.IInflictorGem;
import teamroots.embers.api.item.IInflictorGemHolder;

public class AshenCloakSocketRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		ItemStack cloak = ItemStack.EMPTY;
		int cloaks = 0;
		int strings = 0;
		int gems = 0;
			for (int i = 0; i < inv.getSizeInventory(); i ++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (stack.getItem() instanceof IInflictorGemHolder && ((IInflictorGemHolder) stack.getItem()).getAttachedGemCount(stack) == 0) {
					cloak = stack;
				}
			}
			for (int i = 0; i < inv.getSizeInventory(); i ++){
				ItemStack stack = inv.getStackInSlot(i);
				if (!stack.isEmpty()){
					if (stack.getItem() instanceof IInflictorGemHolder){
						cloaks++;
					}
					else if (stack.getItem() == Items.STRING){
						strings++;
					}
					else if (!cloak.isEmpty() && ((IInflictorGemHolder)cloak.getItem()).canAttachGem(cloak,stack)){
						gems++;
					}
					else {
						return false;
					}
				}
			}
		return !cloak.isEmpty() && cloaks == 1 && strings == 1 && gems > 0 && gems <= ((IInflictorGemHolder)cloak.getItem()).getGemSlots(cloak);
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
			if (!capeStack.hasTagCompound()){
				capeStack.setTagCompound(new NBTTagCompound());
			}
			int counter = 1;
			for (int i = 0; i < inv.getSizeInventory(); i ++){
				ItemStack stack = inv.getStackInSlot(i);
				if (!stack.isEmpty() && stack.getItem() instanceof IInflictorGem) {
					((IInflictorGemHolder)capeStack.getItem()).attachGem(capeStack,stack,counter);
					counter++;
				}
			}
			return capeStack;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(RegistryManager.ashen_cloak_chest,1);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		return remaining;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 3;
	}

}
