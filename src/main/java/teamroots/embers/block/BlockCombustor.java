package teamroots.embers.block;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.tileentity.ITileEntityBase;
import teamroots.embers.tileentity.TileEntityActivatorBottom;
import teamroots.embers.tileentity.TileEntityActivatorTop;
import teamroots.embers.tileentity.TileEntityCombustor;

public class BlockCombustor extends BlockTEBase {
	public static AxisAlignedBB AABB_BASE = new AxisAlignedBB(0,0,0,1,1,1);
	public static AxisAlignedBB AABB_TOP = new AxisAlignedBB(0.125,0,0.125,0.875,0.75,0.875);
	public static final PropertyInteger type = PropertyInteger.create("type",0,5);
	
	public BlockCombustor(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}

    @Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean b)
    {
    	if (state.getValue(type) == 0){
    		collidingBoxes.add(AABB_BASE);
    	}
    	else {
    		collidingBoxes.add(AABB_TOP);
    	}
    }
	
	@Override
	public BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, type);
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(type);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(type, meta);
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		if (this.getMetaFromState(state) == 0){
			world.setBlockState(pos.up(), this.getDefaultState().withProperty(type, 1));
		}
		else {
			world.setBlockState(pos.down(), this.getStateFromMeta(0));
		}
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
		return new ArrayList<ItemStack>();
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player){
		if (!world.isRemote && !player.capabilities.isCreativeMode){
			world.spawnEntity(new EntityItem(world,pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5,new ItemStack(this,1,0)));
		}
		if (this.getMetaFromState(state) == 0){
			world.setBlockToAir(pos.up());
			((ITileEntityBase)world.getTileEntity(pos)).breakBlock(world,pos,state,player);
		}
		else {
			world.setBlockToAir(pos.down());
		}
	}
	
	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion){
		if (!world.isRemote){
			world.spawnEntity(new EntityItem(world,pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5,new ItemStack(this,1,0)));
		}
		IBlockState state = world.getBlockState(pos);
		if (this.getMetaFromState(state) == 0){
			world.setBlockToAir(pos.up());
			((ITileEntityBase)world.getTileEntity(pos)).breakBlock(world,pos,state,null);
		}
		else {
			world.setBlockToAir(pos.down());
		}
		world.setBlockToAir(pos);
	}
	
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos){
		if (world.getBlockState(pos.up()) == Blocks.AIR.getDefaultState()){
			return true;
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		if (meta == 0){
			return new TileEntityCombustor();
		}
		return null;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
		if (state.getValue(type) == 0){
			return ((ITileEntityBase)world.getTileEntity(pos)).activate(world,pos,state,player,hand,side,hitX,hitY,hitZ);
		}
		return false;
	}
}
