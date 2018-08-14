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
import teamroots.embers.util.Misc;

import java.util.Random;

public class MessageSuperheatFX implements IMessage {
	public static Random random = new Random();
	double posX = 0, posY = 0, posZ = 0;
	
	public MessageSuperheatFX(){
		super();
	}
	
	public MessageSuperheatFX(double x, double y, double z){
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

    public static class MessageHolder implements IMessageHandler<MessageSuperheatFX,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageSuperheatFX message, final MessageContext ctx) {
    		World world = Minecraft.getMinecraft().world;
			Minecraft.getMinecraft().addScheduledTask(()-> {
				for (int j = 0; j < 8; j++) {
					float pitch = random.nextFloat() * 360.0f;
					float yaw = random.nextFloat() * 360.0f;
					for (float i = 0; i < 0.8; i += 0.05f) {
						float tx = (float) message.posX + i * (float) Math.sin(Math.toRadians(yaw)) * (float) Math.sin(Math.toRadians(pitch));
						float ty = (float) message.posY + i * (float) Math.cos(Math.toRadians(pitch));
						float tz = (float) message.posZ + i * (float) Math.cos(Math.toRadians(yaw)) * (float) Math.sin(Math.toRadians(pitch));
						float coeff = i / 0.8f;
						ParticleUtil.spawnParticleGlow(world, tx, ty, tz, 0, 0, 0, 255, 64, 16, 0.5f * (1.0f - coeff), 6.0f * (1.0f - coeff), 20);
					}
				}
				for (int j = 0; j < 12; j++) {
					ParticleUtil.spawnParticleSpark(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0.125f * (Misc.random.nextFloat() - 0.5f), 0.125f * (Misc.random.nextFloat()), 0.125f * (Misc.random.nextFloat() - 0.5f), 255, 64, 16, 1.0f + Misc.random.nextFloat(), 60);
				}
			});
    		return null;
        }
    }

}
