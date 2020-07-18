package teamroots.embers.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import net.minecraft.client.resources.I18n;

import javax.annotation.Nonnull;

public class BoilerRecipeCategory extends FluidRecipeCategory {
    public static final String UID = "embers.boiler";
    @Nonnull
    private final String name;

    public BoilerRecipeCategory(IGuiHelper helper) {
        super(helper);
        this.name = I18n.format("embers.jei.recipe.boiler");
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return name;
    }
}
