package teamroots.embers.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;
import teamroots.embers.compat.jei.wrapper.DawnstoneAnvilWrapper;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class DawnstoneAnvilCategory implements IRecipeCategory<DawnstoneAnvilWrapper> {
    public static final int WIDTH = 109;
    public static final int HEIGHT = 57;
    public static final String UID = "embers.dawnstone_anvil";
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String name;
    private final ResourceLocation texture = new ResourceLocation(Embers.MODID, "textures/gui/jei_dawnstone_anvil.png");

    public DawnstoneAnvilCategory(IGuiHelper helper)
    {
        background = helper.createDrawable(texture, 0, 0, WIDTH, HEIGHT);
        this.name = I18n.format("embers.jei.recipe.dawnstone_anvil");
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getModName() {
        return Embers.MODID;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, DawnstoneAnvilWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0,true, 21, 18);
        guiItemStacks.init(1,true, 21, 36);
        guiItemStacks.init(2,false, 76, 27);
        guiItemStacks.init(3,false, 21, 0); //The hammer

        IFocus focus = recipeLayout.getFocus();
        boolean isFocused = recipeWrapper.isFocusRecipe() && focus != null && focus.getValue() instanceof ItemStack;

        guiItemStacks.setOverrideDisplayFocus(null);
        guiItemStacks.set(0,isFocused ? recipeWrapper.getFocusRecipe().getInputs(focus,0) : ingredients.getInputs(ItemStack.class).get(0));
        guiItemStacks.set(1,isFocused ? recipeWrapper.getFocusRecipe().getInputs(focus,1) : ingredients.getInputs(ItemStack.class).get(1));
        guiItemStacks.set(2,isFocused ? recipeWrapper.getFocusRecipe().getOutputs(focus,2) : ingredients.getOutputs(ItemStack.class).get(0));
        guiItemStacks.set(3, Arrays.asList(new ItemStack(RegistryManager.tinker_hammer),new ItemStack(RegistryManager.auto_hammer)));
    }
}
