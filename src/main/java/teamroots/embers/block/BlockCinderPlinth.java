package teamroots.embers.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import teamroots.embers.tileentity.TileEntityCinderPlinth;

public class BlockCinderPlinth extends BlockTEBase {
	
	public BlockCinderPlinth(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCinderPlinth();
	}
}
