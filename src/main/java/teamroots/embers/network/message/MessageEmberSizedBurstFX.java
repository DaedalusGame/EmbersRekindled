package teamroots.embers.network.message;

import java.util.Random;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.particle.ParticleUtil;

public class MessageEmberSizedBurstFX implements IMessage {
	public static Random random = new Random();
	public double posX = 0, posY = 0, posZ = 0;
	public double value = 0;
	
	public MessageEmberSizedBurstFX(){
		super();
	}
	
	public MessageEmberSizedBurstFX(double x, double y, double z, double value){
		super();
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.value = value;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		posX = buf.readDouble();
		posY = buf.readDouble();
		posZ = buf.readDouble();
		value = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
		buf.writeDouble(value);
	}

    public static class MessageHolder implements IMessageHandler<MessageEmberSizedBurstFX,IMessage>
    {
    	@SideOnly(Side.CLIENT)
    	@Override
        public IMessage onMessage(final MessageEmberSizedBurstFX message, final MessageContext ctx) {
    		if (ctx.side == Side.CLIENT){
	    		Minecraft.getMinecraft().addScheduledTask(()-> {
	    			World world = Minecraft.getMinecraft().world;
					for (int k = 0; k < 80; k ++){
						ParticleUtil.spawnParticleGlow(world, (float)message.posX, (float)message.posY, (float)message.posZ, ((float)message.value/3.5f)*0.125f*(random.nextFloat()-0.5f), ((float)message.value/3.5f)*0.125f*(random.nextFloat()-0.5f), ((float)message.value/3.5f)*0.125f*(random.nextFloat()-0.5f), 255, 64, 16, 1.0f, (float)message.value, 24);
					}
	    		});
    		}
    		return null;
        }
    }

}
