package teamroots.embers.research;

import com.google.common.collect.Lists;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.ConfigManager;
import teamroots.embers.util.Vec2i;

import java.util.*;

public class ResearchCategory {
	public static final ArrayList<ResearchBase> NO_PREREQUISITES = Lists.newArrayList();

	public String name = "";
	public double u = 192.0;
	public double v = 0;
	public ResourceLocation texture = new ResourceLocation("embers:textures/gui/codex_index.png");
	public ResourceLocation background = new ResourceLocation("embers:textures/gui/codex_category.png");
	public ArrayList<ResearchBase> researches = new ArrayList<>();
	public ArrayList<ResearchBase> prerequisites = new ArrayList<>();
	public LinkedList<Vec2i> goodLocations = new LinkedList<>();
	
	public ResearchCategory(String name, double v){
		this.name = name;
		this.v = v;
	}
	public ResearchCategory(String name, ResourceLocation loc, double u, double v){
		this.name = name;
		this.v = v;
		this.u = u;
		this.texture = loc;
	}
	
	public ResearchCategory addResearch(ResearchBase base){
		researches.add(base);
		return this;
	}

	public ResearchCategory pushGoodLocations(Vec2i... locations){
		Collections.addAll(goodLocations, locations);
		return this;
	}

	public Vec2i popGoodLocation(){
		if(goodLocations.isEmpty())
			return null;
		return goodLocations.removeFirst();
	}

	public void findByTag(String match,Map<ResearchBase,Integer> result, Set<ResearchCategory> categories)
	{
		if(categories.contains(this))
			return;
		categories.add(this);
		for (ResearchBase research : researches) {
			research.findByTag(match,result,categories);
		}
	}

	public ResearchCategory addPrerequisite(ResearchBase base)
	{
		prerequisites.add(base);
		return this;
	}

	public List<ResearchBase> getPrerequisites() {
		if(ConfigManager.codexCategoryIsProgress)
			return prerequisites;
		else
			return NO_PREREQUISITES;
	}

	public boolean isChecked() {
		return getPrerequisites().stream().allMatch(ResearchBase::isChecked);
	}

	@SideOnly(Side.CLIENT)
	public String getName(){
		return I18n.format("embers.research."+name);
	}

	@SideOnly(Side.CLIENT)
	public List<String> getTooltip(boolean showTooltips)
	{
		ArrayList<String> tooltip = new ArrayList<>();
		boolean isChecked = isChecked();
		if(showTooltips || !isChecked)
		for (ResearchBase prerequisite : getPrerequisites()) {
			String checkmark;
			if(prerequisite.isChecked())
				tooltip.add(I18n.format("embers.research.prerequisite.unlocked",prerequisite.getName()));
			else
				tooltip.add(I18n.format("embers.research.prerequisite.locked",prerequisite.getName()));

		}
		return tooltip;
	}

	public double getIconU() {
		return u;
	}

	public double getIconV() {
		return v;
	}

	public ResourceLocation getBackgroundTexture() {
		return background;
	}

	public ResourceLocation getIndexTexture() {
		return texture;
	}

	public void getAllResearch(Set<ResearchBase> result) {
		for (ResearchBase research : researches) {
			research.getAllResearch(result);
		}
	}
}
