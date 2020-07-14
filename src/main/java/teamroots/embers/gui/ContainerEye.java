package teamroots.embers.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.embers.api.filter.EnumFilterSetting;
import teamroots.embers.util.FilterUtil;
import teamroots.embers.api.filter.IFilterComparator;
import teamroots.embers.util.Vec2i;

import java.util.List;

public class ContainerEye extends Container {
    public enum EnumButton {
        FINISH,
        LEFT,
        RIGHT,
        FLAG,
        INVERT,
    }

    public static final int WIDTH = 176;
    public static final int HEIGHT = 85 + 120;

    public static final int VAR_FLAG = 0;
    public static final int VAR_INVERTED = 1;
    public static final int VAR_FILTER_OFFSET = 2;

    EnumHand hand;
    ItemStack stack;

    IInventory filterInventory = new InventoryBasic("EyeFilter", true,2);

    ItemStack stack1;
    ItemStack stack2;
    boolean inverted;
    EnumFilterSetting flag = EnumFilterSetting.STRICT;
    IFilterComparator comparator;
    int filterOffset;

    public ContainerEye(EntityPlayer player) {
        stack = player.getHeldItemMainhand();
        hand = EnumHand.MAIN_HAND;
        if (stack.isEmpty())
        {
            stack = player.getHeldItemOffhand();
            hand = EnumHand.OFF_HAND;
        }

        readFromStack(stack);

        Vec2i leftItem = getButtonPosition(0,0, 90);
        Vec2i rightItem = getButtonPosition(0,0, 270);
        this.addSlotToContainer(new Slot(filterInventory,0, leftItem.x-8, leftItem.y-8) {
            @Override
            public void onSlotChanged() {
                super.onSlotChanged();
                filterOffset = 0;
                refresh();
            }
        });
        this.addSlotToContainer(new Slot(filterInventory,1, rightItem.x-8, rightItem.y-8) {
            @Override
            public void onSlotChanged() {
                super.onSlotChanged();
                filterOffset = 0;
                refresh();
            }
        });

        bindPlayerInventory(player.inventory);
    }

    private void readFromStack(ItemStack stack)
    {
        NBTTagCompound compound = stack.getTagCompound();
        if(compound != null) {
            String comparatorName = compound.getString("comparator");
            stack1 = new ItemStack(compound.getCompoundTag("stack1"));
            stack2 = new ItemStack(compound.getCompoundTag("stack2"));
            comparator = FilterUtil.getComparator(comparatorName);
            filterOffset = compound.getInteger("offset");
            inverted = compound.getBoolean("inverted");
            flag = EnumFilterSetting.get(compound.getInteger("setting"));
        }
    }

    public void triggerButton(EnumButton button) {
        switch(button)
        {
            case FINISH:
                writeToStack();
                break;
            case LEFT:
                moveLeft();
                break;
            case RIGHT:
                moveRight();
                break;
            case FLAG:
                toggleFlag();
                break;
            case INVERT:
                toggleInvert();
                break;
        }
    }

    public void writeToStack() {
        NBTTagCompound compound = stack.getTagCompound();
        if(compound == null)
            compound = new NBTTagCompound();
        compound.setString("comparator", comparator.getName());
        compound.setInteger("offset", filterOffset);
        compound.setBoolean("inverted", inverted);
        compound.setInteger("setting", flag.ordinal());
        compound.setTag("stack1", stack1.serializeNBT());
        compound.setTag("stack2", stack2.serializeNBT());
        stack.setTagCompound(compound);
    }

    public void toggleInvert() {
        inverted = !inverted;
        detectAndSendChanges();
    }

    public void toggleFlag() {
        flag = flag.rotate(1);
        detectAndSendChanges();
    }

    public void moveLeft() {
        filterOffset--;
        refresh();
        detectAndSendChanges();
    }

    public void moveRight() {
        filterOffset++;
        refresh();
        detectAndSendChanges();
    }

    private void refresh() {
        stack1 = filterInventory.getStackInSlot(0);
        stack2 = filterInventory.getStackInSlot(1);
        comparator = findComparator(stack1, stack2, filterOffset);
    }

    private IFilterComparator findComparator(ItemStack stack1, ItemStack stack2, int offset) {
        if(stack1.isEmpty() && stack2.isEmpty())
            return FilterUtil.ANY;
        List<IFilterComparator> comparators = FilterUtil.getComparators(stack1, stack2);
        if(offset < 0)
            offset = (offset % comparators.size()) + comparators.size();
        return comparators.get(offset % comparators.size());
    }

    protected void broadcastData(IContainerListener crafting)
    {
        crafting.sendWindowProperty(this, VAR_FLAG, flag.ordinal());
        crafting.sendWindowProperty(this, VAR_INVERTED, inverted ? 1 : 0);
        crafting.sendWindowProperty(this, VAR_FILTER_OFFSET, filterOffset);
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        this.broadcastData(listener);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);
            this.broadcastData(icontainerlistener);
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        switch(id)
        {
            case VAR_FILTER_OFFSET:
                filterOffset = data;
                break;
            case VAR_INVERTED:
                inverted = data > 0;
                break;
            case VAR_FLAG:
                flag = EnumFilterSetting.get(data);
                break;
            default:
                super.updateProgressBar(id, data);
                break;
        }
        refresh();
    }

    public Vec2i getButtonPosition(int xPos, int yPos, float angle) {
        int xCenter = xPos + WIDTH / 2;
        int yCenter = yPos + 85 - 50;

        int xButton = xCenter + (int)(-Math.sin(Math.toRadians(angle)) * (WIDTH - 70) / 2);
        int yButton = yCenter + (int)(-Math.cos(Math.toRadians(angle)) * 40);

        return new Vec2i(xButton, yButton);
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn)
    {
        return false;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 2)
            {
                if (!this.mergeItemStack(itemstack1, 2, 2 + 27 + 9, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, 2, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        //TODO
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
        int yInventory = HEIGHT - 85;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, yInventory + 4 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            if (inventoryPlayer.getStackInSlot(i) == stack) {
                addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, yInventory + 4 + 58) {
                    @Override
                    public boolean canTakeStack(EntityPlayer playerIn) {
                        return false;
                    }
                });
            } else {
                addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, yInventory + 4 + 58));
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return !stack.isEmpty() && playerIn.getHeldItem(hand) == stack;
    }
}
