package teamroots.embers.block;

import java.util.List;
import java.util.Random;

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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.tileentity.ITileEntityBase;
import teamroots.embers.tileentity.TileEntityBin;
import teamroots.embers.tileentity.TileEntityLantern;
import teamroots.embers.tileentity.TileEntityTank;

public class BlockLantern extends BlockTEBase {
	public static AxisAlignedBB AABB_BASE = new AxisAlignedBB(0.25,0,0.25,0.75,13.0/16.0,0.75);
	
	public BlockLantern(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return AABB_BASE;
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityLantern();
	}
}
