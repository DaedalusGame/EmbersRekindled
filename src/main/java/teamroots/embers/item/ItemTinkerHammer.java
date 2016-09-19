package teamroots.embers.item;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import teamroots.embers.power.IEmberPacketProducer;
import teamroots.embers.power.IEmberPacketReceiver;
import teamroots.embers.util.Vec2i;
import teamroots.embers.world.EmberWorldData;

public class ItemTinkerHammer extends ItemBase {
	public ItemTinkerHammer() {
		super("tinkerHammer", true);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected){
		if (!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound());
		}
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ){
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof IEmberPacketReceiver && player.isSneaking()){
			stack.getTagCompound().setInteger("targetX", pos.getX());
			stack.getTagCompound().setInteger("targetY", pos.getY());
			stack.getTagCompound().setInteger("targetZ", pos.getZ());
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 1.0f, 2.0f, false);
			return EnumActionResult.SUCCESS;
		}
		else if (tile instanceof IEmberPacketProducer && stack.getTagCompound().hasKey("targetX")){
			((IEmberPacketProducer)tile).setTargetPosition(new BlockPos(stack.getTagCompound().getInteger("targetX"),stack.getTagCompound().getInteger("targetY"),stack.getTagCompound().getInteger("targetZ")));
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
			return EnumActionResult.SUCCESS;
		}
		else if (stack.getTagCompound().hasKey("targetX")){
			stack.getTagCompound().removeTag("targetX");
			stack.getTagCompound().removeTag("targetY");
			stack.getTagCompound().removeTag("targetZ");
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 1.0f, 2.0f, false);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced){
		if (stack.hasTagCompound()){
			if (stack.getTagCompound().hasKey("targetX")){
				tooltip.add(I18n.format("embers.tooltip.targetingBlock"));
				tooltip.add(" X=" + stack.getTagCompound().getInteger("targetX"));
				tooltip.add(" Y=" + stack.getTagCompound().getInteger("targetY"));
				tooltip.add(" Z=" + stack.getTagCompound().getInteger("targetZ"));
			}
		}
	}
}
