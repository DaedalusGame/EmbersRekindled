package teamroots.embers.block;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.RegistryManager;
import teamroots.embers.tileentity.ITileEntityBase;
import teamroots.embers.tileentity.TileEntityEmberBore;
import teamroots.embers.tileentity.TileEntityEmitter;
import teamroots.embers.tileentity.TileEntityPipe;
import teamroots.embers.tileentity.TileEntityTank;

public class BlockLargeTank extends BlockTEBase {
	
	public BlockLargeTank(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityEmberBore();
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
		world.setBlockState(pos.north(), RegistryManager.stoneEdge.getStateFromMeta(0));
		world.setBlockState(pos.north().west(), RegistryManager.stoneEdge.getStateFromMeta(1));
		world.setBlockState(pos.west(), RegistryManager.stoneEdge.getStateFromMeta(2));
		world.setBlockState(pos.south().west(), RegistryManager.stoneEdge.getStateFromMeta(3));
		world.setBlockState(pos.south(), RegistryManager.stoneEdge.getStateFromMeta(4));
		world.setBlockState(pos.south().east(), RegistryManager.stoneEdge.getStateFromMeta(5));
		world.setBlockState(pos.east(), RegistryManager.stoneEdge.getStateFromMeta(6));
		world.setBlockState(pos.north().east(), RegistryManager.stoneEdge.getStateFromMeta(7));
	}
}
