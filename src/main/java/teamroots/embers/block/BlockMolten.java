package teamroots.embers.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.Embers;
import teamroots.embers.block.IModeledBlock;

import javax.annotation.Nonnull;

public class BlockMolten extends BlockFluidClassic implements IModeledBlock {

    public BlockMolten(Fluid fluid) {
        super(fluid, Material.LAVA);

        setRegistryName(fluid.getName());
        setQuantaPerBlock(6);
        fluid.setBlock(this);
    }

    @Override
    public boolean shouldSideBeRendered(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
        IBlockState neighbor = world.getBlockState(pos.offset(side));
        return neighbor.getBlock() != this || super.shouldSideBeRendered(state, world, pos, side);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initModel() {
        final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Embers.MODID + ":fluid", stack.getFluid().getName());
        ModelLoader.setCustomStateMapper(this, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return modelResourceLocation;
            }
        });
    }
}
