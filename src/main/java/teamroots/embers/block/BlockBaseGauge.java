package teamroots.embers.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.api.tile.IExtraDialInformation;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdateRequest;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockBaseGauge extends BlockBase implements teamroots.embers.api.block.IDial {
    public static final PropertyDirection facing = PropertyDirection.create("facing");

    public BlockBaseGauge(Material material, String name, boolean addToTab) {
        super(material, name, addToTab);
    }

    public BlockBaseGauge(Material material) {
        super(material);
    }

    @Override
    public BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state){
        return state.getValue(facing).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        return getDefaultState().withProperty(facing, EnumFacing.getFront(meta));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing face, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        return getDefaultState().withProperty(facing, face);
    }

    @Override
    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side){
        return false;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos){
        if (world.isAirBlock(pos.offset(state.getValue(facing),-1))){
            world.setBlockToAir(pos);
            this.dropBlockAsItem(world, pos, state, 0);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
        switch (state.getValue(facing)){
            case UP:
                return new AxisAlignedBB(0.3125,0,0.3125,0.6875,0.125,0.6875);
            case DOWN:
                return new AxisAlignedBB(0.3125,0.875,0.3125,0.6875,1.0,0.6875);
            case NORTH:
                return new AxisAlignedBB(0.3125,0.3125,0.875,0.6875,0.6875,1.0);
            case SOUTH:
                return new AxisAlignedBB(0.3125,0.3125,0,0.6875,0.6875,0.125);
            case WEST:
                return new AxisAlignedBB(0.875,0.3125,0.3125,1.0,0.6875,0.6875);
            case EAST:
                return new AxisAlignedBB(0.0,0.3125,0.3125,0.125,0.6875,0.6875);
        }
        return new AxisAlignedBB(0.25,0,0.25,0.75,0.125,0.75);
    }

    @Override
    public List<String> getDisplayInfo(World world, BlockPos pos, IBlockState state) {
        ArrayList<String> text = new ArrayList<>();
        EnumFacing facing = state.getValue(BlockBaseGauge.facing);
        TileEntity tileEntity = world.getTileEntity(pos.offset(facing.getOpposite()));
        if (tileEntity != null){
            getTEData(facing, text, tileEntity);
            if(tileEntity instanceof IExtraDialInformation)
                ((IExtraDialInformation) tileEntity).addDialInformation(facing,text,getDialType());
        }
        return text;
    }

    protected abstract void getTEData(EnumFacing facing, ArrayList<String> text, TileEntity tileEntity);

    @Override
    public void updateTEData(World world, IBlockState state, BlockPos pos) {
        BlockPos tilePos = pos.offset(state.getValue(facing).getOpposite());
        TileEntity tile = world.getTileEntity(tilePos);
        if (tile != null){
            PacketHandler.INSTANCE.sendToServer(new MessageTEUpdateRequest(tilePos));
        }
    }
}
