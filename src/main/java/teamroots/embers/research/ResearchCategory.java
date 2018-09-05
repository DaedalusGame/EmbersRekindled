package teamroots.embers.research;

import net.minecraft.util.ResourceLocation;
import teamroots.embers.util.Vec2i;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class ResearchCategory {
	public String name = "";
	public double u = 192.0;
	public double v = 0;
	public ResourceLocation texture = new ResourceLocation("embers:textures/gui/codex_index.png");
	public ResourceLocation background = new ResourceLocation("embers:textures/gui/codex_category.png");
	public ArrayList<ResearchBase> researches = new ArrayList<>();
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
