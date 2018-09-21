package teamroots.embers.power;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import teamroots.embers.api.capabilities.EmbersCapabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DefaultEmberItemCapability implements ICapabilityProvider, teamroots.embers.api.power.IEmberCapability {
    @Nonnull
    private ItemStack stack;

    public DefaultEmberItemCapability(@Nonnull ItemStack stack, double capacity) {
        this.stack = stack;
        if(!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
            setEmber(0);
            setEmberCapacity(capacity);
        }
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == EmbersCapabilities.EMBER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == EmbersCapabilities.EMBER_CAPABILITY)
            return EmbersCapabilities.EMBER_CAPABILITY.cast(this);
        return null;
    }

    @Override
    public double getEmber() {
        return stack.hasTagCompound() ? stack.getTagCompound().getDouble("ember") : 0;
    }

    @Override
    public double getEmberCapacity() {
        return stack.hasTagCompound() ? stack.getTagCompound().getDouble("emberCapacity") : 0;
    }

    @Override
    public void setEmber(double value) {
        if(stack.hasTagCompound()) {
            stack.getTagCompound().setDouble("ember",value);
        }
    }

    @Override
    public void setEmberCapacity(double value) {
        if(stack.hasTagCompound()) {
            stack.getTagCompound().setDouble("emberCapacity",value);
        }
    }

    @Override
    public double addAmount(double value, boolean doAdd) {
        double ember = getEmber();
        double capacity = getEmberCapacity();
        double added = Math.min(capacity - ember,value);
        double newEmber = ember + added;
        if (doAdd){
            if(newEmber != ember)
                onContentsChanged();
            setEmber(ember + added);
        }
        return added;
    }

    @Override
    public double removeAmount(double value, boolean doRemove) {
        double ember = getEmber();
        double removed = Math.min(ember,value);
        double newEmber = ember - removed;
        if (doRemove){
            if(newEmber != ember)
                onContentsChanged();
            setEmber(ember - removed);
        }
        return removed;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        //NOOP
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        //NOOP
    }

    @Override
    public void onContentsChanged() {

    }
}
