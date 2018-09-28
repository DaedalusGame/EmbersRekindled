package teamroots.embers.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.item.block.ItemBlockTank;
import teamroots.embers.tileentity.TileEntityTank;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockTank extends BlockTEBase {
	public static AxisAlignedBB AABB_BASE = new AxisAlignedBB(0,0,0,1,0.25,1);
	public static AxisAlignedBB AABB_SIDE_WEST = new AxisAlignedBB(0,0,0,0.25,1.0,1.0);
	public static AxisAlignedBB AABB_SIDE_EAST = new AxisAlignedBB(0.75,0,0,1.0,1.0,1.0);
	public static AxisAlignedBB AABB_SIDE_NORTH = new AxisAlignedBB(0,0,0,1.0,1.0,0.25);
	public static AxisAlignedBB AABB_SIDE_SOUTH = new AxisAlignedBB(0,0,0.75,1.0,1.0,1.0);
	
	public BlockTank(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
		itemBlock = (new ItemBlockTank(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack){
		super.onBlockPlacedBy(world, pos, state, player, stack);
		if (stack.hasTagCompound()){
			TileEntityTank tile = (TileEntityTank)createNewTileEntity(world, getMetaFromState(state));
			world.setTileEntity(pos, tile);
			tile.getTank().readFromNBT(stack.getTagCompound());
			tile.markDirty();
		}
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		return items;
	}

    @Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean b)
    {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BASE);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_SIDE_WEST);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_SIDE_NORTH);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_SIDE_EAST);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_SIDE_SOUTH);
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityTank();
	}
}
