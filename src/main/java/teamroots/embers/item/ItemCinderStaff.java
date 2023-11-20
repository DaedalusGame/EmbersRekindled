package teamroots.embers.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.Embers;
import teamroots.embers.SoundManager;
import teamroots.embers.api.event.EmberProjectileEvent;
import teamroots.embers.api.event.ItemVisualEvent;
import teamroots.embers.api.item.IProjectileWeapon;
import teamroots.embers.api.projectile.EffectArea;
import teamroots.embers.api.projectile.EffectDamage;
import teamroots.embers.api.projectile.IProjectilePreset;
import teamroots.embers.api.projectile.ProjectileFireball;
import teamroots.embers.config.ConfigTool;
import teamroots.embers.damage.DamageEmber;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.util.EmberInventoryUtil;
import teamroots.embers.util.Misc;

import java.awt.*;

public class ItemCinderStaff extends ItemBase implements IProjectileWeapon {
    public static double EMBER_COST = ConfigTool.CINDER_STAFF_CATEGORY.cost;
    public static int COOLDOWN = ConfigTool.CINDER_STAFF_CATEGORY.cooldown;
    public static double MAX_CHARGE = ConfigTool.CINDER_STAFF_CATEGORY.charge;
    public static float DAMAGE = ConfigTool.CINDER_STAFF_CATEGORY.damage;
    public static float SIZE = ConfigTool.CINDER_STAFF_CATEGORY.size;
    public static float AOE_SIZE = ConfigTool.CINDER_STAFF_CATEGORY.aoe;
    public static int LIFETIME = ConfigTool.CINDER_STAFF_CATEGORY.lifetime;

    public static boolean soundPlaying = false; //Clientside anyway so whatever

    public ItemCinderStaff() {
        super("staff_ember", true);
        this.setMaxStackSize(1);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft) {
        if (!world.isRemote) {
            double charge = (Math.min(MAX_CHARGE, getMaxItemUseDuration(stack) - timeLeft)) / MAX_CHARGE;
            float spawnDistance = 2.0f;//Math.max(1.0f, (float)charge/5.0f);
            Vec3d eyesPos = entity.getPositionEyes(1.0f);
            RayTraceResult traceResult = this.rayTrace(world, (EntityPlayer) entity, false);
            if (traceResult != null && traceResult.typeOfHit == RayTraceResult.Type.BLOCK)
                spawnDistance = (float) Math.min(spawnDistance, traceResult.hitVec.distanceTo(eyesPos));
            Vec3d launchPos = eyesPos.add(entity.getLookVec().scale(spawnDistance));
            float damage = (float) Math.max(charge * DAMAGE, 0.5f);
            float size = (float) Math.max(charge * SIZE, 0.5f);
            float aoeSize = (float) charge * AOE_SIZE;
            int lifetime = charge * DAMAGE >= 1.0 ? LIFETIME : 5;
            EffectArea effect = new EffectArea(new EffectDamage(damage, DamageEmber.EMBER_DAMAGE_SOURCE_FACTORY, 1, 1.0), aoeSize, false);
            ProjectileFireball fireball = new ProjectileFireball(entity, launchPos, entity.getLookVec().scale(0.85), size, lifetime, effect);
            EmberProjectileEvent event = new EmberProjectileEvent(entity, stack, charge, fireball);
            MinecraftForge.EVENT_BUS.post(event);
            if (!event.isCanceled()) {
                for (IProjectilePreset projectile : event.getProjectiles()) {
                    projectile.shoot(world);
                }
            }
            SoundEvent sound;
            if (charge * DAMAGE >= 10.0)
                sound = SoundManager.FIREBALL_BIG;
            else if (charge * DAMAGE >= 1.0)
                sound = SoundManager.FIREBALL;
            else
                sound = SoundManager.CINDER_STAFF_FAIL;
            world.playSound(null, launchPos.x, launchPos.y, launchPos.z, sound, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }
        entity.swingArm(entity.getActiveHand());
        stack.getTagCompound().setInteger("cooldown", COOLDOWN);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || newStack.getItem() != oldStack.getItem();
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setInteger("cooldown", 0);
        } else {
            if (stack.getTagCompound().getInteger("cooldown") > 0) {
                stack.getTagCompound().setInteger("cooldown", stack.getTagCompound().getInteger("cooldown") - 1);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        if (stack.getTagCompound().getInteger("cooldown") > 0)
            player.resetActiveHand();
        double charge = ((Math.min(60, 72000 - count)) / 60.0) * 15.0;
        boolean fullCharge = charge >= 15.0;
        ItemVisualEvent event = new ItemVisualEvent(player, Misc.handToSlot(player.getActiveHand()),stack,new Color(255,64,16),fullCharge ? SoundManager.CINDER_STAFF_LOOP : null, 1.0f, 1.0f, "charge");

        MinecraftForge.EVENT_BUS.post(event);

        if (player.world.isRemote) {
            if (event.hasSound()) {
                if (!soundPlaying) {
                    Embers.proxy.playItemSound(player, this, event.getSound(), SoundCategory.PLAYERS, true, event.getVolume(), event.getPitch());
                    soundPlaying = true;
                }
            } else {
                soundPlaying = false;
            }
        }

        if(event.hasParticles()) {
            Color color = event.getColor();
            float spawnDistance = 2.0f;//Math.max(1.0f, (float)charge/5.0f);
            Vec3d eyesPos = player.getPositionEyes(1.0f);
            RayTraceResult traceResult = this.rayTrace(player.world, (EntityPlayer) player, false);
            if (traceResult != null && traceResult.typeOfHit == RayTraceResult.Type.BLOCK)
                spawnDistance = (float) Math.min(spawnDistance, traceResult.hitVec.distanceTo(eyesPos));
            Vec3d launchPos = eyesPos.add(player.getLookVec().scale(spawnDistance));
            for (int i = 0; i < 4; i++)
                ParticleUtil.spawnParticleGlow(player.getEntityWorld(), (float) launchPos.x + (itemRand.nextFloat() * 0.1f - 0.05f), (float) launchPos.y + (itemRand.nextFloat() * 0.1f - 0.05f), (float) launchPos.z + (itemRand.nextFloat() * 0.1f - 0.05f), 0, 0, 0, color.getRed(), color.getGreen(), color.getBlue(), (float) charge / 1.75f, 24);
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (EmberInventoryUtil.getEmberTotal(player) >= EMBER_COST && stack.getTagCompound().getInteger("cooldown") <= 0 || player.capabilities.isCreativeMode) {
            EmberInventoryUtil.removeEmber(player, EMBER_COST);
            player.setActiveHand(hand);
            if (world.isRemote) {
                Embers.proxy.playItemSound(player, this, SoundManager.CINDER_STAFF_CHARGE, SoundCategory.PLAYERS, false, 1.0f, 1.0f);
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return new ActionResult<>(EnumActionResult.FAIL, stack);
    }
}
