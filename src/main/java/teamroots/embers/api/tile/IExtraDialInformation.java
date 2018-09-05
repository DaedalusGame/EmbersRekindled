package teamroots.embers.api.tile;

import net.minecraft.util.EnumFacing;
import java.util.List;

public interface IExtraDialInformation {
    void addDialInformation(EnumFacing facing, List<String> information, String dialType);

    default int getComparatorData(EnumFacing facing, int data, String dialType) {
        return data;
    }
}
