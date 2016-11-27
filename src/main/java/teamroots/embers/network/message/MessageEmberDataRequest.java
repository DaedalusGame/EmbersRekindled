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
	
	public MessageEmberDataRequest(){
		
	}
	
	public MessageEmberDataRequest(UUID id){
		this.id = id;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		id = new UUID(buf.readLong(),buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(id.getMostSignificantBits());
		buf.writeLong(id.getLeastSignificantBits());
	}

    public static class MessageHolder implements IMessageHandler<MessageEmberDataRequest,IMessage>
    {
        @Override
        public IMessage onMessage(final MessageEmberDataRequest message, final MessageContext ctx) {
        	EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(message.id);
    		for (int i = -16; i < 16; i ++){
    			for (int j = -16; j < 16; j ++){
    				EmberWorldData data = EmberWorldData.get(player.getEntityWorld());
    				if (data != null){
    					if (data.emberData.containsKey(""+(player.chunkCoordX+i)+" "+(player.chunkCoordZ+j))){
    						PacketHandler.INSTANCE.sendTo(new MessageEmberGeneration(""+(player.chunkCoordX+i)+" "+(player.chunkCoordZ+j),data.emberData.get(""+(player.chunkCoordX+i)+" "+(player.chunkCoordZ+j))), (EntityPlayerMP) player);
    					}
    				}
    			}
    		}
    		return null;
        }
    }

}
