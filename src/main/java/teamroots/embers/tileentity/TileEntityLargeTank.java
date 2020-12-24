package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import teamroots.embers.ConfigManager;
import teamroots.embers.RegistryManager;
import teamroots.embers.block.BlockStoneEdge;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.util.FluidColorHelper;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Random;

public class TileEntityLargeTank extends TileEntityOpenTank implements ITileEntityBase, ITickable, IMultiblockMachine {
	int ticksExisted = 0;
	
	@Override
	public AxisAlignedBB getRenderBoundingBox(){
		return super.getRenderBoundingBox().expand(4.0, 256.0, 4.0);
	}
	
	public TileEntityLargeTank(){
		super();
		tank = new FluidTank(Integer.MAX_VALUE){
			@Override
			public void onContentsChanged(){
				TileEntityLargeTank.this.markDirty();
			}

			@Override
			public int fill(FluidStack resource, boolean doFill) {
				if(Misc.isGaseousFluid(resource)) {
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
		if (!heldItem.isEmpty()){
			boolean didFill = FluidUtil.interactWithFluidHandler(player, hand, this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side));
			if (didFill) {
				this.markDirty();
				return true;
			}
		}
		return false;
	}
	
	public int getCapacity(){
		return tank.getCapacity();
	}
	
	public int getAmount(){
		return tank.getFluidAmount();
	}
	
	public Fluid getFluid(){
		if (tank.getFluid() != null){
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
		world.setBlockToAir(pos.add(1,0,0));
		world.setBlockToAir(pos.add(0,0,1));
		world.setBlockToAir(pos.add(-1,0,0));
		world.setBlockToAir(pos.add(0,0,-1));
		world.setBlockToAir(pos.add(1,0,-1));
		world.setBlockToAir(pos.add(-1,0,1));
		world.setBlockToAir(pos.add(1,0,1));
		world.setBlockToAir(pos.add(-1,0,-1));
		world.setTileEntity(pos, null);
	}
	
	public void updateCapacity(){
		int capacity = 0;
		for (int i = 1; isReservoirPart(getPos().add(0, i, 0)); i++){
			capacity += ConfigManager.reservoirCapacity;
		}
		if(tank.getCapacity() != capacity) {
			this.tank.setCapacity(capacity);
			int amount = tank.getFluidAmount();
			if (amount > capacity) {
				tank.drain(amount - capacity,true);
			}
			markDirty();
		}
	}

	protected boolean isReservoirPart(BlockPos pos) {
		IBlockState state = getWorld().getBlockState(pos);
		return state.getBlock() instanceof BlockStoneEdge && state.getValue(BlockStoneEdge.state) == 8;
	}

	@Override
	public void update() {
		ticksExisted ++;
		if (ticksExisted % 20 == 0){
			updateCapacity();
		}
		if (world.isRemote && shouldEmitParticles())
			updateEscapeParticles();
	}

	@Override
	protected void updateEscapeParticles() {
		Color fluidColor = new Color(FluidColorHelper.getColor(lastEscaped), true);
		Random random = new Random();
		int height = getCapacity() / ConfigManager.reservoirCapacity;
		for (int i = 0; i < 3; i++) {
			float xOffset = 0.5f + (random.nextFloat() - 0.5f) * 2 * 0.6f;
			float yOffset = height+0.9f;
			float zOffset = 0.5f + (random.nextFloat() - 0.5f) * 2 * 0.6f;

			ParticleUtil.spawnParticleVapor(world, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, 0, 1 / 20f, 0, fluidColor.getRed() / 255f, fluidColor.getGreen() / 255f, fluidColor.getBlue() / 255f, fluidColor.getAlpha() / 255f, 8, 6, 50);
		}
	}

	@Override
	public void markDirty() {
		super.markDirty();
		Misc.syncTE(this);
	}
}
