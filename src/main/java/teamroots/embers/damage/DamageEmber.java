package teamroots.embers.damage;

import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import teamroots.embers.api.projectile.IProjectilePreset;

import java.util.function.Function;

public class DamageEmber extends DamageSource {
	public static Function<IProjectilePreset, DamageSource> EMBER_DAMAGE_SOURCE_FACTORY = (projectile) -> {
		if(projectile == null)
			return new DamageSource("ember").setMagicDamage();
		else if(projectile.getEntity() == null)
			return new EntityDamageSource("ember", projectile.getShooter()).setMagicDamage();
		else
			return new EntityDamageSourceIndirect("ember", projectile.getEntity(), projectile.getShooter()).setMagicDamage();
	};

	public DamageEmber() {
		super("ember");
		this.setMagicDamage();
	}
}
