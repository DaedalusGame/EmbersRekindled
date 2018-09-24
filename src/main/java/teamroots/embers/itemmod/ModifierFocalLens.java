package teamroots.embers.itemmod;

import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import teamroots.embers.ConfigManager;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.event.EmberProjectileEvent;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.api.itemmod.ModifierProjectileBase;
import teamroots.embers.api.projectile.IProjectilePreset;
import teamroots.embers.api.projectile.ProjectileFireball;
import teamroots.embers.api.projectile.ProjectileRay;

import java.util.ListIterator;

public class ModifierFocalLens extends ModifierProjectileBase {
	public ModifierFocalLens() {
		super("focal_lens",10.0,true);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onProjectileFire(EmberProjectileEvent event) {
		ListIterator<IProjectilePreset> projectiles = event.getProjectiles().listIterator();

		ItemStack weapon = event.getStack();
		if(!weapon.isEmpty() && ItemModUtil.hasHeat(weapon)) {
			int level = ItemModUtil.getModifierLevel(weapon, EmbersAPI.FOCAL_LENS);
			int index = 0;
			int modulo = 1 + (level-1) * 2;
			if(level > 0)
				while (projectiles.hasNext()) {
					IProjectilePreset projectile = projectiles.next();
					if (projectile instanceof ProjectileRay) {
						((ProjectileRay) projectile).setPierceEntities(true);
					}
					else if(projectile instanceof ProjectileFireball) {
						((ProjectileFireball) projectile).setHoming(level * 10,4.0 + level * 1.0,index,modulo, Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, entity -> {
							Entity shooter = projectile.getShooter();
							if(shooter != null && entity.isOnSameTeam(shooter))
								return false;
							if(entity instanceof EntityPlayer && shooter instanceof EntityPlayer && !isPVPEnabled(entity.getEntityWorld()))
								return false;
							return entity.canBeCollidedWith() && shooter != entity;
						}));
					}
					index++;
				}
		}
	}

	public static boolean isPVPEnabled(World world) {
		MinecraftServer server = world.getMinecraftServer();
		return server != null && server.isPVPEnabled() && ConfigManager.pvpEverybodyIsEnemy;
	}
}
