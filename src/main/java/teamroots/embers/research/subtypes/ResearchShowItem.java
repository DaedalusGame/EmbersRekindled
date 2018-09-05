package teamroots.embers.research.subtypes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import teamroots.embers.gui.GuiCodex;
import teamroots.embers.research.ResearchBase;
import teamroots.embers.util.RenderUtil;
import teamroots.embers.util.Vec2i;

import java.util.LinkedList;
import java.util.List;

public class ResearchShowItem extends ResearchBase {
    LinkedList<DisplayItem> displayItems = new LinkedList<>();

    public ResearchShowItem(String location, ItemStack icon, double x, double y) {
        super(location, icon, x, y);
    }

    public ResearchShowItem(String location, ItemStack icon, Vec2i pos) {
        this(location,icon,pos.x,pos.y);
    }

    public ResearchShowItem addItem(DisplayItem item) {
        displayItems.add(item);
        return this;
    }

    @Override
    public List<String> getLines(FontRenderer renderer, String s, int width) {
        List<String> lines = super.getLines(renderer, s, width);
        int offset = displayItems.size() * 24;
        for(int i = 0; i < offset; i+=renderer.FONT_HEIGHT+3)
            lines.add(0,"");
        return lines;
    }

    @Override
    public void renderPageContent(GuiCodex gui, int basePosX, int basePosY, FontRenderer fontRenderer) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(getBackground());
        int textOffX = 3;
        int textOffY = 2;
        int y = 0;
        for (DisplayItem displayItem : displayItems) {
            ItemStack[] stacks = displayItem.getStacks();
            int batchOff = displayItem.sideText == null ? (152-stacks.length*24)/2 : 0;
            int slotY = basePosY - textOffY + 43 + y * 24;
            for(int x = 0; x < stacks.length; x++) {
                int slotX = basePosX - textOffX + batchOff + 20;
                gui.drawTexturedModalRect(slotX + x * 24, slotY, 192, 0, 24, 24);
            }
            y++;
        }
        super.renderPageContent(gui, basePosX, basePosY, fontRenderer);
        y = 0;
        for (DisplayItem displayItem : displayItems) {
            ItemStack[] stacks = displayItem.getStacks();
            int batchOff = displayItem.sideText == null ? (152-stacks.length*24)/2 : 0;
            int slotY = basePosY - textOffY + 43 + y * 24;
            for(int x = 0; x < stacks.length; x++) {
                int slotX = basePosX - textOffX + batchOff + 20 + x * 24;
                gui.renderItemStackMinusTooltipAt(stacks[x], slotX + 4, slotY + 4);
            }
            if(displayItem.sideText != null) {
                List<String> strings = fontRenderer.listFormattedStringToWidth(displayItem.getSideText(), 152 - stacks.length * 24);
                int textOff = strings.size() <= 1 ? (fontRenderer.FONT_HEIGHT + 3) / 2 : 0;
                for (int i = 0; i < Math.min(strings.size(), 2); i++)
                    GuiCodex.drawTextGlowing(fontRenderer, strings.get(i), basePosX + 20 + stacks.length * 24, slotY + textOffY + textOff + i * (fontRenderer.FONT_HEIGHT + 3));
            }
            y++;
        }
    }

    public static class DisplayItem {
        public ItemStack[] stacks;
        public String sideText;

        public DisplayItem(ItemStack... stacks) {
            this.stacks = stacks;
        }

        public DisplayItem(String sideText, ItemStack... stacks) {
            this.stacks = stacks;
            this.sideText = sideText;
        }

        public ItemStack[] getStacks() {
            return stacks;
        }

        public String getSideText() {
            return I18n.format("embers.research.image."+sideText);
        }
    }
}
