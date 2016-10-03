package teamroots.embers.network.message;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.particle.ParticleUtil;
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
    		return new MessageEmberData(EmberWorldData.get(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(message.id).getEntityWorld()));
        }
    }

}
