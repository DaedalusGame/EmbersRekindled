package teamroots.embers.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdateRequest;
import teamroots.embers.util.Misc;

import java.util.ArrayList;
import java.util.List;

public class BlockFluidGauge extends BlockBase implements IDial {
	public static final PropertyDirection facing = PropertyDirection.create("facing");
	
	public BlockFluidGauge(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
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
		return getDefaultState().withProperty(facing,EnumFacing.getFront(meta));
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
		ArrayList<String> text = new ArrayList<String>();
		if (world.getTileEntity(pos.offset(Misc.getOppositeFace(state.getValue(facing)))) != null){
			if (world.getTileEntity(pos.offset(Misc.getOppositeFace(state.getValue(facing)))).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Misc.getOppositeFace(state.getValue(facing)))){
				IFluidHandler handler = world.getTileEntity(pos.offset(Misc.getOppositeFace(state.getValue(facing)))).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Misc.getOppositeFace(state.getValue(facing)));
				if (handler != null){
					IFluidTankProperties[] properties = handler.getTankProperties();
					for (int i = 0; i < properties.length; i ++){
						if (properties[i].getContents() != null){
							text.add(""+properties[i].getContents().getFluid().getLocalizedName(properties[i].getContents())+": "+properties[i].getContents().amount+"/"+properties[i].getCapacity());
						}
					}
				}
			}
		}
		return text;
	}

	@Override
	public void updateTEData(World world, IBlockState state, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos.offset(state.getValue(this.facing)));
		if (tile != null){
			PacketHandler.INSTANCE.sendToServer(new MessageTEUpdateRequest(pos));
		}
	}
}
