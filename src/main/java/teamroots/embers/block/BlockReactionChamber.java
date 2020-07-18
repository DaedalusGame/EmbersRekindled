package teamroots.embers.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.embers.tileentity.TileEntityMiniBoiler;
import teamroots.embers.tileentity.TileEntityReactionChamber;

public class BlockReactionChamber extends BlockTEBase {
    public BlockReactionChamber(Material material, String name, boolean addToTab) {
        super(material, name, addToTab);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityReactionChamber();
    }
}

