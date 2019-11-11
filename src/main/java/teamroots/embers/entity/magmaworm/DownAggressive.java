package teamroots.embers.entity.magmaworm;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import teamroots.embers.entity.EntityMagmaWorm;
import teamroots.embers.entity.MagmaWormPhase;

import java.util.List;

public class DownAggressive extends Down {
    private static final double RANGE_ATTACK = 36;
    private double velocity = 0.5;

    public DownAggressive(PhaseSupplier supplier) {
        super(supplier);
    }

    @Override
    public double getVelocity(EntityMagmaWorm worm) {
        if(worm.isDigging())
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
}
