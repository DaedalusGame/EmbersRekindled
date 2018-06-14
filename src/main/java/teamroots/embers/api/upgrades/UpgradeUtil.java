package teamroots.embers.api.upgrades;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class UpgradeUtil {
    public static IUpgradeUtil IMPL;

    public static List<IUpgradeProvider> getUpgrades(World world, BlockPos pos, EnumFacing[] facings) {
        return IMPL.getUpgrades(world, pos, facings);
    }

    public static List<IUpgradeProvider> getUpgradesForMultiblock(World world, BlockPos pos, EnumFacing[] facings) {
        return IMPL.getUpgradesForMultiblock(world, pos, facings);
    }

    public static void verifyUpgrades(TileEntity tile, List<IUpgradeProvider> list) {
        IMPL.verifyUpgrades(tile, list);
    }

    public static double getTotalSpeedModifier(TileEntity tile, List<IUpgradeProvider> list) {
        return IMPL.getTotalSpeedModifier(tile, list);
    }

    public static int getWorkTime(TileEntity tile, int time, List<IUpgradeProvider> list) {
        return IMPL.getWorkTime(tile,time,list);
    }

    //DO NOT CALL FROM AN UPGRADE'S doWork METHOD!!
    public static boolean doWork(TileEntity tile, List<IUpgradeProvider> list) {
        return IMPL.doWork(tile, list);
    }

    public static double getTotalEmberConsumption(TileEntity tile, double ember, List<IUpgradeProvider> list) {
        return IMPL.getTotalEmberConsumption(tile, ember, list);
    }

    public static double getTotalEmberProduction(TileEntity tile, double ember, List<IUpgradeProvider> list) {
        return IMPL.getTotalEmberProduction(tile, ember, list);
    }

    public static void transformOutput(TileEntity tile, List<ItemStack> outputs, List<IUpgradeProvider> list) {
        IMPL.transformOutput(tile, outputs, list);
    }

    public static FluidStack transformOutput(TileEntity tile, FluidStack output, List<IUpgradeProvider> list) {
        return IMPL.transformOutput(tile, output, list);
    }

    public static boolean getOtherParameter(TileEntity tile, String type, boolean initial, List<IUpgradeProvider> list) {
        return IMPL.getOtherParameter(tile, type, initial, list);
    }

    public static double getOtherParameter(TileEntity tile, String type, double initial, List<IUpgradeProvider> list) {
        return IMPL.getOtherParameter(tile, type, initial, list);
    }

    public static int getOtherParameter(TileEntity tile, String type, int initial, List<IUpgradeProvider> list) {
        return IMPL.getOtherParameter(tile, type, initial, list);
    }

    public static String getOtherParameter(TileEntity tile, String type, String initial, List<IUpgradeProvider> list) {
        return IMPL.getOtherParameter(tile, type, initial, list);
    }

    public static <T> T getOtherParameter(TileEntity tile, String type, T initial, List<IUpgradeProvider> list) {
        return IMPL.getOtherParameter(tile, type, initial, list);
    }
}
