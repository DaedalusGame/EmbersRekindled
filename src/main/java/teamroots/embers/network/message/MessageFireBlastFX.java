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

import java.awt.*;

public class MessageFireBlastFX implements IMessage {
    double x, y, z;
    Color color;
    float scale;
    int lifetime;

    public MessageFireBlastFX() {
    }

    public MessageFireBlastFX(double x, double y, double z, Color color, float scale, int lifetime) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
        this.scale = scale;
        this.lifetime = lifetime;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        color = new Color(buf.readInt(),true);
        scale = buf.readFloat();
        lifetime = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeInt(color.getRGB());
        buf.writeFloat(scale);
        buf.writeInt(lifetime);
    }

    public static class MessageHolder implements IMessageHandler<MessageFireBlastFX, IMessage> {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageFireBlastFX message, final MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                World world = Minecraft.getMinecraft().world;
                ParticleUtil.spawnFireBlast(world,message.x,message.y,message.z,message.color,message.scale,message.lifetime);
            });
            return null;
        }
    }
}
