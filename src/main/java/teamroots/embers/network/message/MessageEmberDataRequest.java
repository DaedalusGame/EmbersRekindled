package teamroots.embers.network.message;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.world.EmberWorldData;

public class MessageEmberDataRequest implements IMessage {
	UUID id = null;
	int chunkCoordX = 0;
	int chunkCoordZ = 0;
	
	public MessageEmberDataRequest(){
		
	}
	
	public MessageEmberDataRequest(UUID id, int chunkCoordX, int chunkCoordZ){
		this.id = id;
		this.chunkCoordX = chunkCoordX;
		this.chunkCoordZ = chunkCoordZ;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		id = new UUID(buf.readLong(),buf.readLong());
		chunkCoordX = buf.readInt();
		chunkCoordZ = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(id.getMostSignificantBits());
		buf.writeLong(id.getLeastSignificantBits());
		buf.writeInt(chunkCoordX);
		buf.writeInt(chunkCoordZ);
	}

    public static class MessageHolder implements IMessageHandler<MessageEmberDataRequest,IMessage>
    {
        @Override
        public IMessage onMessage(final MessageEmberDataRequest message, final MessageContext ctx) {
        	EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(message.id);
        	if (player != null){
				EmberWorldData data = EmberWorldData.get(player.getEntityWorld());
				if (data != null){
					PacketHandler.INSTANCE.sendTo(new MessageEmberData(message.chunkCoordX, message.chunkCoordZ, data.getEmberForChunk(message.chunkCoordX, message.chunkCoordZ)), (EntityPlayerMP) player);
				}
        	}
    		return null;
        }
    }

}
