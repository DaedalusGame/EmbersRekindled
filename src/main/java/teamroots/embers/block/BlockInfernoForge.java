package teamroots.embers.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.RegistryManager;
import teamroots.embers.tileentity.TileEntityInfernoForge;
import teamroots.embers.tileentity.TileEntityInfernoForgeOpening;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockInfernoForge extends BlockTEBase {
	public static final AxisAlignedBB AABB_BASE = new AxisAlignedBB(0,0,0,1,0.75,1);
	public static final AxisAlignedBB AABB_TOP = new AxisAlignedBB(0,0.5,0,1,0.75,1);
	public static final AxisAlignedBB AABB_TOP2 = new AxisAlignedBB(0,0,0,1,0.25,1);
	public static final AxisAlignedBB AABB_NULL = new AxisAlignedBB(0,0,0,0,0,0);
	public static final PropertyBool isTop = PropertyBool.create("top");
	
	public BlockInfernoForge(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}

    @Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean b)
    {
    	if (state.getValue(isTop)){
    		TileEntity t = worldIn.getTileEntity(pos);
    		if (t instanceof TileEntityInfernoForgeOpening){
    			if (((TileEntityInfernoForgeOpening)t).openAmount > 0.0f){
    				addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_NULL);
    			}
    			else {
    				addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_TOP);
    				addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_TOP2);
    			}
    		}
    	}
    	else {
    		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BASE);
    	}
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		if (meta == 0){
			return new TileEntityInfernoForge();
		}
		if (meta == 1){
			return new TileEntityInfernoForgeOpening();
		}
		return null;
	}
	
	@Override
	public BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, isTop);
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		boolean top = state.getValue(isTop);
		return top ? 1 : 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(isTop,meta == 1 ? true : false);
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos){
		if (isReplaceable(world,pos.east())
				&& isReplaceable(world,pos.up())
				&& isReplaceable(world,pos.west())
				&& isReplaceable(world,pos.north())
				&& isReplaceable(world,pos.south())
				&& isReplaceable(world,pos.east().north())
				&& isReplaceable(world,pos.east().south())
				&& isReplaceable(world,pos.west().north())
				&& isReplaceable(world,pos.west().south())
				&& isReplaceable(world,pos.east().up())
				&& isReplaceable(world,pos.west().up())
				&& isReplaceable(world,pos.north().up())
				&& isReplaceable(world,pos.south().up())
				&& isReplaceable(world,pos.east().north().up())
				&& isReplaceable(world,pos.east().south().up())
				&& isReplaceable(world,pos.west().north().up())
				&& isReplaceable(world,pos.west().south().up())){
			return super.canPlaceBlockAt(world,pos);
		}
		return false;
	}

	private boolean isReplaceable(World world, BlockPos pos) {
		return world.getBlockState(pos).getBlock().isReplaceable(world, pos);
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		if (!state.getValue(isTop)){
			world.setBlockState(pos.up(), RegistryManager.inferno_forge.getStateFromMeta(1));
			world.setBlockState(pos.north(), RegistryManager.inferno_forge_edge.getStateFromMeta(0));
			world.setBlockState(pos.north().west(), RegistryManager.inferno_forge_edge.getStateFromMeta(1));
			world.setBlockState(pos.west(), RegistryManager.inferno_forge_edge.getStateFromMeta(2));
			world.setBlockState(pos.south().west(), RegistryManager.inferno_forge_edge.getStateFromMeta(3));
			world.setBlockState(pos.south(), RegistryManager.inferno_forge_edge.getStateFromMeta(4));
			world.setBlockState(pos.south().east(), RegistryManager.inferno_forge_edge.getStateFromMeta(5));
			world.setBlockState(pos.east(), RegistryManager.inferno_forge_edge.getStateFromMeta(6));
			world.setBlockState(pos.north().east(), RegistryManager.inferno_forge_edge.getStateFromMeta(7));
			world.setBlockState(pos.north().up(), RegistryManager.inferno_forge_edge.getStateFromMeta(8));
			world.setBlockState(pos.north().west().up(), RegistryManager.inferno_forge_edge.getStateFromMeta(9));
			world.setBlockState(pos.west().up(), RegistryManager.inferno_forge_edge.getStateFromMeta(10));
			world.setBlockState(pos.south().west().up(), RegistryManager.inferno_forge_edge.getStateFromMeta(11));
			world.setBlockState(pos.south().up(), RegistryManager.inferno_forge_edge.getStateFromMeta(12));
			world.setBlockState(pos.south().east().up(), RegistryManager.inferno_forge_edge.getStateFromMeta(13));
			world.setBlockState(pos.east().up(), RegistryManager.inferno_forge_edge.getStateFromMeta(14));
			world.setBlockState(pos.north().east().up(), RegistryManager.inferno_forge_edge.getStateFromMeta(15));
		}
	}
	
	public void removeEdge(World world, BlockPos pos){
		if (world.getBlockState(pos).getBlock() == RegistryManager.inferno_forge_edge){
			world.setBlockToAir(pos);
			world.notifyBlockUpdate(pos, RegistryManager.inferno_forge_edge.getDefaultState(), Blocks.AIR.getDefaultState(), 8);
		}
		if (world.getBlockState(pos).getBlock() == this){
			world.setBlockToAir(pos);
			world.setTileEntity(pos, null);
			world.notifyBlockUpdate(pos, this.getDefaultState().withProperty(isTop, true), Blocks.AIR.getDefaultState(), 8);
		}
	}
	
	public void cleanEdges(World world, BlockPos pos){
		removeEdge(world,pos.north());
		removeEdge(world,pos.north().west());
		removeEdge(world,pos.west());
		removeEdge(world,pos.south().west());
		removeEdge(world,pos.south());
		removeEdge(world,pos.south().east());
		removeEdge(world,pos.east());
		removeEdge(world,pos.north().east());
		removeEdge(world,pos.north().up());
		removeEdge(world,pos.north().west().up());
		removeEdge(world,pos.west().up());
		removeEdge(world,pos.south().west().up());
		removeEdge(world,pos.south().up());
		removeEdge(world,pos.south().east().up());
		removeEdge(world,pos.east().up());
		removeEdge(world,pos.north().east().up());
		removeEdge(world,pos.up());
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		if (state.getBlock() == this){
			if (!state.getValue(isTop)){
				drops.add(new ItemStack(this,1));
			}
		}
		return drops;
	}
}
