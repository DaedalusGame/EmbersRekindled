package teamroots.embers.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import teamroots.embers.RegistryManager;
import teamroots.embers.entity.EntityEmberProjectile;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.IEmberPacketProducer;
import teamroots.embers.power.IEmberPacketReceiver;
import teamroots.embers.util.EmberInventoryUtil;
import teamroots.embers.util.Vec2i;
import teamroots.embers.world.EmberWorldData;

public class ItemCinderStaff extends ItemBase {
	public ItemCinderStaff() {
		super("staffEmber", true);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft){
		if (!world.isRemote){
			double charge = (((double)Math.min(60, 72000-timeLeft))/60.0)*17.0;
			float spawnDistance = 2.0f;//Math.max(1.0f, (float)charge/5.0f);
			EntityEmberProjectile proj = new EntityEmberProjectile(world);
			proj.initCustom(entity.posX+entity.getLookVec().xCoord*spawnDistance,entity.posY+entity.getEyeHeight()+entity.getLookVec().yCoord*spawnDistance,entity.posZ+entity.getLookVec().zCoord*spawnDistance,entity.getLookVec().xCoord*0.85, entity.getLookVec().yCoord*0.85, entity.getLookVec().zCoord*0.85, charge, entity.getUniqueID());
			world.spawnEntityInWorld(proj);
		}
		stack.getTagCompound().setInteger("cooldown", 10);
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged){
		return slotChanged;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected){
		if (!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("cooldown", 0);
		}
		else {
			if (stack.getTagCompound().getInteger("cooldown") > 0){
				stack.getTagCompound().setInteger("cooldown", stack.getTagCompound().getInteger("cooldown")-1);
			}
		}
	}
	
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count){
		double charge = (((double)Math.min(60, 72000-count))/60.0)*15.0;
		for (int i = 0; i < 4; i ++){
			float spawnDistance = 2.0f;//Math.max(1.0f, (float)charge/5.0f);
			ParticleUtil.spawnParticleGlow(player.getEntityWorld(), (float)player.posX+spawnDistance*(float)player.getLookVec().xCoord+(itemRand.nextFloat()*0.1f-0.05f), (float)player.posY+player.getEyeHeight()+spawnDistance*(float)player.getLookVec().yCoord+(itemRand.nextFloat()*0.1f-0.05f), (float)player.posZ+spawnDistance*(float)player.getLookVec().zCoord+(itemRand.nextFloat()*0.1f-0.05f), 0, 0, 0, 255, 64, 16, (float)charge/1.75f);
		}
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack){
		return 72000;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack){
		return EnumAction.BOW;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand){
		if (EmberInventoryUtil.getEmberTotal(player) >= 25.0 && stack.getTagCompound().getInteger("cooldown") <= 0){
			EmberInventoryUtil.removeEmber(player, 25.0);
			player.setActiveHand(hand);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS,stack);
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS,stack);
	}
}
