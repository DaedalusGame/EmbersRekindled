package teamroots.embers.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.tileentity.TileEntityFluidExtractor;
import teamroots.embers.tileentity.TileEntityFluidPipe;
import teamroots.embers.tileentity.TileEntityItemExtractor;
import teamroots.embers.util.EnumPipeConnection;
import teamroots.embers.util.Misc;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BlockFluidExtractor extends BlockTEBase {
	public BlockFluidExtractor(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityFluidExtractor();
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos){
		TileEntityFluidExtractor p = (TileEntityFluidExtractor)world.getTileEntity(pos);
		p.updateNeighbors(world);
		p.markDirty();
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side){
		return true;
	}
	
	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side){
		return true;
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		if (world.getTileEntity(pos) instanceof TileEntityFluidExtractor){
			((TileEntityFluidExtractor)world.getTileEntity(pos)).updateNeighbors(world);
			world.getTileEntity(pos).markDirty();
		}
	}

	@Override
	public RayTraceResult collisionRayTrace(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Vec3d start, @Nonnull Vec3d end) {
		List<AxisAlignedBB> subBoxes = new ArrayList<>();

		subBoxes.add(new AxisAlignedBB(0.3125, 0.3125, 0.3125, 0.6875, 0.6875, 0.6875));

		if (world.getTileEntity(pos) instanceof TileEntityFluidExtractor) {
			TileEntityFluidExtractor pipe = ((TileEntityFluidExtractor) world.getTileEntity(pos));

			if (pipe.getInternalConnection(EnumFacing.UP) != EnumPipeConnection.NONE)
				subBoxes.add(new AxisAlignedBB(0.375, 0.625, 0.375, 0.625, 1.0, 0.625));
			if (pipe.getInternalConnection(EnumFacing.DOWN) != EnumPipeConnection.NONE)
				subBoxes.add(new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.375, 0.625));
			if (pipe.getInternalConnection(EnumFacing.NORTH) != EnumPipeConnection.NONE)
				subBoxes.add(new AxisAlignedBB(0.375, 0.375, 0.0, 0.625, 0.625, 0.375));
			if (pipe.getInternalConnection(EnumFacing.SOUTH) != EnumPipeConnection.NONE)
				subBoxes.add(new AxisAlignedBB(0.375, 0.375, 0.625, 0.625, 0.625, 1.0));
			if (pipe.getInternalConnection(EnumFacing.WEST) != EnumPipeConnection.NONE)
				subBoxes.add(new AxisAlignedBB(0.0, 0.375, 0.375, 0.375, 0.625, 0.625));
			if (pipe.getInternalConnection(EnumFacing.EAST) != EnumPipeConnection.NONE)
				subBoxes.add(new AxisAlignedBB(0.625, 0.375, 0.375, 1.0, 0.625, 0.625));
		}

		return Misc.raytraceMultiAABB(subBoxes, pos, start, end);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		double x1 = 0.3125;
		double y1 = 0.3125;
		double z1 = 0.3125;
		double x2 = 0.6875;
		double y2 = 0.6875;
		double z2 = 0.6875;

		if (source.getTileEntity(pos) instanceof TileEntityFluidExtractor) {
			TileEntityFluidExtractor pipe = ((TileEntityFluidExtractor) source.getTileEntity(pos));
			if (pipe.getInternalConnection(EnumFacing.UP) != EnumPipeConnection.NONE) {
				y2 = 1;
			}
			if (pipe.getInternalConnection(EnumFacing.DOWN) != EnumPipeConnection.NONE) {
				y1 = 0;
			}
			if (pipe.getInternalConnection(EnumFacing.NORTH) != EnumPipeConnection.NONE) {
				z1 = 0;
			}
			if (pipe.getInternalConnection(EnumFacing.SOUTH) != EnumPipeConnection.NONE) {
				z2 = 1;
			}
			if (pipe.getInternalConnection(EnumFacing.WEST) != EnumPipeConnection.NONE) {
				x1 = 0;
			}
			if (pipe.getInternalConnection(EnumFacing.EAST) != EnumPipeConnection.NONE) {
				x2 = 1;
			}
		}

		return new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
	}
}
