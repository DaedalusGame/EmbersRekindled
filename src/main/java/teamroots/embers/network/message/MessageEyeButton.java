package teamroots.embers.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import teamroots.embers.gui.ContainerEye;

public class MessageEyeButton implements IMessage {
    ContainerEye.EnumButton button;

    public MessageEyeButton() {
        super();
    }

    public MessageEyeButton(ContainerEye.EnumButton button) {
        super();
        this.button = button;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pktBuf = new PacketBuffer(buf);
        button = pktBuf.readEnumValue(ContainerEye.EnumButton.class);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pktBuf = new PacketBuffer(buf);
        pktBuf.writeEnumValue(button);
    }

    public static class MessageHolder implements IMessageHandler<MessageEyeButton, IMessage> {
        @Override
        public IMessage onMessage(final MessageEyeButton message, final MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().player;
            WorldServer world = ctx.getServerHandler().player.getServerWorld();
            world.addScheduledTask(() -> {
                Container container = player.openContainer;
                if (container instanceof ContainerEye) {
                    ContainerEye eye = (ContainerEye) container;
                    eye.triggerButton(message.button);
                }
            });
            return null;
        }
    }
}
