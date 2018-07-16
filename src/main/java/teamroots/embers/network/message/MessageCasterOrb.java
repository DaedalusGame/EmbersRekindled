package teamroots.embers.network.message;

import java.util.Random;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import teamroots.embers.RegistryManager;
import teamroots.embers.SoundManager;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.event.EmberProjectileEvent;
import teamroots.embers.api.event.EmberRemoveEvent;
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
                        float handmod = player.getPrimaryHand() == EnumHandSide.RIGHT ? 1.0f : -1.0f;
                        float offX = handmod * 0.5f * (float) Math.sin(Math.toRadians(-player.rotationYaw - 90));
                        float offZ = handmod * 0.5f * (float) Math.cos(Math.toRadians(-player.rotationYaw - 90));
                        EmberInventoryUtil.removeEmber(player, EmbersAPI.CASTER_ORB.cost);
                        double lookDist = Math.sqrt(message.lookX * message.lookX + message.lookY * message.lookY + message.lookZ * message.lookZ);
                        if (lookDist == 0)
                            return;
                        double xVel = (message.lookX / lookDist) * 0.5;
                        double yVel = (message.lookY / lookDist) * 0.5;
                        double zVel = (message.lookZ / lookDist) * 0.5;
                        double xOrigin = player.posX + offX;
                        double yOrigin = player.posY + player.getEyeHeight();
                        double zOrigin = player.posZ + offZ;
                        EmberProjectileEvent event = new EmberProjectileEvent(player,heldStack,new Vec3d(xOrigin, yOrigin, zOrigin),new Vec3d(xVel,yVel,zVel));
                        MinecraftForge.EVENT_BUS.post(event);
                        if(!event.isCanceled()) {
                            EntityEmberProjectile proj = new EntityEmberProjectile(world);
                            proj.initCustom(xOrigin, yOrigin, zOrigin, xVel, yVel, zVel, 8.0 * (Math.atan(0.6 * (level)) / (1.25)), uuid);
                            world.spawnEntity(proj);
                        }
                        world.playSound(null, xOrigin, yOrigin, zOrigin,SoundManager.FIREBALL, SoundCategory.PLAYERS, 1.0f, 1.0f);
                        ModifierCasterOrb.setCooldown(uuid, 20);
                    }
                }
            });
            return null;
        }
    }

}
