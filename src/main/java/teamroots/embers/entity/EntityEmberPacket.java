package teamroots.embers.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.power.EmberCapabilityProvider;
import teamroots.embers.power.IEmberPacketReceiver;

public class EntityEmberPacket extends Entity {

	BlockPos pos = new BlockPos(0,0,0);
	BlockPos dest = new BlockPos(0,0,0);
	double value = 0;
	int lifetime = 80;
	
	public EntityEmberPacket(World worldIn) {
		super(worldIn);
		setSize(0,0);
		this.setInvisible(true);
	}
	
	public void initCustom(BlockPos pos, BlockPos dest, double vx, double vy, double vz, double value){
		this.posX = pos.getX()+0.5;
		this.posY = pos.getY()+0.5;
		this.posZ = pos.getZ()+0.5;
		this.motionX = vx;
		this.motionY = vy;
		this.motionZ = vz;
		this.dest = dest;
		this.pos = pos;
		this.value = value;
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("destX")){
			dest = new BlockPos(compound.getInteger("destX"),compound.getInteger("destY"),compound.getInteger("destZ"));
			setPosition(compound.getDouble("x"),compound.getDouble("y"),compound.getDouble("z"));
			value = compound.getDouble("value");
			motionX = compound.getDouble("vx");
			motionY = compound.getDouble("vy");
			motionZ = compound.getDouble("vz");
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("destX",dest.getX());
		compound.setInteger("destY",dest.getY());
		compound.setInteger("destZ",dest.getZ());
		compound.setDouble("x", posX);
		compound.setDouble("y", posY);
		compound.setDouble("z", posZ);
		compound.setDouble("value", value);
		compound.setDouble("vx", motionX);
		compound.setDouble("vy", motionY);
		compound.setDouble("vz", motionZ);
	}
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		
		lifetime --;
		if (lifetime <= 0){
			getEntityWorld().removeEntity(this);
			this.kill();
		}
		if (dest.getX() != 0 || dest.getY() != 0 || dest.getZ() != 0){
			double targetX = dest.getX()+0.5;
			double targetY = dest.getY()+0.5;
			double targetZ = dest.getZ()+0.5;
			Vec3d targetVector = new Vec3d(targetX-posX,targetY-posY,targetZ-posZ);
			double length = targetVector.lengthVector();
			targetVector = targetVector.normalize().scale(0.3);
			double weight  = 0;
			if (length <= 3){
				weight = 0.9*((3.0-length)/3.0);
			}
			motionX = (0.9-weight)*motionX+(0.1+weight)*targetVector.xCoord;
			motionY = (0.9-weight)*motionY+(0.1+weight)*targetVector.yCoord;
			motionZ = (0.9-weight)*motionZ+(0.1+weight)*targetVector.zCoord;
		}
		
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		IBlockState state = getEntityWorld().getBlockState(getPosition());
		TileEntity tile = getEntityWorld().getTileEntity(getPosition());
		affectTileEntity(state,tile);
		if (state.isFullCube() && state.isOpaqueCube()){
			getEntityWorld().removeEntity(this);
			this.kill();
		}
		if (getEntityWorld().isRemote){
			for (double i = 0; i < 9; i ++){
				double coeff = i/9.0;
				ParticleUtil.spawnParticleGlow(getEntityWorld(), (float)(prevPosX+(posX-prevPosX)*coeff), (float)(prevPosY+(posY-prevPosY)*coeff), (float)(prevPosZ+(posZ-prevPosZ)*coeff), 0.0125f*(rand.nextFloat()-0.5f), 0.0125f*(rand.nextFloat()-0.5f), 0.0125f*(rand.nextFloat()-0.5f), 255, 64, 16);
			}
		}
	}
	
	public void affectTileEntity(IBlockState state, TileEntity tile){
		if (tile instanceof IEmberPacketReceiver){
			if (tile.hasCapability(EmberCapabilityProvider.emberCapability, null)){
				tile.getCapability(EmberCapabilityProvider.emberCapability, null).addAmount(value, true);
				tile.markDirty();
				tile.getWorld().notifyBlockUpdate(tile.getPos(), state, state, 3);
				getEntityWorld().removeEntity(this);
				this.kill();
			}
		}
	}

}
