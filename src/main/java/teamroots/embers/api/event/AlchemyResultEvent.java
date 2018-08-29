package teamroots.embers.api.event;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import teamroots.embers.api.alchemy.AlchemyResult;

public class AlchemyResultEvent extends UpgradeEvent {
    AlchemyResult result;
    boolean consumeIngredients;
    boolean isFailure;
    ItemStack failureStack;

    public AlchemyResultEvent(TileEntity tile, AlchemyResult result, boolean consumeIngredients, boolean isFailure, ItemStack failureStack) {
        super(tile);
        this.result = result;
        this.consumeIngredients = consumeIngredients;
        this.isFailure = isFailure;
        this.failureStack = failureStack;
    }

    public boolean shouldConsumeIngredients() {
        return consumeIngredients;
    }

    public void setConsumeIngredients(boolean consumeIngredients) {
        this.consumeIngredients = consumeIngredients;
    }

    public AlchemyResult getResult() {
        return result;
    }

    public void setResult(AlchemyResult result) {
        this.result = result;
    }

    public ItemStack getFailureStack() {
        return failureStack;
    }

    public void setFailureStack(ItemStack failureStack) {
        this.failureStack = failureStack;
    }

    public boolean isFailure() {
        return isFailure;
    }

    public void setFailure(boolean failure) {
        isFailure = failure;
    }
}
