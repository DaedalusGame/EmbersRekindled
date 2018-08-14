package teamroots.embers.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class MessageTEUpdateRequest implements IMessage {
    public long pos = 0;

    public MessageTEUpdateRequest() {
        //
    }

    public MessageTEUpdateRequest(BlockPos pos) {
        this.pos = pos.toLong();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = buf.readLong();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos);
    }

    public static class MessageHolder implements IMessageHandler<MessageTEUpdateRequest, IMessage> {
        @Override
        public IMessage onMessage(final MessageTEUpdateRequest message, final MessageContext ctx) {
            BlockPos pos = BlockPos.fromLong(message.pos);
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player != null && player.world != null) {
                TileEntity tileEntity = player.world.getTileEntity(pos);
                if (tileEntity != null) {
                    SPacketUpdateTileEntity packet = tileEntity.getUpdatePacket();
                    if (packet != null)
                        player.connection.sendPacket(packet);
                }
            }
            return null;
        }
    }
}
