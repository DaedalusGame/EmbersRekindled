package teamroots.embers.tileentity;

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
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.EventManager;
import teamroots.embers.RegistryManager;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.util.Misc;

public class TileEntityCinderPlinth extends TileEntity implements ITileEntityBase, ITickable {
	public IEmberCapability capability = new DefaultEmberCapability();
	int angle = 0;
	int turnRate = 0;
	int progress = 0;
	public ItemStackHandler inventory = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
        	TileEntityCinderPlinth.this.markDirty();
        }
	};
	Random random = new Random();
	
	public TileEntityCinderPlinth(){
		super();
		capability.setEmberCapacity(24000);
		capability.setEmber(0);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		capability.writeToNBT(tag);
		tag.setInteger("progress", 0);
		tag.setTag("inventory", inventory.serializeNBT());
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		capability.readFromNBT(tag);
		progress = tag.getInteger("progress");
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
			return true;
		}
		if (capability == EmberCapabilityProvider.emberCapability){
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return (T)this.inventory;
		}
		if (capability == EmberCapabilityProvider.emberCapability){
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

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		if (!heldItem.isEmpty()){
			player.setHeldItem(hand, this.inventory.insertItem(0,heldItem,false));
			markDirty();
			return true;
		}
		else {
			if (!inventory.getStackInSlot(0).isEmpty()){
				if (!getWorld().isRemote){
					player.setHeldItem(hand, inventory.extractItem(0, inventory.getStackInSlot(0).getCount(), false));
					markDirty();
				}
				return true;
			}
		}
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
		turnRate = 1;
		if (!inventory.getStackInSlot(0).isEmpty() && capability.getEmber() > 0){
			progress ++;
			if (getWorld().isRemote){
				ParticleUtil.spawnParticleSmoke(getWorld(), (float)getPos().getX()+0.5f, (float)getPos().getY()+0.875f, (float)getPos().getZ()+0.5f, 0.0125f*(random.nextFloat()-0.5f), 0.05f*(random.nextFloat()+1.0f), 0.0125f*(random.nextFloat()-0.5f), 72, 72, 72, 1.0f, 3.0f+random.nextFloat(), 48);
			}
			capability.removeAmount(0.5, true);
			if (progress > 40){
				progress = 0;
				inventory.extractItem(0, 1, false);
				TileEntity tile = getWorld().getTileEntity(getPos().down());
				boolean doSpawn = true;
				if (tile instanceof TileEntityBin){
					if (((TileEntityBin) tile).inventory.getStackInSlot(0).isEmpty()){
						((TileEntityBin) tile).inventory.insertItem(0, new ItemStack(RegistryManager.dust_ash,1), false);
						tile.markDirty();
						doSpawn = false;
					}
					else if (((TileEntityBin) tile).inventory.getStackInSlot(0).getItem() == RegistryManager.dust_ash && ((TileEntityBin) tile).inventory.getStackInSlot(0).getCount() < 64){
						((TileEntityBin) tile).inventory.insertItem(0, new ItemStack(RegistryManager.dust_ash,1), false);
						tile.markDirty();
						doSpawn = false;
					}
				}
				if (doSpawn && !getWorld().isRemote){
					getWorld().spawnEntity(new EntityItem(getWorld(),getPos().getX()+0.5,getPos().getY()+1.0,getPos().getZ()+0.5,new ItemStack(RegistryManager.dust_ash,1)));
				}
			}
			markDirty();
		}
		else {
			if (progress != 0){
				progress = 0;
				markDirty();
			}
		}
		angle += turnRate;
	}
}
