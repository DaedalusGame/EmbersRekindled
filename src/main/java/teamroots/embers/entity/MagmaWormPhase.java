package teamroots.embers.entity;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import teamroots.embers.entity.magmaworm.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MagmaWormPhase {
    public static abstract class PhaseSupplier {
        public abstract MagmaWormPhase get();

        public PhaseSupplier pickFollowup(Random random) {
            return followups.get(random.nextInt(followups.size()));
        }

        public List<PhaseSupplier> followups = new ArrayList<>();
    }

    public static final PhaseSupplier START = new PhaseSupplier() {
        @Override
        public MagmaWormPhase get() {
            return new Start(this);
        }
    };
    public static final PhaseSupplier DOWN = new PhaseSupplier() {
        @Override
        public MagmaWormPhase get() {
            return new Down(this);
        }
    };
    public static final PhaseSupplier DOWN_AGGRESSIVE = new PhaseSupplier() {
        @Override
        public MagmaWormPhase get() {
            return new DownAggressive(this);
        }
    };
    public static final PhaseSupplier DOWN_SEISM =new PhaseSupplier() {
        @Override
        public MagmaWormPhase get() {
            return new DownSeism(this);
        }
    };
    public static final PhaseSupplier DOWN_STUCK =new PhaseSupplier() {
        @Override
        public MagmaWormPhase get() {
            return new DownStuck(this);
        }
    };
    public static final PhaseSupplier UP =new PhaseSupplier() {
        @Override
        public MagmaWormPhase get() {
            return new Up(this);
        }
    };
    public static final PhaseSupplier UP_AGGRESSIVE =new PhaseSupplier() {
        @Override
        public MagmaWormPhase get() {
            return new UpAggressive(this);
        }
    };
    public static final PhaseSupplier BURROW = new PhaseSupplier() {
        @Override
        public MagmaWormPhase get() {
            return new Burrow(this);
        }
    };
    public static final PhaseSupplier MORTAR = new PhaseSupplier() {
        @Override
        public MagmaWormPhase get() {
            return new Mortar(this);
        }
    };
    public static final PhaseSupplier PILLARS = new PhaseSupplier() {
        @Override
        public MagmaWormPhase get() {
            return new Pillars(this);
        }
    };
    public static final PhaseSupplier HOMING = new PhaseSupplier() {
        @Override
        public MagmaWormPhase get() {
            return new Homing(this);
        }
    };
    public static final PhaseSupplier BURROW_AGGRESSIVE = new PhaseSupplier() {
        @Override
        public MagmaWormPhase get() {
            return new Lunge(this, 1);
        }
    };
    public static final PhaseSupplier BURROW_CHASE = new PhaseSupplier() {
        @Override
        public MagmaWormPhase get() {
            return new Lunge(this, 3);
        }
    };

    static {
        START.followups.add(DOWN);

        UP.followups.add(DOWN);
        UP.followups.add(DOWN_STUCK);
        UP.followups.add(DOWN_AGGRESSIVE);
        UP.followups.add(DOWN_SEISM);
        UP.followups.add(MORTAR);
        UP.followups.add(HOMING);
        UP.followups.add(PILLARS);
        UP_AGGRESSIVE.followups.add(DOWN);
        UP_AGGRESSIVE.followups.add(DOWN_AGGRESSIVE);
        UP_AGGRESSIVE.followups.add(HOMING);
        DOWN.followups.add(UP);
        DOWN.followups.add(UP_AGGRESSIVE);
        DOWN.followups.add(BURROW);
        DOWN.followups.add(BURROW_AGGRESSIVE);
        DOWN.followups.add(BURROW_CHASE);
        DOWN.followups.add(PILLARS);
        DOWN_AGGRESSIVE.followups.add(UP);
        DOWN_AGGRESSIVE.followups.add(UP_AGGRESSIVE);
        DOWN_SEISM.followups.add(UP);
        DOWN_STUCK.followups.add(UP);
        BURROW.followups.add(UP);
        BURROW.followups.add(BURROW_AGGRESSIVE);
        BURROW.followups.add(MORTAR);
        BURROW.followups.add(PILLARS);
        BURROW_AGGRESSIVE.followups.add(UP);
        BURROW_CHASE.followups.add(UP);
        MORTAR.followups.add(DOWN);
        PILLARS.followups.add(DOWN_SEISM);
        HOMING.followups.add(BURROW);
        HOMING.followups.add(DOWN);
    }

    protected final PhaseSupplier supplier;
    protected Random random = new Random();

    public MagmaWormPhase(PhaseSupplier supplier) {
        this.supplier = supplier;
    }

    public static double getHeight(World world, double x, double z) {
        return world.getHeight((int) x, (int) z);
    }

    public MagmaWormPhase pickFollowup(Random random) {
        return supplier.pickFollowup(random).get();
    }

    public double getVelocity(EntityMagmaWorm worm) {
        if(worm.isDigging())
            return 0.5;
        else
            return 1.0;
    }

    public void onStart(EntityMagmaWorm worm) {
        //NOOP
    }

    public void onInterrupt(EntityMagmaWorm worm) {
        //NOOP
    }

    public void onEnd(EntityMagmaWorm worm) {
        //NOOP
    }

    public void onUpdate(EntityMagmaWorm worm) {
        //NOOP
    }

    public void onDig(EntityMagmaWorm worm) {
        //NOOP
    }

    public void onUndig(EntityMagmaWorm worm) {
        //NOOP
    }

    public static void splashMagma(EntityMagmaWorm worm, Vec3d location, int splashes) {
        for(int i = 0; i < splashes; i++) {
            Vec3d velocity = Vec3d.fromPitchYaw(-45 + worm.random.nextFloat() * 30 - 15f, worm.random.nextFloat() * 360).scale(0.3f);
            EntityMagmaProjectile projectile = new EntityMagmaProjectile(worm.world, worm);
            projectile.setPosition(location.x, location.y, location.z);
            projectile.makeSplash(velocity);
            worm.world.spawnEntity(projectile);
        }
    }


}
