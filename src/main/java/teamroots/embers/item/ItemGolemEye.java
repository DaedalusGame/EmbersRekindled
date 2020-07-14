package teamroots.embers.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.embers.Embers;
import teamroots.embers.api.item.FilterSieve;
import teamroots.embers.api.item.IFilter;
import teamroots.embers.api.item.IFilterItem;
import teamroots.embers.gui.GuiHandler;
import teamroots.embers.tileentity.ISpecialFilter;
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
        playerIn.openGui(Embers.instance, GuiHandler.EYE, worldIn, playerIn.getPosition().getX(), playerIn.getPosition().getY(), playerIn.getPosition().getZ());
        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof ISpecialFilter && player.isSneaking()) {
            ItemStack held = player.getHeldItem(hand);
            setFilter(held, ((ISpecialFilter) tile).getSpecialFilter());
            player.setHeldItem(hand,held);
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    public void setFilter(ItemStack filterStack, IFilter filter) {
        NBTTagCompound compound = getOrCreateTagCompound(filterStack);
        compound.setTag("filter",filter.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public IFilter getFilter(ItemStack filterStack) {
        NBTTagCompound compound = filterStack.getTagCompound();
        if (compound == null)
            return FilterUtil.FILTER_ANY;
        return FilterUtil.deserializeFilter(compound.getCompoundTag("filter"));
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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (!stack.hasTagCompound())
            return;
        IFilter filter = getFilter(stack);
        tooltip.add(filter.formatFilter());
    }

    public void reset(ItemStack filterStack) {
        NBTTagCompound compound = filterStack.getTagCompound();
        if (compound == null)
            return;
        compound.removeTag("comparator");
        compound.removeTag("offset");
    }
}
