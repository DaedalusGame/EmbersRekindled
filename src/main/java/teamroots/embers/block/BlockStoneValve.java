package teamroots.embers.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.embers.tileentity.ITileEntityBase;
import teamroots.embers.tileentity.TileEntityStoneValve;

import javax.annotation.Nullable;

public class BlockStoneValve extends BlockStoneEdge {
	public BlockStoneValve(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		int meta = state.getValue(BlockStoneEdge.state);
		switch(meta)
		{
			case(2):
			case(4):
			case(6):
			case(9): return true;
			default: return false;
		}
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityStoneValve();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity tile = world.getTileEntity(pos);
		return tile instanceof ITileEntityBase && ((ITileEntityBase) tile).activate(world, pos, state, player, hand, side, hitX, hitY, hitZ);
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player){
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ITileEntityBase)
			((ITileEntityBase)tile).breakBlock(world,pos,state,player);
		super.onBlockHarvested(world, pos, state, player);
	}

	public BlockPos getMainBlock(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		switch(state.getValue(BlockStoneValve.state))
		{
			case(1): return pos.south().east();
			case(2): return pos.east();
			case(3): return pos.north().east();
			case(4): return pos.north();
			case(5): return pos.north().west();
			case(6): return pos.west();
			case(7): return pos.south().west();
			case(9): return pos.south();
			default: return pos;
		}
	}
}
