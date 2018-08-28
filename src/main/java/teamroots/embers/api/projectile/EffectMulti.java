package teamroots.embers.api.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EffectMulti implements IProjectileEffect {
    List<IProjectileEffect> effects = new ArrayList<>();

    public EffectMulti(List<IProjectileEffect> effects) {
        this.effects.addAll(effects);
    }

    public List<IProjectileEffect> getEffects() {
        return effects;
    }

    public void addEffect(IProjectileEffect effect) {
        effects.add(effect);
    }

    @Override
    public void onHit(World world, RayTraceResult raytrace, IProjectilePreset projectile) {
        for (IProjectileEffect effect : effects) {
            effect.onHit(world, raytrace, projectile);
        }
    }

    @Override
    public void onEntityImpact(Entity entity, IProjectilePreset projectile) {
        for (IProjectileEffect effect : effects) {
            effect.onEntityImpact(entity, projectile);
        }
    }

    @Override
    public void onBlockImpact(World world, BlockPos pos, EnumFacing side, IProjectilePreset projectile) {
        for (IProjectileEffect effect : effects) {
            effect.onBlockImpact(world, pos, side, projectile);
        }
    }

    @Override
    public void onFizzle(World world, Vec3d pos, IProjectilePreset projectile) {
        for (IProjectileEffect effect : effects) {
            effect.onFizzle(world, pos, projectile);
        }
    }
}
