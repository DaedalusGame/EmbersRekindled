package teamroots.embers.item;

import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;
import teamroots.embers.model.ModelArmorBase;
import teamroots.embers.model.ModelAshenCloak;
import teamroots.embers.model.ModelManager;
import teamroots.embers.util.EmberInventoryUtil;

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
				if (gem != ItemStack.EMPTY){
					if (gem.hasTagCompound()){
						if (gem.getTagCompound().hasKey("type")){
							if (gem.getTagCompound().getString("type").compareTo(source.getDamageType()) == 0){
								mult += 0.15f;
							}
						}
					}
				}
			}
		}
		return Math.min(1.0f, mult);
	}
}
