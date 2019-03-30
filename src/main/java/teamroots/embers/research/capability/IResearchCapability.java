package teamroots.embers.research.capability;

import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;

public interface IResearchCapability {
    void setCheckmark(String research, boolean checked);
    boolean isChecked(String research);
    Map<String,Boolean> getCheckmarks();
    void writeToNBT(NBTTagCompound tag);
    void readFromNBT(NBTTagCompound tag);
}
