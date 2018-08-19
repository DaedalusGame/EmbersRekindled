package teamroots.embers.upgrade;

import mysticalmechanics.api.IMechCapability;
import mysticalmechanics.api.MysticalMechanicsAPI;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import teamroots.embers.Embers;
import teamroots.embers.api.event.DialInformationEvent;
import teamroots.embers.api.event.UpgradeEvent;
import teamroots.embers.api.tile.IMechanicallyPowered;
import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.tileentity.TileEntityCatalyticPlug;
import teamroots.embers.util.DefaultUpgradeProvider;

import java.util.HashSet;
import java.util.List;

public class UpgradeActuator extends DefaultUpgradeProvider {
    public UpgradeActuator(TileEntity tile) {
        super("actuator", tile);
    }

    @Override
    public int getLimit(TileEntity tile) {
        return tile instanceof IMechanicallyPowered ? 1 : 0;
    }

    @Override
    public double getSpeed(TileEntity tile, double speed) {
        double power = getPower();
        if(tile instanceof IMechanicallyPowered) {
            IMechanicallyPowered mechTile = (IMechanicallyPowered) tile;
            if(power > mechTile.getMinimumPower() && power <= mechTile.getMaximumPower())
                return mechTile.getMechanicalSpeed(power) * speed;
        }
        return speed;
    }

    @Override
    public double transformEmberConsumption(TileEntity tile, double ember) {
        return 0;
    }

    @Override
    public double getOtherParameter(TileEntity tile, String type, double value) {
        if(type.equals("fuel_consumption"))
            return 0;
        return value;
    }

    @Override
    public boolean doWork(TileEntity tile, List<IUpgradeProvider> upgrades) {
        double power = getPower();
        IMechanicallyPowered mechTile = (IMechanicallyPowered) tile;

        return !(power > mechTile.getMinimumPower() && power <= mechTile.getMaximumPower());
    }

    @Override
    public void throwEvent(TileEntity tile, UpgradeEvent event) {
        IMechanicallyPowered mechTile = (IMechanicallyPowered) tile;
        if(event instanceof DialInformationEvent) {
            double power = getPower();
            double speedModifier = mechTile.getMechanicalSpeed(power) / mechTile.getNominalSpeed();
            DialInformationEvent dialEvent = (DialInformationEvent) event;
            dialEvent.getInformation().add(Embers.proxy.formatLocalize("embers.tooltip.upgrade.actuator",speedModifier)); //Proxy this because it runs in shared code
        }
    }

    private double getPower() {
        if(!tile.hasCapability(MysticalMechanicsAPI.MECH_CAPABILITY,null))
            return 0;
        IMechCapability handler = tile.getCapability(MysticalMechanicsAPI.MECH_CAPABILITY,null);
        return handler.getPower(null);
    }
}
