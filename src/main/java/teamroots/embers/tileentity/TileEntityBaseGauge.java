package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.embers.block.BlockBaseGauge;

public abstract class TileEntityBaseGauge extends TileEntity implements ITileEntityBase, ITickable {
    int comparatorValue = 0;

    public int getComparatorValue() {
        return comparatorValue;
    }

    @Override
    public void update() {
        int oldComparatorValue = comparatorValue;
        EnumFacing facing = getFacing();
        TileEntity tileEntity = world.getTileEntity(pos.offset(facing.getOpposite()));
        if(tileEntity != null)
            comparatorValue = calculateComparatorValue(tileEntity,facing);
        if(comparatorValue != oldComparatorValue) {
            world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
        }
    }

    private EnumFacing getFacing() {
        IBlockState state = world.getBlockState(pos);
        return state.getValue(BlockBaseGauge.facing);
    }

    @Override
    public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {

    }

    public abstract int calculateComparatorValue(TileEntity tileEntity, EnumFacing facing);

    public abstract String getDialType();
}
