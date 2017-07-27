package teamroots.embers.itemmod;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import teamroots.embers.RegistryManager;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageSuperheatFX;
import teamroots.embers.util.EmberInventoryUtil;
import teamroots.embers.util.ItemModUtil;
import teamroots.embers.util.Misc;

public class ModifierBlastingCore extends ModifierBase {

	public ModifierBlastingCore() {
		super(EnumType.ALL,"blasting_core",2.0,true);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onDrops(BreakEvent event){
		if (event.getPlayer() != null){
			if (!event.getPlayer().getHeldItem(EnumHand.MAIN_HAND).isEmpty()){
				ItemStack s = event.getPlayer().getHeldItem(EnumHand.MAIN_HAND);
				if (ItemModUtil.getModifierLevel(s, ItemModUtil.modifierRegistry.get(RegistryManager.blasting_core).name) > 0 && EmberInventoryUtil.getEmberTotal(event.getPlayer()) >= cost){
					int blastingLevel = ItemModUtil.getModifierLevel(s, ItemModUtil.modifierRegistry.get(RegistryManager.blasting_core).name);
					event.getWorld().createExplosion(event.getPlayer(), event.getPos().getX()+0.5, event.getPos().getY()+0.5, event.getPos().getZ()+0.5, 0.5f, true);
					for (int i = 0; i < 6; i ++){
						EnumFacing face = EnumFacing.getFront(i);
						if (Misc.random.nextInt(blastingLevel) != 0){
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
				int blastingLevel = ItemModUtil.getModifierLevel(s, ItemModUtil.modifierRegistry.get(RegistryManager.blasting_core).name);
				float strength = (float)(2.0*(Math.atan(0.6*(blastingLevel))/(Math.PI)));
				if (blastingLevel > 0 && EmberInventoryUtil.getEmberTotal(damager) >= cost){
					event.getEntityLiving().world.createExplosion(event.getEntityLiving(), event.getEntityLiving().posX, event.getEntityLiving().posY+event.getEntityLiving().height/2.0, event.getEntityLiving().posZ, 0.5f, true);
					EmberInventoryUtil.removeEmber(damager, cost);
					List<EntityLivingBase> entities = damager.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(event.getEntityLiving().posX-4.0*strength,event.getEntityLiving().posY-4.0*strength,event.getEntityLiving().posZ-4.0*strength,
																																	event.getEntityLiving().posX+4.0*strength,event.getEntityLiving().posY+4.0*strength,event.getEntityLiving().posZ+4.0*strength));
					for (EntityLivingBase e : entities){
						if (e.getUniqueID().compareTo(damager.getUniqueID()) != 0){
							e.attackEntityFrom(DamageSource.GENERIC, event.getAmount()*strength);
						}
					}
				}
			}
		}
		if (event.getEntity() instanceof EntityPlayer){
			int blastingLevel = ItemModUtil.getArmorMod((EntityPlayer)event.getEntity(), ItemModUtil.modifierRegistry.get(RegistryManager.blasting_core).name);

			float strength = (float)(2.0*(Math.atan(0.6*(blastingLevel))/(Math.PI)));
			if (blastingLevel > 0 && EmberInventoryUtil.getEmberTotal(((EntityPlayer)event.getEntity())) >= cost){
				event.getEntityLiving().world.createExplosion(event.getEntityLiving(), event.getEntityLiving().posX, event.getEntityLiving().posY+event.getEntityLiving().height/2.0, event.getEntityLiving().posZ, 0.5f, true);
				EmberInventoryUtil.removeEmber(((EntityPlayer)event.getEntity()), cost);
				List<EntityLivingBase> entities = event.getEntity().world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(event.getEntityLiving().posX-4.0*strength,event.getEntityLiving().posY-4.0*strength,event.getEntityLiving().posZ-4.0*strength,
																																event.getEntityLiving().posX+4.0*strength,event.getEntityLiving().posY+4.0*strength,event.getEntityLiving().posZ+4.0*strength));
				for (EntityLivingBase e : entities){
					if (e.getUniqueID().compareTo(event.getEntity().getUniqueID()) != 0){
						e.attackEntityFrom(DamageSource.GENERIC, event.getAmount()*strength*0.25f);
						e.knockBack(event.getEntity(), 2.0f*strength, -e.posX+event.getEntity().posX, -e.posZ+event.getEntity().posZ);
					}
				}
			}
		}
	}
	
}
