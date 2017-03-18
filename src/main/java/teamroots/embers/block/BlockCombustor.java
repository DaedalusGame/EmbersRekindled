package teamroots.embers.block;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
import teamroots.embers.RegistryManager;
import teamroots.embers.tileentity.ITileEntityBase;
import teamroots.embers.tileentity.TileEntityActivatorBottom;
import teamroots.embers.tileentity.TileEntityActivatorTop;
import teamroots.embers.tileentity.TileEntityCombustor;
import teamroots.embers.tileentity.TileEntityPipe;

public class BlockCombustor extends BlockTEBase {
	public static AxisAlignedBB AABB_BASE = new AxisAlignedBB(0,0,0,1,1,1);
	public static AxisAlignedBB AABB_TOP = new AxisAlignedBB(0.125,0,0.125,0.875,0.75,0.875);
	public static final PropertyInteger type = PropertyInteger.create("type",0,5);
	
	public BlockCombustor(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
    	if (state.getValue(type) == 0){
    		return AABB_BASE;
    	}
    	return AABB_TOP;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos){
		if (state.getValue(type) == 1){
			if (world.getBlockState(fromPos).getBlock() == RegistryManager.reactor){
				if (fromPos.compareTo(pos.offset(EnumFacing.NORTH)) == 0){
					world.setBlockState(pos, getStateFromMeta(2));
					world.notifyBlockUpdate(pos, state, getStateFromMeta(2), 8);
				}
				if (fromPos.compareTo(pos.offset(EnumFacing.EAST)) == 0){
					world.setBlockState(pos, getStateFromMeta(3));
					world.notifyBlockUpdate(pos, state, getStateFromMeta(3), 8);
				}
				if (fromPos.compareTo(pos.offset(EnumFacing.SOUTH)) == 0){
					world.setBlockState(pos, getStateFromMeta(4));
					world.notifyBlockUpdate(pos, state, getStateFromMeta(4), 8);
				}
				if (fromPos.compareTo(pos.offset(EnumFacing.WEST)) == 0){
					world.setBlockState(pos, getStateFromMeta(5));
					world.notifyBlockUpdate(pos, state, getStateFromMeta(5), 8);
				}
			}
		}
		else if (this.getFacingFromMeta(state.getValue(type)) != EnumFacing.DOWN){
			BlockPos offPos = pos.offset(getFacingFromMeta(state.getValue(type)));
			if (offPos.compareTo(fromPos) == 0){
				if (world.getBlockState(offPos).getBlock() != RegistryManager.reactor){
					if (world.getBlockState(pos.offset(EnumFacing.NORTH)).getBlock() == RegistryManager.reactor){
						world.setBlockState(pos, getStateFromMeta(2));
						world.notifyBlockUpdate(pos, state, getStateFromMeta(2), 8);
					}
					else if (world.getBlockState(pos.offset(EnumFacing.EAST)).getBlock() == RegistryManager.reactor){
						world.setBlockState(pos, getStateFromMeta(3));
						world.notifyBlockUpdate(pos, state, getStateFromMeta(3), 8);
					}
					else if (world.getBlockState(pos.offset(EnumFacing.SOUTH)).getBlock() == RegistryManager.reactor){
						world.setBlockState(pos, getStateFromMeta(4));
						world.notifyBlockUpdate(pos, state, getStateFromMeta(4), 8);
					}
					else if (world.getBlockState(pos.offset(EnumFacing.WEST)).getBlock() == RegistryManager.reactor){
						world.setBlockState(pos, getStateFromMeta(5));
						world.notifyBlockUpdate(pos, state, getStateFromMeta(5), 8);
					}
					else {
						world.setBlockState(pos, getStateFromMeta(1));
						world.notifyBlockUpdate(pos, state, getStateFromMeta(1), 8);
					}
				}
			}
		}
	}
	
	public EnumFacing getFacingFromMeta(int meta){
		if (meta == 2){
			return EnumFacing.NORTH;
		}
		if (meta == 3){
			return EnumFacing.EAST;
		}
		if (meta == 4){
			return EnumFacing.SOUTH;
		}
		if (meta == 5){
			return EnumFacing.WEST;
		}
		return EnumFacing.DOWN;
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing face, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		return getDefaultState().withProperty(type, 0);
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
			if (world.getBlockState(pos.up().offset(EnumFacing.NORTH)).getBlock() == RegistryManager.reactor){
				world.setBlockState(pos.up(), getStateFromMeta(2));
				world.notifyBlockUpdate(pos.up(), state, getStateFromMeta(2), 8);
			}
			else if (world.getBlockState(pos.up().offset(EnumFacing.EAST)).getBlock() == RegistryManager.reactor){
				world.setBlockState(pos.up(), getStateFromMeta(3));
				world.notifyBlockUpdate(pos.up(), state, getStateFromMeta(3), 8);
			}
			else if (world.getBlockState(pos.up().offset(EnumFacing.SOUTH)).getBlock() == RegistryManager.reactor){
				world.setBlockState(pos.up(), getStateFromMeta(4));
				world.notifyBlockUpdate(pos.up(), state, getStateFromMeta(4), 8);
			}
			else if (world.getBlockState(pos.up().offset(EnumFacing.WEST)).getBlock() == RegistryManager.reactor){
				world.setBlockState(pos.up(), getStateFromMeta(5));
				world.notifyBlockUpdate(pos.up(), state, getStateFromMeta(5), 8);
			}
			else {
				world.setBlockState(pos.up(), getStateFromMeta(1));
				world.notifyBlockUpdate(pos.up(), state, getStateFromMeta(1), 8);
			}
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
		if (state.getValue(type) != 0 && world.getBlockState(pos.down()).getBlock() == this || state.getValue(type) == 0 && world.getBlockState(pos.up()).getBlock() == this){
			if (!world.isRemote && !player.capabilities.isCreativeMode){
				world.spawnEntity(new EntityItem(world,pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5,new ItemStack(this,1,0)));
			}
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
