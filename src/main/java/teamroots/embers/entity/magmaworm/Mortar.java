package teamroots.embers.entity.magmaworm;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import teamroots.embers.entity.EntityMagmaProjectile;
import teamroots.embers.entity.EntityMagmaWorm;
import teamroots.embers.entity.MagmaWormPhase;
import teamroots.embers.util.Bezier;

import java.util.ArrayList;
import java.util.List;

public class Mortar extends MagmaWormPhase {
    private static final double RANGE_ATTACK = 36;

    public Mortar(PhaseSupplier supplier) {
        super(supplier);
    }

    @Override
    public double getVelocity(EntityMagmaWorm worm) {
        return 0.5;
    }

    @Override
    public void onStart(EntityMagmaWorm worm) {
        List<Entity> targets = worm.getAttackTargets(new AxisAlignedBB(worm.posX - RANGE_ATTACK, worm.posY - RANGE_ATTACK, worm.posZ - RANGE_ATTACK, worm.posX + RANGE_ATTACK, worm.posY + RANGE_ATTACK, worm.posZ + RANGE_ATTACK));
        if(!targets.isEmpty()) {
            int tries = random.nextInt(2) + 1;
            List<Vec3d> points = new ArrayList<>();
            points.add(new Vec3d(worm.posX, worm.posY, worm.posZ));
            for (int i = 0; i < tries; i++) {
                Entity target = targets.get(random.nextInt(targets.size()));
                Vec3d lastPos = points.get(points.size() - 1);
                double angle = random.nextDouble() * Math.PI * 2;
                double x = target.posX;
                double z = target.posZ;
                double dx = Math.sin(angle) * 5;
                double dz = Math.cos(angle) * 5;
                double height = MagmaWormPhase.getHeight(worm.world, lastPos.x + dx, lastPos.z + dz);
                points.add(new Vec3d(lastPos.x + dx, height + 5 + random.nextDouble() * 15, lastPos.z + dz));
                points.add(new Vec3d(x, height + 5 + random.nextDouble() * 15, z));
            }

            worm.setCurrentSpline(new Bezier(points), 10, 0.3);
        }
    }

    @Override
    public void onUpdate(EntityMagmaWorm worm) {
        if(worm.ticksExisted % 2 == 0)
        for (int i = 0; i < 1; i++) {
            int index = random.nextInt(Math.max(worm.getSegments() - 2, 1));
            Vec3d location = worm.pastPositions.get(index);
            double height = MagmaWormPhase.getHeight(worm.world, location.x, location.z);
            if(location.y - height > 4) {
                Vec3d velocity = Vec3d.fromPitchYaw(-90 + random.nextFloat() * 90 - 45f, random.nextFloat() * 360).scale(0.3f);
                EntityMagmaProjectile projectile = new EntityMagmaProjectile(worm.world, worm);
                projectile.setPosition(location.x, location.y, location.z);
                projectile.makeMortar(velocity);
                worm.world.spawnEntity(projectile);
            }
        }
    }

    @Override
    public void onUndig(EntityMagmaWorm worm) {
        Vec3d location = new Vec3d(worm.posX,worm.posY+2,worm.posZ);
        splashMagma(worm, location, 12);
    }
}
