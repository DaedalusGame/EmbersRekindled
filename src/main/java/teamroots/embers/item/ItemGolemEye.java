package teamroots.embers.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import teamroots.embers.Embers;
import teamroots.embers.api.item.IFilterItem;
import teamroots.embers.util.EnumFilterSetting;
import teamroots.embers.util.FilterUtil;
import teamroots.embers.util.IFilterComparator;

import javax.annotation.Nullable;
import java.util.List;

public class ItemGolemEye extends ItemBase implements IFilterItem {
    private static ThreadLocal<Float> eyeOpen = new ThreadLocal<>();

    public static void setEyeOpen(float amt) {
        eyeOpen.set(amt);
    }

    public ItemGolemEye(String name) {
        super(name, true);
        addPropertyOverride(new ResourceLocation(Embers.MODID,"eye_open"), (stack, worldIn, entityIn) -> eyeOpen.get() != null ? eyeOpen.get() : 0);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(stack.hasTagCompound()) {
            if(playerIn.isSneaking())
                setInvert(stack,!getInvert(stack));
            else
                setSetting(stack,getSetting(stack).rotate(1));
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public boolean acceptsItem(ItemStack filterStack, ItemStack stack) {
        NBTTagCompound compound = filterStack.getTagCompound();
        if (compound == null)
            return true;
        ItemStack stack1 = new ItemStack(compound.getCompoundTag("stack1"));
        ItemStack stack2 = new ItemStack(compound.getCompoundTag("stack2"));
        String comparatorName = compound.getString("comparator");
        int offset = compound.getInteger("offset");
        EnumFilterSetting setting = EnumFilterSetting.values()[compound.getInteger("setting")];
        boolean inverted = compound.getBoolean("invert");

        IFilterComparator comparator = FilterUtil.getComparator(comparatorName);
        if (comparator == null) {
            comparator = findComparator(stack1, stack2, offset);
            compound.setString("comparator", comparator.getName());
        }
        return comparator.isBetween(stack1, stack2, stack, setting) == inverted;
    }

    private IFilterComparator findComparator(ItemStack stack1, ItemStack stack2, int offset) {
        if(stack1.isEmpty() && stack2.isEmpty())
            return FilterUtil.ANY;
        List<IFilterComparator> comparators = FilterUtil.getComparators(stack1, stack2);
        return comparators.get(offset % comparators.size());
    }

    public void incrementOffset(ItemStack filterStack) {
        NBTTagCompound compound = getOrCreateTagCompound(filterStack);
        ItemStack stack1 = new ItemStack(compound.getCompoundTag("stack1"));
        ItemStack stack2 = new ItemStack(compound.getCompoundTag("stack2"));
        int offset = compound.getInteger("offset");
        IFilterComparator comparator = findComparator(stack1, stack2, offset + 1);
        compound.setInteger("offset", offset + 1);
        compound.setString("comparator", comparator.getName());
    }

    public void setInvert(ItemStack filterStack, boolean inverted) {
        NBTTagCompound compound = getOrCreateTagCompound(filterStack);
        compound.setBoolean("invert", inverted);
    }

    public boolean getInvert(ItemStack filterStack) {
        NBTTagCompound compound = filterStack.getTagCompound();
        if (compound == null)
            return false;
        return compound.getBoolean("invert");
    }

    public void setSetting(ItemStack filterStack, EnumFilterSetting setting) {
        NBTTagCompound compound = getOrCreateTagCompound(filterStack);
        compound.setInteger("setting", setting.ordinal());
    }

    public EnumFilterSetting getSetting(ItemStack filterStack) {
        NBTTagCompound compound = filterStack.getTagCompound();
        if (compound == null)
            return EnumFilterSetting.STRICT;
        return EnumFilterSetting.values()[compound.getInteger("setting")];
    }

    public void setStacks(ItemStack filterStack, ItemStack stack1, ItemStack stack2) {
        NBTTagCompound compound = getOrCreateTagCompound(filterStack);
        compound.setTag("stack1", stack1.serializeNBT());
        compound.setTag("stack2", stack2.serializeNBT());
    }

    public void setStacks(ItemStack filterStack, ItemStack stack) {
        setStacks(filterStack, stack, stack);
    }

    private NBTTagCompound getOrCreateTagCompound(ItemStack filterStack) {
        NBTTagCompound compound = filterStack.getTagCompound();
        if(compound == null) {
            compound = new NBTTagCompound();
            filterStack.setTagCompound(compound);
        }
        return compound;
    }

    @Override
    public String formatFilter(ItemStack filterStack) {
        NBTTagCompound compound = filterStack.getTagCompound();
        if (compound == null)
            return I18n.format("embers.filter.any");
        ItemStack stack1 = new ItemStack(compound.getCompoundTag("stack1"));
        ItemStack stack2 = new ItemStack(compound.getCompoundTag("stack2"));
        String comparatorName = compound.getString("comparator");
        int offset = compound.getInteger("offset");
        EnumFilterSetting setting = EnumFilterSetting.values()[compound.getInteger("setting")];
        boolean inverted = compound.getBoolean("invert");

        IFilterComparator comparator = FilterUtil.getComparator(comparatorName);
        if (comparator == null) {
            comparator = findComparator(stack1, stack2, offset);
        }
        return comparator.format(stack1,stack2,setting,inverted);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (!stack.hasTagCompound())
            return;
        tooltip.add(formatFilter(stack));
    }

    public void reset(ItemStack filterStack) {
        NBTTagCompound compound = filterStack.getTagCompound();
        if (compound == null)
            return;
        compound.removeTag("comparator");
        compound.removeTag("offset");
    }
}
