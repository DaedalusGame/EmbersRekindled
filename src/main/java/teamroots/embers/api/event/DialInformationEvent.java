package teamroots.embers.api.event;

import net.minecraft.tileentity.TileEntity;

import java.util.List;

public class DialInformationEvent extends UpgradeEvent {
    List<String> information;
    String dialType;

    public DialInformationEvent(TileEntity tile, List<String> information, String dialType) {
        super(tile);
        this.information = information;
        this.dialType = dialType;
    }

    public List<String> getInformation() {
        return information;
    }

    public String getDialType() {
        return dialType;
    }
}
