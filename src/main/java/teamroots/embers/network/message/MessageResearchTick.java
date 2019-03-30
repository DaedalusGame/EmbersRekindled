package teamroots.embers.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.research.ResearchManager;
import teamroots.embers.research.capability.IResearchCapability;

public class MessageResearchTick implements IMessage {
    public static final int NAME_MAX_LENGTH = 64;
    String research;
    boolean ticked;

    public MessageResearchTick() {
    }

    public MessageResearchTick(String research, boolean ticked) {
        this.research = research;
        this.ticked = ticked;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        research = buffer.readString(NAME_MAX_LENGTH);
        ticked = buffer.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeString(research);
        buffer.writeBoolean(ticked);
    }

    public static class MessageHolder implements IMessageHandler<MessageResearchTick, IMessage> {
        @Override
        public IMessage onMessage(final MessageResearchTick message, final MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            WorldServer world = player.getServerWorld();
            world.addScheduledTask(() -> {
                IResearchCapability research = ResearchManager.getPlayerResearch(player);
                if(research != null) {
                    research.setCheckmark(message.research,message.ticked);
                    ResearchManager.sendResearchData(player);
                }
            });
            return null;
        }
    }
}
