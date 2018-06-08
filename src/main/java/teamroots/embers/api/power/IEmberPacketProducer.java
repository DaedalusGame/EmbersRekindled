package teamroots.embers.api.power;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface IEmberPacketProducer {
	void setTargetPosition(BlockPos pos, EnumFacing side);
}
