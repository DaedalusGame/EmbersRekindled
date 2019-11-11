package teamroots.embers.entity.magmaworm;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import teamroots.embers.entity.EntityMagmaWorm;
import teamroots.embers.entity.MagmaWormPhase;
import teamroots.embers.util.Bezier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Lunge extends MagmaWormPhase {
    private static final double RANGE_ATTACK = 36;

    private int digCooldown;
    private int undigCooldown;
    private Entity entity;
    private int repeat;
    private double velocity = 0.25;

    public Lunge(PhaseSupplier supplier, int repeat) {
        super(supplier);
        this.repeat = repeat;
    }

    public Lunge(PhaseSupplier supplier, Entity entity, int repeat) {
        super(supplier);
        this.entity = entity;
        this.repeat = repeat;
    }

    @Override
    public double getVelocity(EntityMagmaWorm worm) {
        if(worm.isDigging())
            return 0.5;
        return velocity;
    }

    @Override
    public MagmaWormPhase pickFollowup(Random random) {
        if(repeat > 0)
            return new Lunge(supplier,entity,repeat-1);
        return super.pickFollowup(random);
    }

    @Override
    public void onStart(EntityMagmaWorm worm) {
        List<Entity> targets = worm.getAttackTargets(new AxisAlignedBB(worm.posX - RANGE_ATTACK, worm.posY - RANGE_ATTACK, worm.posZ - RANGE_ATTACK, worm.posX + RANGE_ATTACK, worm.posY + RANGE_ATTACK, worm.posZ + RANGE_ATTACK));
        if(!targets.isEmpty()) {
            Entity target = targets.get(random.nextInt(targets.size()));
            List<Vec3d> points = new ArrayList<>();
            points.add(new Vec3d(worm.posX, worm.posY, worm.posZ));
            points.add(new Vec3d(worm.posX, MagmaWormPhase.getHeight(worm.world,worm.posX,worm.posZ)+3, worm.posZ));
            Vec3d targetPos = new Vec3d(target.posX, target.posY + target.height/2, target.posZ);
            points.add(targetPos);
            double angle = random.nextDouble() * Math.PI * 2;
            double dx = Math.sin(angle) * 10;
            double dz = Math.cos(angle) * 10;
            double height = MagmaWormPhase.getHeight(worm.world, targetPos.x + dx, targetPos.z + dz);
            points.add(new Vec3d(targetPos.x + dx, height - 5, targetPos.z + dz));

            worm.setCurrentSpline(new Bezier(points), 10, 0.3);
        }
    }

    @Override
    public void onUpdate(EntityMagmaWorm worm) {
        digCooldown--;
        undigCooldown--;
        velocity = Math.min(2,velocity + 0.01);
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
