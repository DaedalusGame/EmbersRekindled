package teamroots.embers.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;

public class ItemModUtil {
	public static final String HEAT_TAG = Embers.MODID+":heat_tag";
	
	public static Map<Item, String> modifierRegistry = new HashMap<Item, String>();
	
	public static void init(){
		modifierRegistry.put(RegistryManager.ancient_motive_core, "core");
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
		if (getModifierLevel(stack, modifierRegistry.get(mod.getItem())) == 0){
			NBTTagCompound modifier = new NBTTagCompound();
			modifier.setString("name", modifierRegistry.get(mod.getItem()));
			modifier.setTag("item", mod.writeToNBT(new NBTTagCompound()));
			modifier.setInteger("level", 1);
			list.appendTag(modifier);
		}
		else {
			for (int i = 0; i < list.tagCount(); i ++){
				if (list.getCompoundTagAt(i).getString("name").compareTo(modifierRegistry.get(mod.getItem())) == 0){
					list.getCompoundTagAt(i).setInteger("level", Math.min(5, list.getCompoundTagAt(i).getInteger("level")));
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
}
