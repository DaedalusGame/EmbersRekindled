package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.upgrade.UpgradeSiphon;

import javax.annotation.Nullable;

public class TileEntityEmberSiphon extends TileEntity implements ITileEntityBase {
    public UpgradeSiphon upgrade;

    public TileEntityEmberSiphon() {
        upgrade = new UpgradeSiphon(this);
    }

    @Override
    public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {

    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY)
            return facing == EnumFacing.UP;
        if (capability == EmbersCapabilities.EMBER_CAPABILITY && (facing == null || facing.getAxis() != EnumFacing.Axis.Y)) {
            TileEntity tile = world.getTileEntity(pos.up());
            return tile != null && tile.hasCapability(EmbersCapabilities.EMBER_CAPABILITY,EnumFacing.DOWN);
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY && facing == EnumFacing.UP)
            return (T) upgrade;
        if (capability == EmbersCapabilities.EMBER_CAPABILITY && (facing == null || facing.getAxis() != EnumFacing.Axis.Y)) {
            TileEntity tile = world.getTileEntity(pos.up());
            return (T) tile.getCapability(EmbersCapabilities.EMBER_CAPABILITY,EnumFacing.DOWN);
        }
        return super.getCapability(capability, facing);
    }
}
