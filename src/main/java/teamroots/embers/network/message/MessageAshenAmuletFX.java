package teamroots.embers.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.Embers;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.proxy.ClientProxy;

import java.util.Random;
import java.util.UUID;

public class MessageAshenAmuletFX implements IMessage {
    public static Random random = new Random();
    BlockPos pos;
    Integer entityID;

    public MessageAshenAmuletFX() {
    }

    public MessageAshenAmuletFX(BlockPos pos) {
        this.pos = pos;
    }

    public MessageAshenAmuletFX(Entity entity) {
        this.entityID = entity.getEntityId();
    }

    private int getType() {
        if ((pos != null) == (entityID != null))
            return 0;
        else if (pos != null)
            return 1;
        else
            return 2;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int type = buf.readInt();
        switch (type) {
            case 1:
                int posX = buf.readInt();
                int posY = buf.readInt();
                int posZ = buf.readInt();
                pos = new BlockPos(posX, posY, posZ);
                return;
            case 2:
                entityID = buf.readInt();
                return;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        int type = getType();
        buf.writeInt(type);
        switch (type) {
            case 1:
                buf.writeInt(pos.getX());
                buf.writeInt(pos.getY());
                buf.writeInt(pos.getZ());
                return;
            case 2:
                buf.writeInt(entityID);
                return;
        }
    }

    public static class MessageHolder implements IMessageHandler<MessageAshenAmuletFX, IMessage> {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageAshenAmuletFX message, final MessageContext ctx) {
            World world = Minecraft.getMinecraft().world;
            Minecraft.getMinecraft().addScheduledTask(() -> {
                switch (message.getType()) {
                    case 1:
                        ParticleUtil.spawnParticleAsh(world,message.pos,20);
                        return;
                    case 2:
                        Entity entity = world.getEntityByID(message.entityID);
                        ParticleUtil.spawnParticleAsh(world,entity,20);
                        return;
                }
            });
            return null;
        }
    }
}
