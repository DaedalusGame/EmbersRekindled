package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import teamroots.embers.RegistryManager;
import teamroots.embers.config.ConfigMachine;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.util.FluidColorHelper;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Random;

public class TileEntityTank extends TileEntityOpenTank implements ITileEntityBase, ITickable {
    public static int capacity = ConfigMachine.TANK_CATEGORY.capacity;


    public TileEntityTank() {
        super();
        tank = new FluidTank(capacity) {
            @Override
            public void onContentsChanged() {
                TileEntityTank.this.markDirty();
            }

            @Override
            public int fill(FluidStack resource, boolean doFill) {
                if (Misc.isGaseousFluid(resource)) {
                    setEscapedFluid(resource);
                    return resource.amount;
                }
                return super.fill(resource, doFill);
            }
        };
        tank.setTileEntity(this);
        tank.setCanFill(true);
        tank.setCanDrain(true);
    }

    @Override
    public void update() {
        if (world.isRemote && shouldEmitParticles())
            updateEscapeParticles();
    }

    @Override
    protected void updateEscapeParticles() {
        Color fluidColor = new Color(FluidColorHelper.getColor(lastEscaped), true);
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            float xOffset = 0.5f + (random.nextFloat() - 0.5f) * 2 * 0.2f;
            float yOffset = 0.9f;
            float zOffset = 0.5f + (random.nextFloat() - 0.5f) * 2 * 0.2f;

            ParticleUtil.spawnParticleVapor(world, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, 0, 1 / 20f, 0, fluidColor.getRed() / 255f, fluidColor.getGreen() / 255f, fluidColor.getBlue() / 255f, fluidColor.getAlpha() / 255f, 4, 2, 20);
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
        ItemStack heldItem = player.getHeldItem(hand);
        if (!heldItem.isEmpty()) {
            boolean didFill = FluidUtil.interactWithFluidHandler(player, hand, world, pos, side);
            this.markDirty();
            return didFill;
        }
        return false;
    }

    public int getCapacity() {
        return tank.getCapacity();
    }

    public int getAmount() {
        return tank.getFluidAmount();
    }

    public FluidTank getTank() {
        return tank;
    }

    public Fluid getFluid() {
        if (tank.getFluid() != null) {
            return tank.getFluid().getFluid();
        }
        return null;
    }

    public FluidStack getFluidStack() {
        return tank.getFluid();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        this.invalidate();
        if (!world.isRemote && (player == null || !player.capabilities.isCreativeMode)) {
            ItemStack toDrop = new ItemStack(RegistryManager.block_tank, 1);
            if (getTank().getFluidAmount() > 0) {
                NBTTagCompound tag = new NBTTagCompound();
                getTank().writeToNBT(tag);
                toDrop.setTagCompound(tag);
            }
            world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, toDrop));
        }
        world.setTileEntity(pos, null);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        Misc.syncTE(this);
    }
}
