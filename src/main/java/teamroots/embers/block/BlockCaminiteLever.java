package teamroots.embers.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockLever;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCaminiteLever extends BlockBase {

	public static final PropertyEnum<BlockLever.EnumOrientation> FACING = PropertyEnum.<BlockLever.EnumOrientation>create(
			"facing", BlockLever.EnumOrientation.class);
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	protected static final AxisAlignedBB LEVER_NORTH_AABB = new AxisAlignedBB(0.3125D, 0.20000000298023224D, 0.625D,
			0.6875D, 0.800000011920929D, 1.0D);
	protected static final AxisAlignedBB LEVER_SOUTH_AABB = new AxisAlignedBB(0.3125D, 0.20000000298023224D, 0.0D,
			0.6875D, 0.800000011920929D, 0.375D);
	protected static final AxisAlignedBB LEVER_WEST_AABB = new AxisAlignedBB(0.625D, 0.20000000298023224D, 0.3125D,
			1.0D, 0.800000011920929D, 0.6875D);
	protected static final AxisAlignedBB LEVER_EAST_AABB = new AxisAlignedBB(0.0D, 0.20000000298023224D, 0.3125D,
			0.375D, 0.800000011920929D, 0.6875D);
	protected static final AxisAlignedBB LEVER_UP_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D,
			0.6000000238418579D, 0.75D);
	protected static final AxisAlignedBB LEVER_DOWN_AABB = new AxisAlignedBB(0.25D, 0.4000000059604645D, 0.25D, 0.75D,
			1.0D, 0.75D);

	public BlockCaminiteLever(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		return canAttachTo(worldIn, pos, side.getOpposite());
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		for (EnumFacing enumfacing : EnumFacing.values()) {
			if (canAttachTo(worldIn, pos, enumfacing)) {
				return true;
			}
		}

		return false;
	}

	protected static boolean canAttachTo(World worldIn, BlockPos pos, EnumFacing direction) {
		BlockPos blockpos = pos.offset(direction);
        return worldIn.getBlockState(blockpos).isSideSolid(worldIn, blockpos, direction.getOpposite());
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer) {
		IBlockState iblockstate = this.getDefaultState().withProperty(POWERED, Boolean.valueOf(false));

		if (canAttachTo(worldIn, pos, facing.getOpposite())) {
			return iblockstate.withProperty(FACING,
					BlockLever.EnumOrientation.forFacings(facing, placer.getHorizontalFacing()));
		} else {
			for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
				if (enumfacing != facing && canAttachTo(worldIn, pos, enumfacing.getOpposite())) {
					return iblockstate.withProperty(FACING,
							BlockLever.EnumOrientation.forFacings(enumfacing, placer.getHorizontalFacing()));
				}
			}

			if (worldIn.getBlockState(pos.down()).isFullyOpaque()) {
				return iblockstate.withProperty(FACING,
						BlockLever.EnumOrientation.forFacings(EnumFacing.UP, placer.getHorizontalFacing()));
			} else {
				return iblockstate;
			}
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (this.checkCanSurvive(worldIn, pos, state) && !canAttachTo(worldIn, pos,
				((BlockLever.EnumOrientation) state.getValue(FACING)).getFacing().getOpposite())) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}
	}

	private boolean checkCanSurvive(World p_181091_1_, BlockPos p_181091_2_, IBlockState p_181091_3_) {
		if (this.canPlaceBlockAt(p_181091_1_, p_181091_2_)) {
			return true;
		} else {
			this.dropBlockAsItem(p_181091_1_, p_181091_2_, p_181091_3_, 0);
			p_181091_1_.setBlockToAir(p_181091_2_);
			return false;
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch ((BlockLever.EnumOrientation) state.getValue(FACING)) {
		case EAST:
		default:
			return LEVER_EAST_AABB;
		case WEST:
			return LEVER_WEST_AABB;
		case SOUTH:
			return LEVER_SOUTH_AABB;
		case NORTH:
			return LEVER_NORTH_AABB;
		case UP_Z:
		case UP_X:
			return LEVER_UP_AABB;
		case DOWN_X:
		case DOWN_Z:
			return LEVER_DOWN_AABB;
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return true;
		} else {
			state = state.cycleProperty(POWERED);
			worldIn.setBlockState(pos, state, 3);
			float f = ((Boolean) state.getValue(POWERED)).booleanValue() ? 0.6F : 0.5F;
			worldIn.playSound((EntityPlayer) null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, f);
			worldIn.notifyNeighborsOfStateChange(pos, this, false);
			EnumFacing enumfacing = ((BlockLever.EnumOrientation) state.getValue(FACING)).getFacing();
			worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this, false);
			return true;
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (((Boolean) state.getValue(POWERED)).booleanValue()) {
			worldIn.notifyNeighborsOfStateChange(pos, this, false);
			EnumFacing enumfacing = ((BlockLever.EnumOrientation) state.getValue(FACING)).getFacing();
			worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this, false);
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return ((Boolean) blockState.getValue(POWERED)).booleanValue() ? 15 : 0;
	}

	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return !((Boolean) blockState.getValue(POWERED)).booleanValue() ? 0
				: (((BlockLever.EnumOrientation) blockState.getValue(FACING)).getFacing() == side ? 15 : 0);
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, BlockLever.EnumOrientation.byMetadata(meta & 7))
				.withProperty(POWERED, Boolean.valueOf((meta & 8) > 0));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;
		i = i | ((BlockLever.EnumOrientation) state.getValue(FACING)).getMetadata();

		if (((Boolean) state.getValue(POWERED)).booleanValue()) {
			i |= 8;
		}

		return i;
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		switch (rot) {
		case CLOCKWISE_180:

			switch ((BlockLever.EnumOrientation) state.getValue(FACING)) {
			case EAST:
				return state.withProperty(FACING, BlockLever.EnumOrientation.WEST);
			case WEST:
				return state.withProperty(FACING, BlockLever.EnumOrientation.EAST);
			case SOUTH:
				return state.withProperty(FACING, BlockLever.EnumOrientation.NORTH);
			case NORTH:
				return state.withProperty(FACING, BlockLever.EnumOrientation.SOUTH);
			default:
				return state;
			}

		case COUNTERCLOCKWISE_90:

			switch ((BlockLever.EnumOrientation) state.getValue(FACING)) {
			case EAST:
				return state.withProperty(FACING, BlockLever.EnumOrientation.NORTH);
			case WEST:
				return state.withProperty(FACING, BlockLever.EnumOrientation.SOUTH);
			case SOUTH:
				return state.withProperty(FACING, BlockLever.EnumOrientation.EAST);
			case NORTH:
				return state.withProperty(FACING, BlockLever.EnumOrientation.WEST);
			case UP_Z:
				return state.withProperty(FACING, BlockLever.EnumOrientation.UP_X);
			case UP_X:
				return state.withProperty(FACING, BlockLever.EnumOrientation.UP_Z);
			case DOWN_X:
				return state.withProperty(FACING, BlockLever.EnumOrientation.DOWN_Z);
			case DOWN_Z:
				return state.withProperty(FACING, BlockLever.EnumOrientation.DOWN_X);
			}

		case CLOCKWISE_90:

			switch ((BlockLever.EnumOrientation) state.getValue(FACING)) {
			case EAST:
				return state.withProperty(FACING, BlockLever.EnumOrientation.SOUTH);
			case WEST:
				return state.withProperty(FACING, BlockLever.EnumOrientation.NORTH);
			case SOUTH:
				return state.withProperty(FACING, BlockLever.EnumOrientation.WEST);
			case NORTH:
				return state.withProperty(FACING, BlockLever.EnumOrientation.EAST);
			case UP_Z:
				return state.withProperty(FACING, BlockLever.EnumOrientation.UP_X);
			case UP_X:
				return state.withProperty(FACING, BlockLever.EnumOrientation.UP_Z);
			case DOWN_X:
				return state.withProperty(FACING, BlockLever.EnumOrientation.DOWN_Z);
			case DOWN_Z:
				return state.withProperty(FACING, BlockLever.EnumOrientation.DOWN_X);
			}

		default:
			return state;
		}
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state
				.withRotation(mirrorIn.toRotation(((BlockLever.EnumOrientation) state.getValue(FACING)).getFacing()));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, POWERED });
	}

	private boolean canAttach(World world, BlockPos pos, EnumFacing side) {
		return world.isSideSolid(pos, side);
	}

}
