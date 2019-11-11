package teamroots.embers.entity.magmaworm;

import net.minecraft.util.math.Vec3d;
import teamroots.embers.entity.EntityMagmaWorm;
import teamroots.embers.entity.MagmaWormPhase;
import teamroots.embers.util.Bezier;

import java.util.ArrayList;
import java.util.List;

public class Burrow extends MagmaWormPhase {
    private int digCooldown;
    private int undigCooldown;

    public Burrow(PhaseSupplier supplier) {
        super(supplier);
    }

    @Override
    public double getVelocity(EntityMagmaWorm worm) {
        return 1.0;
    }

    @Override
    public void onStart(EntityMagmaWorm worm) {
        int tries = random.nextInt(10) + 10;
        List<Vec3d> points = new ArrayList<>();
        points.add(new Vec3d(worm.posX, worm.posY, worm.posZ));
        for (int i = 0; i < tries; i++) {
            Vec3d lastPos = points.get(points.size() - 1);
            double angle = random.nextDouble() * Math.PI * 2;
            double dx = Math.sin(angle) * 10;
            double dz = Math.cos(angle) * 10;
            double height = MagmaWormPhase.getHeight(worm.world, lastPos.x + dx, lastPos.z + dz);
            if (i % 2 == 0) {
                points.add(new Vec3d(lastPos.x + dx, height + 2, lastPos.z + dz));
            } else {
                points.add(new Vec3d(lastPos.x + dx, height - 5, lastPos.z + dz));
            }
        }

        worm.setCurrentSpline(new Bezier(points), 10, 0.3);
    }

    @Override
    public void onUpdate(EntityMagmaWorm worm) {
        digCooldown--;
        undigCooldown--;
    }

    @Override
    public void onDig(EntityMagmaWorm worm) {
        if(digCooldown > 0)
            return;
        Vec3d location = new Vec3d(worm.posX,worm.posY+2,worm.posZ);
        splashMagma(worm, location, 6);
        digCooldown = 30;
    }

    @Override
    public void onUndig(EntityMagmaWorm worm) {
        if(undigCooldown > 0)
            return;
        Vec3d location = new Vec3d(worm.posX,worm.posY+2,worm.posZ);
        splashMagma(worm, location, 6);
        undigCooldown = 30;
    }
}
