package teamroots.embers.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.Embers;
import teamroots.embers.SoundManager;
import teamroots.embers.api.event.EmberProjectileEvent;
import teamroots.embers.api.projectile.EffectArea;
import teamroots.embers.api.projectile.EffectDamage;
import teamroots.embers.api.projectile.IProjectilePreset;
import teamroots.embers.api.projectile.ProjectileFireball;
import teamroots.embers.damage.DamageEmber;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.util.EmberInventoryUtil;

public class ItemCinderStaff extends ItemBase {

    public static final double EMBER_COST = 25.0;
    public static final int COOLDOWN = 10;

    public static boolean soundPlaying = false; //Clientside anyway so whatever

    public ItemCinderStaff() {
        super("staff_ember", true);
        this.setMaxStackSize(1);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft) {
        if (!world.isRemote) {
            double charge = ((Math.min(60, 72000 - timeLeft)) / 60.0) * 17.0;
            float spawnDistance = 2.0f;//Math.max(1.0f, (float)charge/5.0f);
            double posX = entity.posX + entity.getLookVec().x * spawnDistance;
            double posY = entity.posY + entity.getEyeHeight() + entity.getLookVec().y * spawnDistance;
            double posZ = entity.posZ + entity.getLookVec().z * spawnDistance;
            double value = Math.max(charge, 0.5);
            float aoeSize = (float) value * 0.125f;
            int lifetime = charge >= 1.0 ? 160 : 5;
            EffectArea effect = new EffectArea(new EffectDamage((float) value, DamageEmber.EMBER_DAMAGE_SOURCE_FACTORY, 1, 1.0), aoeSize, false);
            ProjectileFireball fireball = new ProjectileFireball(entity, new Vec3d(posX, posY, posZ), new Vec3d(entity.getLookVec().x * 0.85, entity.getLookVec().y * 0.85, entity.getLookVec().z * 0.85), value, lifetime, effect);
            EmberProjectileEvent event = new EmberProjectileEvent(entity, stack, charge, fireball);
            MinecraftForge.EVENT_BUS.post(event);
            if (!event.isCanceled()) {
                for (IProjectilePreset projectile : event.getProjectiles()) {
                    projectile.shoot(world);
                }
            }
            SoundEvent sound;
            if (charge >= 10.0)
                sound = SoundManager.FIREBALL_BIG;
            else if (charge >= 1.0)
                sound = SoundManager.FIREBALL;
            else
                sound = SoundManager.CINDER_STAFF_FAIL;
            world.playSound(null, posX, posY, posZ, sound, SoundCategory.PLAYERS, 1.0f, 1.0f);
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

        if (player.world.isRemote) {
            if (charge >= 15.0) {
                if (!soundPlaying) {
                    Embers.proxy.playItemSound(player, this, SoundManager.CINDER_STAFF_LOOP, SoundCategory.PLAYERS, true, 1.0f, 1.0f);
                    soundPlaying = true;
                }
            } else {
                soundPlaying = false;
            }
        }

        for (int i = 0; i < 4; i++) {
            float spawnDistance = 2.0f;//Math.max(1.0f, (float)charge/5.0f);
            ParticleUtil.spawnParticleGlow(player.getEntityWorld(), (float) player.posX + spawnDistance * (float) player.getLookVec().x + (itemRand.nextFloat() * 0.1f - 0.05f), (float) player.posY + player.getEyeHeight() + spawnDistance * (float) player.getLookVec().y + (itemRand.nextFloat() * 0.1f - 0.05f), (float) player.posZ + spawnDistance * (float) player.getLookVec().z + (itemRand.nextFloat() * 0.1f - 0.05f), 0, 0, 0, 255, 64, 16, (float) charge / 1.75f, 24);
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
