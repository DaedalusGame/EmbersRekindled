package teamroots.embers.entity.magmaworm;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import teamroots.embers.entity.EntityMagmaWorm;
import teamroots.embers.entity.MagmaWormPhase;

import java.util.List;

public class UpAggressive extends Up {
    private static final double RANGE_ATTACK = 36;

    public UpAggressive(PhaseSupplier supplier) {
        super(supplier);
    }

    @Override
    protected Vec3d getTarget(EntityMagmaWorm worm) {
        List<Entity> targets = worm.getAttackTargets(new AxisAlignedBB(worm.posX - RANGE_ATTACK, worm.posY - RANGE_ATTACK, worm.posZ - RANGE_ATTACK, worm.posX + RANGE_ATTACK, worm.posY + RANGE_ATTACK, worm.posZ + RANGE_ATTACK));
        if(!targets.isEmpty()) {
            Entity target = targets.get(random.nextInt(targets.size()));
            double bottom = MagmaWormPhase.getHeight(target.world, target.posX, target.posZ) + 20;
            return new Vec3d(target.posX,bottom,target.posZ);
        }
        return super.getTarget(worm);
    }
}
