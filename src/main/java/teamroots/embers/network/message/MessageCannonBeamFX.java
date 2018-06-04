package teamroots.embers.network.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.particle.ParticleUtil;

public class MessageCannonBeamFX implements IMessage {
	public static Random random = new Random();
	double posX = 0, posY = 0, posZ = 0;
	double dX = 0, dY = 0, dZ = 0;
	double hitDistance = Double.POSITIVE_INFINITY;
	
	public MessageCannonBeamFX(){
		super();
	}
	
	public MessageCannonBeamFX(double x, double y, double z, double dX, double dY, double dZ, double hitDistance){
		super();
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.dX = dX;
		this.dY = dY;
		this.dZ = dZ;
		this.hitDistance = hitDistance;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		posX = buf.readDouble();
		posY = buf.readDouble();
		posZ = buf.readDouble();
		dX = buf.readDouble();
		dY = buf.readDouble();
		dZ = buf.readDouble();
		hitDistance = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
		buf.writeDouble(dX);
		buf.writeDouble(dY);
		buf.writeDouble(dZ);
		buf.writeDouble(hitDistance);
	}

    public static class MessageHolder implements IMessageHandler<MessageCannonBeamFX,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageCannonBeamFX message, final MessageContext ctx) {
    		World world = Minecraft.getMinecraft().world;
			Minecraft.getMinecraft().addScheduledTask(()-> {
				for (double i = 0; i < 384.0; i++) {
					for (int j = 0; j < 5; j++) {
						message.posX += 0.2 * i * message.dX / 384.0;
						message.posY += 0.2 * i * message.dY / 384.0;
						message.posZ += 0.2 * i * message.dZ / 384.0;
						ParticleUtil.spawnParticleGlow(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0, 0, 0, 255, 64, 16, 2.0f, 24);
					}
					if(i > message.hitDistance) {
						for (int k = 0; k < 80; k++) {
							ParticleUtil.spawnParticleGlow(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0.125f * (random.nextFloat() - 0.5f), 0.125f * (random.nextFloat() - 0.5f), 0.125f * (random.nextFloat() - 0.5f), 255, 64, 16, 2.0f, 24);
						}
						break;
					}
				}
			});
    		return null;
        }
    }

}
