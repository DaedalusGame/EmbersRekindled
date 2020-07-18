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
import teamroots.embers.block.BlockEmberGauge;
import teamroots.embers.tileentity.TileEntityCatalyticPlug;
import teamroots.embers.util.DecimalFormats;
import teamroots.embers.util.DefaultUpgradeProvider;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;

public class UpgradeActuator extends DefaultUpgradeProvider {
    public UpgradeActuator(TileEntity tile) {
        super("actuator", tile);
    }

    @Override
    public int getLimit(TileEntity tile) {
        return tile instanceof IMechanicallyPowered ? 1 : 1;
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
        if(tile instanceof IMechanicallyPowered) {
            return ((IMechanicallyPowered) tile).getStandardPowerRatio() * ember;
        }
        return ember;
    }

    @Override
    public double transformEmberProduction(TileEntity tile, double ember) {
        double power = getPower();
        if(power > 15)
            return ember * 1.5;
        else
            return ember;
    }

    @Override
    public double getOtherParameter(TileEntity tile, String type, double value) {
        if(tile instanceof IMechanicallyPowered) {
            if(type.equals("fuel_consumption"))
                return ((IMechanicallyPowered) tile).getStandardPowerRatio() * value;
        }
        return value;
    }

    @Override
    public boolean doWork(TileEntity tile, List<IUpgradeProvider> upgrades) {
        double power = getPower();
        if(tile instanceof IMechanicallyPowered) {
            IMechanicallyPowered mechTile = (IMechanicallyPowered) tile;
            return !(power > mechTile.getMinimumPower() && power <= mechTile.getMaximumPower());
        }
        return false;
    }

    @Override
    public void throwEvent(TileEntity tile, UpgradeEvent event) {
        if(event instanceof DialInformationEvent) {
            DialInformationEvent dialEvent = (DialInformationEvent) event;
            if(BlockEmberGauge.DIAL_TYPE.equals(dialEvent.getDialType())) {
                DecimalFormat multiplierFormat = Embers.proxy.getDecimalFormat("embers.decimal_format.speed_multiplier");
                if (tile instanceof IMechanicallyPowered) {
                    IMechanicallyPowered mechTile = (IMechanicallyPowered) tile;
                    double power = getPower();
                    double speedModifier = mechTile.getMechanicalSpeed(power) / mechTile.getNominalSpeed();
                    dialEvent.getInformation().add(Embers.proxy.formatLocalize("embers.tooltip.upgrade.actuator", multiplierFormat.format(speedModifier))); //Proxy this because it runs in shared code
                } else {
                    double power = getPower();
                    double productionModifier = power > 15 ? 1.5 : 1.0;
                    dialEvent.getInformation().add(Embers.proxy.formatLocalize("embers.tooltip.upgrade.actuator.other", multiplierFormat.format(productionModifier)));
                }
            }
        }
    }

    private double getPower() {
        if(!tile.hasCapability(MysticalMechanicsAPI.MECH_CAPABILITY,null))
            return 0;
        IMechCapability handler = tile.getCapability(MysticalMechanicsAPI.MECH_CAPABILITY,null);
        return handler.getPower(null);
    }
}
