package teamroots.embers.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.RegistryManager;
import teamroots.embers.tileentity.ITileEntityBase;

public class BlockAdvancedEdge extends BlockBase {
	public static final PropertyInteger state = PropertyInteger.create("state", 0, 9);
	
	public static final AxisAlignedBB AABB_BASE = new AxisAlignedBB(0,0,0,1,1,1);
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return AABB_BASE;
	}
	
	public BlockAdvancedEdge(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list){
		
	}
	
	@Override
	public BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, state);
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(BlockAdvancedEdge.state);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(state,meta);
	}
	
	public void breakBlockSafe(World world, BlockPos pos, EntityPlayer player){
		if (world.getTileEntity(pos) instanceof ITileEntityBase){
			((ITileEntityBase)world.getTileEntity(pos)).breakBlock(world, pos, world.getBlockState(pos), player);
		}
		world.setBlockToAir(pos);
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player){
		if (!world.isRemote && !player.capabilities.isCreativeMode){
			world.spawnEntity(new EntityItem(world,pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5,new ItemStack(RegistryManager.crystal_cell,1,0)));
		}
		if (state.getValue(BlockAdvancedEdge.state) == 9){
			breakBlockSafe(world,pos.south(),player);
			breakBlockSafe(world,pos.south(2),player);
			breakBlockSafe(world,pos.east(),player);
			breakBlockSafe(world,pos.west(),player);
			breakBlockSafe(world,pos.east().south(),player);
			breakBlockSafe(world,pos.west().south(),player);
			breakBlockSafe(world,pos.east().south(2),player);
			breakBlockSafe(world,pos.west().south(2),player);
		}
		if (state.getValue(BlockAdvancedEdge.state) == 1){
			breakBlockSafe(world,pos.east(),player);
			breakBlockSafe(world,pos.east(2),player);
			breakBlockSafe(world,pos.south(),player);
			breakBlockSafe(world,pos.south(2),player);
			breakBlockSafe(world,pos.east().south(),player);
			breakBlockSafe(world,pos.east(2).south(),player);
			breakBlockSafe(world,pos.east().south(2),player);
			breakBlockSafe(world,pos.east(2).south(2),player);
		}
		if (state.getValue(BlockAdvancedEdge.state) == 2){
			breakBlockSafe(world,pos.east(),player);
			breakBlockSafe(world,pos.east(2),player);
			breakBlockSafe(world,pos.north(),player);
			breakBlockSafe(world,pos.south(),player);
			breakBlockSafe(world,pos.north().east(),player);
			breakBlockSafe(world,pos.south().east(),player);
			breakBlockSafe(world,pos.north().east(2),player);
			breakBlockSafe(world,pos.south().east(2),player);
		}
		if (state.getValue(BlockAdvancedEdge.state) == 3){
			breakBlockSafe(world,pos.east(),player);
			breakBlockSafe(world,pos.east(2),player);
			breakBlockSafe(world,pos.north(),player);
			breakBlockSafe(world,pos.north(2),player);
			breakBlockSafe(world,pos.east().north(),player);
			breakBlockSafe(world,pos.east(2).north(),player);
			breakBlockSafe(world,pos.east().north(2),player);
			breakBlockSafe(world,pos.east(2).north(2),player);
		}
		if (state.getValue(BlockAdvancedEdge.state) == 4){
			breakBlockSafe(world,pos.north(),player);
			breakBlockSafe(world,pos.north(2),player);
			breakBlockSafe(world,pos.east(),player);
			breakBlockSafe(world,pos.west(),player);
			breakBlockSafe(world,pos.east().north(),player);
			breakBlockSafe(world,pos.west().north(),player);
			breakBlockSafe(world,pos.east().north(2),player);
			breakBlockSafe(world,pos.west().north(2),player);
		}
		if (state.getValue(BlockAdvancedEdge.state) == 5){
			breakBlockSafe(world,pos.west(),player);
			breakBlockSafe(world,pos.west(2),player);
			breakBlockSafe(world,pos.north(),player);
			breakBlockSafe(world,pos.north(2),player);
			breakBlockSafe(world,pos.west().north(),player);
			breakBlockSafe(world,pos.west(2).north(),player);
			breakBlockSafe(world,pos.west().north(2),player);
			breakBlockSafe(world,pos.west(2).north(2),player);
		}
		if (state.getValue(BlockAdvancedEdge.state) == 6){
			breakBlockSafe(world,pos.west(),player);
			breakBlockSafe(world,pos.west(2),player);
			breakBlockSafe(world,pos.north(),player);
			breakBlockSafe(world,pos.south(),player);
			breakBlockSafe(world,pos.north().west(),player);
			breakBlockSafe(world,pos.south().west(),player);
			breakBlockSafe(world,pos.north().west(2),player);
			breakBlockSafe(world,pos.south().west(2),player);
		}
		if (state.getValue(BlockAdvancedEdge.state) == 7){
			breakBlockSafe(world,pos.west(),player);
			breakBlockSafe(world,pos.west(2),player);
			breakBlockSafe(world,pos.south(),player);
			breakBlockSafe(world,pos.south(2),player);
			breakBlockSafe(world,pos.west().south(),player);
			breakBlockSafe(world,pos.west(2).south(),player);
			breakBlockSafe(world,pos.west().south(2),player);
			breakBlockSafe(world,pos.west(2).south(2),player);
		}
		if (state.getValue(BlockAdvancedEdge.state) == 8){
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
}
