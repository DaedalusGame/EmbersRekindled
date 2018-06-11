package teamroots.embers.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.EventManager;
import teamroots.embers.block.BlockItemTransfer;
import teamroots.embers.block.BlockVacuum;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.util.Misc;

public class TileEntityItemVacuum extends TileEntity implements ITileEntityBase, ITickable, IPressurizable, IItemPipePriority {
	double angle = 0;
	double turnRate = 1;
	public ItemStackHandler inventory = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
        	TileEntityItemVacuum.this.markDirty();
        }
	};
	public BlockPos lastReceived = new BlockPos(0,0,0);
	public int pressure = 15;
	Random random = new Random();
	
	public TileEntityItemVacuum(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setTag("inventory", inventory.serializeNBT());
		tag.setInteger("lastX", this.lastReceived.getX());
		tag.setInteger("lastY", this.lastReceived.getY());
		tag.setInteger("lastZ", this.lastReceived.getZ());
		tag.setInteger("pressure", pressure);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		lastReceived = new BlockPos(tag.getInteger("lastX"),tag.getInteger("lastY"),tag.getInteger("lastZ"));
		pressure = tag.getInteger("pressure");
		inventory.deserializeNBT(tag.getCompoundTag("inventory"));
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
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			IBlockState state = getWorld().getBlockState(getPos());
			if (facing == state.getValue(BlockItemTransfer.facing) || facing == Misc.getOppositeFace(state.getValue(BlockItemTransfer.facing))){
				return true;
			}
			return false;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			IBlockState state = getWorld().getBlockState(getPos());
			if (facing == state.getValue(BlockItemTransfer.facing) || facing == Misc.getOppositeFace(state.getValue(BlockItemTransfer.facing))){
				return (T)this.inventory;
			}
			return null;
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
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, inventory);
		world.setTileEntity(pos, null);
	}

	@Override
	public void update() {
		IBlockState state = getWorld().getBlockState(getPos());
		ArrayList<BlockPos> toUpdate = new ArrayList<BlockPos>();
		ArrayList<EnumFacing> connections = new ArrayList<EnumFacing>();
		if (world.isBlockPowered(getPos())){
			EnumFacing face = state.getValue(BlockVacuum.facing);
			Vec3i vec = face.getDirectionVec();
			List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(getPos().getX()-6+vec.getX()*6,getPos().getY()-6+vec.getY()*6,getPos().getZ()-6+vec.getZ()*6,getPos().getX()+7+vec.getX()*6,getPos().getY()+7+vec.getY()*6,getPos().getZ()+7+vec.getZ()*6));
			if (items.size() > 0){
				for (EntityItem item : items){
					Vec3d v = new Vec3d(item.posX-(this.getPos().getX()+0.5),item.posY-(this.getPos().getY()+0.5),item.posZ-(this.getPos().getZ()+0.5));
					v.normalize();
					item.motionX = (-v.x*0.25*0.2f + item.motionX*0.8f);
					item.motionY = (-v.y*0.25*0.2f + item.motionY*0.8f);
					item.motionZ = (-v.z*0.25*0.2f + item.motionZ*0.8f);
				}
			}
			List<EntityItem> nearestItems = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(getPos().getX()-0.25,getPos().getY()-0.25,getPos().getZ()-0.25,getPos().getX()+1.25,getPos().getY()+1.25,getPos().getZ()+1.25));
			if (nearestItems.size() > 0){
				for (EntityItem item : nearestItems){
					ItemStack stack = inventory.insertItem(0, item.getItem(), true);
					if (stack.getItem() == item.getItem().getItem() && stack.getItemDamage() == item.getItem().getItemDamage() && stack.getCount() < item.getItem().getCount() || stack == ItemStack.EMPTY){
						item.setItem(inventory.insertItem(0, item.getItem(), false));
						if (item.getItem().isEmpty()){
							world.removeEntity(item);
						}
						if (!toUpdate.contains(getPos())){
							toUpdate.add(getPos());
						}
					}
				}
			}
		}
		connections.add(state.getValue(BlockVacuum.facing));
		connections.add(Misc.getOppositeFace(state.getValue(BlockVacuum.facing)));
		if (connections.size() > 0){
			for (int i = 0; i < 1; i ++){
				if (!inventory.getStackInSlot(0).isEmpty()){
					EnumFacing face = connections.get(random.nextInt(connections.size()));
					TileEntity tile = getWorld().getTileEntity(getPos().offset(face));
					if (tile != null){
						IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite());
						if (handler != null){
							ItemStack passStack = new ItemStack(inventory.getStackInSlot(0).getItem(),1,inventory.getStackInSlot(0).getMetadata());
							if (inventory.getStackInSlot(0).hasTagCompound()){
								passStack.setTagCompound(inventory.getStackInSlot(0).getTagCompound());
							}
							int slot = -1;
							for (int j = 0; j < handler.getSlots() && slot == -1; j ++){
								if (handler.getStackInSlot(j).isEmpty()){
									slot = j;
								}
								else {
									if (handler.getStackInSlot(j).getCount() < handler.getSlotLimit(j) && ItemStack.areItemsEqual(handler.getStackInSlot(j), inventory.getStackInSlot(0)) && ItemStack.areItemStackTagsEqual(handler.getStackInSlot(j), inventory.getStackInSlot(0))){
										slot = j;
									}
								}
							}
							if (slot != -1){
								ItemStack added = handler.insertItem(slot, passStack, false);
								if (added.isEmpty()){
									ItemStack extracted = this.inventory.extractItem(0, 1, false);
									if (!extracted.isEmpty()){
										if (tile instanceof TileEntityItemPipe){
											((TileEntityItemPipe)tile).lastReceived = getPos();
										}
										if (!toUpdate.contains(getPos().offset(face))){
											toUpdate.add(getPos().offset(face));
										}
										if (!toUpdate.contains(getPos())){
											toUpdate.add(getPos());
										}
									}
								}
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < toUpdate.size(); i ++){
			TileEntity tile = getWorld().getTileEntity(toUpdate.get(i));
			tile.markDirty();
			if (!getWorld().isRemote && !(tile instanceof ITileEntityBase)){
				tile.markDirty();
				EventManager.markTEForUpdate(toUpdate.get(i),tile);
			}
		}
	}

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public int getPressure() {
		return 15;
	}

	@Override
	public void setPressure(int pressure) {
		//
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
}
