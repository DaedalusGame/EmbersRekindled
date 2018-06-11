package teamroots.embers.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import teamroots.embers.tileentity.TileEntityCopperCell;
import teamroots.embers.tileentity.TileEntityCreativeEmberSource;
import teamroots.embers.tileentity.TileEntityCreativeMechSource;

public class BlockCreativeEmberSource extends BlockTEBase {
	public BlockCreativeEmberSource(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCreativeEmberSource();
	}
}
