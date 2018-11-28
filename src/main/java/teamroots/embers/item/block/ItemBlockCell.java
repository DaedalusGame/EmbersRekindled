package teamroots.embers.item.block;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.item.IHeldEmberCell;
import teamroots.embers.api.item.IInventoryEmberCell;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.block.BlockFluidGauge;
import teamroots.embers.item.ItemEmberStorage;
import teamroots.embers.power.DefaultEmberItemCapability;
import teamroots.embers.tileentity.TileEntityCopperCell;
import teamroots.embers.tileentity.TileEntityTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockCell extends ItemBlock {
    public ItemBlockCell(Block block) {
        super(block);
        this.setHasSubtypes(true);
    }

    public ItemStack withFill(double ember) {
        ItemStack stack = new ItemStack(this);
        if (stack.hasCapability(EmbersCapabilities.EMBER_CAPABILITY,null)) {
            IEmberCapability capability = stack.getCapability(EmbersCapabilities.EMBER_CAPABILITY, null);
            capability.setEmber(ember);
        }
        return stack;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems){
        if (isInCreativeTab(tab)){
            subItems.add(new ItemStack(this));
            subItems.add(withFill(getCapacity()));
        }
    }

    public double getCapacity() {
        return 24000;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new DefaultEmberItemCapability(stack, getCapacity());
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged){
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack){
        if (stack.hasCapability(EmbersCapabilities.EMBER_CAPABILITY,null)){
            return true;
        }
        return false;
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
        super.addInformation(stack, world, tooltip, advanced);
        if (stack.hasCapability(EmbersCapabilities.EMBER_CAPABILITY,null)){
            IEmberCapability capability = stack.getCapability(EmbersCapabilities.EMBER_CAPABILITY,null);
            tooltip.add(ItemEmberStorage.formatEmber(capability.getEmber(),capability.getEmberCapacity()));
        }
    }
}
