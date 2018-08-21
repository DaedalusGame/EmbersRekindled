package teamroots.embers.api.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public interface IExtraDialInformation {
    void addDialInformation(World world, BlockPos pos, IBlockState state, List<String> information, String dialType);
}
