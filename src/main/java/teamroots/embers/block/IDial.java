package teamroots.embers.block;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IDial {
	public List<String> getDisplayInfo(World world, BlockPos pos, IBlockState state);
}
