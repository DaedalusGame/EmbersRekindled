package teamroots.embers.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.embers.RegistryManager;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageEmberSparkleFX;

import java.awt.*;

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
				BlockPos lightPos = pos.offset(face);
				if (world.isAirBlock(lightPos)){
					stack.getTagCompound().setInteger("light",stack.getTagCompound().getInteger("light")-10);
					world.setBlockState(lightPos, RegistryManager.glow.getDefaultState());
					//world.notifyBlockUpdate(lightPos, Blocks.AIR.getDefaultState(), RegistryManager.glow.getDefaultState(), 8);
					PacketHandler.INSTANCE.sendToAll(new MessageEmberSparkleFX(lightPos.getX()+0.5,lightPos.getY()+0.5,lightPos.getZ()+0.5,false));
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
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return Color.WHITE.getRGB();
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
