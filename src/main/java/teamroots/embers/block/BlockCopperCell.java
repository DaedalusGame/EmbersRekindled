package teamroots.embers.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.item.block.ItemBlockCell;
import teamroots.embers.item.block.ItemBlockTank;
import teamroots.embers.tileentity.TileEntityCopperCell;
import teamroots.embers.tileentity.TileEntityTank;

import java.util.ArrayList;
import java.util.List;

public class BlockCopperCell extends BlockTEBase {
	public BlockCopperCell(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
		itemBlock = (new ItemBlockCell(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack){
		super.onBlockPlacedBy(world, pos, state, player, stack);
		if (stack.hasTagCompound()){
			TileEntityCopperCell tile = (TileEntityCopperCell)createNewTileEntity(world, getMetaFromState(state));
			world.setTileEntity(pos, tile);
			if(stack.hasCapability(EmbersCapabilities.EMBER_CAPABILITY,null)) {
				IEmberCapability capability = stack.getCapability(EmbersCapabilities.EMBER_CAPABILITY,null);
				tile.capability.setEmberCapacity(capability.getEmberCapacity());
				tile.capability.setEmber(capability.getEmber());
			}
			tile.markDirty();
		}
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		return items;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCopperCell();
	}
}
