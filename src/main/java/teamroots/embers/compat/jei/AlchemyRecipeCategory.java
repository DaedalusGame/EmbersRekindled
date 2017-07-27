package teamroots.embers.compat.jei;

import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.Embers;
import teamroots.embers.recipe.AlchemyRecipe;
import teamroots.embers.util.RenderUtil;

public class AlchemyRecipeCategory extends BlankRecipeCategory {
    private final IDrawable background, ashBar, ashPartialBar;
    private final String name;
    private IGuiHelper helper = null;
    private AlchemyRecipe lastRecipe = null;
    
    public static ResourceLocation texture = new ResourceLocation("embers:textures/gui/jei_alchemy.png");

    public AlchemyRecipeCategory(IGuiHelper helper){
    	this.helper = helper;
        this.background = helper.createDrawable(texture, 0, 0, 108, 132);
        this.ashBar = helper.createDrawable(texture, 109, 1, 52, 8);
        this.ashPartialBar = helper.createDrawable(texture, 109, 10, 52, 8);
        this.name = I18n.format("embers.jei.recipe.alchemy");
    }

    @Override
    public String getTitle()
    {
        return name;
    }

    @Override
    public IDrawable getBackground(){
        return background;
    }

	@Override
	public String getUid() {
		return "embers.alchemy";
	}

	@Override
	public void setRecipe(IRecipeLayout layout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup stacks = layout.getItemStacks();
        
        stacks.init(0, true, 27, 18);
        stacks.init(1, true, 9, 18);
        stacks.init(2, true, 27, 0);
        stacks.init(3, true, 45, 18);
        stacks.init(4, true, 27, 36);
        stacks.init(5, true, 81, 18);
        
        for (int i = 0; i < 5; i ++){
        	if (ingredients.getInputs(ItemStack.class).size() > i){
        		if (ingredients.getInputs(ItemStack.class).get(i) != null){
        			stacks.set(i, ingredients.getInputs(ItemStack.class).get(i));
        		}
        	}
        }
        if (ingredients.getOutputs(ItemStack.class).size() > 0){
        	stacks.set(5, ingredients.getOutputs(ItemStack.class).get(0));
        }
        
        if (recipeWrapper instanceof AlchemyRecipeWrapper){
        	lastRecipe = ((AlchemyRecipeWrapper)recipeWrapper).recipe;
        }
	}
	
	@Override
	public void drawExtras(Minecraft minecraft){
		super.drawExtras(minecraft);
		int aspectTotal = lastRecipe.ironAspectMin+lastRecipe.ironAspectRange
				+lastRecipe.copperAspectMin+lastRecipe.copperAspectRange
				+lastRecipe.silverAspectMin+lastRecipe.silverAspectRange
				+lastRecipe.leadAspectMin+lastRecipe.leadAspectRange
				+lastRecipe.dawnstoneAspectMin+lastRecipe.dawnstoneAspectRange;
		if (this.lastRecipe.ironAspectMin+this.lastRecipe.ironAspectRange > 0){
			IDrawable ashBar = helper.createDrawable(texture, 109, 1, ((52*lastRecipe.ironAspectMin)/aspectTotal), 8);
			IDrawable ashPartialBar = helper.createDrawable(texture, 109, 11, ((52*(lastRecipe.ironAspectMin+lastRecipe.ironAspectRange))/aspectTotal), 8);
			ashPartialBar.draw(minecraft, 17, 61);
			ashBar.draw(minecraft, 17, 61);
			Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(""+lastRecipe.ironAspectMin+"-"+(lastRecipe.ironAspectMin+lastRecipe.ironAspectRange), 75, 61, 0xFFFFFF);
		}
		if (this.lastRecipe.copperAspectMin+this.lastRecipe.copperAspectRange > 0){
			IDrawable ashBar = helper.createDrawable(texture, 109, 1, ((52*lastRecipe.copperAspectMin)/aspectTotal), 8);
			IDrawable ashPartialBar = helper.createDrawable(texture, 109, 11, ((52*(lastRecipe.copperAspectMin+lastRecipe.copperAspectRange))/aspectTotal), 8);
			ashPartialBar.draw(minecraft, 17, 76);
			ashBar.draw(minecraft, 17, 76);
			Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(""+lastRecipe.copperAspectMin+"-"+(lastRecipe.copperAspectMin+lastRecipe.copperAspectRange), 75, 76, 0xFFFFFF);
		}
		if (this.lastRecipe.dawnstoneAspectMin+this.lastRecipe.dawnstoneAspectRange > 0){
			IDrawable ashBar = helper.createDrawable(texture, 109, 1, ((52*lastRecipe.dawnstoneAspectMin)/aspectTotal), 8);
			IDrawable ashPartialBar = helper.createDrawable(texture, 109, 11, ((52*(lastRecipe.dawnstoneAspectMin+lastRecipe.dawnstoneAspectRange))/aspectTotal), 8);
			ashPartialBar.draw(minecraft, 17, 91);
			ashBar.draw(minecraft, 17, 91);
			Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(""+lastRecipe.dawnstoneAspectMin+"-"+(lastRecipe.dawnstoneAspectMin+lastRecipe.dawnstoneAspectRange), 75, 91, 0xFFFFFF);
		}
		if (this.lastRecipe.leadAspectMin+this.lastRecipe.leadAspectRange > 0){
			IDrawable ashBar = helper.createDrawable(texture, 109, 1, ((52*lastRecipe.leadAspectMin)/aspectTotal), 8);
			IDrawable ashPartialBar = helper.createDrawable(texture, 109, 11, ((52*(lastRecipe.leadAspectMin+lastRecipe.leadAspectRange))/aspectTotal), 8);
			ashPartialBar.draw(minecraft, 17, 106);
			ashBar.draw(minecraft, 17, 106);
			Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(""+lastRecipe.leadAspectMin+"-"+(lastRecipe.leadAspectMin+lastRecipe.leadAspectRange), 75, 106, 0xFFFFFF);
		}
		if (this.lastRecipe.silverAspectMin+this.lastRecipe.silverAspectRange > 0){
			IDrawable ashBar = helper.createDrawable(texture, 109, 1, ((52*lastRecipe.silverAspectMin)/aspectTotal), 8);
			IDrawable ashPartialBar = helper.createDrawable(texture, 109, 11, ((52*(lastRecipe.silverAspectMin+lastRecipe.silverAspectRange))/aspectTotal), 8);
			ashPartialBar.draw(minecraft, 17, 121);
			ashBar.draw(minecraft, 17, 121);
			Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(""+lastRecipe.silverAspectMin+"-"+(lastRecipe.silverAspectMin+lastRecipe.silverAspectRange), 75, 121, 0xFFFFFF);
		}
	}

	@Override
	public String getModName() {
		return Embers.MODID;
	}
}
