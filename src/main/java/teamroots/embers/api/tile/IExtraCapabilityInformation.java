package teamroots.embers.api.tile;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import java.util.List;

public interface IExtraCapabilityInformation {
    default boolean hasCapabilityDescription(Capability<?> capability) {
        return false;
    }

    default void addCapabilityDescription(List<String> strings, Capability<?> capability, EnumFacing facing) {
        //NOOP
    }

    default void addOtherDescription(List<String> strings, EnumFacing facing) {
        //NOOP
    }

    static String formatCapability(EnumIOType ioType, String type, String filter) {
        String typeString = filter == null ? I18n.format(type) : I18n.format("embers.tooltip.goggles.filter",I18n.format(type),filter);
        switch (ioType) {
            case NONE:
                return null;
            case INPUT:
                return I18n.format("embers.tooltip.goggles.input",typeString);
            case OUTPUT:
                return I18n.format("embers.tooltip.goggles.output",typeString);
            default:
                return I18n.format("embers.tooltip.goggles.storage",typeString);
        }
    }

    enum EnumIOType {
        NONE,
        INPUT,
        OUTPUT,
        BOTH
    }
}
