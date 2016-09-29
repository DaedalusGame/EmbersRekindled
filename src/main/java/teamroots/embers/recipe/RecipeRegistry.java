package teamroots.embers.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.RegistryManager;
import teamroots.embers.item.EnumStampType;

public class RecipeRegistry {

	public static Map<ItemStack,ItemMeltingRecipe> meltingRecipes = new HashMap<ItemStack,ItemMeltingRecipe>();
	public static Map<String,ItemMeltingOreRecipe> meltingOreRecipes = new HashMap<String,ItemMeltingOreRecipe>();
	
	public static ArrayList<ItemStampingRecipe> stampingRecipes = new ArrayList<ItemStampingRecipe>();
	public static ArrayList<ItemStampingOreRecipe> stampingOreRecipes = new ArrayList<ItemStampingOreRecipe>();
	
	public static ArrayList<FluidMixingRecipe> mixingRecipes = new ArrayList<FluidMixingRecipe>();
	
	public static void init(){
		meltingOreRecipes.put("oreIron", new ItemMeltingOreRecipe("oreIron",new FluidStack(RegistryManager.fluidMoltenIron,288)));
		meltingOreRecipes.put("ingotIron", new ItemMeltingOreRecipe("ingotIron",new FluidStack(RegistryManager.fluidMoltenIron,144)));
		meltingOreRecipes.put("nuggetIron", new ItemMeltingOreRecipe("nuggetIron",new FluidStack(RegistryManager.fluidMoltenIron,16)));

		meltingOreRecipes.put("oreGold", new ItemMeltingOreRecipe("oreGold",new FluidStack(RegistryManager.fluidMoltenGold,288)));
		meltingOreRecipes.put("ingotGold", new ItemMeltingOreRecipe("ingotGold",new FluidStack(RegistryManager.fluidMoltenGold,144)));
		meltingOreRecipes.put("nuggetGold", new ItemMeltingOreRecipe("nuggetGold",new FluidStack(RegistryManager.fluidMoltenGold,16)));

		meltingOreRecipes.put("oreSilver", new ItemMeltingOreRecipe("oreSilver",new FluidStack(RegistryManager.fluidMoltenSilver,288)));
		meltingOreRecipes.put("ingotSilver", new ItemMeltingOreRecipe("ingotSilver",new FluidStack(RegistryManager.fluidMoltenSilver,144)));
		meltingOreRecipes.put("nuggetSilver", new ItemMeltingOreRecipe("nuggetSilver",new FluidStack(RegistryManager.fluidMoltenSilver,16)));

		meltingOreRecipes.put("oreCopper", new ItemMeltingOreRecipe("oreCopper",new FluidStack(RegistryManager.fluidMoltenCopper,288)));
		meltingOreRecipes.put("ingotCopper", new ItemMeltingOreRecipe("ingotCopper",new FluidStack(RegistryManager.fluidMoltenCopper,144)));
		meltingOreRecipes.put("nuggetCopper", new ItemMeltingOreRecipe("nuggetCopper",new FluidStack(RegistryManager.fluidMoltenCopper,16)));

		meltingOreRecipes.put("oreLead", new ItemMeltingOreRecipe("oreLead",new FluidStack(RegistryManager.fluidMoltenLead,288)));
		meltingOreRecipes.put("ingotLead", new ItemMeltingOreRecipe("ingotLead",new FluidStack(RegistryManager.fluidMoltenLead,144)));
		meltingOreRecipes.put("nuggetLead", new ItemMeltingOreRecipe("nuggetLead",new FluidStack(RegistryManager.fluidMoltenLead,16)));
		
		stampingRecipes.add(new ItemStampingRecipe(null,new FluidStack(RegistryManager.fluidMoltenIron,144),EnumStampType.TYPE_BAR,new ItemStack(Items.IRON_INGOT,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(null,new FluidStack(RegistryManager.fluidMoltenGold,144),EnumStampType.TYPE_BAR,new ItemStack(Items.GOLD_INGOT,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(null,new FluidStack(RegistryManager.fluidMoltenLead,144),EnumStampType.TYPE_BAR,new ItemStack(RegistryManager.ingotLead,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(null,new FluidStack(RegistryManager.fluidMoltenSilver,144),EnumStampType.TYPE_BAR,new ItemStack(RegistryManager.ingotSilver,1),false,false));
		stampingRecipes.add(new ItemStampingRecipe(null,new FluidStack(RegistryManager.fluidMoltenCopper,144),EnumStampType.TYPE_BAR,new ItemStack(RegistryManager.ingotCopper,1),false,false));
		
		mixingRecipes.add(new FluidMixingRecipe(new FluidStack[]{new FluidStack(FluidRegistry.LAVA,1000),new FluidStack(RegistryManager.fluidMoltenIron,1000)}, new FluidStack(FluidRegistry.WATER,1000)));
	}
	
	public static ItemStampingRecipe getStampingRecipe(ItemStack stack, FluidStack fluid, EnumStampType type){
		for (int i = 0; i < stampingRecipes.size(); i ++){
			if (stampingRecipes.get(i).matches(stack, fluid, type)){
				return stampingRecipes.get(i);
			}
		}
		return null;
	}
	
	public static ItemStampingOreRecipe getStampingOreRecipe(ItemStack stack, FluidStack fluid, EnumStampType type){
		for (int i = 0; i < stampingOreRecipes.size(); i ++){
			if (stampingOreRecipes.get(i).matches(stack, fluid, type)){
				return stampingOreRecipes.get(i);
			}
		}
		return null;
	}
	
	public static FluidMixingRecipe getMixingRecipe(ArrayList<FluidStack> fluids){
		for (int i = 0; i < mixingRecipes.size(); i ++){
			if (mixingRecipes.get(i).matches(fluids)){
				return mixingRecipes.get(i);
			}
		}
		return null;
	}
}
