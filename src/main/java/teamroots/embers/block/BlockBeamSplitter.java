package teamroots.embers.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.tileentity.TileEntityBeamSplitter;

public class BlockBeamSplitter extends BlockTEBase {
	public static AxisAlignedBB AABB_UP = new AxisAlignedBB(0.1875,0.1875,0.1875,0.8125,1.0,0.8125);
	public static AxisAlignedBB AABB_DOWN = new AxisAlignedBB(0.1875,0.0,0.1875,0.8125,0.8125,0.8125);
	
	public static final PropertyBool isXAligned = PropertyBool.create("xaligned");
	public static final PropertyBool isConnectedUp = PropertyBool.create("connectedup");
	
	public BlockBeamSplitter(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}

    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        if (state.getValue(isConnectedUp)){
        	return AABB_UP;
        }
        return AABB_DOWN;
    }
	
	@Override
	public BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, isXAligned, isConnectedUp);
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		if (state.getValue(isXAligned) && state.getValue(isConnectedUp)){
			return 3;
		}
		if (state.getValue(isXAligned) && !state.getValue(isConnectedUp)){
			return 2;
		}
		if (!state.getValue(isXAligned) && state.getValue(isConnectedUp)){
			return 1;
		}
		return 0;
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side){
		return side == EnumFacing.UP || side == EnumFacing.DOWN;
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing face, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		IBlockState state = getDefaultState();
		if (placer.getHorizontalFacing() == EnumFacing.EAST || placer.getHorizontalFacing() == EnumFacing.WEST){
			state = state.withProperty(isXAligned, true);
		}
		else {
			state = state.withProperty(isXAligned, false);
		}
		if (face == EnumFacing.DOWN){
			state = state.withProperty(isConnectedUp, true);
		}
		else {
			state = state.withProperty(isConnectedUp, false);
		}
		return state;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		if (meta == 3){
			return getDefaultState().withProperty(isXAligned, true).withProperty(isConnectedUp, true);
		}
		if (meta == 2){
			return getDefaultState().withProperty(isXAligned, true).withProperty(isConnectedUp, false);
		}
		if (meta == 1){
			return getDefaultState().withProperty(isXAligned, false).withProperty(isConnectedUp, true);
		}
		return getDefaultState().withProperty(isXAligned, false).withProperty(isConnectedUp, false);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityBeamSplitter();
	}
}
