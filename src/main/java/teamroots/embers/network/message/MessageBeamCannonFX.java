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

public class MessageBeamCannonFX implements IMessage {
	public static Random random = new Random();
	double posX = 0, posY = 0, posZ = 0;
	double rayX = 0, rayY = 0, rayZ = 0;
	double hitDistance = Double.POSITIVE_INFINITY;

	public MessageBeamCannonFX(){
		super();
	}

	public MessageBeamCannonFX(double x, double y, double z, double rayX, double rayY, double rayZ, double hitDistance){
		super();
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.rayX = rayX;
		this.rayY = rayY;
		this.rayZ = rayZ;
		this.hitDistance = hitDistance;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		posX = buf.readDouble();
		posY = buf.readDouble();
		posZ = buf.readDouble();
		rayX = buf.readDouble();
		rayY = buf.readDouble();
		rayZ = buf.readDouble();
		hitDistance = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
		buf.writeDouble(rayX);
		buf.writeDouble(rayY);
		buf.writeDouble(rayZ);
		buf.writeDouble(hitDistance);
	}

    public static class MessageHolder implements IMessageHandler<MessageBeamCannonFX,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageBeamCannonFX message, final MessageContext ctx) {
			World world = Minecraft.getMinecraft().world;
    		Minecraft.getMinecraft().addScheduledTask(()-> {
				double rayX = message.rayX;
				double rayY = message.rayY;
				double rayZ = message.rayZ;
				for (double i = 0; i < 640.0; i++) {
					message.posX += 0.1 * rayX;
					message.posY += 0.1 * rayY;
					message.posZ += 0.1 * rayZ;
					ParticleUtil.spawnParticleStar(world, (float)message.posX, (float)message.posY, (float)message.posZ, 0.0125f*(random.nextFloat()-0.5f), 0.0125f*(random.nextFloat()-0.5f), 0.0125f*(random.nextFloat()-0.5f), 255, 64, 16, 5.0f, 60);
					if(i > message.hitDistance) {
						for (int k = 0; k < 80; k ++){
							ParticleUtil.spawnParticleGlow(world, (float)message.posX, (float)message.posY, (float)message.posZ, 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), 255, 64, 16, 8.0f, 60);
						}
						break;
					}
				}
	    	});
    		return null;
	    }
    }
}
