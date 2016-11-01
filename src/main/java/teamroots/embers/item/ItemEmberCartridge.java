package teamroots.embers.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageCannonBeamFX;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.util.EmberInventoryUtil;
import teamroots.embers.util.Vec2i;
import teamroots.embers.world.EmberWorldData;

public class ItemEmberCartridge extends ItemBase implements IHeldEmberCell, IEmberItem {

	public ItemEmberCartridge() {
		super("emberCartridge", true);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected){
		if (!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound());
			setEmberCapacity(stack, 6000.0);
			setEmber(stack,0.0);
		}
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack){
		if (stack.hasTagCompound()){
			if (getEmber(stack) < getEmberCapacity(stack)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems){
		ItemStack emptyStack = new ItemStack(this,1);
		emptyStack.setTagCompound(new NBTTagCompound());
		setEmberCapacity(emptyStack, 6000.0);
		setEmber(emptyStack,0.0);
		subItems.add(emptyStack);
		ItemStack fullStack = new ItemStack(this,1);
		fullStack.setTagCompound(new NBTTagCompound());
		setEmberCapacity(fullStack, 6000.0);
		setEmber(fullStack,6000.0);
		subItems.add(fullStack);
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged){
		return slotChanged || oldStack.getItem() != newStack.getItem();
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack){
		if (stack.hasTagCompound()){
			return (getEmberCapacity(stack)-getEmber(stack))/getEmberCapacity(stack);
		}
		return 0.0;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced){
		if (stack.hasTagCompound()){
			tooltip.add(""+getEmber(stack)+" / "+getEmberCapacity(stack));
		}
	}

	@Override
	public double getEmber(ItemStack stack) {
		if (stack.hasTagCompound()){
			return stack.getTagCompound().getDouble(Embers.MODID+":ember");
		}
		return 0;
	}

	@Override
	public double getEmberCapacity(ItemStack stack) {
		if (stack.hasTagCompound()){
			return stack.getTagCompound().getDouble(Embers.MODID+":emberCapacity");
		}
		return 0;
	}

	@Override
	public void setEmber(ItemStack stack, double value) {
		if (!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setDouble(Embers.MODID+":ember",value);
	}

	@Override
	public void setEmberCapacity(ItemStack stack, double value) {
		if (!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setDouble(Embers.MODID+":emberCapacity",value);
	}

	@Override
	public double addAmount(ItemStack stack, double value, boolean doAdd) {
		double ember = stack.getTagCompound().getDouble(Embers.MODID+":ember");
		double capacity = stack.getTagCompound().getDouble(Embers.MODID+":emberCapacity");
		if (ember+value > capacity){
			double added = capacity-ember;
			if (doAdd){
				setEmber(stack,capacity);
				ember = capacity;
			}
			return added;
		}
		if (doAdd){
			setEmber(stack,ember+value);
		}
		return value;
	}

	@Override
	public double removeAmount(ItemStack stack, double value, boolean doRemove) {
		double ember = stack.getTagCompound().getDouble(Embers.MODID+":ember");
		double capacity = stack.getTagCompound().getDouble(Embers.MODID+":emberCapacity");
		if (ember-value < 0){
			double removed = ember;
			if (doRemove){
				setEmber(stack,0);
			}
			return removed;
		}
		if (doRemove){
			setEmber(stack, ember-value);
		}
		return value;
	}
}
