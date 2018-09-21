package teamroots.embers.tileentity;

import net.minecraft.util.EnumFacing;

public interface IFluidPipePriority {
    int getPriority(EnumFacing facing);
}
