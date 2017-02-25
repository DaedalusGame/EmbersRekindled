package teamroots.embers.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import teamroots.embers.Embers;
import teamroots.embers.network.message.MessageEmberData;
import teamroots.embers.util.NoiseGenUtil;

public class EmberWorldData extends WorldSavedData {
	public Map<String, Double> emberData = new HashMap<String, Double>();
	public Map<String, Long> emberTimes = new HashMap<String, Long>();
	public long seed = 0;
	public long ticks = 0;

	public EmberWorldData(String name) {
		super(name);
	}
	
	public EmberWorldData(){
		super(Embers.MODID+":ember");
	}
	
	public static String getChunkString(int chunkX, int chunkY){
		return ""+chunkX+"_"+chunkY;
	}
	
	public static double getEmberNoise(long seed, int x, int y){
		return (double)(Math.pow(NoiseGenUtil.getOctave(seed, x, y, 15),5)*256.0f
				+Math.pow(NoiseGenUtil.getOctave(seed, x, y, 12),5)*240.0f
				+Math.pow(NoiseGenUtil.getOctave(seed, x, y, 8),5)*128.0f
				+Math.pow(NoiseGenUtil.getOctave(seed, x, y, 7),5)*100.0f
				+Math.pow(NoiseGenUtil.getOctave(seed, x, y, 4),5)*40.0f
				+Math.pow(NoiseGenUtil.getOctave(seed, x, y, 3),5)*32.0f
				+Math.pow(NoiseGenUtil.getOctave(seed, x, y, 2),5)*16.0f
				+Math.pow(NoiseGenUtil.getOctave(seed, x, y, 1),5)*10.0f)*1000.0;
	}
	
	public double getEmberForChunk(int chunkX, int chunkY){
		String chunkString = getChunkString(chunkX, chunkY);
		if (!emberData.containsKey(chunkString)){
			emberData.put(chunkString, getEmberNoise(seed, chunkX, chunkY));
			emberTimes.put(chunkString, ticks);
			markDirty();
		}
		else {
			if (!emberTimes.containsKey(chunkString)){
				emberTimes.put(chunkString, ticks);
			}
			else {
				long dTime = ticks-emberTimes.get(chunkString);
				if (dTime < 0){
					dTime = Long.MAX_VALUE-emberTimes.get(chunkString)+ticks;
				}
				emberTimes.replace(chunkString, ticks);
				emberData.replace(chunkString, Math.min(getEmberNoise(seed,chunkX,chunkY),emberData.get(chunkString)+dTime/100.0));
				markDirty();
			}
		}
		return emberData.get(chunkString);
	}
	
	public void setEmberForChunk(int chunkX, int chunkY, double value){
		String chunkString = getChunkString(chunkX, chunkY);
		double previous = emberData.containsKey(chunkString) ? emberData.get(chunkString) : 0;
		if (!emberData.containsKey(chunkString)){
			emberData.put(chunkString, value);
			emberTimes.put(chunkString, ticks);
			markDirty();
		}
		else {
			if (!emberTimes.containsKey(chunkString)){
				emberTimes.put(chunkString, ticks);
			}
			else {
				emberTimes.replace(chunkString, ticks);
			}
			emberData.replace(chunkString, value);
			markDirty();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		seed = tag.getLong("seed");
		ticks = tag.getLong("ticks");
		if (tag.hasKey(Embers.MODID+":emberLocations")){
			NBTTagList list = tag.getTagList(Embers.MODID+":emberLocations", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); i ++){
				NBTTagCompound location = list.getCompoundTagAt(i);
				emberData.put(location.getString("pos"), location.getDouble("value"));
				emberTimes.put(location.getString("pos"), location.getLong("time"));
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		NBTTagList list = new NBTTagList();
		tag.setLong("seed", seed);
		tag.setLong("ticks", ticks);
		String[] keys = emberData.keySet().toArray(new String[emberData.keySet().size()]);
		Double[] values = emberData.values().toArray(new Double[emberData.keySet().size()]);
		Long[] times = emberTimes.values().toArray(new Long[emberData.keySet().size()]);
		for (int i = 0; i < keys.length; i ++){
			NBTTagCompound location = new NBTTagCompound();
			location.setString("pos", keys[i]);
			location.setDouble("value", values[i]);
			location.setDouble("time", times[i]);
			list.appendTag(location);
		}
		tag.setTag(Embers.MODID+":emberLocations", list);
		return tag;
	}
	
	public static EmberWorldData get(World world){
		EmberWorldData data = null;
		if (world != null){
			if ((EmberWorldData)world.loadData(EmberWorldData.class, Embers.MODID+":ember") != null){
				data = (EmberWorldData)world.loadData(EmberWorldData.class, Embers.MODID+":ember");
			}
		}
		if (data == null && world != null){
			data = new EmberWorldData();
			data.seed = world.getSeed();
			world.setData(Embers.MODID+":ember", data);
		}
		return data;
	}

}
