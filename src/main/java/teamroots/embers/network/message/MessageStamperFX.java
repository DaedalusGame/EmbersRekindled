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

public class MessageStamperFX implements IMessage {
	public static Random random = new Random();
	double posX = 0, posY = 0, posZ = 0;
	
	public MessageStamperFX(){
		super();
	}
	
	public MessageStamperFX(double x, double y, double z){
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

    public static class MessageHolder implements IMessageHandler<MessageStamperFX,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageStamperFX message, final MessageContext ctx) {
    		World world = Minecraft.getMinecraft().world;
			Minecraft.getMinecraft().addScheduledTask(()-> {
				for (float a = 0; a < 360; a += random.nextFloat() * 40.0f) {
					float dx = 0.125f * (float) Math.sin(Math.toRadians(a));
					float dz = 0.125f * (float) Math.cos(Math.toRadians(a));
					ParticleUtil.spawnParticleSmoke(world, (float) message.posX + dx, (float) message.posY, (float) message.posZ + dz, dx * 0.125f, -0.015625f * (random.nextFloat()), dz * 0.125f, 128, 128, 128, 0.4f, random.nextFloat() * 4.0f + 4.0f, 40);
				}
				for (float a = 0; a < 1.0f; a += random.nextFloat() * 0.5f) {
					ParticleUtil.spawnParticleSpark(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0.125f * (random.nextFloat() - 0.5f), 0.0625f * (random.nextFloat()), 0.125f * (random.nextFloat() - 0.5f), 255, 64, 16, random.nextFloat() * 0.75f + 0.45f, 80);
				}
			});
    		return null;
        }
    }

}
