package teamroots.embers.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import teamroots.embers.api.item.IHeldEmberCell;
import teamroots.embers.power.DefaultEmberItemCapability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemEmberCartridge extends ItemEmberStorage implements IHeldEmberCell {

	public static final double CAPACITY = 6000.0;

	public ItemEmberCartridge() {
		super("ember_cartridge", true);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
	}

	@Override
	public double getCapacity() {
		return CAPACITY;
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new EmberCartridgeCapability(stack, getCapacity());
	}

	public static class EmberCartridgeCapability extends DefaultEmberItemCapability implements IHeldEmberCell {
		public EmberCartridgeCapability(@Nonnull ItemStack stack, double capacity) {
			super(stack, capacity);
		}
	}
	
	/*public static class EmberChargeColorHandler implements IItemColor {
		public EmberChargeColorHandler(){
			//
		}

		@Override
		public int colorMultiplier(ItemStack stack, int layer) {
			if (layer == 0 && stack.hasTagCompound()){
				if (stack.getItem() instanceof IEmberItem){
					double coeff = ((IEmberItem)stack.getItem()).getEmber(stack)/((IEmberItem)stack.getItem()).getEmberCapacity(stack);
					int r = 255;
					int g = (int)(255.0*(1.0-coeff)+64.0*coeff);
					int b = (int)(255.0*(1.0-coeff)+16.0*coeff);
					return 0xFF0000;
				}
			}
			return 0xFFFFFF;
		}

	}*/

}
