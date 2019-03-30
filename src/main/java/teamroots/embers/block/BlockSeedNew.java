package teamroots.embers.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.tileentity.ITileEntityBase;
import teamroots.embers.tileentity.TileEntitySeedNew;

public abstract class BlockSeedNew extends BlockBase implements ITileEntityProvider {
    public static AxisAlignedBB AABB_BASE = new AxisAlignedBB(0.3125,0.0625,0.3125,0.6875,0.9375,0.6875);
    public BlockRenderLayer layer = BlockRenderLayer.SOLID;
    public BlockSeedNew(Material material, String name, boolean addToTab){
        super(material, name, addToTab);
        setIsFullCube(false);
        setIsOpaqueCube(false);
        setHarvestProperties("pickaxe", 0);
        setHardness(1.6f);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos){
        return AABB_BASE;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
        return ((ITileEntityBase)world.getTileEntity(pos)).activate(world,pos,state,player,hand,side,hitX,hitY,hitZ);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player){
        ((ITileEntityBase)world.getTileEntity(pos)).breakBlock(world,pos,state,player);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntitySeedNew();
    }

    public abstract ResourceLocation getTexture(TileEntitySeedNew tile);

    public abstract ItemStack[] getNuggetDrops(TileEntitySeedNew tile, int n);
}
