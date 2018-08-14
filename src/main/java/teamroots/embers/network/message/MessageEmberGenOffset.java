package teamroots.embers.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.util.EmberGenUtil;

import java.util.Random;

public class MessageEmberGenOffset implements IMessage {
	public static Random random = new Random();
	int offX = 0, offZ = 0;
	
	public MessageEmberGenOffset(){
		super();
	}
	
	public MessageEmberGenOffset(int x, int z){
		super();
		this.offX = x;
		this.offZ = z;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		offX = buf.readInt();
		offZ = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(offX);
		buf.writeInt(offZ);
	}

    public static class MessageHolder implements IMessageHandler<MessageEmberGenOffset,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageEmberGenOffset message, final MessageContext ctx) {
    		Minecraft.getMinecraft().addScheduledTask(()-> {
    			EmberGenUtil.offX = message.offX;
    			EmberGenUtil.offZ = message.offZ;
	    	});
    		return null;
        }
    }

}
