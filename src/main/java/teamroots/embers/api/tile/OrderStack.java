package teamroots.embers.api.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import teamroots.embers.api.filter.IFilter;
import teamroots.embers.util.FilterUtil;

public class OrderStack {
    private BlockPos pos;
    private IFilter filter;
    private int size;

    public OrderStack(BlockPos pos, IFilter filter, int size) {
        this.pos = pos;
        this.filter = filter;
        this.size = size;
    }

    public OrderStack(NBTTagCompound tag) {
        readFromNBT(tag);
    }

    public BlockPos getPos() {
        return pos;
    }

    public IOrderSource getSource(World world) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof IOrderSource)
            return (IOrderSource) tile;
        return null;
    }

    public IFilter getFilter() {
        return filter;
    }

    public int getSize() {
        return size;
    }

    public boolean acceptsItem(World world, ItemStack stack) {
        IOrderSource source = getSource(world);
        if(source != null) {
            IItemHandler itemHandler = source.getItemHandler();
            if(itemHandler != null)
                return filter.acceptsItem(stack, itemHandler);
        }
        return false;
    }

    public void deplete(int n) {
        size -= n;
    }

    public void increment(int n) {
        size += n;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setInteger("x",pos.getX());
        tag.setInteger("y",pos.getY());
        tag.setInteger("z",pos.getZ());
        tag.setTag("filter", filter.writeToNBT(new NBTTagCompound()));
        tag.setInteger("size", size);
        return tag;
    }

    public void readFromNBT(NBTTagCompound tag) {
        pos = new BlockPos(tag.getInteger("x"),tag.getInteger("y"),tag.getInteger("z"));
        filter = FilterUtil.deserializeFilter(tag.getCompoundTag("filter"));
        size = tag.getInteger("size");
    }

    public void reset(IFilter filter, int size) {
        this.filter = filter;
        this.size = size;
    }
}
