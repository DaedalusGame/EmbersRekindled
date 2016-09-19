package teamroots.embers.util;

import net.minecraft.item.Item;

public class StructItem {
	private Item item = null;
	private int meta = 0;
	public StructItem(Item item, int meta){
		this.item = item;
		this.meta = meta;
	}
	
	public Item getItem(){
		return item;
	}
	
	public int getMetadata(){
		return meta;
	}
}
