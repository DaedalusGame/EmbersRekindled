package teamroots.embers.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.tileentity.ITileEntityBase;
import teamroots.embers.tileentity.TileEntityPumpBottom;
import teamroots.embers.tileentity.TileEntityPumpTop;
import teamroots.embers.util.Misc;

import java.util.ArrayList;
import java.util.List;

public class BlockPump extends BlockTEBase {
	public static final PropertyBool isTop = PropertyBool.create("top");
	public static final PropertyDirection facing = PropertyDirection.create("facing");
	
	public BlockPump(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}
	
	@Override
	public BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, isTop, facing);
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		boolean top = state.getValue(isTop);
		return (top ? 1 : 0) * 6 + state.getValue(facing).getIndex();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(facing, EnumFacing.getFront(meta % 6)).withProperty(isTop,meta >= 6 ? true : false);
	}
	
	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion){
		if (!world.isRemote){
			world.spawnEntity(new EntityItem(world,pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5,new ItemStack(this,1,0)));
		}
		IBlockState state = world.getBlockState(pos);
		if (this.getMetaFromState(state) < 6){
			world.setBlockToAir(pos.up());
		}
		else {
			world.setBlockToAir(pos.down());
		}
		((ITileEntityBase)world.getTileEntity(pos)).breakBlock(world,pos,state,null);
		world.setBlockToAir(pos);
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		if (this.getMetaFromState(state) < 6){
			world.setBlockState(pos.up(), this.getDefaultState().withProperty(isTop, true).withProperty(facing, state.getValue(facing)));
		}
		else {
			world.setBlockState(pos.down(), this.getDefaultState().withProperty(isTop, false).withProperty(facing, state.getValue(facing)));
		}
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing face, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		return getDefaultState().withProperty(facing, Misc.getOppositeFace(placer.getHorizontalFacing())).withProperty(isTop, false);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
		return new ArrayList<>();
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player){
		if (state.getValue(isTop) && world.getBlockState(pos.down()).getBlock() == this || !state.getValue(isTop) && world.getBlockState(pos.up()).getBlock() == this){
			if (!world.isRemote && !player.capabilities.isCreativeMode){
				world.spawnEntity(new EntityItem(world,pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5,new ItemStack(this,1,0)));
			}
		}
		if (this.getMetaFromState(state) < 6){
			world.setBlockToAir(pos.up());
		}
		else {
			world.setBlockToAir(pos.down());
		}
		((ITileEntityBase)world.getTileEntity(pos)).breakBlock(world,pos,state,player);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos){
		if (world.getBlockState(pos.up()) == Blocks.AIR.getDefaultState()){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side){
		return side != EnumFacing.UP;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		if (meta >= 6){
			return new TileEntityPumpTop();
		}
		return new TileEntityPumpBottom();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
		return ((ITileEntityBase)world.getTileEntity(pos)).activate(world,pos,state,player,hand,side,hitX,hitY,hitZ);
	}
}
