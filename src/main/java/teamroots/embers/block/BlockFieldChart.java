package teamroots.embers.block;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.RegistryManager;
import teamroots.embers.tileentity.ITileEntityBase;
import teamroots.embers.tileentity.TileEntityDropper;
import teamroots.embers.tileentity.TileEntityFieldChart;
import teamroots.embers.tileentity.TileEntityLargeTank;

public class BlockFieldChart extends BlockTEBase {
	public static final PropertyInteger state = PropertyInteger.create("state", 0, 9);
	
	public static final AxisAlignedBB AABB_BASE = new AxisAlignedBB(0,0,0,1,0.375f,1);
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return AABB_BASE;
	}
	
	public BlockFieldChart(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
		List<ItemStack> list =  new ArrayList<ItemStack>();
		if (state.getValue(BlockFieldChart.state) == 8){
			list.add(new ItemStack(this,1));
		}
		return list;
	}
	
	@Override
	public BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, state);
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(BlockFieldChart.state);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(state,meta);
	}
	
	public void breakBlockSafe(World world, BlockPos pos, EntityPlayer player){
		if (world.getTileEntity(pos) instanceof ITileEntityBase){
			((ITileEntityBase)world.getTileEntity(pos)).breakBlock(world, pos, world.getBlockState(pos), player);
		}
		if (world.getBlockState(pos).getBlock() == this){
			if (world.getBlockState(pos).getValue(BlockFieldChart.state) == 8){
				if (!world.isRemote && !player.capabilities.isCreativeMode){
					world.spawnEntity(new EntityItem(world,pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5,new ItemStack(this,1,0)));
				}
			}
		}
		world.setBlockToAir(pos);
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player){
		if (state.getValue(BlockFieldChart.state) == 9){
			breakBlockSafe(world,pos.south(),player);
			breakBlockSafe(world,pos.south(2),player);
			breakBlockSafe(world,pos.east(),player);
			breakBlockSafe(world,pos.west(),player);
			breakBlockSafe(world,pos.east().south(),player);
			breakBlockSafe(world,pos.west().south(),player);
			breakBlockSafe(world,pos.east().south(2),player);
			breakBlockSafe(world,pos.west().south(2),player);
		}
		if (state.getValue(BlockFieldChart.state) == 1){
			breakBlockSafe(world,pos.east(),player);
			breakBlockSafe(world,pos.east(2),player);
			breakBlockSafe(world,pos.south(),player);
			breakBlockSafe(world,pos.south(2),player);
			breakBlockSafe(world,pos.east().south(),player);
			breakBlockSafe(world,pos.east(2).south(),player);
			breakBlockSafe(world,pos.east().south(2),player);
			breakBlockSafe(world,pos.east(2).south(2),player);
		}
		if (state.getValue(BlockFieldChart.state) == 2){
			breakBlockSafe(world,pos.east(),player);
			breakBlockSafe(world,pos.east(2),player);
			breakBlockSafe(world,pos.north(),player);
			breakBlockSafe(world,pos.south(),player);
			breakBlockSafe(world,pos.north().east(),player);
			breakBlockSafe(world,pos.south().east(),player);
			breakBlockSafe(world,pos.north().east(2),player);
			breakBlockSafe(world,pos.south().east(2),player);
		}
		if (state.getValue(BlockFieldChart.state) == 3){
			breakBlockSafe(world,pos.east(),player);
			breakBlockSafe(world,pos.east(2),player);
			breakBlockSafe(world,pos.north(),player);
			breakBlockSafe(world,pos.north(2),player);
			breakBlockSafe(world,pos.east().north(),player);
			breakBlockSafe(world,pos.east(2).north(),player);
			breakBlockSafe(world,pos.east().north(2),player);
			breakBlockSafe(world,pos.east(2).north(2),player);
		}
		if (state.getValue(BlockFieldChart.state) == 4){
			breakBlockSafe(world,pos.north(),player);
			breakBlockSafe(world,pos.north(2),player);
			breakBlockSafe(world,pos.east(),player);
			breakBlockSafe(world,pos.west(),player);
			breakBlockSafe(world,pos.east().north(),player);
			breakBlockSafe(world,pos.west().north(),player);
			breakBlockSafe(world,pos.east().north(2),player);
			breakBlockSafe(world,pos.west().north(2),player);
		}
		if (state.getValue(BlockFieldChart.state) == 5){
			breakBlockSafe(world,pos.west(),player);
			breakBlockSafe(world,pos.west(2),player);
			breakBlockSafe(world,pos.north(),player);
			breakBlockSafe(world,pos.north(2),player);
			breakBlockSafe(world,pos.west().north(),player);
			breakBlockSafe(world,pos.west(2).north(),player);
			breakBlockSafe(world,pos.west().north(2),player);
			breakBlockSafe(world,pos.west(2).north(2),player);
		}
		if (state.getValue(BlockFieldChart.state) == 6){
			breakBlockSafe(world,pos.west(),player);
			breakBlockSafe(world,pos.west(2),player);
			breakBlockSafe(world,pos.north(),player);
			breakBlockSafe(world,pos.south(),player);
			breakBlockSafe(world,pos.north().west(),player);
			breakBlockSafe(world,pos.south().west(),player);
			breakBlockSafe(world,pos.north().west(2),player);
			breakBlockSafe(world,pos.south().west(2),player);
		}
		if (state.getValue(BlockFieldChart.state) == 7){
			breakBlockSafe(world,pos.west(),player);
			breakBlockSafe(world,pos.west(2),player);
			breakBlockSafe(world,pos.south(),player);
			breakBlockSafe(world,pos.south(2),player);
			breakBlockSafe(world,pos.west().south(),player);
			breakBlockSafe(world,pos.west(2).south(),player);
			breakBlockSafe(world,pos.west().south(2),player);
			breakBlockSafe(world,pos.west(2).south(2),player);
		}
		if (state.getValue(BlockFieldChart.state) == 8){
			breakBlockSafe(world,pos.west(),player);
			breakBlockSafe(world,pos.south(),player);
			breakBlockSafe(world,pos.west().south(),player);
			breakBlockSafe(world,pos.west().north(),player);
			breakBlockSafe(world,pos.south().east(),player);
			breakBlockSafe(world,pos.north().east(),player);
			breakBlockSafe(world,pos.north(),player);
			breakBlockSafe(world,pos.east(),player);
		}
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos){
		if (world.isAirBlock(pos.east())
			&& world.isAirBlock(pos.west())
			&& world.isAirBlock(pos.north())
			&& world.isAirBlock(pos.south())
			&& world.isAirBlock(pos.east().north())
			&& world.isAirBlock(pos.east().south())
			&& world.isAirBlock(pos.west().north())
			&& world.isAirBlock(pos.west().south())){
			return true;
		}
		return false;
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		if (state.getValue(BlockFieldChart.state) == 0){
			world.setBlockState(pos, getStateFromMeta(8));
			world.setBlockState(pos.north(), getStateFromMeta(9));
			world.setBlockState(pos.north().west(), getStateFromMeta(1));
			world.setBlockState(pos.west(), getStateFromMeta(2));
			world.setBlockState(pos.south().west(), getStateFromMeta(3));
			world.setBlockState(pos.south(), getStateFromMeta(4));
			world.setBlockState(pos.south().east(), getStateFromMeta(5));
			world.setBlockState(pos.east(), getStateFromMeta(6));
			world.setBlockState(pos.north().east(), getStateFromMeta(7));
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		if (meta == 8){
			return new TileEntityFieldChart();
		}
		return null;
	}
}
