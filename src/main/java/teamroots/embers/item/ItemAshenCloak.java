package teamroots.embers.item;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.model.ModelAshenCloak;

public class ItemAshenCloak extends ItemArmorBase {

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
	
	public static float getDamageMultiplier(DamageSource source, ItemStack stack){
		float mult = 0.0f;
		for (int i = 1; i < 8; i ++){
			if (stack.hasTagCompound()){
				ItemStack gem = new ItemStack(stack.getTagCompound().getCompoundTag("gem"+i));
				if (!gem.isEmpty() && gem.hasTagCompound() && gem.getTagCompound().hasKey("type") && gem.getTagCompound().getString("type").compareTo(source.getDamageType()) == 0) {
					mult += 0.35f;
				}
			}
		}
		return Math.min(1.0f, mult);
	}
}
