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
import net.minecraft.item.EnumAction;
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
import teamroots.embers.RegistryManager;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageCannonBeamFX;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.util.EmberInventoryUtil;
import teamroots.embers.util.Vec2i;
import teamroots.embers.world.EmberWorldData;

public class ItemEmberCartridge extends ItemBase implements IHeldEmberCell {

	public ItemEmberCartridge() {
		super("emberCartridge", true);
		this.setMaxStackSize(1);
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack){
		if (stack.hasCapability(EmberCapabilityProvider.emberCapability, null)){
			if (stack.getCapability(EmberCapabilityProvider.emberCapability, null).getEmber() < stack.getCapability(EmberCapabilityProvider.emberCapability, null).getEmberCapacity()){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack){
		if (stack.hasCapability(EmberCapabilityProvider.emberCapability, null)){
			return (stack.getCapability(EmberCapabilityProvider.emberCapability, null).getEmberCapacity()-stack.getCapability(EmberCapabilityProvider.emberCapability, null).getEmber())/stack.getCapability(EmberCapabilityProvider.emberCapability, null).getEmberCapacity();
		}
		return 0.0;
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt){
		return new MyEmberProvider();
	}
	
	public class MyEmberProvider implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound> {
		IEmberCapability capability = new DefaultEmberCapability();

		public MyEmberProvider(){
			capability.setEmberCapacity(6000.0);
			capability.setEmber(0);
		}
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			if (capability == EmberCapabilityProvider.emberCapability){
				return true;
			}
			return false;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if (capability == EmberCapabilityProvider.emberCapability){
				return (T)(this.capability);
			}
			return null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound tag = new NBTTagCompound();
			capability.writeToNBT(tag);
			return tag;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			capability.readFromNBT(nbt);
		}
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged){
		return slotChanged;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced){
		if (stack.hasCapability(EmberCapabilityProvider.emberCapability, null)){
			tooltip.add(""+stack.getCapability(EmberCapabilityProvider.emberCapability, null).getEmber()+" / "+stack.getCapability(EmberCapabilityProvider.emberCapability, null).getEmberCapacity());
		}
	}
}
