package teamroots.embers.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import teamroots.embers.api.item.IHeldEmberCell;
import teamroots.embers.api.item.IInventoryEmberCell;
import teamroots.embers.power.DefaultEmberItemCapability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemEmberJar extends ItemEmberStorage {
	public static final double CAPACITY = 2000.0;

	public ItemEmberJar() {
		super("ember_jar", true);
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
		return new EmberJarCapability(stack, getCapacity());
	}

	public static class EmberJarCapability extends DefaultEmberItemCapability implements IInventoryEmberCell, IHeldEmberCell {
		public EmberJarCapability(@Nonnull ItemStack stack, double capacity) {
			super(stack, capacity);
		}
	}
}
