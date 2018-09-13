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

public class MessageExplosionCharmFX implements IMessage {
    public static Random random = new Random();
    double posX = 0, posY = 0, posZ = 0;
    double absorbX = 0, absorbY = 0, absorbZ = 0;

    public MessageExplosionCharmFX() {
    }

    public MessageExplosionCharmFX(double posX, double posY, double posZ, double absorbX, double absorbY, double absorbZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.absorbX = absorbX;
        this.absorbY = absorbY;
        this.absorbZ = absorbZ;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        posX = buf.readDouble();
        posY = buf.readDouble();
        posZ = buf.readDouble();
        absorbX = buf.readDouble();
        absorbY = buf.readDouble();
        absorbZ = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(posX);
        buf.writeDouble(posY);
        buf.writeDouble(posZ);
        buf.writeDouble(absorbX);
        buf.writeDouble(absorbY);
        buf.writeDouble(absorbZ);
    }

    public static class MessageHolder implements IMessageHandler<MessageExplosionCharmFX, IMessage> {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageExplosionCharmFX message, final MessageContext ctx) {
            World world = Minecraft.getMinecraft().world;
            Minecraft.getMinecraft().addScheduledTask(() -> {
                for (int k = 0; k < 40; k++) {
                    int lifetime = 12 + random.nextInt(12);
                    double angleA = random.nextDouble() * Math.PI * 2;
                    double angleB = random.nextDouble() * Math.PI * 2;
                    double dist = random.nextDouble() * 1.0 + 0.5;
                    float xOffset = (float) (Math.cos(angleA) * Math.cos(angleB));
                    float yOffset = (float) (Math.sin(angleA) * Math.cos(angleB));
                    float zOffset = (float) Math.sin(angleB);
                    double x = message.posX + xOffset * dist;
                    double y = message.posY + yOffset * dist;
                    double z = message.posZ + zOffset * dist;
                    double vx = (message.absorbX - x) / lifetime;
                    double vy = (message.absorbY - y) / lifetime;
                    double vz = (message.absorbZ - z) / lifetime;
                    ParticleUtil.spawnParticleGlow(world, (float) x, (float) y, (float) z, (float) vx, (float) vy, (float) vz, 255, 64, 16, 2.0f + random.nextFloat() * 2.0f, lifetime);
                    float smokeSpeed = 0.15f;
                    ParticleUtil.spawnParticleSmoke(world, (float)(message.posX + xOffset * dist), (float)(message.posY + yOffset * dist), (float)(message.posZ + zOffset * dist), xOffset * smokeSpeed, yOffset * smokeSpeed, zOffset * smokeSpeed, 80, 80, 80, 160, 4.0f + random.nextFloat() * 20.0f, 20+random.nextInt(20));
                }
            });
            return null;
        }
    }
}
