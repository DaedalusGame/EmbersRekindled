package teamroots.embers.api.event;

import net.minecraft.tileentity.TileEntity;

import java.util.List;

public class DialInformationEvent extends UpgradeEvent {
    List<String> information;

    public DialInformationEvent(TileEntity tile, List<String> information) {
        super(tile);
        this.information = information;
    }

    public List<String> getInformation() {
        return information;
    }
}
