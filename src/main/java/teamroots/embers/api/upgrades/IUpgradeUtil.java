package teamroots.embers.api.upgrades;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.api.event.UpgradeEvent;

import java.util.List;

public interface IUpgradeUtil {
    List<IUpgradeProvider> getUpgrades(World world, BlockPos pos, EnumFacing[] facings);

    List<IUpgradeProvider> getUpgradesForMultiblock(World world, BlockPos pos, EnumFacing[] facings);

    void getUpgrades(World world, BlockPos pos, EnumFacing[] facings, List<IUpgradeProvider> upgrades);

    void collectUpgrades(World world, BlockPos pos, EnumFacing side, List<IUpgradeProvider> upgrades);

    void verifyUpgrades(TileEntity tile, List<IUpgradeProvider> list);

    int getWorkTime(TileEntity tile, int time, List<IUpgradeProvider> list);

    double getTotalSpeedModifier(TileEntity tile, List<IUpgradeProvider> list);

    boolean doTick(TileEntity tile, List<IUpgradeProvider> list);

    boolean doWork(TileEntity tile, List<IUpgradeProvider> list);

    double getTotalEmberConsumption(TileEntity tile, double ember, List<IUpgradeProvider> list);

    double getTotalEmberProduction(TileEntity tile, double ember, List<IUpgradeProvider> list);

    void transformOutput(TileEntity tile, List<ItemStack> outputs, List<IUpgradeProvider> list);

    FluidStack transformOutput(TileEntity tile, FluidStack output, List<IUpgradeProvider> list);

    boolean getOtherParameter(TileEntity tile, String type, boolean initial, List<IUpgradeProvider> list);

    double getOtherParameter(TileEntity tile, String type, double initial, List<IUpgradeProvider> list);

    int getOtherParameter(TileEntity tile, String type, int initial, List<IUpgradeProvider> list);

    String getOtherParameter(TileEntity tile, String type, String initial, List<IUpgradeProvider> list);

    <T> T getOtherParameter(TileEntity tile, String type, T initial, List<IUpgradeProvider> list);

    void throwEvent(TileEntity tile, UpgradeEvent event, List<IUpgradeProvider> list);
}
