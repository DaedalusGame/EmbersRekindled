package teamroots.embers.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.particle.ParticleUtil;

import java.util.Random;

public class BlockLantern extends BlockBase {
	public static AxisAlignedBB AABB_BASE = new AxisAlignedBB(0.25,0,0.25,0.75,13.0/16.0,0.75);
	
	public BlockLantern(Material material, String name, boolean addToTab) {
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
		for (int i = 0; i < 3; i ++){
			ParticleUtil.spawnParticleGlow(world, pos.getX()+0.5f, pos.getY()+0.375f, pos.getZ()+0.5f, (random.nextFloat()-0.5f)*0.003f, (random.nextFloat())*0.003f, (random.nextFloat()-0.5f)*0.003f, 255, 64, 16, 2.5f, 120);
		}
	}

    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return AABB_BASE;
    }
}
