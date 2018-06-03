package teamroots.embers.tileentity;

import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import teamroots.embers.EventManager;
import teamroots.embers.block.BlockBreaker;

public class TileEntityBreaker extends TileEntity implements ITileEntityBase, ITickable {
	int ticksExisted = 0;
	Random random = new Random();
	WeakReference<FakePlayer> fakePlayer;
	
	public TileEntityBreaker(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		return tag;
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
	public void onLoad() {
		if(!world.isRemote)
			initFakePlayer();
	}

	protected void initFakePlayer() {
		FakePlayer player = FakePlayerFactory.get((WorldServer) world, new GameProfile(new UUID(13, 13), "embers_breaker"));
		player.connection = new NetHandlerPlayServer(FMLCommonHandler.instance().getMinecraftServerInstance(), new NetworkManager(EnumPacketDirection.SERVERBOUND), player) {
			@Override
			public void sendPacket(Packet packetIn)
			{

			}
		};
		fakePlayer = new WeakReference<>(player);
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

	@Override
	public void update() {
		ticksExisted ++;
		IBlockState state = world.getBlockState(pos);
		if (ticksExisted % 20 == 0 && world.isBlockIndirectlyGettingPowered(getPos()) == 0 && state.getBlock() instanceof BlockBreaker && !world.isRemote){
			EnumFacing facing = getFacing();
			mineBlock(pos.offset(facing));
		}
	}

	protected void mineBlock(BlockPos breakPos) {

		FakePlayer player = fakePlayer.get();
		if(player == null)
			return;
		//player.interactionManager.tryHarvestBlock(breakPos);
		int exp = net.minecraftforge.common.ForgeHooks.onBlockBreakEvent(world, player.interactionManager.getGameType(), player, breakPos);
		if (exp != -1) {
			IBlockState state = world.getBlockState(breakPos);
			TileEntity tile = this.world.getTileEntity(breakPos);
			Block block = state.getBlock();

			NonNullList<ItemStack> drops = null;

			if ((block instanceof BlockCommandBlock || block instanceof BlockStructure) && !player.canUseCommandBlock())
			{
				world.notifyBlockUpdate(breakPos, state, state, 3);
			}
			else
			{
				world.playEvent(player, 2001, breakPos, Block.getStateId(state));
				boolean flag1;

				if (player.isCreative())
				{
					removeBlock(player,breakPos,false);
				}
				else
				{
					flag1 = removeBlock(player, breakPos, true);
					if (flag1) {
						EventManager.captureDrops(true);
						state.getBlock().harvestBlock(world, player, breakPos, state, tile, ItemStack.EMPTY);
						drops = EventManager.captureDrops(false);
					}
				}
			}

			if(drops != null)
				collectDrops(drops);
		}
	}

	public EnumFacing getFacing() {
		IBlockState state = world.getBlockState(pos);
		return state.getValue(BlockBreaker.facing);
	}

	private void collectDrops(NonNullList<ItemStack> stacks) {
		EnumFacing facing = getFacing();
		BlockPos frontPos = getPos().offset(facing);
		BlockPos binPos = getPos().offset(facing.getOpposite());
		boolean capture = getWorld().getTileEntity(binPos) instanceof TileEntityBin;
		for (ItemStack stack : stacks){
			if (capture){
				TileEntityBin bin = (TileEntityBin)getWorld().getTileEntity(binPos);
				ItemStack remainder = bin.inventory.insertItem(0, stack, false);
				if (!remainder.isEmpty() && !getWorld().isRemote){
					EntityItem item = new EntityItem(getWorld(), frontPos.getX()+0.5, frontPos.getY()+1.0625f, frontPos.getZ()+0.5,remainder);
					getWorld().spawnEntity(item);
				}
				bin.markDirty();
				markDirty();
			}
			else {
				EntityItem item = new EntityItem(getWorld(), frontPos.getX()+0.5, frontPos.getY()+1.0625f, frontPos.getZ()+0.5,stack);
				getWorld().spawnEntity(item);
			}
		}
	}

	private boolean removeBlock(FakePlayer player, BlockPos pos, boolean canHarvest) {
		IBlockState state = this.world.getBlockState(pos);
		boolean flag = state.getBlock().removedByPlayer(state, world, pos, player, canHarvest);
		if (flag) {
			state.getBlock().onBlockDestroyedByPlayer(this.world, pos, state);
		}
		return flag;
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
