package teamroots.embers.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
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
    Integer entityID;
    AxisAlignedBB aabb;

    public MessageAshenAmuletFX() {
    }

    public MessageAshenAmuletFX(Entity entity) {
        this.entityID = entity.getEntityId();
    }

    public MessageAshenAmuletFX(BlockPos pos) {
        this.aabb = new AxisAlignedBB(pos);
    }

    public MessageAshenAmuletFX(AxisAlignedBB aabb) {
        this.aabb = aabb;
    }

    private int getType() {
        if ((aabb != null) == (entityID != null))
            return 0;
        else if (aabb != null)
            return 1;
        else
            return 2;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int type = buf.readInt();
        switch (type) {
            case 1:
                double minX = buf.readDouble();
                double minY = buf.readDouble();
                double minZ = buf.readDouble();
                double maxX = buf.readDouble();
                double maxY = buf.readDouble();
                double maxZ = buf.readDouble();
                aabb = new AxisAlignedBB(minX,minY,minZ,maxX,maxY,maxZ);
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
                buf.writeDouble(aabb.minX);
                buf.writeDouble(aabb.minY);
                buf.writeDouble(aabb.minZ);
                buf.writeDouble(aabb.maxX);
                buf.writeDouble(aabb.maxY);
                buf.writeDouble(aabb.maxZ);
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
                        ParticleUtil.spawnParticleAsh(world,message.aabb,20);
                        return;
                    case 2:
                        Entity entity = world.getEntityByID(message.entityID);
                        if(entity != null)
                            ParticleUtil.spawnParticleAsh(world,entity,20);
                        return;
                }
            });
            return null;
        }
    }
}
