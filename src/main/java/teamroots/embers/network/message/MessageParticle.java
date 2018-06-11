package teamroots.embers.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.particle.ParticleUtil;

public class MessageParticle implements IMessage {
	String particleName = "";
	double x = 0, y = 0, z = 0;
	double vx = 0, vy = 0, vz = 0;
	double r = 0, g = 0, b = 0;
	
	public MessageParticle(String particleName, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b){
		this.particleName = particleName;
		this.x = x;
		this.y = y;
		this.z = z;
		this.vx = vx;
		this.vy = vy;
		this.vz = vz;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		particleName = ByteBufUtils.readUTF8String(buf);
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		vx = buf.readDouble();
		vy = buf.readDouble();
		vz = buf.readDouble();
		r = buf.readDouble();
		g = buf.readDouble();
		b = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, particleName);
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeDouble(vx);
		buf.writeDouble(vy);
		buf.writeDouble(vz);
		buf.writeDouble(r);
		buf.writeDouble(g);
		buf.writeDouble(b);
	}

    public static class MessageHolder implements IMessageHandler<MessageParticle,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageParticle message, final MessageContext ctx) {
        	ParticleUtil.spawnParticlesFromPacket(message, FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld());
        	return null;
        }
    }

}
