package teamroots.embers.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockNodeBase extends BlockTEBase {
    public static final PropertyDirection facing = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockNodeBase(Material material, String name, boolean addToTab) {
        super(material, name, addToTab);
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(facing).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(facing, EnumFacing.getHorizontal(meta));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing face, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing facing;
        if (face.getAxis() != EnumFacing.Axis.Y)
            facing = face.getOpposite();
        else
            facing = placer.getHorizontalFacing();
        return getDefaultState().withProperty(BlockNodeBase.facing, facing);
    }
}

