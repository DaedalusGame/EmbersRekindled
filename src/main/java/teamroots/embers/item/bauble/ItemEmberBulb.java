package teamroots.embers.item.bauble;

import baubles.api.BaubleType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.item.IHeldEmberCell;
import teamroots.embers.api.item.IInventoryEmberCell;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.item.ItemEmberStorage;
import teamroots.embers.power.DefaultEmberItemCapability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemEmberBulb extends ItemBaubleBase implements IInventoryEmberCell, IHeldEmberCell {
	public static final double CAPACITY = 1000.0;

	public ItemEmberBulb() {
		super("ember_bulb", BaubleType.TRINKET, true);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
	}

	public double getCapacity() {
		return CAPACITY;
	}

	public ItemStack withFill(double ember) {
		ItemStack stack = getDefaultInstance();
		if (stack.hasCapability(EmbersCapabilities.EMBER_CAPABILITY,null)) {
			IEmberCapability capability = stack.getCapability(EmbersCapabilities.EMBER_CAPABILITY, null);
			capability.setEmber(ember);
		}
		return stack;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems){
		if (isInCreativeTab(tab)){
			subItems.add(getDefaultInstance());
			subItems.add(withFill(getCapacity()));
		}
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack){
		if (stack.hasCapability(EmbersCapabilities.EMBER_CAPABILITY,null)){
			IEmberCapability capability = stack.getCapability(EmbersCapabilities.EMBER_CAPABILITY,null);
			if (capability.getEmber() < capability.getEmberCapacity()){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged){
		return slotChanged || oldStack.getItem() != newStack.getItem();
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack){
		if (stack.hasCapability(EmbersCapabilities.EMBER_CAPABILITY,null)){
			IEmberCapability capability = stack.getCapability(EmbersCapabilities.EMBER_CAPABILITY,null);
			return (capability.getEmberCapacity()-capability.getEmber())/capability.getEmberCapacity();
		}
		return 0.0;
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced){
		if (stack.hasCapability(EmbersCapabilities.EMBER_CAPABILITY,null)){
			IEmberCapability capability = stack.getCapability(EmbersCapabilities.EMBER_CAPABILITY,null);
			tooltip.add(ItemEmberStorage.formatEmber(capability.getEmber(),capability.getEmberCapacity()));
		}
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
