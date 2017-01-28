package teamroots.embers.item;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.embers.RegistryManager;
import teamroots.embers.power.IEmberPacketProducer;
import teamroots.embers.power.IEmberPacketReceiver;
import teamroots.embers.tileentity.ITargetable;

public class ItemGlimmerShard extends ItemBase {
	public ItemGlimmerShard() {
		super("glimmer_shard", true);
		this.setMaxStackSize(1);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected){
		if (!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("light", 800);
		}
		else {
			if (!world.isRemote){
				if (world.getLightBrightness(entity.getPosition()) > 0.625f && entity.posY > world.getTopSolidOrLiquidBlock(entity.getPosition()).getY()-2){
					stack.getTagCompound().setInteger("light", Math.min(800, stack.getTagCompound().getInteger("light")+1));
				}
			}
		}
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ){
		ItemStack stack = player.getHeldItem(hand);
		if (stack.hasTagCompound()){
			if (stack.getTagCompound().getInteger("light") >= 10){
				if (world.isAirBlock(pos.offset(face))){
					stack.getTagCompound().setInteger("light",stack.getTagCompound().getInteger("light")-10);
					world.setBlockState(pos.offset(face), RegistryManager.glow.getDefaultState());
					world.notifyBlockUpdate(pos.offset(face), Blocks.AIR.getDefaultState(), RegistryManager.glow.getDefaultState(), 8);
					return EnumActionResult.SUCCESS;
				}
			}
		}
		return EnumActionResult.FAIL;
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack){
		if (stack.hasTagCompound()){
			if (stack.getTagCompound().getInteger("light") < 800){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged){
		return slotChanged || oldStack.getItem() != newStack.getItem();
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack){
		if (stack.hasTagCompound()){
			return (800.0-(double)stack.getTagCompound().getInteger("light"))/800.0;
		}
		return 0.0;
	}
}
