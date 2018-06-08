package teamroots.embers.util;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.tileentity.TileEntityMechCore;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class UpgradeUtil {
    public static List<IUpgradeProvider> getUpgrades(World world, BlockPos pos, EnumFacing[] facings)
    {
        LinkedList<IUpgradeProvider> upgrades = new LinkedList<>();
        for (EnumFacing facing: facings) {
            TileEntity te = world.getTileEntity(pos.offset(facing));
            if(te != null && te.hasCapability(EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY,facing.getOpposite()))
            {
                upgrades.add(te.getCapability(EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY,facing.getOpposite()));
            }
        }
        return upgrades;
    }

    public static List<IUpgradeProvider> getUpgradesForMultiblock(World world, BlockPos pos, EnumFacing[] facings)
    {
        LinkedList<IUpgradeProvider> upgrades = new LinkedList<>();
        for (EnumFacing facing: facings) {
            TileEntity te = world.getTileEntity(pos.offset(facing));
            if(te instanceof TileEntityMechCore)
            {
                upgrades.addAll(getUpgrades(world,pos.offset(facing),EnumFacing.VALUES));
            }
        }
        return upgrades;
    }

    public static void verifyUpgrades(TileEntity tile,List<IUpgradeProvider> list)
    {
        //Count, remove, sort
        //This call is expensive. Ideally should be cached. The total time complexity is O(n + n^2 + n log n) = O(n^2) for an ArrayList.
        //Total time complexity for a LinkedList should be O(n + n + n log n) = O(n log n). Slightly better.
        HashMap<String,Integer> upgradeCounts = new HashMap<>();
        list.forEach(x -> {
            String id = x.getUpgradeId();
            upgradeCounts.put(x.getUpgradeId(), upgradeCounts.getOrDefault(id,0) + 1);
        });
        list.removeIf(x -> upgradeCounts.get(x.getUpgradeId()) > x.getLimit(tile));
        list.sort((x,y) -> Integer.compare(x.getPriority(),y.getPriority()));
    }

    public static double getTotalSpeedModifier(TileEntity tile,List<IUpgradeProvider> list)
    {
        double total = 1.0f;

        for (IUpgradeProvider upgrade : list) {
            total = upgrade.getSpeed(tile, total);
        }

        return total;
    }

    //DO NOT CALL FROM AN UPGRADE'S doWork METHOD!!
    public static boolean doWork(TileEntity tile, List<IUpgradeProvider> list)
    {
        for (IUpgradeProvider upgrade: list) {
            if(upgrade.doWork(tile,list))
                return true;
        }

        return false;
    }

    public static double getTotalEmberConsumption(TileEntity tile, double ember, List<IUpgradeProvider> list)
    {
        for (IUpgradeProvider upgrade : list) {
            ember = upgrade.transformEmberConsumption(tile, ember);
        }

        return ember;
    }

    public static double getTotalEmberProduction(TileEntity tile, double ember, List<IUpgradeProvider> list)
    {
        for (IUpgradeProvider upgrade : list) {
            ember = upgrade.transformEmberProduction(tile, ember);
        }

        return ember;
    }

    public static void transformOutput(TileEntity tile, List<ItemStack> outputs, List<IUpgradeProvider> list)
    {
        for (IUpgradeProvider upgrade : list) {
            upgrade.transformOutput(tile,outputs);
        }
    }

    public static FluidStack transformOutput(TileEntity tile, FluidStack output, List<IUpgradeProvider> list)
    {
        for (IUpgradeProvider upgrade : list) {
            output = upgrade.transformOutput(tile,output);
        }

        return output;
    }

    public static boolean getOtherParameter(TileEntity tile, String type, boolean initial, List<IUpgradeProvider> list)
    {
        for (IUpgradeProvider upgrade : list) {
            initial = upgrade.getOtherParameter(tile,type,initial);
        }

        return initial;
    }

    public static double getOtherParameter(TileEntity tile, String type, double initial, List<IUpgradeProvider> list)
    {
        for (IUpgradeProvider upgrade : list) {
            initial = upgrade.getOtherParameter(tile,type,initial);
        }

        return initial;
    }

    public static int getOtherParameter(TileEntity tile, String type, int initial, List<IUpgradeProvider> list)
    {
        for (IUpgradeProvider upgrade : list) {
            initial = upgrade.getOtherParameter(tile,type,initial);
        }

        return initial;
    }

    public static String getOtherParameter(TileEntity tile, String type, String initial, List<IUpgradeProvider> list)
    {
        for (IUpgradeProvider upgrade : list) {
            initial = upgrade.getOtherParameter(tile,type,initial);
        }

        return initial;
    }

    public static <T> T getOtherParameter(TileEntity tile, String type, T initial, List<IUpgradeProvider> list)
    {
        for (IUpgradeProvider upgrade : list) {
            initial = upgrade.getOtherParameter(tile,type,initial);
        }

        return initial;
    }
}
