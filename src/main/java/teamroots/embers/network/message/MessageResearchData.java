package teamroots.embers.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import teamroots.embers.research.ResearchManager;

import java.util.HashMap;
import java.util.Map;

public class MessageResearchData implements IMessage {
    public static final int NAME_MAX_LENGTH = 64;
    Map<String,Boolean> ticks;

    public MessageResearchData() {
        this.ticks = new HashMap<>();
    }

    public MessageResearchData(Map<String, Boolean> ticks) {
        this.ticks = ticks;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        int entries = buffer.readInt();
        for(int i = 0; i < entries; i++) {
            String key = buffer.readString(NAME_MAX_LENGTH);
            boolean value = buffer.readBoolean();
            ticks.put(key,value);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeInt(ticks.size());
        for (Map.Entry<String,Boolean> entry : ticks.entrySet()) {
            buffer.writeString(entry.getKey());
            buffer.writeBoolean(entry.getValue());
        }
    }

    public static class MessageHolder implements IMessageHandler<MessageResearchData, IMessage> {
        @Override
        public IMessage onMessage(final MessageResearchData message, final MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(()-> {
                ResearchManager.receiveResearchData(message.ticks);
            });
            return null;
        }
    }
}
