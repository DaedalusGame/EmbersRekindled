package teamroots.embers.api.misc;

import net.minecraft.block.state.IBlockState;

public interface IMetalCoefficient {
    boolean matches(IBlockState state);

    double getCoefficient(IBlockState state);
}
