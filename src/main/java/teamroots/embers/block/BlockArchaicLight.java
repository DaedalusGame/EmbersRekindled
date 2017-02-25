package teamroots.embers.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.RegistryManager;
import teamroots.embers.entity.EntityAncientGolem;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.tileentity.ITileEntityBase;
import teamroots.embers.tileentity.TileEntityActivatorBottom;
import teamroots.embers.tileentity.TileEntityActivatorTop;

public class BlockArchaicLight extends BlockBase {
	
	public BlockArchaicLight(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
		this.needsRandomTick = true;
	}
	
	@Override
	public int tickRate(World world){
		return 1;
	}
	
	@Override
	public boolean requiresUpdates(){
		return true;
	}
	
	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random){
		for (int i = 0; i < 12; i ++){
			int chance = random.nextInt(3);
			if (chance == 0){
				ParticleUtil.spawnParticleGlow(world, pos.getX()-0.03125f+1.0625f*random.nextInt(2), pos.getY()+0.125f+0.75f*random.nextFloat(), pos.getZ()+0.125f+0.75f*random.nextFloat(), (random.nextFloat()-0.5f)*0.003f, (random.nextFloat())*0.003f, (random.nextFloat()-0.5f)*0.003f, 255, 64, 16, 2.5f, 120);
			}
			if (chance == 1){
				ParticleUtil.spawnParticleGlow(world, pos.getX()+0.125f+0.75f*random.nextFloat(), pos.getY()-0.03125f+1.0625f*random.nextInt(2), pos.getZ()+0.125f+0.75f*random.nextFloat(), (random.nextFloat()-0.5f)*0.003f, (random.nextFloat())*0.003f, (random.nextFloat()-0.5f)*0.003f, 255, 64, 16, 2.5f, 120);
			}
			if (chance == 2){
				ParticleUtil.spawnParticleGlow(world, pos.getX()+0.125f+0.75f*random.nextFloat(), pos.getY()+0.125f+0.75f*random.nextFloat(), pos.getZ()-0.03125f+1.0625f*random.nextInt(2), (random.nextFloat()-0.5f)*0.003f, (random.nextFloat())*0.003f, (random.nextFloat()-0.5f)*0.003f, 255, 64, 16, 2.5f, 120);
			}
		}
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		if (world.getBlockState(pos.down()).getBlock() == RegistryManager.archaic_bricks && world.getBlockState(pos.down(2)).getBlock() == RegistryManager.archaic_bricks){
			if (!world.isRemote){
				EntityAncientGolem golem = new EntityAncientGolem(world);
				golem.onInitialSpawn(world.getDifficultyForLocation(pos), null);
				golem.setPosition(pos.getX()+0.5, pos.getY()-1.0, pos.getZ()+0.5);
				world.spawnEntity(golem);
			}
			world.destroyBlock(pos, false);
			world.destroyBlock(pos.down(), false);
			world.destroyBlock(pos.down(2), false);
		}
	}
}
