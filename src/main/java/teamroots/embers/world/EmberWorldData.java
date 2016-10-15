package teamroots.embers.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import teamroots.embers.Embers;
import teamroots.embers.network.message.MessageEmberData;
import teamroots.embers.util.Vec2i;

public class EmberWorldData extends WorldSavedData {
	public Map<String, Double> emberData = new HashMap<String, Double>();

	public EmberWorldData(String name) {
		super(name);
	}
	
	public EmberWorldData(){
		super(Embers.MODID+":ember");
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		if (tag.hasKey(Embers.MODID+":emberLocations")){
			NBTTagList list = tag.getTagList(Embers.MODID+":emberLocations", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); i ++){
				NBTTagCompound location = list.getCompoundTagAt(i);
				emberData.put(location.getString("pos"), location.getDouble("value"));
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		NBTTagList list = new NBTTagList();
		Iterator<Entry<String, Double>> values = emberData.entrySet().iterator();
		while (values.hasNext()){
			Entry<String, Double> entry = values.next();
			NBTTagCompound location = new NBTTagCompound();
			location.setString("pos", entry.getKey());
			location.setDouble("value", entry.getValue());
			list.appendTag(location);
		}
		tag.setTag(Embers.MODID+":emberLocations", list);
		return tag;
	}

	public void readPacketData(MessageEmberData packet) {
		NBTTagCompound tag = packet.tag;
		if (tag.hasKey(Embers.MODID+":emberLocations")){
			NBTTagList list = tag.getTagList(Embers.MODID+":emberLocations", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); i ++){
				NBTTagCompound location = list.getCompoundTagAt(i);
				emberData.put(location.getString("pos"), location.getDouble("value"));
			}
		}
	}
	
	public ArrayList<MessageEmberData> getData(){
		ArrayList<MessageEmberData> packets = new ArrayList<MessageEmberData>();
		Iterator<Entry<String, Double>> values = emberData.entrySet().iterator();
		int i = 0;
		NBTTagList list = new NBTTagList();
		while (values.hasNext()){
			if (i % 512 == 0){
				i = 0;
				NBTTagCompound tag = new NBTTagCompound();
				tag.setTag(Embers.MODID+":emberLocations", list);
				packets.add(new MessageEmberData(tag));
				list = new NBTTagList();
			}
			Entry<String, Double> entry = values.next();
			NBTTagCompound location = new NBTTagCompound();
			location.setString("pos", entry.getKey());
			location.setDouble("value", entry.getValue());
			list.appendTag(location);
			i ++;
		}
		return packets;
	}
	
	public static EmberWorldData get(World world){
		EmberWorldData data = null;
		if (world != null){
			if ((EmberWorldData)world.loadItemData(EmberWorldData.class, Embers.MODID+":ember") != null){
				data = (EmberWorldData)world.loadItemData(EmberWorldData.class, Embers.MODID+":ember");
			}
		}
		if (data == null && world != null){
			data = new EmberWorldData();
			world.setItemData(Embers.MODID+":ember", data);
		}
		return data;
	}

}
