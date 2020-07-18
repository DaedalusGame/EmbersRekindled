package teamroots.embers.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import net.minecraft.client.resources.I18n;

import javax.annotation.Nonnull;

public class EngineRecipeCategory extends FluidRecipeCategory {
    public static final String UID = "embers.steam_engine";
    @Nonnull
    private final String name;

    public EngineRecipeCategory(IGuiHelper helper) {
        super(helper);
        this.name = I18n.format("embers.jei.recipe.steam_engine");
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
