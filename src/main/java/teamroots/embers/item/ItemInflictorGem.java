package teamroots.embers.item;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import teamroots.embers.SoundManager;
import teamroots.embers.api.item.IInflictorGem;

import javax.annotation.Nullable;
import java.util.List;

public class ItemInflictorGem extends ItemBase implements IInflictorGem {
	public ItemInflictorGem() {
		super("inflictor_gem",true);
		this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
		ItemStack stack = player.getHeldItem(hand);
		if (player.isSneaking() && stack.getItemDamage() == 1){
			stack.setItemDamage(0);
			stack.getTagCompound().removeTag("type");
			if(player.getHealth() > 1f)
				player.setHealth(Math.max(player.getHealth()-10.0f,1f));
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}
		return new ActionResult<>(EnumActionResult.FAIL, stack);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected){
		if (!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound());
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced){
		if (stack.hasTagCompound()){
			if (stack.getTagCompound().hasKey("type")){
				tooltip.add(I18n.format("embers.tooltip.inflictor")+stack.getTagCompound().getString("type"));
			}
		}
	}
	
	@Override
	public void initModel(){
		ModelBakery.registerItemVariants(this,
				new ModelResourceLocation(getRegistryName().toString()+"_empty"),
				new ModelResourceLocation(getRegistryName().toString()+"_full"));
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName().toString()+"_empty"));
		ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation(getRegistryName().toString()+"_full"));
	}

	@Override
	public void attuneSource(ItemStack stack, @Nullable EntityLivingBase entity, DamageSource source) {
		String damageType = source.damageType;
		if (damageType.compareTo("mob") != 0 && damageType.compareTo("generic") != 0 && damageType.compareTo("player") != 0 && damageType.compareTo("arrow") != 0) {
			if(stack.hasTagCompound()) {
				stack.setItemDamage(1);
				stack.getTagCompound().setString("type", damageType);
				if (entity != null)
					entity.getEntityWorld().playSound(null, entity.posX, entity.posY, entity.posZ, SoundManager.INFLICTOR_GEM, SoundCategory.PLAYERS, 1.0f, 1.0f);
			}
		}
	}

	@Override
	public String getAttunedSource(ItemStack stack) {
		if(!stack.isEmpty() && stack.hasTagCompound() &&  stack.getTagCompound().hasKey("type"))
			return stack.getTagCompound().getString("type");
		return null;
	}

	@Override
	public float getDamageResistance(ItemStack stack, float modifier) {
		return 0.35f;
	}
}
