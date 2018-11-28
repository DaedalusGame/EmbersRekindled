package teamroots.embers.item;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.api.item.IInflictorGem;
import teamroots.embers.api.item.IInflictorGemHolder;
import teamroots.embers.api.item.IInfoGoggles;
import teamroots.embers.model.ModelAshenCloak;

import java.util.Objects;

public class ItemAshenCloak extends ItemArmorBase implements IInflictorGemHolder, IInfoGoggles {

	public ItemAshenCloak(ArmorMaterial material, int reduction, EntityEquipmentSlot slot) {
		super(material, reduction, slot, "ashen_cloak", true);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type){
		return "embers:textures/models/armor/robe.png";
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public ModelBiped getArmorModel(EntityLivingBase living, ItemStack stack, EntityEquipmentSlot slot, ModelBiped _default){
		return new ModelAshenCloak(slot);
	}

	@Override
	public int getGemSlots(ItemStack holder) {
		return 7;
	}

	@Override
	public boolean canAttachGem(ItemStack holder, ItemStack gem) {
		return gem.getItem() instanceof IInflictorGem;
	}

	@Override
	public void attachGem(ItemStack holder, ItemStack gem, int slot) {
		if (!holder.hasTagCompound()) {
			holder.setTagCompound(new NBTTagCompound());
		}
		holder.getTagCompound().setTag("gem"+slot, gem.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public ItemStack detachGem(ItemStack holder, int slot) {
		if (holder.hasTagCompound() && holder.getTagCompound().hasKey("gem"+slot)) {
			ItemStack gem = new ItemStack(holder.getTagCompound().getCompoundTag("gem"+slot));
			holder.getTagCompound().removeTag("gem"+slot);
			return gem;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void clearGems(ItemStack holder) {
		NBTTagCompound tagCompound = holder.getTagCompound();
		if(tagCompound == null)
			return;
		for (int i = 1; i <= getGemSlots(holder); i ++){
			if (tagCompound.hasKey("gem"+i)){
				tagCompound.removeTag("gem"+i);
			}
		}
	}

	@Override
	public ItemStack[] getAttachedGems(ItemStack holder) { //Potentially default???
		ItemStack[] stacks = new ItemStack[getGemSlots(holder)];
		for(int i = 1; i <= stacks.length; i++) {
			if(holder.hasTagCompound())
				stacks[i-1] = new ItemStack(holder.getTagCompound().getCompoundTag("gem"+i));
			else
				stacks[i-1] = ItemStack.EMPTY;
		}
		return stacks;
	}

	@Override
	public float getTotalDamageResistance(EntityLivingBase entity, DamageSource source, ItemStack holder) {
		float reduction = 0;

		if (entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemAshenCloak &&
				entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemAshenCloak &&
				entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() instanceof ItemAshenCloak &&
				entity.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() instanceof ItemAshenCloak) {
			for (ItemStack stack : getAttachedGems(holder)) {
				Item item = stack.getItem();
				if(item instanceof IInflictorGem && Objects.equals(((IInflictorGem) item).getAttunedSource(stack),source.getDamageType())) {
					reduction += ((IInflictorGem) item).getDamageResistance(stack,reduction);
				}
			}
		}

		return reduction;
	}

	@Override
	public boolean shouldDisplayInfo(EntityPlayer player, ItemStack stack, EntityEquipmentSlot slot) {
		return slot == EntityEquipmentSlot.HEAD;
	}
}
