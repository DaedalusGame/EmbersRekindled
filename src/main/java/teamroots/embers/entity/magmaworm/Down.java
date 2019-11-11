package teamroots.embers.entity.magmaworm;

import net.minecraft.util.math.Vec3d;
import teamroots.embers.entity.EntityMagmaWorm;
import teamroots.embers.entity.MagmaWormPhase;

public class Down extends Jump {

    public Down(PhaseSupplier supplier) {
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
    public void onDig(EntityMagmaWorm worm) {
        Vec3d location = new Vec3d(worm.posX,worm.posY+2,worm.posZ);
        splashMagma(worm, location, 12);
    }
}
