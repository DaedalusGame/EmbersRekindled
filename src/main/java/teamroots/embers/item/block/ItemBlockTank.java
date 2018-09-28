package teamroots.embers.item.block;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import teamroots.embers.block.BlockFluidGauge;
import teamroots.embers.tileentity.TileEntityTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockTank extends ItemBlock {
    public ItemBlockTank(Block block) {
        super(block);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new FluidHandlerItemStack(stack, TileEntityTank.capacity) { //Damnit Elu
            @Override
            protected void setFluid(FluidStack fluid) {
                if (!container.hasTagCompound())
                {
                    container.setTagCompound(new NBTTagCompound());
                }

                NBTTagCompound tagCompound = container.getTagCompound();
                tagCompound.removeTag("Empty");
                fluid.writeToNBT(tagCompound);
            }

            @Nullable
            @Override
            public FluidStack getFluid() {
                NBTTagCompound tagCompound = container.getTagCompound();
                if (tagCompound == null || tagCompound.hasKey("Empty"))
                {
                    return null;
                }

                return FluidStack.loadFluidStackFromNBT(tagCompound);
            }

            @Override
            protected void setContainerToEmpty() {
                NBTTagCompound tagCompound = container.getTagCompound();
                tagCompound.setString("Empty","");
                tagCompound.removeTag("FluidName");
                tagCompound.removeTag("Amount");
                tagCompound.removeTag("Tag");
            }
        };
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,null)) {
            IFluidHandler handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,null);
            for(IFluidTankProperties properties : handler.getTankProperties()) {
                FluidStack fluidStack = properties.getContents();
                tooltip.add(TextFormatting.GRAY+BlockFluidGauge.formatFluidStack(fluidStack, properties.getCapacity()));
            }
        }
    }
}
