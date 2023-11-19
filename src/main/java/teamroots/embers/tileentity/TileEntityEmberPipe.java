package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.SoundManager;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.item.ItemTinkerHammer;
import teamroots.embers.util.EnumPipeConnection;
import teamroots.embers.util.Misc;

public class TileEntityEmberPipe extends TileEntityEmberPipeBase {
	EnumPipeConnection[] connections = new EnumPipeConnection[EnumFacing.VALUES.length];
	boolean syncConnections;
	boolean active;

	double currentPush;

	public static final double INIT_PUSH = 10;
	public static final double INCREMENT_PUSH = 10;

	public TileEntityEmberPipe() {
		super();
	}

	public void updateNeighbors(IBlockAccess world) {
		for (EnumFacing facing : EnumFacing.VALUES) {
			setInternalConnection(facing, getConnection(world, getPos().offset(facing), facing));
		}
		syncConnections = true;
	}

	@Override
	public void update() {
		//if (world.isRemote && clogged && isAnySideUnclogged())
		//	Misc.spawnClogParticles(world, pos, 1, 0.25f);
		if (!world.isRemote) {
			active = getWorld().isBlockPowered(getPos());
			if(clogged || !active) {
				currentPush = INIT_PUSH;
			} else {
				currentPush += INCREMENT_PUSH;
			}
			for (EnumFacing facing : EnumFacing.VALUES) {
				if (!isConnected(facing))
					continue;
				TileEntity tile = world.getTileEntity(pos.offset(facing));
				if (tile != null && tile.hasCapability(EmbersCapabilities.EMBER_CAPABILITY, facing.getOpposite())) {
					if (active) {
						IEmberCapability handler = tile.getCapability(EmbersCapabilities.EMBER_CAPABILITY, facing.getOpposite());
						if (handler.getEmber() > 0 && packet == 0){
                            packet = handler.removeAmount(Math.min(currentPush, handler.getEmberCapacity() / 10), true);
							syncPacket = true;
						}
						setFrom(facing, true);
					} else {
						setFrom(facing, false);
					}
				}
			}
		}
		super.update();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		writeConnections(tag);
		return tag;
	}

	private void writeConnections(NBTTagCompound tag) {
		tag.setInteger("up", getInternalConnection(EnumFacing.UP).getIndex());
		tag.setInteger("down", getInternalConnection(EnumFacing.DOWN).getIndex());
		tag.setInteger("north", getInternalConnection(EnumFacing.NORTH).getIndex());
		tag.setInteger("south", getInternalConnection(EnumFacing.SOUTH).getIndex());
		tag.setInteger("west", getInternalConnection(EnumFacing.WEST).getIndex());
		tag.setInteger("east", getInternalConnection(EnumFacing.EAST).getIndex());
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if (tag.hasKey("up"))
			setInternalConnection(EnumFacing.UP, EnumPipeConnection.fromIndex(tag.getInteger("up")));
		if (tag.hasKey("down"))
			setInternalConnection(EnumFacing.DOWN, EnumPipeConnection.fromIndex(tag.getInteger("down")));
		if (tag.hasKey("north"))
			setInternalConnection(EnumFacing.NORTH, EnumPipeConnection.fromIndex(tag.getInteger("north")));
		if (tag.hasKey("south"))
			setInternalConnection(EnumFacing.SOUTH, EnumPipeConnection.fromIndex(tag.getInteger("south")));
		if (tag.hasKey("west"))
			setInternalConnection(EnumFacing.WEST, EnumPipeConnection.fromIndex(tag.getInteger("west")));
		if (tag.hasKey("east"))
			setInternalConnection(EnumFacing.EAST, EnumPipeConnection.fromIndex(tag.getInteger("east")));
	}

	@Override
	public NBTTagCompound getSyncTag() {
		NBTTagCompound compound = super.getUpdateTag();
		if (syncConnections)
			writeConnections(compound);
		return compound;
	}

	@Override
	protected boolean requiresSync() {
		return syncConnections || super.requiresSync();
	}

	@Override
	protected void resetSync() {
		super.resetSync();
		syncConnections = false;
	}

	@Override
	int getCapacity() {
		return 240;
	}

	public EnumPipeConnection getConnection(EnumFacing side) {
		if (getInternalConnection(side) == EnumPipeConnection.FORCENONE)
			return EnumPipeConnection.NEIGHBORNONE;
		return EnumPipeConnection.PIPE;
	}

	@Override
	public EnumPipeConnection getInternalConnection(EnumFacing facing) {
		return connections[facing.getIndex()] != null ? connections[facing.getIndex()] : EnumPipeConnection.NONE;
	}

	@Override
	void setInternalConnection(EnumFacing facing, EnumPipeConnection connection) {
		connections[facing.getIndex()] = connection;
	}

	@Override
	boolean isConnected(EnumFacing facing) {
		return getInternalConnection(facing).canTransfer();
	}

	public EnumPipeConnection getConnection(IBlockAccess world, BlockPos pos, EnumFacing side) {
		TileEntity tile = world.getTileEntity(pos);
		IEmberCapability cap = tile != null ? tile.getCapability(EmbersCapabilities.EMBER_CAPABILITY, side.getOpposite()) : null;
		if (getInternalConnection(side) == EnumPipeConnection.FORCENONE) {
			return EnumPipeConnection.FORCENONE;
		} else if (tile instanceof IEmberPipeConnectable) {
			return ((IEmberPipeConnectable) tile).getConnection(side.getOpposite());
		} else if (cap != null && cap.acceptsVolatile()) {
			return EnumPipeConnection.BLOCK;
		} else if (Misc.isValidPipeConnector(world,pos,side)) {
			return EnumPipeConnection.LEVER;
		} else if (Misc.isValidLever(world, pos, side)) {
			return EnumPipeConnection.LEVER;
		}
		return EnumPipeConnection.NONE;
	}

	public void reverseConnection(EnumFacing face) {
		EnumPipeConnection connection = getInternalConnection(face);
		setInternalConnection(face, reverseForce(connection));
		TileEntity tile = world.getTileEntity(pos.offset(face));
		if(tile instanceof TileEntityEmberPipe)
			((TileEntityEmberPipe) tile).updateNeighbors(world);
		if (connection == EnumPipeConnection.FORCENONE) {
			world.playSound(null, pos, SoundManager.PIPE_CONNECT, SoundCategory.BLOCKS, 1.0f, 1.0f);
		} else if (connection != EnumPipeConnection.NONE && connection != EnumPipeConnection.LEVER) {
			world.playSound(null, pos, SoundManager.PIPE_DISCONNECT, SoundCategory.BLOCKS, 1.0f, 1.0f);
		}
	}

	public static EnumPipeConnection reverseForce(EnumPipeConnection connect) {
		if (connect == EnumPipeConnection.FORCENONE) {
			return EnumPipeConnection.NONE;
		}
		else if (connect != EnumPipeConnection.NONE && connect != EnumPipeConnection.LEVER) {
			return EnumPipeConnection.FORCENONE;
		}
		return EnumPipeConnection.NONE;
	}

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
							EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemTinkerHammer) {
			if (side == EnumFacing.UP || side == EnumFacing.DOWN) {
				if (Math.abs(hitX - 0.5) > Math.abs(hitZ - 0.5)) {
					if (hitX < 0.5) {
						this.reverseConnection(EnumFacing.WEST);
					} else {
						this.reverseConnection(EnumFacing.EAST);
					}
				} else {
					if (hitZ < 0.5) {
						this.reverseConnection(EnumFacing.NORTH);
					} else {
						this.reverseConnection(EnumFacing.SOUTH);
					}
				}
			}
			if (side == EnumFacing.EAST || side == EnumFacing.WEST) {
				if (Math.abs(hitY - 0.5) > Math.abs(hitZ - 0.5)) {
					if (hitY < 0.5) {
						this.reverseConnection(EnumFacing.DOWN);
					} else {
						this.reverseConnection(EnumFacing.UP);
					}
				} else {
					if (hitZ < 0.5) {
						this.reverseConnection(EnumFacing.NORTH);
					} else {
						this.reverseConnection(EnumFacing.SOUTH);
					}
				}
			}
			if (side == EnumFacing.NORTH || side == EnumFacing.SOUTH) {
				if (Math.abs(hitX - 0.5) > Math.abs(hitY - 0.5)) {
					if (hitX < 0.5) {
						this.reverseConnection(EnumFacing.WEST);
					} else {
						this.reverseConnection(EnumFacing.EAST);
					}
				} else {
					if (hitY < 0.5) {
						this.reverseConnection(EnumFacing.DOWN);
					} else {
						this.reverseConnection(EnumFacing.UP);
					}
				}
			}
			updateNeighbors(world);
			return true;
		}
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		world.setTileEntity(pos, null);
	}

	@Override
	public void markDirty() {
		super.markDirty();
		Misc.syncTE(this);
	}
}
