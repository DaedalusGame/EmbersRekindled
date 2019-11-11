package teamroots.embers.research;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.ConfigManager;
import teamroots.embers.gui.GuiCodex;
import teamroots.embers.util.Vec2i;

import java.util.*;

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

	public float shownAmount = 0;
	public float shownTarget = 0;

	public boolean checked;
	
	public ResearchBase(String location, ItemStack icon, double x, double y){
		this.name = location;
		this.icon = icon;
		this.x = 48+(int)(x*24);
		this.y = 48+(int)(y*24);
	}

	public ResearchBase(String location, ItemStack icon, Vec2i pos) {
		this(location,icon,pos.x,pos.y);
	}

	public List<ResearchCategory> getNeededFor() {
		ArrayList<ResearchCategory> neededFor = new ArrayList<>();
		if(ConfigManager.codexCategoryIsProgress)
		for (ResearchCategory category : ResearchManager.researches) {
			if(category.prerequisites.contains(this))
				neededFor.add(category);
		}
		return neededFor;
	}

	public void findByTag(String match,Map<ResearchBase,Integer> result, Set<ResearchCategory> categories)
	{
		if(result.containsKey(this))
			return;
		String[] matchParts = match.split("\\|");
		int totalScore = 0;

		for (String matchPart : matchParts)
			if(!matchPart.isEmpty()) {
				int tagScore = matchTags(matchPart);
				int nameScore = scoreMatches(getName(),matchPart);
				int textScore = scoreMatches(getText(),matchPart);
				int score = textScore + tagScore * 100 + nameScore * 1000;
				if(score <= 0)
					return;
				totalScore += score;
			}

		if(totalScore > 0)
			result.put(this, totalScore);
	}

	public int matchTags(String match)
	{
		int score = 0;
		int matches = 0;
		for (String tag : getTags()) {
			int matchScore = scoreMatches(tag,match);
			if(matchScore > 0)
				matches++;
			score += matchScore;
		}
		return score + matches * 100;
	}

	private int scoreMatches(String tag, String match)
	{

			tag = tag.toLowerCase();
			match = match.toLowerCase();
			int matches = 0;
			int positionalScore = 0;
			int index = 0;
			do {
				index = tag.indexOf(match, index);
				if (index >= 0) {
					matches++;
					positionalScore += tag.length() - index;
					index++;
				}
			} while (index >= 0);
			return matches * 10 + positionalScore;

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

	public void check(boolean checked) {
		this.checked = checked;
	}

	public boolean isChecked() {
		return checked;
	}

	public boolean areAncestorsChecked() {
		if(ConfigManager.codexEntryIsProgress)
			return isChecked() || ancestors.stream().allMatch(ResearchBase::isChecked)/*ancestors.isEmpty() || ancestors.stream().anyMatch(ResearchBase::isChecked)*/;
		else
			return true;
	}

	@SideOnly(Side.CLIENT)
	public List<String> getTooltip(boolean showTooltips)
	{
		ArrayList<String> tooltip = new ArrayList<>();
		if(showTooltips || !isChecked()) {
			for (ResearchCategory neededFor : getNeededFor()) {
				tooltip.add(I18n.format("embers.research.prerequisite",neededFor.getName()));
			}
		}
		return tooltip;
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
	private String[] getTags() {
		String translateKey = "embers.research.page." + name + ".tags";
		if(I18n.hasKey(translateKey)) {
			return I18n.format(translateKey).split(";");
		} else {
			return new String[0];
		}
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

	public void getAllResearch(Set<ResearchBase> result) {
		if(result.contains(this))
			return;
		result.add(this);
	}

	public boolean isPathTowards(ResearchBase target) {
		return this == target;
	}
}
