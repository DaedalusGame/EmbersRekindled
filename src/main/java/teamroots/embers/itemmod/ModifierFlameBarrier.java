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
import teamroots.embers.RegistryManager;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageFlameShieldFX;
import teamroots.embers.network.message.MessageSuperheatFX;
import teamroots.embers.util.EmberInventoryUtil;
import teamroots.embers.util.ItemModUtil;
import teamroots.embers.util.Misc;

public class ModifierFlameBarrier extends ModifierBase {

	public ModifierFlameBarrier() {
		super(EnumType.ARMOR,"flame_barrier",2.0,true);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onHit(LivingHurtEvent event){
		if (event.getEntity() instanceof EntityPlayer && event.getSource().getEntity() instanceof EntityLivingBase){
			int blastingLevel = ItemModUtil.getArmorMod((EntityPlayer)event.getEntity(), ItemModUtil.modifierRegistry.get(RegistryManager.flame_barrier).name);

			float strength = (float)(2.0*(Math.atan(0.6*(blastingLevel))/(Math.PI)));
			if (blastingLevel > 0 && EmberInventoryUtil.getEmberTotal(((EntityPlayer)event.getEntity())) >= cost){
				EmberInventoryUtil.removeEmber(((EntityPlayer)event.getEntity()), cost);
				((EntityLivingBase)event.getSource().getEntity()).attackEntityFrom(RegistryManager.damage_ember.causePlayerDamage((EntityPlayer)event.getEntity()), strength*event.getAmount()*0.5f);
				((EntityLivingBase)event.getSource().getEntity()).setFire(blastingLevel+1);
				if (!event.getEntity().world.isRemote){
					PacketHandler.INSTANCE.sendToAll(new MessageFlameShieldFX(event.getEntity().posX,event.getEntity().posY+event.getEntity().height/2.0,event.getEntity().posZ));
				}
			}
		}
	}
	
}
