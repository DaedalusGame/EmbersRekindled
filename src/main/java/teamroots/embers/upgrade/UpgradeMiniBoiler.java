package teamroots.embers.upgrade;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import teamroots.embers.api.event.EmberEvent;
import teamroots.embers.api.event.UpgradeEvent;
import teamroots.embers.api.tile.IMechanicallyPowered;
import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.api.upgrades.UpgradeUtil;
import teamroots.embers.tileentity.TileEntityCatalyticPlug;
import teamroots.embers.tileentity.TileEntityMiniBoiler;
import teamroots.embers.util.DefaultUpgradeProvider;

import java.util.HashSet;
import java.util.List;

public class UpgradeMiniBoiler extends DefaultUpgradeProvider {
    public UpgradeMiniBoiler(TileEntity tile) {
        super("mini_boiler", tile);
    }

    @Override
    public int getLimit(TileEntity tile) {
        return tile instanceof IMechanicallyPowered ? 0 : 4;
    }

    @Override
    public void throwEvent(TileEntity tile, UpgradeEvent event) {
        if(event instanceof EmberEvent) {
            ((TileEntityMiniBoiler) this.tile).boil(((EmberEvent) event).getAmount());
        }
    }
}
