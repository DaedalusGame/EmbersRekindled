package teamroots.embers.api.upgrades;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.api.event.UpgradeEvent;

import java.util.List;

public interface IUpgradeProvider {
    //The upgrade id
    String getUpgradeId();

    //Determines the order in which upgrades apply
    default int getPriority() {
        return 0;
    }

    //Determines how many of this upgrade can be applied. If the number surpasses this limit, they'll all be discarded!
    default int getLimit(TileEntity tile) {
        return Integer.MAX_VALUE;
    }

    //The speed modifier that this upgrade provides
    default double getSpeed(TileEntity tile, double speed) {
        return speed;
    }

    //Called on machine update
    //If this returns true, this call replaces the entire update call.
    default boolean doTick(TileEntity tile, List<IUpgradeProvider> upgrades) {
        return false;
    }

    //Called if machine is working
    //If this returns true, this call replaces the usual work the machine would do.
    default boolean doWork(TileEntity tile, List<IUpgradeProvider> upgrades) {
        return false;
    }

    default double transformEmberConsumption(TileEntity tile, double ember) {
        return ember;
    }

    default double transformEmberProduction(TileEntity tile, double ember) {
        return ember;
    }

    //Called when machine would output items, allows modification of what items the machine outputs.
    default void transformOutput(TileEntity tile, List<ItemStack> outputs) {
        //NOOP
    }

    //Called when machine would output fluid, allows modification of what fluid it will output.
    default FluidStack transformOutput(TileEntity tile, FluidStack output)
    {
        return output;
    }

    default boolean getOtherParameter(TileEntity tile, String type, boolean value) { return value; }

    default double getOtherParameter(TileEntity tile, String type, double value) { return value; }

    default int getOtherParameter(TileEntity tile, String type, int value) { return value; }

    default String getOtherParameter(TileEntity tile, String type, String value) { return value; }

    default <T> T getOtherParameter(TileEntity tile, String type, T value) { return value; }

    default void throwEvent(TileEntity tile, UpgradeEvent event) {}
}
