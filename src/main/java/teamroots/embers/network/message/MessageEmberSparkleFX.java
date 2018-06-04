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

public class MessageEmberSparkleFX implements IMessage {
    public static Random random = new Random();
    double posX = 0, posY = 0, posZ = 0;

    public MessageEmberSparkleFX() {
        super();
    }

    public MessageEmberSparkleFX(double x, double y, double z) {
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

    public static class MessageHolder implements IMessageHandler<MessageEmberSparkleFX, IMessage> {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageEmberSparkleFX message, final MessageContext ctx) {
            World world = Minecraft.getMinecraft().world;
            if (world.isRemote) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    for (double i = 0; i < 18; i++) {
                        ParticleUtil.spawnParticleStar(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0.0125f * (random.nextFloat() - 0.5f), 0.0125f * (random.nextFloat() - 0.5f), 0.0125f * (random.nextFloat() - 0.5f), 255, 64, 16, 3.5f + 0.5f * random.nextFloat(), 40);
                    }
                });
            }
            return null;
        }
    }

}
