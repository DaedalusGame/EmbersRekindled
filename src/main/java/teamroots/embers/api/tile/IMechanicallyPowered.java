package teamroots.embers.api.tile;

public interface IMechanicallyPowered {
    double getMechanicalSpeed(double power);

    double getNominalSpeed();

    default double getMinimumPower() {
        return 0;
    }

    default double getMaximumPower() {
        return Double.POSITIVE_INFINITY;
    }
}
