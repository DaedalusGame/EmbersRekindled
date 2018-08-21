package teamroots.embers.util;

import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;

public class FluidUtil {
	public static FluidStack getFluid(World world, BlockPos pos, IBlockState state){
		if (state.getBlock() instanceof IFluidBlock && ((IFluidBlock)state.getBlock()).canDrain(world, pos)){
			return new FluidStack(((BlockFluidBase)state.getBlock()).getFluid(),1000);
		}
		if (state.getBlock() instanceof BlockStaticLiquid){
			if (state.getValue(BlockStaticLiquid.LEVEL) == 0){
				if (state.getBlock() == Blocks.WATER){
					return new FluidStack(FluidRegistry.WATER,1000);
				}
				if (state.getBlock() == Blocks.LAVA){
					return new FluidStack(FluidRegistry.LAVA,1000);
				}
			}
		}
		
		return null;
	}

	public static boolean areFluidsEqual(Fluid a, Fluid b) {
		return a != null && b != null && a.getName().equals(b.getName());
	}
}
