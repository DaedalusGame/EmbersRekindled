package teamroots.embers.util;

import mysticalmechanics.api.IMechCapability;
import net.minecraft.util.EnumFacing;

public class ConsumerMechCapability implements IMechCapability {
    public double[] power = new double[6];
    double maxPower;
    boolean dirty = true;
    boolean additive = false; //Whether power input will be added together or the largest will be chosen

    public void setAdditive(boolean additive) {
        this.additive = additive;
    }

    public void markDirty() {
        dirty = true;
    }

    @Override
    public double getPower(EnumFacing enumFacing) {
        if (enumFacing == null) {
            if (dirty) {
                recalculateMax();
                dirty = false;
            }
            return maxPower;
        }
        return power[enumFacing.getIndex()];
    }

    private void recalculateMax() {
        maxPower = 0;
        for (EnumFacing facing : EnumFacing.VALUES) {
            double power = getPower(facing);
            if (additive)
                maxPower += power;
            else
                maxPower = Math.max(power, maxPower);
        }
    }

    @Override
    public void setPower(double value, EnumFacing enumFacing) {
        if (enumFacing == null)
            for (int i = 0; i < 6; i++)
                power[i] = value;
        else {
            double oldPower = power[enumFacing.getIndex()];
            this.power[enumFacing.getIndex()] = value;
            if (oldPower != value) {
                dirty = true;
                onPowerChange();
            }
        }
    }

    @Override
    public void onPowerChange() {

    }

    @Override
    public boolean isOutput(EnumFacing from) {
        return false;
    }
}
