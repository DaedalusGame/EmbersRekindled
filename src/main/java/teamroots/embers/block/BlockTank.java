package teamroots.embers.block;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.embers.tileentity.ITileEntityBase;
import teamroots.embers.tileentity.TileEntityTank;

public class BlockTank extends BlockTEBase {
	public static AxisAlignedBB AABB_BASE = new AxisAlignedBB(0,0,0,1,0.25,1);
	public static AxisAlignedBB AABB_SIDE_WEST = new AxisAlignedBB(0,0,0,0.25,1.0,1.0);
	public static AxisAlignedBB AABB_SIDE_EAST = new AxisAlignedBB(0.75,0,0,1.0,1.0,1.0);
	public static AxisAlignedBB AABB_SIDE_NORTH = new AxisAlignedBB(0,0,0,1.0,1.0,0.25);
	public static AxisAlignedBB AABB_SIDE_SOUTH = new AxisAlignedBB(0,0,0.75,1.0,1.0,1.0);
	
	public BlockTank(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}

    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn)
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
