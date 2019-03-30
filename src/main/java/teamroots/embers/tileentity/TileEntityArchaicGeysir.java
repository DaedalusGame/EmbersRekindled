package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Random;

public class TileEntityArchaicGeysir extends TileEntityOpenTank implements ITileEntityBase, ITickable, IMultiblockMachine {
	int ticksExisted = 0;
	long charge = 0;

	@Override
	public AxisAlignedBB getRenderBoundingBox(){
		return super.getRenderBoundingBox().expand(4.0, 256.0, 4.0);
	}

	public TileEntityArchaicGeysir(){
		super();
		tank = new FluidTank(Integer.MAX_VALUE){
			@Override
			public void onContentsChanged(){
				TileEntityArchaicGeysir.this.markDirty();
			}

			@Override
			public int fill(FluidStack resource, boolean doFill) {
				if(Misc.isGaseousFluid(resource)) {
					setEscapedFluid(resource);
					return resource.amount;
				}
				return 0;
			}
		};
		tank.setTileEntity(this);
		tank.setCanFill(true);
		tank.setCanDrain(false);
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

	@Override
	public void update() {
		ticksExisted ++;
		if (world.isRemote && true)
			updateEscapeParticles();
		lastEscapedTickServer = lastEscapedTickClient = world.getTotalWorldTime();
	}

	@Override
	protected void updateEscapeParticles() {
		Color fluidColor = new Color(99,100,135);
		Random random = new Random();
		float force = 0.5f;
		for (int i = 0; i < 15; i++) {
			float xOffset = 0.5f + (random.nextFloat() - 0.5f) * 2 * 0.6f * force;
			float yOffset = 1.1f+0.4f*force;
			float zOffset = 0.5f + (random.nextFloat() - 0.5f) * 2 * 0.6f * force;

			double angle = random.nextDouble()*2*Math.PI;
			float velocity = random.nextFloat()*0.2f;

			velocity *= force;
			float xVel = (float)Math.sin(angle)*velocity;
			float yVel = velocity*5.0f;
			float zVel = (float)Math.cos(angle)*velocity;

			ParticleUtil.spawnParticleVapor(world, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, xVel, yVel, zVel, fluidColor.getRed() / 255f, fluidColor.getGreen() / 255f, fluidColor.getBlue() / 255f, fluidColor.getAlpha() / 255f, 6*force, 20*force, 40);
		}
	}

	@Override
	public void markDirty() {
		super.markDirty();
		Misc.syncTE(this);
	}
}
