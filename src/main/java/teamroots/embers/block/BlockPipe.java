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
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.tileentity.TileEntityItemPipe;
import teamroots.embers.tileentity.TileEntityPipe;
import teamroots.embers.tileentity.TileEntityPump;

public class BlockPipe extends BlockTEBase {
	public BlockPipe(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityPipe();
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor){
		((TileEntityPipe)world.getTileEntity(pos)).updateNeighbors(world);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos){
		((TileEntityPipe)world.getTileEntity(pos)).updateNeighbors(world);
		world.getTileEntity(pos).markDirty();
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		if (world.getTileEntity(pos) instanceof TileEntityPipe){
			((TileEntityPipe)world.getTileEntity(pos)).updateNeighbors(world);
			world.getTileEntity(pos).markDirty();
		}
	}
	
	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side){
		if (world.getTileEntity(pos) instanceof TileEntityPipe){
			return ((TileEntityPipe)world.getTileEntity(pos)).getConnection(side) == TileEntityPipe.EnumPipeConnection.NONE;
		}
		return true;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		double x1 = 0.375;
		double y1 = 0.375;
		double z1 = 0.375;
		double x2 = 0.625;
		double y2 = 0.625;
		double z2 = 0.625;
		
		if (source.getTileEntity(pos) instanceof TileEntityPipe){
			TileEntityPipe pipe = ((TileEntityPipe)source.getTileEntity(pos));
			if (pipe.up != TileEntityPipe.EnumPipeConnection.NONE){
				y2 = 1;
			}
			if (pipe.down != TileEntityPipe.EnumPipeConnection.NONE){
				y1 = 0;
			}
			if (pipe.north != TileEntityPipe.EnumPipeConnection.NONE){
				z1 = 0;
			}
			if (pipe.south != TileEntityPipe.EnumPipeConnection.NONE){
				z2 = 1;
			}
			if (pipe.west != TileEntityPipe.EnumPipeConnection.NONE){
				x1 = 0;
			}
			if (pipe.east != TileEntityPipe.EnumPipeConnection.NONE){
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
