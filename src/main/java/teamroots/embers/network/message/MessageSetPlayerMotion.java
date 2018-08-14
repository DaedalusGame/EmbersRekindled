package teamroots.embers.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;
import java.util.UUID;

public class MessageSetPlayerMotion implements IMessage {
	public static Random random = new Random();
	double posX = 0, posY = 0, posZ = 0;
	public UUID id = null;
	
	public MessageSetPlayerMotion(){
		super();
	}
	
	public MessageSetPlayerMotion(UUID id, double x, double y, double z){
		super();
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.id = id;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		posX = buf.readDouble();
		posY = buf.readDouble();
		posZ = buf.readDouble();
		id = new UUID(buf.readLong(),buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
		buf.writeLong(id.getMostSignificantBits());
		buf.writeLong(id.getLeastSignificantBits());
	}

    public static class MessageHolder implements IMessageHandler<MessageSetPlayerMotion,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageSetPlayerMotion message, final MessageContext ctx) {
    		World world = Minecraft.getMinecraft().world;
			Minecraft.getMinecraft().addScheduledTask(()-> {
				EntityPlayer p = world.getPlayerEntityByUUID(message.id);
				if (p != null) {
					p.motionX = message.posX;
					p.motionY = message.posY;
					p.motionZ = message.posZ;
				}
			});
    		return null;
        }
    }

}
