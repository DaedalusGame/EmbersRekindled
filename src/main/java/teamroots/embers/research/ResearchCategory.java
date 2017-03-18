package teamroots.embers.research;

import java.util.ArrayList;

public class ResearchCategory {
	public String name = "";
	public double v = 0;
	public ArrayList<ResearchBase> researches = new ArrayList<ResearchBase>();
	
	public ResearchCategory(String name, double v){
		this.name = name;
		this.v = v;
	}
	
	public ResearchCategory addResearch(ResearchBase base){
		researches.add(base);
		return this;
	}
}
