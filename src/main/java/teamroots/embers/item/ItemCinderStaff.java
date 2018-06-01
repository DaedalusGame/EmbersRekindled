package teamroots.embers.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.SoundManager;
import teamroots.embers.entity.EntityEmberProjectile;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.util.EmberInventoryUtil;

public class ItemCinderStaff extends ItemBase {

	public static final double EMBER_COST = 25.0;
	public static final int COOLDOWN = 10;

	public ItemCinderStaff() {
		super("staff_ember", true);
		this.setMaxStackSize(1);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft){
		if (!world.isRemote){
			double charge = ((Math.min(60, 72000-timeLeft))/60.0)*17.0;
			float spawnDistance = 2.0f;//Math.max(1.0f, (float)charge/5.0f);
			EntityEmberProjectile proj = new EntityEmberProjectile(world);
			double posX = entity.posX + entity.getLookVec().x * spawnDistance;
			double posY = entity.posY + entity.getEyeHeight() + entity.getLookVec().y * spawnDistance;
			double posZ = entity.posZ + entity.getLookVec().z * spawnDistance;
			proj.initCustom(posX, posY, posZ,entity.getLookVec().x*0.85, entity.getLookVec().y*0.85, entity.getLookVec().z*0.85, charge, entity.getUniqueID());
			world.spawnEntity(proj);
			world.playSound(null,posX,posY,posZ, SoundManager.FIREBALL_BIG, SoundCategory.PLAYERS, 1.0f, 1.0f);
		}
		stack.getTagCompound().setInteger("cooldown", COOLDOWN);
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged){
		return slotChanged || newStack.getItem() != oldStack.getItem();
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
	
	@SideOnly(Side.CLIENT)
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count){
		double charge = ((Math.min(60, 72000-count))/60.0)*15.0;
		for (int i = 0; i < 4; i ++){
			float spawnDistance = 2.0f;//Math.max(1.0f, (float)charge/5.0f);
			ParticleUtil.spawnParticleGlow(player.getEntityWorld(), (float)player.posX+spawnDistance*(float)player.getLookVec().x+(itemRand.nextFloat()*0.1f-0.05f), (float)player.posY+player.getEyeHeight()+spawnDistance*(float)player.getLookVec().y+(itemRand.nextFloat()*0.1f-0.05f), (float)player.posZ+spawnDistance*(float)player.getLookVec().z+(itemRand.nextFloat()*0.1f-0.05f), 0, 0, 0, 255, 64, 16, (float)charge/1.75f, 24);
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
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
		ItemStack stack = player.getHeldItem(hand);
		if (EmberInventoryUtil.getEmberTotal(player) >= EMBER_COST && stack.getTagCompound().getInteger("cooldown") <= 0 || player.capabilities.isCreativeMode){
			EmberInventoryUtil.removeEmber(player, EMBER_COST);
			player.setActiveHand(hand);
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}
		return new ActionResult<>(EnumActionResult.FAIL, stack);
	}
}
