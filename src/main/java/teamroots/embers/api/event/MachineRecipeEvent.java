package teamroots.embers.api.event;

import net.minecraft.tileentity.TileEntity;

public class MachineRecipeEvent<T> extends UpgradeEvent {
    T recipe;
    T originalRecipe;

    public MachineRecipeEvent(TileEntity tile, T recipe) {
        super(tile);
        this.recipe = recipe;
        this.originalRecipe = recipe;
    }

    public T getOriginalRecipe() {
        return originalRecipe;
    }

    public T getRecipe() {
        return recipe;
    }

    public void setRecipe(T recipe) {
        this.recipe = recipe;
    }

    public static class Success<T> extends MachineRecipeEvent<T> {
        public Success(TileEntity tile, T recipe) {
            super(tile, recipe);
        }
    }
}
