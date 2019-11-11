package teamroots.embers.entity.magmaworm;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import teamroots.embers.entity.EntityMagmaProjectile;
import teamroots.embers.entity.EntityMagmaWorm;
import teamroots.embers.entity.MagmaWormPhase;

import java.util.List;

public class DownSeism extends Down {
    private static final double RANGE_ATTACK = 36;

    private boolean impacted;
    private double velocity = 0.5;

    public DownSeism(PhaseSupplier supplier) {
        super(supplier);
    }

    @Override
    public double getVelocity(EntityMagmaWorm worm) {
        if(impacted)
            return 0.5;
        return velocity;
    }

    @Override
    public void onUpdate(EntityMagmaWorm worm) {
        velocity = Math.min(2,velocity + 0.05);
    }

    @Override
    protected Vec3d getTarget(EntityMagmaWorm worm) {
        List<Entity> targets = worm.getAttackTargets(new AxisAlignedBB(worm.posX - RANGE_ATTACK, worm.posY - RANGE_ATTACK, worm.posZ - RANGE_ATTACK, worm.posX + RANGE_ATTACK, worm.posY + RANGE_ATTACK, worm.posZ + RANGE_ATTACK));
        if(!targets.isEmpty()) {
            Entity target = targets.get(random.nextInt(targets.size()));
            double bottom = MagmaWormPhase.getHeight(target.world, target.posX, target.posZ) - 10;
            return new Vec3d(target.posX,bottom,target.posZ);
        }
        return super.getTarget(worm);
    }

    @Override
    public void onDig(EntityMagmaWorm worm) {
        super.onDig(worm);
        if(!impacted) {
            int delay = 30;
            for(int i = 0; i < 10; i++) {
                double angle = random.nextDouble() * Math.PI * 2;
                double distance = random.nextDouble() * 9 + 1;
                double dx = Math.sin(angle) * distance;
                double dz = Math.cos(angle) * distance;
                double height = MagmaWormPhase.getHeight(worm.world, worm.posX + dx, worm.posZ + dz);
                EntityMagmaProjectile projectile = new EntityMagmaProjectile(worm.world, worm);
                projectile.setPosition(worm.posX + dx, height + 0.5, worm.posZ + dz);
                projectile.makePillar(12,delay+i*5);
                worm.world.spawnEntity(projectile);
            }
            impacted = true;
        }
    }
}
