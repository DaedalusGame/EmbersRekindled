package teamroots.embers.block;

import java.util.List;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.tileentity.ITileEntityBase;
import teamroots.embers.tileentity.TileEntityDeepLine;
import teamroots.embers.tileentity.TileEntityTank;

public class BlockDeepLine extends BlockTEBase {
	public static AxisAlignedBB BASE_BB = new AxisAlignedBB(0,0,0,1,1,1);
	public static AxisAlignedBB MID1_BB = new AxisAlignedBB(0.3125,0,0.3125,0.6875,1.0,0.6875);
	public static AxisAlignedBB MID2_BB = new AxisAlignedBB(0.375,0,0.375,0.625,1,0.625);
	public static AxisAlignedBB TOP_BB = new AxisAlignedBB(0.25,0,0.25,0.75,0.75,0.75);
	public static final PropertyInteger height = PropertyInteger.create("height", 0, 3);
	public static final PropertyInteger facing = PropertyInteger.create("facing", 0, 3);
	
	public BlockDeepLine(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}
	
	@Override
	public BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, height, facing);
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(height) + 4*(state.getValue(facing));
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		if (state.getValue(height) == 0){
			world.setBlockState(pos.up(), this.getDefaultState().withProperty(height, 1).withProperty(facing, state.getValue(facing)));
			world.setBlockState(pos.up().up(), this.getDefaultState().withProperty(height, 2).withProperty(facing, state.getValue(facing)));
			world.setBlockState(pos.up().up().up(), this.getDefaultState().withProperty(height, 3).withProperty(facing, state.getValue(facing)));
		}
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player){
		if (!world.isRemote && !player.capabilities.isCreativeMode){
			world.spawnEntityInWorld(new EntityItem(world,pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5,new ItemStack(this,1,0)));
		}
		if (state.getValue(height) == 0){
			world.setBlockToAir(pos.up());
			world.setBlockToAir(pos.up().up());
			world.setBlockToAir(pos.up().up().up());
			((ITileEntityBase)world.getTileEntity(pos)).breakBlock(world,pos,state,player);
		}
		if (state.getValue(height) == 1){
			world.setBlockToAir(pos.down());
			world.setBlockToAir(pos.up());
			world.setBlockToAir(pos.up().up());
		}
		if (state.getValue(height) == 2){
			world.setBlockToAir(pos.down().down());
			world.setBlockToAir(pos.down());
			world.setBlockToAir(pos.up());
		}
		if (state.getValue(height) == 3){
			world.setBlockToAir(pos.down().down().down());
			world.setBlockToAir(pos.down().down());
			world.setBlockToAir(pos.down());
		}
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos){
		if (world.getBlockState(pos.up()) == Blocks.AIR.getDefaultState()
				&& world.getBlockState(pos.up().up()) == Blocks.AIR.getDefaultState()
				&& world.getBlockState(pos.up().up().up()) == Blocks.AIR.getDefaultState()){
			return true;
		}
		return false;
	}
	
	public static EnumFacing getFacing(IBlockState state){
		return EnumFacing.getFront(state.getValue(facing)+2);
	}
	
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing face, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		return getDefaultState().withProperty(height, 0).withProperty(facing,placer.getHorizontalFacing().getOpposite().getIndex()-2);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		switch (state.getValue(height)){
		case 0:
			return BASE_BB;
		case 1:
			return MID1_BB;
		case 2:
			return MID2_BB;
		case 3:
			return TOP_BB;
		}
		return BASE_BB;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
		return null;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(height,meta % 4).withProperty(facing, meta/4);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		if (meta % 4 == 0){
			return new TileEntityDeepLine();
		}
		return null;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ){
		if (state.getValue(height) == 0){
			return super.onBlockActivated(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
		}
		return false;
	}
}
