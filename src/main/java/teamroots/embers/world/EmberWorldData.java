package teamroots.embers.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import teamroots.embers.Embers;
import teamroots.embers.util.EmberGenUtil;

public class EmberWorldData extends WorldSavedData {
	public EmberWorldData(String name) {
		super(name);
	}
	
	public EmberWorldData(){
		super(Embers.MODID);
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
			if ((EmberWorldData)world.loadData(EmberWorldData.class, Embers.MODID) != null){
				data = (EmberWorldData)world.loadData(EmberWorldData.class, Embers.MODID);
			}
		}
		if (data == null && world != null){
			data = new EmberWorldData();
		}
		return data;
	}

}
