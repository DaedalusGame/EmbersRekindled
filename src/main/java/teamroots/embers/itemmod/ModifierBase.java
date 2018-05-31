package teamroots.embers.itemmod;

public class ModifierBase {
	public enum EnumType {
		TOOL, ARMOR, ALL, CHESTPLATE
	}
	
	public EnumType type = EnumType.ALL;
	public String name = "";
	public double cost = 0;
	public boolean countTowardsTotalLevel = false;
	
	public ModifierBase(EnumType type, String name, double cost, boolean levelCounts){
		this.type = type;
		this.name = name;
		this.cost = cost;
		this.countTowardsTotalLevel = levelCounts;
	}
}
