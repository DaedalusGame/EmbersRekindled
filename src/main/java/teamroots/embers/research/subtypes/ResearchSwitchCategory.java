package teamroots.embers.research.subtypes;

import net.minecraft.item.ItemStack;
import teamroots.embers.SoundManager;
import teamroots.embers.gui.GuiCodex;
import teamroots.embers.research.ResearchBase;
import teamroots.embers.research.ResearchCategory;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public void getAllResearch(Set<ResearchBase> result) {
        if(result.contains(this))
            return;
        targetCategory.getAllResearch(result);
    }

    @Override
    public void findByTag(String match, Map<ResearchBase, Integer> result, Set<ResearchCategory> categories) {
        int startResults = result.size();
        targetCategory.findByTag(match,result,categories);
        if(startResults != result.size())
        {
            int categoryScore = result.entrySet().stream().filter(entry -> targetCategory.researches.contains(entry.getKey())).mapToInt(Map.Entry::getValue).max().orElse(0);
            result.put(this,categoryScore);
        }
    }

    @Override
    public boolean onOpen(GuiCodex gui) {
        gui.pushLastCategory(gui.researchCategory);
        gui.researchCategory = targetCategory;
        gui.playSound(SoundManager.CODEX_CATEGORY_SWITCH);
        return false;
    }

    @Override
    public void check(boolean checked) {
        //NOOP
    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public boolean isHidden() {
        return targetCategory.researches.size() < minEntries;
    }
}
