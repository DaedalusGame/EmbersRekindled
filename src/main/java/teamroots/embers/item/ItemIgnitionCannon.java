package teamroots.embers.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.embers.RegistryManager;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageCannonBeamFX;
import teamroots.embers.util.EmberInventoryUtil;

public class ItemIgnitionCannon extends ItemBase {
	public ItemIgnitionCannon() {
		super("ignition_cannon", true);
		this.setMaxStackSize(1);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft){
		double charge = (Math.min(20, timeLeft))/20.0;
		double posX = entity.posX+entity.getLookVec().xCoord+(entity.width/2.0)*Math.sin(Math.toRadians(-entity.rotationYaw-90));
		double posY = entity.posY+entity.getEyeHeight()-0.2+entity.getLookVec().yCoord;
		double posZ = entity.posZ+entity.getLookVec().zCoord+(entity.width/2.0)*Math.cos(Math.toRadians(-entity.rotationYaw-90));
		
		double targX = entity.posX+entity.getLookVec().xCoord*96.0f+(30.0*(1.0-charge)*(itemRand.nextFloat()-0.5));
		double targY = entity.posY+entity.getLookVec().yCoord*96.0f+(30.0*(1.0-charge)*(itemRand.nextFloat()-0.5));
		double targZ = entity.posZ+entity.getLookVec().zCoord*96.0f+(30.0*(1.0-charge)*(itemRand.nextFloat()-0.5));
		
		double dX = targX-posX;
		double dY = targY-posY;
		double dZ = targZ-posZ;
		boolean doContinue = true;
		if (!world.isRemote){
			PacketHandler.INSTANCE.sendToAll(new MessageCannonBeamFX(entity.getUniqueID(),posX,posY,posZ,dX,dY,dZ));
		}
		for (double i = 0; i < 384.0 && doContinue; i ++){
			for (int j = 0; j < 5; j ++){
				posX += 0.2*i*dX/384.0;
				posY += 0.2*i*dY/384.0;
				posZ += 0.2*i*dZ/384.0;
			}
			IBlockState state = world.getBlockState(new BlockPos(posX,posY,posZ));
			if (state.isFullCube() && state.isOpaqueCube()){
				doContinue = false;
			}
			List<EntityLivingBase> rawEntities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX-0.85,posY-0.85,posZ-0.85,posX+0.85,posY+0.85,posZ+0.85));
			ArrayList<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
			for (int j = 0; j < rawEntities.size(); j ++){
				if (rawEntities.get(j).getUniqueID().compareTo(entity.getUniqueID()) != 0){
					entities.add(rawEntities.get(j));
				}
			}
			if (entities.size() > 0){
				entities.get(0).setFire(1);
				entities.get(0).attackEntityFrom(DamageSource.causeMobDamage(entity), 7.0f);
				entities.get(0).setLastAttacker(entity);
				entities.get(0).setRevengeTarget(entity);
				entities.get(0).knockBack(entity, 0.5f, -dX, -dZ);
				doContinue = false;
			}
		}
		stack.getTagCompound().setInteger("cooldown", 10);
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
		if (EmberInventoryUtil.getEmberTotal(player) >= 25.0 && stack.getTagCompound().getInteger("cooldown") <= 0 || player.capabilities.isCreativeMode){
			EmberInventoryUtil.removeEmber(player, 25.0);
			player.setActiveHand(hand);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS,stack);
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL,stack);
	}
}
