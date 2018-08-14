package teamroots.embers.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.tileentity.TileEntityFluidExtractor;
import teamroots.embers.tileentity.TileEntityFluidPipe;
import teamroots.embers.tileentity.TileEntityFluidPipe.EnumPipeConnection;

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
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side){
		return true;
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		double x1 = 0.3125;
		double y1 = 0.3125;
		double z1 = 0.3125;
		double x2 = 0.6875;
		double y2 = 0.6875;
		double z2 = 0.6875;
		
		if (source.getTileEntity(pos) instanceof TileEntityFluidExtractor){
			TileEntityFluidExtractor pipe = ((TileEntityFluidExtractor)source.getTileEntity(pos));
			if (pipe.up != EnumPipeConnection.NONE){
				y2 = 1;
			}
			if (pipe.down != EnumPipeConnection.NONE){
				y1 = 0;
			}
			if (pipe.north != EnumPipeConnection.NONE){
				z1 = 0;
			}
			if (pipe.south != EnumPipeConnection.NONE){
				z2 = 1;
			}
			if (pipe.west != EnumPipeConnection.NONE){
				x1 = 0;
			}
			if (pipe.east != EnumPipeConnection.NONE){
				x2 = 1;
			}
		}
		
		return new AxisAlignedBB(x1,y1,z1,x2,y2,z2);
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player){
		super.onBlockHarvested(world, pos, state, player);
		if (world.getTileEntity(pos.up()) instanceof TileEntityFluidPipe){
			((TileEntityFluidPipe)world.getTileEntity(pos.up())).updateNeighbors(world);
		}
		if (world.getTileEntity(pos.down()) instanceof TileEntityFluidPipe){
			((TileEntityFluidPipe)world.getTileEntity(pos.down())).updateNeighbors(world);
		}
		if (world.getTileEntity(pos.north()) instanceof TileEntityFluidPipe){
			((TileEntityFluidPipe)world.getTileEntity(pos.north())).updateNeighbors(world);
		}
		if (world.getTileEntity(pos.south()) instanceof TileEntityFluidPipe){
			((TileEntityFluidPipe)world.getTileEntity(pos.south())).updateNeighbors(world);
		}
		if (world.getTileEntity(pos.west()) instanceof TileEntityFluidPipe){
			((TileEntityFluidPipe)world.getTileEntity(pos.west())).updateNeighbors(world);
		}
		if (world.getTileEntity(pos.east()) instanceof TileEntityFluidPipe){
			((TileEntityFluidPipe)world.getTileEntity(pos.east())).updateNeighbors(world);
		}
	}
}
