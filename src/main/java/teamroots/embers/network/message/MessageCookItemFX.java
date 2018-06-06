package teamroots.embers.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class MessageCookItemFX implements IMessage {
    public static Random random = new Random();
    double posX = 0, posY = 0, posZ = 0;

    public MessageCookItemFX(){
        super();
    }

    public MessageCookItemFX(double x, double y, double z){
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

    public static class MessageHolder implements IMessageHandler<MessageCookItemFX,IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageCookItemFX message, final MessageContext ctx) {
            World world = Minecraft.getMinecraft().world;
            if (world.isRemote){
                Minecraft.getMinecraft().addScheduledTask(()-> {
                    for (int j = 0; j < 3; j++) {
                        if (random.nextBoolean()) {
                            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, message.posX, message.posY, message.posZ, 0, 0, 0, 0);
                        } else {
                            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, message.posX, message.posY, message.posZ, 0, 0, 0, 0);
                        }
                    }
                });
            }
            return null;
        }
    }

}
