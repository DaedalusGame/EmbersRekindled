package teamroots.embers.itemmod;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.api.itemmod.ModifierBase;
import teamroots.embers.util.EmberInventoryUtil;
import teamroots.embers.util.Misc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class ModifierBlastingCore extends ModifierBase {
	private final HashSet<Entity> blastedEntities = new HashSet<>();
	public ModifierBlastingCore() {
		super(EnumType.TOOL_OR_ARMOR,"blasting_core",2.0,true);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private double getChanceBonus(double resonance) {
		if (resonance > 1.0) {
			return 1.0 + (resonance - 1.0) * 0.5;
		}
		return resonance;
	}

	@SubscribeEvent
	public void onDrops(BreakEvent event){
		ItemStack s;
		int blastingLevel;
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		if (event.getPlayer() != null && !event.getPlayer().getHeldItem(EnumHand.MAIN_HAND).isEmpty() && (blastingLevel = ItemModUtil.getModifierLevel(s = event.getPlayer().getHeldItem(EnumHand.MAIN_HAND), EmbersAPI.BLASTING_CORE)) > 0 && EmberInventoryUtil.getEmberTotal(event.getPlayer()) >= this.cost) {
            world.createExplosion(event.getPlayer(), (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, 0.5f, true);
            double resonance = EmbersAPI.getEmberEfficiency(s);
            double chance = (double)blastingLevel / (double)(blastingLevel + 1) * this.getChanceBonus(resonance);
            for (BlockPos toExplode : this.getBlastCube(world, pos, event.getPlayer(), chance)) {
                IBlockState state = world.getBlockState(toExplode);
                if (!(state.getBlockHardness(world, toExplode) >= 0.0f) || !event.getPlayer().canHarvestBlock(world.getBlockState(toExplode))) continue;
                world.destroyBlock(toExplode, true);
                world.notifyBlockUpdate(toExplode, state, Blocks.AIR.getDefaultState(), 8);
            }
            EmberInventoryUtil.removeEmber(event.getPlayer(), this.cost);
        }
	}

	public Iterable<BlockPos> getBlastAdjacent(World world, BlockPos pos, EntityPlayer player, double chance) {
		ArrayList<BlockPos> posList = new ArrayList<>();
		for (int i = 0; i < 6; i ++){
			EnumFacing face = EnumFacing.getFront(i);
			if (Misc.random.nextDouble() < chance){
				posList.add(pos.offset(face));
			}
		}
		return posList;
	}

	public Iterable<BlockPos> getBlastCube(World world, BlockPos pos, EntityPlayer player, double chance) {
		ArrayList<BlockPos> posList = new ArrayList<>();
		for (EnumFacing facePrimary : EnumFacing.VALUES){
			if (Misc.random.nextDouble() < chance){
				BlockPos posPrimary = pos.offset(facePrimary);
				posList.add(posPrimary);

				for (EnumFacing faceSecondary : EnumFacing.VALUES){
					if(faceSecondary.getAxis() == facePrimary.getAxis())
						continue;
					if (Misc.random.nextDouble() < chance - 0.5){
						BlockPos posSecondary = posPrimary.offset(faceSecondary);
						posList.add(posSecondary);

						for (EnumFacing faceTertiary : EnumFacing.VALUES){
							if(faceTertiary.getAxis() == facePrimary.getAxis() || faceTertiary.getAxis() == faceSecondary.getAxis())
								continue;
							if (Misc.random.nextDouble() < chance - 1.0){
								BlockPos posTertiary = posSecondary.offset(faceTertiary);
								posList.add(posTertiary);
							}
						}
					}
				}
			}
		}
		return posList;
	}

	@SubscribeEvent
	public void onHit(LivingHurtEvent event){
		if(!this.blastedEntities.contains(event.getEntity()) && event.getSource().getTrueSource() != event.getEntity() && event.getSource().getImmediateSource() != event.getEntity())
		try {
			if (event.getSource().getTrueSource() instanceof EntityPlayer) {
				EntityPlayer damager = (EntityPlayer) event.getSource().getTrueSource();
				blastedEntities.add(damager);
				ItemStack s = damager.getHeldItemMainhand();
				if (!s.isEmpty()) {
					int blastingLevel = ItemModUtil.getModifierLevel(s, EmbersAPI.BLASTING_CORE);
					if (blastingLevel > 0 && EmberInventoryUtil.getEmberTotal(damager) >= cost) {
						double resonance = EmbersAPI.getEmberEfficiency(s);
						float strength = (float) ((resonance + 1) * (Math.atan(0.6 * (blastingLevel)) / (Math.PI)));

						EmberInventoryUtil.removeEmber(damager, cost);
						List<EntityLivingBase> entities = damager.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(event.getEntityLiving().posX - 4.0 * strength, event.getEntityLiving().posY - 4.0 * strength, event.getEntityLiving().posZ - 4.0 * strength,
								event.getEntityLiving().posX + 4.0 * strength, event.getEntityLiving().posY + 4.0 * strength, event.getEntityLiving().posZ + 4.0 * strength));
						for (EntityLivingBase e : entities) {
							if (!Objects.equals(e.getUniqueID(), damager.getUniqueID())) {
								e.attackEntityFrom(DamageSource.causeExplosionDamage(damager), event.getAmount() * strength);
								e.hurtResistantTime = 0;
							}
						}
						event.getEntityLiving().world.createExplosion(event.getEntityLiving(), event.getEntityLiving().posX, event.getEntityLiving().posY + event.getEntityLiving().height / 2.0, event.getEntityLiving().posZ, 0.5f, true);
					}
				}
			}
			if (event.getEntity() instanceof EntityPlayer) {
				EntityPlayer damager = (EntityPlayer) event.getEntity();
				int blastingLevel = ItemModUtil.getArmorModifierLevel(damager, EmbersAPI.BLASTING_CORE);

				if (blastingLevel > 0 && EmberInventoryUtil.getEmberTotal(damager) >= cost) {
					float strength = (float) (2.0 * (Math.atan(0.6 * (blastingLevel)) / (Math.PI)));
					EmberInventoryUtil.removeEmber(damager, cost);
					List<EntityLivingBase> entities = damager.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(event.getEntityLiving().posX - 4.0 * strength, event.getEntityLiving().posY - 4.0 * strength, event.getEntityLiving().posZ - 4.0 * strength,
							event.getEntityLiving().posX + 4.0 * strength, event.getEntityLiving().posY + 4.0 * strength, event.getEntityLiving().posZ + 4.0 * strength));
					for (EntityLivingBase e : entities) {
						if (!Objects.equals(e.getUniqueID(), event.getEntity().getUniqueID())) {
							blastedEntities.add(e);
							e.attackEntityFrom(DamageSource.causeExplosionDamage(damager), event.getAmount() * strength * 0.25f);
							e.knockBack(event.getEntity(), 2.0f * strength, -e.posX + damager.posX, -e.posZ + damager.posZ);
							e.hurtResistantTime = 0;
						}
					}
					event.getEntityLiving().world.createExplosion(event.getEntityLiving(), event.getEntityLiving().posX, event.getEntityLiving().posY + event.getEntityLiving().height / 2.0, event.getEntityLiving().posZ, 0.5f, true);
				}
			}
		} finally {
			blastedEntities.clear();
		}
	}
	
}
