package teamroots.embers.tileentity;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.block.BlockInfernoForge;
import teamroots.embers.block.BlockInfernoForgeEdge;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.util.Misc;

public class TileEntityInfernoForgeOpening extends TileEntity implements ITileEntityBase, ITickable {
	public boolean isOpen = false;
	public boolean prevState = false;
	public float openAmount = 0.0f;
	
	public TileEntityInfernoForgeOpening(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setBoolean("isOpen", isOpen);
		tag.setBoolean("prevState", prevState);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		isOpen = tag.getBoolean("isOpen");
		prevState = tag.getBoolean("prevState");
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
	
	public boolean dirty = false;
	
	@Override
	public void markForUpdate(){
		dirty = true;
	}
	
	@Override
	public boolean needsUpdate(){
		return dirty;
	}
	
	@Override
	public void clean(){
		dirty = false;
	}
	
	@Override
	public void markDirty(){
		markForUpdate();
		super.markDirty();
	}

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!player.isSneaking()){
			prevState = isOpen;
			isOpen = !isOpen;
			markDirty();
			return true;
		}
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		BlockInfernoForgeEdge.breakBlockSafe(world, pos.down(), player);
		world.setTileEntity(pos, null);
	}

	@Override
	public void update() {
		if (isOpen && !prevState && this.openAmount < 1.0f){
			openAmount = 0.5f+0.5f*openAmount;
			if (openAmount > 0.99f){
				openAmount = 1.0f;
				prevState = isOpen;
			}
		}
		if (!isOpen && prevState && this.openAmount > 0.0f){
			openAmount = 0.5f*openAmount;
			if (openAmount < 0.01f){
				openAmount = 0.0f;
				prevState = isOpen;
			}
		}
	}
}
