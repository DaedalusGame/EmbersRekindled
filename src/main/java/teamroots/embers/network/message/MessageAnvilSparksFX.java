package teamroots.embers.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.particle.ParticleUtil;

import java.util.Random;

public class MessageAnvilSparksFX implements IMessage {
	public static Random random = new Random();
	double posX = 0, posY = 0, posZ = 0;
	
	public MessageAnvilSparksFX(){
		super();
	}
	
	public MessageAnvilSparksFX(double x, double y, double z){
		super();
		this.posX = x;
		this.posY = y;
		this.posZ = z;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		posX = buf.readDouble();
		posY = buf.readDouble();
		posZ = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
	}

    public static class MessageHolder implements IMessageHandler<MessageAnvilSparksFX,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageAnvilSparksFX message, final MessageContext ctx) {
    		World world = Minecraft.getMinecraft().world;
			Minecraft.getMinecraft().addScheduledTask(()-> {
				for (float a = 0; a < 1.0f; a += random.nextFloat() * 4.0f) {
					if (a < random.nextFloat() * 4.0f) {
						ParticleUtil.spawnParticleSpark(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0.125f * (random.nextFloat() - 0.5f), 0.0625f * (random.nextFloat()), 0.125f * (random.nextFloat() - 0.5f), 255, 64, 16, random.nextFloat() * 0.75f + 0.45f, 80);
					}
				}
			});
    		return null;
        }
    }

}
