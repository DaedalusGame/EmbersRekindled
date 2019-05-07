package teamroots.embers.research.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ResearchCapabilityProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {
    private IResearchCapability capability = null;

    public ResearchCapabilityProvider(){
        capability = new DefaultResearchCapability();
    }

    public ResearchCapabilityProvider(IResearchCapability capability){
        this.capability = capability;
    }

    @CapabilityInject(IResearchCapability.class)
    public static final Capability<IResearchCapability> researchCapability = null;

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == researchCapability;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (researchCapability != null && capability == researchCapability)
            return researchCapability.cast(this.capability);
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        capability.writeToNBT(compound);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        capability.readFromNBT(compound);
    }
}
