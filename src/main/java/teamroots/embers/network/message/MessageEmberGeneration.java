package teamroots.embers.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.world.EmberWorldData;

public class MessageEmberGeneration implements IMessage {
	String key = "";
	double ember = 0;
	
	public MessageEmberGeneration(){
		//
	}
	
	public MessageEmberGeneration(String key, double ember){
		this.key = key;
		this.ember = ember;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		key = ByteBufUtils.readUTF8String(buf);
		ember = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, key);
		buf.writeDouble(ember);
	}

    public static class MessageHolder implements IMessageHandler<MessageEmberGeneration,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageEmberGeneration message, final MessageContext ctx) {
        	EmberWorldData data = EmberWorldData.get(Minecraft.getMinecraft().theWorld);
        	if (data.emberData.containsKey(message.key)){
            	data.emberData.replace(message.key, message.ember);
        	}
        	else {
            	data.emberData.put(message.key, message.ember);
        	}
        	data.markDirty();
    		return null;
        }
    }

}
