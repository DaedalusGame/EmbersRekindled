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

public class MessageMetallurgicDustFX implements IMessage {
	public static Random random = new Random();
	int posX = 0, posY = 0, posZ = 0;

	public MessageMetallurgicDustFX(){
		super();
	}

	public MessageMetallurgicDustFX(int x, int y, int z){
		super();
		this.posX = x;
		this.posY = y;
		this.posZ = z;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		posX = buf.readInt();
		posY = buf.readInt();
		posZ = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
	}

    public static class MessageHolder implements IMessageHandler<MessageMetallurgicDustFX,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageMetallurgicDustFX message, final MessageContext ctx) {
    		World world = Minecraft.getMinecraft().world;
			if (world.isRemote){
				Minecraft.getMinecraft().addScheduledTask(()-> {
					for (double i = 0; i < 6; i++) {
						ParticleUtil.spawnParticleSpark(world, message.posX + random.nextFloat(), message.posY + random.nextFloat(), message.posZ + random.nextFloat(), 0.0125f * (random.nextFloat() - 0.5f), 0.0125f * (random.nextFloat() - 0.5f), 0.0125f * (random.nextFloat() - 0.5f), 255, 64, 16, 3.0f + random.nextFloat(), 3+random.nextInt(10));
					}
				});
			}
    		return null;
        }
    }

}
