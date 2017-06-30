package teamroots.embers.tileentity;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockLever;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import teamroots.embers.EventManager;
import teamroots.embers.RegistryManager;
import teamroots.embers.block.BlockBeamCannon;
import teamroots.embers.block.BlockEmberEmitter;
import teamroots.embers.entity.EntityEmberPacket;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageBeamCannonFX;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.DefaultEmberCapability;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberCapability;
import teamroots.embers.power.IEmberPacketProducer;
import teamroots.embers.power.IEmberPacketReceiver;

public class TileEntityBeamCannon extends TileEntity implements ITileEntityBase, ITickable, ITargetable {
	public IEmberCapability capability = new DefaultEmberCapability();
	public BlockPos target = null;
	public long ticksExisted = 0;
	public Random random = new Random();
	int offset = random.nextInt(40);
	
	public TileEntityBeamCannon(){
		super();
		this.onLoad();
		capability.setEmberCapacity(2000);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		if (target != null){
			tag.setInteger("targetX", target.getX());
			tag.setInteger("targetY", target.getY());
			tag.setInteger("targetZ", target.getZ());
		}
		capability.writeToNBT(tag);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		if (tag.hasKey("targetX")){
			target = new BlockPos(tag.getInteger("targetX"), tag.getInteger("targetY"), tag.getInteger("targetZ"));
		}
		capability.readFromNBT(tag);
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
	public void update() {
		if (this.target == null && this.ticksExisted == 0){
			IBlockState state = getWorld().getBlockState(getPos());
			this.target = getPos().offset(state.getValue(BlockBeamCannon.facing));
		}
		ticksExisted ++;
		if (this.capability.getEmber() >= 400 && this.ticksExisted % 200 == 0 && getWorld().isBlockIndirectlyGettingPowered(getPos()) != 0){
			Vec3d ray = (new Vec3d(target.getX()-getPos().getX(),target.getY()-getPos().getY(),target.getZ()-getPos().getZ())).normalize();
			if (!getWorld().isRemote){
				double posX = getPos().getX()+0.5;
				double posY = getPos().getY()+0.5;
				double posZ = getPos().getZ()+0.5;
				boolean doContinue = true;
				for (int i = 0; i < 640 && doContinue; i ++){
					posX += ray.xCoord*0.1;
					posY += ray.yCoord*0.1;
					posZ += ray.zCoord*0.1;
					IBlockState state = getWorld().getBlockState(new BlockPos(posX,posY,posZ));
					TileEntity tile = getWorld().getTileEntity(new BlockPos(posX,posY,posZ));
					if (tile instanceof TileEntityAlchemyTablet){
						((TileEntityAlchemyTablet)tile).sparkProgress();
						doContinue = false;
					}
					if (tile instanceof IEmberPacketReceiver){
						if (tile.hasCapability(EmberCapabilityProvider.emberCapability, null)){
							tile.getCapability(EmberCapabilityProvider.emberCapability, null).addAmount(capability.getEmber(), true);
							tile.markDirty();
						}
						doContinue = false;
					}
					if (state.isFullCube() && state.isOpaqueCube()){
						doContinue = false;
					}
					List<EntityLivingBase> rawEntities = getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX-0.85,posY-0.85,posZ-0.85,posX+0.85,posY+0.85,posZ+0.85));
					for (int j = 0; j < rawEntities.size(); j ++){
						rawEntities.get(j).attackEntityFrom(RegistryManager.damage_ember, 25.0f);
					}
				}
				this.capability.setEmber(0);
				markDirty();	
				PacketHandler.INSTANCE.sendToAll(new MessageBeamCannonFX(this));
			}
		}
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		if (capability == EmberCapabilityProvider.emberCapability){
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == EmberCapabilityProvider.emberCapability){
			return (T)this.capability;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void setTarget(BlockPos pos) {
		this.target = pos;
		markDirty();
	}
}
