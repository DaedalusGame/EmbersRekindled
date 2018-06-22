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
		buf.writeInt(posX);
		buf.writeInt(posY);
		buf.writeInt(posZ);
	}

    public static class MessageHolder implements IMessageHandler<MessageMetallurgicDustFX,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageMetallurgicDustFX message, final MessageContext ctx) {
    		World world = Minecraft.getMinecraft().world;
			if (world.isRemote){
				Minecraft.getMinecraft().addScheduledTask(()-> {
					for (double i = 0; i < 20; i++) {
						float offsetX = random.nextFloat();
						float offsetY = random.nextFloat();
						float offsetZ = random.nextFloat();
						ParticleUtil.spawnParticleSpark(world, message.posX + offsetX, message.posY + offsetY, message.posZ + offsetZ, 0.1f * (offsetX - 0.5f), 0.1f * (offsetY - 0.5f), 0.1f * (offsetZ - 0.5f), 255, 64, 16, 10.0f + random.nextFloat(), 12+random.nextInt(12));
					}
				});
			}
    		return null;
        }
    }

}
