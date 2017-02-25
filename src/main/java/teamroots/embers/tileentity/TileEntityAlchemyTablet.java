package teamroots.embers.tileentity;

import java.util.ArrayList;
import java.util.Comparator;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.RegistryManager;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageEmberSphereFX;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.recipe.AlchemyRecipe;
import teamroots.embers.recipe.RecipeRegistry;
import teamroots.embers.util.Misc;

public class TileEntityAlchemyTablet extends TileEntity implements ITileEntityBase, ITickable {
	public IEmberCapability capability = new DefaultEmberCapability();
	int angle = 0;
	int turnRate = 0;
	public int progress = 0;
	int ash = 0;
	public int process = 0;
	int copper = 0, iron = 0, dawnstone = 0, silver = 0, lead = 0;
	public ItemStackHandler north = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
        	TileEntityAlchemyTablet.this.markDirty();
        }
	};
	public ItemStackHandler south = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
        	TileEntityAlchemyTablet.this.markDirty();
        }
	};
	public ItemStackHandler east = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
        	TileEntityAlchemyTablet.this.markDirty();
        }
	};
	public ItemStackHandler west = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
        	TileEntityAlchemyTablet.this.markDirty();
        }
	};
	public ItemStackHandler center = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
        	TileEntityAlchemyTablet.this.markDirty();
        }
	};
	Random random = new Random();
	
	public TileEntityAlchemyTablet(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setInteger("progress", progress);
		tag.setInteger("iron", iron);
		tag.setInteger("dawnstone", dawnstone);
		tag.setInteger("copper", copper);
		tag.setInteger("silver", silver);
		tag.setInteger("lead", lead);
		tag.setTag("north", north.serializeNBT());
		tag.setTag("south", south.serializeNBT());
		tag.setTag("east", east.serializeNBT());
		tag.setTag("west", west.serializeNBT());
		tag.setTag("center", center.serializeNBT());
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		progress = tag.getInteger("progress");
		iron = tag.getInteger("iron");
		dawnstone = tag.getInteger("dawnstone");
		copper = tag.getInteger("copper");
		silver = tag.getInteger("silver");
		lead = tag.getInteger("lead");
		north.deserializeNBT(tag.getCompoundTag("north"));
		south.deserializeNBT(tag.getCompoundTag("south"));
		east.deserializeNBT(tag.getCompoundTag("east"));
		west.deserializeNBT(tag.getCompoundTag("west"));
		center.deserializeNBT(tag.getCompoundTag("center"));
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
			if (facing != EnumFacing.UP){
				return true;
			}
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			if (facing == EnumFacing.DOWN){
				return (T)center;
			}
			if (facing == EnumFacing.NORTH){
				return (T)north;
			}
			if (facing == EnumFacing.SOUTH){
				return (T)south;
			}
			if (facing == EnumFacing.EAST){
				return (T)east;
			}
			if (facing == EnumFacing.WEST){
				return (T)west;
			}
		}
		return super.getCapability(capability, facing);
	}
	
	public int getSlotForPos(float hitX, float hitZ){
		return ((int)(hitX/0.3333))*3 + ((int)(hitZ/0.3333));
	}
	
	public ItemStackHandler getInventoryForFace(EnumFacing facing){
		if (facing == EnumFacing.DOWN){
			return center;
		}
		if (facing == EnumFacing.NORTH){
			return north;
		}
		if (facing == EnumFacing.SOUTH){
			return south;
		}
		if (facing == EnumFacing.EAST){
			return east;
		}
		if (facing == EnumFacing.WEST){
			return west;
		}
		return center;
	}
	
	public void sparkProgress(){
		AlchemyRecipe recipe = RecipeRegistry.getAlchemyRecipe(center.getStackInSlot(0), east.getStackInSlot(0), west.getStackInSlot(0), north.getStackInSlot(0), south.getStackInSlot(0));
		if (recipe != null){
			if (getNearbyAsh(getNearbyPedestals()) >= recipe.dawnstoneAspectMin+recipe.copperAspectMin+recipe.ironAspectMin+recipe.silverAspectMin+recipe.leadAspectMin){
				this.progress = 1;
				markDirty();
			}
		}
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
		ItemStack heldItem = player.getHeldItem(hand);
		if (heldItem != ItemStack.EMPTY){
			player.setHeldItem(hand, getInventoryForFace(side).insertItem(0,heldItem,false));
			markDirty();
			return true;
		}
		else {
			if (getInventoryForFace(side).getStackInSlot(0) != ItemStack.EMPTY){
				if (!getWorld().isRemote){
					player.setHeldItem(hand, getInventoryForFace(side).extractItem(0, getInventoryForFace(side).getStackInSlot(0).getCount(), false));
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
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, north);
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, south);
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, east);
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, west);
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, center);
		world.setTileEntity(pos, null);
	}
	
	public List<TileEntityAlchemyPedestal> getNearbyPedestals(){
		ArrayList<TileEntityAlchemyPedestal> pedestals = new ArrayList<TileEntityAlchemyPedestal>();
		for (int i = -3; i < 4; i ++){
			for (int j = -3; j < 4; j ++){
				TileEntity tile = getWorld().getTileEntity(getPos().add(i,1,j));
				if (tile instanceof TileEntityAlchemyPedestal){
					pedestals.add((TileEntityAlchemyPedestal)tile);
				}
			}
		}
		return pedestals;
	}
	
	public int getNearbyAsh(List<TileEntityAlchemyPedestal> pedestals){
		int count = 0;
		for (TileEntityAlchemyPedestal pedestal : pedestals){
			if (pedestal.inventory.getStackInSlot(0) != ItemStack.EMPTY){
				count += pedestal.inventory.getStackInSlot(0).getCount();
			}
		}
		return count;
	}

	@Override
	public void update() {
		angle += 1.0f;
		if (progress == 1){
			if (process < 20){
				process ++;
			}
			List<TileEntityAlchemyPedestal> pedestals = getNearbyPedestals();
			if (getWorld().isRemote){
				for (int i = 0; i < pedestals.size(); i ++){
					ParticleUtil.spawnParticleStar(getWorld(), pedestals.get(i).getPos().getX()+0.5f, pedestals.get(i).getPos().getY()+1.0f, pedestals.get(i).getPos().getZ()+0.5f, 0.0125f*(random.nextFloat()-0.5f), 0.0125f*(random.nextFloat()-0.5f), 0.0125f*(random.nextFloat()-0.5f), 255, 64, 16, 3.5f+0.5f*random.nextFloat(), 40);
					for (int j = 0; j < 8; j ++){
						float coeff = random.nextFloat();
						float x = (getPos().getX()+0.5f)*coeff + (1.0f-coeff)*(pedestals.get(i).getPos().getX()+0.5f);
						float y = (getPos().getY()+0.875f)*coeff + (1.0f-coeff)*(pedestals.get(i).getPos().getY()+1.0f);
						float z = (getPos().getZ()+0.5f)*coeff + (1.0f-coeff)*(pedestals.get(i).getPos().getZ()+0.5f);
						ParticleUtil.spawnParticleGlow(getWorld(), x, y, z, 0.0125f*(random.nextFloat()-0.5f), 0.0125f*(random.nextFloat()-0.5f), 0.0125f*(random.nextFloat()-0.5f), 255, 64, 16, 2.0f, 24);
					}
				}
			}
			if (angle % 10 == 0){
				if (getNearbyAsh(pedestals) > 0){
					TileEntityAlchemyPedestal pedestal = pedestals.get(random.nextInt(pedestals.size()));
					while (pedestal.inventory.extractItem(0, 1, true) == ItemStack.EMPTY){
						pedestal = pedestals.get(random.nextInt(pedestals.size()));
					}
					if (pedestal.inventory.getStackInSlot(1) != ItemStack.EMPTY){
						if (getWorld().isRemote){
							for (int j = 0; j < 20; j ++){
								float dx = (getPos().getX()+0.5f) - (pedestal.getPos().getX()+0.5f);
								float dy = (getPos().getY()+0.875f) - (pedestal.getPos().getY()+1.0f);
								float dz = (getPos().getZ()+0.5f) - (pedestal.getPos().getZ()+0.5f);
								float lifetime = random.nextFloat()*24.0f+24.0f;
								ParticleUtil.spawnParticleStar(getWorld(), pedestal.getPos().getX()+0.5f, pedestal.getPos().getY()+1.0f, pedestal.getPos().getZ()+0.5f, dx/lifetime, dy/lifetime, dz/lifetime, 255, 64, 16, 4.0f, (int)lifetime);
							}
						}
						ItemStack stack = pedestal.inventory.extractItem(0, 1, false);
						if (pedestal.inventory.getStackInSlot(1).getItem() == RegistryManager.aspectus_iron){
							this.iron ++;
						}
						if (pedestal.inventory.getStackInSlot(1).getItem() == RegistryManager.aspectus_dawnstone){
							this.dawnstone ++;
						}
						if (pedestal.inventory.getStackInSlot(1).getItem() == RegistryManager.aspectus_copper){
							this.copper ++;
						}
						if (pedestal.inventory.getStackInSlot(1).getItem() == RegistryManager.aspectus_silver){
							this.silver ++;
						}
						if (pedestal.inventory.getStackInSlot(1).getItem() == RegistryManager.aspectus_lead){
							this.lead ++;
						}
						markDirty();
						pedestal.markDirty();
					}
				}
				else {
					AlchemyRecipe recipe = RecipeRegistry.getAlchemyRecipe(center.getStackInSlot(0), east.getStackInSlot(0), west.getStackInSlot(0), north.getStackInSlot(0), south.getStackInSlot(0));
					if (recipe != null && !getWorld().isRemote){
						ItemStack stack = recipe.getResult(getWorld(), iron, dawnstone, copper, silver, lead).copy();
						if (!getWorld().isRemote){
							getWorld().spawnEntity(new EntityItem(getWorld(),getPos().getX()+0.5,getPos().getY()+1.0f,getPos().getZ()+0.5,stack));
							PacketHandler.INSTANCE.sendToAll(new MessageEmberSphereFX(getPos().getX()+0.5,getPos().getY()+0.875,getPos().getZ()+0.5));
						}
						this.progress = 0;
			        	this.iron = 0;
			        	this.dawnstone = 0;
			        	this.copper = 0;
			        	this.silver = 0;
			        	this.lead = 0;
			        	this.center.setStackInSlot(0, decrStack(this.center.getStackInSlot(0)));
			        	this.north.setStackInSlot(0, decrStack(this.north.getStackInSlot(0)));
			        	this.south.setStackInSlot(0, decrStack(this.south.getStackInSlot(0)));
			        	this.east.setStackInSlot(0, decrStack(this.east.getStackInSlot(0)));
			        	this.west.setStackInSlot(0, decrStack(this.west.getStackInSlot(0)));
			        	markDirty();
					}
				}
			}
		}
		if (progress == 0){
			if (process > 0){
				process --;
			}
		}
	}
	
	public ItemStack decrStack(ItemStack stack){
		if (stack != ItemStack.EMPTY){
			stack.shrink(1);
			if (stack.getCount() == 0){
				return ItemStack.EMPTY;
			}
		}
		return stack;
	}
}
