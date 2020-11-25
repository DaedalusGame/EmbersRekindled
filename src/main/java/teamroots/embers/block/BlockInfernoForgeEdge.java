package teamroots.embers.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.RegistryManager;
import teamroots.embers.tileentity.ITileEntityBase;

import java.util.ArrayList;

public class BlockInfernoForgeEdge extends BlockBase {
	public static final PropertyInteger state = PropertyInteger.create("state", 0, 15);
	
	public BlockInfernoForgeEdge(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}
	
	@Override
	public BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, state);
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(BlockInfernoForgeEdge.state);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(state,meta);
	}
	
	public static void breakBlockSafe(World world, BlockPos pos, EntityPlayer player){
		if (world.getTileEntity(pos) instanceof ITileEntityBase){
			((ITileEntityBase)world.getTileEntity(pos)).breakBlock(world, pos, world.getBlockState(pos), player);
			if (!world.isRemote && !player.capabilities.isCreativeMode){
				if (!world.getBlockState(pos).getValue(BlockInfernoForge.isTop)){
					world.spawnEntity(new EntityItem(world,pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5,new ItemStack(world.getBlockState(pos).getBlock())));
				}		
			}
		}
		world.setBlockToAir(pos);
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
		return new ArrayList<ItemStack>();
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list){
		
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player){
		for (int i = -1; i <= 1; i ++){
			for (int j = -1; j <= 0; j ++){
				for (int k = -1; k <= 1; k ++){
					if (world.getBlockState(pos.add(i,j,k)).getBlock() == RegistryManager.inferno_forge){
						if (!world.getBlockState(pos.add(i, j, k)).getValue(BlockInfernoForge.isTop)){
							breakBlockSafe(world,pos.add(i, j, k),player);
							break;  // we found The Forge
						}
					}
				}
			}
		}
	}

	@Override
	public Item getItemBlock() {
		return null;
	}
}
