package teamroots.embers.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.List;

public class BlockQuartzOre extends BlockBase {
	
	public BlockQuartzOre(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
		return Blocks.QUARTZ_ORE.getDrops(world, pos, state, fortune);
	}
}
