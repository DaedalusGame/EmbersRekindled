package teamroots.embers.upgrade;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import teamroots.embers.api.event.MachineRecipeEvent;
import teamroots.embers.api.event.UpgradeEvent;
import teamroots.embers.recipe.ItemMeltingRecipe;
import teamroots.embers.tileentity.TileEntityGeoSeparator;
import teamroots.embers.util.DefaultUpgradeProvider;

public class UpgradeGeoSeparator extends DefaultUpgradeProvider {
    public UpgradeGeoSeparator(TileEntity tile) {
        super("geo_separator", tile);
    }

    @Override
    public void throwEvent(TileEntity tile, UpgradeEvent event) {
        if(event instanceof MachineRecipeEvent.Success) {
            Object recipe = ((MachineRecipeEvent<?>) event).getRecipe();
            if(recipe instanceof ItemMeltingRecipe) {
                FluidStack bonus = ((ItemMeltingRecipe) recipe).getBonusOutput();
                if (bonus != null && this.tile instanceof TileEntityGeoSeparator && this.tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,null)) {
                    IFluidHandler fluidHandler = this.tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
                    fluidHandler.fill(bonus.copy(),true);
                }
            }
        }
    }
}
