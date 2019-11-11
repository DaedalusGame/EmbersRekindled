package teamroots.embers.entity.magmaworm;

import net.minecraft.util.math.Vec3d;
import teamroots.embers.entity.EntityMagmaWorm;
import teamroots.embers.entity.MagmaWormPhase;

public class DownStuck extends Jump {
    private boolean stuck;
    private double stuckTimer;
    private double velocityModifier = 0.1;

    public DownStuck(PhaseSupplier supplier) {
        super(supplier);
    }

    @Override
    protected Vec3d getTarget(EntityMagmaWorm worm) {
        double angle = random.nextDouble() * Math.PI * 2;
        double dx = Math.sin(angle) * 20;
        double dz = Math.cos(angle) * 20;
        double bottom = MagmaWormPhase.getHeight(worm.world, worm.posX + dx, worm.posZ + dz) - 10;
        return new Vec3d(worm.posX + dx, bottom, worm.posZ + dz);
    }

    @Override
    public double getVelocity(EntityMagmaWorm worm) {
        return velocityModifier;
    }

    @Override
    public void onUpdate(EntityMagmaWorm worm) {
        if(stuckTimer < 0)
            velocityModifier = Math.min(1.0,velocityModifier+0.01);
        if(!stuck)
            velocityModifier = Math.min(1.5,velocityModifier+0.05);
        stuckTimer--;
    }

    @Override
    public void onDig(EntityMagmaWorm worm) {
        if(!stuck) {
            stuck = true;
            stuckTimer = 480;
            velocityModifier = 0;
        }
    }
}
