package teamroots.embers.research;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ResearchBase {
	public String name = "";
	public double v = 0;
	public ItemStack icon = ItemStack.EMPTY;
	public int x = 0;
	public int y = 0;
	public List<ResearchBase> ancestors = new ArrayList<ResearchBase>();
	
	public float selectedAmount = 0;
	public float selectionTarget = 0;
	
	public ResearchBase(String location, ItemStack icon, int x, int y){
		this.name = location;
		this.icon = icon;
		this.x = 48+x*24;
		this.y = 48+y*24;
	}
	
	public ResearchBase addAncestor(ResearchBase base){
		this.ancestors.add(base);
		return this;
	}
	
	@SideOnly(Side.CLIENT)
	public String getTitle(){
		return I18n.format("embers.research."+name+".title");
	}
	
	@SideOnly(Side.CLIENT)
	public static ArrayList<String> getLines(String s){
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> words = new ArrayList<String>();
		String temp = "";
		int counter = 0;
		for (int i = 0; i < s.length(); i ++){
			temp += s.charAt(i);
			if (s.charAt(i) == ' '){
				words.add(temp);
				temp = "";
			}
		}
		words.add(temp);
		temp = "";
		for (int i = 0; i < words.size(); i ++){
			counter += Minecraft.getMinecraft().fontRendererObj.getStringWidth(words.get(i));
			if (counter > 152){
				list.add(temp);
				temp = words.get(i);
				counter = Minecraft.getMinecraft().fontRendererObj.getStringWidth(words.get(i));
			}
			else {
				temp += words.get(i);
			}
		}
		list.add(temp);
		return list;
	}
}
