package teamroots.embers.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import com.jcraft.jorbis.Block;
import com.mojang.authlib.GameProfile;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
import net.minecraft.world.GameType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.block.BlockBreaker;
import teamroots.embers.block.BlockItemTransfer;
import teamroots.embers.block.BlockVacuum;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.util.Misc;

public class TileEntityBreaker extends TileEntity implements ITileEntityBase, ITickable {
	int ticksExisted = 0;
	Random random = new Random();
	
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
		if (ticksExisted % 20 == 0 && world.isBlockIndirectlyGettingPowered(getPos()) == 0){
			IBlockState state = world.getBlockState(getPos());
			IBlockState target = world.getBlockState(pos.offset(state.getValue(BlockBreaker.facing)));
			if (!(target.getBlock() instanceof BlockLiquid) && target.getBlockHardness(getWorld(), getPos().offset(state.getValue(BlockBreaker.facing))) != -1){
				List<ItemStack> drops = target.getBlock().getDrops(world, getPos().offset(state.getValue(BlockBreaker.facing)), target, 0);
				if (!world.isRemote){
					//world.getBlockState(getPos().offset(state.getValue(BlockBreaker.facing))).getBlock().onBlockHarvested(world, getPos().offset(state.getValue(BlockBreaker.facing)), world.getBlockState(getPos().offset(state.getValue(BlockBreaker.facing))), null);
					FakePlayer p = new FakePlayer((WorldServer) world, new GameProfile(new UUID(13,13), "embers_breaker"));
					target.getBlock().onBlockHarvested(world, pos.offset(state.getValue(BlockBreaker.facing)), target, p);
					world.destroyBlock(getPos().offset(state.getValue(BlockBreaker.facing)), false);
					world.notifyBlockUpdate(getPos().offset(state.getValue(BlockBreaker.facing)), state, Blocks.AIR.getDefaultState(), 8);
				}
				for (ItemStack i : drops){
					BlockPos binPos = getPos().offset(state.getValue(BlockBreaker.facing),-1);
					if (getWorld().getTileEntity(binPos) instanceof TileEntityBin){
						TileEntityBin bin = (TileEntityBin)getWorld().getTileEntity(binPos);
						ItemStack remainder = bin.inventory.insertItem(0, i, false);
						if (remainder != ItemStack.EMPTY && !getWorld().isRemote){
							EntityItem item = new EntityItem(getWorld(),getPos().offset(state.getValue(BlockBreaker.facing)).getX()+0.5,getPos().offset(state.getValue(BlockBreaker.facing)).getY()+1.0625f,getPos().offset(state.getValue(BlockBreaker.facing)).getZ()+0.5,remainder);
							getWorld().spawnEntity(item);
						}
						bin.markDirty();
						markDirty();
					}
					else if (!world.isRemote){
						EntityItem item = new EntityItem(getWorld(),getPos().offset(state.getValue(BlockBreaker.facing)).getX()+0.5,getPos().offset(state.getValue(BlockBreaker.facing)).getY()+1.0625f,getPos().offset(state.getValue(BlockBreaker.facing)).getZ()+0.5,i);
						getWorld().spawnEntity(item);
					}
				}
				if (!world.isRemote && world instanceof WorldServer){
					ForgeHooks.onBlockBreakEvent(world, GameType.SURVIVAL, FakePlayerFactory.getMinecraft((WorldServer)world), getPos());
				}
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
}
