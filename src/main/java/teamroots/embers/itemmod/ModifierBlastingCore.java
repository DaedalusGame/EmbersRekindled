package teamroots.embers.itemmod;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.api.itemmod.ModifierBase;
import teamroots.embers.util.EmberInventoryUtil;
import teamroots.embers.util.Misc;

import java.util.List;

public class ModifierBlastingCore extends ModifierBase {

	public ModifierBlastingCore() {
		super(EnumType.TOOL_OR_ARMOR,"blasting_core",2.0,true);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onDrops(BreakEvent event){
		if (event.getPlayer() != null){
			if (!event.getPlayer().getHeldItem(EnumHand.MAIN_HAND).isEmpty()){
				ItemStack s = event.getPlayer().getHeldItem(EnumHand.MAIN_HAND);
				if (ItemModUtil.getModifierLevel(s, EmbersAPI.BLASTING_CORE) > 0 && EmberInventoryUtil.getEmberTotal(event.getPlayer()) >= cost){
					int blastingLevel = ItemModUtil.getModifierLevel(s, EmbersAPI.BLASTING_CORE);
					event.getWorld().createExplosion(event.getPlayer(), event.getPos().getX()+0.5, event.getPos().getY()+0.5, event.getPos().getZ()+0.5, 0.5f, true);
					for (int i = 0; i < 6; i ++){
						EnumFacing face = EnumFacing.getFront(i);
						if (Misc.random.nextInt(blastingLevel+1) != 0){
							BlockPos pos = event.getPos().offset(face);
							if (event.getPlayer().canHarvestBlock(event.getWorld().getBlockState(pos))){
								IBlockState state = event.getWorld().getBlockState(pos);
								event.getWorld().destroyBlock(pos, true);
								event.getWorld().notifyBlockUpdate(pos, state, Blocks.AIR.getDefaultState(), 8);
							}
						}
					}
					EmberInventoryUtil.removeEmber(event.getPlayer(), cost);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onHit(LivingHurtEvent event){
		if (event.getSource().getTrueSource() instanceof EntityPlayer){
			EntityPlayer damager = (EntityPlayer)event.getSource().getTrueSource();
			ItemStack s = damager.getHeldItemMainhand();
			if (!s.isEmpty()){
				int blastingLevel = ItemModUtil.getModifierLevel(s, EmbersAPI.BLASTING_CORE);
				float strength = (float)(2.0*(Math.atan(0.6*(blastingLevel))/(Math.PI)));
				if (blastingLevel > 0 && EmberInventoryUtil.getEmberTotal(damager) >= cost){
					EmberInventoryUtil.removeEmber(damager, cost);
					List<EntityLivingBase> entities = damager.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(event.getEntityLiving().posX-4.0*strength,event.getEntityLiving().posY-4.0*strength,event.getEntityLiving().posZ-4.0*strength,
																																	event.getEntityLiving().posX+4.0*strength,event.getEntityLiving().posY+4.0*strength,event.getEntityLiving().posZ+4.0*strength));
					for (EntityLivingBase e : entities){
						if (e.getUniqueID().compareTo(damager.getUniqueID()) != 0){
							e.attackEntityFrom(DamageSource.causeExplosionDamage(damager), event.getAmount()*strength);
							e.hurtResistantTime = 0;
						}
					}
					event.getEntityLiving().world.createExplosion(event.getEntityLiving(), event.getEntityLiving().posX, event.getEntityLiving().posY+event.getEntityLiving().height/2.0, event.getEntityLiving().posZ, 0.5f, true);
				}
			}
		}
		if (event.getEntity() instanceof EntityPlayer){
			EntityPlayer damager = (EntityPlayer)event.getEntity();
			int blastingLevel = ItemModUtil.getArmorModifierLevel(damager, EmbersAPI.BLASTING_CORE);

			if (blastingLevel > 0 && EmberInventoryUtil.getEmberTotal(damager) >= cost){
				float strength = (float)(2.0*(Math.atan(0.6*(blastingLevel))/(Math.PI)));
				EmberInventoryUtil.removeEmber(damager, cost);
				List<EntityLivingBase> entities = damager.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(event.getEntityLiving().posX-4.0*strength,event.getEntityLiving().posY-4.0*strength,event.getEntityLiving().posZ-4.0*strength,
																																event.getEntityLiving().posX+4.0*strength,event.getEntityLiving().posY+4.0*strength,event.getEntityLiving().posZ+4.0*strength));
				for (EntityLivingBase e : entities){
					if (e.getUniqueID().compareTo(event.getEntity().getUniqueID()) != 0){
						e.attackEntityFrom(DamageSource.causeExplosionDamage(damager), event.getAmount()*strength*0.25f);
						e.knockBack(event.getEntity(), 2.0f*strength, -e.posX+damager.posX, -e.posZ+damager.posZ);
						e.hurtResistantTime = 0;
					}
				}
				event.getEntityLiving().world.createExplosion(event.getEntityLiving(), event.getEntityLiving().posX, event.getEntityLiving().posY+event.getEntityLiving().height/2.0, event.getEntityLiving().posZ, 0.5f, true);
			}
		}
	}
	
}
