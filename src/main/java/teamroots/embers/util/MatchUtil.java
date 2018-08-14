package teamroots.embers.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MatchUtil {
	public static class StateComparator implements Comparator<IBlockState> {
		@Override
		public int compare(IBlockState arg0, IBlockState arg1) {
			return (arg0.getBlock().getRegistryName().toString()+"_"+arg0.getBlock().getMetaFromState(arg0)).compareTo(arg1.getBlock().getRegistryName().toString()+"_"+arg1.getBlock().getMetaFromState(arg1));
		}
	}
	
	public static String getOreKeys(ItemStack stack){
		int[] keys = OreDictionary.getOreIDs(stack);
		String r = "";
		for (int i = 0; i < keys.length; i ++){
			r += OreDictionary.getOreName(keys[i]);
		}
		return r;
	}
	
	public static class StackComparator implements Comparator<ItemStack> {
		@Override
		public int compare(ItemStack arg0, ItemStack arg1) {
			return (getOreKeys(arg0)+"_"+arg0.getItem().getRegistryName().toString()+"_"+arg0.getItemDamage()).compareTo(getOreKeys(arg0)+"_"+arg1.getItem().getRegistryName().toString()+"_"+arg1.getItemDamage());
		}
	}
	
	public static boolean areStacksEqualOreDict(ItemStack stack1, ItemStack stack2){
		if (stack1.isEmpty() && stack2.isEmpty()){
			return true;
		}
		else if (stack1.isEmpty() && !stack2.isEmpty() || stack2.isEmpty() && !stack1.isEmpty()){
			return false;
		}
		else {
			int[] ores1 = OreDictionary.getOreIDs(stack1);
			int[] ores2 = OreDictionary.getOreIDs(stack2);
			for (int i = 0; i < ores1.length; i ++){
				for (int j = 0; j < ores2.length; j ++){
					if (ores1[i] == ores2[j]){
						return true;
					}
				}
			}
		}
		return ItemStack.areItemStacksEqual(stack1, stack2);
	}
	
	public static StateComparator stateComparator = new StateComparator();
	
	public static boolean stateListsMatch(List<IBlockState> list1, List<IBlockState> list2){
		list1.sort(stateComparator);
		list2.sort(stateComparator);
		boolean doMatch = list1.size() == list2.size();
		if (doMatch){
			for (int i = 0; i < list1.size(); i ++){
				if (list1.get(i).getBlock() != list2.get(i).getBlock() || list1.get(i).getBlock().getMetaFromState(list1.get(i)) != list2.get(i).getBlock().getMetaFromState(list2.get(i))){
					doMatch = false;
				}
			}
		}
		return doMatch;
	}
	
	public static StackComparator stackComparator = new StackComparator();
	
	public static class NameComparator implements Comparator<String> {

		@Override
		public int compare(String arg0, String arg1) {
			return arg0.compareTo(arg1);
		}
		
	}
	
	public static NameComparator nameComparator = new NameComparator();
	
	public static boolean stackListsMatch(List<ItemStack> list1, List<ItemStack> list2){
		ArrayList<ItemStack> list1backup = new ArrayList<ItemStack>();
		ArrayList<ItemStack> list2backup = new ArrayList<ItemStack>();
		if (list1.size() == list2.size()){
			for (int i = 0; i < list1.size(); i ++){
				boolean doContinue = true;
				for (int j = 0; j < list2.size() && doContinue; j ++){
					if (areStacksEqualOreDict(list1.get(i),list2.get(j))){
						list1backup.add(list1.get(i).copy());
						list2backup.add(list2.get(j).copy());
						list1.remove(i);
						list2.remove(j);
						i = -1;
						j = -1;
						doContinue = false;
					}
				}
			}
			boolean doMatch = false;
			if (list1.size() == 0 && list2.size() == 0){
				doMatch = true;
			}
			for (ItemStack i : list1backup){
				list1.add(i);
			}
			for (ItemStack i : list2backup){
				list2.add(i);
			}
			return doMatch;
		}
		return false;
	}
}
