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
import teamroots.embers.entity.EntityEmberProjectile;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.util.Misc;

public class MessageSpawnEmberProj implements IMessage {
	public static Random random = new Random();
	double posX = 0, posY = 0, posZ = 0;
	double motionX = 0, motionY = 0, motionZ = 0;
	double size = 0;
	
	public MessageSpawnEmberProj(){
		super();
	}
	
	public MessageSpawnEmberProj(double x, double y, double z, double vx, double vy, double vz, double size){
		super();
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.motionX = vx;
		this.motionY = vy;
		this.motionZ = vz;
		this.size = size;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		posX = buf.readDouble();
		posY = buf.readDouble();
		posZ = buf.readDouble();
		motionX = buf.readDouble();
		motionY = buf.readDouble();
		motionZ = buf.readDouble();
		size = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
		buf.writeDouble(motionX);
		buf.writeDouble(motionY);
		buf.writeDouble(motionZ);
		buf.writeDouble(size);
	}

    public static class MessageHolder implements IMessageHandler<MessageSpawnEmberProj,IMessage>
    {
        @Override
        public IMessage onMessage(final MessageSpawnEmberProj message, final MessageContext ctx) {
    		World world = ctx.getServerHandler().playerEntity.world;
    		EntityEmberProjectile proj = new EntityEmberProjectile(world);
    		proj.initCustom(message.posX, message.posY, message.posZ, message.motionX, message.motionY, message.motionZ, message.size, ctx.getServerHandler().playerEntity.getUniqueID());
			world.spawnEntity(proj);
			return null;
        }
    }

}
