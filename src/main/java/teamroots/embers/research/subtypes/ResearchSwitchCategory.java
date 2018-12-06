package teamroots.embers.research.subtypes;

import net.minecraft.item.ItemStack;
import teamroots.embers.SoundManager;
import teamroots.embers.gui.GuiCodex;
import teamroots.embers.research.ResearchBase;
import teamroots.embers.research.ResearchCategory;

public class ResearchSwitchCategory extends ResearchBase {
    ResearchCategory targetCategory;
    int minEntries;

    public ResearchSwitchCategory(String location, ItemStack icon, double x, double y) {
        super(location, icon, x, y);
    }

    public ResearchSwitchCategory setTargetCategory(ResearchCategory category) {
        this.targetCategory = category;
        return this;
    }

    public ResearchSwitchCategory setMinEntries(int entries) {
        this.minEntries = entries;
        return this;
    }

    @Override
    public boolean onOpen(GuiCodex gui) {
        gui.pushLastCategory(gui.researchCategory);
        gui.researchCategory = targetCategory;
        gui.playSound(SoundManager.CODEX_CATEGORY_SWITCH);
        return false;
    }

    @Override
    public boolean isHidden() {
        return targetCategory.researches.size() < minEntries;
    }
}
