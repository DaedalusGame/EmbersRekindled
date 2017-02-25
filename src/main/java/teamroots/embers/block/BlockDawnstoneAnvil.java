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
import teamroots.embers.tileentity.TileEntityDawnstoneAnvil;

public class BlockDawnstoneAnvil extends BlockTEBase {
	public static AxisAlignedBB AABB_Z = new AxisAlignedBB(0, 0, 0.25, 1, 1, 0.75);
	public static AxisAlignedBB AABB_X = new AxisAlignedBB(0.25, 0, 0, 0.75, 1, 1);
	
	public static final PropertyBool isXAligned = PropertyBool.create("xaligned");
	
	public BlockDawnstoneAnvil(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}

    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        if (state.getValue(isXAligned)){
        	return AABB_X;
        }
        return AABB_Z;
    }
	
	@Override
	public BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, isXAligned);
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		if (state.getValue(isXAligned)){
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
		return state;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(isXAligned, meta == 1);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityDawnstoneAnvil();
	}
}
