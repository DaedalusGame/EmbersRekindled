package teamroots.embers.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.item.ItemAlchemicWaste;
import teamroots.embers.util.AlchemyResult;
import teamroots.embers.util.AspectList;
import teamroots.embers.util.AspectList.AspectRangeList;
import teamroots.embers.util.MatchUtil;

public class AlchemyRecipe {
	//Binary compat
	@Deprecated
	public int ironAspectMin = 0, dawnstoneAspectMin = 0, copperAspectMin = 0, silverAspectMin = 0, leadAspectMin = 0;
	@Deprecated
	public int ironAspectRange = 0, dawnstoneAspectRange = 0, copperAspectRange = 0, silverAspectRange = 0, leadAspectRange = 0;
	@Deprecated
	public List<ItemStack> inputs = new ArrayList<>();
	@Deprecated
	public ItemStack centerInput = ItemStack.EMPTY;

	public Ingredient centerIngredient;
	public List<Ingredient> outsideIngredients;
	public AspectRangeList aspectRange;
	public ItemStack result = ItemStack.EMPTY;

	Random random = new Random();

	public AlchemyRecipe(AspectRangeList range, Ingredient center, List<Ingredient> outside, ItemStack result) {
		this.result = result;
		this.centerIngredient = center;
		this.outsideIngredients = outside;
		this.aspectRange = range;
	}

	public AlchemyRecipe(int ironMin, int ironMax, int dawnstoneMin, int dawnstoneMax, int copperMin, int copperMax, int silverMin, int silverMax, int leadMin, int leadMax, ItemStack center, ItemStack east, ItemStack west, ItemStack north, ItemStack south, ItemStack result){
		new AspectRangeList(
				AspectList.createStandard(ironMin,dawnstoneMin,copperMin,silverMin,leadMin),
				AspectList.createStandard(ironMax,dawnstoneMax,copperMax,silverMax,leadMax)
		);
		centerIngredient = Ingredient.fromStacks(center);
		outsideIngredients = new ArrayList<>();
		this.result = result;
	}
	
	public int getIron(World world){
		random = new Random(world.getSeed());
		return this.ironAspectMin+random.nextInt(this.ironAspectRange+1);
	}
	
	public int getDawnstone(World world){
		random = new Random(world.getSeed());
		return this.dawnstoneAspectMin+random.nextInt(this.dawnstoneAspectRange+1);
	}
	
	public int getCopper(World world){
		random = new Random(world.getSeed());
		return this.copperAspectMin+random.nextInt(this.copperAspectRange+1);
	}
	
	public int getSilver(World world){
		random = new Random(world.getSeed());
		return this.silverAspectMin+random.nextInt(this.silverAspectRange+1);
	}
	
	public int getLead(World world){
		random = new Random(world.getSeed());
		return this.leadAspectMin+random.nextInt(this.leadAspectRange+1);
	}

	public AlchemyResult matchAshes(AspectList list, World world) {
		return AlchemyResult.create(list, aspectRange, world);
	}

	public ItemStack getResult(TileEntity tile, AspectList aspects) {
		World world = tile.getWorld();
		AlchemyResult result = matchAshes(aspects, world);
		if (result.getAccuracy() == 1.0)
			return this.result.copy();
		else
			return result.createFailure();
	}

	public boolean matches(ItemStack center, List<ItemStack> test) {
		if (!centerIngredient.apply(center))
			return false;

		ArrayList<Ingredient> ingredients = new ArrayList<>(outsideIngredients);
		while (test.size() > ingredients.size()) {
			ingredients.add(Ingredient.EMPTY);
		}
		for (ItemStack stack : test) {
			Optional<Ingredient> found = ingredients.stream().filter(x -> x.apply(stack)).findFirst();
			if (found.isPresent())
				ingredients.remove(found.get());
			else
				return false;
		}

		return true;
	}


	public ItemStack getResult(World world, int iron, int dawnstone, int copper, int silver, int lead){
		double dIron = Math.abs((double)(iron-getIron(world)));
		double dDawnstone = Math.abs((double)(dawnstone-getDawnstone(world)));
		double dCopper = Math.abs((double)(copper-getCopper(world)));
		double dSilver = Math.abs((double)(silver-getSilver(world)));
		double dLead = Math.abs((double)(lead-getLead(world)));
		double accuracy = Math.max(0, 1.0-(dIron+dDawnstone+dCopper+dSilver+dLead)/(double)(getIron(world)+getDawnstone(world)+getCopper(world)+getSilver(world)+getLead(world)));
		accuracy = Math.round(accuracy*100.0)/100.0;
		if (accuracy == 1.0){
			return this.result;
		}
		return ItemAlchemicWaste.create((int)dIron, (int)dCopper, (int)dSilver, (int)dDawnstone, (int)dLead, iron+dawnstone+copper+silver+lead);
	}
}
