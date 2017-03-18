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
import teamroots.embers.util.EmberGenUtil;
import teamroots.embers.util.NoiseGenUtil;

public class EmberWorldData extends WorldSavedData {
	public EmberWorldData(String name) {
		super(name);
	}
	
	public EmberWorldData(){
		super(Embers.MODID+":ember");
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		EmberGenUtil.offX = tag.getInteger("offX");
		EmberGenUtil.offZ = tag.getInteger("offZ");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setInteger("offX", EmberGenUtil.offX);
		tag.setInteger("offZ", EmberGenUtil.offZ);
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
		}
		return data;
	}

}
