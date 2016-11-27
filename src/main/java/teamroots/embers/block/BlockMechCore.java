package teamroots.embers.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import teamroots.embers.tileentity.TileEntityMechCore;

public class BlockMechCore extends BlockTEBase implements ITileEntityProvider {
	public static final PropertyDirection facing = PropertyDirection.create("facing");
	
	public BlockMechCore(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMechCore();
	}
}
