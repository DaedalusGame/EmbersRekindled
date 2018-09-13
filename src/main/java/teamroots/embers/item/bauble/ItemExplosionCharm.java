package teamroots.embers.item.bauble;

import baubles.api.BaubleType;
import baubles.api.cap.BaublesCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageExplosionCharmFX;
import teamroots.embers.network.message.MessageFlameShieldFX;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.WeakHashMap;

public class ItemExplosionCharm extends ItemBaubleBase {
    public static final int COOLDOWN = 100;
    public static final int MERCY_TIME = 5;

    public static WeakHashMap<Entity, Integer> cooldownTicks = new WeakHashMap<>();
    public static WeakHashMap<Entity, Integer> mercyTicks = new WeakHashMap<>();

    public static void setCooldown(Entity entity, int ticks) {
        cooldownTicks.put(entity, ticks);
    }

    public static boolean hasCooldown(Entity entity) {
        return cooldownTicks.getOrDefault(entity, 0) > 0;
    }

    public static void setMercy(Entity entity, int ticks) {
        mercyTicks.put(entity, ticks);
    }

    public static boolean hasMercy(Entity entity) {
        return mercyTicks.getOrDefault(entity, 0) > 0;
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            for (Entity entity : cooldownTicks.keySet()) {
                Integer ticks = cooldownTicks.get(entity) - 1;
                cooldownTicks.put(entity, ticks);
            }
            for (Entity entity : mercyTicks.keySet()) {
                Integer ticks = mercyTicks.get(entity) - 1;
                mercyTicks.put(entity, ticks);
            }
        }
    }

    public ItemExplosionCharm(String name, boolean addToTab) {
        super(name, BaubleType.CHARM, addToTab);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onExplosion(ExplosionEvent.Start event) {
        if (event.getWorld().isRemote)
            return;
        Explosion explosion = event.getExplosion();
        float f3 = 4.0F;
        Vec3d explosionPos = explosion.getPosition();
        int k1 = MathHelper.floor(explosionPos.x - (double) f3 - 1.0D);
        int l1 = MathHelper.floor(explosionPos.x + (double) f3 + 1.0D);
        int i2 = MathHelper.floor(explosionPos.y - (double) f3 - 1.0D);
        int i1 = MathHelper.floor(explosionPos.y + (double) f3 + 1.0D);
        int j2 = MathHelper.floor(explosionPos.z - (double) f3 - 1.0D);
        int j1 = MathHelper.floor(explosionPos.z + (double) f3 + 1.0D);
        List<Entity> entities = event.getWorld().getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB((double) k1, (double) i2, (double) j2, (double) l1, (double) i1, (double) j1));

        for (Entity entity : entities) {
            if (entity.hasCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null)) {
                NonNullList<ItemStack> stacks = BaublesUtil.getBaubles(entity.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null), BaubleType.CHARM);
                ItemStack stack = stacks.get(0);
                if (stack.getItem() == this) {
                    if (!hasCooldown(entity) || hasMercy(entity)) {
                        PacketHandler.INSTANCE.sendToAll(new MessageExplosionCharmFX(explosionPos.x, explosionPos.y, explosionPos.z, entity.posX, entity.posY + entity.height / 2.0, entity.posZ));
                        event.setCanceled(true);
                        if (!hasCooldown(entity)) {
                            setCooldown(entity, COOLDOWN);
                            setMercy(entity, MERCY_TIME);
                        }
                    }
                }
            }
        }
    }
}
