package teamroots.embers.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import net.minecraft.client.resources.I18n;

import javax.annotation.Nonnull;

public class ReactionChamberCategory extends FluidRecipeCategory {
    public static final String UID = "embers.reaction_chamber";
    @Nonnull
    private final String name;

    public ReactionChamberCategory(IGuiHelper helper) {
        super(helper);
        this.name = I18n.format("embers.jei.recipe.reaction_chamber");
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
