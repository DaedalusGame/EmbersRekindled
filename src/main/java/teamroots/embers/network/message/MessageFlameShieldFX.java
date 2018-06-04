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
import teamroots.embers.util.Misc;

public class MessageFlameShieldFX implements IMessage {
	public static Random random = new Random();
	double posX = 0, posY = 0, posZ = 0;
	
	public MessageFlameShieldFX(){
		super();
	}
	
	public MessageFlameShieldFX(double x, double y, double z){
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

    public static class MessageHolder implements IMessageHandler<MessageFlameShieldFX,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageFlameShieldFX message, final MessageContext ctx) {
    		World world = Minecraft.getMinecraft().world;
			Minecraft.getMinecraft().addScheduledTask(()-> {
				for (int j = 0; j < 360; j += 18) {
					float offX = 0.65f * (float) Math.sin(Math.toRadians(j));
					float offZ = 0.65f * (float) Math.cos(Math.toRadians(j));
					if (random.nextBoolean()) {
						if (random.nextBoolean()) {
							ParticleUtil.spawnParticleGlow(world, (float) message.posX + offX, (float) message.posY, (float) message.posZ + offZ, 0.03125f * (random.nextFloat() - 0.5f), 0.03125f * (random.nextFloat() - 0.5f), 0.03125f * (random.nextFloat() - 0.5f), 255, 64, 16, random.nextFloat() * 4.0f, 40);
						} else {
							ParticleUtil.spawnParticleStar(world, (float) message.posX + offX, (float) message.posY, (float) message.posZ + offZ, 0.03125f * (random.nextFloat() - 0.5f), 0.03125f * (random.nextFloat() - 0.5f), 0.03125f * (random.nextFloat() - 0.5f), 255, 64, 16, random.nextFloat() * 4.0f, 40);
						}
					} else {
						ParticleUtil.spawnParticleGlow(world, (float) message.posX + offX, (float) message.posY, (float) message.posZ + offZ, 0.03125f * (random.nextFloat() - 0.5f), 0.0625f * (random.nextFloat()), 0.03125f * (random.nextFloat() - 0.5f), 255, 64, 16, 2.0f + random.nextFloat() * 6.0f, 40);
						ParticleUtil.spawnParticleGlow(world, (float) message.posX + offX, (float) message.posY, (float) message.posZ + offZ, 0.03125f * (random.nextFloat() - 0.5f), 0.125f * (random.nextFloat()), 0.03125f * (random.nextFloat() - 0.5f), 255, 64, 16, 2.0f + random.nextFloat() * 2.0f, 40);
					}
				}
			});
    		return null;
        }
    }

}
