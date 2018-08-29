package teamroots.embers.api.event;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.Event;
import teamroots.embers.api.projectile.IProjectileEffect;
import teamroots.embers.api.projectile.IProjectilePreset;

import java.util.List;

public class EmberProjectileEvent extends Event {
    private EntityLivingBase shooter;
    private ItemStack stack;
    private List<IProjectilePreset> projectiles;
    private double charge;

    public EmberProjectileEvent(EntityLivingBase shooter, ItemStack stack, double charge, List<IProjectilePreset> projectiles) {
        this.shooter = shooter;
        this.stack = stack;
        this.projectiles = projectiles;
        this.charge = charge;
    }

    public EmberProjectileEvent(EntityLivingBase shooter, ItemStack stack, double charge, IProjectilePreset... projectiles) {
        this(shooter, stack, charge, Lists.newArrayList(projectiles));
    }

    public ItemStack getStack() {
        return stack;
    }

    public EntityLivingBase getShooter() {
        return shooter;
    }

    public List<IProjectilePreset> getProjectiles() {
        return projectiles;
    }

    public double getCharge() {
        return charge;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
