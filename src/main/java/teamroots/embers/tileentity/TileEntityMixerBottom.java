package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import teamroots.embers.Embers;
import teamroots.embers.EventManager;
import teamroots.embers.SoundManager;
import teamroots.embers.api.tile.IExtraDialInformation;
import teamroots.embers.api.upgrades.IUpgradeProvider;
import teamroots.embers.api.upgrades.UpgradeUtil;
import teamroots.embers.block.BlockFluidGauge;
import teamroots.embers.recipe.FluidMixingRecipe;
import teamroots.embers.recipe.RecipeRegistry;
import teamroots.embers.util.Misc;
import teamroots.embers.util.sound.ISoundController;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class TileEntityMixerBottom extends TileEntity implements ITileEntityBase, ITickable, ISoundController, IExtraDialInformation {
    public static final double EMBER_COST = 2.0;

    public FluidTank north = new FluidTank(8000);
    public FluidTank south = new FluidTank(8000);
    public FluidTank east = new FluidTank(8000);
    public FluidTank west = new FluidTank(8000);
    public FluidTank[] tanks;
    Random random = new Random();
    int progress = -1;
    boolean isWorking;

    public static final int SOUND_PROCESS = 1;
    public static final int[] SOUND_IDS = new int[]{SOUND_PROCESS};

    HashSet<Integer> soundsPlaying = new HashSet<>();

    public TileEntityMixerBottom() {
        super();
        tanks = new FluidTank[]{north, south, east, west};
    }

    public FluidTank[] getTanks() {
        return tanks;
    }

    public ArrayList<FluidStack> getFluids() {
        ArrayList<FluidStack> fluids = new ArrayList<>();
        if (north.getFluid() != null) {
            fluids.add(north.getFluid());
        }
        if (south.getFluid() != null) {
            fluids.add(south.getFluid());
        }
        if (east.getFluid() != null) {
            fluids.add(east.getFluid());
        }
        if (west.getFluid() != null) {
            fluids.add(west.getFluid());
        }
        return fluids;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        NBTTagCompound northTank = new NBTTagCompound();
        north.writeToNBT(northTank);
        tag.setTag("northTank", northTank);
        NBTTagCompound southTank = new NBTTagCompound();
        south.writeToNBT(southTank);
        tag.setTag("southTank", southTank);
        NBTTagCompound eastTank = new NBTTagCompound();
        east.writeToNBT(eastTank);
        tag.setTag("eastTank", eastTank);
        NBTTagCompound westTank = new NBTTagCompound();
        west.writeToNBT(westTank);
        tag.setTag("westTank", westTank);
        tag.setInteger("progress", progress);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        north.readFromNBT(tag.getCompoundTag("northTank"));
        south.readFromNBT(tag.getCompoundTag("southTank"));
        east.readFromNBT(tag.getCompoundTag("eastTank"));
        west.readFromNBT(tag.getCompoundTag("westTank"));
        if (tag.hasKey("progress")) {
            progress = tag.getInteger("progress");
        }
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
        world.setTileEntity(pos, null);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != EnumFacing.UP && facing != EnumFacing.DOWN && facing != null) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            switch (facing) {
                case DOWN:
                    break;
                case EAST:
                    return (T) east;
                case NORTH:
                    return (T) north;
                case SOUTH:
                    return (T) south;
                case UP:
                    break;
                case WEST:
                    return (T) west;
            }
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        if (getWorld().isRemote)
            handleSound();
        World world = getWorld();
        BlockPos pos = getPos();
        TileEntityMixerTop top = (TileEntityMixerTop) world.getTileEntity(pos.up());
        isWorking = false;
        if (top != null) {
            List<IUpgradeProvider> upgrades = UpgradeUtil.getUpgrades(world, pos.up(), EnumFacing.VALUES);
            UpgradeUtil.verifyUpgrades(this, upgrades);
            if (UpgradeUtil.doTick(this, upgrades))
                return;
            double emberCost = UpgradeUtil.getTotalEmberConsumption(this, EMBER_COST, upgrades);
            if (top.capability.getEmber() >= emberCost) {
                ArrayList<FluidStack> fluids = getFluids();
                FluidMixingRecipe recipe = RecipeRegistry.getMixingRecipe(fluids);
                if (recipe != null) {
                    boolean cancel = UpgradeUtil.doWork(this, upgrades);
                    if(!cancel) {
                        IFluidHandler tank = top.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
                        FluidStack output = recipe.getResult(fluids);
                        output = UpgradeUtil.transformOutput(this, output, upgrades);
                        int amount = tank.fill(output, false);
                        if (amount != 0) {
                            isWorking = true;
                            tank.fill(output, true);
                            consumeFluids(recipe);
                            top.capability.removeAmount(emberCost, true);
                            markDirty();
                            top.markDirty();
                        }
                    }
                }
            }
        }
    }

    public void consumeFluids(FluidMixingRecipe recipe) {
        for (int j = 0; j < recipe.inputs.size(); j++) {
            FluidStack recipeFluid = recipe.inputs.get(j).copy();
            for (FluidTank tank : tanks) {
                FluidStack tankFluid = tank.getFluid();
                if (recipeFluid != null && tankFluid != null && recipeFluid.getFluid() == tankFluid.getFluid()) {
                    FluidStack stack = tank.drain(recipeFluid.amount, true);
                    recipeFluid.amount -= stack != null ? stack.amount : 0;
                }
            }
        }
    }

    @Override
    public void playSound(int id) {
        switch (id) {
            case SOUND_PROCESS:
                Embers.proxy.playMachineSound(this, SOUND_PROCESS, SoundManager.MIXER_LOOP, SoundCategory.BLOCKS, true, 1.0f, 1.0f, (float) pos.getX() + 0.5f, (float) pos.getY() + 1.0f, (float) pos.getZ() + 0.5f);
                break;
        }
        soundsPlaying.add(id);
    }

    @Override
    public void stopSound(int id) {
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
        return id == SOUND_PROCESS && isWorking;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        Misc.syncTE(this);
    }

    @Override
    public void addDialInformation(EnumFacing facing, List<String> information, String dialType) {
        if(BlockFluidGauge.DIAL_TYPE.equals(dialType)) {
            information.clear();
            information.add(TextFormatting.BOLD.toString()+I18n.format("embers.tooltip.side.north")+TextFormatting.RESET.toString()+" "+BlockFluidGauge.formatFluidStack(north.getFluid(),north.getCapacity()));
            information.add(TextFormatting.BOLD.toString()+I18n.format("embers.tooltip.side.east")+TextFormatting.RESET.toString()+" "+BlockFluidGauge.formatFluidStack(east.getFluid(),east.getCapacity()));
            information.add(TextFormatting.BOLD.toString()+I18n.format("embers.tooltip.side.south")+TextFormatting.RESET.toString()+" "+BlockFluidGauge.formatFluidStack(south.getFluid(),south.getCapacity()));
            information.add(TextFormatting.BOLD.toString()+I18n.format("embers.tooltip.side.west")+TextFormatting.RESET.toString()+" "+BlockFluidGauge.formatFluidStack(west.getFluid(),south.getCapacity()));
        }
    }
}
