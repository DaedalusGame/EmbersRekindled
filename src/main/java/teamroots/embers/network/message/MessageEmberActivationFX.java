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
import teamroots.embers.block.BlockEmberInjector;
import teamroots.embers.particle.ParticleUtil;

public class MessageEmberActivationFX implements IMessage {
	public static Random random = new Random();
	double posX = 0, posY = 0, posZ = 0;
	
	public MessageEmberActivationFX(){
		super();
	}
	
	public MessageEmberActivationFX(double x, double y, double z){
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

    public static class MessageHolder implements IMessageHandler<MessageEmberActivationFX,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageEmberActivationFX message, final MessageContext ctx) {
    		World world = Minecraft.getMinecraft().world;
			for (int k = 0; k < 80; k ++){
				ParticleUtil.spawnParticleLineGlow(world, (float)message.posX+0.25f*(random.nextFloat()-0.5f), (float)message.posY, (float)message.posZ+0.25f*(random.nextFloat()-0.5f), (float)message.posX+0.75f*(random.nextFloat()-0.5f), (float)message.posY+2.0f+0.75f*(random.nextFloat()-0.5f), (float)message.posZ+0.75f*(random.nextFloat()-0.5f), 255, 64, 16, 3.0f+random.nextFloat()*1.5f, 20+random.nextInt(20));
			}
			for (int k = 0; k < 20; k ++){
				ParticleUtil.spawnParticleSmoke(world, (float)message.posX+0.25f*(random.nextFloat()-0.5f), (float)message.posY, (float)message.posZ+0.25f*(random.nextFloat()-0.5f), 0.0625f*(random.nextFloat()-0.5f), 0.0625f+0.0625f*(random.nextFloat()-0.5f), 0.0625f*(random.nextFloat()-0.5f), 64, 64, 64, 0.5f, 4.0f+random.nextFloat()*4.0f, 60);
			}
    		for (float a = 0; a < 10; a += 1){
    			ParticleUtil.spawnParticleSpark(world, (float)message.posX, (float)message.posY+0.5f, (float)message.posZ, 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()), 0.125f*(random.nextFloat()-0.5f), 255, 64, 16, random.nextFloat()*0.75f+0.45f, 60+random.nextInt(20));
    		}
    		return null;
        }
    }

}
