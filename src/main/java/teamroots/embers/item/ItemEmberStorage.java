package teamroots.embers.item;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.Embers;
import teamroots.embers.EventManager;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.util.Misc;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.util.List;

public abstract class ItemEmberStorage extends ItemBase {
	public ItemEmberStorage(String name, boolean addToTab) {
		super(name, addToTab);
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

	public abstract double getCapacity();

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
			tooltip.add(formatEmber(capability.getEmber(),capability.getEmberCapacity()));
		}
	}

	public static String formatEmber(double ember, double emberCapacity) {
		DecimalFormat emberFormat = Embers.proxy.getDecimalFormat("embers.decimal_format.ember");
		return I18n.format("embers.tooltip.item.ember", emberFormat.format(ember), emberFormat.format(emberCapacity));
	}

	@SideOnly(Side.CLIENT)
	public static class ColorHandler implements IItemColor {
		@Override
		public int colorMultiplier(@Nonnull ItemStack stack, int tintIndex) {
			if (tintIndex == 1){
				if (stack.hasCapability(EmbersCapabilities.EMBER_CAPABILITY,null)){
					IEmberCapability capability = stack.getCapability(EmbersCapabilities.EMBER_CAPABILITY,null);
					float coeff = (float)(capability.getEmber() / capability.getEmberCapacity());
					float timerSine = ((float)Math.sin(8.0*Math.toRadians(EventManager.ticks % 360))+1.0f)/2.0f;
					int r = (int)255.0f;
					int g = (int)(255.0f*(1.0f-coeff) + (64.0f*timerSine+64.0f)*coeff);
					int b = (int)(255.0f*(1.0f-coeff) + 16.0f*coeff);
					return (r << 16) + (g << 8) + b;
				}
			}
			return Misc.intColor(255, 255, 255);
		}
	}
}
