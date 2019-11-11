package teamroots.embers.tileentity;

import mysticalmechanics.api.MysticalMechanicsAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.Embers;
import teamroots.embers.EventManager;
import teamroots.embers.SoundManager;
import teamroots.embers.api.event.DialInformationEvent;
import teamroots.embers.api.tile.IExtraCapabilityInformation;
import teamroots.embers.api.tile.IExtraDialInformation;
import teamroots.embers.api.tile.IMechanicallyPowered;
import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.api.upgrades.UpgradeUtil;
import teamroots.embers.recipe.BoreOutput;
import teamroots.embers.recipe.RecipeRegistry;
import teamroots.embers.util.EmberGenUtil;
import teamroots.embers.util.Misc;
import teamroots.embers.util.WeightedItemStack;
import teamroots.embers.util.sound.ISoundController;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class TileEntityEmberBore extends TileEntity implements ITileEntityBase, ITickable, IMultiblockMachine, ISoundController, IMechanicallyPowered, IExtraDialInformation, IExtraCapabilityInformation {
    public static final int MAX_LEVEL = 7;
    public static final int BORE_TIME = 200;
    public static final int SLOT_FUEL = 8;
    public static final double FUEL_CONSUMPTION = 3;

    public static final int SOUND_ON = 1;
    public static final int SOUND_ON_DRILL = 2;
    public static final int[] SOUND_IDS = new int[]{SOUND_ON, SOUND_ON_DRILL};

    Random random = new Random();
    public long ticksExisted = 0;
    public float angle = 0;
    public double ticksFueled = 0;
    public float lastAngle;
    boolean isRunning;

    HashSet<Integer> soundsPlaying = new HashSet<>();
    private List<IUpgradeProvider> upgrades = new ArrayList<>();
    private double speedMod;

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.add(-1, -2, -1), pos.add(2, 1, 2));
    }

    public ItemStackHandler inventory = new EmberBoreInventory(9);

    public TileEntityEmberBore() {
        super();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setTag("inventory", inventory.serializeNBT());
        tag.setDouble("fueled", ticksFueled);
        tag.setBoolean("isRunning", isRunning);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        NBTTagCompound inventoryTag = tag.getCompoundTag("inventory");
        inventoryTag.removeTag("Size"); //Migrating old Ember Bores
        this.inventory.deserializeNBT(inventoryTag);
        ticksFueled = tag.getDouble("fueled");
        isRunning = tag.getBoolean("isRunning");
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                            EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        this.invalidate();
        Misc.spawnInventoryInWorld(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, inventory);
        world.setBlockToAir(pos.add(1, 0, 0));
        world.setBlockToAir(pos.add(0, 0, 1));
        world.setBlockToAir(pos.add(-1, 0, 0));
        world.setBlockToAir(pos.add(0, 0, -1));
        world.setBlockToAir(pos.add(1, 0, -1));
        world.setBlockToAir(pos.add(-1, 0, 1));
        world.setBlockToAir(pos.add(1, 0, 1));
        world.setBlockToAir(pos.add(-1, 0, -1));
        world.setTileEntity(pos, null);
    }

    public EmberBoreInventory getInventory() {
        return (EmberBoreInventory) inventory;
    }

    public boolean canMine() {
        return getPos().getY() <= MAX_LEVEL;
    }

    public boolean canInsert(ArrayList<ItemStack> returns) {
        for (ItemStack stack : returns) {
            ItemStack returned = stack;
            for (int slot = 0; slot < getInventory().getSlots() - 1; slot++) {
                returned = getInventory().insertItemInternal(slot, returned, true);
            }
            if (!returned.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void insert(ArrayList<ItemStack> returns) {
        for (ItemStack stack : returns) {
            ItemStack returned = stack;
            for (int slot = 0; slot < getInventory().getSlots() - 1; slot++) {
                returned = getInventory().insertItemInternal(slot, returned, false);
            }
        }
    }

    @Override
    public void update() {
        upgrades = UpgradeUtil.getUpgrades(world, pos, new EnumFacing[]{EnumFacing.UP});
        UpgradeUtil.verifyUpgrades(this, upgrades);
        if (UpgradeUtil.doTick(this, upgrades))
            return;
        if (getWorld().isRemote)
            handleSound();
        speedMod = UpgradeUtil.getTotalSpeedModifier(this, upgrades);
        lastAngle = angle;
        if (isRunning) {
            angle += 12.0f * speedMod;
        }
        boolean previousRunning = isRunning;
        if (!getWorld().isRemote) {
            isRunning = false;
            ticksExisted++;

            double fuelConsumption = UpgradeUtil.getOtherParameter(this, "fuel_consumption", FUEL_CONSUMPTION, upgrades);
            boolean cancel = false;
            if (ticksFueled >= fuelConsumption) {
                isRunning = true;
                ticksFueled -= fuelConsumption;
                cancel = UpgradeUtil.doWork(this, upgrades);
            } else {
                ticksFueled = 0;
            }

            if (!cancel) {
                if (ticksFueled < fuelConsumption) {
                    ItemStack fuel = inventory.getStackInSlot(SLOT_FUEL);
                    if (!fuel.isEmpty()) {
                        ItemStack fuelCopy = fuel.copy();
                        int burnTime = TileEntityFurnace.getItemBurnTime(fuelCopy);
                        if (burnTime > 0) {
                            ticksFueled = burnTime;
                            fuel.shrink(1);
                            if (fuel.isEmpty())
                                inventory.setStackInSlot(SLOT_FUEL, fuelCopy.getItem().getContainerItem(fuelCopy));
                            markDirty();
                        }
                    }
                } else if (canMine()) {
                    int boreTime = (int) Math.ceil(BORE_TIME * (1 / speedMod));
                    if (ticksExisted % boreTime == 0) {
                        if (random.nextFloat() < EmberGenUtil.getEmberDensity(world.getSeed(), getPos().getX(), getPos().getZ())) {
                            BoreOutput output = RecipeRegistry.getBoreOutput(world, getPos());
                            if (output != null) {
                                ArrayList<ItemStack> returns = new ArrayList<>();
                                if (!output.stacks.isEmpty()) {
                                    WeightedItemStack picked = WeightedRandom.getRandomItem(random, output.stacks);
                                    returns.add(picked.getStack().copy());
                                }
                                UpgradeUtil.transformOutput(this, returns, upgrades);
                                if (canInsert(returns)) {
                                    insert(returns);
                                }
                            }
                        }
                    }
                }
            } else {
                isRunning = false;
            }

            if (isRunning != previousRunning) {
                markDirty();
            }
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        Misc.syncTE(this);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) this.inventory;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void playSound(int id) {
        float soundX = (float) pos.getX() + 0.5f;
        float soundY = (float) pos.getY() - 0.5f;
        float soundZ = (float) pos.getZ() + 0.5f;
        switch (id) {
            case SOUND_ON:
                Embers.proxy.playMachineSound(this, SOUND_ON, SoundManager.BORE_LOOP, SoundCategory.BLOCKS, true, 1.0f, 1.0f, soundX, soundY, soundZ);
                break;
            case SOUND_ON_DRILL:
                Embers.proxy.playMachineSound(this, SOUND_ON_DRILL, SoundManager.BORE_LOOP_MINE, SoundCategory.BLOCKS, true, 1.0f, 1.0f, soundX, soundY, soundZ);
                break;
        }
        world.playSound(soundX, soundY, soundZ, SoundManager.BORE_START, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
        soundsPlaying.add(id);
    }

    @Override
    public void stopSound(int id) {
        world.playSound((float) pos.getX() + 0.5f, (float) pos.getY() - 0.5f, (float) pos.getZ() + 0.5f, SoundManager.BORE_STOP, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
        soundsPlaying.remove(id);
    }

    @Override
    public boolean isSoundPlaying(int id) {
        return soundsPlaying.contains(id);
    }

    @Override
    public int[] getSoundIDs() {
        return SOUND_IDS;
    }

    @Override
    public boolean shouldPlaySound(int id) {
        /*if(ticksFueled > 0) {
            return isRunning && id == SOUND_ON_DRILL || id == SOUND_ON && !isRunning;
		}*/

        return isRunning;
    }

    @Override
    public float getCurrentVolume(int id, float volume) {
        boolean isMining = canMine();

        switch (id) {
            case SOUND_ON:
                return !isMining ? 1.0f : 0.0f;
            case SOUND_ON_DRILL:
                return isMining ? 1.0f : 0.0f;
            default:
                return 0f;
        }
    }

    @Override
    public float getCurrentPitch(int id, float pitch) {
        return (float) speedMod;
    }

    @Override
    public double getMechanicalSpeed(double power) {
        return power > 0 ? Math.log10(power / 15) * 3 : 0;
    }

    @Override
    public double getMinimumPower() {
        return 15;
    }

    @Override
    public double getNominalSpeed() {
        return 1;
    }

    @Override
    public void addDialInformation(EnumFacing facing, List<String> information, String dialType) {
        UpgradeUtil.throwEvent(this, new DialInformationEvent(this, information, dialType), upgrades);
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return true;
    }

    @Override
    public void addCapabilityDescription(List<String> strings, Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            strings.add(IExtraCapabilityInformation.formatCapability(EnumIOType.INPUT, "embers.tooltip.goggles.item", I18n.format("embers.tooltip.goggles.item.fuel")));
            strings.add(IExtraCapabilityInformation.formatCapability(EnumIOType.OUTPUT, "embers.tooltip.goggles.item", I18n.format("embers.tooltip.goggles.item.ember")));
        }
    }

    public class EmberBoreInventory extends ItemStackHandler {
        public EmberBoreInventory() {
        }

        public EmberBoreInventory(int size) {
            super(size);
        }

        public EmberBoreInventory(NonNullList<ItemStack> stacks) {
            super(stacks);
        }

        @Override
        protected void onContentsChanged(int slot) {
            TileEntityEmberBore.this.markDirty();
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            int burntime = TileEntityFurnace.getItemBurnTime(stack);
            if (slot == SLOT_FUEL && burntime != 0) {
                return super.insertItem(slot, stack, simulate);
            } else if (burntime != 0) {
                return super.insertItem(SLOT_FUEL, stack, simulate);
            }
            return stack;
        }

        public ItemStack insertItemInternal(int slot, ItemStack stack, boolean simulate) {
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            ItemStack currentFuel = super.extractItem(slot, amount, true);
            int burntime = TileEntityFurnace.getItemBurnTime(currentFuel);
            if (slot == SLOT_FUEL && burntime != 0) {
                return ItemStack.EMPTY;
            }
            return super.extractItem(slot, amount, simulate);
        }
    }
}
