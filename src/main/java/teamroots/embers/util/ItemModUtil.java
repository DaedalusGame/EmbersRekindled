package teamroots.embers.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import teamroots.embers.api.itemmod.IItemModUtil;
import teamroots.embers.api.itemmod.ModifierBase;
import teamroots.embers.itemmod.ModifierCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: Phase out
@Deprecated
public class ItemModUtil {
	public static final String HEAT_TAG = IItemModUtil.HEAT_TAG;

	//Ingredient? Not really as critical as the other ones.
	//TODO: Move these to registry
	public static Map<Item, ModifierBase> modifierRegistry = new HashMap<>();
	public static Map<String, ModifierBase> nameToModifier = new HashMap<>();

	public static ArrayList<ModifierBase> weaponModifiers = new ArrayList<>();
	public static ArrayList<ModifierBase> armorModifiers = new ArrayList<>();
	public static ArrayList<ModifierBase> otherModifiers = new ArrayList<>();

	@Deprecated
	public static void init(){
	}

	public static void registerModifier(Item item, ModifierBase modifier) {
		modifierRegistry.put(item, modifier);
		nameToModifier.put(modifier.name, modifier);

		if (!(modifier instanceof ModifierCore))
		switch (modifier.type) { //Create smaller classes for faster lookup
			case ALL:
			case PROJECTILE:
				otherModifiers.add(modifier);
				break;
			case TOOL_OR_ARMOR:
				weaponModifiers.add(modifier);
				armorModifiers.add(modifier);
				break;
			case TOOL:
				weaponModifiers.add(modifier);
				break;
			case ARMOR:
			case HELMET:
			case CHESTPLATE:
			case LEGGINGS:
			case BOOTS:
				armorModifiers.add(modifier);
				break;
		}
	}

	public static boolean canAnyModifierApply(ItemStack stack) {
		Item item = stack.getItem();
		List<ModifierBase> testList = otherModifiers;
		if(item instanceof ItemTool || item instanceof ItemSword || item instanceof ItemHoe)
			testList = weaponModifiers;
		else if(item instanceof ItemArmor)
			testList = armorModifiers;

		for(ModifierBase modifier : testList) {
			if(modifier.canApplyTo(stack))
				return true;
		}

		return false;
	}
	
	public static void checkForTag(ItemStack stack){
		if (!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound());
		}
		if (!stack.getTagCompound().hasKey(HEAT_TAG)){
			stack.getTagCompound().setTag(HEAT_TAG, new NBTTagCompound());
			stack.getTagCompound().getCompoundTag(HEAT_TAG).setInteger("heat_level", 0);
			stack.getTagCompound().getCompoundTag(HEAT_TAG).setFloat("heat", 0);
			stack.getTagCompound().getCompoundTag(HEAT_TAG).setTag("modifiers", new NBTTagList());
		}
	}
	
	public static boolean isModValid(ItemStack stack, ItemStack mod){
		ModifierBase b = modifierRegistry.get(mod.getItem());

		return b.canApplyTo(stack);
	}
	
	public static boolean hasModifier(ItemStack stack, String name){
		if (hasHeat(stack)){
			NBTTagList list = stack.getTagCompound().getCompoundTag(HEAT_TAG).getTagList("modifiers", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); i ++){
				NBTTagCompound compound = list.getCompoundTagAt(i);
				if (compound.hasKey("name")){
					if (compound.getString("name").compareTo(name) == 0){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static void addModifier(ItemStack stack, ItemStack mod){
		checkForTag(stack);
		NBTTagList list = stack.getTagCompound().getCompoundTag(HEAT_TAG).getTagList("modifiers", Constants.NBT.TAG_COMPOUND);
		ModifierBase modifier = modifierRegistry.get(mod.getItem());
		if (getModifierLevel(stack, modifier.name) == 0){
			NBTTagCompound modifierCompound = new NBTTagCompound();
			modifierCompound.setString("name", modifier.name);
			modifierCompound.setTag("item", mod.writeToNBT(new NBTTagCompound()));
			modifierCompound.setInteger("level", 1);
			list.appendTag(modifierCompound);
		}
		else {
			incModifierLevel(stack, modifier.name);
		}
		modifier.onApply(stack);
	}
	
	public static int incModifierLevel(ItemStack stack, String name){
		if (hasHeat(stack)){
			NBTTagList list = stack.getTagCompound().getCompoundTag(HEAT_TAG).getTagList("modifiers", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); i ++){
				NBTTagCompound compound = list.getCompoundTagAt(i);
				if (compound.hasKey("name")){
					if (compound.getString("name").compareTo(name) == 0){
						compound.setInteger("level", compound.getInteger("level")+1);
					}
				}
			}
		}
		return 0;
	}
	
	public static int getTotalModLevel(ItemStack stack){
		int total = 0;
		if (hasHeat(stack)){
			NBTTagList list = stack.getTagCompound().getCompoundTag(HEAT_TAG).getTagList("modifiers", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); i ++){
				NBTTagCompound compound = list.getCompoundTagAt(i);
				ItemStack s = new ItemStack(compound.getCompoundTag("item"));
				if (modifierRegistry.get(s.getItem()).countTowardsTotalLevel){
					total += compound.getInteger("level");
				}
			}
		}
		return total;
	}
	
	public static void setModifierLevel(ItemStack stack, String name, int level){
		if (hasHeat(stack)){
			NBTTagList list = stack.getTagCompound().getCompoundTag(HEAT_TAG).getTagList("modifiers", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); i ++){
				NBTTagCompound compound = list.getCompoundTagAt(i);
				if (compound.hasKey("name")){
					if (compound.getString("name").compareTo(name) == 0){
						compound.setInteger("level", level);
					}
				}
			}
		}
	}
	
	public static int getModifierLevel(ItemStack stack, String name){
		if (hasHeat(stack)){
			NBTTagList list = stack.getTagCompound().getCompoundTag(HEAT_TAG).getTagList("modifiers", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); i ++){
				NBTTagCompound compound = list.getCompoundTagAt(i);
				if (compound.hasKey("name")){
					if (compound.getString("name").compareTo(name) == 0){
						return compound.getInteger("level");
					}
				}
			}
		}
		return 0;
	}
	
	public static float getMaxHeat(ItemStack stack){
		if (stack.hasTagCompound()){
			if (stack.getTagCompound().hasKey(HEAT_TAG)){
				return 500f + 250f*stack.getTagCompound().getCompoundTag(HEAT_TAG).getFloat("heat_level");
			}
		}
		return 0.0f;
	}
	
	public static float getHeat(ItemStack stack){
		if (stack.hasTagCompound()){
			if (stack.getTagCompound().hasKey(HEAT_TAG)){
				return stack.getTagCompound().getCompoundTag(HEAT_TAG).getFloat("heat");
			}
		}
		return 0.0f;
	}
	
	public static int getLevel(ItemStack stack){
		checkForTag(stack);
		return stack.getTagCompound().getCompoundTag(HEAT_TAG).getInteger("heat_level");
	}
	
	public static void setLevel(ItemStack stack, int level){
		checkForTag(stack);
		stack.getTagCompound().getCompoundTag(HEAT_TAG).setInteger("heat_level",level);
	}
	
	public static void addHeat(ItemStack stack, float heat){
		checkForTag(stack);
		stack.getTagCompound().getCompoundTag(HEAT_TAG).setFloat("heat", Math.min(getMaxHeat(stack), getHeat(stack)+heat));
	}
	
	public static void setHeat(ItemStack stack, float heat){
		checkForTag(stack);
		stack.getTagCompound().getCompoundTag(HEAT_TAG).setFloat("heat", heat);
	}
	
	public static boolean hasHeat(ItemStack stack){
		if (!stack.isEmpty()){
			if (stack.hasTagCompound()){
				if (stack.getTagCompound().hasKey(HEAT_TAG)){
					return true;
				}
			}
		}
		return false;
	}
	
	public static int getArmorMod(EntityPlayer p, String name){
		int maxLevel = 0;
		if (hasHeat(p.getItemStackFromSlot(EntityEquipmentSlot.HEAD))){
			int l = getModifierLevel(p.getItemStackFromSlot(EntityEquipmentSlot.HEAD),name);
			if (l > maxLevel){
				maxLevel = l;
			}
		}
		if (hasHeat(p.getItemStackFromSlot(EntityEquipmentSlot.CHEST))){
			int l = getModifierLevel(p.getItemStackFromSlot(EntityEquipmentSlot.CHEST),name);
			if (l > maxLevel){
				maxLevel = l;
			}
		}
		if (hasHeat(p.getItemStackFromSlot(EntityEquipmentSlot.LEGS))){
			int l = getModifierLevel(p.getItemStackFromSlot(EntityEquipmentSlot.LEGS),name);
			if (l > maxLevel){
				maxLevel = l;
			}
		}
		if (hasHeat(p.getItemStackFromSlot(EntityEquipmentSlot.FEET))){
			int l = getModifierLevel(p.getItemStackFromSlot(EntityEquipmentSlot.FEET),name);
			if (l > maxLevel){
				maxLevel = l;
			}
		}
		return maxLevel;
	}
}
