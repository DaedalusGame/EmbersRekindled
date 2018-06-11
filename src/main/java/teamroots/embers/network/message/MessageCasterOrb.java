package teamroots.embers.network.message;

import java.util.Random;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import teamroots.embers.RegistryManager;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.entity.EntityEmberProjectile;
import teamroots.embers.api.itemmod.ModifierBase;
import teamroots.embers.itemmod.ModifierCasterOrb;
import teamroots.embers.util.EmberInventoryUtil;

public class MessageCasterOrb implements IMessage {
    public static Random random = new Random();
    double lookX = 0;
    double lookY = 0;
    double lookZ = 0;

    public MessageCasterOrb() {
        super();
    }

    public MessageCasterOrb(double lookX, double lookY, double lookZ) {
        super();
        this.lookX = lookX;
        this.lookY = lookY;
        this.lookZ = lookZ;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        lookX = buf.readDouble();
        lookY = buf.readDouble();
        lookZ = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(lookX);
        buf.writeDouble(lookY);
        buf.writeDouble(lookZ);
    }

    public static class MessageHolder implements IMessageHandler<MessageCasterOrb, IMessage> {
        @Override
        public IMessage onMessage(final MessageCasterOrb message, final MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().player;
            WorldServer world = ctx.getServerHandler().player.getServerWorld();
            world.addScheduledTask(() -> {
                ItemStack heldStack = player.getHeldItemMainhand();
                if (ItemModUtil.hasHeat(heldStack)) {
                    int level = ItemModUtil.getModifierLevel(heldStack, EmbersAPI.CASTER_ORB);
                    UUID uuid = player.getUniqueID();
                    if (level > 0 && EmberInventoryUtil.getEmberTotal(player) > EmbersAPI.CASTER_ORB.cost && !ModifierCasterOrb.hasCooldown(uuid)) {
                        float offX = 0.5f * (float) Math.sin(Math.toRadians(-player.rotationYaw - 90));
                        float offZ = 0.5f * (float) Math.cos(Math.toRadians(-player.rotationYaw - 90));
                        EmberInventoryUtil.removeEmber(player, EmbersAPI.CASTER_ORB.cost);
                        double lookDist = Math.sqrt(message.lookX * message.lookX + message.lookY * message.lookY + message.lookZ * message.lookZ);
                        if (lookDist == 0)
                            return;
                        double xVel = (message.lookX / lookDist) * 0.5;
                        double yVel = (message.lookY / lookDist) * 0.5;
                        double zVel = (message.lookZ / lookDist) * 0.5;
                        EntityEmberProjectile proj = new EntityEmberProjectile(world);
                        proj.initCustom(player.posX + offX, player.posY + player.getEyeHeight(), player.posZ + offZ, xVel, yVel, zVel, 8.0 * (Math.atan(0.6 * (level)) / (1.25)), uuid);
                        world.spawnEntity(proj);
                        ModifierCasterOrb.setCooldown(uuid, 20);
                    }
                }
            });
            return null;
        }
    }

}
