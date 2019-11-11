package teamroots.embers.entity.magmaworm;

import com.google.common.collect.Lists;
import net.minecraft.util.math.Vec3d;
import teamroots.embers.entity.EntityMagmaWorm;
import teamroots.embers.entity.MagmaWormPhase;
import teamroots.embers.util.Bezier;

public abstract class Jump extends MagmaWormPhase {

    public Jump(PhaseSupplier supplier) {
        super(supplier);
    }

    protected abstract Vec3d getTarget(EntityMagmaWorm worm);

    @Override
    public void onStart(EntityMagmaWorm worm) {
        Vec3d targetPoint = getTarget(worm);
        Vec3d currentPoint = new Vec3d(worm.posX, worm.posY, worm.posZ);
        Vec3d topPoint = new Vec3d(targetPoint.x, worm.posY, targetPoint.z);
        worm.setCurrentSpline(new Bezier(Lists.newArrayList(currentPoint, topPoint, targetPoint)), 10, 0.3);
    }
}
