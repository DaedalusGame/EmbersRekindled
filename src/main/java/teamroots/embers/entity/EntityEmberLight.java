package teamroots.embers.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.embers.RegistryManager;
import teamroots.embers.particle.ParticleUtil;

public class EntityEmberLight extends Entity {
    BlockPos pos = new BlockPos(0,0,0);
	int lifetime = 160;
	public BlockPos dest = new BlockPos(0,0,0);
	
	public EntityEmberLight(World worldIn) {
		super(worldIn);
		this.setInvisible(true);
	}
	
	public void initCustom(double x, double y, double z, double vx, double vy, double vz){
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.motionX = vx;
		this.motionY = vy;
		this.motionZ = vz;
	}

	@Override
	protected void entityInit() {
	}
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		lifetime --;
		if (lifetime <= 0){
			getEntityWorld().removeEntity(this);
			this.setDead();
		}
		this.motionY -= 0.05f;
		
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		IBlockState state = getEntityWorld().getBlockState(getPosition());
		if (state.isFullCube() && state.isOpaqueCube() && !getEntityWorld().isRemote){
			EnumFacing face = EnumFacing.UP;
			boolean didHit = false;
			double blockX = getPosition().getX()+0.5;
			double blockY = getPosition().getY()+0.5;
			double blockZ = getPosition().getZ()+0.5;
			if (Math.abs(posX-blockX) > Math.abs(posZ-blockZ) && Math.abs(posX-blockX) > Math.abs(posY-blockY)){
				if (posX-blockX > 0){
					face = EnumFacing.WEST;
				}
				else {
					face = EnumFacing.EAST;
				}
			}
			else if (Math.abs(posY-blockY) > Math.abs(posZ-blockZ) && Math.abs(posY-blockY) > Math.abs(posX-blockX)){
				if (posY-blockY > 0){
					face = EnumFacing.DOWN;
				}
				else {
					face = EnumFacing.UP;
				}
			}
			else if (Math.abs(posZ-blockZ) > Math.abs(posX-blockX) && Math.abs(posZ-blockZ) > Math.abs(posY-blockY)){
				if (posZ-blockZ > 0){
					face = EnumFacing.NORTH;
				}
				else {
					face = EnumFacing.SOUTH;
				}
			}
			if (getEntityWorld().isAirBlock(getPosition().offset(face)) || getEntityWorld().getBlockState(getPosition().offset(face)).getBlock().isReplaceable(getEntityWorld(), getPosition().offset(face))){
				getEntityWorld().setBlockState(getPosition().offset(face), RegistryManager.glow.getDefaultState());
				getEntityWorld().notifyBlockUpdate(getPosition().offset(face), Blocks.AIR.getDefaultState(), RegistryManager.glow.getDefaultState(), 8);
				didHit = true;
			}
			if (!didHit){
				if (posX-blockX > 0){
					face = EnumFacing.WEST;
				}
				else {
					face = EnumFacing.EAST;
				}
				if (getEntityWorld().isAirBlock(getPosition().offset(face)) || getEntityWorld().getBlockState(getPosition().offset(face)).getBlock().isReplaceable(getEntityWorld(), getPosition().offset(face))){
					getEntityWorld().setBlockState(getPosition().offset(face), RegistryManager.glow.getDefaultState());
					getEntityWorld().notifyBlockUpdate(getPosition().offset(face), Blocks.AIR.getDefaultState(), RegistryManager.glow.getDefaultState(), 8);
					didHit = true;
				}
			}
			if (!didHit){
				if (posY-blockY > 0){
					face = EnumFacing.DOWN;
				}
				else {
					face = EnumFacing.UP;
				}
				if (getEntityWorld().isAirBlock(getPosition().offset(face)) || getEntityWorld().getBlockState(getPosition().offset(face)).getBlock().isReplaceable(getEntityWorld(), getPosition().offset(face))){
					getEntityWorld().setBlockState(getPosition().offset(face), RegistryManager.glow.getDefaultState());
					getEntityWorld().notifyBlockUpdate(getPosition().offset(face), Blocks.AIR.getDefaultState(), RegistryManager.glow.getDefaultState(), 8);
					didHit = true;
				}
			}
			if (!didHit){
				if (posZ-blockZ > 0){
					face = EnumFacing.NORTH;
				}
				else {
					face = EnumFacing.SOUTH;
				}
				if (getEntityWorld().isAirBlock(getPosition().offset(face)) || getEntityWorld().getBlockState(getPosition().offset(face)).getBlock().isReplaceable(getEntityWorld(), getPosition().offset(face))){
					getEntityWorld().setBlockState(getPosition().offset(face), RegistryManager.glow.getDefaultState());
					getEntityWorld().notifyBlockUpdate(getPosition().offset(face), Blocks.AIR.getDefaultState(), RegistryManager.glow.getDefaultState(), 8);
					didHit = true;
				}
			}
			this.setDead();
		}
		if (getEntityWorld().isRemote){
			for (double i = 0; i < 9; i ++){
				double coeff = i/9.0;
				ParticleUtil.spawnParticleGlow(getEntityWorld(), (float)(prevPosX+(posX-prevPosX)*coeff), (float)(prevPosY+(posY-prevPosY)*coeff), (float)(prevPosZ+(posZ-prevPosZ)*coeff), 0.0125f*(rand.nextFloat()-0.5f), 0.0125f*(rand.nextFloat()-0.5f), 0.0125f*(rand.nextFloat()-0.5f), 255, 64, 16, 3.0f, 24);
			}
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		
	}
}
