package teamroots.embers.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.util.FluidColorHelper;

import java.awt.*;
import java.util.Random;

public abstract class TileEntityOpenTank extends TileFluidHandler {
    FluidStack lastEscaped = null;
    long lastEscapedTickServer;
    long lastEscapedTickClient;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound compound = super.writeToNBT(tag);
        if(lastEscaped != null) {
            compound.setTag("lastEscaped",lastEscaped.writeToNBT(new NBTTagCompound()));
            compound.setLong("lastEscapedTick",lastEscapedTickServer);
        }
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if(tag.hasKey("lastEscaped")) {
            lastEscaped = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("lastEscaped"));
            lastEscapedTickServer = tag.getLong("lastEscapedTick");
        }
    }

    public void setEscapedFluid(FluidStack stack) {
        lastEscaped = stack;
        lastEscapedTickServer = world.getTotalWorldTime();
        markDirty();
    }

    protected boolean shouldEmitParticles() {
        if(lastEscaped == null)
            return false;
        if(lastEscapedTickClient < lastEscapedTickServer) {
            lastEscapedTickClient = lastEscapedTickServer;
            return true;
        }
        long dTime = world.getTotalWorldTime() - lastEscapedTickClient;
        if(dTime < lastEscaped.amount+5)
            return true;
        return false;
    }

    protected abstract void updateEscapeParticles();
}
