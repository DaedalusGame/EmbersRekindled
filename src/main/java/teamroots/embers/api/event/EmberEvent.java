package teamroots.embers.api.event;

import net.minecraft.tileentity.TileEntity;

public class EmberEvent extends UpgradeEvent {
    EnumType type;
    double amount;

    public EmberEvent(TileEntity tile, EnumType type, double amount) {
        super(tile);
        this.type = type;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public EnumType getType() {
        return type;
    }

    public enum EnumType {
        PRODUCE,
        CONSUME
    }
}
