package teamroots.embers.tileentity;

import net.minecraft.util.EnumFacing;
import teamroots.embers.util.EnumPipeConnection;

public interface IFluidPipeConnectable {
    EnumPipeConnection getConnection(EnumFacing facing);
}
