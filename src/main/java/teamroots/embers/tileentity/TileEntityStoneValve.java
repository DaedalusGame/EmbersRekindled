package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.IItemHandler;
import teamroots.embers.block.BlockStoneValve;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityStoneValve extends TileEntity implements ITileEntityBase, IMultiblockMachine, ITickable {
    TileEntityLargeTank tank;
    IFluidHandler fluidHandler;
    //IItemHandler itemHandler;

    public TileEntityStoneValve() {
        fluidHandler = new IFluidHandler() {
            @Override
            public IFluidTankProperties[] getTankProperties() {
                if (tank != null)
                    return tank.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).getTankProperties();
                return new IFluidTankProperties[0];
            }

            @Override
            public int fill(FluidStack resource, boolean doFill) {
                if (tank != null)
                    return tank.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).fill(resource, doFill);
                return 0;
            }

            @Nullable
            @Override
            public FluidStack drain(FluidStack resource, boolean doDrain) {
                if (tank != null)
                    return tank.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).drain(resource, doDrain);
                return null;
            }

            @Nullable
            @Override
            public FluidStack drain(int amount, boolean doDrain) {
                if (tank != null)
                    return tank.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).drain(amount, doDrain);
                return null;
            }
        };
        /*itemHandler = new IItemHandler() {
            @Override
            public int getSlots() {
                return 1;
            }

            @Nonnull
            @Override
            public ItemStack getStackInSlot(int slot) {
                return ItemStack.EMPTY;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                return stack;
            }

            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return ItemStack.EMPTY;
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };*/
    }

    public TileEntityLargeTank getTank() {
        return tank;
    }

    public EnumFacing getFacing() {
        IBlockState state = world.getBlockState(pos);
        switch (state.getValue(BlockStoneValve.state)) {
            case (2):
                return EnumFacing.WEST;
            case (4):
                return EnumFacing.SOUTH;
            case (6):
                return EnumFacing.EAST;
            case (9):
                return EnumFacing.NORTH;
            default:
                return null;
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && (facing == null || facing == getFacing()))
            return true;
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T) fluidHandler;
        return super.getCapability(capability, facing);
    }

    public void updateTank() {
        if (isInvalid())
            return;
        BlockStoneValve valve = (BlockStoneValve) getBlockType();
        BlockPos basePos = valve.getMainBlock(world, pos);
        boolean foundBlock = false;
        for (int i = 0; i < 64 && !foundBlock; i++) {
            TileEntity tile = world.getTileEntity(basePos.add(0, -i, 0));
            if (tile instanceof TileEntityLargeTank) {
                tank = (TileEntityLargeTank) tile;
                foundBlock = true;
            }
        }
    }

    @Override
    public void update() {
        if (tank == null || tank.isInvalid())
            updateTank();
    }

    @Override
    public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
            return true;
        if (tank != null)
            return tank.activate(world, tank.getPos(), world.getBlockState(tank.getPos()), player, hand, side, hitX, hitY, hitZ);
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {

    }
}
