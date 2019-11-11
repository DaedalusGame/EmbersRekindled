package teamroots.embers.entity.magmaworm;

import net.minecraft.util.math.Vec3d;
import teamroots.embers.entity.EntityMagmaWorm;
import teamroots.embers.entity.MagmaWormPhase;

public class Start extends MagmaWormPhase {

    public Start(PhaseSupplier supplier) {
        super(supplier);
    }

    @Override
    public double getVelocity(EntityMagmaWorm worm) {
        return 1.0;
    }

    @Override
    public void onStart(EntityMagmaWorm worm) {
        Vec3d currentPos = new Vec3d(worm.posX, worm.posY, worm.posZ);
        worm.setCurrentSpline((time) -> {
            double angle = time * 4 * Math.PI * 2;
            double distance = 5;
            return new Vec3d(currentPos.x + Math.sin(angle) * time * distance, currentPos.y + time * 20, currentPos.z + Math.cos(angle) * time * distance);
        }, 30, 0);
    }
}
