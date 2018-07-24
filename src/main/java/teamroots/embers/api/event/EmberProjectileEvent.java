package teamroots.embers.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.Event;
import teamroots.embers.api.misc.IProjectileEffect;

import java.util.List;

public class EmberProjectileEvent extends Event {
    private EntityPlayer shooter;
    private ItemStack stack;
    private Vec3d origin;
    private Vec3d velocity;
    private List<IProjectileEffect> projectiles;

    public EmberProjectileEvent(EntityPlayer shooter, ItemStack stack, Vec3d origin, Vec3d velocity) {
        this.shooter = shooter;
        this.stack = stack;
        this.origin = origin;
        this.velocity = velocity;
    }

    public ItemStack getStack() {
        return stack;
    }

    public EntityPlayer getShooter() {
        return shooter;
    }

    public Vec3d getOrigin() {
        return origin;
    }

    public Vec3d getVelocity() {
        return velocity;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
