package teamroots.embers.research;

import java.util.ArrayList;

import net.minecraft.util.ResourceLocation;

public class ResearchCategory {
	public String name = "";
	public double u = 192.0;
	public double v = 0;
	public ResourceLocation texture = new ResourceLocation("embers:textures/gui/codex_index.png");
	public ResourceLocation background = new ResourceLocation("embers:textures/gui/codex_category.png");
	public ArrayList<ResearchBase> researches = new ArrayList<ResearchBase>();
	
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
}
