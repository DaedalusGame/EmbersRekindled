package teamroots.embers.api.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ScaleEvent extends Event {
	private EntityLivingBase entity;
	private double scalePassRate = 0.0;
	private double scaleDamageRate = 1.0;
	private float damage;
	private DamageSource source;

	public ScaleEvent(EntityLivingBase entity, float damage, DamageSource source, double scaleDamageRate, double scalePassRate) {
		this.entity = entity;
		this.scalePassRate = scalePassRate;
		this.scaleDamageRate = scaleDamageRate;
		this.damage = damage;
		this.source = source;
	}

	public EntityLivingBase getEntity(){
		return entity;
	}

	public double getDamage() {
		return damage;
	}

	public DamageSource getDamageSource() {
		return source;
	}

	public double getScalePassRate() {
		return scalePassRate;
	}

	public void setScalePassRate(double scalePassRate) {
		this.scalePassRate = scalePassRate;
	}

	public double getScaleDamageRate() {
		return scaleDamageRate;
	}

	public void setScaleDamageRate(double scaleDamageRate) {
		this.scaleDamageRate = scaleDamageRate;
	}
}
