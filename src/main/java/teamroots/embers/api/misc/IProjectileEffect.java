package teamroots.embers.api.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IProjectileEffect {
    void onEntityImpact(Entity entity, EntityLivingBase shooter, @Nullable Entity projectile);

    void onBlockImpact(World world, BlockPos pos, EntityLivingBase shooter, @Nullable Entity projectile);

    void onFizzle(World world, Vec3d pos, @Nullable Entity projectile);

    void shoot(World world, Vec3d pos, Vec3d velocity, EntityLivingBase shooter, ItemStack weapon);
}
