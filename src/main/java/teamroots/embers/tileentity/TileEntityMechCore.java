package teamroots.embers.tileentity;

import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityMechCore extends TileEntity implements ITileEntityBase {
	Random random = new Random();
	
	public TileEntityMechCore(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		return super.writeToNBT(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
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
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		
		if (getWorld().getTileEntity(getPos().offset(EnumFacing.DOWN)) instanceof IMultiblockMachine){
			return getWorld().getTileEntity(getPos().offset(EnumFacing.DOWN)).hasCapability(capability, facing);
		}
		if (getWorld().getTileEntity(getPos().offset(EnumFacing.UP)) instanceof IMultiblockMachine){
			return getWorld().getTileEntity(getPos().offset(EnumFacing.UP)).hasCapability(capability, facing);
		}
		if (getWorld().getTileEntity(getPos().offset(EnumFacing.WEST)) instanceof IMultiblockMachine){
			return getWorld().getTileEntity(getPos().offset(EnumFacing.WEST)).hasCapability(capability, facing);
		}
		if (getWorld().getTileEntity(getPos().offset(EnumFacing.EAST)) instanceof IMultiblockMachine){
			return getWorld().getTileEntity(getPos().offset(EnumFacing.EAST)).hasCapability(capability, facing);
		}
		if (getWorld().getTileEntity(getPos().offset(EnumFacing.NORTH)) instanceof IMultiblockMachine){
			return getWorld().getTileEntity(getPos().offset(EnumFacing.NORTH)).hasCapability(capability, facing);
		}
		if (getWorld().getTileEntity(getPos().offset(EnumFacing.SOUTH)) instanceof IMultiblockMachine){
			return getWorld().getTileEntity(getPos().offset(EnumFacing.SOUTH)).hasCapability(capability, facing);
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		ArrayList<EnumFacing> faces = new ArrayList<EnumFacing>();
		
		if (getWorld().getTileEntity(getPos().offset(EnumFacing.DOWN)) instanceof IMultiblockMachine){
			faces.add(EnumFacing.DOWN);
		}
		if (getWorld().getTileEntity(getPos().offset(EnumFacing.UP)) instanceof IMultiblockMachine){
			faces.add(EnumFacing.UP);
		}
		if (getWorld().getTileEntity(getPos().offset(EnumFacing.WEST)) instanceof IMultiblockMachine){
			faces.add(EnumFacing.WEST);
		}
		if (getWorld().getTileEntity(getPos().offset(EnumFacing.EAST)) instanceof IMultiblockMachine){
			faces.add(EnumFacing.EAST);
		}
		if (getWorld().getTileEntity(getPos().offset(EnumFacing.NORTH)) instanceof IMultiblockMachine){
			faces.add(EnumFacing.NORTH);
		}
		if (getWorld().getTileEntity(getPos().offset(EnumFacing.SOUTH)) instanceof IMultiblockMachine){
			faces.add(EnumFacing.SOUTH);
		}
		
		if (faces.size() > 0){
			return getWorld().getTileEntity(getPos().offset(faces.get(random.nextInt(faces.size())))).getCapability(capability, facing);
		}
		return super.getCapability(capability, facing);
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
}
