package teamroots.embers.research;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ResearchBase {
	public String name = "";
	public String title = "";
	public ItemStack icon = ItemStack.EMPTY;
	public ResearchBase(String location, ItemStack icon){
		this.name = location;
		this.icon = icon;
	}
	
	@SideOnly(Side.CLIENT)
	public String getTitle(){
		return I18n.format("embers.research."+name+".title");
	}
	
	@SideOnly(Side.CLIENT)
	public ArrayList<String> getLines(){
		String s = I18n.format("embers.research."+name);
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
			if (counter > 256){
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
