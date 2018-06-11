package teamroots.embers.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
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
import teamroots.embers.EventManager;
import teamroots.embers.block.BlockAxle;
import teamroots.embers.block.BlockGearbox;
import teamroots.embers.item.ItemGear;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.power.DefaultMechCapability;
import teamroots.embers.power.MechCapabilityProvider;
import teamroots.embers.util.Misc;

public class TileEntityGearbox extends TileEntity implements ITileEntityBase {
	int ticksExisted = 0;
	EnumFacing from = null;
	public int connections = 0;
	public ItemStack[] gears = new ItemStack[]{
			ItemStack.EMPTY,
			ItemStack.EMPTY,
			ItemStack.EMPTY,
			ItemStack.EMPTY,
			ItemStack.EMPTY,
			ItemStack.EMPTY
	};
	
	public DefaultMechCapability capability = new DefaultMechCapability(){
		@Override
		public void onContentsChanged(){
			TileEntityGearbox box = TileEntityGearbox.this;
			box.updateNeighbors();
			box.markDirty();
		}

		@Override
		public double getPower(EnumFacing from) {
			if (from != null && gears[from.getIndex()].isEmpty()){
				return 0;
			}
			if (from == TileEntityGearbox.this.from || from == null){
				return super.getPower(from);
			}
			else {
				return super.getPower(from)/((double)(Math.max(1,connections)));
			}
		}

		@Override
		public void setPower(double value, EnumFacing from) {
			if (from != null && gears[from.getIndex()].isEmpty()){
				return;
			}
			if (from == TileEntityGearbox.this.from || from == null){
				super.setPower(value, from);
			}
			if (value != getPower(null)){
				onContentsChanged();
			}
		}
	};
	
	public void updateNeighbors(){
		IBlockState state = world.getBlockState(getPos());
		if (state.getBlock() instanceof BlockGearbox){
			from = state.getValue(BlockGearbox.facing);
			TileEntity t = world.getTileEntity(getPos().offset(from));
			if (t instanceof TileEntityAxle){
				((TileEntityAxle)t).updateNeighbors();
			}
		}
		connections = 0;
		List<EnumFacing> toUpdate = new ArrayList<EnumFacing>();
		for (EnumFacing f : EnumFacing.values()){
			if (f != null && f != from){
				TileEntity t = world.getTileEntity(getPos().offset(f));
				if (t != null && t.hasCapability(MechCapabilityProvider.mechCapability, Misc.getOppositeFace(f))){
					toUpdate.add(f);
				}
			}
		}
		for (EnumFacing f : toUpdate){
			if (!getGear(f).isEmpty()){
				connections ++;
			}
		}
		for (EnumFacing f : toUpdate){
			BlockPos p = getPos().offset(f);
			TileEntity t = world.getTileEntity(p);
			if (t instanceof TileEntityAxle && ((TileEntityAxle) t).front == null){
				IBlockState s = world.getBlockState(p);
				if (s.getBlock() instanceof BlockAxle){
					((TileEntityAxle)t).front = Misc.getOppositeFace(s.getValue(BlockAxle.facing));
					t.markDirty();
				}
			}
			if (!(t instanceof TileEntityAxle) || t instanceof TileEntityAxle && ((TileEntityAxle)t).front == f.getOpposite()){
				t.getCapability(MechCapabilityProvider.mechCapability, Misc.getOppositeFace(f)).setPower(capability.getPower(f),Misc.getOppositeFace(f));
				t.markDirty();	
			}
		}
	}
	
	public TileEntityGearbox(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		capability.writeToNBT(tag);
		if (from != null){
			tag.setInteger("from",from.getIndex());
		}
		for (int i = 0; i < 6; i ++){
			tag.setTag("gear"+i, gears[i].writeToNBT(new NBTTagCompound()));
		}
		tag.setInteger("connections", connections);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		capability.readFromNBT(tag);
		if (tag.hasKey("from")){
			from = EnumFacing.getFront(tag.getInteger("from"));
		}
		for (int i = 0; i < 6; i ++){
			gears[i] = new ItemStack(tag.getCompoundTag("gear"+i));
		}
		connections = tag.getInteger("connections");
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
		if (capability == MechCapabilityProvider.mechCapability){
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == MechCapabilityProvider.mechCapability){
			return (T)this.capability;
		}
		return super.getCapability(capability, facing);
	}
	
	public boolean dirty = false;
	
	@Override
	public void markForUpdate(){
		EventManager.markTEForUpdate(getPos(), this);
	}
	
	@Override
	public void markDirty(){
		markForUpdate();
		super.markDirty();
	}
	
	public ItemStack getGear(EnumFacing side){
		return gears[side.getIndex()];
	}
	
	public void setGear(EnumFacing side, ItemStack stack){
		gears[side.getIndex()] = stack;
		markDirty();
	}

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		if (!heldItem.isEmpty()){
			if (heldItem.getItem() instanceof ItemGear){
				if (getGear(side).isEmpty()){
					ItemStack gear = heldItem.copy();
					gear.setCount(1);
					this.setGear(side,gear);
					heldItem.shrink(1);
					if (heldItem.getCount() == 0){
						player.setHeldItem(hand, ItemStack.EMPTY);
					}
					capability.onContentsChanged();
					return true;
				}
			}
		}
		else {
			if (!getGear(side).isEmpty()){
				ItemStack gear = getGear(side);
				if (!world.isRemote){
					world.spawnEntity(new EntityItem(world,player.posX,player.posY+player.height/2.0f,player.posZ,gear));
				}
				setGear(side,ItemStack.EMPTY);
				capability.onContentsChanged();
				return true;
			}
		}
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		capability.setPower(0f,null);
		updateNeighbors();
		for (int i = 0; i < 6; i ++){
			if (!world.isRemote){
				world.spawnEntity(new EntityItem(world,pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5,gears[i]));
			}
		}
		world.setTileEntity(pos, null);
	}
}
