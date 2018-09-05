package teamroots.embers.research;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.gui.GuiCodex;
import teamroots.embers.util.Vec2i;

import java.util.ArrayList;
import java.util.List;

public class ResearchBase {
	public String name = "";
	public double u = 0.75;
	public double v = 0.0;
	public ItemStack icon = ItemStack.EMPTY;
	public int x = 0;
	public int y = 0;
	public List<ResearchBase> ancestors = new ArrayList<>();
	public ResourceLocation iconBackground = new ResourceLocation("embers:textures/gui/codex_category.png");
	public ResourceLocation background = new ResourceLocation("embers:textures/gui/codex_normal.png");

	public ResearchBase firstPage;
	public int pageNumber;
	List<ResearchBase> pages = new ArrayList<>();
	
	public float selectedAmount = 0;
	public float selectionTarget = 0;
	
	public ResearchBase(String location, ItemStack icon, double x, double y){
		this.name = location;
		this.icon = icon;
		this.x = 48+(int)(x*24);
		this.y = 48+(int)(y*24);
	}

	public ResearchBase(String location, ItemStack icon, Vec2i pos) {
		this(location,icon,pos.x,pos.y);
	}
	
	public ResearchBase addAncestor(ResearchBase base){
		this.ancestors.add(base);
		return this;
	}

	public ResearchBase setIconBackground(ResourceLocation resourceLocation, double u, double v) {
		this.iconBackground = resourceLocation;
		this.u = u;
		this.v = v;
		return this;
	}

	public ResearchBase setBackground(ResourceLocation resourceLocation) {
		this.background = resourceLocation;
		return this;
	}

	public ResearchBase addPage(ResearchBase page) {
		if(firstPage != null)
			return firstPage.addPage(page);
		pages.add(page);
		page.pageNumber = getPageCount();
		page.firstPage = getFirstPage();
		return this;
	}

	public boolean isHidden() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public String getName(){
		return I18n.format("embers.research.page."+name);
	}

	@SideOnly(Side.CLIENT)
	public String getTitle(){
		if(hasMultiplePages())
			return I18n.format("embers.research.multipage",I18n.format("embers.research.page."+getFirstPage().name+".title"),pageNumber+1,getPageCount()+1);
		else
			return I18n.format("embers.research.page."+name+".title");
	}

	@SideOnly(Side.CLIENT)
	public String getText(){
		return I18n.format("embers.research.page."+name+".desc");
	}

	@SideOnly(Side.CLIENT)
	public List<String> getLines(FontRenderer fontRenderer, String s, int width){
		return Lists.newArrayList(fontRenderer.listFormattedStringToWidth(s, width));
	}

	public ResourceLocation getBackground() {
		return background;
	}

	public ResourceLocation getIconBackground() {
		return iconBackground;
	}

	public double getIconBackgroundU() {
		return u;
	}

	public double getIconBackgroundV() {
		return v;
	}

	public ItemStack getIcon() {
		return icon;
	}

	public boolean hasMultiplePages() {
		return getPageCount() > 0;
	}

	public ResearchBase getPage(int i) {
		i = MathHelper.clamp(i,0,getPageCount());
		if(i <= 0)
			return getFirstPage();
		else
			return getPages().get(i-1);
	}

	public ResearchBase getFirstPage() {
		if(firstPage != null)
			return firstPage;
		else
			return this;
	}

	public ResearchBase getNextPage() {
		return getPage(pageNumber+1);
	}

	public ResearchBase getPreviousPage() {
		return getPage(pageNumber-1);
	}

	public int getPageCount() {
		return getPages().size();
	}

	public List<ResearchBase> getPages() {
		if(firstPage != null)
			return firstPage.pages;
		else
			return pages;
	}

	public boolean onOpen(GuiCodex gui) {
		return true;
	}

	public boolean onClose(GuiCodex gui) {
		return true;
	}

	public void renderPageContent(GuiCodex gui, int basePosX, int basePosY, FontRenderer fontRenderer) {
		List<String> strings = getLines(fontRenderer, getText(), 152);
		for (int i = 0; i < Math.min(strings.size(),17); i++){
			GuiCodex.drawTextGlowing(fontRenderer, strings.get(i), basePosX+20, basePosY+43+i*(fontRenderer.FONT_HEIGHT+3));
		}
	}
}
