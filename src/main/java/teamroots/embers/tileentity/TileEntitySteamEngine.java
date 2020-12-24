package teamroots.embers.tileentity;

import mysticalmechanics.api.DefaultMechCapability;
import mysticalmechanics.api.MysticalMechanicsAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.Embers;
import teamroots.embers.SoundManager;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.misc.ILiquidFuel;
import teamroots.embers.api.tile.IExtraCapabilityInformation;
import teamroots.embers.block.BlockSteamEngine;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.util.Misc;
import teamroots.embers.util.sound.ISoundController;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashSet;
import java.util.List;

public class TileEntitySteamEngine extends TileEntity implements ITileEntityBase, ITickable, ISoundController, IExtraCapabilityInformation {
    class BurningFuel {
        ItemStack solidFuel = ItemStack.EMPTY;
        FluidStack liquidFuel;
        int timeLeft;

        public BurningFuel() {

        }

        public BurningFuel(ItemStack solidFuel, int timeLeft) {
            this.solidFuel = solidFuel;
            this.timeLeft = timeLeft;
        }

        public BurningFuel(FluidStack liquidFuel, int timeLeft) {
            this.liquidFuel = liquidFuel;
            this.timeLeft = timeLeft;
        }

        public void tick() {
            timeLeft--;
        }

        public void reset() {
            solidFuel = ItemStack.EMPTY;
            liquidFuel = null;
            timeLeft = 0;
        }

        public boolean isSolid() {
            return !solidFuel.isEmpty();
        }

        public boolean isLiquid() {
            return liquidFuel != null;
        }

        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            if(liquidFuel != null)
                tag.setTag("fluid", liquidFuel.writeToNBT(new NBTTagCompound()));
            if(!solidFuel.isEmpty())
                tag.setTag("item", solidFuel.serializeNBT());
            tag.setInteger("timeLeft",timeLeft);
            return tag;
        }

        public void readFromNBT(NBTTagCompound tag) {
            if(tag.hasKey("fluid"))
                liquidFuel = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("fluid"));
            if(tag.hasKey("item"))
                solidFuel = new ItemStack(tag.getCompoundTag("item"));
            timeLeft = tag.getInteger("timeLeft");
        }

        public boolean isEmpty() {
            return timeLeft <= 0;
        }

        public Color getColor() {
            if(isSolid())
                return new Color(72,72,72, 128);
            if(isLiquid()) {
                ILiquidFuel fuelHandler = EmbersAPI.getSteamEngineFuel(liquidFuel);
                if(fuelHandler != null)
                    return fuelHandler.getBurnColor(liquidFuel);
            }
            return new Color(0,0,0,0);
        }
    }

    public static int NORMAL_FLUID_THRESHOLD = 10;
    public static int NORMAL_FLUID_CONSUMPTION = 4;
    public static int GAS_CONSUMPTION = 20;
    public static double MAX_POWER = 50;
    public static int CAPACITY = 8000;
    public static double SOLID_POWER = 20;
    public static double FUEL_MULTIPLIER = 2;

    public static final int SOUND_BURN = 1;
    public static final int SOUND_STEAM = 2;
    public static final int[] SOUND_IDS = new int[]{SOUND_BURN, SOUND_STEAM};

    BurningFuel currentFuel = new BurningFuel();

    int ticksExisted = 0;
    //int burnProgress = 0;
    //int steamProgress = 0;
    HashSet<Integer> soundsPlaying = new HashSet<>();
    EnumFacing front = EnumFacing.UP;
    public FluidTank tank = new FluidTank(CAPACITY);
    public DefaultMechCapability capability = new DefaultMechCapability() {
        @Override
        public void setPower(double value, EnumFacing from) {
            if (from == null)
                super.setPower(value, null);
        }
    };
    public ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            TileEntitySteamEngine.this.markDirty();
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (TileEntityFurnace.getItemBurnTime(stack) == 0) {
                return stack;
            }
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            ItemStack currentFuel = super.extractItem(slot, amount, true);
            int burntime = TileEntityFurnace.getItemBurnTime(currentFuel);
            if (burntime != 0) {
                return ItemStack.EMPTY;
            }
            return super.extractItem(slot, amount, simulate);
        }

    };

    public TileEntitySteamEngine() {
        super();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setDouble("mech_power", capability.power);
        tag.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
        tag.setTag("progress", currentFuel.writeToNBT(new NBTTagCompound()));
        tag.setInteger("front", front.getIndex());
        tag.setTag("inventory", inventory.serializeNBT());
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        capability.power = tag.getDouble("mech_power");
        tank.readFromNBT(tag.getCompoundTag("tank"));
        currentFuel.readFromNBT(tag.getCompoundTag("progress"));
        inventory.deserializeNBT(tag.getCompoundTag("inventory"));
        front = EnumFacing.getFront(tag.getInteger("front"));
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
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == MysticalMechanicsAPI.MECH_CAPABILITY) {
            return facing == front;
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return true;
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == MysticalMechanicsAPI.MECH_CAPABILITY) {
            return (T) this.capability;
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) tank;
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) inventory;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        Misc.syncTE(this);
    }

    @Override
    public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                            EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        //TODO: Any fluid container
        boolean didFill = FluidUtil.interactWithFluidHandler(player, hand, tank);
        if (didFill) {
            this.markDirty();
            return true;
        }
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        this.invalidate();
        Misc.spawnInventoryInWorld(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, inventory);
        capability.setPower(0f, null);
        updateNearby();
        world.setTileEntity(pos, null);
    }

    public void updateNearby() {
        for (EnumFacing f : EnumFacing.values()) {
            TileEntity t = world.getTileEntity(getPos().offset(f));
            if (t != null && f == front) {
                if (t.hasCapability(MysticalMechanicsAPI.MECH_CAPABILITY, Misc.getOppositeFace(f))) {
                    t.getCapability(MysticalMechanicsAPI.MECH_CAPABILITY, Misc.getOppositeFace(f)).setPower(capability.getPower(Misc.getOppositeFace(f)), Misc.getOppositeFace(f));
                    t.markDirty();
                }
            }
        }
    }

    private ItemStack copyWithSize(ItemStack stack, int size) {
        stack = stack.copy();
        stack.setCount(size);
        return stack;
    }

    @Override
    public void update() {
        IBlockState state = world.getBlockState(getPos());
        if (state.getBlock() instanceof BlockSteamEngine) {
            this.front = state.getValue(BlockSteamEngine.facing);
        }

        boolean dirty = false;
        double powerGenerated = 0;
        if (world.isRemote) {
            spawnParticles();
            handleSound();
        }

        if(!world.isRemote && !currentFuel.isEmpty()) {
            currentFuel.tick();
            if (currentFuel.isEmpty()) {
                currentFuel.reset();
                dirty = true;
            }
        }

        if (currentFuel.isEmpty()) {
            FluidStack fluid = tank.getFluid();
            ILiquidFuel fuelHandler = EmbersAPI.getSteamEngineFuel(fluid);
            if (fluid != null && fuelHandler != null) { //Overclocked steam power
                fluid = tank.drain(Math.min(GAS_CONSUMPTION, Math.max(fluid.amount - 1, 1)), false);
                if (!world.isRemote) {
                    currentFuel = new BurningFuel(fluid, fuelHandler.getTime(fluid));
                    tank.drain(fluid, true);
                    dirty = true;
                }
            } else { //Otherwise try normal power generation from water and coal
                if (!world.isRemote && !inventory.getStackInSlot(0).isEmpty() && fluid != null && fluid.getFluid() == FluidRegistry.WATER && tank.getFluidAmount() >= NORMAL_FLUID_THRESHOLD) {
                    ItemStack fuel = inventory.getStackInSlot(0);
                    if (!fuel.isEmpty()) {
                        ItemStack fuelCopy = fuel.copy();
                        int burnTime = TileEntityFurnace.getItemBurnTime(fuelCopy);
                        if (burnTime > 0) {
                            currentFuel = new BurningFuel(copyWithSize(fuelCopy, 1), (int)(burnTime * FUEL_MULTIPLIER));
                            fuel.shrink(1);
                            if (fuel.isEmpty())
                                inventory.setStackInSlot(0, fuelCopy.getItem().getContainerItem(fuelCopy));
                            dirty = true;
                        }
                    }
                }
            }
        }

        if (currentFuel.isLiquid()) { //Generate liquid power
            FluidStack fluid = currentFuel.liquidFuel;
            ILiquidFuel fuelHandler = EmbersAPI.getSteamEngineFuel(fluid);
            powerGenerated = Misc.getDiminishedPower(fuelHandler.getPower(fluid), MAX_POWER, 1);
        }

        if (currentFuel.isSolid()) { //Generate solid power
            FluidStack fluid = tank.getFluid();
            if (tank.getFluidAmount() >= NORMAL_FLUID_CONSUMPTION && fluid != null && fluid.getFluid() == FluidRegistry.WATER) {
                if (!world.isRemote) {
                    tank.drain(NORMAL_FLUID_CONSUMPTION, true);
                    powerGenerated = SOLID_POWER;
                    dirty = true;
                }
            } else {
                currentFuel.reset(); //Waste the rest of the fuel
            }
        }

        if(dirty)
            markDirty();

        if (!world.isRemote && capability.getPower(null) != powerGenerated) {
            capability.setPower(powerGenerated, null);
            updateNearby();
        }
    }

    private void spawnParticles() {
        if (currentFuel.isEmpty())
            return;
        boolean vapor = currentFuel.isLiquid();
        for (int i = 0; i < 4; i++) {
            float offX = 0.09375f + 0.8125f * (float) Misc.random.nextInt(2);
            float offZ = 0.28125f + 0.4375f * (float) Misc.random.nextInt(2);
            if (front.getAxis() == EnumFacing.Axis.X) {
                float h = offX;
                offX = offZ;
                offZ = h;
            }

            Color color = currentFuel.getColor();

            if (vapor)
                ParticleUtil.spawnParticleVapor(world,
                        getPos().getX() + offX, getPos().getY() + 1.0f, getPos().getZ() + offZ,
                        0.025f * (Misc.random.nextFloat() - 0.5f), 0.125f * (Misc.random.nextFloat()), 0.025f * (Misc.random.nextFloat() - 0.5f),
                        color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 255f, 0.5f, 2.0f + Misc.random.nextFloat(), 24);
            else
                ParticleUtil.spawnParticleSmoke(world,
                        getPos().getX() + offX, getPos().getY() + 1.0f, getPos().getZ() + offZ,
                        0.025f * (Misc.random.nextFloat() - 0.5f), 0.125f * (Misc.random.nextFloat()), 0.025f * (Misc.random.nextFloat() - 0.5f),
                        color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 255f, 2.0f + Misc.random.nextFloat(), 24);
        }
    }

    @Override
    public void playSound(int id) {
        float soundX = (float) pos.getX() + 0.5f;
        float soundY = (float) pos.getY() + 0.5f;
        float soundZ = (float) pos.getZ() + 0.5f;
        switch (id) {
            case SOUND_BURN:
                Embers.proxy.playMachineSound(this, SOUND_BURN, SoundManager.STEAM_ENGINE_LOOP_BURN, SoundCategory.BLOCKS, true, 1.0f, 1.0f, soundX, soundY, soundZ);
                world.playSound(soundX, soundY, soundZ, SoundManager.STEAM_ENGINE_START_BURN, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
                break;
            case SOUND_STEAM:
                Embers.proxy.playMachineSound(this, SOUND_STEAM, SoundManager.STEAM_ENGINE_LOOP_STEAM, SoundCategory.BLOCKS, true, 1.0f, 1.0f, soundX, soundY, soundZ);
                world.playSound(soundX, soundY, soundZ, SoundManager.STEAM_ENGINE_START_STEAM, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
                break;
        }
        soundsPlaying.add(id);
    }

    @Override
    public void stopSound(int id) {
        world.playSound((float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f, SoundManager.STEAM_ENGINE_STOP, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
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
        switch (id) {
            case SOUND_BURN:
                return currentFuel.isSolid();
            case SOUND_STEAM:
                return currentFuel.isLiquid();
        }
        return false;
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }

    @Override
    public void addCapabilityDescription(List<String> strings, Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            strings.add(IExtraCapabilityInformation.formatCapability(EnumIOType.INPUT, "embers.tooltip.goggles.item", I18n.format("embers.tooltip.goggles.item.fuel")));
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            strings.add(IExtraCapabilityInformation.formatCapability(EnumIOType.INPUT, "embers.tooltip.goggles.fluid", I18n.format("embers.tooltip.goggles.fluid.water_or_steam")));
    }
}
