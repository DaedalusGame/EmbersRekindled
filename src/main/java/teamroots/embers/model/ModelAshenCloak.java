package teamroots.embers.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ModelAshenCloak extends ModelArmorBase {
	
	public EntityEquipmentSlot slot;
	
	public ModelAshenCloak(EntityEquipmentSlot slot){
		super(slot);
		this.isChild = false;
		ModelArmorHolder m = (ModelArmorHolder) ModelManager.models.get("ashenCloak");
		this.head = m.head;
		this.armL = m.armL;
		this.armR = m.armR;
		this.chest = m.chest;
		this.legL = m.legL;
		this.legR = m.legR;
		this.legsTop = m.legsTop;
		this.bootL = m.bootL;
		this.bootR = m.bootR;
		this.cape = m.cape;
	    this.armorScale = 1.2f;
	}
}
