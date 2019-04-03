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
import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.block.BlockNodeBase;
import teamroots.embers.upgrade.UpgradeConservation;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;

public abstract class TileEntityNodeBase extends TileEntity implements ITileEntityBase {
	protected IUpgradeProvider upgrade;

	public TileEntityNodeBase() {
		super();
		upgrade = initUpgrade();
	}

	protected abstract IUpgradeProvider initUpgrade();

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability == EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY) {
			return facing == getFacing();
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability == EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY && facing == getFacing()) {
			return (T) upgrade;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
							EnumFacing side, float hitX, float hitY, float hitZ) {
		return false;
	}

	public EnumFacing getFacing() {
		IBlockState state = world.getBlockState(pos);
		return state.getValue(BlockNodeBase.facing);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		world.setTileEntity(pos, null);
	}

	@Override
	public void markDirty() {
		super.markDirty();
		Misc.syncTE(this);
	}
}
