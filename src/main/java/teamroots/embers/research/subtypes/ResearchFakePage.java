package teamroots.embers.research.subtypes;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import teamroots.embers.SoundManager;
import teamroots.embers.gui.GuiCodex;
import teamroots.embers.research.ResearchBase;
import teamroots.embers.util.Vec2i;

public class ResearchFakePage extends ResearchBase {
    ResearchBase targetPage;

    public ResearchFakePage(ResearchBase page, double x, double y) {
        super(page.name, ItemStack.EMPTY, x, y);
        targetPage = page;
    }

    public ResearchFakePage(ResearchBase page, Vec2i pos) {
        this(page,pos.x,pos.y);
    }

    @Override
    public String getName() {
        return targetPage.getName();
    }

    @Override
    public String getTitle() {
        return targetPage.getTitle();
    }

    @Override
    public ItemStack getIcon() {
        return targetPage.getIcon();
    }

    @Override
    public ResourceLocation getIconBackground() {
        return targetPage.getIconBackground();
    }

    @Override
    public double getIconBackgroundU() {
        return targetPage.getIconBackgroundU();
    }

    @Override
    public double getIconBackgroundV() {
        return targetPage.getIconBackgroundV();
    }

    @Override
    public boolean onOpen(GuiCodex gui) {
        gui.researchPage = targetPage;
        gui.playSound(SoundManager.CODEX_PAGE_OPEN);
        return false;
    }
}
