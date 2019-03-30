package teamroots.embers.apiimpl;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.event.UpgradeEvent;
import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.api.upgrades.IUpgradeProxy;
import teamroots.embers.api.upgrades.IUpgradeUtil;

import java.util.*;

public class UpgradeUtilImpl implements IUpgradeUtil {
    public List<IUpgradeProvider> getUpgrades(World world, BlockPos pos, EnumFacing[] facings)
    {
        LinkedList<IUpgradeProvider> upgrades = new LinkedList<>();
        /*for (EnumFacing facing: facings) {
            TileEntity te = world.getTileEntity(pos.offset(facing));
            if(te != null && te.hasCapability(EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY,facing.getOpposite()))
            {
                upgrades.add(te.getCapability(EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY,facing.getOpposite()));
            }
        }*/
        getUpgrades(world,pos,facings,upgrades);
        return upgrades;
    }

    @Deprecated
    public List<IUpgradeProvider> getUpgradesForMultiblock(World world, BlockPos pos, EnumFacing[] facings)
    {
        /*LinkedList<IUpgradeProvider> upgrades = new LinkedList<>();
        for (EnumFacing facing: facings) {
            TileEntity te = world.getTileEntity(pos.offset(facing));
            if(te instanceof TileEntityMechCore)
            {
                upgrades.addAll(getUpgrades(world,pos.offset(facing),EnumFacing.VALUES));
            }
        }
        return upgrades;*/
        return getUpgrades(world,pos,facings);
    }

    public void getUpgrades(World world, BlockPos pos, EnumFacing[] facings, List<IUpgradeProvider> upgrades)
    {
        for (EnumFacing facing: facings) {
            collectUpgrades(world,pos.offset(facing),facing.getOpposite(),upgrades);
        }
        resetCheckedProxies();
    }

    ThreadLocal<Set<IUpgradeProxy>> checkedProxies = ThreadLocal.withInitial(HashSet::new);

    private boolean isProxyChecked(IUpgradeProxy proxy)
    {
        return checkedProxies.get().contains(proxy);
    }

    private void addCheckedProxy(IUpgradeProxy proxy)
    {
        checkedProxies.get().add(proxy);
    }

    private void resetCheckedProxies()
    {
        checkedProxies.remove();
    }

    public void collectUpgrades(World world, BlockPos pos, EnumFacing side, List<IUpgradeProvider> upgrades) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te.hasCapability(EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY, side)) {
            upgrades.add(te.getCapability(EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY, side));
        }
        if (te instanceof IUpgradeProxy) {
            IUpgradeProxy proxy = (IUpgradeProxy) te;
            if (!isProxyChecked(proxy) && proxy.isProvider(side)) { //Prevent infinite recursion
                addCheckedProxy(proxy);
                proxy.collectUpgrades(upgrades);
            }
        }
    }

    public void verifyUpgrades(TileEntity tile,List<IUpgradeProvider> list)
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

    @Override
    public int getWorkTime(TileEntity tile, int time, List<IUpgradeProvider> list) {
        double speedmod = getTotalSpeedModifier(tile,list);
        if(speedmod == 0) //Stop.
            return Integer.MAX_VALUE;
        return (int)(time * (1.0 / speedmod));
    }

    public double getTotalSpeedModifier(TileEntity tile,List<IUpgradeProvider> list)
    {
        double total = 1.0f;

        for (IUpgradeProvider upgrade : list) {
            total = upgrade.getSpeed(tile, total);
        }

        return total;
    }

    @Override
    public boolean doTick(TileEntity tile, List<IUpgradeProvider> list) {
        for (IUpgradeProvider upgrade: list) {
            if(upgrade.doTick(tile,list))
                return true;
        }

        return false;
    }

    //DO NOT CALL FROM AN UPGRADE'S doWork METHOD!!
    public boolean doWork(TileEntity tile, List<IUpgradeProvider> list)
    {
        for (IUpgradeProvider upgrade: list) {
            if(upgrade.doWork(tile,list))
                return true;
        }

        return false;
    }

    public double getTotalEmberConsumption(TileEntity tile, double ember, List<IUpgradeProvider> list)
    {
        for (IUpgradeProvider upgrade : list) {
            ember = upgrade.transformEmberConsumption(tile, ember);
        }

        return ember;
    }

    public double getTotalEmberProduction(TileEntity tile, double ember, List<IUpgradeProvider> list)
    {
        for (IUpgradeProvider upgrade : list) {
            ember = upgrade.transformEmberProduction(tile, ember);
        }

        return ember;
    }

    public void transformOutput(TileEntity tile, List<ItemStack> outputs, List<IUpgradeProvider> list)
    {
        for (IUpgradeProvider upgrade : list) {
            upgrade.transformOutput(tile,outputs);
        }
    }

    public FluidStack transformOutput(TileEntity tile, FluidStack output, List<IUpgradeProvider> list)
    {
        for (IUpgradeProvider upgrade : list) {
            output = upgrade.transformOutput(tile,output);
        }

        return output;
    }

    public boolean getOtherParameter(TileEntity tile, String type, boolean initial, List<IUpgradeProvider> list)
    {
        for (IUpgradeProvider upgrade : list) {
            initial = upgrade.getOtherParameter(tile,type,initial);
        }

        return initial;
    }

    public double getOtherParameter(TileEntity tile, String type, double initial, List<IUpgradeProvider> list)
    {
        for (IUpgradeProvider upgrade : list) {
            initial = upgrade.getOtherParameter(tile,type,initial);
        }

        return initial;
    }

    public int getOtherParameter(TileEntity tile, String type, int initial, List<IUpgradeProvider> list)
    {
        for (IUpgradeProvider upgrade : list) {
            initial = upgrade.getOtherParameter(tile,type,initial);
        }

        return initial;
    }

    public String getOtherParameter(TileEntity tile, String type, String initial, List<IUpgradeProvider> list)
    {
        for (IUpgradeProvider upgrade : list) {
            initial = upgrade.getOtherParameter(tile,type,initial);
        }

        return initial;
    }

    public <T> T getOtherParameter(TileEntity tile, String type, T initial, List<IUpgradeProvider> list)
    {
        for (IUpgradeProvider upgrade : list) {
            initial = upgrade.getOtherParameter(tile,type,initial);
        }

        return initial;
    }

    @Override
    public void throwEvent(TileEntity tile, UpgradeEvent event, List<IUpgradeProvider> list) {
        for (IUpgradeProvider upgrade : list) {
            upgrade.throwEvent(tile,event);
        }
    }
}
