package teamroots.embers.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import teamroots.embers.tileentity.TileEntityMechActuatorSingle;

import javax.annotation.Nullable;

public class BlockMechActuatorSingle extends BlockMechActuator {
    public BlockMechActuatorSingle(Material material, String name, boolean addToTab) {
        super(material, name, addToTab);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityMechActuatorSingle();
    }
}
