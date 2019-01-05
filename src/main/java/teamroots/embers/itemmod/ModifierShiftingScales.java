package teamroots.embers.itemmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.ConfigManager;
import teamroots.embers.SoundManager;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.event.ScaleEvent;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.api.itemmod.ModifierBase;
import teamroots.embers.util.RenderUtil;

import java.util.*;

public class ModifierShiftingScales extends ModifierBase {
    private static class ShardParticle {
        double x;
        double y;
        int frame;
        double xSpeed;
        double ySpeed;
        double gravity;

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public int getFrame() {
            return frame;
        }

        public ShardParticle(double x, double y, int frame, double xSpeed, double ySpeed, double gravity) {
            this.x = x;
            this.y = y;
            this.frame = frame;
            this.xSpeed = xSpeed;
            this.ySpeed = ySpeed;
            this.gravity = gravity;
        }

        public void update() {
            x += xSpeed;
            y += ySpeed;

            if (ySpeed < 12)
                ySpeed += gravity;

            frame++;
        }
    }

    public static final ResourceLocation TEXTURE_HUD = new ResourceLocation("embers:textures/gui/icons.png");

    //Server
    public static final IAttribute SCALES = new RangedAttribute(null, "generic.scales", 0.0D, 0.0D, 2048.0D).setShouldWatch(true);
    public static final int COOLDOWN = 33;
    public static final double MOVE_PER_SECOND_THRESHOLD = 0.5;

    public static HashSet<String> unaffectedDamageTypes = new HashSet<>();
    public static HashMap<UUID, Integer> cooldownTicksServer = new HashMap<>();
    public static HashMap<UUID, Vec3d> lastPositionServer = new HashMap<>();

    //Client
    public static ArrayList<ShardParticle> shards = new ArrayList<>();
    public static int scalesLast = 0;

    public static void setLastPosition(UUID uuid, Vec3d pos) {
        lastPositionServer.put(uuid, pos);
    }

    public static double getMoveDistance(UUID uuid, Vec3d pos) {
        Vec3d lastPos = lastPositionServer.getOrDefault(uuid, pos);
        return lastPos.distanceTo(pos);
    }

    private static void resetEntity(UUID uuid) {
        lastPositionServer.remove(uuid);
        cooldownTicksServer.remove(uuid);
    }

    public static void setCooldown(UUID uuid, int ticks) {
        cooldownTicksServer.put(uuid, ticks);
    }

    public static void setMaxCooldown(UUID uuid, int ticks) {
        cooldownTicksServer.put(uuid, Math.max(ticks, cooldownTicksServer.getOrDefault(uuid, 0)));
    }

    public static boolean hasCooldown(UUID uuid) {
        return cooldownTicksServer.getOrDefault(uuid, 0) > 0;
    }

    public ModifierShiftingScales() {
        super(EnumType.ARMOR, "shifting_scales", 0.0, true);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityConstructEvent(EntityConstructing event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityLivingBase) {
            ((EntityLivingBase) entity).getAttributeMap().registerAttribute(SCALES);
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            for (UUID uuid : cooldownTicksServer.keySet()) {
                int ticks = cooldownTicksServer.get(uuid) - 1;
                cooldownTicksServer.put(uuid, ticks);
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();

        if (!entity.world.isRemote) {
            UUID uuid = entity.getUniqueID();
            int scaleLevel = ItemModUtil.getArmorModifierLevel(entity, EmbersAPI.SHIFTING_SCALES) * 2;
            if (scaleLevel > 0) {
                if (getMoveDistance(uuid, entity.getPositionVector()) * 20 > MOVE_PER_SECOND_THRESHOLD)
                    setMaxCooldown(uuid, COOLDOWN);

                double scales = EmbersAPI.getScales(entity);
                if (!hasCooldown(uuid)) {
                    scales += 1;
                    setCooldown(uuid, COOLDOWN);
                }
                scales = Math.min(Math.min(scales, scaleLevel * 3), entity.getMaxHealth() * 1.5);
                EmbersAPI.setScales(entity, scales);
                setLastPosition(uuid, entity.getPositionVector());
            } else {
                resetEntity(uuid);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onHit(LivingDamageEvent event) {

        EntityLivingBase entity = event.getEntityLiving();
        DamageSource source = event.getSource();
        if (unaffectedDamageTypes.contains(source.getDamageType()))
            return;

        int scaleLevel = ItemModUtil.getArmorModifierLevel(entity, EmbersAPI.SHIFTING_SCALES) * 2;
        if (scaleLevel > 0) {
            if (!entity.world.isRemote)
                setMaxCooldown(entity.getUniqueID(), COOLDOWN * 3);
            ScaleEvent scaleEvent = new ScaleEvent(entity, event.getAmount(), source, ConfigManager.scaleDamageRates.getOrDefault(source.getDamageType(), 1.0), ConfigManager.scaleDamagePasses.getOrDefault(source.getDamageType(), 0.0));
            MinecraftForge.EVENT_BUS.post(scaleEvent);
            double totalDamage = event.getAmount();
            double extraDamage = totalDamage * scaleEvent.getScalePassRate();
            totalDamage -= extraDamage;
            double multiplier = scaleEvent.getScaleDamageRate();
            double damage = totalDamage * multiplier;
            double scales = EmbersAPI.getScales(entity);
            double absorbed = Math.min(scales, damage);
            double prevScales = scales;
            scales -= absorbed;
            damage -= absorbed;
            if ((int) scales < (int) prevScales) {
                entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, SoundManager.SHIFTING_SCALES_BREAK, entity instanceof EntityPlayer ? SoundCategory.PLAYERS : SoundCategory.HOSTILE, 10.0f, 1.0f);
            }
            EmbersAPI.setScales(entity, scales);
            event.setAmount((float) ((damage == 0 ? 0 : damage / multiplier) + extraDamage));
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDrawScreenPre(RenderGameOverlayEvent.Pre event) {
        ScaledResolution resolution = event.getResolution();
        if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.player;
            mc.renderEngine.bindTexture(TEXTURE_HUD);
            int x = getBarX(resolution);
            int y = getBarY(resolution);

            int scales = (int) Math.ceil(EmbersAPI.getScales(player));

            int segs = scales / 3;
            int last = scales % 3;
            if (last > 0)
                segs++;
            int u = 18;
            int v = 0;

            for (int i = 0; i < segs; i++) {
                if (i == segs - 1)
                    u = ((last + 2) % 3) * 9;

                RenderUtil.drawTexturedModalRect(x + 8 * (i % 10), y - 10 * (i / 10), 0, u / 256.0, v / 256.0, (u + 9) / 256.0, (v + 9) / 256.0, 9, 9);
            }

            GlStateManager.enableAlpha();
            GlStateManager.color(1F, 1F, 1F, 1F);
            mc.renderEngine.bindTexture(Gui.ICONS);
        }
    }

    @SideOnly(Side.CLIENT)
    private int getBarY(ScaledResolution resolution) {
        return resolution.getScaledHeight() - 42;
    }

    @SideOnly(Side.CLIENT)
    private int getBarX(ScaledResolution resolution) {
        return resolution.getScaledWidth() / 2 - 11 - 8 * 10;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            Iterator<ShardParticle> iterator = shards.iterator();
            while (iterator.hasNext()) {
                ShardParticle particle = iterator.next();
                particle.update();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
        ScaledResolution resolution = event.getResolution();
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        mc.renderEngine.bindTexture(TEXTURE_HUD);
        Iterator<ShardParticle> iterator = shards.iterator();
        GlStateManager.enableAlpha();
        while (iterator.hasNext()) {
            ShardParticle particle = iterator.next();
            if (particle.getY() > resolution.getScaledHeight())
                iterator.remove();
            int u = particle.getFrame() % 8 > 4 ? 5 : 0;
            int v = 9;
            RenderUtil.drawTexturedModalRect((int) particle.getX() - 2, (int) particle.getY() - 2, 0, u / 256.0, v / 256.0, (u + 5) / 256.0, (v + 5) / 256.0, 5, 5);
        }
        int scales = (int) Math.ceil(EmbersAPI.getScales(player));
        Random random = new Random();
        if (scales < scalesLast) {
            int x = getBarX(resolution);
            int y = getBarY(resolution);

            int segsLast = scalesLast / 3;
            int lastLast = scalesLast % 3;
            if (lastLast > 0)
                segsLast++;
            int segs = scales / 3;
            int last = scales % 3;
            if (last > 0)
                segs++;

            for (int i = 0; i < Math.max(segs, segsLast); i++) {
                int currentScale = i * 3 + last;
                if (currentScale < scales)
                    continue;
                int xHeart = x + 8 * (i % 10) + 4;
                int yHeart = y - 10 * (i / 10) + 4;
                int pieces = 2;
                if (lastLast == 1 && i == Math.max(segs, segsLast) - 1)
                    pieces = 1;
                for (int e = 0; e < pieces; e++)
                    shards.add(new ShardParticle(xHeart, yHeart, random.nextInt(8), (random.nextDouble() - 0.5) * 10, (random.nextDouble() - 0.5) * 10, 0.5));
            }
        }
        scalesLast = scales;
        mc.renderEngine.bindTexture(Gui.ICONS);
    }
}
