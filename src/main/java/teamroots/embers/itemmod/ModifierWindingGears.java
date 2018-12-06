package teamroots.embers.itemmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.SoundManager;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.api.itemmod.ModifierBase;
import teamroots.embers.util.RenderUtil;

import java.util.Map;
import java.util.WeakHashMap;

public class ModifierWindingGears extends ModifierBase {
    public static final ResourceLocation TEXTURE_HUD = new ResourceLocation("embers:textures/gui/icons.png");
    public static final int BAR_U = 0;
    public static final int BAR_V = 32;
    public static final int BAR_WIDTH = 180;
    public static final int BAR_HEIGHT = 8;

    public static final String TAG_CHARGE = "windingGearsCharge";
    public static final String TAG_CHARGE_TIME = "windingGearsLastTime";
    public static final double MAX_CHARGE = 500.0;
    public static final int CHARGE_DECAY_DELAY = 20;
    public static final double CHARGE_DECAY = 0.25;

    static int ticks;
    static double angle, angleLast;
    static int spool, spoolLast;
    static ThreadLocal<Map<Entity,Double>> bounceLocal = ThreadLocal.withInitial(WeakHashMap::new);

    public ModifierWindingGears() {
        super(EnumType.ALL, "winding_gears", 0.0, true);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean canApplyTo(ItemStack stack) {
        return super.canApplyTo(stack) && (canApplyToType(stack,EnumType.TOOL) || canApplyToType(stack,EnumType.BOOTS)); //This is dumb and i feel dumb for writing it
    }

    @SideOnly(Side.CLIENT)
    private int getBarY(ScaledResolution resolution) {
        return resolution.getScaledHeight() - 31;
    }

    @SideOnly(Side.CLIENT)
    private int getBarX(ScaledResolution resolution) {
        return resolution.getScaledWidth() / 2 - 11 - 81;
    }

    public static ItemStack getHeldClockworkTool(EntityLivingBase entity) {
        ItemStack mainStack = entity.getHeldItemMainhand();
        ItemStack offStack = entity.getHeldItemOffhand();
        boolean isClockworkMain = isClockworkTool(mainStack);
        boolean isClockworkOff = isClockworkTool(offStack);
        if (isClockworkMain == isClockworkOff)
            return ItemStack.EMPTY;
        else if (isClockworkMain)
            return mainStack;
        else
            return offStack;
    }

    public static boolean isClockworkTool(ItemStack stack) {
        return ItemModUtil.hasHeat(stack) && ItemModUtil.hasModifier(stack, EmbersAPI.WINDING_GEARS);
    }

    public static double getChargeDecay(World world, ItemStack stack) {
        return CHARGE_DECAY;
    }

    public static double getCharge(World world, ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if(tagCompound != null) {
            long dTime = getTimeSinceLastCharge(world, stack);
            return Math.max(0,tagCompound.getDouble(TAG_CHARGE) - Math.max(0, dTime - CHARGE_DECAY_DELAY) * getChargeDecay(world,stack));
        }
        else
            return 0;
    }

    private static long getTimeSinceLastCharge(World world, ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if(tagCompound != null) {
            long lastTime = tagCompound.getLong(TAG_CHARGE_TIME);
            long currentTime = world.getTotalWorldTime();
            if (lastTime > currentTime)
                return 0;
            else
                return currentTime - lastTime;
        }
        return Long.MAX_VALUE;
    }

    public static double getMaxCharge(World world, ItemStack stack) {
        int level = getClockworkLevel(stack);
        return Math.min(200.0 * level, MAX_CHARGE);
    }

    private static int getClockworkLevel(ItemStack stack) {
        int level = ItemModUtil.getModifierLevel(stack, EmbersAPI.WINDING_GEARS);
        return level;
    }

    public static void setCharge(World world, ItemStack stack, double charge) {
        if(world.isRemote)
            return;
        NBTTagCompound tagCompound = stack.getTagCompound();
        if(tagCompound != null) {
            tagCompound.setDouble(TAG_CHARGE, charge);
            tagCompound.setLong(TAG_CHARGE_TIME, world.getTotalWorldTime());
        }
    }

    public static void depleteCharge(World world, ItemStack stack, double charge) {
        setCharge(world,stack,Math.max(0,getCharge(world,stack)-charge));
    }

    public static void addCharge(World world, ItemStack stack, double charge) {
        if(world.isRemote)
            return;
        setCharge(world,stack,Math.min(getMaxCharge(world,stack),getCharge(world,stack)+charge));
        NBTTagCompound tagCompound = stack.getTagCompound();
        if(tagCompound != null)
            tagCompound.setLong(TAG_CHARGE_TIME, world.getTotalWorldTime());
    }

    public static float getSpeedBonus(World world,ItemStack stack) {
        double charge = getCharge(world,stack);
        return (float) MathHelper.clampedLerp(-0.2, 20.0, (charge - 50.0) / 300.0);
    }

    public static float getDamageBonus(World world,ItemStack stack) {
        double charge = getCharge(world,stack);
        return (float) MathHelper.clampedLerp(1.0, 6.0, (charge - 50.0) / 300.0);
    }

    public static double getRotationSpeed(World world,ItemStack stack) {
        long dTime = getTimeSinceLastCharge(world, stack);
        double charge = getCharge(world,stack);
        double standardSpeed = MathHelper.clampedLerp(0.0,400.0,charge / 500.0);
        if(dTime > CHARGE_DECAY_DELAY && charge > 0)
            return MathHelper.clampedLerp(0,-10,(dTime - CHARGE_DECAY_DELAY) / 10.0);
        else
            return MathHelper.clampedLerp(standardSpeed,0,(dTime - 10) / 10.0);
    }

    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        ItemStack stack = getHeldClockworkTool(entity);
        if(!stack.isEmpty() && isClockworkTool(entity.getItemStackFromSlot(EntityEquipmentSlot.FEET))) {
            double charge = getCharge(entity.world, stack);
            double cost = Math.max(16,charge * (80.0/500.0));
            if(charge > 0)
            {
                if(entity.isSprinting() && charge > Math.max(40,cost*1.5)) {
                    entity.motionX *= 2;
                    entity.motionZ *= 2;
                    cost = Math.max(40,cost*1.5);
                }
                entity.motionY += MathHelper.clampedLerp(0.0,7.0/20.0,charge / 500.0);
                if(charge >= cost)
                    entity.playSound(SoundManager.WINDING_GEARS_SPRING,1.0f,1.5f);
            }

            if(!entity.world.isRemote)
                depleteCharge(entity.world, stack, cost);
        }
    }

    @SubscribeEvent
    public void onTick(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        Map<Entity,Double> bounce = bounceLocal.get();
        if(bounce.containsKey(entity)) {
            entity.motionY += bounce.get(entity);
            bounce.remove(entity);
        }
    }

    @SubscribeEvent
    public void onFall(LivingFallEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        ItemStack stack = getHeldClockworkTool(entity);
        if(!stack.isEmpty() && isClockworkTool(entity.getItemStackFromSlot(EntityEquipmentSlot.FEET))) {
            double spoolCost = Math.max(0,event.getDistance()-1) * 5;
            if(getCharge(entity.world, stack) >= spoolCost)
            {
                event.setDamageMultiplier(0);
                if(entity.motionY < -0.5) {
                    if(!entity.world.isRemote)
                        depleteCharge(entity.world, stack,spoolCost);
                    bounceLocal.get().put(entity,-entity.motionY);
                }
            }
        }
    }

    @SubscribeEvent
    public void onAttack(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        if (source.getTrueSource() instanceof EntityLivingBase) {
            EntityLivingBase player = (EntityLivingBase) source.getTrueSource();
            ItemStack mainStack = player.getHeldItemMainhand();
            float damage = event.getAmount();
            if (isClockworkTool(mainStack)) {
                double charge = getCharge(player.world, mainStack);
                double cost = 5;
                if (charge >= getMaxCharge(player.world, mainStack)) {
                    //event.setAmount(damage + getDamageBonus(mainStack));
                    cost = charge;
                }
                if(!player.world.isRemote)
                    depleteCharge(player.world, mainStack,cost);
            }
        }
    }

    @SubscribeEvent
    public void getBreakSpeed(PlayerEvent.BreakSpeed event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack mainStack = player.getHeldItemMainhand();
        float speed = event.getNewSpeed();
        if (isClockworkTool(mainStack)) {
            double charge = getCharge(player.world, mainStack);
            if (charge > 0) {
                event.setNewSpeed(Math.max(Math.min(speed,0.1f),speed + getSpeedBonus(player.world, mainStack)));
            }
        }
    }

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if(!event.isCanceled() && player != null) {
            ItemStack mainStack = player.getHeldItemMainhand();
            if (isClockworkTool(mainStack)) {
                double charge = getCharge(player.world, mainStack);
                if (charge > 0) {
                    if(!player.world.isRemote)
                        depleteCharge(player.world, mainStack,charge);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = event.getItemStack();

        if (isClockworkTool(stack)) {
            int level = getClockworkLevel(stack);
            double maxCharge = getMaxCharge(player.world,stack);

            if (level > 0) {
                if (stack.getTagCompound() == null)
                    stack.setTagCompound(new NBTTagCompound()); //I promise you this has a 0% chance of ever running
                double charge = getCharge(player.world, stack);
                double addAmount = Math.max((0.025 + 0.01 * level) * (maxCharge - charge), 5);
                addCharge(player.world, stack, addAmount);
                player.swingArm(event.getHand());
                event.setCancellationResult(EnumActionResult.PASS);
                event.setCanceled(true);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientUpdate(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            ticks++;
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayerSP player = mc.player;

            if (player != null) {
                ItemStack stack = getHeldClockworkTool(player);
                if (!stack.isEmpty()) {
                    spoolLast = spool;
                    spool = (int) (BAR_WIDTH * 4 * getCharge(player.world, stack) / MAX_CHARGE);
                    angleLast = angle;
                    angle += getRotationSpeed(player.world, stack);
                    //Auto-Attack
                    RayTraceResult objectMouseOver = mc.objectMouseOver;
                    if(mc.gameSettings.keyBindAttack.isKeyDown() && canAutoAttack(player, stack, objectMouseOver))
                        mc.playerController.attackEntity(player, objectMouseOver.entityHit);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private boolean canAutoAttack(EntityPlayerSP player, ItemStack stack, RayTraceResult objectMouseOver) {
        return player.getCooledAttackStrength(0) >= 1.0f && getCharge(player.world, stack) > 0 && objectMouseOver != null && objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY && /*!isInvulnerable(objectMouseOver.entityHit) &&*/ !player.isRowingBoat();
    }

    @SideOnly(Side.CLIENT)
    private boolean isInvulnerable(Entity entity)
    {
        return entity.getIsInvulnerable() || (entity instanceof EntityLivingBase && entity.hurtResistantTime > 0);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDrawScreenPre(RenderGameOverlayEvent.Pre event) {
        float partialTicks = event.getPartialTicks();
        ScaledResolution resolution = event.getResolution();
        int fill = (int) (spoolLast * (1 - partialTicks) + spool * partialTicks);
        double currentAngle = angleLast * (1 - partialTicks) + angle * partialTicks;
        int gearFrame = (int) (currentAngle * 4f / 360f);
        int uGear = (gearFrame % 4) * 10;
        int vGear = 16;
        fill += 16;
        if (event.getType() == RenderGameOverlayEvent.ElementType.JUMPBAR || event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.player;
            if (player == null)
                return;
            ItemStack stack = getHeldClockworkTool(player);
            if (!stack.isEmpty()) {
                mc.renderEngine.bindTexture(TEXTURE_HUD);
                int x = getBarX(resolution);
                int y = getBarY(resolution);

                int segs = fill / 32;
                int last = fill % 32;
                int u = BAR_U;
                int v = BAR_V;

                int evenWidth = (segs) * 8;
                int oddWidth = (segs) * 8 - 4;
                int evenFillBack = MathHelper.clamp(last - 16, 0, 8);
                int evenFillFront = MathHelper.clamp(last - 24, 0, 8);
                int oddFillBack = MathHelper.clamp(last - 0, 0, 8);
                int oddFillFront = MathHelper.clamp(last - 8, 0, 8);

                RenderUtil.drawTexturedModalRect(x - 9, y - 1, -80, uGear / 256.0, vGear / 256.0, (uGear + 10) / 256.0, (vGear + 10) / 256.0, 10, 10);

                RenderUtil.drawTexturedModalRect(x, y, -80, u / 256.0, v / 256.0, (u + evenWidth) / 256.0, (v + BAR_HEIGHT) / 256.0, evenWidth, BAR_HEIGHT);
                RenderUtil.drawTexturedModalRect(x + evenWidth, y, -80, (u + evenWidth) / 256.0, v / 256.0, (u + evenWidth + 8) / 256.0, (v + evenFillFront) / 256.0, 8, evenFillFront);
                v += 8;
                RenderUtil.drawTexturedModalRect(x, y, -90, u / 256.0, v / 256.0, (u + evenWidth) / 256.0, (v + BAR_HEIGHT) / 256.0, evenWidth, BAR_HEIGHT);
                RenderUtil.drawTexturedModalRect(x + evenWidth, y + 8 - evenFillBack, -90, (u + evenWidth) / 256.0, (v + 8 - evenFillBack) / 256.0, (u + evenWidth + 8) / 256.0, (v + 8) / 256.0, 8, evenFillBack);
                v += 8;
                RenderUtil.drawTexturedModalRect(x, y, -80, u / 256.0, v / 256.0, (u + oddWidth) / 256.0, (v + BAR_HEIGHT) / 256.0, oddWidth, BAR_HEIGHT);
                RenderUtil.drawTexturedModalRect(x + oddWidth, y, -80, (u + oddWidth) / 256.0, v / 256.0, (u + oddWidth + 8) / 256.0, (v + oddFillFront) / 256.0, 8, oddFillFront);
                v += 8;
                RenderUtil.drawTexturedModalRect(x, y, -90, u / 256.0, v / 256.0, (u + oddWidth) / 256.0, (v + BAR_HEIGHT) / 256.0, oddWidth, BAR_HEIGHT);
                RenderUtil.drawTexturedModalRect(x + oddWidth, y + 8 - oddFillBack, -90, (u + oddWidth) / 256.0, (v + 8 - oddFillBack) / 256.0, (u + oddWidth + 8) / 256.0, (v + 8) / 256.0, 8, oddFillBack);


                GlStateManager.enableAlpha();
                GlStateManager.color(1F, 1F, 1F, 1F);
                mc.renderEngine.bindTexture(Gui.ICONS);
            }
        }
    }
}
