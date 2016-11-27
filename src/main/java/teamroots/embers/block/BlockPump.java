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
import teamroots.embers.tileentity.TileEntityPipe;
import teamroots.embers.tileentity.TileEntityPump;

public class BlockPump extends BlockTEBase {
	public BlockPump(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityPump();
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block){
		if (world.getTileEntity(pos) != null){
			((TileEntityPump)world.getTileEntity(pos)).updateNeighbors(world);
			world.notifyBlockUpdate(pos, state, world.getBlockState(pos), 3);
		}
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor){
		if (world.getTileEntity(pos) != null){
			((TileEntityPump)world.getTileEntity(pos)).updateNeighbors(world);
		}
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side){
		return true;
	}
	
	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side){
		return true;
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		if (world.getTileEntity(pos) != null){
			((TileEntityPump)world.getTileEntity(pos)).updateNeighbors(world);
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
		
		if (source.getTileEntity(pos) instanceof TileEntityPump){
			TileEntityPump pipe = ((TileEntityPump)source.getTileEntity(pos));
			if (pipe.up != TileEntityPump.EnumPipeConnection.NONE){
				y2 = 1;
			}
			if (pipe.down != TileEntityPump.EnumPipeConnection.NONE){
				y1 = 0;
			}
			if (pipe.north != TileEntityPump.EnumPipeConnection.NONE){
				z1 = 0;
			}
			if (pipe.south != TileEntityPump.EnumPipeConnection.NONE){
				z2 = 1;
			}
			if (pipe.west != TileEntityPump.EnumPipeConnection.NONE){
				x1 = 0;
			}
			if (pipe.east != TileEntityPump.EnumPipeConnection.NONE){
				x2 = 1;
			}
		}
		
		return new AxisAlignedBB(x1,y1,z1,x2,y2,z2);
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player){
		super.onBlockHarvested(world, pos, state, player);
		if (world.getTileEntity(pos.up()) instanceof TileEntityPipe){
			((TileEntityPipe)world.getTileEntity(pos.up())).updateNeighbors(world);
		}
		if (world.getTileEntity(pos.down()) instanceof TileEntityPipe){
			((TileEntityPipe)world.getTileEntity(pos.down())).updateNeighbors(world);
		}
		if (world.getTileEntity(pos.north()) instanceof TileEntityPipe){
			((TileEntityPipe)world.getTileEntity(pos.north())).updateNeighbors(world);
		}
		if (world.getTileEntity(pos.south()) instanceof TileEntityPipe){
			((TileEntityPipe)world.getTileEntity(pos.south())).updateNeighbors(world);
		}
		if (world.getTileEntity(pos.west()) instanceof TileEntityPipe){
			((TileEntityPipe)world.getTileEntity(pos.west())).updateNeighbors(world);
		}
		if (world.getTileEntity(pos.east()) instanceof TileEntityPipe){
			((TileEntityPipe)world.getTileEntity(pos.east())).updateNeighbors(world);
		}
	}
}
